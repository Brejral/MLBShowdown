package com.brejral.mlbshowdown.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.brejral.mlbshowdown.MLBShowdown;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		MLBShowdown showdown = new MLBShowdown();
		config.width = showdown.screenSizeX;
		config.height = showdown.screenSizeY;
		new LwjglApplication(showdown, config);
	}
}
