package com.brejral.mlbshowdown.game;

import java.util.Random;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.menu.MainMenu;
import com.brejral.mlbshowdown.stats.StatKeeper;
import com.brejral.mlbshowdown.team.Team;
import com.brejral.mlbshowdown.user.User;

public class Game {
	public final MLBShowdown sd;
	public GameScreen screen;
	public StatKeeper statKeeper;
	public Database db;
	public AssetManager manager;
	public Team awayTeam, homeTeam;
	public Card batter, pitcher, runner1, runner2, runner3;
	public int inning = 1, outs, homeScore, awayScore, pitch, swing;
	public int pitchCardBonus = 0, swingCardBonus = 0;
	boolean isTop = true, isPChart = true, isTopExtraInnings = false;
	public String result;
	public Random rand;
	
	/**
	 * Creates a new game
	 * @param showdown - the global MLBShowdown 
	 * @param assetManager - the asset manager
	 */
	public Game(MLBShowdown showdown, AssetManager assetManager) {
		sd = showdown;
		manager = assetManager;
		db = sd.db;
		rand = new Random(System.currentTimeMillis());
		awayTeam = new Team(sd, new User(sd, true), manager, "Mariners");
		homeTeam = new Team(sd, sd.user, manager, "Angels");
		statKeeper = new StatKeeper(this);
	}
	
	/**
	 *  Set the game's starting parameters
	 */
	public void startGame() {
		awayTeam.lineupSpot = 0;
		homeTeam.lineupSpot = 0;
		setBatter();
		setPitcher();
	}
	
	/**
	 *  Sets the pitcher to the current pitcher of the team
	 */
	public void setPitcher() {
		removeCardVisibility(pitcher);
		if (isTop) {
			pitcher = homeTeam.positions.get(1);
		} else {
			pitcher = awayTeam.positions.get(1);
		}
		pitcher.setName("Pitcher");
		pitcher.setPosition(450f, 260f);
		pitcher.setScale(.2f);
		pitcher.setVisible(true);
		pitcher.setZIndex(1);
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
		batter = team.lineup.get(team.lineupSpot);
		batter.setName("Batter");
		batter.setPosition(450, 75);
		batter.setScale(.2f);
		batter.setVisible(true);
		batter.setZIndex(5);
	}
	
