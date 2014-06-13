package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
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
   FreeTypeFontGenerator generator;
   FreeTypeFontParameter fontParameter;
   BitmapFont aeroDisplayItalicFont36;
   Texture emptyTexture;
   Texture fullTexture;
   NinePatch emptyNp;
   NinePatch fullNp;
   long timeStart;

   public GameLoadingScreen(MLBShowdown showdown) {
      timeStart = System.currentTimeMillis();
      sd = showdown;
      manager = new AssetManager();
      manager.setLoader(Team.class, new TeamLoader(new InternalFileHandleResolver()));
      game = new Game(sd, manager);
      batch = new SpriteBatch();
      generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/aero_matics_display_italic.ttf"));
      fontParameter = new FreeTypeFontParameter();
      fontParameter.size = 24;
      aeroDisplayItalicFont36 = generator.generateFont(fontParameter);
      emptyTexture = new Texture(Gdx.files.internal("images/empty.png"));
      fullTexture = new Texture(Gdx.files.internal("images/full.png"));
      emptyNp = new NinePatch(new TextureRegion(emptyTexture, 24, 24), 8, 8, 8, 8);
      fullNp = new NinePatch(new TextureRegion(fullTexture, 24, 24), 8, 8, 8, 8);
      GameStage.loadActorsWithAssetManager(manager);
   }

   private void doneLoading() {
      sd.setScreen(new GameScreen(sd, game));
      System.out.println("Loading Time " + ((System.currentTimeMillis() - timeStart) / 1000) + " sec");
   }

   @Override
   public void render(float delta) {
      if (manager.update()) {
         doneLoading();
      }

      Gdx.gl.glClearColor(73f / 255f, 145f / 255f, 94f / 255f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      batch.begin();
      batch.draw(sd.fieldTexture, 0, 0);
      float progress = manager.getProgress();
      emptyNp.draw(batch, 90, 285, 720, 30);
      fullNp.draw(batch, 90, 285, 720 * progress, 30);
      aeroDisplayItalicFont36.setColor(Color.BLACK);
      aeroDisplayItalicFont36.drawMultiLine(batch, (int) (progress * 100) + "% Loaded", 450, 310, 0, BitmapFont.HAlignment.CENTER);
      batch.end();
      System.out.println();
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
