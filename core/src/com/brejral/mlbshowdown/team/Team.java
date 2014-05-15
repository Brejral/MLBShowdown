package com.brejral.mlbshowdown.team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.card.CardLoader.CardParameter;
import com.brejral.mlbshowdown.user.User;

public class Team {
	private final MLBShowdown sd;
	private AssetManager manager;
	public User user;
	private Database db;
	public List<Card> roster = new ArrayList<Card>();
	public List<Card> lineup = new ArrayList<Card>();
	public List<Card> positions = new ArrayList<Card>();
	public List<Card> bullpen = new ArrayList<Card>();
	public List<Card> rotation = new ArrayList<Card>();
	public List<Card> bench = new ArrayList<Card>();
	public List<String> rosterNums, lineupNums, rotationNums, positionsNums, bullpenNums, benchNums;
	public String fullName, location, nickName;
	public Texture logo;
	public int points, rotationSpot, lineupSpot;
	
	public Team(MLBShowdown showdown, User usr, AssetManager assetManager, String name) {
		sd = showdown;
		user = usr;
		manager = assetManager;
		db = sd.db;
		nickName = name;
		rotationSpot = 0;
		getTeamDataFromDB();
	}
	
	private void getTeamDataFromDB() {
		DatabaseCursor cursor = null;
		try {
			String query = "Select * from teams where nickname = '" + nickName + "';";
			cursor = db.rawQuery(query);
			System.out.println();
			
			// Get Roster
			String str = cursor.getString(4);
			rosterNums = Arrays.asList(str.split(","));
			
			// Get Lineup
			str = cursor.getString(5);
			lineupNums = Arrays.asList(str.split(","));
			
			
			// Get Pitching Rotation
			str = cursor.getString(6);
			rotationNums = Arrays.asList(str.split(","));
			
			// Get Positions
			str = cursor.getString(7);
			positionsNums = Arrays.asList(str.split(","));
			rotationSpot = Integer.parseInt(positionsNums.get(1));
			
			for (String num : rosterNums) {
				// TODO Add a loading progress screen here
				int iden = Integer.parseInt(num);
				CardParameter param = new CardParameter(sd, iden);
				manager.load(num, Card.class, param);
				
				// Also add empty Cards to update later to all card lists
				if (lineup.size() < lineupNums.size()) {
					lineup.add(new Card());
				}
				if (rotation.size() < rotationNums.size()) {
					rotation.add(new Card());
				}
				if (positions.size() < positionsNums.size()) {
					positions.add(new Card());
				}
			}
		} catch(SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
	
	public void updateCardLists() {
		// Update card lists with Cards from the roster
		for (String num : rosterNums) {
			if (manager.isLoaded(num, Card.class)) {
				Card card = manager.get(num, Card.class);
				roster.add(card);
				int index = lineupNums.indexOf(Integer.toString(card.id));
				if (index > -1) {
					lineup.set(index, card);
				}
				index = rotationNums.indexOf(Integer.toString(card.id));
				if (index > -1) {
					if (index == rotationSpot) {
						positions.set(1, card);
					}
					rotation.set(index, card);
				}
				index = positionsNums.indexOf(Integer.toString(card.id));
				if (index > -1) {
					positions.set(index, card);
				}
			} else {
				System.out.println("Card " + num + " not loaded");
			}
		}
	}
	
	public void nextLineupSpot() {
		if (lineupSpot == 8) {
			lineupSpot = 0;
			return;
		}
		lineupSpot++;
	}
	
	public String getPosition(Card card) {
		int i = positions.indexOf(card);
		String pos = CardConstants.POSITION_TEXT[i];
		return pos;
	}
	
	public int getInfieldBonus() {
		int bonus = 0;
		for (int i = 3; i < 7; i++) {
			Card card = positions.get(i);
			if (card.pos1.contains(CardConstants.POSITION_TEXT[i])) {
				bonus += card.posBonus1;
			} else if (card.pos2.contains(CardConstants.POSITION_TEXT[i])) {
				bonus += card.posBonus2;
			} else if (i == 3 && card.pos1.isEmpty()) {
				bonus -= 2;
			} else if (i == 3) {
				bonus--;
			} else {
				try {
					throw new Exception("The player cannot play " + CardConstants.POSITION_TEXT[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bonus;
	}
	
	public int getOutfieldBonus() {
		int bonus = 0;
		for (int i = 7; i < 10; i++) {
			Card card = positions.get(i);
			if (card.pos1.contains(CardConstants.POSITION_TEXT[i]) || card.pos1.contains("OF")) {
				bonus += card.posBonus1;
			} else if (card.pos2.contains(CardConstants.POSITION_TEXT[i]) || card.pos1.contains("OF")) {
				bonus += card.posBonus2;
			} else {
				try {
					throw new Exception("The player cannot play " + CardConstants.POSITION_TEXT[i]);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return bonus;
	}
}
