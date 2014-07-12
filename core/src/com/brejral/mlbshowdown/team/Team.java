package com.brejral.mlbshowdown.team;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.user.User;

public class Team {
   private final MLBShowdown sd;
   public User user;
   private Database db;
   public List<Card> roster = new ArrayList<Card>();
   public List<Card> lineup = new ArrayList<Card>();
   public List<Card> positions = new ArrayList<Card>();
   public List<Card> bullpen = new ArrayList<Card>();
   public List<Card> rotation = new ArrayList<Card>();
   public List<Card> bench = new ArrayList<Card>();
   public List<String> rosterNums, lineupNums, rotationNums, positionsNums, bullpenNums, benchNums;
   public String fullName, location, nickName, abrev;
   public Texture logo;
   public int points, rotationSpot, lineupSpot = 0;
   public boolean isNL = false;

   public Team(MLBShowdown showdown, User usr, String name) {
      sd = showdown;
      user = usr;
      db = sd.db;
      nickName = name;
      rotationSpot = 0;
      getTeamDataFromDB();
      updateCardInfoLists();
   }

   private void getTeamDataFromDB() {
      DatabaseCursor cursor = null;
      try {
         StringBuilder query = new StringBuilder("Select * from teams where nickname = '" + nickName + "';");
         cursor = db.rawQuery(query.toString());
         fullName = cursor.getString(1);
         location = cursor.getString(2);
         abrev = cursor.getString(11);
         isNL = cursor.getInt(10) > 0 ? true : false;

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
         
         str = cursor.getString(8);
         benchNums = Arrays.asList(str.split(","));
         
         str = cursor.getString(9);
         bullpenNums = Arrays.asList(str.split(","));
         
         isNL = cursor.getInt(10) > 0 ? true : false;
         
         query = new StringBuilder("Select * from cards where ");

         for (String num : rosterNums) {
            query.append("id = "+num+ " ");
            if (rosterNums.indexOf(num) != rosterNums.size() - 1) {
               query.append("or ");
            }
         }
         query.append(";");
         cursor = db.rawQuery(query.toString());
         while (cursor.next()) {
            roster.add(new Card(cursor));
         }
      } catch (SQLiteGdxException e) {
         e.printStackTrace();
      }
   }
   
   private Card getCardInfoFromRoster(String num) {
      int id = Integer.parseInt(num);
      for (Card info : roster) {
         if (info.id == id) {
            return info;
         }
      }
      return null;
   }

   public void updateCardInfoLists() {
      // Update card lists with Cards from the roster
      for (String num : lineupNums) {
         lineup.add(getCardInfoFromRoster(num));
      }
      for (String num : benchNums) {
         bench.add(getCardInfoFromRoster(num));
      }
      for (String num : rotationNums) {
         rotation.add(getCardInfoFromRoster(num));
      }
      for (String num : positionsNums) {
         if (num.equals("0")) {
            positions.add(rotation.get(rotationSpot));
         } else {
            positions.add(getCardInfoFromRoster(num));
         }
      }
      for (String num : bullpenNums) {
         bullpen.add(getCardInfoFromRoster(num));
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
         Card cardInfo = positions.get(i);
         if (cardInfo.pos1.contains(CardConstants.POSITION_TEXT[i]) || cardInfo.pos1.contains("IF")) {
            bonus += cardInfo.posBonus1;
         } else if (cardInfo.pos2.contains(CardConstants.POSITION_TEXT[i]) || cardInfo.pos2.contains("IF")) {
            bonus += cardInfo.posBonus2;
         } else if (i == 3 && cardInfo.pos1.isEmpty()) {
            bonus -= 2;
         } else if (i == 3) {
            bonus--;
         } else {
            try {
               throw new Exception("The player cannot play " + CardConstants.POSITION_TEXT[i]);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }
      return bonus;
   }

   public int getOutfieldBonus() {
      int bonus = 0;
      for (int i = 7; i < 10; i++) {
         Card cardInfo = positions.get(i);
         if (cardInfo.pos1.contains(CardConstants.POSITION_TEXT[i]) || cardInfo.pos1.contains("OF")) {
            bonus += cardInfo.posBonus1;
         } else if (cardInfo.pos2.contains(CardConstants.POSITION_TEXT[i]) || cardInfo.pos1.contains("OF")) {
            bonus += cardInfo.posBonus2;
         } else {
            try {
               throw new Exception("The player cannot play " + CardConstants.POSITION_TEXT[i]);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }
      return bonus;
   }

   public List<Card> getBattingStats() {
      List<Card> batters = new ArrayList<>();
      for (Card cardInfo : roster) {
         if (cardInfo.gameStats[MLBShowdown.GAME_STATS.indexOf("ORDER1")] > 0) {
            batters.add(cardInfo);
         }
      }
      batters.sort(new StatComparator("Batting"));
      return batters;
   }

   public List<Card> getPitchingStats() {
      List<Card> pitchers = new ArrayList<>();
      for (Card cardInfo : roster) {
         if (cardInfo.gameStats[MLBShowdown.GAME_STATS.indexOf("PORDER")] > 0) {
            pitchers.add(cardInfo);
         }
      }
      pitchers.sort(new StatComparator("Pitching"));
      return pitchers;
   }

   public Card getCardInfoForId(int id) {
      for (Card cardInfo : roster) {
         if (cardInfo.id == id) {
            return cardInfo;
         }
      }
      return null;
   }

   public class StatComparator implements Comparator<Card> {
      String type;

      public StatComparator(String ty) {
         type = ty;
      }

      @Override
      public int compare(Card card1, Card card2) {
         if (type.equals("Pitching")) {
            return card1.gameStats[MLBShowdown.GAME_STATS.indexOf("PORDER")] - card2.gameStats[MLBShowdown.GAME_STATS.indexOf("PORDER")];
         } else if (type.equals("Batting")) {
            if (card1.gameStats[MLBShowdown.GAME_STATS.indexOf("ORDER1")] == card2.gameStats[MLBShowdown.GAME_STATS.indexOf("ORDER1")]) {
               return card1.gameStats[MLBShowdown.GAME_STATS.indexOf("ORDER2")] - card2.gameStats[MLBShowdown.GAME_STATS.indexOf("ORDER2")];
            }
            return card1.gameStats[MLBShowdown.GAME_STATS.indexOf("ORDER1")] - card2.gameStats[MLBShowdown.GAME_STATS.indexOf("ORDER1")];
         }
         return 0;
      }

      @Override
      public Comparator<Card> reversed() {
         return null;
      }

      @Override
      public Comparator<Card> thenComparing(Comparator<? super Card> arg0) {
         return null;
      }

      @Override
      public <U extends Comparable<? super U>> Comparator<Card> thenComparing(Function<? super Card, ? extends U> arg0) {
         return null;
      }

      @Override
      public <U> Comparator<Card> thenComparing(Function<? super Card, ? extends U> arg0, Comparator<? super U> arg1) {
         return null;
      }

      @Override
      public Comparator<Card> thenComparingDouble(ToDoubleFunction<? super Card> arg0) {
         return null;
      }

      @Override
      public Comparator<Card> thenComparingInt(ToIntFunction<? super Card> arg0) {
         return null;
      }

      @Override
      public Comparator<Card> thenComparingLong(ToLongFunction<? super Card> arg0) {
         return null;
      }

   }
}
