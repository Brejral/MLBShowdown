package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameStage extends Stage {
	public Game game;
	
	public GameStage(Viewport viewport, Game gm) {
		super(viewport);
		game = gm;
	}
}
