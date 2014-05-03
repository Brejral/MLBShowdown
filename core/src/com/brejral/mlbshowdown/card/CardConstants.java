package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class CardConstants {
	public static Texture CHART_TEXTURE = new Texture(Gdx.files.internal("images/chart_background.png"));
	public static Texture ONBASE_TEXTURE = new Texture(Gdx.files.internal("images/onbase_background.png"));
	public static Texture CONTROL_TEXTURE = new Texture(Gdx.files.internal("images/control_background.png"));
	public static String[] CHART_TEXT = {"PU", "SO", "GB", "FB", "BB", "1B", "1B+", "2B", "3B", "HR"};
	public static Texture TEAM_LOGOS_TEXTURE = new Texture(Gdx.files.internal("images/team_logos.png"));
	public static Texture TEAM_LOGOS_GOLD_TEXTURE = new Texture(Gdx.files.internal("images/team_logos_gold.png"));
	public static int TEAM_LOGOS_NUM_X = 6;
	public static int TEAM_LOGOS_NUM_Y = 5;
}
