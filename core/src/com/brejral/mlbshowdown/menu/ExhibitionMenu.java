package com.brejral.mlbshowdown.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.game.GameLoadingScreen;

public class ExhibitionMenu implements Screen {
	final MLBShowdown sd;
	Stage stage;
	Array<String> teamNames;
	Array<TeamSnippet> teams;
	TeamSnippet homeTeam;
	TeamSnippet awayTeam;
	
	public ExhibitionMenu(MLBShowdown showdown) {
		sd = showdown;
		stage = new Stage(new ScreenViewport());
		teams = new Array<TeamSnippet>();
		teamNames = new Array<String>();
		loadTeamSnippets();
		homeTeam = teams.get(0);
		awayTeam = teams.get(0);
		addActorsToStage();
		setTeamInfo(true);
      setTeamInfo(false);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
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
	
	private void addActorsToStage() {
	   Image background = new Image(sd.fieldTexture);
	   background.setColor(new Color(1f, 1f, 1f, .5f));
	   stage.addActor(background);
	   
	   Label menuTitle = new Label("Exhibition Menu", sd.skin.get("aero72", LabelStyle.class));
	   menuTitle.setAlignment(Align.center);
	   menuTitle.setPosition(0, 518);
	   menuTitle.setWidth(900);
	   stage.addActor(menuTitle);
	   
	   Label awayTitle = new Label("Away", sd.skin.get("aero36", LabelStyle.class));
	   awayTitle.setAlignment(Align.center);
      awayTitle.setPosition(0, 482);
	   awayTitle.setWidth(450);
      stage.addActor(awayTitle);
      
      SelectBox<String> awayTeamSelect = new SelectBox<>(sd.skin.get(SelectBoxStyle.class));
      awayTeamSelect.setName("Away Team Selection");
      awayTeamSelect.setItems(teamNames);
      awayTeamSelect.setPosition(150, 455);
      awayTeamSelect.setSize(150, 30);
      awayTeamSelect.addListener(new ChangeListener() {
         @SuppressWarnings("rawtypes")
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            awayTeam = getTeamSnippet((String)((SelectBox) event.getListenerActor()).getSelected());
            setTeamInfo(false);
         }
      });
      stage.addActor(awayTeamSelect);
      
      Label awayTeamFullName = new Label("", sd.skin.get("aero30", LabelStyle.class));
      awayTeamFullName.setName("Away Team Full Name");
      awayTeamFullName.setPosition(50, 400);
      stage.addActor(awayTeamFullName);
      
      Image awayTeamLogo = new Image();
      awayTeamLogo.setName("Away Team Logo");
      awayTeamLogo.setPosition(340, 400);
      awayTeamLogo.setScale(.5f);
      stage.addActor(awayTeamLogo);
      
      Label homeTitle = new Label("Home", sd.skin.get("aero36", LabelStyle.class));
      homeTitle.setAlignment(Align.center);
      homeTitle.setPosition(450, 482);
      homeTitle.setWidth(450);
      stage.addActor(homeTitle);
      
      SelectBox<String> homeTeamSelect = new SelectBox<>(sd.skin.get(SelectBoxStyle.class));
      homeTeamSelect.setName("Home Team Selection");
      homeTeamSelect.setItems(teamNames);
      homeTeamSelect.setPosition(600, 455);
      homeTeamSelect.setSize(150, 30);
      homeTeamSelect.addListener(new ChangeListener() {
         @SuppressWarnings("rawtypes")
         @Override
         public void changed(ChangeEvent event, Actor actor) {
            homeTeam = getTeamSnippet((String) ((SelectBox) event.getListenerActor()).getSelected());
            setTeamInfo(true);
         }
      });
      stage.addActor(homeTeamSelect);
      
      Label homeTeamFullName = new Label("", sd.skin.get("aero30", LabelStyle.class));
      homeTeamFullName.setName("Home Team Full Name");
      homeTeamFullName.setAlignment(Align.right);
      homeTeamFullName.setPosition(850, 400);
      stage.addActor(homeTeamFullName);
      
      Image homeTeamLogo = new Image();
      homeTeamLogo.setName("Home Team Logo");
      homeTeamLogo.setPosition(460, 400);
      homeTeamLogo.setScale(.5f);
      stage.addActor(homeTeamLogo);
      
      TextButton startGameButton = new TextButton("Start Game", sd.skin.get(TextButtonStyle.class));
      startGameButton.setPosition(450 - startGameButton.getStyle().font.getBounds(startGameButton.getText()).width/2, 300);
      startGameButton.align(Align.center);
      startGameButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            sd.setScreen(new GameLoadingScreen(sd, awayTeam.nickName, homeTeam.nickName));
         }
      });
      stage.addActor(startGameButton);
	}
	
	private void loadTeamSnippets() {
	   String query = "SELECT * FROM TEAMS;";
	   try {
         DatabaseCursor cursor = sd.db.rawQuery(query);
         while (cursor.next()) {
            TeamSnippet snippet = new TeamSnippet(cursor);
            teams.add(snippet);
            teamNames.add(snippet.nickName);
         }
      } catch (SQLiteGdxException e) {
         e.printStackTrace();
      }
	}
	
	private TeamSnippet getTeamSnippet(String selected) {
	   for (TeamSnippet snip : teams) {
         if (snip.nickName.equals(selected)) {
            return snip;
         }
      }
	   return null;
   }
	
	private void setTeamInfo(boolean home) {
	   Label fullName = (Label)stage.getRoot().findActor((home ? "Home" : "Away") + " Team Full Name");
	   fullName.setText((home ? homeTeam : awayTeam).fullName);
      fullName.setPosition((home ? 560 : 340 - fullName.getWidth()), 400);
      Image logo = (Image)stage.getRoot().findActor((home ? "Home" : "Away") + " Team Logo");
      logo.setDrawable(new TextureRegionDrawable((home ? homeTeam : awayTeam).logo));
	}
	
	public class TeamSnippet {
	   public String fullName, nickName, location;
	   public Sprite logo;
	   
	   public TeamSnippet(DatabaseCursor cursor) {
	      fullName = cursor.getString(1);
	      location = cursor.getString(2);
	      nickName = cursor.getString(3);
	      logo = MLBShowdown.getLogo(nickName, "C");
	   }
	   
	}
}
