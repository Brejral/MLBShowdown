package com.brejral.mlbshowdown.stats;

import java.util.Hashtable;
import java.util.Map.Entry;

import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;

public class UpdateStat {
	Card card;
	Hashtable<String, Integer> updates;
	
	public UpdateStat(Card cd) {
		card = cd;
		updates = new Hashtable<>();
	}
	
	public void increaseColumn(String col) {
		updates.put(col, 1);
	}
	
   public void increaseColumn(String col, int val) {
		if (!updates.containsKey(col)) {
			updates.put(col, val);
		} else {
			int value = updates.get(col);
			value += val;
			updates.replace(col, value);
		}
	}
	
	public void executeUpdate() {
		for (Entry<String, Integer> entry: updates.entrySet()) {
			card.gameStats[MLBShowdown.GAME_STATS.indexOf(entry.getKey())] += entry.getValue();
		}
	}
}
