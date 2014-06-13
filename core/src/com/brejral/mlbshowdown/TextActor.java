package com.brejral.mlbshowdown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextActor extends Actor {
   FreeTypeFontGenerator generator;
   FreeTypeFontParameter param;
   BitmapFont font;
   String text;
   public static final int LEFT = 0, CENTER = 1, RIGHT = 2;
   int align;

   public TextActor(String txt, String fontString, int size) {
      text = txt;
      generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + fontString));
      param = new FreeTypeFontParameter();
      param.size = size;
      font = generator.generateFont(param);
      align = CENTER;
   }

   public TextActor(String fontString, int size) {
      text = "";
      generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + fontString));
      param = new FreeTypeFontParameter();
      param.size = size;
      font = generator.generateFont(param);
      align = CENTER;
   }

   public void setAlignment(int alignment) {
      align = alignment;
   }

   public void setSize(int size) {
      param.size = size;
      font = generator.generateFont(param);
   }

   public void setText(String txt) {
      text = txt;
   }

   public String getText() {
      return text;
   }

   @Override
   public float getWidth() {
      return font.getBounds(text).width;
   }

   @Override
   public void draw(Batch batch, float parentAlpha) {
      font.setColor(getColor());
      TextBounds tb = font.getBounds(text);
      int w = (int) tb.width;
      int h = (int) tb.height;
      float x = 0;
      switch (align) {
      case LEFT:
         x = getX();
         break;
      case CENTER:
         x = getX() - w / 2;
         break;
      case RIGHT:
         x = getX() - w;
         break;
      }
      font.draw(batch, text, x, getY() + h / 2);
   }
}
