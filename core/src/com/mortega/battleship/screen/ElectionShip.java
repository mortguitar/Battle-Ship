package com.mortega.battleship.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mortega.battleship.BattleShip;
import com.mortega.battleship.tools.ScrollingBackground;

import static com.mortega.battleship.screen.GameScreen.SHIP_HEIGHT_PIXEL;
import static com.mortega.battleship.screen.GameScreen.SHIP_WIDTH_PIXEL;

public class ElectionShip implements Screen {

    private static final int NAVE_ONE_BUTTON_WIDTH = 440;
    private static final int NAVE_ONE_BUTTON_HEIGHT = 171;

    private static final int NAVE_TWO_BUTTON_WIDTH = 440;
    private static final int NAVE_TWO_BUTTON_HEIGHT = 171;

    private static final int NAVE_ULTIMATE_BUTTON_WIDTH = 440;
    private static final int NAVE_ULTIMATE_BUTTON_HEIGHT = 171;

    private static final int NAVE_ONE_BUTTON_Y = 700;
    private static final int NAVE_TWO_BUTTON_Y = 400;
    private static final int NAVE_ULTIMATE_BUTTON_Y = 100;

    public enum Tipo {
        NORMAL, ULTIMATE
    }

    public static Tipo tipo;

    private final BattleShip game;

    private Texture nextButtonActive;
    private Texture nextButtonInactive;
    private Texture backButtonActive;
    private Texture backButtonInactive;
    private Texture selectButtonActive;
    private Texture selectButtonInactive;

    private Texture ship;
    private Texture shipDark;
    private Texture shipUltimate;
    private Texture mostrarNave;

    static TextureRegion[][] rollSpriteSheet;

    public ElectionShip(final BattleShip game) {

        this.game = game;
        nextButtonActive = new Texture("next_button_active.png");
        nextButtonInactive = new Texture("next_button_inactive.png");
        backButtonActive = new Texture("back_button_active.png");
        backButtonInactive = new Texture("back_button_inactive.png");
        selectButtonActive = new Texture("select_button_active.png");
        selectButtonInactive = new Texture("select_button_inactive.png");

        ship = new Texture("ship.png");
        shipDark = new Texture("ship_dark.png");
        shipUltimate = new Texture("ship_ultimate.png");

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);

        final ElectionShip electionShip = this;

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //Exit button
                int x = BattleShip.WIDTH / 2 - NAVE_ONE_BUTTON_WIDTH / 2;
                if (game.camera.getInputInGameWorld().x < x + NAVE_ONE_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < NAVE_ONE_BUTTON_Y + NAVE_ONE_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > NAVE_ONE_BUTTON_Y) {
                    mostrarNave = new Texture(String.valueOf(ship));
                    rollSpriteSheet = TextureRegion.split(
                            new Texture(String.valueOf(mostrarNave)), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
                    game.setScreen(new GameScreen(game));
                }

                x = BattleShip.WIDTH / 2 - NAVE_TWO_BUTTON_WIDTH / 2;
                if (game.camera.getInputInGameWorld().x < x + NAVE_TWO_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < NAVE_TWO_BUTTON_Y + NAVE_TWO_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > NAVE_TWO_BUTTON_Y) {
                    mostrarNave = new Texture(String.valueOf(shipDark));
                    rollSpriteSheet = TextureRegion.split(
                            new Texture(String.valueOf(mostrarNave)), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
                    game.setScreen(new GameScreen(game));
                }

                x = BattleShip.WIDTH / 2 - NAVE_ULTIMATE_BUTTON_WIDTH / 2;
                if (game.camera.getInputInGameWorld().x < x + NAVE_ULTIMATE_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < NAVE_ULTIMATE_BUTTON_Y + NAVE_ULTIMATE_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > NAVE_ULTIMATE_BUTTON_Y) {
                    mostrarNave = new Texture(String.valueOf(shipUltimate));
                    tipo = Tipo.ULTIMATE;
                    rollSpriteSheet = TextureRegion.split(
                            new Texture(String.valueOf(mostrarNave)), SHIP_WIDTH_PIXEL, SHIP_HEIGHT_PIXEL);
                    game.setScreen(new GameScreen(game));

                }

                return super.touchUp(screenX, screenY, pointer, button);
            }
        });
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        game.batch.begin();
        Gdx.gl.glClearColor(0.001f, 0.001f, 0.001f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        game.scrollingBackground.updateAndRender(delta, game.batch);

        int x = BattleShip.WIDTH / 2 - NAVE_ONE_BUTTON_WIDTH / 2;
        if (game.camera.getInputInGameWorld().x < x + NAVE_ONE_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < NAVE_ONE_BUTTON_Y + NAVE_ONE_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > NAVE_ONE_BUTTON_Y) {
            game.batch.draw(nextButtonActive, x, NAVE_ONE_BUTTON_Y, NAVE_ONE_BUTTON_WIDTH, NAVE_ONE_BUTTON_HEIGHT);
        } else {
            game.batch.draw(nextButtonInactive, x, NAVE_ONE_BUTTON_Y, NAVE_ONE_BUTTON_WIDTH, NAVE_ONE_BUTTON_HEIGHT);
        }

        x = BattleShip.WIDTH / 2 - NAVE_TWO_BUTTON_WIDTH / 2;
        if (game.camera.getInputInGameWorld().x < x + NAVE_TWO_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < NAVE_TWO_BUTTON_Y + NAVE_TWO_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > NAVE_TWO_BUTTON_Y) {
            game.batch.draw(backButtonActive, x, NAVE_TWO_BUTTON_Y, NAVE_TWO_BUTTON_WIDTH, NAVE_TWO_BUTTON_HEIGHT);
        } else {
            game.batch.draw(backButtonInactive, x, NAVE_TWO_BUTTON_Y, NAVE_TWO_BUTTON_WIDTH, NAVE_TWO_BUTTON_HEIGHT);
        }

        x = BattleShip.WIDTH / 2 - NAVE_ULTIMATE_BUTTON_WIDTH / 2;
        if (game.camera.getInputInGameWorld().x < x + NAVE_ULTIMATE_BUTTON_WIDTH && game.camera.getInputInGameWorld().x > x && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y < NAVE_ULTIMATE_BUTTON_Y + NAVE_ULTIMATE_BUTTON_HEIGHT && BattleShip.HEIGHT - game.camera.getInputInGameWorld().y > NAVE_ULTIMATE_BUTTON_Y) {
            game.batch.draw(selectButtonActive, x, NAVE_ULTIMATE_BUTTON_Y, NAVE_ULTIMATE_BUTTON_WIDTH, NAVE_ULTIMATE_BUTTON_HEIGHT);
        } else {
            game.batch.draw(selectButtonInactive, x, NAVE_ULTIMATE_BUTTON_Y, NAVE_ULTIMATE_BUTTON_WIDTH, NAVE_ULTIMATE_BUTTON_HEIGHT);
        }

        game.batch.end();
    }

        @Override
        public void resize ( int width, int height){

        }

        @Override
        public void pause () {

        }

        @Override
        public void resume () {

        }

        @Override
        public void hide () {

        }

        @Override
        public void dispose () {
            Gdx.input.setInputProcessor(null);
        }
    }
