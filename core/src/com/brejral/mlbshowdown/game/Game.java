package com.brejral.mlbshowdown.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.TextActor;
import com.brejral.mlbshowdown.card.CardActor;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.menu.MainMenu;
import com.brejral.mlbshowdown.stats.StatKeeper;
import com.brejral.mlbshowdown.team.Team;
import com.brejral.mlbshowdown.team.TeamLoader.TeamParameter;
import com.brejral.mlbshowdown.user.User;

public class Game {
   public final MLBShowdown sd;
   public GameScreen screen;
   public GameStage stage;
   public StatKeeper statKeeper;
   public Database db;
   public AssetManager manager;
   public Team awayTeam, homeTeam;
   public CardActor batter, pitcher, runner1, runner2, runner3;
   public int inning = 1, outs, homeScore, awayScore, homeHits, awayHits;
   public int homeErrors, awayErrors, pitch, swing, fieldingThrow;
   public int pitchCardBonus = 0, swingCardBonus = 0;
   boolean isTop = true, isPChart = true, isTopExtraInnings = false;
   public List<Integer> homeScorePerInning = new ArrayList<>();
   public List<Integer> awayScorePerInning = new ArrayList<>();
   public String result, result2;
   public Random rand;
   public Timer timer = new Timer();
   public boolean checkForDoublePlay = false, checkForAdvancement = false;
   public boolean checkForAdvancementRunner2 = false, checkForAdvancementRunner3 = false;

   /**
    * Creates a new game
    * 
    * @param showdown
    *           - the global MLBShowdown
    * @param assetManager
    *           - the asset manager
    */
   public Game(MLBShowdown showdown, AssetManager assetManager) {
      sd = showdown;
      manager = assetManager;
      db = sd.db;
      rand = new Random(System.currentTimeMillis());
      TeamParameter param = new TeamParameter(sd, new User(sd, true), "Mariners");
      manager.load("Away Team", Team.class, param);
      param = new TeamParameter(sd, sd.user, "Angels");
      manager.load("Home Team", Team.class, param);
      statKeeper = new StatKeeper(this);
      awayScorePerInning.add(0);
   }
   
   public void setTeams() {
      awayTeam = manager.get("Away Team");
      homeTeam = manager.get("Home Team");
   }

   /**
    * Set the game's starting parameters
    */
   public void startGame() {
      statKeeper.setUpArraysForGame();
      setBatter();
      setPitcher();
   }

   /**
    * Sets the pitcher to the current pitcher of the team
    */
   public void setPitcher() {
      removeCardVisibility(pitcher);
      pitcher = Pools.obtain(CardActor.class);
      stage.addActor(pitcher);
      if (isTop) {
         pitcher.setCardInfo(homeTeam.positions.get(1));
      } else {
         pitcher.setCardInfo(awayTeam.positions.get(1));
      }
      pitcher.setName("Pitcher");
      pitcher.setPosition(450f, 260f);
      pitcher.setScale(.2f);
      pitcher.setVisible(true);
      pitcher.setZIndex(50);
   }

   /**
    * Sets the batter to the card at the current lineup spot
    */
   public void setBatter() {
      Team team;
      if (isTop) {
         team = awayTeam;
      } else {
         team = homeTeam;
      }
      batter = Pools.obtain(CardActor.class);
      stage.addActor(batter);
      batter.setCardInfo(team.lineup.get(team.lineupSpot));
      batter.setName("Batter");
      batter.setPosition(450, 75);
      batter.setScale(.2f);
      batter.setVisible(true);
      batter.setZIndex(50);
   }

   /**
    * Sets the runner at first base to Card instance (can be null)
    * 
    * @param card
    *           - Card instance
    */
   public void setRunner1(CardActor card) {
      runner1 = card;
      if (runner1 != null) {
         runner1.setName("Runner1");
         runner1.setPosition(625, 260);
         runner1.setScale(.2f);
         runner1.setVisible(true);
         runner1.setZIndex(4);
      }
   }

