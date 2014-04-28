package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.card.Batter;

public class GameScreen implements Screen {
	SpriteBatch batch;
	MLBShowdown showdown;
	Game game;
	FreeTypeFontGenerator straightGenerator;
	FreeTypeFontGenerator slantGenerator;
	FreeTypeFontParameter fontParameter;
	BitmapFont straightCardFont;
	BitmapFont straightCardFont2;
	BitmapFont slantCardFont;
	BitmapFont slantCardFont2;
	Batter testCard;
	Texture backgroundTexture;
	boolean add;
		
	public GameScreen(MLBShowdown mlbsd) {
		showdown = mlbsd;
		batch = new SpriteBatch();
		game = new Game();
		straightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));
		slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
		fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 36;
		slantCardFont = slantGenerator.generateFont(fontParameter);
		fontParameter.size = 37;
		slantCardFont2 = slantGenerator.generateFont(fontParameter);
		testCard = new Batter("Mike Trout", "mike_trout.png");
		backgroundTexture = new Texture(Gdx.files.internal("images/baseball_diamond.png"));
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		testCard.draw(batch);
		batch.end();
		
		if (testCard.scale > .35f && !add) {
			testCard.scale -= .02f;
			if (testCard.scale <= .35f) {
				add = true;
			}
		}
		if (testCard.scale < 1f && add) {
			testCard.scale += .02f;
			if (testCard.scale >= 1f) {
				add = false;
			}
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
		
	}

}
