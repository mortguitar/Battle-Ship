package com.mortega.battleship.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mortega.battleship.BattleShip;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Battle Ship";
		cfg.resizable = false;
		cfg.width = 1;	cfg.height = 1;
		cfg.fullscreen = true;
		new LwjglApplication(new BattleShip(), cfg);
	}
}
