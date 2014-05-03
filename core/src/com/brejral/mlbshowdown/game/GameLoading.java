package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.Screen;
import com.brejral.mlbshowdown.MLBShowdown;

public class GameLoading implements Screen {
	MLBShowdown showdown;
	Game game;
	
	public GameLoading(MLBShowdown sd) {
		showdown = sd;
		game = new Game(showdown.db);
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		
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
