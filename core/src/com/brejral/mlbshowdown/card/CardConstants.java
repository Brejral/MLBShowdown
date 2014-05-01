package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class CardConstants {
	public static Texture CHART_TEXTURE = new Texture(Gdx.files.internal("images/chart_background.png"));
	public static Texture ONBASE_TEXTURE = new Texture(Gdx.files.internal("images/onbase_background.png"));
	public static Texture CONTROL_TEXTURE = new Texture(Gdx.files.internal("images/control_background.png"));
	public static String[] CHART_TEXT = {"PU", "SO", "GB", "FB", "BB", "1B", "1B+", "2B", "3B", "HR"};
}
