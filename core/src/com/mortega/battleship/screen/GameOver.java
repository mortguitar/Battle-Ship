package com.mortega.battleship.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.mortega.battleship.BattleShip;
import com.mortega.battleship.tools.ScrollingBackground;

public class GameOver implements Screen {

    private static final int BANNER_WIDTH = 360;
    private static final int BANNER_HEIGHT = 115;

    private BattleShip game;

    private int score, highscore;

    private Texture gameOverBanner;

    private BitmapFont scoreFont;
    private BitmapFont menuFont;

    GameOver(BattleShip game, int score) {

        this.game = game;
        this.score = score;

        //Get highscore from save file
        Preferences prefs = Gdx.app.getPreferences("battleship");
        this.highscore = prefs.getInteger("highscore", 0);

        //Check if score beats highscore
        if (score > highscore) {
            prefs.putInteger("highscore", score);
            prefs.flush();
        }

        //Load textures and fonts
        gameOverBanner = new Texture("game_over.png");

        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        scoreFont.getData().setScale(1.2f,1.2f);

        menuFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        menuFont.getData().setScale(1f,1f);

        game.scrollingBackground.setSpeedFixed(true);
        game.scrollingBackground.setSpeed(ScrollingBackground.DEFAULT_SPEED);
    }

    @Override
    public void show () {}

    @Override
    public void render (float delta) {

        Gdx.gl.glClearColor(0.3f, 0.000f, 0.000f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.scrollingBackground.updateAndRender(delta, game.batch);

        game.batch.draw(gameOverBanner, (BattleShip.WIDTH >> 1) - (BANNER_WIDTH >> 1), BattleShip.HEIGHT - BANNER_HEIGHT - 30, BANNER_WIDTH, BANNER_HEIGHT);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "Score: \n" + score, Color.WHITE, 0, Align.center, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore: \n" + highscore, Color.WHITE, 0, Align.center, false);

        scoreFont.draw(game.batch, scoreLayout, 740,800);
        scoreFont.draw(game.batch, highscoreLayout,740, 600);

        float touchX = game.camera.getInputInGameWorld().x, touchY = BattleShip.HEIGHT - game.camera.getInputInGameWorld().y;

        GlyphLayout tryAgainLayout = new GlyphLayout(menuFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(menuFont, "Main Menu");

        float tryAgainX = 440;
        float tryAgainY = 90;
        float mainMenuX = 790;
        float mainMenuY = 90;

        //Checks if hovering over try again button
        if (touchX >= tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY >= tryAgainY - tryAgainLayout.height && touchY < tryAgainY)
            tryAgainLayout.setText(menuFont, "Try Again", Color.YELLOW, 0, Align.left, false);

        //Checks if hovering over main menu button
        if (touchX >= mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY >= mainMenuY - mainMenuLayout.height && touchY < mainMenuY)
            mainMenuLayout.setText(menuFont, "Main Menu", Color.YELLOW, 0, Align.left, false);

        //If try again and main menu is being pressed
        if (Gdx.input.isTouched()) {
            //Try again
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new GameScreen(game));
                return;
            }

            //main menu
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenu(game));
                return;
            }
        }

        //Draw buttons
        menuFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        menuFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub

    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }
}
