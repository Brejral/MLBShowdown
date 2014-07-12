package com.brejral.mlbshowdown.game;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.Tab;
import com.brejral.mlbshowdown.TabContainer;
import com.brejral.mlbshowdown.TabPane;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardActor;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.team.Team;

public class GameStage extends Stage {
   public MLBShowdown sd;
   public Game game;
   AssetManager manager;
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
      slantCardFont = MLBShowdown.getMuroslantFont(36);
      slantCardFont2 = MLBShowdown.getMuroslantFont(37);
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

   /**
    * Adds all the actors to the stage to be drawn
    */
   public void addActorsToStage() {
      Image diamond = new Image(game.sd.fieldTexture);
      diamond.setName("Field Background");
      addActor(diamond);
      
      Label promptText = new Label("", sd.skin, "aero20");
      promptText.setName("Prompt Text");
      promptText.setPosition(800, 300);
      promptText.setColor(Color.WHITE);
      addActor(promptText);

      TextButton menuButton = new TextButton("Menu", sd.skin);
      menuButton.setName("Menu Button");
      menuButton.setPosition(5, 575);
      menuButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            GameMenu menu = new GameMenu(sd.skin, game);
            addActor(menu);
         }
      });
      addActor(menuButton);

      TextButton advanceNButton = new TextButton("Advance None", sd.skin);
      advanceNButton.setName("Advance None Button");
      advanceNButton.setVisible(false);
      advanceNButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advanceNButtonClicked();
         }
      });
      addActor(advanceNButton);

      TextButton advance3Button = new TextButton("Advance 3rd", sd.skin);
      advance3Button.setName("Advance 3rd Button");
      advance3Button.setVisible(false);
      advance3Button.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advance3ButtonClicked();
         }
      });
      addActor(advance3Button);

      TextButton advance2Button = new TextButton("Advance 2nd", sd.skin);
      advance2Button.setName("Advance 2nd Button");
      advance2Button.setVisible(false);
      advance2Button.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advance2ButtonClicked();
         }
      });
      addActor(advance2Button);

      TextButton advanceBButton = new TextButton("Advance Both", sd.skin);
      advanceBButton.setName("Advance Both Button");
      advanceBButton.setVisible(false);
      advanceBButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            advanceBButtonClicked();
         }
      });
      addActor(advanceBButton);

      TextButton throwHButton = new TextButton("Throw to Home", sd.skin);
      throwHButton.setName("Throw Home Button");
      throwHButton.setVisible(false);
      throwHButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            throwHButtonClicked();
         }
      });
      addActor(throwHButton);

      TextButton throw3Button = new TextButton("Throw to 3rd", sd.skin);
      throw3Button.setName("Throw 3rd Button");
      throw3Button.setVisible(false);
      throw3Button.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            throw3ButtonClicked();
         }
      });
      addActor(throw3Button);
      
      TextButton pitchButton = new TextButton("Pitch", sd.skin);
      pitchButton.setName("Pitch Button");
      pitchButton.setPosition(800, 150);
      pitchButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            game.pitch();
         }
      });
      addActor(pitchButton);

      TextButton swingButton = new TextButton("Swing", sd.skin);
      swingButton.setName("Swing Button");
      swingButton.setPosition(800, 100);
      swingButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            game.swing();
         }
      });
      addActor(swingButton);

      TextButton throwButton = new TextButton("Throw", sd.skin);
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

      Label pitchText = new Label("", sd.skin, "aero30");
      pitchText.setName("Pitch Text");
      pitchText.setAlignment(Align.center);
      pitchText.setColor(Color.WHITE);
      addActor(pitchText);
      
      Label swingText = new Label("", sd.skin, "aero30");
      swingText.setName("Swing Text");
      swingText.setAlignment(Align.center);
      swingText.setColor(Color.WHITE);
      addActor(swingText);
      
      Label throwText = new Label("", sd.skin, "aero30");
      throwText.setName("Throw Text");
      throwText.setAlignment(Align.center);
      throwText.setColor(Color.WHITE);
      addActor(throwText);

      Label chartText = new Label("", sd.skin, "aero20");
      chartText.setName("Chart Text");
      chartText.setAlignment(Align.left);
      chartText.setColor(Color.WHITE);
      addActor(chartText);

      Label resultText = new Label("", sd.skin, "aero20");
      resultText.setName("Result Text");
      resultText.setAlignment(Align.center);
      resultText.setColor(Color.WHITE);
      addActor(resultText);

      Label resultText2 = new Label("", sd.skin, "aero20");
      resultText2.setName("Result Text2");
      resultText2.setAlignment(Align.left);
      resultText2.setColor(Color.WHITE);
      addActor(resultText2);
      
      Table actionTable = new Table();
      actionTable.setPosition(780, 100);
      addActor(actionTable);
      actionTable.add(pitchText).right().padRight(4).height(30).width(40);
      actionTable.add(pitchButton).left().width(100);
      actionTable.row();
      actionTable.add(chartText).colspan(2).height(25).left();
      actionTable.row();
      actionTable.add(swingText).right().padRight(4).height(30).width(40);
      actionTable.add(swingButton).left().width(100);
      actionTable.row();
      actionTable.add(resultText).height(25).center();
      actionTable.row();
      actionTable.add(throwText).right().padRight(4).height(30).width(40);
      actionTable.add(throwButton).left().width(100);
      actionTable.row();
      actionTable.add(resultText2).colspan(2).height(25).left();
      
      Group playerCardGroup = new Group();
      playerCardGroup.setName("Player Card Group");
      addActor(playerCardGroup);
      
      addMiniScoreboardToStage();

      addScoreboardToStage();

      addLineupTextToStage();
   }

   public void throwHButtonClicked() {
      removeSetupForThrowCheck();
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      throwButton.setVisible(true);
      Label promptText = (Label) getRoot().findActor("Prompt Text");
      promptText.setText("Throw Home");
      disableButtons();
      game.scheduleThrowRoll();
   }

   public void throw3ButtonClicked() {
      removeSetupForThrowCheck();
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      throwButton.setVisible(true);
      Label promptText = (Label) getRoot().findActor("Prompt Text");
      promptText.setText("Throw to 3rd");
      disableButtons();
      game.scheduleThrowRoll();
   }

   public void advanceBButtonClicked() {
      removeSetupForAdvancementCheck();
      Label promptText = (Label) getRoot().findActor("Prompt Text");
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
      Label promptText = (Label) getRoot().findActor("Prompt Text");
      promptText.setText("Throw to 3rd");
      disableButtons();
      game.scheduleThrowRoll();
   }

   public void advance3ButtonClicked() {
      game.checkForAdvancementRunner2 = false;
      removeSetupForAdvancementCheck();
      TextButton throwButton = (TextButton) getRoot().findActor("Throw Button");
      throwButton.setVisible(true);
      Label promptText = (Label) getRoot().findActor("Prompt Text");
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
   private void addMiniScoreboardToStage() {
      Table table = new Table();
      table.setBackground(new NinePatchDrawable(game.sd.boardNP));
      table.setName("Mini Scoreboard");
      table.setSize(200, 80);
      table.setPosition(700, 520);
      table.setTouchable(Touchable.enabled);
      table.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            Actor board = event.getTarget();
            if (board.getName().equals("Mini Scoreboard")) {
               for (Actor actor : ((Table)getRoot().findActor("Mini Scoreboard")).getChildren()) {
                  actor.setVisible(false);
               }
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
                     getRoot().findActor("Scoreboard").setVisible(true);
                     getRoot().findActor("Scoreboard").setZIndex(10000);
                     updateScoreboard();
                     updateBoxScoreStats();
                     updateGameLog();
                  }
               });
               board.addAction(sequence);
               board.setZIndex(10000);
               board.setName("Scoreboard Background");
            } else {
               getRoot().findActor("Scoreboard").setVisible(false);
               SequenceAction sequence = new SequenceAction();
               MoveToAction move = new MoveToAction();
               move.setPosition(700, 520);
               move.setDuration(MLBShowdown.ANIMATION_SPEED);
               SizeToAction size = new SizeToAction();
               size.setSize(200, 80);
               size.setDuration(MLBShowdown.ANIMATION_SPEED);
               ParallelAction parallel = new ParallelAction(move, size);
               sequence.addAction(parallel);
               sequence.addAction(new RunnableAction() {
                  @Override
                  public void run() {
                     for (Actor actor : ((Table)getRoot().findActor("Mini Scoreboard")).getChildren()) {
                        actor.setVisible(true);
                     }
                  }
               });
               board.addAction(sequence);
               board.setZIndex(10000);
               board.setName("Mini Scoreboard");
            }
         }
      });
      addActor(table);

      Image inningTopMarker = new Image(new Texture(Gdx.files.internal("images/triangle_yellow_up.png")));
      inningTopMarker.setName("Inning Top Marker");

      Image inningBottomMarker = new Image(new Texture(Gdx.files.internal("images/triangle_yellow_down.png")));
      inningBottomMarker.setName("Inning Bottom Marker");
      inningBottomMarker.setVisible(false);

      Label homeTeamText = new Label(game.homeTeam.nickName, sd.skin, "aero15");
      homeTeamText.setName("Home Team Mini Scoreboard");
      homeTeamText.setColor(Color.WHITE);

      Label awayTeamText = new Label(game.awayTeam.nickName, sd.skin, "aero15");
      awayTeamText.setName("Away Team Mini Scoreboard");
      awayTeamText.setColor(Color.WHITE);

      Label inningText = new Label("1", sd.skin, "muro45");
      inningText.setName("Inning Text");
      inningText.setColor(Color.WHITE);
      
      Label homeScoreText = new Label(Integer.toString(game.homeScore), sd.skin, "muro50"); 
      homeScoreText.setName("Home Score");
      homeScoreText.setColor(Color.WHITE);

      Label awayScoreText = new Label(Integer.toString(game.awayScore), sd.skin, "muro50");
      awayScoreText.setName("Away Score");
      awayScoreText.setColor(Color.WHITE);

      Label outsText = new Label("OUTS", sd.skin, "aero15");
      outsText.setName("Outs");
      outsText.setColor(Color.WHITE);

      Image outMarker1 = new Image(sd.blackCircle);
      outMarker1.setName("Out Marker 1");

      Image outMarker2 = new Image(sd.blackCircle);
      outMarker2.setName("Out Marker 2");
      
      Table leftVGroup = new Table();
      leftVGroup.add(awayScoreText).bottom().height(50);
      leftVGroup.row();
      leftVGroup.add(awayTeamText).center();
      table.add(leftVGroup).width(65).center();
      Table middleVGroup = new Table();
      Table middleHGroup1 = new Table();
      middleVGroup.add(middleHGroup1).colspan(3).height(45);
      Table middleVGroup1 = new Table();
      middleVGroup1.add(inningTopMarker).width(16).height(8).padLeft(-3);
      middleVGroup1.row();
      middleVGroup1.add(inningBottomMarker).width(16).height(8).padLeft(-3);
      middleHGroup1.add(inningText);
      middleHGroup1.add(middleVGroup1).padLeft(5);
      middleVGroup.row();
      middleVGroup.add(outsText);
      middleVGroup.add(outMarker1).size(8).padLeft(2);
      middleVGroup.add(outMarker2).size(8).padLeft(2);
      table.add(middleVGroup).width(70).center();
      Table rightVGroup = new Table();
      rightVGroup.add(homeScoreText).bottom().height(50);
      rightVGroup.row();
      rightVGroup.add(homeTeamText).center();
      table.add(rightVGroup).width(65).center();
   }

   /**
    * Adds the scoreboard and stats to stage
    */
   private void addScoreboardToStage() {
      Group scoreboard = new Group();
      scoreboard.setVisible(false);
      scoreboard.setName("Scoreboard");
      addActor(scoreboard);
      
      Table scoreboardTable = new Table();
      scoreboardTable.setName("Scoreboard Table");
      scoreboardTable.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("images/scoreboard_grid.png")))));
      scoreboardTable.setPosition(45, 470);
      scoreboardTable.setSize(810, 125);
      scoreboard.addActor(scoreboardTable);
      scoreboardTable.add().width(190).height(50);
      
      String[] headers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "R", "H", "E" };
      for (String str: headers) {
         Label inningNum = new Label(str, sd.skin, "muro50");
         inningNum.setName("Scoreboard Header " + str);
         inningNum.setAlignment(Align.center);
         inningNum.setColor(Color.WHITE);
         if (str.equals("9")) {
            scoreboardTable.add(inningNum).center().width(50).height(50).padRight(20);
         } else {
            scoreboardTable.add(inningNum).center().width(50).height(50);
         }
      }
      
      scoreboardTable.row();
      
      Label awayTeam = new Label(game.awayTeam.nickName, sd.skin, "aero40");
      awayTeam.setName("Away Team Nickname");
      awayTeam.setAlignment(Align.right);
      awayTeam.setColor(Color.WHITE);
      scoreboardTable.add(awayTeam).width(190).height(38);
      
      for (String str : headers) {
         Label awayInningScore = new Label("", sd.skin, "muro40");
         awayInningScore.setName("Away " + str);
         awayInningScore.setAlignment(Align.center);
         awayInningScore.setColor(Color.WHITE);
         if (str.equals("9")) {
            scoreboardTable.add(awayInningScore).center().width(50).height(38).padRight(20);
         } else {
            scoreboardTable.add(awayInningScore).center().width(50).height(38);
         }
      }
      
      scoreboardTable.row();

      Label homeTeam = new Label(game.homeTeam.nickName, sd.skin, "aero40");
      homeTeam.setName("Home Team Nickname");
      homeTeam.setAlignment(Align.right);
      homeTeam.setColor(Color.WHITE);
      scoreboardTable.add(homeTeam).width(190).height(38);

      for (String str : headers) {
         Label homeInningScore = new Label("", sd.skin, "muro40");
         homeInningScore.setName("Home " + str);
         homeInningScore.setAlignment(Align.center);
         homeInningScore.setColor(Color.WHITE);
         if (str.equals("9")) {
            scoreboardTable.add(homeInningScore).center().width(50).height(38).padRight(20);
         } else {
            scoreboardTable.add(homeInningScore).center().width(50).height(38);
         }
      }
      
      TabContainer boxScoreContainer = new TabContainer(sd.skin);
      boxScoreContainer.setBackground(new NinePatchDrawable(sd.boardNP));
      Tab boxScore = new Tab("Box Score", boxScoreContainer, sd.skin);
      boxScore.setHeight(20);
      TabContainer gameLogContainer = new TabContainer(sd.skin);
      gameLogContainer.setBackground(new NinePatchDrawable(sd.boardNP));
      Tab gameLog = new Tab("Game Log", gameLogContainer, sd.skin);
      gameLog.setHeight(20);
      TabPane tabPane = new TabPane(sd.skin);
      tabPane.addTab(boxScore);
      tabPane.addTab(gameLog);
      tabPane.setPosition(5, 5);
      tabPane.setSize(890, 465);
      scoreboard.addActor(tabPane);
      
      setupBoxScore(boxScore);

      setupGameLog(gameLog);
   }

   private void setupBoxScore(Tab boxScore) {
      TabContainer container = boxScore.getContainer();
      Table table = new Table();
      ScrollPane scrollPane = new ScrollPane(table);
      scrollPane.setScrollBarPositions(false, true);
      scrollPane.setScrollingDisabled(true, false);
      container.add(scrollPane).fill().expand();
      scrollPane.setFillParent(true);
      
      Table awayBatting = new Table();
      awayBatting.setName("Away Batting Stats Table");
      table.add(awayBatting).pad(30);
      Label awayBattingLabel = new Label(game.awayTeam.nickName + " Batting", sd.skin, "aero20");
      awayBattingLabel.setName("Away Batting Label");
      awayBattingLabel.setColor(Color.WHITE);
      awayBattingLabel.setAlignment(Align.left);
      awayBatting.add(awayBattingLabel).width(150);
      
      for (String str : battingStats) {
         Label header = new Label(str, sd.skin, "aero20");
         header.setName("Away Batting Stat Header " + str);
         header.setAlignment(Align.center);
         header.setColor(Color.WHITE);
         awayBatting.add(header).padLeft(4).padRight(4);
      }
      
      for (int i = 1; i < 10; i++) {
         addBatterStatRow(awayBatting, "Away", i);
      }
      
      Table homeBatting = new Table();
      homeBatting.setName("Home Batting Stats Table");
      table.add(homeBatting).pad(30);
      Label homeBattingLabel = new Label(game.homeTeam.nickName + " Batting", sd.skin, "aero20");
      homeBattingLabel.setName("Home Batting Label");
      homeBattingLabel.setColor(Color.WHITE);
      homeBattingLabel.setAlignment(Align.left);
      homeBatting.add(homeBattingLabel).width(150);
      
      for (String str : battingStats) {
         Label header = new Label(str, sd.skin, "aero20");
         header.setName("Home Batting Stat Header " + str);
         header.setAlignment(Align.center);
         header.setColor(Color.WHITE);
         homeBatting.add(header).padLeft(4).padRight(4);
      }
      
      for (int i = 1; i < 10; i++) {
         addBatterStatRow(homeBatting, "Home", i);
      }
      
      table.row();
      
      Table awayPitching = new Table();
      awayPitching.setName("Away Pitching Stats Table");
      table.add(awayPitching).pad(30);;
      Label awayPitchingLabel = new Label(game.awayTeam.nickName + " Pitching", sd.skin, "aero20");
      awayPitchingLabel.setName("Away Pitching Label");
      awayPitchingLabel.setColor(Color.WHITE);
      awayPitchingLabel.setAlignment(Align.left);
      awayPitching.add(awayPitchingLabel).width(150);
      
      for (String str : pitchingStats) {
         Label header = new Label(str, sd.skin, "aero20");
         header.setName("Away Pitching Stat Header " + str);
         header.setAlignment(Align.center);
         header.setColor(Color.WHITE);
         awayPitching.add(header).padLeft(4).padRight(4);
      }
      
      addPitcherStatRow(awayPitching, "Away", 1);
      
      Table homePitching = new Table();
      homePitching.setName("Home Pitching Stats Table");
      table.add(homePitching).pad(30);;
      Label homePitchingLabel = new Label(game.homeTeam.nickName + " Pitching", sd.skin, "aero20");
      homePitchingLabel.setName("Home Pitching Label");
      homePitchingLabel.setColor(Color.WHITE);
      homePitchingLabel.setAlignment(Align.left);
      homePitching.add(homePitchingLabel).width(150);
      
      for (String str : pitchingStats) {
         Label header = new Label(str, sd.skin, "aero20");
         header.setName("Home Pitching Stat Header " + str);
         header.setAlignment(Align.center);
         header.setColor(Color.WHITE);
         homePitching.add(header).padLeft(4).padRight(4);
      }
      
      addPitcherStatRow(homePitching, "Home", 1);
   }
   
   private void setupGameLog(Tab gameLog) {
      TabContainer container = gameLog.getContainer();
      Label gameLogLabel = new Label("", sd.skin, "aero20");
      gameLogLabel.setName("Game Log Label");
      gameLogLabel.setAlignment(Align.left);
      ScrollPane scrollPane = new ScrollPane(gameLogLabel);
      scrollPane.setScrollBarPositions(false, true);
      scrollPane.setScrollingDisabled(true, false);
      container.add(scrollPane).fill().expand();
   }
   
   private void updateGameLog() {
      ((Label)getRoot().findActor("Game Log Label")).setText(game.gameLog.toString());
   }

   private void updateScoreboard() {
      String[] headers = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "R", "H", "E" };
      int i = 0;
      for (String str : headers) {
         Label homeInningScore = (Label) getRoot().findActor("Home " + str);
         if (game.homeScorePerInning.size() > i) {
            homeInningScore.setText(Integer.toString(game.homeScorePerInning.get(i)));
         } else if (str.equals("R")) {
            homeInningScore.setText(Integer.toString(game.homeScore));
         } else if (str.equals("H")) {
            homeInningScore.setText(Integer.toString(game.homeHits));
         } else if (str.equals("E")) {
            homeInningScore.setText(Integer.toString(game.homeErrors));
         }
         Label awayInningScore = (Label) getRoot().findActor("Away " + str);
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
   
   private void updateBoxScoreStats() {
      // Away Batting
      List<Card> cards = game.awayTeam.getBattingStats();
      int rows = ((Table)getRoot().findActor("Away Batting Stats Table")).getCells().size()/9;
      int i = 1;
      while (cards.size() > rows) {
         addBatterStatRow((Table)getRoot().findActor("Away Batting Stats Table"), "Away", rows + i);
         i++;
      }
      i = 1;
      for (Card card : cards) {
         updateBatterStatRow("Away", i, card);
         i++;
      }
      
      // Away Pitching
      cards = game.awayTeam.getPitchingStats();
      rows = ((Table)getRoot().findActor("Away Pitching Stats Table")).getCells().size()/9;
      i = 1;
      while (cards.size() > rows) {
         addPitcherStatRow((Table)getRoot().findActor("Away Pitching Stats Table"), "Away", rows + i);
         i++;
      }
      i = 1;
      for (Card card : cards) {
         updatePitcherStatRow("Away", i, card);
         i++;
      }
      
      // Home Batting
      cards = game.homeTeam.getBattingStats();
      rows = ((Table)getRoot().findActor("Home Batting Stats Table")).getCells().size()/9;
      i = 1;
      while (cards.size() > rows) {
         addBatterStatRow((Table)getRoot().findActor("Home Batting Stats Table"), "Home", rows + i);
         i++;
      }
      i = 1;
      for (Card card : cards) {
         updateBatterStatRow("Home", i, card);
         i++;
      }
      
      // Home Pitching
      cards = game.homeTeam.getPitchingStats();
      rows = ((Table)getRoot().findActor("Home Pitching Stats Table")).getCells().size()/9;
      i = 1;
      while (cards.size() > rows) {
         addPitcherStatRow((Table)getRoot().findActor("Home Pitching Stats Table"), "Home", rows + i);
         i++;
      }
      i = 1;
      for (Card card : cards) {
         updatePitcherStatRow("Home", i, card);
         i++;
      }
   }

   private void updateBatterStatRow(String homeAway, int i, Card card) {
      Group scoreboard = (Group) getRoot().findActor("Scoreboard");
      Label name = (Label) scoreboard.findActor(homeAway + " Batting Name " + i);
      name.setText(card.name);
      for (String str : battingStats) {
         Label stat = (Label) scoreboard.findActor(homeAway + " Batting " + str + " " + i);
         stat.setText(game.statKeeper.getBattingStat(card.gameStats, str));
      }
   }

   private void updatePitcherStatRow(String homeAway, int i, Card card) {
      Group scoreboard = (Group) getRoot().findActor("Scoreboard");
      Label name = (Label) scoreboard.findActor(homeAway + " Pitching Name " + i);
      name.setText(card.name);
      for (String str : pitchingStats) {
         Label stat = (Label) scoreboard.findActor(homeAway + " Pitching " + str + " " + i);
         stat.setText(game.statKeeper.getPitchingStat(card.gameStats, str));
      }
   }

   private void addBatterStatRow(Table table, String homeAway, int i) {
      table.row();
      Label name = new Label("", sd.skin, "aero16");
      name.setName(homeAway + " Batting Name " + i);
      name.setColor(Color.WHITE);
      name.setAlignment(Align.left);
      table.add(name).width(150);

      for (String str : battingStats) {
         Label stat = new Label("", sd.skin, "muro16");
         stat.setName(homeAway + " Batting " + str + " " + i);
         stat.setAlignment(Align.center);
         stat.setColor(Color.WHITE);
         table.add(stat).padLeft(4).padRight(4);
      }
   }

   private void addPitcherStatRow(Table table, String homeAway, int i) {
      table.row();
      Label name = new Label("", sd.skin, "aero16");
      name.setName(homeAway + " Pitching Name " + i);
      name.setColor(Color.WHITE);
      name.setAlignment(Align.left);
      table.add(name).width(150);

      for (String str : pitchingStats) {
         Label stat = new Label("", sd.skin, "muro16");
         stat.setName(homeAway + " Pitching " + str + " " + i);
         stat.setColor(Color.WHITE);
         stat.setAlignment(Align.center);
         table.add(stat).padLeft(4).padRight(4);
      }
   }

   /**
    * Adds the lineup text to the stage
    */
   private void addLineupTextToStage() {
      Group lineup = new Group();
      lineup.setName("Lineup Group");
      addActor(lineup);
      
      Table table = new Table();
      table.setName("Lineup Table");
      table.setBackground(new NinePatchDrawable(game.sd.boardNP));
      table.setPosition(0, 0);
      table.setSize(155, 145);
      table.center();
      table.addListener(new InputListener() {
         @Override
         public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            if (fromActor == null || !fromActor.getName().equals("Lineup Card")) {
               game.timer.scheduleTask(new Task() {
                  @Override
                  public void run() {
                     getRoot().findActor("Lineup Card").setVisible(true);
                     getRoot().findActor("Lineup Card").setZIndex(10000);
                  }
               }, 2);
            }
         }
      });
      addActor(table);
      
      Table innerTable = new Table();
      table.add(innerTable).width(table.getWidth()).padLeft(5).left();

      for (int i = 0; i < 9; i++) {
         Label num = new Label(Integer.toString(i + 1), sd.skin, "aero15");
         num.setName("Lineup Num " + (i + 1));
         num.setAlignment(Align.center);
         innerTable.add(num).width(12).height(15).center();
         Label pos = new Label("", sd.skin, "aero15");
         pos.setName("Lineup Position " + (i + 1));
         pos.setAlignment(Align.center);
         innerTable.add(pos).width(24).height(15).center();
         Label name = new Label("", sd.skin, "aero15");
         name.setName("Lineup Name " + (i + 1));
         innerTable.add(name).expandX().height(15).left();
         innerTable.row();
      }

      addActor(new LineupCard());
      setLineupText();
   }
   
   @Override
   public void draw() {
      super.draw();
   }

   public void setupStageForAdvancementCheck() {
      Label promptText = (Label) getRoot().findActor("Prompt Text");
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
      Label promptText = (Label) getRoot().findActor("Prompt Text");
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
      Label promptText = (Label) getRoot().findActor("Prompt Text");
      if (game.checkForDoublePlay) {
         throwButton.setVisible(true);
         promptText.setText("Double Play Roll");
      } else {
         throwButton.setVisible(false);
         Label result2 = (Label) getRoot().findActor("Result Text2");
         Label throwText = (Label) getRoot().findActor("Throw Text");
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
         Label pos = (Label) getRoot().findActor("Lineup Position " + (i + 1));
         Label name = (Label) getRoot().findActor("Lineup Name " + (i + 1));
         pos.setText(team.getPosition(team.lineup.get(i)));
         name.setText(team.lineup.get(i).name);
      }
   }

   /**
    * Sets the pitch text based on the pitch roll
    */
   public void setPitchText() {
      Label text = (Label) getRoot().findActor("Pitch Text");
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
      Label text = (Label) getRoot().findActor("Swing Text");
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
      Label text = (Label) getRoot().findActor("Throw Text");
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
      Label text = (Label) getRoot().findActor("Chart Text");
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
      Label text = (Label) getRoot().findActor("Result Text");
      if ((game.isFieldingTeamUser() && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) <= 3)
            || (game.isBattingTeamUser() && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) > 3)) {
         text.setColor(Color.GREEN);
      } else {
         text.setColor(Color.RED);
      }
      text.setText(game.swing > 0 ? game.result : "");
   }

   public void setResultText2() {
      Label text = (Label) getRoot().findActor("Result Text2");
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
      Label text = (Label) getRoot().findActor("Inning Text");
      text.setText(Integer.toString(game.inning));
   }

   /**
    * Sets the out markers based on the number of outs
    */
   public void setOutMarkers() {
      Image marker1 = (Image) getRoot().findActor("Out Marker 1");
      Image marker2 = (Image) getRoot().findActor("Out Marker 2");
      if (game.outs == 0) {
         marker1.setDrawable(new SpriteDrawable(new Sprite(sd.blackCircle)));
         marker2.setDrawable(new SpriteDrawable(new Sprite(sd.blackCircle)));
      } else if (game.outs == 1) {
         marker1.setDrawable(new SpriteDrawable(new Sprite(sd.yellowCircle)));
         marker2.setDrawable(new SpriteDrawable(new Sprite(sd.blackCircle)));
      } else {
         marker1.setDrawable(new SpriteDrawable(new Sprite(sd.yellowCircle)));
         marker2.setDrawable(new SpriteDrawable(new Sprite(sd.yellowCircle)));
      }
   }

   /**
    * Sets the top or bottom arrow by the inning
    */
   public void setTopBottomInningMarker() {
      Image markert = (Image) getRoot().findActor("Inning Top Marker");
      Image markerb = (Image) getRoot().findActor("Inning Bottom Marker");
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
      Label home = (Label) getRoot().findActor("Home Score");
      Label away = (Label) getRoot().findActor("Away Score");
      home.setText(Integer.toString(game.homeScore));
      away.setText(Integer.toString(game.awayScore));
   }

   public void removePromptText() {
      Label promptText = (Label) getRoot().findActor("Prompt Text");
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
         pitchButton.setDisabled(true);
      } else {
         pitchButton.setTouchable(Touchable.enabled);
         pitchButton.setDisabled(false);
      }
      if (game.isBattingTeamCpu()) {
         swingButton.setTouchable(Touchable.disabled);
         swingButton.setDisabled(true);
      } else {
         swingButton.setTouchable(Touchable.enabled);
         swingButton.setDisabled(false);
      }
      if (game.isFieldingTeamCpu() && throwButton.isVisible()) {
         throwButton.setTouchable(Touchable.disabled);
         throwButton.setDisabled(true);
      } else {
         throwButton.setTouchable(Touchable.enabled);
         throwButton.setDisabled(false);
      }
      if (game.pitch > 0) {
         pitchButton.setTouchable(Touchable.disabled);
         pitchButton.setDisabled(true);
      }
      if (game.swing > 0 || game.pitch == 0) {
         swingButton.setTouchable(Touchable.disabled);
         swingButton.setDisabled(true);
      }
      if ((game.fieldingThrow > 0 || game.swing == 0) && throwButton.isVisible()) {
         throwButton.setTouchable(Touchable.disabled);
         swingButton.setDisabled(true);
      }
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
         lheight = getRoot().findActor("Lineup Table").getHeight();
         lwidth = getRoot().findActor("Lineup Table").getWidth();
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
               texture = cardActor.card.cardTexture;
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
         setZIndex(10000);
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
