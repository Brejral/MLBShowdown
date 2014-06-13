package com.brejral.mlbshowdown;

import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.brejral.mlbshowdown.game.Game;

public class AnimationRunnable extends RunnableAction {
   private Game game;

   public AnimationRunnable(Game gm) {
      game = gm;
   }

   @Override
   public void run() {
      this.getActor().getActions().clear();
      if (game.result2 != null && !game.result2.isEmpty() && game.checkForAdvancement) {
         game.checkEndOfAdvanceAnimation();
      } else {
         game.checkEndOfAnimation();
      }
   }
}
