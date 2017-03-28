package com.gerantech.extensions.gplus;

import java.util.ArrayList;

public class Human extends Friend
{
	public String email, birtdate;
	public ArrayList<Friend> friends;

	public Human(String id, String name, String email, String url, String photoUrl, String birtdate, String gender)
	{
		super(id, name, url, photoUrl, gender);

		this.email = email;
		this.birtdate = birtdate;
		friends = new ArrayList<Friend>();
	}
	
	public void addFriend(Friend f)
	{
		friends.add(f);
	}
}
