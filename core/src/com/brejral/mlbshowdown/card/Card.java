package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;

public class Card {
	public Database db;
	public String cardType, name, image, team, rarity, bats, throwHand, pos1, pos2, icons;
	public Texture backgroundTexture;
	public TextureRegion cardTexture;
	public float scale;
	public int onbase, control, ip, speed, posX, posY, cardnum, id, points, posBonus1, posBonus2;
	public int[] chart = new int[10];
	public FreeTypeFontGenerator straightGenerator;
	public FreeTypeFontGenerator slantGenerator;
	public FreeTypeFontParameter nameFontParameter;
	public FreeTypeFontParameter obcFontParameter;
	public FreeTypeFontParameter chartFontParameter;
	public BitmapFont nameFont;
	public BitmapFont obcFont;
	public BitmapFont chartFont;
	
	public Card(Database database, int iden) {
		db = database;
		id = iden;
		populateFieldsFromDB();
		backgroundTexture = new Texture(Gdx.files.internal("images/" + image));
		straightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muro.ttf"));
		slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
		nameFontParameter = new FreeTypeFontParameter();
		nameFontParameter.size = 39;
		nameFont = slantGenerator.generateFont(nameFontParameter);
		chartFontParameter = new FreeTypeFontParameter();
		chartFontParameter.size = 18;
		chartFont = straightGenerator.generateFont(chartFontParameter);
		obcFontParameter = new FreeTypeFontParameter();
		obcFontParameter.size = 45;
		obcFont = straightGenerator.generateFont(obcFontParameter);
		createTexture();
	}
	
	private void populateFieldsFromDB() {
		DatabaseCursor cursor = null;
		try {
			cursor = db.rawQuery("Select * from cards where id = "+ id + ";");
			cardType = cursor.getString(25);
			cardnum = cursor.getInt(1);
			name = cursor.getString(2);
			team = cursor.getString(3);
			points = cursor.getInt(4);
			rarity = cursor.getString(5);
			pos1 = cursor.getString(6);
			switch(cardType) {
			case "Batter":
				posBonus1 = cursor.getInt(7);
				pos2 = cursor.getString(8);
				posBonus2 = cursor.getInt(9);
				bats = cursor.getString(10);
				speed = cursor.getInt(11);
				onbase = cursor.getInt(12);
				break;
			case "Pitcher":
				throwHand = cursor.getString(10);
				speed = cursor.getInt(11);
				onbase = cursor.getInt(12);
				break;
			}
			icons = cursor.getString(13);
			if (cardType == "Batter" || cardType == "Pitcher") {
				for (int i = 0; i < 10; i++) {
					chart[i] = cursor.getInt(14+i);
				}
			}
			image = cursor.getString(24);
			
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(cardTexture, posX, posY, cardTexture.getRegionWidth()*scale, cardTexture.getRegionHeight()*scale);
	}
	
	public void dispose() {
		backgroundTexture.dispose();
	}
	
	public void createTexture() {
		switch(cardType){
		case "Batter":
			createBatterTexture();
			break;
		case "Pitcher":
			createPitcherTexture();
			break;
		case "Strategy":
			createStrategyTexture();
			break;
		}
		
	}
	
	private void createStrategyTexture() {
		// TODO Auto-generated method stub
		
	}

	private void createPitcherTexture() {
		SpriteBatch batch = new SpriteBatch();
		FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		cardTexture = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
		cardTexture.flip(false, true);
		
		fbo.begin();
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		batch.setBlendFunction(-1, -1);
		Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
		
		// Piece the image together
		batch.draw(backgroundTexture, 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
		batch.draw(CardConstants.CHART_TEXTURE, 0, 60, CardConstants.CHART_TEXTURE.getWidth(), CardConstants.CHART_TEXTURE.getHeight());
		batch.draw(CardConstants.CONTROL_TEXTURE, 10, 130, CardConstants.CONTROL_TEXTURE.getWidth(), CardConstants.CONTROL_TEXTURE.getHeight());

		drawNameText(batch);
		
		batch.end();
		
		fbo.end();
	}

	private void createBatterTexture () {
		SpriteBatch batch = new SpriteBatch();
		FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
		cardTexture = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
		cardTexture.flip(false, true);
		
		fbo.begin();
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		
		batch.setBlendFunction(-1, -1);
		Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
		
		// Piece the image together
		batch.draw(backgroundTexture, 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
		batch.draw(CardConstants.CHART_TEXTURE, 0, 60, CardConstants.CHART_TEXTURE.getWidth(), CardConstants.CHART_TEXTURE.getHeight());
		batch.draw(CardConstants.ONBASE_TEXTURE, 10, 130, CardConstants.ONBASE_TEXTURE.getWidth(), CardConstants.ONBASE_TEXTURE.getHeight());
		
		drawNameText(batch);
		
		//Draw the onbase text
		int obtx, obty;
		String obString = Integer.toString(onbase);
		obtx = 51 - (int)obcFont.getBounds(obString).width/2;
		obty = 194;
		obcFont.setColor(Color.BLACK);
		obcFont.draw(batch, obString, obtx-1, obty-1);
		obcFont.draw(batch, obString, obtx,   obty-1);
		obcFont.draw(batch, obString, obtx-1, obty+1);
		obcFont.draw(batch, obString, obtx,   obty+1);
		obcFont.draw(batch, obString, obtx-1, obty);
		obcFont.draw(batch, obString, obtx+1, obty-1);
		obcFont.draw(batch, obString, obtx+1, obty);
		obcFont.draw(batch, obString, obtx+1, obty+1);
		obcFont.setColor(Color.RED);
		obcFont.draw(batch, obString, obtx, obty);
		
		//Draw the chart text
		int ctx, cty;
		ctx = 105;
		cty = 145;
		chartFont.setColor(Color.WHITE);
		String ptString = Integer.toString(points) + " PT.";
		chartFont.draw(batch, ptString, ctx, cty);
		
		batch.end();
		
		fbo.end();
	}
	
	private void drawNameText(SpriteBatch batch) {
		int ntx, nty; //Name Text X and Y Positions
		ntx = 105;
		nty = 186;
		nameFont.setColor(Color.RED);
		nameFont.draw(batch, name, ntx-1, nty);
		nameFont.draw(batch, name, ntx-1, nty-1);
		nameFont.draw(batch, name, ntx+1, nty);
		nameFont.draw(batch, name, ntx-1, nty+1);
		nameFont.draw(batch, name, ntx,   nty+1);
		nameFont.draw(batch, name, ntx+1, nty-1);
		nameFont.draw(batch, name, ntx,   nty-1);
		nameFont.draw(batch, name, ntx+1, nty+1);
		nameFont.setColor(Color.WHITE);
		nameFont.draw(batch, name, ntx, nty);
		
	}
	
}
