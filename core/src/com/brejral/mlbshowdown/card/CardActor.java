package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.game.GameStage;

public class CardActor extends Actor implements Poolable {
   public Texture backgroundTexture;
   public TextureRegion cardTexture;
   public FreeTypeFontGenerator onbaseGenerator;
   public FreeTypeFontGenerator controlGenerator;
   public FreeTypeFontGenerator chartGenerator;
   public FreeTypeFontGenerator slantGenerator;
   public FreeTypeFontParameter fontParameter;
   public BitmapFont nameFont;
   public BitmapFont onbaseFont;
   public BitmapFont controlFont;
   public BitmapFont chartFont;
   public BitmapFont cnFont;
   public boolean isZoomed = false;
   public boolean isSelected = false;
   public boolean isImageLoaded = false;
   public float preZoomX, preZoomY, preZoomScale;
   public Card card;

   public CardActor() {
      onbaseGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muro.ttf"));
      controlGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muro.ttf"));
      chartGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/US101.TTF"));
      slantGenerator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Muroslant.ttf"));
      fontParameter = new FreeTypeFontParameter();
      fontParameter.size = 39;
      nameFont = slantGenerator.generateFont(fontParameter);
      fontParameter.size = 18;
      chartFont = chartGenerator.generateFont(fontParameter);
      fontParameter.size = 15;
      cnFont = chartGenerator.generateFont(fontParameter);
      fontParameter.size = 45;
      onbaseFont = onbaseGenerator.generateFont(fontParameter);
      fontParameter.size = 40;
      controlFont = controlGenerator.generateFont(fontParameter);
      setVisible(false);
      setTouchable(Touchable.enabled);
      addListener(new InputListener() {
         @Override
         public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            CardActor card = (CardActor) event.getTarget();
            GameStage gs = (GameStage) event.getStage();
            if (!isZoomed && !card.hasActions()) {
               card.zoom();
               gs.game.timer.stop();
            }
            return true;
         }
      });
   }
   
   public void setCardInfo(Card info) {
      card = info;
      createTexture();
   }

   /**
    * Draw the card to centered on it's position and scaled from its original
    * size of 510x710
    * 
    * @param batch
    *           - The sprite batch to draw to
    */
   @Override
   public void draw(Batch batch, float alpha) {
      int w = (int) (getWidth() * getScaleX());
      int h = (int) (getHeight() * getScaleY());
      if (!isImageLoaded) {
         createTexture();
      }
      batch.draw(cardTexture, getX() - w / 2, getY() - h / 2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
   }

   public void dispose() {
      backgroundTexture.dispose();
   }

   public void createTexture() {
      backgroundTexture = new Texture(Gdx.files.internal("images/" + card.image));
      switch (card.cardType) {
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
      setBounds(getX(), getY(), cardTexture.getRegionWidth(), cardTexture.getRegionHeight());
      isImageLoaded = true;
   }

   private void createStrategyTexture() {

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

      createNameText(batch);

      // Draw the control text
      int ctx, cty;
      String cntrlString = "+" + card.controlAdj;
      ctx = 45 - (int) controlFont.getBounds(cntrlString).width / 2;
      cty = 189;
      controlFont.setColor(Color.BLACK);
      controlFont.draw(batch, cntrlString, ctx - 1, cty - 1);
      controlFont.draw(batch, cntrlString, ctx, cty - 1);
      controlFont.draw(batch, cntrlString, ctx - 1, cty + 1);
      controlFont.draw(batch, cntrlString, ctx, cty + 1);
      controlFont.draw(batch, cntrlString, ctx - 1, cty);
      controlFont.draw(batch, cntrlString, ctx + 1, cty - 1);
      controlFont.draw(batch, cntrlString, ctx + 1, cty);
      controlFont.draw(batch, cntrlString, ctx + 1, cty + 1);
      controlFont.setColor(Color.RED);
      controlFont.draw(batch, cntrlString, ctx, cty);

      createPitcherChartText(batch);

      createTeamLogo(batch);

      batch.end();

      fbo.end();
   }

   private void createBatterTexture() {
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

      createNameText(batch);

      // Draw the onbase text
      int obtx, obty;
      String obString = Integer.toString(card.onbase);
      obtx = 51 - (int) onbaseFont.getBounds(obString).width / 2;
      obty = 194;
      onbaseFont.setColor(Color.BLACK);
      onbaseFont.draw(batch, obString, obtx - 1, obty - 1);
      onbaseFont.draw(batch, obString, obtx, obty - 1);
      onbaseFont.draw(batch, obString, obtx - 1, obty + 1);
      onbaseFont.draw(batch, obString, obtx, obty + 1);
      onbaseFont.draw(batch, obString, obtx - 1, obty);
      onbaseFont.draw(batch, obString, obtx + 1, obty - 1);
      onbaseFont.draw(batch, obString, obtx + 1, obty);
      onbaseFont.draw(batch, obString, obtx + 1, obty + 1);
      onbaseFont.setColor(Color.RED);
      onbaseFont.draw(batch, obString, obtx, obty);

      createBatterChartText(batch);

      createTeamLogo(batch);

      batch.end();

      fbo.end();
   }

   private void createBatterChartText(SpriteBatch batch) {
      // Draw the chart text
      chartFont.setColor(Color.WHITE);
      int ctx, cty;
      ctx = 105;
      cty = 145;
      String ptString = card.points + " PT.";
      chartFont.draw(batch, ptString, ctx, cty);

      ctx += (int) chartFont.getBounds(ptString).width + 20;
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
      chartFont.draw(batch, speedString, ctx, cty);

      ctx += (int) chartFont.getBounds(speedString).width + 20;
      String batsString = "BATS " + card.bats;
      chartFont.draw(batch, batsString, ctx, cty);

      ctx += (int) chartFont.getBounds(batsString).width + 20;
      String position1String = card.pos1 + " + " + card.posBonus1;
      chartFont.draw(batch, position1String, ctx, cty);

      if (card.pos2 != null) {
         ctx += (int) chartFont.getBounds(position1String).width + 20;
         String position2String = card.pos2 + " + " + card.posBonus2;
         chartFont.draw(batch, position2String, ctx, cty);
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
            int strLen = (int) chartFont.getBounds(resultString).width;
            chartFont.draw(batch, resultString, ctx - strLen / 2, cty);
            strLen = (int) chartFont.getBounds(CardConstants.CHART_TEXT[i]).width;
            chartFont.draw(batch, CardConstants.CHART_TEXT[i], ctx - strLen / 2, cty - 25);
            startNum = card.chart[i] + 1;
            ctx += 52;
         }
      }

      createCardNumText(batch);
   }

   private void createPitcherChartText(SpriteBatch batch) {
      // Draw the chart text
      chartFont.setColor(Color.WHITE);
      int ctx, cty;
      ctx = 105;
      cty = 145;
      String ptString = card.points + " PT.";
      chartFont.draw(batch, ptString, ctx, cty);

      ctx += (int) chartFont.getBounds(ptString).width + 20;
      String position1String = card.pos1;
      chartFont.draw(batch, position1String, ctx, cty);

      ctx += (int) chartFont.getBounds(position1String).width + 20;
      String throwsString = card.throwHand + "HP";
      chartFont.draw(batch, throwsString, ctx, cty);

      ctx += (int) chartFont.getBounds(throwsString).width + 20;
      String speedString = "IP " + card.ipAdj;
      if (card.ipAdj < 1) {
         chartFont.setColor(Color.RED);
      }
      chartFont.draw(batch, speedString, ctx, cty);
      chartFont.setColor(Color.WHITE);

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
            int strLen = (int) chartFont.getBounds(resultString).width;
            chartFont.draw(batch, resultString, ctx - strLen / 2, cty);
            strLen = (int) chartFont.getBounds(CardConstants.CHART_TEXT[i]).width;
            chartFont.draw(batch, CardConstants.CHART_TEXT[i], ctx - strLen / 2, cty - 25);
            startNum = card.chart[i] + 1;
            ctx += 58;
         }
      }

      createCardNumText(batch);
   }

   private void createNameText(SpriteBatch batch) {
      int ntx, nty; // Name Text X and Y Positions
      ntx = 105;
      nty = 186;
      nameFont.setColor(Color.RED);
      nameFont.draw(batch, card.name, ntx - 1, nty);
      nameFont.draw(batch, card.name, ntx - 1, nty - 1);
      nameFont.draw(batch, card.name, ntx + 1, nty);
      nameFont.draw(batch, card.name, ntx - 1, nty + 1);
      nameFont.draw(batch, card.name, ntx, nty + 1);
      nameFont.draw(batch, card.name, ntx + 1, nty - 1);
      nameFont.draw(batch, card.name, ntx, nty - 1);
      nameFont.draw(batch, card.name, ntx + 1, nty + 1);
      nameFont.setColor(Color.WHITE);
      nameFont.draw(batch, card.name, ntx, nty);
   }

   private void createCardNumText(SpriteBatch batch) {
      // Draw the year and the card number
      if (card.cardnum != 0) {
         int ctx = 420;
         int cty = 73;
         String cardnumString = Integer.toString(card.cardnum);
         String yearString = cardnumString.substring(0, 2);
         String numString = cardnumString.substring(2, 5);
         String yearNumString = numString + "          " + "'" + yearString;
         cnFont.draw(batch, yearNumString, ctx, cty);
      }
   }

   private void createTeamLogo(SpriteBatch batch) {
      Texture tex = null;
      if (card.rarity.equals("P")) {
         tex = CardConstants.TEAM_LOGOS_GOLD_TEXTURE;
      } else {
         tex = CardConstants.TEAM_LOGOS_TEXTURE;
      }
      int index = MLBShowdown.getTeamNamesList().indexOf(card.team) + 1;
      int row = (int) Math.ceil((float) index / 6f);
      int col = index - (row - 1) * 6;
      Sprite logo = new Sprite(tex, 200 * (col - 1), 200 * (row - 1), 200, 200);
      logo.setScale(.4f);
      logo.setPosition(340, 100);
      logo.draw(batch);
   }

   @Override
   public Actor hit(float x, float y, boolean touchable) {
      x = Gdx.input.getX();
      y = Gdx.graphics.getHeight() - Gdx.input.getY();
      int w = (int) (getWidth() * getScaleX());
      int h = (int) (getHeight() * getScaleY());
      if (x >= getX() - w / 2 && x <= getX() + w / 2 && y <= getY() + h / 2 && y >= getY() - h / 2) {
         return this;
      }
      return null;
   };

   public boolean hasActions() {
      return getActions().size > 0;
   }

   public void zoom() {
      MoveToAction moveAct = new MoveToAction();
      ScaleToAction scaleAct = new ScaleToAction();
      if (!isZoomed) {
         preZoomX = getX();
         preZoomY = getY();
         preZoomScale = getScaleX(); // Scale should be the same for x and y for
                                     // Cards
         moveAct.setPosition(450, 300);
         scaleAct.setScale(.8f);
         setZIndex(100);
      } else {
         moveAct.setPosition(preZoomX, preZoomY);
         scaleAct.setScale(preZoomScale);
      }
      moveAct.setDuration(MLBShowdown.ZOOM_ANIMATION_SPEED);
      scaleAct.setDuration(MLBShowdown.ZOOM_ANIMATION_SPEED);
      addAction(moveAct);
      addAction(scaleAct);
      isZoomed = !isZoomed;
   }

   @Override
   public void reset() {
      setVisible(false);
      card = null;
      cardTexture = null;
   }
}
