package com.brejral.mlbshowdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextActor extends Actor {
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter param;
	BitmapFont font;
	String text;
	
	public TextActor(String txt, String fontString, int size) {
		text = txt;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + fontString));
		param = new FreeTypeFontParameter();
		param.size = size;
		font = generator.generateFont(param);
	}
	
	public void setSize(int size) {
		param.size = size;
		font = generator.generateFont(param);
	}
	
	public void setText(String txt) {
		text = txt;
	}
	
	@Override
	public float getWidth() {
		return font.getBounds(text).width;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		font.setColor(getColor());
		TextBounds tb = font.getBounds(text);
		int w = (int) tb.width;
		int h = (int) tb.height;
		font.draw(batch, text, getX() - w/2, getY() + h/2);
	}
}
