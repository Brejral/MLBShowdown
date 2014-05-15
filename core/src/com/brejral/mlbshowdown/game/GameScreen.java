package com.brejral.mlbshowdown.game;

import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.ShapeActor;
import com.brejral.mlbshowdown.TextActor;
import com.brejral.mlbshowdown.card.Card;
import com.brejral.mlbshowdown.card.CardConstants;
import com.brejral.mlbshowdown.team.Team;

public class GameScreen implements Screen {
	MLBShowdown sd;
	Stage stage;
	Game game;
	FreeTypeFontGenerator aeroGenerator;
	FreeTypeFontGenerator straightGenerator;
	FreeTypeFontGenerator slantGenerator;
	FreeTypeFontParameter fontParameter;
	BitmapFont straightCardFont;
	BitmapFont straightCardFont2;
	BitmapFont slantCardFont;
	BitmapFont slantCardFont2;
	
	/**
	 * Creates a new GameScreen to be used with a Game
	 * @param showdown - the global MLBShowdown variable
	 * @param gm - the game that is created in the GameLoading screen
	 */
	public GameScreen(MLBShowdown showdown, Game gm) {
		sd = showdown;
		stage = new GameStage(new ScreenViewport(), gm);
		stage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				GameStage stage = (GameStage) event.getStage();
				if (stage.game.isCardZoomed() && !stage.game.getZoomedCard().hasActions()) {
					stage.game.getZoomedCard().zoom();
				}
				return true;
			}
		});
		Gdx.input.setInputProcessor(stage);
		game = gm;
		game.screen = this;
		
		aeroGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/aero_matics_display_italic.ttf"));
		straightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));
		slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
		fontParameter = new FreeTypeFontParameter();
		fontParameter.size = 36;
		slantCardFont = slantGenerator.generateFont(fontParameter);
		fontParameter.size = 37;
		slantCardFont2 = slantGenerator.generateFont(fontParameter);
		
		addActorsToStage();
		disableButtons();
		game.scheduleTasks();
		game.statKeeper.setUpTableForGame();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(73f/255f, 145f/255f, 94f/255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
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
	
	/**
	 * Adds all the actors to the stage to be drawn
	 */
	public void addActorsToStage() {
		stage.addActor(new Image(sd.fieldTexture));
		Image image = new Image(sd.boardNP);
		image.setSize(200, 100);
		image.setPosition(700, 500);
		image.setTouchable(Touchable.enabled);
		stage.addActor(image);
		for (Card card : game.awayTeam.roster) {
			stage.addActor(card);
		}
		for (Card card : game.homeTeam.roster) {
			stage.addActor(card);
		}
		TextButtonStyle style = new TextButtonStyle();
		fontParameter.size = 20;
		style.font = aeroGenerator.generateFont(fontParameter);
		style.over = sd.buttonOverDrawable;
		TextButton pitchButton = new TextButton("PITCH", style);
		pitchButton.setName("Pitch Button");
		pitchButton.setPosition(800, 100);
		pitchButton.setVisible(true);
		pitchButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.pitch();
			}
		});
		stage.addActor(pitchButton);
		
		TextButton swingButton = new TextButton("SWING", style);
		swingButton.setName("Swing Button");
		swingButton.setPosition(800, 50);
		swingButton.setVisible(true);
		swingButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.swing();
			}
		});
		stage.addActor(swingButton);
		
		TextActor pitchText = new TextActor("", "aero_matics_display_italic.ttf", 30);
		pitchText.setName("Pitch Text");
		pitchText.setPosition(780, 115);
		pitchText.setColor(Color.WHITE);
		stage.addActor(pitchText);
		
		TextActor swingText = new TextActor("", "aero_matics_display_italic.ttf", 30);
		swingText.setName("Swing Text");
		swingText.setPosition(780, 65);
		swingText.setColor(Color.WHITE);
		stage.addActor(swingText);
		
		TextActor chartText = new TextActor("", "aero_matics_display_italic.ttf", 20);
		chartText.setName("Chart Text");
		chartText.setPosition(780, 90);
		chartText.setColor(Color.WHITE);
		stage.addActor(chartText);
		
		TextActor resultText = new TextActor("", "aero_matics_display_italic.ttf", 20);
		resultText.setName("Result Text");
		resultText.setPosition(780, 40);
		resultText.setColor(Color.WHITE);
		stage.addActor(resultText);
		
		TextActor inningText = new TextActor("1", "Muro.ttf", 40);
		inningText.setName("Inning Text");
		inningText.setPosition(795, 570);
		inningText.setColor(Color.WHITE);
		stage.addActor(inningText);
		
		TextActor innText = new TextActor("INNING", "aero_matics_display_italic.ttf", 20);
		innText.setPosition(800, 540);
		innText.setColor(Color.WHITE);
		stage.addActor(innText);
		
		int innX = (int) (inningText.getX() + inningText.getWidth()/2);
		float[] itverts = {innX+ 5,571,innX+ 10,576,innX+ 15,571};
		ShapeActor inningTopMarker = new ShapeActor(ShapeType.Filled, itverts);
		inningTopMarker.setName("Inning Top Marker");
		inningTopMarker.setColor(Color.YELLOW);
		stage.addActor(inningTopMarker);
		
		float[] ibverts = {innX+ 5,569,innX + 10,564,innX + 15,569};
		ShapeActor inningBottomMarker = new ShapeActor(ShapeType.Filled, ibverts);
		inningBottomMarker.setName("Inning Bottom Marker");
		inningBottomMarker.setColor(Color.YELLOW);
		inningBottomMarker.setVisible(false);
		stage.addActor(inningBottomMarker);
		
		TextActor homeTeamText = new TextActor(game.homeTeam.nickName, "aero_matics_display_italic.ttf", 15);
		homeTeamText.setPosition(865, 520);
		homeTeamText.setColor(Color.WHITE);
		stage.addActor(homeTeamText);
		
		TextActor awayTeamText = new TextActor(game.awayTeam.nickName, "aero_matics_display_italic.ttf", 15);
		awayTeamText.setPosition(735, 520);
		awayTeamText.setColor(Color.WHITE);
		stage.addActor(awayTeamText);
		
		TextActor homeScoreText = new TextActor(Integer.toString(game.homeScore), "Muro.ttf", 50);
		homeScoreText.setName("Home Score");
		homeScoreText.setPosition(865, 555);
		homeScoreText.setColor(Color.WHITE);
		stage.addActor(homeScoreText);
		
		TextActor awayScoreText = new TextActor(Integer.toString(game.awayScore), "Muro.ttf", 50);
		awayScoreText.setName("Away Score");
		awayScoreText.setPosition(735, 555);
		awayScoreText.setColor(Color.WHITE);
		stage.addActor(awayScoreText);
		
		TextActor outsText = new TextActor("OUTS", "aero_matics_display_italic.ttf", 15);
		outsText.setPosition(788, 517);
		outsText.setColor(Color.WHITE);
		stage.addActor(outsText);
		
		float[] verts1 = {813, 515};
		ShapeActor outMarker1 = new ShapeActor(ShapeType.Filled, verts1, 4);
		outMarker1.setName("Out Marker 1");
		outMarker1.setColor(Color.BLACK);
		stage.addActor(outMarker1);
		
		float[] verts2 = {824, 515};
		ShapeActor outMarker2 = new ShapeActor(ShapeType.Filled, verts2, 4);
		outMarker2.setName("Out Marker 2");
		outMarker2.setColor(Color.BLACK);
		stage.addActor(outMarker2);
		
		addLineupTextToStage();
	}
	
	/**
	 * Adds the lineup text to the stage
	 */
	public void addLineupTextToStage() {
		int x1 = 10;
		int x2 = 25;
		int x3 = 38;
		int y = 125;
		int ydiff = 14;
		int size = 14;
		String font = "aero_matics_display_italic.ttf";
		
		Image lineupBackground = new Image(sd.boardNP);
		lineupBackground.setPosition(0, 0);
		lineupBackground.setSize(150, 138);
		stage.addActor(lineupBackground);
		
		for (int i = 0; i < 9; i++) {
			TextActor num = new TextActor(Integer.toString(i + 1), font, size);
			num.setName("Lineup Num " + (i + 1));
			num.setPosition(x1, y);
			num.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
			stage.addActor(num);
			TextActor pos = new TextActor(game.awayTeam.getPosition(game.awayTeam.lineup.get(i)), font, size);
			pos.setName("Lineup Position " + (i + 1));
			pos.setPosition(x2, y);
			pos.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
			stage.addActor(pos);
			TextActor name = new TextActor(game.awayTeam.lineup.get(i).name, font, size);
			int s = 1;
			while (name.getWidth() > 100) {
				name.setSize(size - s);
				s++;
			}
			name.setName("Lineup Name " + (i + 1));
			name.setPosition(x3 + name.getWidth()/2, y);
			name.setColor(i == 0 ? Color.YELLOW : Color.WHITE);
			stage.addActor(name);
			y -= ydiff;
		}
	}
	
	/**
	 * Sets the pitch text based on the pitch roll
	 */
	public void setPitchText() {
		TextActor text = (TextActor) stage.getRoot().findActor("Pitch Text");
		if ((game.isTop && game.isPChart && game.homeTeam.user == sd.user) || 
				(!game.isTop && !game.isPChart && game.homeTeam.user == sd.user)) {
			text.setColor(Color.GREEN);
		} else {
			text.setColor(Color.RED);
		}
		String pitchText = game.pitch > 0 ? Integer.toString(game.pitch) : "";
		text.setText(pitchText);
	}
	
	/**
	 * Sets the swing text based on the swing roll
	 */
	public void setSwingText() {
		TextActor text = (TextActor) stage.getRoot().findActor("Swing Text");
		if ((game.isTop && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) <= 3 && game.homeTeam.user == sd.user) || 
				(!game.isTop && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) > 3 && game.homeTeam.user == sd.user)) {
			text.setColor(Color.GREEN);
		} else {
			text.setColor(Color.RED);
		}
		String swingText = game.swing > 0 ? Integer.toString(game.swing) : "";
		text.setText(swingText);
	}
	
	/**
	 * Sets the chart text (Batter's or Pitcher's) based on the 
	 */
	public void setChartText() {
		TextActor text = (TextActor) stage.getRoot().findActor("Chart Text");
		if ((game.isTop && game.isPChart && game.homeTeam.user == sd.user) || 
				(!game.isTop && !game.isPChart && game.homeTeam.user == sd.user)) {
			text.setColor(Color.GREEN);
		} else {
			text.setColor(Color.RED);
		}
		text.setText(game.pitch == 0 ? "" : (game.isPChart ? "Pitcher's Chart" : "Batter's Chart"));
	}
	
	/**
	 * Sets the result text based on the result
	 */
	public void setResultText() {
		TextActor text = (TextActor) stage.getRoot().findActor("Result Text");
		if ((game.isTop && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) <= 3 && game.homeTeam.user == sd.user) || 
				(!game.isTop && Arrays.asList(CardConstants.CHART_TEXT).indexOf(game.result) > 3 && game.homeTeam.user == sd.user)) {
			text.setColor(Color.GREEN);
		} else {
			text.setColor(Color.RED);
		}
		text.setText(game.swing > 0 ? game.result : "");
	}
	
	/**
	 * Sets the inning text based on the inning
	 */
	public void setInningText() {
		TextActor text = (TextActor) stage.getRoot().findActor("Inning Text");
		text.setText(Integer.toString(game.inning));
	}
	
	/**
	 * Sets the out markers based on the number of outs
	 */
	public void setOutMarkers() {
		ShapeActor marker1 = (ShapeActor) stage.getRoot().findActor("Out Marker 1");
		ShapeActor marker2 = (ShapeActor) stage.getRoot().findActor("Out Marker 2");
		if (game.outs == 0) {
			marker1.setColor(Color.BLACK);
			marker2.setColor(Color.BLACK);
		} else if (game.outs == 1) {
			marker1.setColor(Color.YELLOW);
			marker2.setColor(Color.BLACK);
		} else {
			marker1.setColor(Color.YELLOW);
			marker2.setColor(Color.YELLOW);
		}
	}
	
	/**
	 * Sets the top or bottom arrow by the inning
	 */
	public void setTopBottomInningMarker() {
		TextActor act = (TextActor) stage.getRoot().findActor("Inning Text");
		int x = (int) (act.getX() + act.getWidth()/2);
		float[] itverts = {x + 5,571,x + 10,576,x + 15,571};
		ShapeActor markert = (ShapeActor) stage.getRoot().findActor("Inning Top Marker");
		markert.setVertices(itverts);
		float[] ibverts = {x + 5,569,x + 10,564,x + 15,569};
		ShapeActor markerb = (ShapeActor) stage.getRoot().findActor("Inning Bottom Marker");
		markerb.setVertices(ibverts);
		if (game.isTop) {
			markert.setVisible(true);
			markerb.setVisible(false);
		} else {
			markert.setVisible(false);
			markerb.setVisible(true);
		}
	}
	
	/**
	 * Sets the away and home score
	 */
	public void setScoreText() {
		TextActor home = (TextActor) stage.getRoot().findActor("Home Score");
		TextActor away = (TextActor) stage.getRoot().findActor("Away Score");
		home.setText(Integer.toString(game.homeScore));
		away.setText(Integer.toString(game.awayScore));
	}

	/**
	 * Disables the swing or pitch buttons based on which player is the user
	 */
	public void disableButtons() {
		TextButton pitchButton = (TextButton) stage.getRoot().findActor("Pitch Button");
		TextButton swingButton = (TextButton) stage.getRoot().findActor("Swing Button");
		if ((game.isTop && !sd.user.equals(game.homeTeam.user)) || (!game.isTop && !sd.user.equals(game.awayTeam.user))) {
			pitchButton.setTouchable(Touchable.disabled);
		} else {
			pitchButton.setTouchable(Touchable.enabled);
		}
		if ((game.isTop && !sd.user.equals(game.awayTeam.user)) || (!game.isTop && !sd.user.equals(game.homeTeam.user))) {
			swingButton.setTouchable(Touchable.disabled);
		} else {
			swingButton.setTouchable(Touchable.enabled);
		}
		if (game.pitch > 0) pitchButton.setTouchable(Touchable.disabled);
		if (game.swing > 0 || game.pitch == 0) swingButton.setTouchable(Touchable.disabled);
	}
	
	/**
	 * Highlights the current batter in the lineup and removes the highlight from other batters
	 */
	public void setLineupHighlight() {
		int index = 0;
		if (game.isTop) {
			index = game.awayTeam.lineupSpot + 1;
		} else {
			index = game.homeTeam.lineupSpot + 1;
		}
		for (int i = 1; i < 10; i++) {
			Color c = Color.WHITE;
			if (i == index) {
				c = Color.YELLOW;
			}
			stage.getRoot().findActor("Lineup Num " + i).setColor(c);;
			stage.getRoot().findActor("Lineup Position " + i).setColor(c);
			stage.getRoot().findActor("Lineup Name " + i).setColor(c);
		}
	}
	
	/**
	 * Sets the Lineup text to the lineup for the current batting team
	 */
	public void setLineupText() {
		Team team = null;
		if (game.isTop) {
			team = game.awayTeam;
		} else {
			team = game.homeTeam;
		}
		for (int i = 0; i < 9; i++) {
			TextActor pos = (TextActor) stage.getRoot().findActor("Lineup Position " + (i + 1));
			TextActor name = (TextActor) stage.getRoot().findActor("Lineup Name " + (i + 1));
			pos.setText(team.getPosition(team.lineup.get(i)));
			name.setText(team.lineup.get(i).name);
			name.setPosition(38 + name.getWidth()/2, name.getY());
		}
	}
}
