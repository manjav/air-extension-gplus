# About
Adobe AIR native extension (ANE) for Android to connect to Google+ .<br />

It uses Google Plus API (com.google.android.gms.common.api.GoogleApiClient).<br />
Supported functionality:<br />
- user info (Google+ ID, name, Google+ url, email, photo url, birthdate, gender)<br />
- friends info (Google+ ID, name, Google+ url, photo url, gender)<br />

# Docs
Please, read docs and try ANE before asking any questions.<br />
http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1<br />
http://help.adobe.com/en_US/air/extensions/index.html<br />
https://developers.google.com/identity/sign-in/android/start<br />


# Installation
Extension ID: com.pozirk.AndroidInAppPurchase<br />
Add "gplus-extension.ane" from package folder to ane folder in your AIR project.<br />
Add the following lines to your AIR Aplication-app.xml file inside &lt;manifestAdditions&gt; section:<br />
<br />

&lt;application android:enabled="true"&gt;<br />
	&lt;meta-data android:name="com.google.android.gms.version" android:value="4452000" /&gt;<br />
	&lt;activity android:name="com.gerantech.extensions.gplus.GPConnector" android:theme="@android:style/Theme.Translucent.NoTitleBar" /&gt;<br />
&lt;/application&gt;<br />

&lt;uses-permission android:name="android.permission.INTERNET" /&gt;<br />
&lt;uses-permission android:name="android.permission.GET_ACCOUNTS" /&gt;<br />
&lt;uses-permission android:name="android.permission.USE_CREDENTIALS" /&gt;<br />
&lt;uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /&gt;<br />
&lt;uses-permission android:name="android.permission.READ_PHONE_STATE" /&gt;<br />


# Examples
```actionscript
import com.gerantech.extensions.gplus.GPlusExtension;
import com.gerantech.extensions.gplus.events.GPlusEvent;
import com.gerantech.extensions.gplus.models.Result;

...

private var extension:GPlusExtension;
...

//> initialization of Google Plus API
extension = GPlusExtension.instance;
extension.addEventListener(GPlusEvent.GPLUS_RESULT , extension_eventsHandler);

...

//> call this methods step by step after you dont received any error or failure message
extension.init();
...
extension.login();
...
extension.logout();
...
extension.revokeAccess();

protected function extension_eventsHandler(event:GPlusEvent):void
{
	switch(event.result.response)
	{
		case Result.CONNECTION_FAILED:
		case Result.LOGIN_CANCELED:
		case Result.LOGIN_FAILED:
		case Result.NETWORK_ERROR:
		case Result.PERSON_NULL:
		case Result.REVOKE_ACCESS:
			trace(event.result.response + "  " + event.result.success + "  " + event.result.message);
			break;
		case Result.PERSON_INFORMATION:
			trace(event.result.person.name + " " + event.result.person.email + " " + event.result.person.friends.length);
			break;
		
	}
	//trace(event.type, event.data);
}

```

# Misc
ANE is build for AIR 18.0+, in order to rebuild for another version do the following:<br />
- edit "air\extension.xml" and change 18 in very first line to any X.x you need;<br />
- edit "package.bat" and in the very last line change path from AIR 18.0 SDK to any AIR X.x SDK you need;<br />
- execute "package.bat" to repack the ANE.<br />



## Thanks to
[ravitamada](http://www.androidhive.info/2014/02/android-login-with-google-plus-account-1)