	/**
	 * Sets the runner at first base to Card instance (can be null)
	 * 
	 * @param card - Card instance
	 */
	public void setRunner1(Card card) {
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
	 * @param card - Card instance
	 */
	public void setRunner2(Card card) {
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
	 * @param card - Card instance
	 */
	public void setRunner3(Card card) {
		runner3 = card;
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
	 * @param card - The Card to remove visibility from
	 */
	public void removeCardVisibility(Card card) {
		if (card != null) {
			card.setName("");
			card.setVisible(false);
		}
	}
	
	/**
	 * Performs all actions for the pitch roll
	 */
	public void pitch() {
		rollPitch();
		setChart();
		screen.setPitchText();
		screen.setChartText();
		screen.disableButtons();
		scheduleTasks();
	}
	
	/**
	 * Performs all actions for the swing roll
	 */
	public void swing() {
		rollSwing();
		getResult();
		screen.setSwingText();
		screen.setResultText();
		screen.disableButtons();
		scheduleTasks();
		
	}
	
	/**
	 * Generates a random number(1-20) for the pitch roll and adds control and bonuses
	 */
	public void rollPitch() {
		pitch = rand.nextInt(20) + 1 + pitcher.controlAdj + pitchCardBonus;
	}
	
	/**
	 * Generates a random number(1-20) for the swing roll and adds bonuses
	 */
	public void rollSwing() {
		swing = rand.nextInt(20) + 1 + swingCardBonus;
	}
	
	/**
	 * Sets the chart to select the result from; based on the pitch roll and the batter's onbase
	 */
	public void setChart() {
		if (pitch > batter.onbase) {
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
			getResultFromChart(pitcher.chart);
		} else {
			getResultFromChart(batter.chart);
		}
	}
	
	/**
	 * Gets the result from the chart based off the swing and sets the result string
	 * @param chart - int array of size 10; chart to get the result from
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
		switch(result) {
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
			if (runner2 != null ) {
				runner2.addAction(CardConstants.getRunToThirdAction(this, runner2));
			}
			if (runner3 != null) {
				runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
			}break;
		case "FB":
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
				runner1.addAction(CardConstants.getRunToSecondAction(this, runner1));
			}
			if (runner2 != null) {
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
				runner1.addAction(CardConstants.getRunToSecondAction(this, runner1));
			}
			if (runner2 != null) {
				runner2.addAction(CardConstants.getRunToThirdAction(this, runner2));
			}
			if (runner3 != null) {
				runner3.addAction(CardConstants.getRunToHomeAction(this, runner3));
			}
			break;
		case "2B":
			batter.addAction(CardConstants.getRunToSecondAction(this, batter));
			if (runner1 != null) {
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
		switch(result) {
		case "PU":
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "PU");
			statKeeper.increaseStat(pitcher, "OUTS");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "PU");
			removeCardVisibility(batter);
			addOut();
			break;
		case "SO":
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "SO");
			statKeeper.increaseStat(pitcher, "OUTS");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "SO");
			removeCardVisibility(batter);
			addOut();
			break;
		case "GB":
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "GB");
			statKeeper.increaseStat(pitcher, "OUTS");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "GB");
			if (runner3 != null) {
				if (outs < 2) {
					statKeeper.increaseStat(runner3, "R");
					statKeeper.increaseStat(pitcher, "R");
					statKeeper.increaseStat(pitcher, "ER");
					statKeeper.increaseStat(batter, "RBI");
					removeCardVisibility(runner3);
					addScore();
				}
			}
			setRunner3(runner2);
			setRunner2(runner1);
			setRunner1(null);
			removeCardVisibility(batter);
			addOut();
			break;
		case "FB":
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "FB");
			statKeeper.increaseStat(pitcher, "OUTS");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "FB");
			removeCardVisibility(batter);
			addOut();
			break;
		case "BB":
			statKeeper.increaseStat(batter, "BB");
			statKeeper.increaseStat(pitcher, "BB");
			if (runner3 != null && runner2 != null && runner1 != null) {
				statKeeper.increaseStat(runner3, "R");
				statKeeper.increaseStat(pitcher, "R");
				statKeeper.increaseStat(pitcher, "ER");
				statKeeper.increaseStat(batter, "RBI");
				removeCardVisibility(runner3);
				addScore();
			}
			if (runner2 != null && runner1!= null) {
				setRunner3(runner2);
			}
			if (runner1 != null) {
				setRunner2(runner1);
			}
			setRunner1(batter);
			break;
		case "1B":
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "SINGLE");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "SINGLE");
			if (runner3 != null) {
				statKeeper.increaseStat(runner3, "R");
				statKeeper.increaseStat(pitcher, "R");
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
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "SINGLE");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "SINGLE");
			if (runner3 != null) {
				statKeeper.increaseStat(runner3, "R");
				statKeeper.increaseStat(pitcher, "R");
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
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "DOUBLE");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "DOUBLE");
			if (runner3 != null) {
				statKeeper.increaseStat(runner3, "R");
				statKeeper.increaseStat(pitcher, "R");
				statKeeper.increaseStat(pitcher, "ER");
				statKeeper.increaseStat(batter, "RBI");
				removeCardVisibility(runner3);
				addScore();
			}
			if (runner2 != null) {
				statKeeper.increaseStat(runner2, "R");
				statKeeper.increaseStat(pitcher, "R");
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
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "TRIPLE");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "TRIPLE");
			if (runner3 != null) {
				statKeeper.increaseStat(runner3, "R");
				statKeeper.increaseStat(pitcher, "R");
				statKeeper.increaseStat(pitcher, "ER");
				statKeeper.increaseStat(batter, "RBI");
				removeCardVisibility(runner3);
				addScore();
			}
			if (runner2 != null) {
				statKeeper.increaseStat(runner2, "R");
				statKeeper.increaseStat(pitcher, "R");
				statKeeper.increaseStat(pitcher, "ER");
				statKeeper.increaseStat(batter, "RBI");
				removeCardVisibility(runner2);
				addScore();
			}
			if (runner1 != null) {
				statKeeper.increaseStat(runner1, "R");
				statKeeper.increaseStat(pitcher, "R");
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
			statKeeper.increaseStat(batter, "AB");
			statKeeper.increaseStat(batter, "HR");
			statKeeper.increaseStat(pitcher, "AB");
			statKeeper.increaseStat(pitcher, "HR");
			if (runner3 != null) {
				statKeeper.increaseStat(runner3, "R");
				statKeeper.increaseStat(pitcher, "R");
				statKeeper.increaseStat(pitcher, "ER");
				statKeeper.increaseStat(batter, "RBI");
				removeCardVisibility(runner3);
				addScore();
			}
			if (runner2 != null) {
				statKeeper.increaseStat(runner2, "R");
				statKeeper.increaseStat(pitcher, "R");
				statKeeper.increaseStat(pitcher, "ER");
				statKeeper.increaseStat(batter, "RBI");
				removeCardVisibility(runner2);
				addScore();
			}
			if (runner1 != null) {
				statKeeper.increaseStat(runner1, "R");
				statKeeper.increaseStat(pitcher, "R");
				statKeeper.increaseStat(pitcher, "ER");
				statKeeper.increaseStat(batter, "RBI");
				removeCardVisibility(runner1);
				addScore();
			}
			statKeeper.increaseStat(batter, "R");
			statKeeper.increaseStat(pitcher, "R");
			statKeeper.increaseStat(pitcher, "ER");
			statKeeper.increaseStat(batter, "RBI");
			removeCardVisibility(batter);
			addScore();
			setRunner3(null);
			setRunner2(null);
			setRunner1(null);
			break;
		}
		//statKeeper.executeGameStatChanges();
	}
	
	/**
	 * Resets fields for the next at-bat
	 */
	public void resetForNextAB() {
		pitch = 0;
		swing = 0;
		result = "";
		screen.setPitchText();
		screen.setChartText();
		screen.setSwingText();
		screen.setResultText();
		setNewBatter();
		screen.disableButtons();
		scheduleTasks();
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
		screen.setLineupHighlight();
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
		nextLineupSpot();
		checkEndOfInning();
		resetForNextAB();
	}
	
	/**
	 * Schedules automated tasks based on certain events and checks
	 */
	public void scheduleTasks() {
		TextButton button = (TextButton) screen.stage.getRoot().findActor("Pitch Button");
		if (pitch == 0 && !button.isTouchable()) {
			Timer timer = new Timer();
			timer.scheduleTask(new Task() {
				@Override
				public void run() {
					pitch();
					this.cancel();
				}
			}, MLBShowdown.TASK_SPEED);
		} 
		button = (TextButton) screen.stage.getRoot().findActor("Swing Button");
		if (swing == 0 && pitch != 0 && !button.isTouchable()) {
			Timer timer = new Timer();
			timer.scheduleTask(new Task() {
				@Override
				public void run() {
					swing();
					this.cancel();
				}
			}, MLBShowdown.TASK_SPEED);
		}
		if (swing != 0 && pitch != 0) {
			Timer timer = new Timer();
			timer.scheduleTask(new Task() {
				@Override
				public void run() {
					setActionsForResult();
					this.cancel();
				}
			}, MLBShowdown.TASK_SPEED);
		}
	}
	
	/**
	 * Checks for the end of the inning
	 */
	public void checkEndOfInning() {
		if (outs > 2) {
			outs = 0;
			screen.setOutMarkers();
			removeCardVisibility(runner1);
			removeCardVisibility(runner2);
			removeCardVisibility(runner3);
			setRunner1(null);
			setRunner2(null);
			setRunner3(null);
			isTop = !isTop;
			pitcher.addIp();
			screen.setTopBottomInningMarker();
			screen.disableButtons();
			screen.setLineupText();
			if (isTop) {
				inning++;
				screen.setInningText();
				if (inning > 9) {
					isTopExtraInnings = true;
				}
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
			}
		}
		if (!isTop  && inning > 8) {
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
		screen.setOutMarkers();
	}

	/**
	 * Adds a run to the away or home score
	 */
	public void addScore() {
		if (isTop) {
			awayScore++;
		} else {
			homeScore++;
		}
		screen.setScoreText();
	}
	
	public boolean isCardZoomed() {
		return (pitcher.isZoomed && !pitcher.hasActions()) || (batter.isZoomed && !batter.hasActions()) 
				|| (runner1 != null && runner1.isZoomed && !runner1.hasActions())
				|| (runner2 != null && runner2.isZoomed && !runner2.hasActions()) 
				|| (runner3 != null && runner3.isZoomed && !runner3.hasActions());
	}
	
	public Card getZoomedCard() {
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
