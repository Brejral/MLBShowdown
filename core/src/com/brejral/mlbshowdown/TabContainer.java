package com.brejral.mlbshowdown;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Create a container for a {@link Tab tab}.
 * 
 * @author Kyu
 *
 */
public class TabContainer extends Table {

   private TabContainerStyle style;

   /**
    * Create a {@link TabContainer}.
    * 
    * @param skin
    *           the skin to use in this container
    */
   public TabContainer(Skin skin) {
      this(skin, "default");
   }

   public TabContainer(Skin skin, String styleName) {
      super(skin);

      setStyle(skin.get(styleName, TabContainerStyle.class));
   }

   /**
    * Apply a {@link TabContainerStyle style}.
    * 
    * @param style
    */
   public void setStyle(TabContainerStyle style) {
      this.style = style;

      setBackground(style.background);
   }

   /**
    * @return the current {@link TabContainerStyle style}
    */
   public TabContainerStyle getStyle() {
      return style;
   }

   /**
    * Define the style of a {@link Tab tab} {@link TabContainer container}.
    * 
    * @author Kyu
    *
    */
   public static class TabContainerStyle {

      /** Optional */
      private Drawable background;

      public TabContainerStyle() {
      }

   }

}