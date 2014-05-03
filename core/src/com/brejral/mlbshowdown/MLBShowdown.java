package com.brejral.mlbshowdown;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.menu.MainMenu;

public class MLBShowdown extends Game {
	public SpriteBatch batch;
	public final int screenWidth = 900;
	public final int screenHeight = 600;
	public Database db;
	public static final String DATABASE_NAME = "mlbshowdownDB.db";
	public static final int DATABASE_VERSION = 1;
													
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));
		db = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, "", null);
		db.setupDatabase();
		try {
			db.openOrCreateDatabase();
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}
	
	public static ArrayList<String> getTeamNamesList() {
		ArrayList<String> list = new ArrayList<String>();
		String[] array = {	"Angels","Astros","Athletics","Blue Jays","Braves","Brewers",
				"Cardinals", "Cubs","Diamondbacks","Dodgers","Giants","Indians",
				"Mariners","Marlins","Mets","Nationals","Orioles","Padres",
				"Phillies","Pirates","Rangers","Rays","Red Sox","Reds",
				"Rockies","Royals","Tigers","Twins","White Sox","Yankees"};
		Collections.addAll(list, array);
		return list;
	}
	
	public static String getIpText(int outs) {
		int inn = outs/3;
		StringBuilder ipStr = new StringBuilder(inn);
		float innf = (float)outs/3f;
		float diff = innf - (float)inn;
		if (diff == 0f) {
			ipStr.append(".0");
		} else if (innf < .5) {
			ipStr.append(".1");
		} else {
			ipStr.append(".2");
		}
		return ipStr.toString();
	}
}
