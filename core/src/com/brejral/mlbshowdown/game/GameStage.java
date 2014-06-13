package com.brejral.mlbshowdown.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.ShapeActor;
import com.brejral.mlbshowdown.ShapeActorLoader;
import com.brejral.mlbshowdown.TextActor;
import com.brejral.mlbshowdown.TextActorLoader;
import com.brejral.mlbshowdown.TextActorLoader.TextActorParameter;
import com.brejral.mlbshowdown.card.CardActor;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.team.Team;

public class GameStage extends Stage {
   public MLBShowdown sd;
   public Game game;
   public FreeTypeFontGenerator aeroGenerator;
   public FreeTypeFontParameter fontParameter;
   AssetManager manager;
   FreeTypeFontGenerator straightGenerator;
   FreeTypeFontGenerator slantGenerator;
   BitmapFont straightCardFont;
   BitmapFont straightCardFont2;
   BitmapFont slantCardFont;
   BitmapFont slantCardFont2;
   public static String[] battingStats = { "AB", "R", "H", "RBI", "HR", "BB", "SO", "AVG" };
   public static String[] pitchingStats = { "IP", "H", "R", "ER", "BB", "SO", "HR", "ERA" };

   public GameStage(Viewport viewport, Game gm) {
      super(viewport);
      game = gm;
      sd = game.sd;
      manager = game.manager;
      aeroGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/aero_matics_display_italic.ttf"));
      fontParameter = new FreeTypeFontParameter();
      straightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));
      slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
      fontParameter.size = 36;
      slantCardFont = slantGenerator.generateFont(fontParameter);
      fontParameter.size = 37;
      slantCardFont2 = slantGenerator.generateFont(fontParameter);
      addListener(new InputListener() {
         @Override
         public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            GameStage stage = (GameStage) event.getStage();
            if (stage.game.isCardZoomed() && !stage.game.getZoomedCard().hasActions()) {
               stage.game.getZoomedCard().zoom();
               stage.game.timer.start();
            }
            return true;
         }
      });
   }

   public static void loadActorsWithAssetManager(AssetManager manager) {
      manager.setLoader(TextActor.class, new TextActorLoader(new InternalFileHandleResolver()));
      manager.setLoader(ShapeActor.class, new ShapeActorLoader(new InternalFileHandleResolver()));

      TextActorParameter textParameter = new TextActorParameter("aero_matics_display_italic.ttf", 30);
      manager.load("Pitch Text", TextActor.class, textParameter);
      manager.load("Swing Text", TextActor.class, textParameter);
      manager.load("Throw Text", TextActor.class, textParameter);

      textParameter = new TextActorParameter("aero_matics_display_italic.ttf", 50);
      manager.load("Away Team Nickname", TextActor.class, textParameter);
      manager.load("Home Team Nickname", TextActor.class, textParameter);

      textParameter = new TextActorParameter("aero_matics_display_italic.ttf", 20);
      manager.load("Pitching Label", TextActor.class, textParameter);
      manager.load("Batting Label", TextActor.class, textParameter);

      textParameter = new TextActorParameter("aero_matics_display_italic.ttf", 20);
      manager.load("Chart Text", TextActor.class, textParameter);
      manager.load("Result Text", TextActor.class, textParameter);
      manager.load("Result Text2", TextActor.class, textParameter);
      manager.load("Prompt Text", TextActor.class, textParameter);
      manager.load("Inning", TextActor.class, textParameter);
      for (String str : battingStats) {
         manager.load("Batting Stat Header " + str, TextActor.class, textParameter);
      }
      for (String str : pitchingStats) {
         manager.load("Pitching Stat Header " + str, TextActor.class, textParameter);
      }

      textParameter = new TextActorParameter("aero_matics_display_italic.ttf", 15);
      manager.load("Away Team Mini Scoreboard", TextActor.class, textParameter);
      manager.load("Home Team Mini Scoreboard", TextActor.class, textParameter);
      manager.load("Outs", TextActor.class, textParameter);

      textParameter = new TextActorParameter("aero_matics_display_italic.ttf", 15);
      for (int i = 1; i < 10; i++) {
         manager.load("Lineup Num " + i, TextActor.class, textParameter);
         manager.load("Lineup Position " + i, TextActor.class, textParameter);
         manager.load("Lineup Name " + i, TextActor.class, textParameter);
      }
      
      textParameter = new TextActorParameter("Muro.ttf", 40);
      manager.load("Inning Text", TextActor.class, textParameter);

      textParameter = new TextActorParameter("Muro.ttf", 50);
      manager.load("Away Score", TextActor.class, textParameter);
      manager.load("Home Score", TextActor.class, textParameter);

      String[] headers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "R", "H", "E" };
      TextActorParameter tp40 = new TextActorParameter("Muro.ttf", 40);
      for (String str : headers) {
         manager.load("Scoreboard Header " + str, TextActor.class, textParameter);
         manager.load("Away " + str, TextActor.class, tp40);
         manager.load("Home " + str, TextActor.class, tp40);
      }
      for (int i = 0; i < 15; i++) {
         textParameter = new TextActorParameter("aero_matics_display_italic.ttf", 16);
         manager.load("Batter Name " + (i + 1), TextActor.class, textParameter);
         if (i < 8) {
            manager.load("Pitcher Name " + (i + 1), TextActor.class, textParameter);
         }

         textParameter = new TextActorParameter("Muro.ttf", 16);
         for (String str : battingStats) {
            manager.load("Batter " + str + " " + (i + 1), TextActor.class, textParameter);
         }
         if (i < 8) {
            for (String str : pitchingStats) {
               manager.load("Pitcher " + str + " " + (i + 1), TextActor.class, textParameter);
            }
         }
      }
   }

   /**
    * Adds all the actors to the stage to be drawn
    */
   public void addActorsToStage() {
      Image diamond = new Image(game.sd.fieldTexture);
      diamond.setName("Field Background");
      addActor(diamond);
      
      TextActor promptText = manager.get("Prompt Text");
      promptText.setName("Prompt Text");
      promptText.setPosition(800, 300);
      promptText.setColor(Color.WHITE);
      addActor(promptText);

      TextButtonStyle style = new TextButtonStyle();
      fontParameter.size = 20;
      style.fontColor = Color.WHITE;
      style.font = aeroGenerator.generateFont(fontParameter);
      style.overFontColor = Color.YELLOW;

      TextButton advanceNButton = new TextButton("Advance None", style);
      advanceNButton.setName("Advance None Button");
      advanceNButton.setVisible(false);
      advanceNButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advanceNButtonClicked();
         }
      });
      addActor(advanceNButton);

      TextButton advance3Button = new TextButton("Advance 3rd", style);
      advance3Button.setName("Advance 3rd Button");
      advance3Button.setVisible(false);
      advance3Button.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advance3ButtonClicked();
         }
      });
      addActor(advance3Button);

      TextButton advance2Button = new TextButton("Advance 2nd", style);
      advance2Button.setName("Advance 2nd Button");
      advance2Button.setVisible(false);
      advance2Button.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advance2ButtonClicked();
         }
      });
      addActor(advance2Button);

      TextButton advanceBButton = new TextButton("Advance Both", style);
      advanceBButton.setName("Advance Both Button");
      advanceBButton.setVisible(false);
      advanceBButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advanceBButtonClicked();
         }
      });
      addActor(advanceBButton);

      TextButton throwHButton = new TextButton("Throw to Home", style);
      throwHButton.setName("Throw Home Button");
      throwHButton.setVisible(false);
      throwHButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            throwHButtonClicked();
         }
      });
      addActor(throwHButton);

      TextButton throw3Button = new TextButton("Throw to 3rd", style);
      throw3Button.setName("Throw 3rd Button");
      throw3Button.setVisible(false);
      throw3Button.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            throw3ButtonClicked();
         }
      });
      addActor(throw3Button);

      TextButton pitchButton = new TextButton("PITCH", style);
      pitchButton.setName("Pitch Button");
      pitchButton.setPosition(800, 150);
      pitchButton.setVisible(true);
      pitchButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            game.pitch();
         }
      });
      addActor(pitchButton);

      TextButton swingButton = new TextButton("SWING", style);
      swingButton.setName("Swing Button");
      swingButton.setPosition(800, 100);
      swingButton.setVisible(true);
      swingButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            game.swing();
         }
      });
      addActor(swingButton);

      TextButton throwButton = new TextButton("THROW", style);
      throwButton.setName("Throw Button");
      throwButton.setPosition(800, 50);
      throwButton.setVisible(false);
      throwButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            game.fieldingThrow();
         }
      });
      addActor(throwButton);

      TextActor pitchText = manager.get("Pitch Text");
      pitchText.setName("Pitch Text");
      pitchText.setPosition(780, 165);
      pitchText.setColor(Color.WHITE);
      addActor(pitchText);

      TextActor chartText = manager.get("Chart Text");
      chartText.setName("Chart Text");
      chartText.setPosition(780, 140);
      chartText.setColor(Color.WHITE);
      addActor(chartText);

      TextActor swingText = manager.get("Swing Text");
      swingText.setName("Swing Text");
      swingText.setPosition(780, 115);
      swingText.setColor(Color.WHITE);
      addActor(swingText);

      TextActor resultText = manager.get("Result Text");
      resultText.setName("Result Text");
      resultText.setPosition(780, 90);
      resultText.setColor(Color.WHITE);
      addActor(resultText);

      TextActor throwText = manager.get("Throw Text");
      throwText.setName("Throw Text");
      throwText.setPosition(780, 65);
      throwText.setColor(Color.WHITE);
      addActor(throwText);

      TextActor resultText2 = manager.get("Result Text2");
      resultText2.setName("Result Text2");
      resultText2.setPosition(780, 40);
      resultText2.setColor(Color.WHITE);
      addActor(resultText2);

      addMiniScoreboardToStage();

      addScoreboardToStage();

      addLineupTextToStage();
   }

   public void throwHButtonClicked() {
      removeSetupForThrowCheck();
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      throwButton.setVisible(true);
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      promptText.setText("Throw Home");
      disableButtons();
      game.scheduleThrowRoll();
   }

   public void throw3ButtonClicked() {
      removeSetupForThrowCheck();
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      throwButton.setVisible(true);
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      promptText.setText("Throw to 3rd");
      disableButtons();
      game.scheduleThrowRoll();
   }

   public void advanceBButtonClicked() {
      removeSetupForAdvancementCheck();
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      promptText.setText("Throw to Base?");
      TextButton throw3Button = (TextButton) getRoot().findActor("Throw 3rd Button");
      throw3Button.setVisible(true);
      throw3Button.setPosition(800 - throw3Button.getWidth() / 2, 270);
      TextButton throwHButton = (TextButton) getRoot().findActor("Throw Home Button");
      throwHButton.setVisible(true);
      throwHButton.setPosition(800 - throwHButton.getWidth() / 2, 240);
      if (game.isFieldingTeamCpu()) {
         String str = GameAI.selectBaseToThrowTo(game);
         if (str.equals("3")) {
            throw3ButtonClicked();
         } else if (str.equals("H")) {
            throwHButtonClicked();
         }
      }
   }

   public void advance2ButtonClicked() {
      removeSetupForAdvancementCheck();
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      throwButton.setVisible(true);
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      promptText.setText("Throw to 3rd");
      disableButtons();
      game.scheduleThrowRoll();
   }

   public void advance3ButtonClicked() {
      game.checkForAdvancementRunner2 = false;
      removeSetupForAdvancementCheck();
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      throwButton.setVisible(true);
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      promptText.setText("Throw Home");
      disableButtons();
      game.scheduleThrowRoll();
   }

   public void advanceNButtonClicked() {
      game.checkForAdvancement = false;
      game.checkForAdvancementRunner2 = false;
      game.checkForAdvancementRunner3 = false;
      removeSetupForAdvancementCheck();
      game.advanceAtBat();
   }

   /**
    * Adds the scoreboard text to the stage
    */
   public void addMiniScoreboardToStage() {
      // Mini Scoreboard Group
      Group scoreboard = new Group();
      scoreboard.setName("Mini Scoreboard Group");
      scoreboard.setZIndex(100);

      Image image = new Image(game.sd.boardNP);
      image.setName("Mini Scoreboard Background");
      image.setSize(200, 100);
      image.setPosition(700, 500);
      image.setZIndex(0);
      image.setTouchable(Touchable.enabled);
      image.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            Actor board = event.getTarget();
            if (board.getName().equals("Mini Scoreboard Background")) {
               getRoot().findActor("Mini Scoreboard Group").setVisible(false);
               SequenceAction sequence = new SequenceAction();
               MoveToAction move = new MoveToAction();
               move.setPosition(0, 0);
               move.setDuration(MLBShowdown.ANIMATION_SPEED);
               SizeToAction size = new SizeToAction();
               size.setSize(MLBShowdown.SCREEN_WIDTH, MLBShowdown.SCREEN_HEIGHT);
               size.setDuration(MLBShowdown.ANIMATION_SPEED);
               ParallelAction parallel = new ParallelAction(move, size);
               sequence.addAction(parallel);
               sequence.addAction(new RunnableAction() {
                  @Override
                  public void run() {
                     getRoot().findActor("Scoreboard Group").setVisible(true);
                     getRoot().findActor("Scoreboard Group").setZIndex(101);
                     updateScoreboard();
                     setAwayStatsTab();
                  }
               });
               board.addAction(sequence);
               board.setZIndex(100);
               board.setName("Scoreboard Background");
            } else {
               getRoot().findActor("Scoreboard Group").setVisible(false);
               SequenceAction sequence = new SequenceAction();
               MoveToAction move = new MoveToAction();
               move.setPosition(700, 500);
               move.setDuration(MLBShowdown.ANIMATION_SPEED);
               SizeToAction size = new SizeToAction();
               size.setSize(200, 100);
               size.setDuration(MLBShowdown.ANIMATION_SPEED);
               ParallelAction parallel = new ParallelAction(move, size);
               sequence.addAction(parallel);
               sequence.addAction(new RunnableAction() {
                  @Override
                  public void run() {
                     getRoot().findActor("Mini Scoreboard Group").setVisible(true);
                     getRoot().findActor("Mini Scoreboard Group").setZIndex(101);
                  }
               });
               board.addAction(sequence);
               board.setZIndex(100);
               board.setName("Mini Scoreboard Background");
            }
         }
      });
      addActor(image);

      TextActor inningText = manager.get("Inning Text");
      inningText.setText("1");
      inningText.setName("Inning Text");
      inningText.setPosition(795, 570);
      inningText.setColor(Color.WHITE);
      scoreboard.addActor(inningText);

      TextActor innText = manager.get("Inning");
      innText.setText("INNING");
      innText.setName("Inning");
      innText.setPosition(800, 540);
      innText.setColor(Color.WHITE);
      scoreboard.addActor(innText);

      int innX = (int) (inningText.getX() + inningText.getWidth() / 2);
      float[] itverts = { innX + 5, 571, innX + 10, 576, innX + 15, 571 };
      ShapeActor inningTopMarker = new ShapeActor(ShapeType.Filled, itverts);
      inningTopMarker.setName("Inning Top Marker");
      inningTopMarker.setColor(Color.YELLOW);
      scoreboard.addActor(inningTopMarker);

      float[] ibverts = { innX + 5, 569, innX + 10, 564, innX + 15, 569 };
      ShapeActor inningBottomMarker = new ShapeActor(ShapeType.Filled, ibverts);
      inningBottomMarker.setName("Inning Bottom Marker");
      inningBottomMarker.setColor(Color.YELLOW);
      inningBottomMarker.setVisible(false);
      scoreboard.addActor(inningBottomMarker);

      TextActor homeTeamText = manager.get("Home Team Mini Scoreboard");
      homeTeamText.setText(game.homeTeam.nickName);
      homeTeamText.setName("Home Team Mini Scoreboard");
      homeTeamText.setPosition(865, 520);
      homeTeamText.setColor(Color.WHITE);
      scoreboard.addActor(homeTeamText);

      TextActor awayTeamText = manager.get("Away Team Mini Scoreboard");
      awayTeamText.setText(game.awayTeam.nickName);
      awayTeamText.setName("Away Team Mini Scoreboard");
      awayTeamText.setPosition(735, 520);
      awayTeamText.setColor(Color.WHITE);
      scoreboard.addActor(awayTeamText);

      TextActor homeScoreText = manager.get("Home Score");
      homeScoreText.setName("Home Score");
      homeScoreText.setText(Integer.toString(game.homeScore));
      homeScoreText.setPosition(865, 555);
      homeScoreText.setColor(Color.WHITE);
      scoreboard.addActor(homeScoreText);

      TextActor awayScoreText = manager.get("Away Score");
      awayScoreText.setName("Away Score");
      awayScoreText.setText(Integer.toString(game.awayScore));
      awayScoreText.setPosition(735, 555);
      awayScoreText.setColor(Color.WHITE);
      scoreboard.addActor(awayScoreText);

      TextActor outsText = manager.get("Outs");
      outsText.setName("Outs");
      outsText.setText("OUTS");
      outsText.setPosition(788, 517);
      outsText.setColor(Color.WHITE);
      scoreboard.addActor(outsText);

      float[] verts1 = { 813, 515 };
      ShapeActor outMarker1 = new ShapeActor(ShapeType.Filled, verts1, 4);
      outMarker1.setName("Out Marker 1");
      outMarker1.setColor(Color.BLACK);
      scoreboard.addActor(outMarker1);

      float[] verts2 = { 824, 515 };
      ShapeActor outMarker2 = new ShapeActor(ShapeType.Filled, verts2, 4);
      outMarker2.setName("Out Marker 2");
      outMarker2.setColor(Color.BLACK);
      scoreboard.addActor(outMarker2);

      addActor(scoreboard);
   }

   /**
    * Adds the scoreboard and stats to stage
    */
   public void addScoreboardToStage() {
      Group scoreboard = new Group();
      scoreboard.setVisible(false);
      scoreboard.setName("Scoreboard Group");
      addActor(scoreboard);

      TextActor awayTeam = manager.get("Away Team Nickname");
      awayTeam.setName("Away Team Nickname");
      awayTeam.setText(game.awayTeam.nickName);
      awayTeam.setAlignment(TextActor.RIGHT);
      awayTeam.setPosition(200, 530);
      awayTeam.setColor(Color.WHITE);
      scoreboard.addActor(awayTeam);

      TextActor homeTeam = manager.get("Home Team Nickname");
      homeTeam.setName("Home Team Nickname");
      homeTeam.setText(game.homeTeam.nickName);
      homeTeam.setAlignment(TextActor.RIGHT);
      homeTeam.setPosition(200, 490);
      homeTeam.setColor(Color.WHITE);
      scoreboard.addActor(homeTeam);

      float[] vertsh = { 20, 548 };
      ShapeActor lineh = new ShapeActor(ShapeType.Filled, vertsh, 810, 4);
      lineh.setColor(Color.WHITE);
      scoreboard.addActor(lineh);

      String[] headers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "R", "H", "E" };
      int x = 250;
      for (String str : headers) {
         TextActor inningNum = manager.get("Scoreboard Header " + str);
         inningNum.setName("Scoreboard Header " + str);
         inningNum.setText(str);
         inningNum.setPosition(x, 575);
         inningNum.setColor(Color.WHITE);
         scoreboard.addActor(inningNum);

         TextActor awayInningScore = manager.get("Away " + str);
         awayInningScore.setName("Away " + str);
         awayInningScore.setPosition(x, 525);
         awayInningScore.setColor(Color.WHITE);
         scoreboard.addActor(awayInningScore);

         TextActor homeInningScore = manager.get("Home " + str);
         homeInningScore.setName("Home " + str);
         homeInningScore.setPosition(x, 485);
         homeInningScore.setColor(Color.WHITE);
         scoreboard.addActor(homeInningScore);
         if (str.equals("9")) {
            float[] vertsv = { x + 33, 470 };
            ShapeActor linev = new ShapeActor(ShapeType.Filled, vertsv, 4, 125);
            linev.setColor(Color.WHITE);
            scoreboard.addActor(linev);
            x += 20;
         }
         x += 50;
      }

      float[] tsverts = { 4, 452 };
      ShapeActor topShadow = new ShapeActor(ShapeType.Filled, tsverts, 447, 1);
      topShadow.setName("Top Shadow");
      topShadow.setColor(0, 0, 0, .3f);
      scoreboard.addActor(topShadow);

      float[] tlverts = { 4, 450 };
      ShapeActor topLine = new ShapeActor(ShapeType.Filled, tlverts, 447, 2);
      topLine.setName("Top Line");
      topLine.setColor(Color.WHITE);
      scoreboard.addActor(topLine);

      float[] vlverts = { 449, 420 };
      ShapeActor middleLine = new ShapeActor(ShapeType.Filled, vlverts, 2, 30);
      middleLine.setColor(Color.WHITE);
      scoreboard.addActor(middleLine);

      float[] vsverts = { 451, 420 };
      ShapeActor middleShadow = new ShapeActor(ShapeType.Filled, vsverts, 1, 33);
      middleShadow.setName("Middle Shadow");
      middleShadow.setColor(0, 0, 0, .3f);
      scoreboard.addActor(middleShadow);

      float[] bsverts = { 451, 422 };
      ShapeActor bottomShadow = new ShapeActor(ShapeType.Filled, bsverts, 445, 1);
      bottomShadow.setName("Bottom Shadow");
      bottomShadow.setColor(0, 0, 0, .3f);
      scoreboard.addActor(bottomShadow);

      float[] blverts = { 450, 420 };
      ShapeActor bottomLine = new ShapeActor(ShapeType.Filled, blverts, 448, 2);
      bottomLine.setName("Bottom Line");
      bottomLine.setColor(Color.WHITE);
      scoreboard.addActor(bottomLine);

      TextButtonStyle tbStyle = new TextButtonStyle();
      fontParameter.size = 28;
      tbStyle.font = aeroGenerator.generateFont(fontParameter);
      tbStyle.fontColor = Color.WHITE;
      tbStyle.overFontColor = Color.YELLOW;
      TextButton homeTab = new TextButton(game.homeTeam.nickName, tbStyle);
      homeTab.setSize(445, 28);
      homeTab.setPosition(451, 422);
      homeTab.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            setHomeStatsTab();
         }
      });
      scoreboard.addActor(homeTab);

      TextButton awayTab = new TextButton(game.awayTeam.nickName, tbStyle);
      awayTab.setSize(445, 28);
      awayTab.setPosition(4, 422);
      awayTab.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            setAwayStatsTab();
         }
      });
      scoreboard.addActor(awayTab);

      addStatHeaders();

      addStatRows();
   }

   public void updateScoreboard() {
      String[] headers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "R", "H", "E" };
      int i = 0;
      for (String str : headers) {
         TextActor homeInningScore = (TextActor) getRoot().findActor("Home " + str);
         if (game.homeScorePerInning.size() > i) {
            homeInningScore.setText(Integer.toString(game.homeScorePerInning.get(i)));
         } else if (str.equals("R")) {
            homeInningScore.setText(Integer.toString(game.homeScore));
         } else if (str.equals("H")) {
            homeInningScore.setText(Integer.toString(game.homeHits));
         } else if (str.equals("E")) {
            homeInningScore.setText(Integer.toString(game.homeErrors));
         }
         TextActor awayInningScore = (TextActor) getRoot().findActor("Away " + str);
         if (game.awayScorePerInning.size() > i) {
            awayInningScore.setText(Integer.toString(game.awayScorePerInning.get(i)));
         } else if (str.equals("R")) {
            awayInningScore.setText(Integer.toString(game.awayScore));
         } else if (str.equals("H")) {
            awayInningScore.setText(Integer.toString(game.awayHits));
         } else if (str.equals("E")) {
            awayInningScore.setText(Integer.toString(game.awayErrors));
         }
         i++;
      }
   }

   public void addStatHeaders() {
      Group scoreboard = (Group) getRoot().findActor("Scoreboard Group");
      TextActor hittingLabel = manager.get("Batting Label");
      hittingLabel.setName("Batting Label");
      hittingLabel.setText("Batting");
      hittingLabel.setColor(Color.WHITE);
      hittingLabel.setPosition(225, 400);
      scoreboard.addActor(hittingLabel);

      TextActor pitchingLabel = manager.get("Pitching Label");
      pitchingLabel.setName("Pitching Label");
      pitchingLabel.setText("Pitching");
      pitchingLabel.setColor(Color.WHITE);
      pitchingLabel.setPosition(675, 400);
      scoreboard.addActor(pitchingLabel);

      int x = 180;
      for (String str : battingStats) {
         TextActor header = manager.get("Batting Stat Header " + str);
         header.setName("Batting Stat Header " + str);
         header.setText(str);
         header.setColor(Color.WHITE);
         header.setPosition(x, 370);
         scoreboard.addActor(header);
         x += 35;
      }
      x = 630;
      for (String str : pitchingStats) {
         TextActor header = manager.get("Pitching Stat Header " + str);
         header.setName("Pitching Stat Header " + str);
         header.setText(str);
         header.setColor(Color.WHITE);
         header.setPosition(x, 370);
         scoreboard.addActor(header);
         x += 35;
      }
   }

   public void addStatRows() {
      for (int i = 0; i < 15; i++) {
         addBatterStatRow(i + 1);
         if (i < 8) {
            addPitcherStatRow(i + 1);
         }
      }
   }

   public void setAwayStatsTab() {
      ShapeActor topShadow = (ShapeActor) getRoot().findActor("Top Shadow");
      float[] tsverts = { 4, 452 };
      topShadow.setVertices(tsverts);

      ShapeActor topLine = (ShapeActor) getRoot().findActor("Top Line");
      float[] tlverts = { 4, 450 };
      topLine.setVertices(tlverts);

      ShapeActor bottomShadow = (ShapeActor) getRoot().findActor("Bottom Shadow");
      float[] bsverts = { 451, 422 };
      bottomShadow.setVertices(bsverts);

      ShapeActor bottomLine = (ShapeActor) getRoot().findActor("Bottom Line");
      float[] blverts = { 450, 420 };
      bottomLine.setVertices(blverts);

      ShapeActor middleShadow = (ShapeActor) getRoot().findActor("Middle Shadow");
      float[] msverts = { 451, 420 };
      middleShadow.setVertices(msverts);

      for (int i = 0; i < 15; i++) {
         Card cardInfo = i < game.awayTeam.getBattingStats().size() ? game.awayTeam.getBattingStats().get(i) : null;
         updateBatterStatRow(i + 1, cardInfo);
         if (i < 8) {
            cardInfo = i < game.awayTeam.getPitchingStats().size() ? game.awayTeam.getPitchingStats().get(i) : null;
            updatePitcherStatRow(i + 1, cardInfo);
         }
      }
   }

   public void setHomeStatsTab() {
      ShapeActor topShadow = (ShapeActor) getRoot().findActor("Top Shadow");
      float[] tsverts = { 449, 452 };
      topShadow.setVertices(tsverts);

      ShapeActor topLine = (ShapeActor) getRoot().findActor("Top Line");
      float[] tlverts = { 449, 450 };
      topLine.setVertices(tlverts);

      ShapeActor bottomShadow = (ShapeActor) getRoot().findActor("Bottom Shadow");
      float[] bsverts = { 4, 422 };
      bottomShadow.setVertices(bsverts);

      ShapeActor bottomLine = (ShapeActor) getRoot().findActor("Bottom Line");
      float[] blverts = { 2, 420 };
      bottomLine.setVertices(blverts);

      ShapeActor middleShadow = (ShapeActor) getRoot().findActor("Middle Shadow");
      float[] msverts = { 448, 420 };
      middleShadow.setVertices(msverts);

      for (int i = 0; i < 15; i++) {
         Card card = i < game.homeTeam.getBattingStats().size() ? game.homeTeam.getBattingStats().get(i) : null;
         updateBatterStatRow(i + 1, card);
         if (i < 8) {
            card = i < game.homeTeam.getPitchingStats().size() ? game.homeTeam.getPitchingStats().get(i) : null;
            updatePitcherStatRow(i + 1, card);
         }
      }
   }

   private void updateBatterStatRow(int i, Card cardInfo) {
      Group scoreboard = (Group) getRoot().findActor("Scoreboard Group");
      TextActor name = (TextActor) scoreboard.findActor("Batter Name " + i);
      name.setText(cardInfo != null ? cardInfo.name : "");
      for (String str : battingStats) {
         TextActor stat = (TextActor) scoreboard.findActor("Batter " + str + " " + i);
         stat.setText(cardInfo != null ? game.statKeeper.getBattingStat(cardInfo.gameStats, str) : "");
      }
   }

   private void updatePitcherStatRow(int i, Card card) {
      Group scoreboard = (Group) getRoot().findActor("Scoreboard Group");
      TextActor name = (TextActor) scoreboard.findActor("Pitcher Name " + i);
      name.setText(card != null ? card.name : "");
      for (String str : pitchingStats) {
         TextActor stat = (TextActor) scoreboard.findActor("Pitcher " + str + " " + i);
         stat.setText(card != null ? game.statKeeper.getPitchingStat(card.gameStats, str) : "");
      }
   }

   private void addBatterStatRow(int i) {
      Group scoreboard = (Group) getRoot().findActor("Scoreboard Group");
      int y = 370 - 20 * i;
      TextActor name = manager.get("Batter Name " + i);
      name.setName("Batter Name " + i);
      name.setColor(Color.WHITE);
      name.setAlignment(TextActor.LEFT);
      name.setPosition(10, y);
      scoreboard.addActor(name);

      int x = 180;
      for (String str : battingStats) {
         TextActor stat = manager.get("Batter " + str + " " + i);
         stat.setName("Batter " + str + " " + i);
         stat.setColor(Color.WHITE);
         stat.setPosition(x, y);
         scoreboard.addActor(stat);
         x += 35;
      }
   }

   private void addPitcherStatRow(int i) {
      Group scoreboard = (Group) getRoot().findActor("Scoreboard Group");
      int y = 370 - 20 * i;
      TextActor name = manager.get("Pitcher Name " + i);
      name.setName("Pitcher Name " + i);
      name.setColor(Color.WHITE);
      name.setAlignment(TextActor.LEFT);
      name.setPosition(460, y);
      scoreboard.addActor(name);

      int x = 630;
      for (String str : pitchingStats) {
         TextActor stat = manager.get("Pitcher " + str + " " + i);
         stat.setName("Pitcher " + str + " " + i);
         stat.setColor(Color.WHITE);
         stat.setPosition(x, y);
         scoreboard.addActor(stat);
         x += 35;
      }
   }

   /**
    * Adds the lineup text to the stage
    */
   public void addLineupTextToStage() {
      Group lineup = new Group();
      lineup.setName("Lineup Group");
      addActor(lineup);

      int x1 = 10;
      int x2 = 25;
      int x3 = 38;
      int y = 125;
      int ydiff = 14;

      Image lineupBackground = new Image(game.sd.boardNP);
      lineupBackground.setName("Lineup Background");
      lineupBackground.setPosition(0, 0);
      lineupBackground.setSize(150, 138);
      lineupBackground.addListener(new InputListener() {
         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (fromActor == null || !fromActor.getName().equals("Lineup Card")) {
               game.timer.scheduleTask(new Task() {
                  @Override
                  public void run() {
                     getRoot().findActor("Lineup Card").setZIndex(1000);
                     getRoot().findActor("Lineup Card").setVisible(true);
                  }
               }, 2);
            }
         }
      });
      lineup.addActor(lineupBackground);

      for (int i = 0; i < 9; i++) {
         TextActor num = manager.get("Lineup Num " + (i + 1));
         num.setName("Lineup Num " + (i + 1));
         num.setText(Integer.toString(i + 1));
         num.setPosition(x1, y);
         num.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
         lineup.addActor(num);
         TextActor pos = manager.get("Lineup Position " + (i + 1));
         pos.setName("Lineup Position " + (i + 1));
         pos.setPosition(x2, y);
         pos.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
         lineup.addActor(pos);
         TextActor name = manager.get("Lineup Name " + (i + 1));
         name.setName("Lineup Name " + (i + 1));
         name.setAlignment(TextActor.LEFT);
         name.setPosition(x3, y);
         name.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
         lineup.addActor(name);
         y -= ydiff;
      }
      // This was added because of a bug when the lineup card is shown
      // The last actor flashes when the card is rendering.
      // This is a useless actor
      lineup.addActor(new TextActor(".", "Muro.ttf", 3));

      lineup.addActor(new LineupCard());
      setLineupText();
   }
   
   @Override
   public void draw() {
      super.draw();
   }

   public void setupStageForAdvancementCheck() {
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      TextButton advanceNButton = (TextButton) getRoot().findActor("Advance None Button");
      TextButton advance2Button = (TextButton) getRoot().findActor("Advance 2nd Button");
      TextButton advance3Button = (TextButton) getRoot().findActor("Advance 3rd Button");
      TextButton advanceBButton = (TextButton) getRoot().findActor("Advance Both Button");
      if (game.checkForAdvancementRunner2 && game.checkForAdvancementRunner3) {
         promptText.setText("Advance Runners?");
         advanceBButton.setPosition(800 - advanceBButton.getWidth() / 2, 270);
         advanceBButton.setVisible(true);
         advance3Button.setPosition(800 - advance3Button.getWidth() / 2, 240);
         advance3Button.setVisible(true);
         advanceNButton.setPosition(800 - advanceNButton.getWidth() / 2, 210);
         advanceNButton.setVisible(true);
      } else if (game.checkForAdvancementRunner2 || game.checkForAdvancementRunner3) {
         promptText.setText("Advance Runner?");
         if (game.checkForAdvancementRunner2) {
            advance2Button.setPosition(800 - advance2Button.getWidth() / 2, 270);
            advance2Button.setVisible(true);
         } else if (game.checkForAdvancementRunner3) {
            advance3Button.setPosition(800 - advance3Button.getWidth() / 2, 270);
            advance3Button.setVisible(true);
         }
         advanceNButton.setPosition(800 - advanceNButton.getWidth() / 2, 240);
         advanceNButton.setVisible(true);
      }
      if (game.isBattingTeamCpu()) {
         String str = GameAI.selectRunnersToAdvance(game);
         if (str.equals("B")) {
            advanceBButtonClicked();
            System.out.println("Both Button Clicked");
         } else if (str.equals("3")) {
            advance3ButtonClicked();
            System.out.println("3rd Button Clicked");
         } else if (str.equals("2")) {
            advance2ButtonClicked();
            System.out.println("2nd Button Clicked");
         } else if (str.equals("N")) {
            advanceNButtonClicked();
            System.out.println("None Button Clicked");
         }
      }
   }

   public void removeSetupForAdvancementCheck() {
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      promptText.setText("");
      getRoot().findActor("Advance None Button").setVisible(false);
      getRoot().findActor("Advance 2nd Button").setVisible(false);
      getRoot().findActor("Advance 3rd Button").setVisible(false);
      getRoot().findActor("Advance Both Button").setVisible(false);
   }

   public void removeSetupForThrowCheck() {
      getRoot().findActor("Throw Home Button").setVisible(false);
      getRoot().findActor("Throw 3rd Button").setVisible(false);
   }

   public void setupStageForDoublePlayCheck() {
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      if (game.checkForDoublePlay) {
         throwButton.setVisible(true);
         promptText.setText("Double Play Roll");
      } else {
         throwButton.setVisible(false);
         TextActor result2 = (TextActor) getRoot().findActor("Result Text2");
         TextActor throwText = (TextActor) getRoot().findActor("Throw Text");
         result2.setText("");
         throwText.setText("");
         promptText.setText("");
      }
   }

   /**
    * Highlights the current batter in the lineup and removes the highlight from
    * other batters
    */
   public void setLineupHighlight() {
      int index = 0;
      if (game.isTop) {
         index = game.awayTeam.lineupSpot + 1;
      } else {
         index = game.homeTeam.lineupSpot + 1;
      }
      for (int i = 1; i < 10; i++) {
         Color c = Color.WHITE;
         if (i == index) {
            c = Color.YELLOW;
         }
         getRoot().findActor("Lineup Num " + i).setColor(c);
         getRoot().findActor("Lineup Position " + i).setColor(c);
         getRoot().findActor("Lineup Name " + i).setColor(c);
      }
   }

   /**
    * Sets the Lineup text to the lineup for the current batting team
    */
   public void setLineupText() {
      Team team = null;
      if (game.isTop) {
         team = game.awayTeam;
      } else {
         team = game.homeTeam;
      }
      for (int i = 0; i < 9; i++) {
         TextActor pos = (TextActor) getRoot().findActor("Lineup Position " + (i + 1));
         TextActor name = (TextActor) getRoot().findActor("Lineup Name " + (i + 1));
         pos.setText(team.getPosition(team.lineup.get(i)));
         name.setText(team.lineup.get(i).name);
      }
   }

   /**
    * Sets the pitch text based on the pitch roll
    */
   public void setPitchText() {
      TextActor text = (TextActor) getRoot().findActor("Pitch Text");
      if ((game.isFieldingTeamUser() && game.isPChart)
            || (game.isBattingTeamUser() && !game.isPChart)) {
         text.setColor(Color.GREEN);
      } else {
         text.setColor(Color.RED);
      }
      String pitchText = game.pitch > 0 ? Integer.toString(game.pitch) : "";
      text.setText(pitchText);
   }

   /**
    * Sets the swing text based on the swing roll
    */
   public void setSwingText() {
      TextActor text = (TextActor) getRoot().findActor("Swing Text");
      if ((game.isFieldingTeamUser() && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) <= 3)
            || (game.isBattingTeamUser() && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) > 3)) {
         text.setColor(Color.GREEN);
      } else {
         text.setColor(Color.RED);
      }
      String swingText = game.swing > 0 ? Integer.toString(game.swing) : "";
      text.setText(swingText);
   }

   public void setThrowText() {
      TextActor text = (TextActor) getRoot().findActor("Throw Text");
      if ((game.isFieldingTeamUser() && (game.result2.contains("Out") || game.result2.equals("Double Play")))
            || (game.isBattingTeamUser() && game.result2.contains("Safe"))) {
         text.setColor(Color.GREEN);
      } else {
         text.setColor(Color.RED);
      }
      String throwText = game.fieldingThrow > 0 ? Integer.toString(game.fieldingThrow) : "";
      if (game.fieldingThrow == 0) {
         getRoot().findActor("Throw Button").setVisible(false);
      }
      text.setText(throwText);
   }

   /**
    * Sets the chart text (Batter's or Pitcher's) based on the
    */
   public void setChartText() {
      TextActor text = (TextActor) getRoot().findActor("Chart Text");
      if ((game.isFieldingTeamUser() && game.isPChart)
            || (game.isBattingTeamUser() && !game.isPChart)) {
         text.setColor(Color.GREEN);
      } else {
         text.setColor(Color.RED);
      }
      text.setText(game.pitch == 0 ? "" : (game.isPChart ? "Pitcher's Chart" : "Batter's Chart"));
   }

   /**
    * Sets the result text based on the result
    */
   public void setResultText() {
      TextActor text = (TextActor) getRoot().findActor("Result Text");
      if ((game.isFieldingTeamUser() && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) <= 3)
            || (game.isBattingTeamUser() && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) > 3)) {
         text.setColor(Color.GREEN);
      } else {
         text.setColor(Color.RED);
      }
      text.setText(game.swing > 0 ? game.result : "");
   }

   public void setResultText2() {
      TextActor text = (TextActor) getRoot().findActor("Result Text2");
      if ((game.isFieldingTeamUser() && (game.result2.contains("Out") || game.result2.equals("Double Play")))
            || (game.isBattingTeamUser() && game.result2.contains("Safe"))) {
         text.setColor(Color.GREEN);
      } else {
         text.setColor(Color.RED);
      }
      text.setText(game.fieldingThrow > 0 ? game.result2 : "");
   }

   /**
    * Sets the inning text based on the inning
    */
   public void setInningText() {
      TextActor text = (TextActor) getRoot().findActor("Inning Text");
      text.setText(Integer.toString(game.inning));
   }

   /**
    * Sets the out markers based on the number of outs
    */
   public void setOutMarkers() {
      ShapeActor marker1 = (ShapeActor) getRoot().findActor("Out Marker 1");
      ShapeActor marker2 = (ShapeActor) getRoot().findActor("Out Marker 2");
      if (game.outs == 0) {
         marker1.setColor(Color.BLACK);
         marker2.setColor(Color.BLACK);
      } else if (game.outs == 1) {
         marker1.setColor(Color.YELLOW);
         marker2.setColor(Color.BLACK);
      } else {
         marker1.setColor(Color.YELLOW);
         marker2.setColor(Color.YELLOW);
      }
   }

   /**
    * Sets the top or bottom arrow by the inning
    */
   public void setTopBottomInningMarker() {
      TextActor act = (TextActor) getRoot().findActor("Inning Text");
      int x = (int) (act.getX() + act.getWidth() / 2);
      float[] itverts = { x + 5, 571, x + 10, 576, x + 15, 571 };
      ShapeActor markert = (ShapeActor) getRoot().findActor("Inning Top Marker");
      markert.setVertices(itverts);
      float[] ibverts = { x + 5, 569, x + 10, 564, x + 15, 569 };
      ShapeActor markerb = (ShapeActor) getRoot().findActor("Inning Bottom Marker");
      markerb.setVertices(ibverts);
      if (game.isTop) {
         markert.setVisible(true);
         markerb.setVisible(false);
      } else {
         markert.setVisible(false);
         markerb.setVisible(true);
      }
   }

   /**
    * Sets the away and home score
    */
   public void setScoreText() {
      TextActor home = (TextActor) getRoot().findActor("Home Score");
      TextActor away = (TextActor) getRoot().findActor("Away Score");
      home.setText(Integer.toString(game.homeScore));
      away.setText(Integer.toString(game.awayScore));
   }

   public void removePromptText() {
      TextActor promptText = (TextActor) getRoot().findActor("Prompt Text");
      promptText.setText("");
   }

   /**
    * Disables the swing or pitch buttons based on which player is the user
    */
   public void disableButtons() {
      TextButton pitchButton = (TextButton) getRoot().findActor("Pitch Button");
      TextButton swingButton = (TextButton) getRoot().findActor("Swing Button");
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      if (game.isFieldingTeamCpu()) {
         pitchButton.setTouchable(Touchable.disabled);
      } else {
         pitchButton.setTouchable(Touchable.enabled);
      }
      if (game.isBattingTeamCpu()) {
         swingButton.setTouchable(Touchable.disabled);
      } else {
         swingButton.setTouchable(Touchable.enabled);
      }
      if (game.isFieldingTeamCpu() && throwButton.isVisible()) {
         throwButton.setTouchable(Touchable.disabled);
      } else {
         throwButton.setTouchable(Touchable.enabled);
      }
      if (game.pitch > 0)
         pitchButton.setTouchable(Touchable.disabled);
      if (game.swing > 0 || game.pitch == 0)
         swingButton.setTouchable(Touchable.disabled);
      if ((game.fieldingThrow > 0 || game.swing == 0) && throwButton.isVisible())
         throwButton.setTouchable(Touchable.disabled);
   }

   private class LineupCard extends Actor {
      private TextureRegion texture;
      private CardActor cardActor;
      public int position = 100;
      public float lheight;
      public float lwidth;

      public LineupCard() {
         setName("Lineup Card");
         setVisible(false);
         setScale(.6f);
         lheight = getRoot().findActor("Lineup Background").getHeight();
         lwidth = getRoot().findActor("Lineup Background").getWidth();
      }

      private void setTexture() {
         Team team = null;
         if (game.isTop) {
            team = game.awayTeam;
         } else {
            team = game.homeTeam;
         }
         float y = lheight - (MLBShowdown.SCREEN_HEIGHT - Gdx.input.getY());
         int index = (int) Math.floor(y / (lheight / 9));
         if (position != index && index >= 0 && index < team.lineup.size()) {
            if (cardActor == null || !cardActor.card.equals(team.lineup.get(index))) {
               if (cardActor == null) {
                  cardActor = Pools.obtain(CardActor.class);
               }
               cardActor.setCardInfo(team.lineup.get(index));
               texture = cardActor.cardTexture;
            }
            setVisible(true);
            setSize(texture.getRegionWidth(), texture.getRegionHeight());
         } else if (index < 0) {
            texture = null;
            setVisible(false);
            if (cardActor != null) Pools.free(cardActor);
            cardActor = null;
         }
         if (Gdx.input.getX() > lwidth) {
            texture = null;
            setVisible(false);
            if (cardActor != null) Pools.free(cardActor);
            cardActor = null;
         }
      }

      @Override
      public void setVisible(boolean visible) {
         setZIndex(1000);
         super.setVisible(visible);
      };

      @Override
      public void draw(Batch batch, float parentAlpha) {
         setTexture();
         setPosition(Gdx.input.getX() + 1, MLBShowdown.SCREEN_HEIGHT - Gdx.input.getY() + 1);
         if (texture != null) {
            batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
         }
      }
   }
}
