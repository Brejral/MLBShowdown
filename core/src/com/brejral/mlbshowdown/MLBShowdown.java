package com.brejral.mlbshowdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.menu.MainMenu;
import com.brejral.mlbshowdown.user.User;

public class MLBShowdown extends Game {
   public static final boolean DEV_MODE = false;
   public static final int SCREEN_WIDTH = 900;
   public static final int SCREEN_HEIGHT = 600;
   public static final String DATABASE_NAME = "mlbshowdownDB.db";
   public static final int DATABASE_VERSION = 1;
   public static final int CARD_WIDTH = 510;
   public static final int CARD_HEIGHT = 710;
   public static float ANIMATION_SPEED = 1f;
   public static float TASK_SPEED = 1f;
   public static float ZOOM_ANIMATION_SPEED = .2f;
   public SpriteBatch batch;
   public Database db;
   public User user;
   public Texture fieldTexture;
   public NinePatch boardNP;
   public static String[] GAME_STATS_ARRAY = { "ORDER1", "ORDER2", "PORDER", "G", "PA", "AB", "R", "RBI", "PU", "SO", "GB", "FB", "BB", "SINGLE", "DOUBLE", "TRIPLE", "HR", "SAC", "GS", "BF", "OUTS",
         "ABP", "RP", "ER", "PUP", "SOP", "GBP", "FBP", "BBP", "SINGLEP", "DOUBLEP", "TRIPLEP", "HRP" };
   public static List<String> GAME_STATS = Arrays.asList(GAME_STATS_ARRAY);

   @Override
   public void create() {
      batch = new SpriteBatch();
      this.setScreen(new MainMenu(this));
      fieldTexture = new Texture(Gdx.files.internal("images/baseball_diamond.png"));
      db = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, "", null);
      boardNP = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("images/scoreboard_background.png")), 64, 64), 8, 8, 8, 8);
      db.setupDatabase();
      user = new User(this, false);
      try {
         db.openOrCreateDatabase();
      } catch (SQLiteGdxException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void render() {
      super.render();
   }

   @Override
   public void dispose() {
      batch.dispose();
   }

   public static ArrayList<String> getTeamNamesList() {
      ArrayList<String> list = new ArrayList<String>();
      String[] array = { "Angels", "Astros", "Athletics", "Blue Jays", "Braves", "Brewers", "Cardinals", "Cubs", "Diamondbacks", "Dodgers", "Giants", "Indians", "Mariners", "Marlins", "Mets",
            "Nationals", "Orioles", "Padres", "Phillies", "Pirates", "Rangers", "Rays", "Red Sox", "Reds", "Rockies", "Royals", "Tigers", "Twins", "White Sox", "Yankees" };
      Collections.addAll(list, array);
      return list;
   }

   public static String getIpText(int outs) {
      int inn = outs / 3;
      StringBuilder ipStr = new StringBuilder(inn);
      float innf = (float) outs / 3f;
      float diff = innf - (float) inn;
      if (diff == 0f) {
         ipStr.append(".0");
      } else if (innf < .5) {
         ipStr.append(".1");
      } else {
         ipStr.append(".2");
      }
      return ipStr.toString();
   }
}
