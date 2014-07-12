package com.brejral.mlbshowdown.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.game.GameLoadingScreen;

public class ExhibitionMenu implements Screen {
	final MLBShowdown sd;
	SpriteBatch batch;
	BitmapFont aeroDisplayItalicFont36;
	
	public ExhibitionMenu(MLBShowdown showdown) {
		sd = showdown;
		batch = new SpriteBatch();
		aeroDisplayItalicFont36 = MLBShowdown.getAeroItalicFont(36);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(sd.fieldTexture, 0, 0);
		aeroDisplayItalicFont36.setColor(Color.WHITE);
		aeroDisplayItalicFont36.draw(batch, "ExhibitionMenu", 10, 590);
		aeroDisplayItalicFont36.draw(batch, "Press SPACE to continue", 50, 500);
		batch.end();
		
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			sd.setScreen(new GameLoadingScreen(sd));
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
		aeroDisplayItalicFont36.dispose();
	}

}
