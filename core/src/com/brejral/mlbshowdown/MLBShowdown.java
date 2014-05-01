package com.brejral.mlbshowdown;

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
	public Database showdownDB;
	public static final String DATABASE_NAME = "mlbshowdownDB.db";
	public static final int DATABASE_VERSION = 1;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));
		showdownDB = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, "", null);
		showdownDB.setupDatabase();
		try {
			showdownDB.openOrCreateDatabase();
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
}
