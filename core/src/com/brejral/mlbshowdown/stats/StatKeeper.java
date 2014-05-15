package com.brejral.mlbshowdown.stats;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.game.Game;

public class StatKeeper {
	Game game;
	MLBShowdown sd;
	Database db;
	CareerStats careerStats;
	SeasonStats seasonStats;
	GameStats gameStats;
	List<UpdateStat> updates;
	
	public StatKeeper(Game gm) {
		game = gm;
		db = game.sd.db;
		careerStats = new CareerStats(game.sd);
		seasonStats = new SeasonStats(game.sd);
		gameStats = new GameStats(game.sd);
		updates = new ArrayList<UpdateStat>();
	}
	
	public StatKeeper(MLBShowdown sd) {
		careerStats = new CareerStats(sd);
		seasonStats = new SeasonStats(sd);
	}
	
	public void setUpTableForGame() {
		try {
			db.execSQL("Delete from gamestats;");
			for (Card card: game.awayTeam.roster) {
				String sql = "Insert into gamestats (cardnum) values ("+card.id+");";
				db.execSQL(sql);
			}
			for (Card card: game.homeTeam.roster) {
				String sql = "Insert into gamestats (cardnum) values ("+card.id+");";
				db.execSQL(sql);
			}
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
	
	public int getGameStat(String col, Card card) {
		return 0;
	}
	
	public int getSeasonStat(String col, Card card) {
		return 0;
	}
	
	public int getCareerStat(String col, Card card) {
		return 0;
	}
	
	public void updateSeasonAndCareerStatsFromGame() {
		seasonStats.updateFromGame(gameStats);
		careerStats.updateFromGame(gameStats);
	}
	
	public void executeGameStatChanges() {
		gameStats.executeUpdates(updates);
		updates = new ArrayList<UpdateStat>();
	}
	
	public void increaseStat(Card card, String col, int val) {
		UpdateStat update = getUpdateStat(card);
		update.increaseColumn(col, val);
	}
	
	public void increaseStat(Card card, String col) {
		increaseStat(card, col, 1);
	}
	
	public UpdateStat getUpdateStat(Card card) {
		for (UpdateStat update: updates) {
			if (update.card.equals(card)) {
				return update;
			}
		}
		UpdateStat update = new UpdateStat(card);
		updates.add(update);
		return update;
	}
}
