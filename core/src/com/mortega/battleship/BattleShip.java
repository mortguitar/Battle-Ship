package com.mortega.battleship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mortega.battleship.tools.GameCamera;
import com.mortega.battleship.screen.MainMenu;
import com.mortega.battleship.tools.ScrollingBackground;

public class BattleShip extends Game {

	public static final int WIDTH = 1480;
	public static final int HEIGHT = 1050;

	public SpriteBatch batch;
	public ScrollingBackground scrollingBackground;
	public GameCamera camera;
	
	@Override
	public void create () {

		batch = new SpriteBatch();
		camera = new GameCamera(WIDTH, HEIGHT);

		this.scrollingBackground = new ScrollingBackground();
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {

		super.render();
		batch.setProjectionMatrix(camera.combined());
	}
	
	public void resize(int width, int height) {

		camera.update(width, height);
		super.resize(width, height);
	}
}
