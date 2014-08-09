package com.brejral.mlbshowdown.card;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.brejral.mlbshowdown.AnimationRunnable;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.game.Game;

public class CardConstants {
   public static int CARD_WIDTH = 510;
   public static int CARD_HEIGHT = 710;
	public static Texture CHART_TEXTURE = new Texture(Gdx.files.internal("images/chart_background.png"));
	public static Texture ONBASE_TEXTURE = new Texture(Gdx.files.internal("images/onbase_background.png"));
	public static Texture CONTROL_TEXTURE = new Texture(Gdx.files.internal("images/control_background.png"));
	public static String[] CHART_TEXT = {"PU", "SO", "GB", "FB", "BB", "1B", "1B+", "2B", "3B", "HR"};
	public static String[] POSITION_TEXT = {"DH","P","C","1B","2B","3B","SS","LF","CF","RF"};
	public static Texture TEAM_LOGOS_TEXTURE = new Texture(Gdx.files.internal("images/team_logos.png"));
	public static Texture TEAM_LOGOS_GOLD_TEXTURE = new Texture(Gdx.files.internal("images/team_logos_gold.png"));
	public static int TEAM_LOGOS_NUM_X = 6;
	public static int TEAM_LOGOS_NUM_Y = 5;
	public static String[] CARD_INFO_COLS = {
      "ID","CARDNUM","NAME","TEAM","POINTS","RARITY","POSITION1","POSITIONBONUS1","POSITION2","POSITIONBONUS2",
      "BATS_THROWS","SPEED_IP","ONBASE_CONTROL","ICONS","PU","SO","GB","FB","BB","SINGLE","SINGLEPLUS","DOUBLE2", 
      "TRIPLE","HR","IMAGE","CARDTYPE","LASTNAME"
   };
	public static List<String> CARD_INFO = Arrays.asList(CARD_INFO_COLS);
	public static String[] CARD_INFO_COL_TYPES = {
	   "N","N","S","S","N","S","S","N","S","N","S","N","N","S","N","N","N","N","N","N","N","N","N","N","S","S","S"
	};
	public static BitmapFont NAME_FONT = MLBShowdown.getMuroslantFont(39);
	public static BitmapFont CHART_FONT = MLBShowdown.getUS101Font(18);
	public static BitmapFont CARDNUM_FONT = MLBShowdown.getUS101Font(16);
	public static BitmapFont CONTROL_FONT = MLBShowdown.getMuroFont(40);
	public static BitmapFont ONBASE_FONT = MLBShowdown.getMuroFont(45);
   
   
	public static SequenceAction getRunToFirstAction(Game game, CardActor card) {
		SequenceAction action = new SequenceAction();
		MoveToAction action1 = new MoveToAction();
		action1.setPosition(625, 260);
		action1.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action1);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
	
