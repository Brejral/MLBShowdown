package com.brejral.mlbshowdown.card;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.brejral.mlbshowdown.MLBShowdown;

public class Card {
   public int[] chart = new int[10], gameStats = new int[MLBShowdown.GAME_STATS.size()];
   public String cardType, name, lastName, image, team, rarity, bats, throwHand, pos1, pos2, icons;
   public int onbase, control, ip, speed, cardnum, points, posBonus1, posBonus2;
   public int ipAdj, controlAdj;
   public boolean isImageLoaded = false;
   public TextureRegion cardTexture;
   
   public Card(DatabaseCursor cursor) {
      populateInfo(cursor);
      CardConstants.createTexture(this);
   }

   public Card(Card card) {
      chart = card.chart;
      gameStats = card.gameStats;
      cardType = card.cardType;
      name = card.name;
      lastName = card.lastName;
      image = card.image;
      team = card.team;
      rarity = card.rarity;
      bats = card.bats;
      throwHand = card.throwHand;
      pos1 = card.pos1;
      pos2 = card.pos2;
      icons = card.icons;
      onbase = card.onbase;
      control = card.control;
      ip = card.ip;
      speed = card.speed;
      cardnum = card.cardnum;
      points = card.points;
      posBonus1 = card.posBonus1;
      posBonus2 = card.posBonus2;
      ipAdj = card.ipAdj;
      controlAdj = card.controlAdj;
      isImageLoaded = card.isImageLoaded;
      cardTexture = card.cardTexture;
   }
   
   private void populateInfo(DatabaseCursor cursor) {
      cardType = cursor.getString(24);
      cardnum = cursor.getInt(0);
      name = cursor.getString(1);
      lastName = cursor.getString(25);
      team = cursor.getString(2);
      points = cursor.getInt(3);
      rarity = cursor.getString(4);
      pos1 = cursor.getString(5);
      switch (cardType) {
      case "Batter":
         posBonus1 = cursor.getInt(6);
         pos2 = cursor.getString(7);
         posBonus2 = cursor.getInt(8);
         bats = cursor.getString(9);
         onbase = cursor.getInt(10);
         speed = cursor.getInt(11);
         break;
      case "Pitcher":
         throwHand = cursor.getString(9);
         control = cursor.getInt(10);
         ip = cursor.getInt(11);
         ipAdj = ip;
         controlAdj = control;
         break;
      }
      icons = cursor.getString(12);
      if (cardType.equals("Batter") || cardType.equals("Pitcher")) {
         for (int i = 0; i < 10; i++) {
            chart[i] = cursor.getInt(13 + i);
         }
      }
      image = cursor.getString(23);
   }
   
   public void setControl() {
      controlAdj = control + (ipAdj < 1 ? (ipAdj - 1) : 0);
   }
   
   public void setIP() {
      int ipOld = ipAdj;
      ipAdj = ip - getGameStat("OUTS") / 3 - getGameStat("RP") / 3;
      if (ipOld != ipAdj) {
         setControl();
      }
   }

   public String getPositions() {
      return pos1 + (pos2 != null && !pos2.isEmpty() ? "/" + pos2 : "");
   }
   
   public int getGameStat(String col) {
      return gameStats[MLBShowdown.GAME_STATS.indexOf(col)];
   }
   
   public boolean canPlayPosition(String pos) {
      List<String> positions = Arrays.asList(getPositions().split("/"));
      return (pos.equals("1B") || pos.equals("DH")) || ((pos.equals("LF") || pos.equals("RF") || pos.equals("CF")) && positions.contains("OF")) 
            || positions.contains(pos);
   }
}
