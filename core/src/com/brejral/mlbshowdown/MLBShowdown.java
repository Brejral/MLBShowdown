package com.brejral.mlbshowdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.Tab.TabStyle;
import com.brejral.mlbshowdown.TabContainer.TabContainerStyle;
import com.brejral.mlbshowdown.TabPane.TabPaneStyle;
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
   public static FreeTypeFontGenerator MURO_GENERATOR = null;
   public static FreeTypeFontGenerator US101_GENERATOR = null;
   public static FreeTypeFontGenerator MUROSLANT_GENERATOR = null;
   public static FreeTypeFontGenerator AEROITALIC_GENERATOR = null;
   public SpriteBatch batch;
   public Database db;
   public User user;
   public Texture fieldTexture;
   public Texture yellowCircle;
   public Texture blackCircle;
   public Skin skin = new Skin();
   public NinePatch boardNP;
   public static String[] GAME_STATS_ARRAY = { "ORDER1", "ORDER2", "PORDER", "G", "PA", "AB", "R", "RBI", "PU", "SO", "GB", "FB", "BB", "SINGLE", "DOUBLE", "TRIPLE", "HR", "SAC", "SB", 
      "GS", "BF", "OUTS", "ABP", "RP", "ER", "PUP", "SOP", "GBP", "FBP", "BBP", "SINGLEP", "DOUBLEP", "TRIPLEP", "HRP" };
   public static List<String> GAME_STATS = Arrays.asList(GAME_STATS_ARRAY);
   
   @Override
   public void create() {
      setFreeTypeFontGenerators();
      batch = new SpriteBatch();
      this.setScreen(new MainMenu(this));
      fieldTexture = new Texture(Gdx.files.internal("images/baseball_diamond.png"));
      blackCircle = new Texture(Gdx.files.internal("images/circle_black.png"));
      yellowCircle = new Texture(Gdx.files.internal("images/circle_yellow.png"));
      db = DatabaseFactory.getNewDatabase(DATABASE_NAME, DATABASE_VERSION, "", null);
      boardNP = new NinePatch(new TextureRegion(new Texture(Gdx.files.internal("images/scoreboard_background.png")), 64, 64), 8, 8, 8, 8);
      setSkins();
      db.setupDatabase();
      user = new User(this, false);
      try {
         db.openOrCreateDatabase();
      } catch (SQLiteGdxException e) {
         e.printStackTrace();
      }
      //setDbCardNums();
   }

   private static void setFreeTypeFontGenerators() {
      MURO_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muro.ttf"));
      US101_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));
      MUROSLANT_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
      AEROITALIC_GENERATOR = new FreeTypeFontGenerator(Gdx.files.internal("fonts/aero_matics_display_italic.ttf"));
   }
   
   private void setSkins() {
      TextButtonStyle tbStyle = new TextButtonStyle();
      tbStyle.fontColor = Color.WHITE;
      tbStyle.font = MLBShowdown.getAeroItalicFont(20);
      tbStyle.overFontColor = Color.YELLOW;
      tbStyle.disabledFontColor = Color.GRAY;
      skin.add("default", tbStyle, TextButtonStyle.class);
      tbStyle = new TextButtonStyle();
      tbStyle.fontColor = Color.WHITE;
      tbStyle.font = MLBShowdown.getAeroItalicFont(20);
      tbStyle.overFontColor = Color.YELLOW;
      tbStyle.checkedFontColor = Color.RED;
      tbStyle.disabledFontColor = Color.GRAY;
      skin.add("subs", tbStyle, TextButtonStyle.class);
      
      WindowStyle windowStyle = new WindowStyle();
      windowStyle.background = new NinePatchDrawable(boardNP);
      windowStyle.titleFont = MLBShowdown.getAeroItalicFont(12);
      skin.add("default", windowStyle, WindowStyle.class);
      
      LabelStyle labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getAeroItalicFont(20);
      skin.add("aero20", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getAeroItalicFont(15);
      skin.add("aero15", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getAeroItalicFont(16);
      skin.add("aero16", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getAeroItalicFont(30);
      skin.add("aero30", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getAeroItalicFont(40);
      skin.add("aero40", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getMuroFont(16);
      skin.add("muro16", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getMuroFont(45);
      skin.add("muro45", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getMuroFont(50);
      skin.add("muro50", labelStyle, LabelStyle.class);
      labelStyle = new LabelStyle();
      labelStyle.font = MLBShowdown.getMuroFont(40);
      skin.add("muro40", labelStyle, LabelStyle.class);
      
      TabPaneStyle tabPaneStyle = new TabPaneStyle();
      TabContainerStyle tabContainerStyle = new TabContainerStyle();
      TabStyle tabStyle = new TabStyle();
      tabStyle.font = MLBShowdown.getAeroItalicFont(28);
      tabStyle.fontColor = Color.WHITE;
      tabStyle.overFontColor = Color.YELLOW;
      tabStyle.checked = new NinePatchDrawable(boardNP);
      skin.add("default", tabPaneStyle, TabPaneStyle.class);
      skin.add("default", tabContainerStyle, TabContainerStyle.class);
      skin.add("default", tabStyle, TabStyle.class);
      
      
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
   
   public static BitmapFont getUS101Font(int size) {
      FreeTypeFontParameter parameter = new FreeTypeFontParameter();
      parameter.size = size;
      return US101_GENERATOR.generateFont(parameter);
   }
   
   public static BitmapFont getMuroFont(int size) {
      FreeTypeFontParameter parameter = new FreeTypeFontParameter();
      parameter.size = size;
      return MURO_GENERATOR.generateFont(parameter);
   }

   public static BitmapFont getMuroslantFont(int size) {
      FreeTypeFontParameter parameter = new FreeTypeFontParameter();
      parameter.size = size;
      return MUROSLANT_GENERATOR.generateFont(parameter);
   }
   
   public static BitmapFont getAeroItalicFont(int size) {
      FreeTypeFontParameter parameter = new FreeTypeFontParameter();
      parameter.size = size;
      return AEROITALIC_GENERATOR.generateFont(parameter);
   }
   
   @SuppressWarnings("unused")
   private void setDbCardNums() {
      String query = "Select * from cards order by team, lastname;";
      StringBuilder sql = new StringBuilder();
      int cardnum = 14001;
      DatabaseCursor cursor;
      try {
         cursor = db.rawQuery(query);
         while (cursor.next()) {
            sql.append("Update cards set cardnum = " + cardnum + " where id = " + cursor.getInt(0) + "; ");
            cardnum++;
         }
         db.execSQL(sql.toString());
      } catch (SQLiteGdxException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
