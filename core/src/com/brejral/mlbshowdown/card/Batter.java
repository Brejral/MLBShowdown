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
import com.badlogic.gdx.sql.DatabaseFactory;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.MLBShowdown;

public class Batter extends Card {
	int onbase, speed;
	int[] chart = new int[10];
	String bats;
	FreeTypeFontGenerator straightGenerator;
	FreeTypeFontGenerator slantGenerator;
	FreeTypeFontParameter nameFontParameter;
	BitmapFont nameFont;

	public Batter(int iden) {
		id = iden;
		populateFieldsFromDB();
		backgroundTexture = new Texture(Gdx.files.internal("images/" + image));
		straightGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));
		slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
		nameFontParameter = new FreeTypeFontParameter();
		nameFontParameter.size = 39;
		nameFont = slantGenerator.generateFont(nameFontParameter);
		createTexture();
	}
	
	private void populateFieldsFromDB() {
		Database db = DatabaseFactory.getNewDatabase(MLBShowdown.DATABASE_NAME, MLBShowdown.DATABASE_VERSION, null, null);
//		db.setupDatabase();
		
		DatabaseCursor cursor = null;
		try {
			db.openOrCreateDatabase();
			cursor = db.rawQuery("Select * from main.players where id = "+ id + ";");
			name = cursor.getString(2);
			image = cursor.getString(24);
		} catch (SQLiteGdxException e) {
			e.printStackTrace();
		}
	}
	
	public void createTexture () {
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
		nameFont.setColor(Color.RED);
		int ntx, nty; //Name Text X and Y Positions
		ntx = 105;
		nty = 186;
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
		
		batch.end();
		
		fbo.end();
	}

}
