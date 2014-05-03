package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.sql.Database;
import com.badlogic.gdx.sql.DatabaseCursor;
import com.badlogic.gdx.sql.SQLiteGdxException;
import com.brejral.mlbshowdown.MLBShowdown;

public class Card {
	public Database db;
	public String cardType, name, image, team, rarity, bats, throwHand, pos1, pos2, icons;
	public Texture backgroundTexture;
	public TextureRegion cardTexture;
	public float scale;
	public int onbase, control, ip, speed, posX, posY, cardnum, id, points, posBonus1, posBonus2;
	public int controlAdj, ipAdj;
	public int[] chart = new int[10];
	public FreeTypeFontGenerator obcGenerator;
	public FreeTypeFontGenerator chartGenerator;
	public FreeTypeFontGenerator slantGenerator;
	public FreeTypeFontParameter nameFontParameter;
	public FreeTypeFontParameter obcFontParameter;
	public FreeTypeFontParameter chartFontParameter;
	public BitmapFont nameFont;
	public BitmapFont obcFont;
	public BitmapFont chartFont;
	public BitmapFont cnFont;
	
	public Card() {
		
	}
	
	public Card(Database database, int iden) {
		db = database;
		id = iden;
		getCardDataFromDB();
		backgroundTexture = new Texture(Gdx.files.internal("images/" + image));
		obcGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muro.ttf"));
		chartGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));
		slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
		nameFontParameter = new FreeTypeFontParameter();
		nameFontParameter.size = 39;
		nameFont = slantGenerator.generateFont(nameFontParameter);
		chartFontParameter = new FreeTypeFontParameter();
		chartFontParameter.size = 18;
		chartFont = chartGenerator.generateFont(chartFontParameter);
		chartFontParameter.size = 15;
		cnFont = chartGenerator.generateFont(chartFontParameter);
		obcFontParameter = new FreeTypeFontParameter();
		obcFontParameter.size = 45;
		if (cardType.equals("Pitcher")) {
			obcFontParameter.size -= 5;
		}
		obcFont = obcGenerator.generateFont(obcFontParameter);
		createTexture();
	}
	
	private void getCardDataFromDB() {
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
				ip = cursor.getInt(11);
				control = cursor.getInt(12);
				break;
			}
			icons = cursor.getString(13);
			if (cardType.equals("Batter") || cardType.equals("Pitcher")) {
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
		FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, 510, 710, false);
		cardTexture = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
		cardTexture.flip(false, true);
		
		fbo.begin();
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.getProjectionMatrix().setToOrtho2D(0, 0, fbo.getWidth(), fbo.getHeight());
		batch.begin();
		
		batch.setBlendFunction(-1, -1);
		Gdx.gl20.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE);
		
		// Piece the image together
		batch.draw(backgroundTexture, 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
		batch.draw(CardConstants.CHART_TEXTURE, 0, 60, CardConstants.CHART_TEXTURE.getWidth(), CardConstants.CHART_TEXTURE.getHeight());
		batch.draw(CardConstants.CONTROL_TEXTURE, 10, 130, CardConstants.CONTROL_TEXTURE.getWidth(), CardConstants.CONTROL_TEXTURE.getHeight());

		drawNameText(batch);
		
		//Draw the control text
		int ctx, cty;
		String cntrlString = "+" + control;
		ctx = 45 - (int)obcFont.getBounds(cntrlString).width/2;
		cty = 189;
		obcFont.setColor(Color.BLACK);
		obcFont.draw(batch, cntrlString, ctx-1, cty-1);
		obcFont.draw(batch, cntrlString, ctx,   cty-1);
		obcFont.draw(batch, cntrlString, ctx-1, cty+1);
		obcFont.draw(batch, cntrlString, ctx,   cty+1);
		obcFont.draw(batch, cntrlString, ctx-1, cty);
		obcFont.draw(batch, cntrlString, ctx+1, cty-1);
		obcFont.draw(batch, cntrlString, ctx+1, cty);
		obcFont.draw(batch, cntrlString, ctx+1, cty+1);
		obcFont.setColor(Color.RED);
		obcFont.draw(batch, cntrlString, ctx, cty);
		
		drawPitcherChartText(batch);
		
		drawTeamLogo(batch);
		
		batch.end();
		
		fbo.end();
	}

	private void createBatterTexture () {
		SpriteBatch batch = new SpriteBatch();
		FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, 510, 710, false);
		cardTexture = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
		cardTexture.flip(false, true);
		
		fbo.begin();
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.getProjectionMatrix().setToOrtho2D(0, 0, fbo.getWidth(), fbo.getHeight());
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
		
		drawBatterChartText(batch);
		
		drawTeamLogo(batch);
		
		batch.end();
		
		fbo.end();
	}
	
	private void drawBatterChartText(SpriteBatch batch) {
		//Draw the chart text
		chartFont.setColor(Color.WHITE);
		int ctx, cty;
		ctx = 105;
		cty = 145;
		String ptString = points + " PT.";
		chartFont.draw(batch, ptString, ctx, cty);
		
		ctx += (int)chartFont.getBounds(ptString).width + 20;
		StringBuilder builder = new StringBuilder("SPEED ");
		if (speed >= 18) {
			builder.append("A (");
		} else if (speed >= 13) {
			builder.append("B (");
		} else {
			builder.append("C (");
		}
		builder.append(speed + ")");
		String speedString = builder.toString();
		chartFont.draw(batch, speedString, ctx, cty);
		
		ctx += (int)chartFont.getBounds(speedString).width + 20;
		String batsString = "BATS " + bats;
		chartFont.draw(batch, batsString, ctx, cty);
		
		ctx += (int)chartFont.getBounds(batsString).width + 20;
		String position1String = pos1 + " + " + posBonus1;
		chartFont.draw(batch, position1String, ctx, cty);
		
		if (pos2 != null) {
			ctx += (int)chartFont.getBounds(position1String).width + 20;
			String position2String = pos2 + " + " + posBonus2;
			chartFont.draw(batch, position2String, ctx, cty);
		}
		
		// Draw the chart text and results
		cty -= 25;
		ctx = 47;
		int startNum = 1;
		String resultString = null;
		for (int i = 0; i < chart.length; i++) {
			if (i != 0) {
				if (i == 9) {
					resultString = chart[i] + "+";
				} else if (chart[i] == startNum) {
					resultString = Integer.toString(chart[i]);
				} else if (chart[i] > startNum) {
					resultString = startNum + "---" + chart[i];
				} else {
					resultString = "---";
				}
				int strLen = (int)chartFont.getBounds(resultString).width;
				chartFont.draw(batch, resultString, ctx - strLen/2, cty);
				strLen = (int)chartFont.getBounds(CardConstants.CHART_TEXT[i]).width;
				chartFont.draw(batch, CardConstants.CHART_TEXT[i], ctx - strLen/2, cty - 25);
				startNum = chart[i] + 1;
				ctx += 52;
			}
		}
		
		drawCardNumText(batch);
		
	}
	
	private void drawPitcherChartText(SpriteBatch batch) {
		//Draw the chart text
				chartFont.setColor(Color.WHITE);
				int ctx, cty;
				ctx = 105;
				cty = 145;
				String ptString = points + " PT.";
				chartFont.draw(batch, ptString, ctx, cty);
				
				ctx += (int)chartFont.getBounds(ptString).width + 20;
				String position1String = pos1;
				chartFont.draw(batch, position1String, ctx, cty);
				
				ctx += (int)chartFont.getBounds(position1String).width + 20;
				String throwsString = throwHand + "HP";
				chartFont.draw(batch, throwsString, ctx, cty);
				
				ctx += (int)chartFont.getBounds(throwsString).width + 20;
				String speedString = "IP " + ip;
				chartFont.draw(batch, speedString, ctx, cty);
				
				// Draw the chart text and results
				cty -= 25;
				ctx = 52;
				int startNum = 1;
				String resultString = null;
				for (int i = 0; i < chart.length; i++) {
					if (i != 6 && i != 8) {
						if (i == 9) {
							resultString = chart[i] + "+";
						} else if (chart[i] == startNum) {
							resultString = Integer.toString(chart[i]);
						} else if (chart[i] > startNum) {
							resultString = startNum + "---" + chart[i];
						} else {
							resultString = "---";
						}
						int strLen = (int)chartFont.getBounds(resultString).width;
						chartFont.draw(batch, resultString, ctx - strLen/2, cty);
						strLen = (int)chartFont.getBounds(CardConstants.CHART_TEXT[i]).width;
						chartFont.draw(batch, CardConstants.CHART_TEXT[i], ctx - strLen/2, cty - 25);
						startNum = chart[i] + 1;
						ctx += 58;
					}
				}
				
				drawCardNumText(batch);
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
	
	private void drawCardNumText(SpriteBatch batch) {
		// Draw the year and the card number
		if (cardnum != 0) {
			int ctx = 420;
			int cty = 73;
			String cardnumString = Integer.toString(cardnum);
			String yearString = cardnumString.substring(0, 2);
			String numString = cardnumString.substring(2, 5);
			String yearNumString = numString + "          " + "'" + yearString;
			cnFont.draw(batch, yearNumString, ctx, cty);
		}
	}
	
	private void drawTeamLogo(SpriteBatch batch) {
		Texture tex = null;
		if (rarity.equals("P")) {
			tex = CardConstants.TEAM_LOGOS_GOLD_TEXTURE;
		} else {
			tex = CardConstants.TEAM_LOGOS_TEXTURE;
		}
		int index = MLBShowdown.getTeamNamesList().indexOf(team) + 1;
		int row = (int) Math.ceil((float)index/6f);
		int col = index - (row-1)*6;
		Sprite logo = new Sprite(tex, 200*(col - 1), 200*(row - 1), 200, 200);
		logo.setScale(.4f);
		logo.setPosition(340, 100);
		logo.draw(batch);
	}
}
