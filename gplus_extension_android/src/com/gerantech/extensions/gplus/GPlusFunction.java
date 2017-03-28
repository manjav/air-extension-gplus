package com.gerantech.extensions.gplus;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;

public class GPlusFunction implements FREFunction 
{

	private static final String TAG = "GPlus";

	@Override
	public FREObject call(FREContext context, FREObject[] args) 
	{
		Log.i(TAG, "GPlusFunction called");
		
		GPlusExtensionContext gpExtContext = (GPlusExtensionContext) context;
		Activity _activity = gpExtContext.getActivity();
		
		GPConnector.extensionContext = gpExtContext;
		try 
		{
			int _type = args[0].getAsInt();

			Log.i(TAG, _type+" GPlusFunction try");
			Intent intent = new Intent(_activity.getApplicationContext(), GPConnector.class);
			intent.putExtra("connectMode", _type);
			_activity.startActivity(intent);
			Log.i(TAG, _type+" startActivity");
		} 
		catch (Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
