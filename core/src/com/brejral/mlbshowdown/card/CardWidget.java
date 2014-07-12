package com.brejral.mlbshowdown.card;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class CardWidget extends Widget {
   Card card;

   public CardWidget() {
   }

   public void setCard(Card cd) {
      card = cd;
      if (card != null) {
         setBounds(getX(), getY(), card.cardTexture.getRegionWidth(), card.cardTexture.getRegionHeight());
      }
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      super.draw(batch, parentAlpha);
      if (card != null) {
         batch.draw(card.cardTexture, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
      }
   }

}
