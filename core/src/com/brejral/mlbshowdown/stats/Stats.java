package com.brejral.mlbshowdown.stats;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardActor;

public class Stats {
	MLBShowdown sd;
	Database db;
	DatabaseCursor cursor;
	public String table;
	public List<String> columns;
	
	public Stats(MLBShowdown showdown) {
		sd = showdown;
		db = sd.db;
	}
	
	public void setCursor(List<Card> cards) {
		StringBuilder sql = new StringBuilder("SELECT * FROM " + table);
		sql.append(" WHERE");
		for (Iterator<Card> iter = cards.iterator(); iter.hasNext(); ) {
			Card card = iter.next();
			sql.append(" CARDNUM = " + card.id);
			if (iter.hasNext()) {
				sql.append(" OR");
			}
		}
		sql.append(";");
		System.out.println(sql.toString());
		
		try {
			cursor = db.rawQuery(sql.toString());
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
	
	public int getInt(CardActor card, String col) {
		int index = columns.indexOf(col);
		return cursor.getInt(index);
	}
	
	public String getUpdatesSqlString(List<UpdateStat> updates) {
		StringBuilder sql = new StringBuilder();
		for (UpdateStat update: updates) {
			sql.append("UPDATE " + table + " SET ");
			for (Iterator<Entry<String, Integer>> iter = update.updates.entrySet().iterator(); iter.hasNext(); ) {
				Entry<String, Integer> entry = iter.next();
				sql.append(entry.getKey() + " = " + entry.getKey() + " + "+ entry.getValue());
				if (iter.hasNext()) {
					sql.append(", ");
				}
			}
			sql.append(" WHERE CARDNUM = " + update.card.id + "; ");
		}
		return sql.toString();
	}
	
	public void executeUpdates(List<UpdateStat> updates) {
		String sql = getUpdatesSqlString(updates);
		try {
			db.execSQL(sql);
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
}
