package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.brejral.mlbshowdown.AnimationRunnable;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.game.Game;

public class CardConstants {
	public static Texture CHART_TEXTURE = new Texture(Gdx.files.internal("images/chart_background.png"));
	public static Texture ONBASE_TEXTURE = new Texture(Gdx.files.internal("images/onbase_background.png"));
	public static Texture CONTROL_TEXTURE = new Texture(Gdx.files.internal("images/control_background.png"));
	public static String[] CHART_TEXT = {"PU", "SO", "GB", "FB", "BB", "1B", "1B+", "2B", "3B", "HR"};
	public static String[] POSITION_TEXT = {"DH","P","C","1B","2B","3B","SS","LF","CF","RF"};
	public static Texture TEAM_LOGOS_TEXTURE = new Texture(Gdx.files.internal("images/team_logos.png"));
	public static Texture TEAM_LOGOS_GOLD_TEXTURE = new Texture(Gdx.files.internal("images/team_logos_gold.png"));
	public static int TEAM_LOGOS_NUM_X = 6;
	public static int TEAM_LOGOS_NUM_Y = 5;
	
	public static SequenceAction getRunToFirstAction(Game game, Card card) {
		SequenceAction action = new SequenceAction();
		MoveToAction action1 = new MoveToAction();
		action1.setPosition(625, 260);
		action1.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action1);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
	
	public static SequenceAction getRunToSecondAction(Game game, Card card) {
		SequenceAction action = new SequenceAction();
		if (card.getX() == 450 && card.getY() == 75) {
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(625, 260);
			action1.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action1);
		}
		MoveToAction action2 = new MoveToAction();
		action2.setPosition(450, 450);
		action2.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action2);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
	
	public static SequenceAction getRunToThirdAction(Game game, Card card) {
		SequenceAction action = new SequenceAction();
		if (card.getX() == 450 && card.getY() == 75) {
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(625, 260);
			action1.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action1);
		}
		if ((card.getX() == 450 && card.getY() == 75) ||
				(card.getX() == 625 && card.getY() == 260)) {
			MoveToAction action2 = new MoveToAction();
			action2.setPosition(450, 450);
			action2.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action2);
		}
		MoveToAction action3 = new MoveToAction();
		action3.setPosition(275, 260);
		action3.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action3);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
	
	public static SequenceAction getRunToHomeAction(Game game, Card card) {
		SequenceAction action = new SequenceAction();
		if (card.getX() == 450 && card.getY() == 75) {
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(625, 260);
			action1.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action1);
		}
		if ((card.getX() == 450 && card.getY() == 75) ||
				(card.getX() == 625 && card.getY() == 260)) {
			MoveToAction action2 = new MoveToAction();
			action2.setPosition(450, 450);
			action2.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action2);
		}
		if ((card.getX() == 450 && card.getY() == 75) ||
				(card.getX() == 625 && card.getY() == 260) ||
				(card.getX() == 450 && card.getY() == 450)) {
			MoveToAction action3 = new MoveToAction();
			action3.setPosition(275, 260);
			action3.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action3);
		}
		MoveToAction action4 = new MoveToAction();
		action4.setPosition(450, 75);
		action4.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action4);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
}
