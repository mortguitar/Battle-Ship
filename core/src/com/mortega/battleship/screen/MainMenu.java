package com.mortega.battleship.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.mortega.battleship.BattleShip;
import com.mortega.battleship.tools.ScrollingBackground;

public class MainMenu implements Screen {

    private static final int EXIT_BUTTON_WIDTH = 340;
    private static final int EXIT_BUTTON_HEIGHT = 132;
    private static final int PLAY_BUTTON_WIDTH = 340;
    private static final int PLAY_BUTTON_HEIGHT = 132;
    private static final int EXIT_BUTTON_Y = 100;
    private static final int PLAY_BUTTON_Y = 360;
    private static final int LOGO_WIDTH = 700;
    private static final int LOGO_HEIGHT = 194;
    private static final int LOGO_Y = 750;
    private static final int SHIP_WIDTH = 400;
    private static final int SHIP_HEIGHT = 400;
    private static final int SHIP_Y = 360;
    private static final int SETTING_WIDTH = 340;
    private static final int SETTING_HEIGHT = 132;
    private static final int SETTING_Y = 230;

    private final BattleShip game;

    private Texture playButtonActive;
    private Texture playButtonInactive;
    private Texture exitButtonActive;
    private Texture exitButtonInactive;
    private Texture settingButtonInactive;
    private Texture settingButtonActive;
    private Texture logo;
    private Texture ship;

    public MainMenu (final BattleShip game) {

        this.game = game;
        playButtonActive = new Texture("play_button_active.png");
        playButtonInactive = new Texture("play_button_inactive.png");
        exitButtonActive = new Texture("exit_button_active.png");
        exitButtonInactive = new Texture("exit_button_inactive.png");
        settingButtonInactive = new Texture("settings_button_inactive.png");
        settingButtonActive = new Texture("settings_button_active.png");

        logo = new Texture("logo.png");
        ship = new Texture("ship_main.png");

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        final MainMenu mainMenu = this;

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Salir de la app
                int x = BattleShip.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
                if (game.camera.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > EXIT_BUTTON_Y) {
                    mainMenu.dispose();
                    Gdx.app.exit();
                }

                //Ajustes
                x = BattleShip.WIDTH / 2 - SETTING_WIDTH / 2;
                if (game.camera.getInputInGameWorld().x < x + SETTING_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < SETTING_Y + SETTING_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > SETTING_Y) {
                    mainMenu.dispose();
                    game.setScreen(new GameSetting(game));
                }

                //Jugar al juego
                x = BattleShip.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
                if (game.camera.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > PLAY_BUTTON_Y) {
                    mainMenu.dispose();
                    game.setScreen(new ElectionShip(game));
                }

                return super.touchUp(screenX, screenY, pointer, button);
            }

        });
    }

    @Override
    public void show () {

    }

    @Override
    public void render (float delta) {

        Gdx.gl.glClearColor(0.001f, 0.001f, 0.001f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta, game.batch);

        int x = BattleShip.WIDTH / 2 - EXIT_BUTTON_WIDTH / 2;
        if (game.camera.getInputInGameWorld().x < x + EXIT_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        } else {
            game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        x = BattleShip.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;
        if (game.camera.getInputInGameWorld().x < x + PLAY_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > PLAY_BUTTON_Y) {
            game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        } else {
            game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        x = BattleShip.WIDTH / 2 - SETTING_WIDTH / 2;
        if (game.camera.getInputInGameWorld().x < x + SETTING_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < SETTING_Y + SETTING_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > SETTING_Y) {
            game.batch.draw(settingButtonActive, x, SETTING_Y, SETTING_WIDTH, SETTING_HEIGHT);
        } else {
            game.batch.draw(settingButtonInactive, x, SETTING_Y, SETTING_WIDTH, SETTING_HEIGHT);
        }

        game.batch.draw(logo, (BattleShip.WIDTH >> 1) - (LOGO_WIDTH >> 1), LOGO_Y, LOGO_WIDTH, LOGO_HEIGHT);
        game.batch.draw(ship, (BattleShip.WIDTH >> 1) - (SHIP_WIDTH >> 1), SHIP_Y, SHIP_WIDTH, SHIP_HEIGHT);

        game.batch.end();
    }

    @Override
    public void resize (int width, int height) {

    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        Gdx.input.setInputProcessor(null);
    }
}
