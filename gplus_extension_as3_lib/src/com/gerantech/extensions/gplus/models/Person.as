package com.gerantech.extensions.gplus.models
{
	public class Person extends Friend
	{
		public var email:String;
		public var birtdate:String;
		public var friends:Vector.<Friend>;
		
		public function Person(json:Object)
		{
			super(json);
			
			this.email = json.email;
			this.birtdate = json.birtdate;
			friends = new Vector.<Friend>();
			for each(var f:Object in json.friends)
				friends.push(new Friend(f));
		}
	}
}