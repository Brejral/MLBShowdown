package com.brejral.mlbshowdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainMenu implements Screen {
	final MLBShowdown mlbShowdown;
	SpriteBatch batch;
	Texture backgroundTexture;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter fontParameter;
	BitmapFont aeroDisplayItalicFont72;
	BitmapFont aeroDisplayItalicFont36;
	
	public MainMenu(final MLBShowdown showdown) {
		mlbShowdown = showdown;
		batch = new SpriteBatch();
		backgroundTexture = new Texture("images/baseball_diamond.png");
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/aero_matics_display_italic.ttf"));
		fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 72;
		aeroDisplayItalicFont72 = generator.generateFont(fontParameter);
		fontParameter.size = 36;
		aeroDisplayItalicFont36 = generator.generateFont(fontParameter);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setColor(1, 1, 1, .5f);
		batch.draw(backgroundTexture, 0, 0);
		aeroDisplayItalicFont72.draw(batch, "MLB Showdown 2014", 10, 890);
		aeroDisplayItalicFont36.draw(batch, "Exhibition", 50, 800);
		batch.end();
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
		generator.dispose();
	}

}
