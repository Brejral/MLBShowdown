package com.brejral.mlbshowdown.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brejral.mlbshowdown.MLBShowdown;

public class MainMenu implements Screen {
	final MLBShowdown sd;
	SpriteBatch batch;
	Stage stage;
	
	public MainMenu(final MLBShowdown showdown) {
		sd = showdown;
		stage = new Stage(new ScreenViewport());
		addActorsToStage();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
      Table.drawDebug(stage);
	}
	

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
	}
	
	public void addActorsToStage() {
	   Image background = new Image(sd.fieldTexture);
	   background.setColor(new Color(1f, 1f, 1f, .5f));
	   stage.addActor(background);
	   
	   Label menuTitle = new Label("MLB Showdown 2014", sd.skin.get("aero72", LabelStyle.class));
	   menuTitle.setAlignment(Align.left);
	   menuTitle.setPosition(10, 518);
	   stage.addActor(menuTitle);
	   
	   TextButton exhibitionButton = new TextButton("Exhibition", sd.skin.get("aero36", TextButtonStyle.class));
	   exhibitionButton.addListener(new ClickListener() {
	      @Override
	      public void clicked(InputEvent event, float x, float y) {
	         sd.setScreen(new ExhibitionMenu(sd));
	      }
	   });
	   
	   TextButton seasonButton = new TextButton("Season", sd.skin.get("aero36", TextButtonStyle.class));
	   seasonButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
         }
      });
	   
	   TextButton tournamentButton = new TextButton("Tournament", sd.skin.get("aero36", TextButtonStyle.class));
	   tournamentButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
         }
      });
      
      TextButton managementButton = new TextButton("Management", sd.skin.get("aero36", TextButtonStyle.class));
      managementButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
         }
      });
      
      TextButton statisticsButton = new TextButton("Statistics", sd.skin.get("aero36", TextButtonStyle.class));
      statisticsButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
         }
      });
      
      TextButton settingsButton = new TextButton("Settings", sd.skin.get("aero36", TextButtonStyle.class));
      settingsButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
         }
      });
	   
	   Table buttonTable = new Table();
	   buttonTable.align(Align.left);
	   buttonTable.setPosition(50, 350);
	   buttonTable.add(exhibitionButton).height(50).left();
	   buttonTable.row();
	   buttonTable.add(seasonButton).height(50).left();
	   buttonTable.row();
	   buttonTable.add(tournamentButton).height(50).left();
	   buttonTable.row();
	   buttonTable.add(managementButton).height(50).left();
	   buttonTable.row();
	   buttonTable.add(statisticsButton).height(50).left();
	   buttonTable.row();
	   buttonTable.add(settingsButton).height(50).left();
	   stage.addActor(buttonTable);
	}
}
