package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Card {
	public String name, image;
	public Texture backgroundTexture;
	public TextureRegion cardTexture;
	public int posX, posY, cardnum, id;
	public float scale;
	
	public void draw(SpriteBatch batch) {
		batch.draw(cardTexture, posX, posY, cardTexture.getRegionWidth()*scale, cardTexture.getRegionHeight()*scale);
	}
	
	public void dispose() {
		backgroundTexture.dispose();
	}
	
}