   /**
    * Sets the runner at second base to Card instance (can be null)
    * 
    * @param card
    *           - Card instance
    */
   public void setRunner2(CardActor card) {
      runner2 = card;
      if (runner2 != null) {
         runner2.setName("Runner2");
         runner2.setPosition(450, 450);
         runner2.setScale(.2f);
         runner2.setVisible(true);
         runner2.setZIndex(3);
      }
   }

   /**
    * Sets the runner at third base to Card instance (can be null)
    * 
    * @param card
    *           - Card instance
    */
   public void setRunner3(CardActor card) {
      runner3 = card;
      if (runner3 != null) {
         runner3.setName("Runner3");
         runner3.setPosition(275, 260);
         runner3.setScale(.2f);
         runner3.setVisible(true);
         runner3.setZIndex(2);
      }
   }

   /**
    * Remove the visibility of the Card Actor
    * 
    * @param card
    *           - The Card to remove visibility from
    */
   public void removeCardVisibility(CardActor card) {
      if (card != null) {
         card.remove();
         Pools.free(card);
      }
   }

   /**
    * Performs all actions for the pitch roll
    */
   public void pitch() {
      rollPitch();
      setChart();
      stage.setPitchText();
      stage.setChartText();
      stage.disableButtons();
      scheduleSwingRoll();
   }

   /**
    * Performs all actions for the swing roll
    */
   public void swing() {
      rollSwing();
      getResult();
      stage.setSwingText();
      stage.setResultText();
      stage.disableButtons();
      scheduleActionsForResult();
   }

   public void fieldingThrow() {
      if (checkForDoublePlay) {
         rollForDoublePlay();
         getDoublePlayResult();
         stage.setThrowText();
         stage.setResultText2();
         stage.disableButtons();
         endFieldingCheck();
      } else if (checkForAdvancement) {
         rollForAdvancement();
         getAdvancementResult();
         stage.setThrowText();
         stage.setResultText2();
         stage.disableButtons();
         setActionsForResult2();
      }
   }

   /**
    * Generates a random number(1-20) for the pitch roll and adds control and
    * bonuses
    */
   public void rollPitch() {
      pitch = rand.nextInt(20) + 1 + pitcher.card.controlAdj + pitchCardBonus;
   }

   /**
    * Generates a random number(1-20) for the swing roll and adds bonuses
    */
   public void rollSwing() {
      swing = rand.nextInt(20) + 1 + swingCardBonus;
   }

   public void rollForDoublePlay() {
      fieldingThrow = rand.nextInt(20) + 1 + (isTop ? awayTeam.getInfieldBonus() : homeTeam.getInfieldBonus());
   }

   public void rollForAdvancement() {
      fieldingThrow = rand.nextInt(20) + 1 + (isTop ? awayTeam.getOutfieldBonus() : homeTeam.getOutfieldBonus());
   }

   /**
    * Sets the chart to select the result from; based on the pitch roll and the
    * batter's onbase
    */
   public void setChart() {
      if (pitch > batter.card.onbase) {
         isPChart = true;
      } else {
         isPChart = false;
      }
   }

   /**
    * Gets the result from the swing based on which chart to use
    */
   public void getResult() {
      if (isPChart) {
         getResultFromChart(pitcher.card.chart);
      } else {
         getResultFromChart(batter.card.chart);
      }
      if (MLBShowdown.DEV_MODE) {
         result = "FB";
      }
   }

   public void getDoublePlayResult() {
      if (fieldingThrow > batter.card.speed) {
         result2 = "Double Play";
      } else {
         result2 = "Safe at First";
      }
   }

