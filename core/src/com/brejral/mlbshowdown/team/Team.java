package com.brejral.mlbshowdown.team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.card.Card;

public class Team {
	private Database db;
	public List<Card> roster = new ArrayList<Card>();
	public List<Card> lineup = new ArrayList<Card>();
	public List<Card> positions = new ArrayList<Card>();
	public List<Card> bullpen = new ArrayList<Card>();
	public List<Card> rotation = new ArrayList<Card>();
	public List<Card> bench = new ArrayList<Card>();
	public String fullName, location, nickName;
	public Texture logo;
	public int points;
	
	public Team(Database database, String name) {
		db = database;
		nickName = name;
		getTeamDataFromDB();
	}
	
	private void getTeamDataFromDB() {
		DatabaseCursor cursor = null;
		try {
			String query = "Select * from teams where nickname = '" + nickName + "';";
			cursor = db.rawQuery(query);
			
			// Get Roster
			String str = cursor.getString(4);
			String[] rosterNums = str.split(",");
			
			// Get Lineup
			str = cursor.getString(5);
			List<String> lineupNums = Arrays.asList(str.split(","));
			
			
			// Get Pitching Rotation
			str = cursor.getString(6);
			List<String> rotationNums = Arrays.asList(str.split(","));
			
			// Get Positions
			str = cursor.getString(7);
			List<String> positionsNums = Arrays.asList(str.split(","));
			
			for (String num : rosterNums) {
				// TODO Add a loading progress screen here
				int iden = Integer.parseInt(num);
				roster.add(new Card(db, iden));
				
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
			
			// Update card lists with Cards from the roster
			for (Card card : roster) {
				int index = lineupNums.indexOf(Integer.toString(card.id));
				if (index > -1) {
					lineup.set(index, card);
				}
				index = rotationNums.indexOf(Integer.toString(card.id));
				if (index > -1) {
					rotation.set(index, card);
				}
				index = positionsNums.indexOf(Integer.toString(card.id));
				if (index > -1) {
					positions.set(index, card);
				}
			}
			
		} catch(SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
}
