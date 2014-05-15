package com.brejral.mlbshowdown.stats;

import com.brejral.mlbshowdown.MLBShowdown;

public class CareerStats extends Stats {
	
	
	public CareerStats(MLBShowdown showdown) {
		super(showdown);
		table = "CAREER";
	}
	
	public void updateFromGame(GameStats game) {
		
	}

}
