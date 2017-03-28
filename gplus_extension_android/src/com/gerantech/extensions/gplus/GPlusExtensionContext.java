package com.gerantech.extensions.gplus;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

public class GPlusExtensionContext  extends FREContext
{

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		
		Log.e("GPlus", "Map function called");
		
		Map<String, FREFunction> functionMap=new HashMap<String, FREFunction>();
		
		functionMap.put("connect", new GPlusFunction());
		return functionMap;
	}
}