   public void getAdvancementResult() {
      TextActor promptText = (TextActor) stage.getRoot().findActor("Prompt Text");
      int speedAdj = batter.card.speed + (outs == 2 && !result.equals("FB") ? 5 : 0) + (promptText.getText().equals("Throw Home") ? 5 : 0);
      if (fieldingThrow > speedAdj) {
         result2 = promptText.getText().equals("Throw Home") ? "Out at Home" : "Out at 3rd";
      } else {
         result2 = promptText.getText().equals("Throw Home") ? "Safe at Home" : "Safe at 3rd";
      }
   }

   /**
    * Gets the result from the chart based off the swing and sets the result
    * string
    * 
    * @param chart
    *           - int array of size 10; chart to get the result from
    */
   public void getResultFromChart(int[] chart) {
      int index = 0;
      for (int i = 0; i < chart.length; i++) {
         if (i == chart.length - 1) {
            index = i;
            break;
         } else if (swing <= chart[i]) {
            index = i;
            break;
         }
      }
      result = CardConstants.CHART_TEXT[index];
   }

   /**
    * Gives actions to the players based on what the result is
    */
   public void setActionsForResult() {
      switch (result) {
      case "PU":
         checkEndOfAnimation();
         break;
      case "SO":
         checkEndOfAnimation();
         break;
      case "GB":
         batter.addAction(CardConstants.getRunToFirstAction(this, batter));
         if (runner1 != null) {
            runner1.addAction(CardConstants.getRunToSecondAction(this, runner1));
         }
         if (runner2 != null) {
            runner2.addAction(CardConstants.getRunToThirdAction(this, runner2));
         }
         if (runner3 != null) {
            runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
         }
         break;
      case "FB":
         if ((runner2 != null || runner3 != null) && outs < 2) {
            checkForAdvancement = true;
            checkForAdvancementRunner2 = (runner2 != null);
            checkForAdvancementRunner3 = (runner3 != null);
         }
         checkEndOfAnimation();
         break;
      case "BB":
         batter.addAction(CardConstants.getRunToFirstAction(this, batter));
         if (runner1 != null) {
            runner1.addAction(CardConstants.getRunToSecondAction(this, runner1));
         }
         if (runner2 != null && runner1 != null) {
            runner2.addAction(CardConstants.getRunToThirdAction(this, runner2));
         }
         if (runner3 != null && runner2 != null && runner1 != null) {
            runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
         }
         break;
      case "1B":
         batter.addAction(CardConstants.getRunToFirstAction(this, batter));
         if (runner1 != null) {
            checkForAdvancement = true;
            checkForAdvancementRunner2 = true;
            runner1.addAction(CardConstants.getRunToSecondAction(this, runner1));
         }
         if (runner2 != null) {
            checkForAdvancement = true;
            checkForAdvancementRunner3 = true;
            runner2.addAction(CardConstants.getRunToThirdAction(this, runner2));
         }
         if (runner3 != null) {
            runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
         }
         break;
      case "1B+":
         if (runner1 == null) {
            batter.addAction(CardConstants.getRunToSecondAction(this, batter));
         } else {
            batter.addAction(CardConstants.getRunToFirstAction(this, batter));
         }
         if (runner1 != null) {
            checkForAdvancement = true;
            checkForAdvancementRunner2 = true;
            runner1.addAction(CardConstants.getRunToSecondAction(this, runner1));
         }
         if (runner2 != null) {
            checkForAdvancement = true;
            checkForAdvancementRunner3 = true;
            runner2.addAction(CardConstants.getRunToThirdAction(this, runner2));
         }
         if (runner3 != null) {
            runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
         }
         break;
      case "2B":
         batter.addAction(CardConstants.getRunToSecondAction(this, batter));
         if (runner1 != null) {
            checkForAdvancement = true;
            checkForAdvancementRunner3 = true;
            runner1.addAction(CardConstants.getRunToThirdAction(this, runner1));
         }
         if (runner2 != null) {
            runner2.addAction(CardConstants.getRunToHomeAction(this, runner2));
         }
         if (runner3 != null) {
            runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
         }
         break;
      case "3B":
         batter.addAction(CardConstants.getRunToThirdAction(this, batter));
         if (runner1 != null) {
            runner1.addAction(CardConstants.getRunToHomeAction(this, runner1));
         }
         if (runner2 != null) {
            runner2.addAction(CardConstants.getRunToHomeAction(this, runner2));
         }
         if (runner3 != null) {
            runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
         }
         break;
      case "HR":
         batter.addAction(CardConstants.getRunToHomeAction(this, batter));
         if (runner1 != null) {
            runner1.addAction(CardConstants.getRunToHomeAction(this, runner1));
         }
         if (runner2 != null) {
            runner2.addAction(CardConstants.getRunToHomeAction(this, runner2));
         }
         if (runner3 != null) {
            runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
         }
         break;
      }
   }

