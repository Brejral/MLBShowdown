package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.brejral.mlbshowdown.MLBShowdown;
import com.brejral.mlbshowdown.team.Team;
import com.brejral.mlbshowdown.team.TeamLoader;

public class GameLoadingScreen implements Screen {
   final MLBShowdown sd;
   AssetManager manager;
   Game game;
   SpriteBatch batch;
   Rectangle borderInner, borderOuter, progressBar;
   BitmapFont font;
   long timeStart;
   boolean renderedOnce = false;

   public GameLoadingScreen(MLBShowdown showdown) {
      timeStart = System.currentTimeMillis();
      sd = showdown;
      manager = new AssetManager();
      manager.setLoader(Team.class, new TeamLoader(new InternalFileHandleResolver()));
      game = new Game(sd, manager);
      batch = new SpriteBatch();
      font = MLBShowdown.getAeroItalicFont(70);
   }

   private void doneLoading() {
      sd.setScreen(new GameScreen(sd, game));
      System.out.println("Loading Time " + ((System.currentTimeMillis() - timeStart) / 1000) + " sec");
   }

   @Override
   public void render(float delta) {
      Gdx.gl.glClearColor(73f / 255f, 145f / 255f, 94f / 255f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      batch.begin();
      batch.draw(sd.fieldTexture, 0, 0);
      font.setColor(Color.WHITE);
      font.drawMultiLine(batch, "Loading...", 450, 595, 0, BitmapFont.HAlignment.CENTER);
      batch.end();
      
      if (renderedOnce && manager.update()) {
         doneLoading();
      }
      renderedOnce = true;
   }

   @Override
   public void resize(int width, int height) {

   }

   @Override
   public void show() {

   }

   @Override
   public void hide() {

   }

   @Override
   public void pause() {

   }

   @Override
   public void resume() {

   }

   @Override
   public void dispose() {

   }

}
