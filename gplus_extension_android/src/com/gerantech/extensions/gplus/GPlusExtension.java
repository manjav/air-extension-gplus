package com.gerantech.extensions.gplus;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;




public class GPlusExtension implements FREExtension
{
    /** Called when the activity is first created. */
	@Override
	public FREContext createContext(String arg0)
	{
		// TODO Auto-generated method stub
		Log.e("A.N.E", "Inside Create Context");
		return new GPlusExtensionContext();
	}

	@Override
	public void dispose() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize()
	{
		// TODO Auto-generated method stub
	}
}