   /**
    * Updates the Game fields based on the result
    */
   public void updateGameFromResult() {
      statKeeper.increaseStat(batter, "PA");
      statKeeper.increaseStat(pitcher, "BF");
      switch (result) {
      case "PU":
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "PU");
         statKeeper.increaseStat(pitcher, "OUTS");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "PUP");
         removeCardVisibility(batter);
         addOut();
         break;
      case "SO":
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "SO");
         statKeeper.increaseStat(pitcher, "OUTS");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "SOP");
         removeCardVisibility(batter);
         addOut();
         break;
      case "GB":
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "GB");
         statKeeper.increaseStat(pitcher, "OUTS");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "GBP");
         if (runner3 != null) {
            if (outs < 1 || (outs < 2 && !checkForDoublePlay) || (outs == 1 && !result2.equals("Double Play"))) {
               statKeeper.increaseStat(runner3, "R");
               statKeeper.increaseStat(pitcher, "RP");
               statKeeper.increaseStat(pitcher, "ER");
               if (!result2.equals("Double Play")) {
                  statKeeper.increaseStat(batter, "RBI");
               }
               addScore();
            }
            removeCardVisibility(runner3);
         }
         setRunner3(runner2);
         if (runner1 == null || outs == 2) {
            removeCardVisibility(batter);
            setRunner2(runner1);
            setRunner1(null);
         } else {
            removeCardVisibility(runner1);
            setRunner2(null);
            if (result2.equals("Double Play")) {
               removeCardVisibility(batter);
               setRunner1(null);
            } else {
               setRunner1(batter);
            }
         }
         addOut();
         if (checkForDoublePlay && result2.equals("Double Play")) {
            statKeeper.increaseStat(pitcher, "OUTS");
            addOut();
         }
         break;
      case "FB":
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "FB");
         statKeeper.increaseStat(pitcher, "OUTS");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "FBP");
         removeCardVisibility(batter);
         addOut();
         break;
      case "BB":
         statKeeper.increaseStat(batter, "BB");
         statKeeper.increaseStat(pitcher, "BBP");
         if (runner3 != null && runner2 != null && runner1 != null) {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner3);
            addScore();
         }
         if (runner2 != null && runner1 != null) {
            setRunner3(runner2);
         }
         if (runner1 != null) {
            setRunner2(runner1);
         }
         setRunner1(batter);
         break;
      case "1B":
         addHit();
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "SINGLE");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "SINGLEP");
         if (runner3 != null) {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner3);
            addScore();
         }
         setRunner3(runner2);
         setRunner2(runner1);
         setRunner1(batter);
         break;
      case "1B+":
         addHit();
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "SINGLE");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "SINGLEP");
         if (runner3 != null) {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner3);
            addScore();
         }
         setRunner3(runner2);
         setRunner2(runner1);
         if (runner2 != null) {
            setRunner1(batter);
         } else {
            setRunner2(batter);
            setRunner1(null);
         }
         break;
      case "2B":
         addHit();
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "DOUBLE");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "DOUBLEP");
         if (runner3 != null) {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner3);
            addScore();
         }
         if (runner2 != null) {
            statKeeper.increaseStat(runner2, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner2);
            addScore();
         }
         setRunner3(runner1);
         setRunner2(batter);
         setRunner1(null);
         break;
      case "3B":
         addHit();
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "TRIPLE");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "TRIPLEP");
         if (runner3 != null) {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner3);
            addScore();
         }
         if (runner2 != null) {
            statKeeper.increaseStat(runner2, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner2);
            addScore();
         }
         if (runner1 != null) {
            statKeeper.increaseStat(runner1, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner1);
            addScore();
         }
         setRunner3(batter);
         setRunner2(null);
         setRunner1(null);
         break;
      case "HR":
         addHit();
         statKeeper.increaseStat(batter, "AB");
         statKeeper.increaseStat(batter, "HR");
         statKeeper.increaseStat(pitcher, "ABP");
         statKeeper.increaseStat(pitcher, "HRP");
         if (runner3 != null) {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner3);
            addScore();
         }
         if (runner2 != null) {
            statKeeper.increaseStat(runner2, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner2);
            addScore();
         }
         if (runner1 != null) {
            statKeeper.increaseStat(runner1, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner1);
            addScore();
         }
         statKeeper.increaseStat(batter, "R");
         statKeeper.increaseStat(pitcher, "RP");
         statKeeper.increaseStat(pitcher, "ER");
         statKeeper.increaseStat(batter, "RBI");
         removeCardVisibility(batter);
         addScore();
         setRunner3(null);
         setRunner2(null);
         setRunner1(null);
         break;
      }
      statKeeper.executeGameStatChanges();
      int ipOld = pitcher.card.ipAdj;
      pitcher.card.setIP();
      if (pitcher.card.ipAdj != ipOld) {
         pitcher.createTexture();
      }
   }

   public void setActionsForResult2() {
      if (checkForAdvancementRunner2) {
         runner2.addAction(CardConstants.getRunToThirdAction(this, runner2));
      }
      if (checkForAdvancementRunner3) {
         runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
      }
   }

   public void updateGameFromResult2() {
      if (checkForAdvancementRunner2 && checkForAdvancementRunner3) {
         if (result2.equals("Out at 3rd")) {
            if (outs != 2 || runner2.card.speed <= runner3.card.speed) {
               statKeeper.increaseStat(runner3, "R");
               statKeeper.increaseStat(pitcher, "RP");
               statKeeper.increaseStat(pitcher, "ER");
               statKeeper.increaseStat(batter, "RBI");
               removeCardVisibility(runner3);
               addScore();
               if (result.equals("FB")) {
                  statKeeper.increaseStat(batter, "AB", -1);
                  statKeeper.increaseStat(batter, "SAC");
               }
            }
            statKeeper.increaseStat(pitcher, "OUTS");
            removeCardVisibility(runner2);
            addOut();
         } else if (result2.equals("Out at Home")) {
            statKeeper.increaseStat(pitcher, "OUTS");
            removeCardVisibility(runner3);
            setRunner3(runner2);
            addOut();
         } else {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            removeCardVisibility(runner3);
            addScore();
            if (result.equals("FB")) {
               statKeeper.increaseStat(batter, "AB", -1);
               statKeeper.increaseStat(batter, "SAC");
            }
            setRunner3(runner2);
         }
         setRunner2(null);
      } else if (checkForAdvancementRunner3) {
         if (result2.equals("Out at Home")) {
            statKeeper.increaseStat(pitcher, "OUTS");
            addOut();
         } else {
            statKeeper.increaseStat(runner3, "R");
            statKeeper.increaseStat(pitcher, "RP");
            statKeeper.increaseStat(pitcher, "ER");
            statKeeper.increaseStat(batter, "RBI");
            addScore();
            if (result.equals("FB")) {
               statKeeper.increaseStat(batter, "AB", -1);
               statKeeper.increaseStat(batter, "SAC");
            }
         }
         removeCardVisibility(runner3);
         setRunner3(null);
      } else if (checkForAdvancementRunner2) {
         if (result2.equals("Out at 3rd")) {
            statKeeper.increaseStat(pitcher, "OUTS");
            removeCardVisibility(runner2);
            addOut();
         } else {
            setRunner3(runner2);
         }
         setRunner2(null);
      }
      statKeeper.executeGameStatChanges();
   }

   /**
    * Resets fields for the next at-bat
    */
   public void resetForNextAB() {
      pitch = 0;
      swing = 0;
      fieldingThrow = 0;
      result = "";
      result2 = "";
      stage.setPitchText();
      stage.setChartText();
      stage.setSwingText();
      stage.setResultText();
      stage.setResultText2();
      stage.setThrowText();
      stage.removePromptText();
      setNewBatter();
      stage.disableButtons();
      schedulePitchRoll();
   }

   /**
    * Sets the next lineup spot number
    */
   public void nextLineupSpot() {
      if (isTop) {
         awayTeam.nextLineupSpot();
      } else {
         homeTeam.nextLineupSpot();
      }
   }

   /**
    * Sets the new batter for the next at-bat
    */
   public void setNewBatter() {
      setBatter();
      stage.setLineupHighlight();
   }

   /**
    * Checks if there are any actions left on the runner from animation
    */
   public void checkEndOfAnimation() {
      if (batter.getActions().size > 0) {
         return;
      }
      if (runner1 != null && runner1.getActions().size > 0) {
         return;
      }
      if (runner2 != null && runner2.getActions().size > 0) {
         return;
      }
      if (runner3 != null && runner3.getActions().size > 0) {
         return;
      }
      updateGameFromResult();
      if (checkForDoublePlay) {
         checkForDoublePlay = false;
         stage.setupStageForDoublePlayCheck();
      }
      if (!checkForAdvancement || outs > 2) {
         advanceAtBat();
      } else {
         stage.setupStageForAdvancementCheck();
         System.out.println("Check For Advancement");
      }
   }

   public void checkEndOfAdvanceAnimation() {
      if (runner2 != null && runner2.getActions().size > 0) {
         return;
      }
      if (runner3 != null && runner3.getActions().size > 0) {
         return;
      }
      updateGameFromResult2();
      checkForAdvancement = false;
      checkForAdvancementRunner2 = false;
      checkForAdvancementRunner3 = false;
      advanceAtBat();
   }

   public void endFieldingCheck() {
      if (checkForDoublePlay) {
         setActionsForResult();
      } else if (checkForAdvancement) {
         setActionsForResult2();
      }
   }

   public void advanceAtBat() {
      System.out.println("Advance At Bat");
      nextLineupSpot();
      checkEndOfInning();
      resetForNextAB();
   }

   public void schedulePitchRoll() {
      TextButton button = (TextButton) stage.getRoot().findActor("Pitch Button");
      if (pitch == 0 && !button.isTouchable()) {
         timer.scheduleTask(new Task() {
            @Override
            public void run() {
               pitch();
               this.cancel();
            }
         }, MLBShowdown.TASK_SPEED);
      }
   }

   public void scheduleSwingRoll() {
      TextButton button = (TextButton) stage.getRoot().findActor("Swing Button");
      if (swing == 0 && pitch != 0 && !button.isTouchable()) {
         timer.scheduleTask(new Task() {
            @Override
            public void run() {
               swing();
               this.cancel();
            }
         }, MLBShowdown.TASK_SPEED);
      }
   }

   public void scheduleThrowRoll() {
      TextButton button = (TextButton) stage.getRoot().findActor("Throw Button");
      if ((checkForAdvancement || checkForDoublePlay) && !button.isTouchable()) {
         timer.scheduleTask(new Task() {
            @Override
            public void run() {
               fieldingThrow();
               this.cancel();
            }
         }, MLBShowdown.TASK_SPEED);
      }
   }

   public void scheduleActionsForResult() {
      timer.scheduleTask(new Task() {
         @Override
         public void run() {
            if (result.equals("GB") && outs < 2 && runner1 != null) {
               checkForDoublePlay = true;
               stage.setupStageForDoublePlayCheck();
               stage.disableButtons();
               scheduleThrowRoll();
            } else {
               setActionsForResult();
            }
            this.cancel();
         }
      }, MLBShowdown.TASK_SPEED);
   }

   /**
    * Checks for the end of the inning
    */
   public void checkEndOfInning() {
      if (outs > 2) {
         outs = 0;
         stage.setOutMarkers();
         removeCardVisibility(runner1);
         removeCardVisibility(runner2);
         removeCardVisibility(runner3);
         setRunner1(null);
         setRunner2(null);
         setRunner3(null);
         isTop = !isTop;
         stage.setTopBottomInningMarker();
         stage.disableButtons();
         stage.setLineupText();
         if (isTop) {
            inning++;
            stage.setInningText();
            if (inning > 9) {
               isTopExtraInnings = true;
            }
            awayScorePerInning.add(0);
         } else {
            homeScorePerInning.add(0);
         }
         setPitcher();
      }
      checkEndOfGame();
   }

   /**
    * Checks for the end of the game
    */
   public void checkEndOfGame() {
      if (inning > 9 && isTopExtraInnings) {
         isTopExtraInnings = false;
         if (awayScore > homeScore) {
            endGame();
         } else {
         }
      }
      if (!isTop && inning > 8) {
         if (homeScore > awayScore) {
            endGame();
         }
      }
   }

   /**
    * Ends the game
    */
   public void endGame() {
      sd.setScreen(new MainMenu(sd));
   }

   /**
    * Adds an out and updates the screen
    */
   public void addOut() {
      outs++;
      stage.setOutMarkers();
   }

   /**
    * Adds a run to the away or home score
    */
   public void addScore() {
      if (isTop) {
         awayScore++;
         int score = awayScorePerInning.get(inning - 1);
         awayScorePerInning.set(inning - 1, ++score);
      } else {
         homeScore++;
         int score = homeScorePerInning.get(inning - 1);
         homeScorePerInning.set(inning - 1, ++score);
      }
      stage.setScoreText();
   }

   public void addHit() {
      if (isTop) {
         awayHits++;
      } else {
         homeHits++;
      }
   }

   public boolean isFieldingTeamCpu() {
      return (isTop && !sd.user.equals(homeTeam.user)) || (!isTop && !sd.user.equals(awayTeam.user));
   }

   public boolean isFieldingTeamUser() {
      return (isTop && sd.user.equals(homeTeam.user)) || (!isTop && sd.user.equals(awayTeam.user));
   }

   public boolean isBattingTeamCpu() {
      return (isTop && !sd.user.equals(awayTeam.user)) || (!isTop && !sd.user.equals(homeTeam.user));
   }

   public boolean isBattingTeamUser() {
      return (isTop && sd.user.equals(awayTeam.user)) || (!isTop && sd.user.equals(homeTeam.user));
   }

   public boolean isCardZoomed() {
      return (pitcher.isZoomed && !pitcher.hasActions()) || (batter.isZoomed && !batter.hasActions()) || (runner1 != null && runner1.isZoomed && !runner1.hasActions())
            || (runner2 != null && runner2.isZoomed && !runner2.hasActions()) || (runner3 != null && runner3.isZoomed && !runner3.hasActions());
   }

   public CardActor getZoomedCard() {
      if (pitcher.isZoomed && !pitcher.hasActions()) {
         return pitcher;
      }
      if (batter.isZoomed && !batter.hasActions()) {
         return batter;
      }
      if (runner1 != null && runner1.isZoomed && !runner1.hasActions()) {
         return runner1;
      }
      if (runner2 != null && runner2.isZoomed && !runner2.hasActions()) {
         return runner2;
      }
      if (runner3 != null && runner3.isZoomed && !runner3.hasActions()) {
         return runner3;
      }
      return null;
   }
}
