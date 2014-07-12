package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.brejral.mlbshowdown.MLBShowdown;

public class GameScreen implements Screen {
   MLBShowdown sd;
   GameStage stage;
   Game game;

   /**
    * Creates a new GameScreen to be used with a Game
    * 
    * @param showdown
    *           - the global MLBShowdown variable
    * @param gm
    *           - the game that is created in the GameLoading screen
    */
   public GameScreen(MLBShowdown showdown, Game gm) {
      sd = showdown;
      stage = new GameStage(new ScreenViewport(), gm);
      game = gm;
      game.screen = this;
      game.stage = stage;
      game.setTeams();

      stage.addActorsToStage();
      game.startGame();
      stage.disableButtons();
      game.schedulePitchRoll();
   }

   @Override
   public void render(float delta) {
      Gdx.gl.glClearColor(73f / 255f, 145f / 255f, 94f / 255f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
      Table.drawDebug(stage);
   }

   @Override
   public void resize(int width, int height) {
      System.out.println("GameScreen resize");
   }

   @Override
   public void show() {
      Gdx.input.setInputProcessor(stage);
   }

   @Override
   public void hide() {
      System.out.println("GameScreen hide");
   }

   @Override
   public void pause() {
      System.out.println("GameScreen pause");
   }

   @Override
   public void resume() {
      System.out.println("GameScreen resume");
   }

   @Override
   public void dispose() {
      System.out.println("GameScreen dispose");
   }
}