	public static SequenceAction getRunToSecondAction(Game game, CardActor card) {
		SequenceAction action = new SequenceAction();
		if (card.getX() == 450 && card.getY() == 75) {
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(625, 260);
			action1.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action1);
		}
		MoveToAction action2 = new MoveToAction();
		action2.setPosition(450, 450);
		action2.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action2);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
	
	public static SequenceAction getRunToThirdAction(Game game, CardActor card) {
		SequenceAction action = new SequenceAction();
		if (card.getX() == 450 && card.getY() == 75) {
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(625, 260);
			action1.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action1);
		}
		if ((card.getX() == 450 && card.getY() == 75) ||
				(card.getX() == 625 && card.getY() == 260)) {
			MoveToAction action2 = new MoveToAction();
			action2.setPosition(450, 450);
			action2.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action2);
		}
		MoveToAction action3 = new MoveToAction();
		action3.setPosition(275, 260);
		action3.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action3);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
	
	public static SequenceAction getRunToHomeAction(Game game, CardActor card) {
		SequenceAction action = new SequenceAction();
      if (card.getX() == 450 && card.getY() == 75) {
			MoveToAction action1 = new MoveToAction();
			action1.setPosition(625, 260);
			action1.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action1);
		}
		if ((card.getX() == 450 && card.getY() == 75) ||
				(card.getX() == 625 && card.getY() == 260)) {
			MoveToAction action2 = new MoveToAction();
			action2.setPosition(450, 450);
			action2.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action2);
		}
		if ((card.getX() == 450 && card.getY() == 75) ||
				(card.getX() == 625 && card.getY() == 260) ||
				(card.getX() == 450 && card.getY() == 450)) {
			MoveToAction action3 = new MoveToAction();
			action3.setPosition(275, 260);
			action3.setDuration(MLBShowdown.ANIMATION_SPEED);
			action.addAction(action3);
		}
		MoveToAction action4 = new MoveToAction();
		action4.setPosition(450, 75);
		action4.setDuration(MLBShowdown.ANIMATION_SPEED);
		action.addAction(action4);
		AnimationRunnable runnable = new AnimationRunnable(game);
		action.addAction(runnable);
		return action;
	}
	
	public static void createTexture(Card card) {
      switch (card.cardType) {
      case "Batter":
         createBatterTexture(card);
         break;
      case "Pitcher":
         createPitcherTexture(card);
         break;
      case "Strategy":
         createStrategyTexture(card);
         break;
      }
      card.isImageLoaded = true;
   }

   private static void createStrategyTexture(Card card) {

   }

   private static void createPitcherTexture(Card card) {
      Texture backgroundTexture = new Texture(Gdx.files.internal("images/" + card.image));
      SpriteBatch batch = new SpriteBatch();
      FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, 510, 710, false);
      card.cardTexture = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
      card.cardTexture.flip(false, true);

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

      createNameText(batch, card);

      // Draw the control text
      int ctx, cty;
      String cntrlString = "+" + card.controlAdj;
      ctx = 45 - (int) CardConstants.CONTROL_FONT.getBounds(cntrlString).width / 2;
      cty = 189;
      CardConstants.CONTROL_FONT.setColor(Color.BLACK);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx - 1, cty - 1);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx, cty - 1);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx - 1, cty + 1);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx, cty + 1);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx - 1, cty);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx + 1, cty - 1);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx + 1, cty);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx + 1, cty + 1);
      CardConstants.CONTROL_FONT.setColor(Color.RED);
      CardConstants.CONTROL_FONT.draw(batch, cntrlString, ctx, cty);

      createPitcherChartText(batch, card);

      createTeamLogo(batch, card);

      batch.end();

      fbo.end();
   }

   private static void createBatterTexture(Card card) {
      Texture backgroundTexture = new Texture(Gdx.files.internal("images/" + card.image));
      SpriteBatch batch = new SpriteBatch();
      FrameBuffer fbo = new FrameBuffer(Format.RGBA8888, 510, 710, false);
      card.cardTexture = new TextureRegion(fbo.getColorBufferTexture(), 0, 0, backgroundTexture.getWidth(), backgroundTexture.getHeight());
      card.cardTexture.flip(false, true);

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

      createNameText(batch, card);

      // Draw the onbase text
      int obtx, obty;
      String obString = Integer.toString(card.onbase);
      obtx = 51 - (int) CardConstants.ONBASE_FONT.getBounds(obString).width / 2;
      obty = 194;
      CardConstants.ONBASE_FONT.setColor(Color.BLACK);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx - 1, obty - 1);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx, obty - 1);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx - 1, obty + 1);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx, obty + 1);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx - 1, obty);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx + 1, obty - 1);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx + 1, obty);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx + 1, obty + 1);
      CardConstants.ONBASE_FONT.setColor(Color.RED);
      CardConstants.ONBASE_FONT.draw(batch, obString, obtx, obty);

      createBatterChartText(batch, card);

      createTeamLogo(batch, card);

      batch.end();

      fbo.end();
   }

   private static void createBatterChartText(SpriteBatch batch, Card card) {
      // Draw the chart text
      CardConstants.CHART_FONT.setColor(Color.WHITE);
      int ctx, cty;
      ctx = 105;
      cty = 145;
      String ptString = card.points + " PT.";
      CardConstants.CHART_FONT.draw(batch, ptString, ctx, cty);

      ctx += (int) CardConstants.CHART_FONT.getBounds(ptString).width + 20;
      StringBuilder builder = new StringBuilder("SPEED ");
      if (card.speed >= 18) {
         builder.append("A (");
      } else if (card.speed >= 13) {
         builder.append("B (");
      } else {
         builder.append("C (");
      }
      builder.append(card.speed + ")");
      String speedString = builder.toString();
      CardConstants.CHART_FONT.draw(batch, speedString, ctx, cty);

      ctx += (int) CardConstants.CHART_FONT.getBounds(speedString).width + 20;
      String batsString = "BATS " + card.bats;
      CardConstants.CHART_FONT.draw(batch, batsString, ctx, cty);

      ctx += (int) CardConstants.CHART_FONT.getBounds(batsString).width + 20;
      String position1String = card.pos1 + " + " + card.posBonus1;
      CardConstants.CHART_FONT.draw(batch, position1String, ctx, cty);

      if (card.pos2 != null && !card.pos2.isEmpty()) {
         ctx += (int) CardConstants.CHART_FONT.getBounds(position1String).width + 20;
         String position2String = card.pos2 + " + " + card.posBonus2;
         CardConstants.CHART_FONT.draw(batch, position2String, ctx, cty);
      }

      // Draw the chart text and results
      cty -= 25;
      ctx = 47;
      int startNum = 1;
      String resultString = null;
      for (int i = 0; i < card.chart.length; i++) {
         if (i != 0) {
            if (i == 9) {
               resultString = card.chart[i] + "+";
            } else if (card.chart[i] == startNum) {
               resultString = Integer.toString(card.chart[i]);
            } else if (card.chart[i] > startNum) {
               resultString = startNum + "---" + card.chart[i];
            } else {
               resultString = "---";
            }
            int strLen = (int) CardConstants.CHART_FONT.getBounds(resultString).width;
            CardConstants.CHART_FONT.draw(batch, resultString, ctx - strLen / 2, cty);
            strLen = (int) CardConstants.CHART_FONT.getBounds(CardConstants.CHART_TEXT[i]).width;
            CardConstants.CHART_FONT.draw(batch, CardConstants.CHART_TEXT[i], ctx - strLen / 2, cty - 25);
            startNum = card.chart[i] + 1;
            ctx += 52;
         }
      }

      createCardNumText(batch, card);
   }

   private static void createPitcherChartText(SpriteBatch batch, Card card) {
      // Draw the chart text
      CardConstants.CHART_FONT.setColor(Color.WHITE);
      int ctx, cty;
      ctx = 105;
      cty = 145;
      String ptString = card.points + " PT.";
      CardConstants.CHART_FONT.draw(batch, ptString, ctx, cty);

      ctx += (int) CardConstants.CHART_FONT.getBounds(ptString).width + 20;
      String position1String = card.pos1;
      CardConstants.CHART_FONT.draw(batch, position1String, ctx, cty);

      ctx += (int) CardConstants.CHART_FONT.getBounds(position1String).width + 20;
      String throwsString = card.throwHand + "HP";
      CardConstants.CHART_FONT.draw(batch, throwsString, ctx, cty);

      ctx += (int) CardConstants.CHART_FONT.getBounds(throwsString).width + 20;
      String speedString = "IP " + card.ipAdj;
      if (card.ipAdj < 1) {
         CardConstants.CHART_FONT.setColor(Color.RED);
      }
      CardConstants.CHART_FONT.draw(batch, speedString, ctx, cty);
      CardConstants.CHART_FONT.setColor(Color.WHITE);

      // Draw the chart text and results
      cty -= 25;
      ctx = 52;
      int startNum = 1;
      String resultString = null;
      for (int i = 0; i < card.chart.length; i++) {
         if (i != 6 && i != 8) {
            if (i == 9) {
               resultString = card.chart[i] + "+";
            } else if (card.chart[i] == startNum) {
               resultString = Integer.toString(card.chart[i]);
            } else if (card.chart[i] > startNum) {
               resultString = startNum + "---" + card.chart[i];
            } else {
               resultString = "---";
            }
            int strLen = (int) CardConstants.CHART_FONT.getBounds(resultString).width;
            CardConstants.CHART_FONT.draw(batch, resultString, ctx - strLen / 2, cty);
            strLen = (int) CardConstants.CHART_FONT.getBounds(CardConstants.CHART_TEXT[i]).width;
            CardConstants.CHART_FONT.draw(batch, CardConstants.CHART_TEXT[i], ctx - strLen / 2, cty - 25);
            startNum = card.chart[i] + 1;
            ctx += 58;
         }
      }

      createCardNumText(batch, card);
   }

   private static void createNameText(SpriteBatch batch, Card card) {
      int ntx, nty; // Name Text X and Y Positions
      ntx = 105;
      nty = 186;
      BitmapFont font = CardConstants.NAME_FONT;
      while (font.getBounds(card.name).width > (400 - ntx)) {
         font = MLBShowdown.getMuroslantFont((int)(font.getLineHeight() - 3));
      }
      font.setColor(Color.RED);
      font.draw(batch, card.name, ntx - 1, nty);
      font.draw(batch, card.name, ntx - 1, nty - 1);
      font.draw(batch, card.name, ntx + 1, nty);
      font.draw(batch, card.name, ntx - 1, nty + 1);
      font.draw(batch, card.name, ntx, nty + 1);
      font.draw(batch, card.name, ntx + 1, nty - 1);
      font.draw(batch, card.name, ntx, nty - 1);
      font.draw(batch, card.name, ntx + 1, nty + 1);
      font.setColor(Color.WHITE);
      font.draw(batch, card.name, ntx, nty);
   }

   private static void createCardNumText(SpriteBatch batch, Card card) {
      // Draw the year and the card number
      if (card.cardnum != 0) {
         int ctx = 420;
         int cty = 74;
         String cardnumString = Integer.toString(card.cardnum);
         String yearString = cardnumString.substring(0, 2);
         String numString = cardnumString.substring(2, 5);
         String yearNumString = numString + "          " + "'" + yearString;
         CardConstants.CARDNUM_FONT.draw(batch, yearNumString, ctx, cty);
      }
   }

   private static void createTeamLogo(SpriteBatch batch, Card card) {
      Sprite logo = MLBShowdown.getLogo(card.team, card.rarity);
      logo.setScale(.4f);
      logo.setPosition(340, 100);
      logo.draw(batch);
   }
}
