package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
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
   public boolean isZoomed = false;
   public boolean isSelected = false;
   public float preZoomX, preZoomY, preZoomScale;
   public Card card;

   public CardActor() {
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
      setBounds(getX(), getY(), card.cardTexture.getRegionWidth(), card.cardTexture.getRegionHeight());
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
      batch.draw(card.cardTexture, getX() - w / 2, getY() - h / 2, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
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
   }
}
