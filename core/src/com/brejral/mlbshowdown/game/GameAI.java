package com.brejral.mlbshowdown.game;


public class GameAI {
   public static String selectBaseToThrowTo(Game game) {
      int scoreDiff = 0;
      int outfieldBonus = 0;
      if (game.isTop) {
         outfieldBonus = game.homeTeam.getOutfieldBonus();
         scoreDiff = game.homeScore - game.awayScore;
      } else {
         outfieldBonus = game.awayTeam.getOutfieldBonus();
         scoreDiff = game.awayScore - game.homeScore;
      }
      int safeH, safe3;
      safe3 = game.runner2.card.speed + (game.outs == 2 && !game.result.equals("FB") ? 5 : 0) - outfieldBonus;
      safeH = game.runner3.card.speed + 5 + (game.outs == 2 && !game.result.equals("FB") ? 5 : 0) - outfieldBonus;
      System.out.println("Safe3 = "+safe3);
      System.out.println("SafeH = "+safeH);
      System.out.println("ScoreDiff = "+scoreDiff);
      if ((safeH <= 12 && (safe3 > safeH)) || safe3 > 20 || scoreDiff == 1) {
         System.out.println("Throw Home");
         return "H";
      }
      if (safe3 <= 12 || safeH > 20 || (game.outs == 2 && game.runner2.card.speed > game.runner3.card.speed) || (scoreDiff > 1 && safe3 < safeH)) {
         System.out.println("Throw 3rd");
         return "3";
      }
      System.out.println("Throw Home");
      return "H";
   }

   public static String selectRunnersToAdvance(Game game) {
      int outfieldBonus = 0;
      boolean advance3 = false, advance2 = false;
      if (game.isTop) {
         outfieldBonus = game.homeTeam.getOutfieldBonus();
      } else {
         outfieldBonus = game.awayTeam.getOutfieldBonus();
      }
      int safeH = 0, safe3 = 0;
      if (game.checkForAdvancementRunner2) {
         safe3 = game.runner2.card.speed + (game.outs == 2 && !game.result.equals("FB") ? 5 : 0) - outfieldBonus;
         if (safe3 >= 14) {
            advance2 = true;
         }
      }
      if (game.checkForAdvancementRunner3) {
         safeH = game.runner3.card.speed + 5 + (game.outs == 2 && !game.result.equals("FB") ? 5 : 0) - outfieldBonus;
         if (safeH >= 14) {
            advance3 = true;
         }
      }
      System.out.println("Safe3 = "+safe3);
      System.out.println("SafeH = "+safeH);
      System.out.println("Advance2 = "+advance2);
      System.out.println("Advance3 = "+advance3);
      if (advance2 && advance3) {
         return "B";
      } else if ((advance2 && !game.checkForAdvancementRunner3) || advance3) {
         return advance3 ? "3" : "2";
      } else {
         return "N";
      }
   }
}
