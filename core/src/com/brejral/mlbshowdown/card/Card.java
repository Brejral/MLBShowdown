package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Card {
	public String name, image;
	public Texture imageTexture;
	public int posX, posY;
	public float scale;
	
	public void draw (SpriteBatch batch) {
		batch.draw(imageTexture, posX, posY, imageTexture.getWidth()*scale, imageTexture.getHeight()*scale);
	}
	
}
