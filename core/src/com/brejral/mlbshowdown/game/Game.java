package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.sql.Database;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.team.Team;

public class Game {
	Database db;
	AssetManager manager;
	Team awayTeam, homeTeam;
	Card batter, pitcher, runner1, runner2, runner3;
	int inning, outs, homeScore, awayScore, pitch, swing;
	boolean top;
	
	
	public Game(Database data) {
		manager = new AssetManager();
		db = data;
		awayTeam = new Team(db, "Mariners");
		homeTeam = new Team(db, "Angels");
	}
}
