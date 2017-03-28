package com.gerantech.extensions.gplus.models
{
	public class Friend
	{
		public var id:String;
		public var name:String;
		public var url:String;
		public var photoUrl:String;
		public var gender:String;
		
		public function Friend(json:Object)
		{
			this.id = json.id;
			this.name = json.name;
			this.url = json.url;
			this.photoUrl = json.photoUrl;
			this.gender = json.gender;
		}
	}
}