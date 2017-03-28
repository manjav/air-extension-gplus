package com.gerantech.extensions.gplus.models
{
	public class Result
	{
		public var response:int;
		public var success:Boolean;
		public var message:String;
		public var person:Person;
		
		public static const PERSON_INFORMATION:int = 101;
		public static const REVOKE_ACCESS:int = 102;
		
		public static const PERSON_NULL:int = 201;
		public static const NETWORK_ERROR:int = 202;
		public static const CONNECTION_FAILED:int = 203;
		public static const LOGIN_CANCELED:int = 204;
		public static const LOGIN_FAILED:int = 205;		
		
		public function Result(jsonStr:String)
		{
			var json:Object = JSON.parse(jsonStr);
			
			response = json.response;
			
			success = json.success;
			
			if(json.message)
				message = json.message;
			
			if(json.data)
				person = new Person(json.data);
		}
	}
}