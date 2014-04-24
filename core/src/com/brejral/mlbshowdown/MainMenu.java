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
		menuList.Add(new MenuItem("Exhibition", 800);
		menuList.Add(new MenuItem("Season", 750);
		menuList.Add(new MenuItem("Tournament", 700);
		menuList.Add(new MenuItem("Management", 650);
		menuList.Add(new MenuItem("Statistics", 600);
		menuList.Add(new MenuItem("Settings", 550);
		
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
	}
	
	private void drawMenuText() {
		aeroDisplayItalicFont72.draw(batch, "MLB Showdown 2014", 10, 890);
		for (Iterator<MenuItem> i = menuList.iterator(); i.HasNext(); ) {
			MenuItem item = i.next();
			if (item.isHighlighted()) {
				batch.setColor(1, 1, 0, 1);
			} else {
				batch.setColor(1, 1, 1, 1);
			}
			aeroDisplayItalicFont36.draw(batch, item.text, 50, item.positionY);
		}
		aeroDisplayItalicFont36.draw(batch, "Exhibition", 50, 800);
		aeroDisplayItalicFont36.draw(batch, "Season", 50, 750);
		aeroDisplayItalicFont36.draw(batch, "Tournament", 50, 700);
		aeroDisplayItalicFont36.draw(batch, "Management", 50, 650);
		aeroDisplayItalicFont36.draw(batch, "Statistics", 50, 600);
		aeroDisplayItalicFont36.draw(batch, "Settings", 50, 550);
		
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
		fontParameter.dispose();
		aeroDisplayItalicFont72.dispose();
		aeroDisplayItalicFont36.dispose();
		batch.dispose();
		generator.dispose();
	}

	public class MenuItem {
		String text;
		int positionY;
		
		public MenuItem(String str, int y) {
			this.text = str;
			this.positionY = y;
		}
		
		public boolean isHighlighted() {
			
		}
	}
}
