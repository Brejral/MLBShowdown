package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Batter extends Card {
	int onbase, speed;
	int[] chart = new int[10];
	String bats;
	FreeTypeFontGenerator straightGenerator;
	FreeTypeFontGenerator slantGenerator;
	FreeTypeFontParameter nameFontParameter;
	BitmapFont nameFont;

	public Batter(String nm, String img) {
		name = nm;
		image = img;
		imageTexture = new Texture(Gdx.files.internal("images/" + image));
		posX = 45;
		posY = 95;
		scale = 1f;
		straightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));;
		slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
		nameFontParameter = new FreeTypeFontParameter();
		nameFontParameter.size = 36;
		nameFont = slantGenerator.generateFont(nameFontParameter);
	}

	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		batch.draw(CardConstants.CHART_TEXTURE, posX, posY+60*scale, CardConstants.CHART_TEXTURE.getWidth()*scale, CardConstants.CHART_TEXTURE.getHeight()*scale);
		batch.draw(CardConstants.ONBASE_TEXTURE, posX+10*scale, posY+130*scale, CardConstants.ONBASE_TEXTURE.getWidth()*scale, CardConstants.ONBASE_TEXTURE.getHeight()*scale);
		if (Math.round(36*scale) != nameFontParameter.size) {
			nameFontParameter.size = Math.round(36*scale);
			nameFont = slantGenerator.generateFont(nameFontParameter);
		}
		nameFont.setColor(Color.RED);
		nameFont.draw(batch, name, posX+100*scale, posY+185*scale);
		nameFont.setColor(Color.WHITE);
		nameFont.draw(batch, name, posX+100*scale+1, posY+185*scale+1);
		
	}
}
