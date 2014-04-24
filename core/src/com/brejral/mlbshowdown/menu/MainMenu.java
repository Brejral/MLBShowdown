package com.brejral.mlbshowdown.menu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.brejral.mlbshowdown.MLBShowdown;

public class MainMenu implements Screen {
	final MLBShowdown mlbShowdown;
	SpriteBatch batch;
	Texture backgroundTexture;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter fontParameter;
	BitmapFont aeroDisplayItalicFont72;
	BitmapFont aeroDisplayItalicFont36;
	List<MenuItem> menuList;
	
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
		menuList = new ArrayList<MenuItem>();
		menuList.add(new MenuItem("Exhibition", 800, new ExhibitionMenu(mlbShowdown)));
		menuList.add(new MenuItem("Season", 750));
		menuList.add(new MenuItem("Tournament", 700));
		menuList.add(new MenuItem("Management", 650));
		menuList.add(new MenuItem("Statistics", 600));
		menuList.add(new MenuItem("Settings", 550));
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.setColor(1, 1, 1, .5f);
		batch.draw(backgroundTexture, 0, 0);
		drawMenuText();
		batch.end();
		
		processUserInput();
	}
	
	private void drawMenuText() {
		aeroDisplayItalicFont72.draw(batch, "MLB Showdown 2014", 10, 890);
		for (Iterator<MenuItem> i = menuList.iterator(); i.hasNext(); ) {
			MenuItem item = i.next();
			if (item.isHighlighted()) {
				aeroDisplayItalicFont36.setColor(Color.YELLOW);
			} else {
				aeroDisplayItalicFont36.setColor(Color.WHITE);
			}
			aeroDisplayItalicFont36.draw(batch, item.text, item.positionX, item.positionY);
		}
		
	}
	
	private void processUserInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			for (Iterator<MenuItem> i = menuList.iterator(); i.hasNext(); ) {
				MenuItem item = i.next();
				if (item.isHighlighted()) {
					if (item.screen != null) {
						mlbShowdown.setScreen(item.screen);
					}
				}
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
		backgroundTexture.dispose();
		aeroDisplayItalicFont72.dispose();
		aeroDisplayItalicFont36.dispose();
		batch.dispose();
		generator.dispose();
	}

	public class MenuItem {
		String text;
		int positionX, positionY;
		Screen screen;
		
		public MenuItem(String str, int y, Screen scrn) {
			this.text = str;
			this.positionX = 50;
			this.positionY = y;
			this.screen = scrn;
		}
		
		public MenuItem(String str, int y) {
			this.text = str;
			this.positionX = 50;
			this.positionY = y;
			this.screen = null;
		}
		
		public boolean isHighlighted() {
			int posX = Gdx.input.getX();
			int posY = mlbShowdown.screenSizeY - Gdx.input.getY();
			TextBounds bounds = aeroDisplayItalicFont36.getBounds(this.text);
			if (posX >= this.positionX && posX <= this.positionX + bounds.width &&
					posY <= this.positionY && posY >= (this.positionY - bounds.height)) {
				return true;
			}
			return false;
		}
		
		public void onClick() {
			
		}
	}
}
