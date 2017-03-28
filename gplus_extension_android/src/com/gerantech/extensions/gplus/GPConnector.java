package com.gerantech.extensions.gplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

public class GPConnector extends Activity implements ConnectionCallbacks, OnConnectionFailedListener , ResultCallback<People.LoadPeopleResult>
{
	private static final int CONNECT_MODE_INIT = 0;
	private static final int CONNECT_MODE_SIGIN = 1;
	private static final int CONNECT_MODE_SIGNOUT = 2;
	private static final int CONNECT_MODE_REVOKE = 3;
	
	private static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "GPlus";

	// Profile pic image size in pixels
	public Context mContext;
	public static FREContext extensionContext;

	/**
	 * @param mContext
	 * @param extensionContext
	 
	public GPConnector(Context mContext, FREContext extensionContext)
	{
		super();
		
		this.mContext = mContext;
		this.extensionContext = extensionContext;
		
		mGoogleApiClient = new GoogleApiClient.Builder(this.mContext)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		mGoogleApiClient.connect();
		Log.i(TAG, "__ onCreate" + " " + connectMode);
	}*/

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	private boolean mSignInClicked;

	private ConnectionResult mConnectionResult;
	private int connectMode = 0;
	private Human human;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		connectMode = getIntent().getExtras().getInt("connectMode");
		mContext = this;
		//Log.i(TAG, "__ onCreate" + " " + connectMode);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();
	}

	protected void onStart() 
	{
		//Log.i(TAG, "__ onStart ");
		mGoogleApiClient.connect();
		super.onStart();
	}

	protected void onStop() 
	{
		super.onStop();
		if (mGoogleApiClient.isConnected()) 
			mGoogleApiClient.disconnect();
	}

	/**
	 * Method to resolve any signin errors
	 * */
	public void resolveSignInError() 
	{
		//Log.i(TAG, "__ resolveSignInError ");
		if (mConnectionResult!=null && mConnectionResult.hasResolution()) 
		{
			try 
			{
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} 
			catch (SendIntentException e)
			{
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) 
	{
		//Log.i(TAG, "__ onConnectionFailed "+result.hasResolution()+"  mIntentInProgress:" + mIntentInProgress + " mSignInClicked:" + mSignInClicked + " connectMode:" + connectMode );
		if (!result.hasResolution()) 
		{
			GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
			return;
		}

		if (!mIntentInProgress) 
		{
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked)
			{
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
			else
			{
				if(connectMode!=CONNECT_MODE_SIGIN)
				{
					sendResult(new Result(Result.LOGIN_FAILED));
				}
				else if(connectMode==CONNECT_MODE_SIGIN)
				{
					connectMode = CONNECT_MODE_INIT;
					signInWithGplus();
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) 
	{
		//Log.i(TAG, "__ onActivityResult");
		if (requestCode == RC_SIGN_IN) 
		{
			if (responseCode != RESULT_OK)
				mSignInClicked = false;
			
			mIntentInProgress = false;
			
			if (!mGoogleApiClient.isConnecting())
				mGoogleApiClient.connect();
			
		}
	}

	@Override
	public void onConnected(Bundle arg0) 
	{
		//Log.i(TAG, "__ onConnected "+mGoogleApiClient.isConnected() + " " +connectMode);
		mSignInClicked = false;
		
		//Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		
		if(connectMode==CONNECT_MODE_SIGNOUT)
		{
			signOutFromGplus();
			return;
		}
		else if(connectMode==CONNECT_MODE_REVOKE)
		{
			revokeGplusAccess();
			return;
		}

		// Get user's information
		if(human==null)
			getProfileInformation();
	}
	
	/**
	 * Fetching user's information name, email, profile pic
	 * */
	private void getProfileInformation()
	{
		try
		{
			Person p = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
			if (p != null)
			{
				//Log.i(TAG, "__ getProfileInformation ");
				human = new Human(p.getId(), p.getDisplayName(), Plus.AccountApi.getAccountName(mGoogleApiClient), p.getUrl(), p.getImage().getUrl(), p.getBirthday(), String.valueOf(p.getGender()));

				//Log.i(TAG, "name: " + human.name + ", url: " + human.url + ", email: " + human.email + ", photoUrl: " + human.photoUrl + ", birtdate: " + human.birtdate + ", gender: " + human.gender);
				
				Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(this);
			} 
			else 
			{
				sendResult(new Result(Result.PERSON_NULL));
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onResult(LoadPeopleResult peopleData) 
	{
		//Log.i(TAG, "__ onResult");
		if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS)
		{
			PersonBuffer personBuffer = peopleData.getPersonBuffer();
			Person p;
			try 
			{
				int count = personBuffer.getCount();
				for (int i=0; i<count; i++)
				{
					p = personBuffer.get(i);
					human.addFriend(new Friend(p.getId(), p.getDisplayName(), p.getUrl(), p.getImage().getUrl(), String.valueOf(p.getGender())));
				}
				sendResult(new Result(Result.PERSON_INFORMATION, human));
			} 
			finally
			{
				personBuffer.close();
			}
		}
		else
		{
			Result res = new Result(Result.NETWORK_ERROR);
			res.message = "Error requesting visible circles: " + peopleData.getStatus();
			sendResult(res);
		}
	}


	@Override
	public void onConnectionSuspended(int arg0) 
	{
		Log.i(TAG, "__ onConnectionSuspended");
		mGoogleApiClient.connect();
		//MainActivity.instance().updateUI(false);
	}

	/**
	 * Sign-in into google
	 * */
	public void signInWithGplus()
	{
		Log.i(TAG, "__ signInWithGplus "+mGoogleApiClient.isConnecting());
		if (!mGoogleApiClient.isConnecting()) 
		{
			mSignInClicked = true;
			resolveSignInError();
		}
	}

	/**
	 * Sign-out from google
	 * */
	public void signOutFromGplus() 
	{
		if (mGoogleApiClient.isConnected()) 
		{
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			mGoogleApiClient.connect();
			//MainActivity.instance().updateUI(false);
		}
	}

	/**
	 * Revoking access from google
	 * */
	public void revokeGplusAccess() 
	{
		if (mGoogleApiClient.isConnected()) 
		{
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
					.setResultCallback(new ResultCallback<Status>() 
							{
						@Override
						public void onResult(Status arg0)
						{
							Log.e(TAG, "User access revoked!");
							mGoogleApiClient.connect();
							sendResult(new Result(Result.REVOKE_ACCESS));
						}
					});
		}
	}

	/**
	 * Utils _______________________________________________________
	 */
	
	private void sendResult(Result result) 
	{
		String ret = result.toJson();
        Log.d(TAG, ret);
		extensionContext.dispatchStatusEventAsync(Float.toString(result.response), ret);
		finish();
	}
	
	public static void launchClass(Activity parentActivity, int connectMode) 
	{
        Log.d(TAG, "__ Launching bridge activity: parent:" + parentActivity + " " + connectMode);
        Intent bridgeIntent = new Intent(parentActivity, GPConnector.class);
        bridgeIntent.putExtra("connectMode", connectMode);
        parentActivity.startActivity(bridgeIntent);
        Log.d(TAG, "__ Launched");
	}
	/*public void longInfo(String str) {
    if(str.length() > 4000) {
        Log.i(TAG, str.substring(0, 4000));
        longInfo(str.substring(4000));
    } else
        Log.i(TAG, str);
	}*/
}