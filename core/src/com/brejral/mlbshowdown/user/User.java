package com.brejral.mlbshowdown.user;

import com.brejral.mlbshowdown.MLBShowdown;

public class User {
	MLBShowdown sd;
	boolean isCpu = false;
	
	public User(MLBShowdown showdown, boolean cpu) {
		sd = showdown;
		isCpu = cpu;
	}
}
