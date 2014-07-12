package com.brejral.mlbshowdown.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.brejral.mlbshowdown.MLBShowdown;

public class GameStatsScreen implements Screen {
   public MLBShowdown sd;
   public Game game;
   public Screen screen;
   public Stage stage;
   public BitmapFont teamText, statText;

   public GameStatsScreen(Game gm, Screen scrn) {
      game = gm;
      sd = game.sd;
      screen = scrn;
      teamText = MLBShowdown.getAeroItalicFont(30);
      statText = MLBShowdown.getAeroItalicFont(20);
      stage = new Stage();
      addActorsToStage();

      Gdx.input.setInputProcessor(stage);
   }

   @Override
   public void render(float delta) {
      Gdx.gl.glClearColor(0, 0, 0, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      stage.act(Gdx.graphics.getDeltaTime());
      stage.draw();
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

   public void addActorsToStage() {
      Image background = new Image(sd.fieldTexture);
      background.setColor(1, 1, 1, 0.5f);
      stage.addActor(background);

      TextButtonStyle style = new TextButtonStyle();
      style.fontColor = Color.WHITE;
      style.overFontColor = Color.YELLOW;
      style.font = statText;
      TextButton backButton = new TextButton("Back", style);
      backButton.setPosition(850, 570);
      backButton.setTouchable(Touchable.enabled);
      backButton.addListener(new ClickListener() {
         @Override
         public void clicked(InputEvent event, float x, float y) {
            System.out.println("Clicked back button");
            game.sd.setScreen(screen);
         }
      });
      stage.addActor(backButton);
   }
}
