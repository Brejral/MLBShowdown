package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.brejral.mlbshowdown.MLBShowdown;

public class Card {
   public int[] chart = new int[10], gameStats = new int[MLBShowdown.GAME_STATS.size()];
   public String cardType, name, lastName, image, team, rarity, bats, throwHand, pos1, pos2, icons;
   public int onbase, control, ip, speed, cardnum, id, points, posBonus1, posBonus2;
   public int ipAdj, controlAdj;
   public boolean isImageLoaded = false;
   public TextureRegion cardTexture;
   
   public Card(DatabaseCursor cursor) {
      populateInfo(cursor);
      CardConstants.createTexture(this);
   }
   
   private void populateInfo(DatabaseCursor cursor) {
      id = cursor.getInt(0);
      cardType = cursor.getString(25);
      cardnum = cursor.getInt(1);
      name = cursor.getString(2);
      lastName = cursor.getString(26);
      team = cursor.getString(3);
      points = cursor.getInt(4);
      rarity = cursor.getString(5);
      pos1 = cursor.getString(6);
      switch (cardType) {
      case "Batter":
         posBonus1 = cursor.getInt(7);
         pos2 = cursor.getString(8);
         posBonus2 = cursor.getInt(9);
         bats = cursor.getString(10);
         speed = cursor.getInt(11);
         onbase = cursor.getInt(12);
         break;
      case "Pitcher":
         throwHand = cursor.getString(10);
         ip = cursor.getInt(11);
         ipAdj = ip;
         control = cursor.getInt(12);
         controlAdj = control;
         break;
      }
      icons = cursor.getString(13);
      if (cardType.equals("Batter") || cardType.equals("Pitcher")) {
         for (int i = 0; i < 10; i++) {
            chart[i] = cursor.getInt(14 + i);
         }
      }
      image = cursor.getString(24);
   }
   
   public void setControl() {
      controlAdj = control + (ipAdj < 1 ? (ipAdj - 1) : 0);
   }
   
   public void setIP() {
      int ipOld = ipAdj;
      ipAdj = ip - gameStats[MLBShowdown.GAME_STATS.indexOf("OUTS")] / 3 - gameStats[MLBShowdown.GAME_STATS.indexOf("RP")] / 3;
      if (ipOld != ipAdj) {
         setControl();
      }
   }

   public String getPositions() {
      return pos1 + (pos2 != null ? "/" + pos2 : "");
   }
}
