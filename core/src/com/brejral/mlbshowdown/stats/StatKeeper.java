package com.brejral.mlbshowdown.stats;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.sql.Database;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardActor;
import com.brejral.mlbshowdown.game.Game;
import com.brejral.mlbshowdown.team.Team;

public class StatKeeper {
   Game game;
   MLBShowdown sd;
   Database db;
   CareerStats careerStats;
   SeasonStats seasonStats;
   List<UpdateStat> updates;
   static List<String> GAME_STATS = MLBShowdown.GAME_STATS;

   public StatKeeper(Game gm) {
      game = gm;
      db = game.sd.db;
      careerStats = new CareerStats(game.sd);
      seasonStats = new SeasonStats(game.sd);
      updates = new ArrayList<UpdateStat>();
   }

   public StatKeeper(MLBShowdown sd) {
      careerStats = new CareerStats(sd);
      seasonStats = new SeasonStats(sd);
   }

   public void setUpStatsForGame() {
      for (Card card : game.awayTeam.lineup) {
         int order = game.awayTeam.lineup.indexOf(card) + 1;
         increaseStat(card, "ORDER1", order);
         increaseStat(card, "ORDER2");
         increaseStat(card, "G");
      }
      for (Card card : game.homeTeam.lineup) {
         int order = game.homeTeam.lineup.indexOf(card) + 1;
         increaseStat(card, "ORDER1", order);
         increaseStat(card, "ORDER2");
         increaseStat(card, "G");
      }
      increaseStat(game.homeTeam.positions.get(1), "G");
      increaseStat(game.homeTeam.positions.get(1), "GS");
      increaseStat(game.homeTeam.positions.get(1), "PORDER");
      increaseStat(game.awayTeam.positions.get(1), "G");
      increaseStat(game.awayTeam.positions.get(1), "GS");
      increaseStat(game.awayTeam.positions.get(1), "PORDER");
      executeGameStatChanges();
   }
   
   public void setUpStatsForSubstitutions() {
      for (Card card : game.awayTeam.lineup) {
         if (getGameStat("G", card) != 1) {
            int order = game.awayTeam.lineup.indexOf(card) + 1;
            increaseStat(card, "G");
            increaseStat(card, "ORDER1", order);
            increaseStat(card, "ORDER2", nextOrder2(game.awayTeam, order));
         }
      }
      for (Card card : game.homeTeam.lineup) {
         if (getGameStat("G", card) != 1) {
            int order = game.homeTeam.lineup.indexOf(card) + 1;
            increaseStat(card, "G");
            increaseStat(card, "ORDER1", order);
            increaseStat(card, "ORDER2", nextOrder2(game.homeTeam, order));
         }
      }
      if (getGameStat("G", game.awayTeam.positions.get(1)) != 1) {
         increaseStat(game.awayTeam.positions.get(1), "G");
         increaseStat(game.awayTeam.positions.get(1), "PORDER", nextPOrder(game.awayTeam));
      }
      if (getGameStat("G", game.homeTeam.positions.get(1)) != 1) {
         increaseStat(game.homeTeam.positions.get(1), "G");
         increaseStat(game.homeTeam.positions.get(1), "PORDER", nextPOrder(game.homeTeam));
      }
      executeGameStatChanges();
   }

   public static int getGameStat(String col, Card card) {
      return card.getGameStat(col);
   }

   public int getSeasonStat(String col, Card card) {
      return 0;
   }

   public int getCareerStat(String col, Card card) {
      return 0;
   }

   public void updateSeasonAndCareerStatsFromGame() {
   }

   public void executeGameStatChanges() {
      for (UpdateStat update : updates) {
         update.executeUpdate();
      }
      updates = new ArrayList<UpdateStat>();
   }
   
   public void increaseStat(CardActor cardActor, String col, int val) {
      increaseStat(cardActor.card, col, val);
   }
   
   public void increaseStat(CardActor cardActor, String col) {
      increaseStat(cardActor.card, col);
   }

   public void increaseStat(Card card, String col, int val) {
      UpdateStat update = getUpdateStat(card);
      update.increaseColumn(col, val);
   }

   public void increaseStat(Card card, String col) {
      increaseStat(card, col, 1);
   }

   public UpdateStat getUpdateStat(Card card) {
      for (UpdateStat update : updates) {
         if (update.card.equals(card)) {
            return update;
         }
      }
      UpdateStat update = new UpdateStat(card);
      updates.add(update);
      return update;
   }

   public static int nextPOrder(Team team) {
      int pOrder = 0;
      for (Card card : team.roster) {
         int p = getGameStat("PORDER", card);
         if (p > pOrder) {
            pOrder = p;
         }
      }
      return ++pOrder;
   }
   
   public static int nextOrder2(Team team, int order1) {
      int order2 = 0;
      for (Card card : team.roster) {
         int on1 = getGameStat("ORDER1", card);
         int on2 = getGameStat("ORDER2", card);
         if (on1 == order1 && on2 > order2) {
            order2 = on2;
         }
      }
      return ++order2;
   }
   
   public String getPitchingStat(int[] array, String col) {
      switch (col) {
      case "ERA":
         if (array[GAME_STATS.indexOf("ER")] > 0 && array[GAME_STATS.indexOf("OUTS")] == 0) {
            return "INF";
         } else if (array[GAME_STATS.indexOf("OUTS")] == 0) {
            return "0.00";
         }
         DecimalFormat df = new DecimalFormat("0.00");
         return df.format(27f * (float) array[GAME_STATS.indexOf("ER")] / (float) array[GAME_STATS.indexOf("OUTS")]);
      case "IP":
         int outs = array[GAME_STATS.indexOf("OUTS")];
         return outs / 3 + "." + (outs - outs / 3 * 3);
      case "BB":
         return Integer.toString(array[GAME_STATS.indexOf("BBP")]);
      case "SO":
         return Integer.toString(array[GAME_STATS.indexOf("SOP")]);
      case "H":
         return Integer.toString(array[GAME_STATS.indexOf("SINGLEP")] + array[GAME_STATS.indexOf("DOUBLEP")] + array[GAME_STATS.indexOf("TRIPLEP")] + array[GAME_STATS.indexOf("HRP")]);
      case "R":
         return Integer.toString(array[GAME_STATS.indexOf("RP")]);
      default:
         return Integer.toString(array[GAME_STATS.indexOf(col)]);
      }
   }

   public String getBattingStat(int[] array, String col) {
      switch (col) {
      case "H":
         return Integer.toString(array[GAME_STATS.indexOf("SINGLE")] + array[GAME_STATS.indexOf("DOUBLE")] + array[GAME_STATS.indexOf("TRIPLE")] + array[GAME_STATS.indexOf("HR")]);
      case "AVG":
         if (array[GAME_STATS.indexOf("AB")] == 0) {
            return ".000";
         }
         DecimalFormat df = new DecimalFormat("#.000");
         return df.format((float) (array[GAME_STATS.indexOf("SINGLE")] + array[GAME_STATS.indexOf("DOUBLE")] + array[GAME_STATS.indexOf("TRIPLE")] + array[GAME_STATS.indexOf("HR")])
               / (float) (array[GAME_STATS.indexOf("AB")]));
      default:
         return Integer.toString(array[GAME_STATS.indexOf(col)]);
      }
   }
}
