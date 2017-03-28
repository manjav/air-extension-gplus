package com.gerantech.extensions.gplus;

import com.google.gson.Gson;


public class Result
{
	public static final int PERSON_INFORMATION = 101;
	public static final int REVOKE_ACCESS = 102;
	
	public static final int PERSON_NULL = 201;
	public static final int NETWORK_ERROR = 202;
	public static final int CONNECTION_FAILED = 203;
	public static final int LOGIN_CANCELED = 204;
	public static final int LOGIN_FAILED = 205;
	
	public Boolean success;
	public String message;
	public Object data;
	public int response; 
	
	public Result(int response) 
	{
		this.response = response;
		fillParams();
	}

	public Result(int response, Object data) 
	{
		this(response);
		this.data = data;
	}

	public Result(int response, String message) 
	{
		this(response);
		this.message = message;
	}
	
	public Result(int response, String message, Object data) 
	{
		this(response, message);
		this.data = data;
	}
	
	
	
	private void fillParams()
	{
		success = response>199;
		
		switch (response) 
		{
		case PERSON_NULL:
			message = "Person information is null";
			break;
		case NETWORK_ERROR:
			message = "Network error";;
			break;
		case CONNECTION_FAILED:
			message = "Connection Failed";
			break;
		case LOGIN_CANCELED:
			message = "Connection Canceled";
			break;
		case LOGIN_FAILED:
			message = "Login Failed";
			break;
			
		case PERSON_INFORMATION:
			message = "Person information is ready";
			break;
		case REVOKE_ACCESS:
			message = "User access revoked!";
			break;
		}
	}
	
	public String toJson()
	{
		return new Gson().toJson(this);
	}
}
