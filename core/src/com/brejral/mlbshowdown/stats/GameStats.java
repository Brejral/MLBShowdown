package com.brejral.mlbshowdown.stats;

import java.util.Arrays;

import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.MLBShowdown;

public class GameStats extends Stats {
	public static String[] columnArray = {
		"CARDNUM","G","PA","AB","R","RBI","PU","SO","GB","FB",
		"BB","SINGLE","DOUBLE","TRIPLE","HR","OUTS","ER","SAC",
		"BF"
	};

	public GameStats(MLBShowdown showdown) {
		super(showdown);
		table = "GAMESTATS";
		columns = Arrays.asList(columnArray);
	}
	
	public void clearGameStats() {
		StringBuilder sql = new StringBuilder("DELETE FROM " + table + ";");
		try {
			db.execSQL(sql.toString());
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
}
