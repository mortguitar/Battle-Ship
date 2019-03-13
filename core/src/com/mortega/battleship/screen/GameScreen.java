package com.mortega.battleship.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.mortega.battleship.BattleShip;
import com.mortega.battleship.character.Asteroid;
import com.mortega.battleship.character.Explosion;
import com.mortega.battleship.character.Fire;
import com.mortega.battleship.tools.Colisiones;

import java.util.ArrayList;
import java.util.Random;

import static com.mortega.battleship.character.Asteroid.Tipo.*;
import static com.mortega.battleship.screen.ElectionShip.Tipo.ULTIMATE;
import static com.mortega.battleship.screen.ElectionShip.rollSpriteSheet;

public class GameScreen implements Screen {

    public static final float SPEED = 300;

    public static final float SHIP_ANIMATION_SPEED = 0.7f;
    public static final int SHIP_WIDTH_PIXEL = 17;
    public static final int SHIP_HEIGHT_PIXEL = 32;
    public static final int SHIP_WIDTH = SHIP_WIDTH_PIXEL * 3;
    public static final int SHIP_HEIGHT = SHIP_HEIGHT_PIXEL * 3;

    public static final float ROLL_TIMER_SWITCH_TIME = 0.25f;
    public static float SHOOT_WAIT_TIME = 0.3f;

    public static final float MIN_ASTEROID_SPAWN_TIME = 0.3f;
    public static final float MAX_ASTEROID_SPAWN_TIME = 0.7f;

    public static final int ASTEROID_NORMAL_FASE_ONE = 1000;
    public static final int ASTEROID_METAL = 10000;
    public static final int ASTEROID_FIRE = 25000;
    public static final int ASTEROID_NORMAL_FASE_TWO = 40000;
    public static final int ASTEROID_ICE = 60000;
    public static final int ASTEROID_TO_BIG_CHUNGUS = 80000;

    private Animation[] rolls;

    private float x;
    private float y;
    private int roll;
    private float rollTimer;
    private float stateTime;
    private float shootTimer;
    private float asteroidSpawnTimer;

    private Random random;

    private BattleShip game;

    private ArrayList<Fire> fires;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Explosion> explosions;

    private ArrayList<Asteroid> asteroidsToRemove;
    private ArrayList<Fire> fireRemove;


    private Texture blank;

    private BitmapFont scoreFont;
    private BitmapFont bigChungusFont;

    private Colisiones playerRect;

    private GlyphLayout drawRestante;

    private int health = 7;//0 = mueres, 7 = vida completa
    public static int score;
    public static int scoreRestante;

    private boolean showControls = true;

    private Sound asteroidSound;
    private Music musicaFondo;

    GameScreen(BattleShip game) {

        this.game = game;

        y = 15;
        x = (BattleShip.WIDTH >> 1) - (SHIP_WIDTH >> 1);
        fires = new ArrayList<Fire>();
        asteroids = new ArrayList<Asteroid>();
        explosions = new ArrayList<Explosion>();
        scoreFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));
        bigChungusFont = new BitmapFont(Gdx.files.internal("fonts/score.fnt"));

        playerRect = new Colisiones(0, 0, SHIP_WIDTH, SHIP_HEIGHT);

        blank = new Texture("blank.png");

        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("music/Space Jam Theme Song.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.play();

        score = 0;

        random = new Random();
        asteroidSpawnTimer = random.nextFloat() *
                (MAX_ASTEROID_SPAWN_TIME - MIN_ASTEROID_SPAWN_TIME) + MIN_ASTEROID_SPAWN_TIME;

        shootTimer = 0;

        roll = 2;
        rollTimer = 0;
        rolls = new Animation[5];

        rolls[0] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[2]);//TODO Izquierda
        rolls[1] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[1]);
        rolls[2] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[0]);//Centro
        rolls[3] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[3]);
        rolls[4] = new Animation(SHIP_ANIMATION_SPEED, rollSpriteSheet[4]);//TODO Derecha

        game.scrollingBackground.setSpeedFixed(false);
    }

    @Override
    public void show () {

    }

    @Override
    public void render (float delta) {

        game.batch.begin();

        //Llamar al código de disparo del personaje
        disparoPersonaje(delta);

        //Spawn de asteroides
        spawnAsteroides(delta);

        //Refrescar la lista de asteroides
        refrescarAsteroides(delta);

        //Refrescar municion de la nave
        refrescarMunicion(delta);

        //Refrescar las explosiones
        refrescarExplosion(delta);

        //Movimiento del personaje
        moveShip();

        //Revision de colisones
        generalColisiones(delta);

        //Metodos que dibujan  los elementos de informacion que apareceran en la pantalla
        dibujoVida();

        game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);

        dibujoControles();
        game.batch.end();
    }

    private void generalColisiones(float delta) {

        //Antes de que el jugador se mueva, comprueba el sistema de colisiones
        playerRect.move(x, y);

        //Antes de actualizar, comprueba las balas
        for (Fire bullet : fires) {

            //Aqui se anima las colisiones y hacen que ocurran
            for (Asteroid asteroid : asteroids) {

                if (bullet.getCollisionRect().collidesWith(asteroid.getColisiones())) {

                    asteroidSound = Gdx.audio.newSound(Gdx.files.internal("music/sound effects explosion.mp3"));
                    asteroidSound.play();

                    Asteroid.vida -= 1;

                    if (Asteroid.vida < 0) {
                        fireRemove.add(bullet);
                        asteroidsToRemove.add(asteroid);
                        explosions.add(new Explosion(asteroid.getPosicionX(), asteroid.getPosicionY()));
                        if (Asteroid.tipo == NORMAL)
                            if (Asteroid.contador == 2) {
                                score += 200;
                                scoreRestante = ASTEROID_NORMAL_FASE_TWO - score;

                            } else if (Asteroid.contador == 3) {
                                score += 600;
                                scoreRestante = ASTEROID_TO_BIG_CHUNGUS - score;
                            } else {
                                score += 50;
                                scoreRestante = ASTEROID_NORMAL_FASE_ONE - score;
                            }
                        else if (Asteroid.tipo == METAL) {
                            score += 250;
                            scoreRestante = ASTEROID_METAL - score;
                        } else if (Asteroid.tipo == FUEGO) {
                            score += 500;
                            scoreRestante = ASTEROID_FIRE - score;
                        } else if (Asteroid.tipo == HIELO) {
                            asteroidSound = Gdx.audio.newSound(Gdx.files.internal("music/Ice spell.mp3"));
                            asteroidSound.play();
                            score += 700;
                            scoreRestante = ASTEROID_ICE - score;
                        } else if (Asteroid.tipo == BIG_CHUNGUS)
                            score += 666;
                    }
                }
            }
        }

        fires.removeAll(fireRemove);

        for (Asteroid asteroid : asteroids) {
            if (asteroid.getColisiones().collidesWith(playerRect)) {
                asteroidsToRemove.add(asteroid);

                if (Asteroid.tipo == NORMAL)
                    health -= 1;
                else if (Asteroid.tipo == METAL)
                    health -= 2;
                else if (Asteroid.tipo == FUEGO)
                    health -= 3;
                else if (Asteroid.tipo == HIELO)
                    health -= 1;
                else if (Asteroid.tipo == BIG_CHUNGUS)
                    health -= 20;

                //Si la el jugador ya no tiene vida, Se inicia la ventana de GAME OVER
                if (health <= 0) {
                    this.dispose();
                    game.setScreen(new GameOver(game, score));
                    return;
                }
            }
        }
        asteroids.removeAll(asteroidsToRemove);

        stateTime += delta;

        Gdx.gl.glClearColor(0.001f, 0.001f, 0.001f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.scrollingBackground.updateAndRender(delta, game.batch);

        scoreFont.getData().setScale(1,1);
        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, "" + score);
        scoreFont.draw(game.batch, scoreLayout, (BattleShip.WIDTH >> 1) - scoreLayout.width / 2, BattleShip.HEIGHT - scoreLayout.height - 10);

        for (Fire fire : fires) {
            fire.render(game.batch);
        }

        for (Asteroid asteroid : asteroids) {
            asteroid.render(game.batch);
        }

        for (Explosion explosion : explosions) {
            explosion.render(game.batch);
        }
    }

    private void moveShip() {

        if (isLeft()) {//Izquierda
            x -= SPEED * Gdx.graphics.getDeltaTime();

            if (x < 370)
                x = 370;

            //Si el boton sigue presionandose a la izquierda
            if (isJustLeft() && !isRight() && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            //Actualiza el rotar de la nave
            rollTimer -= Gdx.graphics.getDeltaTime();
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll--;
            }
        } else {
            if (roll < 2) {
                //Cuando no se pulsa hace que retorne a su posicion original
                rollTimer += Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll++;
                }
            }
        }

        if (isRight()) {//Derecha
            x += SPEED * Gdx.graphics.getDeltaTime();

            if (x + SHIP_WIDTH > 1100)
                x = 1100 - SHIP_WIDTH;

            //Si se sigue presionando la tecla
            if (isJustRight() && !isLeft() && roll > 0) {
                rollTimer = 0;
                roll--;
            }

            //Se actualiza el rotar de la nave
            rollTimer += Gdx.graphics.getDeltaTime();
            if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll < 4) {
                rollTimer -= ROLL_TIMER_SWITCH_TIME;
                roll++;
            }
        } else {
            if (roll > 2) {
                //Retorna a la posicion original
                rollTimer -= Gdx.graphics.getDeltaTime();
                if (Math.abs(rollTimer) > ROLL_TIMER_SWITCH_TIME && roll > 0) {
                    rollTimer -= ROLL_TIMER_SWITCH_TIME;
                    roll--;
                }
            }
        }
    }

    private void dibujoVida() {

        //Draw health
        scoreFont.getData().setScale(0.6f,0.6f);

        GlyphLayout infoHealth = new GlyphLayout(scoreFont, "Vidas");
        scoreFont.draw(game.batch, infoHealth, 50,1000);

        GlyphLayout healthLayout = new GlyphLayout(scoreFont, "" + health);
        scoreFont.draw(game.batch, healthLayout, 50, 960);

        GlyphLayout infoScoreRestante = new GlyphLayout(scoreFont, "Siguiente nivel");
        scoreFont.draw(game.batch, infoScoreRestante, 1200, 1000);

        if (Asteroid.tipo == HIELO) {

            musicaFondo.stop();
            musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("music/Chungus Anthem.mp3"));
            musicaFondo.setLooping(true);
            musicaFondo.play();

            bigChungusFont.getData().setScale(0.8f,4.9f);
            bigChungusFont.setColor(Color.YELLOW);

            GlyphLayout bigchungus = new GlyphLayout(bigChungusFont, "BIG CHUNGUS\nIS\nNEAR");
            bigChungusFont.draw(game.batch, bigchungus, 50,870);
            bigChungusFont.draw(game.batch, bigchungus, 1200, 870);

            drawRestante = new GlyphLayout(scoreFont, "" + scoreRestante);
            scoreFont.draw(game.batch, drawRestante, 1200, 960);
        }
        else if (Asteroid.tipo == BIG_CHUNGUS) {

            bigChungusFont.getData().setScale(0.8f,4.9f);
            bigChungusFont.setColor(Color.RED);
            drawRestante.reset();

            GlyphLayout bigchungus = new GlyphLayout(bigChungusFont, "¡ATENCION! \nBIG CHUNGUS\nIS COMING");
            drawRestante.setText(scoreFont,""+score);
            bigChungusFont.draw(game.batch, bigchungus, 50,870);
            bigChungusFont.draw(game.batch, bigchungus, 1200, 870);
            scoreFont.draw(game.batch, drawRestante, 1200, 960);
        }
        else {
            drawRestante = new GlyphLayout(scoreFont, "" + scoreRestante);
            scoreFont.draw(game.batch, drawRestante, 1200, 960);
        }


        game.batch.draw((TextureRegion) rolls[roll].getKeyFrame(stateTime, true), x, y, SHIP_WIDTH, SHIP_HEIGHT);
    }

    private void dibujoControles() {

        if (showControls) {

                scoreFont.getData().setScale(0.5f, 0.6f);
                GlyphLayout instructionsLayout = new GlyphLayout (
                        scoreFont,
                        "Presiona A y D para desplazarte por la pantalla",
                        Color.WHITE, BattleShip.WIDTH - 15, Align.center, true
                );

                scoreFont.draw(
                        game.batch,
                        instructionsLayout,
                        5, 150
                );
        }
    }

    private void refrescarExplosion(float delta) {

        //Actualiza las explosiones
        ArrayList<Explosion> explosionsToRemove = new ArrayList<Explosion>();
        for (Explosion explosion : explosions) {
            explosion.update(delta);
            if (explosion.remove)
                explosionsToRemove.add(explosion);
        }
        explosions.removeAll(explosionsToRemove);
    }

    private void refrescarMunicion(float delta) {

        fireRemove = new ArrayList<Fire>();
        for (Fire fire : fires) {
            fire.update(delta);
            if (fire.remove)
                fireRemove.add(fire);
        }
    }

    private void spawnAsteroides(float delta) {

        asteroidSpawnTimer -= delta;
        if (asteroidSpawnTimer <= 0) {
            if (Asteroid.tipo == FUEGO) {
                asteroidSpawnTimer = random.nextFloat() * ((MAX_ASTEROID_SPAWN_TIME + 0.1f) - (MIN_ASTEROID_SPAWN_TIME + 0.1f)) + MIN_ASTEROID_SPAWN_TIME;
                asteroids.add(new Asteroid(random.nextInt(740 - Asteroid.WIDTH)));
            }
            else if (Asteroid.tipo == BIG_CHUNGUS) {
                asteroidSpawnTimer = random.nextFloat() * ((20f) - (17f)) + 17f;
                asteroids.add(new Asteroid(random.nextInt(400 - Asteroid.WIDTH)));
            }
            else if (Asteroid.tipo == HIELO) {
                asteroidSpawnTimer = random.nextFloat() * ((9f) - (3f)) + 3f;
                asteroids.add(new Asteroid(random.nextInt(740 - Asteroid.WIDTH)));
            }
            else {
                asteroidSpawnTimer = random.nextFloat() * ((MAX_ASTEROID_SPAWN_TIME) - (MIN_ASTEROID_SPAWN_TIME)) + MIN_ASTEROID_SPAWN_TIME;
                asteroids.add(new Asteroid(random.nextInt(740 - Asteroid.WIDTH)));
            }


        }
    }

    private void refrescarAsteroides(float delta) {

        asteroidsToRemove = new ArrayList<Asteroid>();
        for (Asteroid asteroid : asteroids) {
            asteroid.update(delta);
            if (asteroid.remove)
                asteroidsToRemove.add(asteroid);
        }
    }

    private void disparoPersonaje(float delta) {

        if (ElectionShip.tipo == ULTIMATE)
            SHOOT_WAIT_TIME = 0.04f;

        shootTimer += delta;
        if ((isRight() || isLeft()) && shootTimer >= SHOOT_WAIT_TIME) {

            shootTimer = 0;

            //No se veran los controles cuando el usuario haya disparado
            showControls = false;

            int offset = 4;
            if (roll == 1 || roll == 3)
                offset = 8;

            if (roll == 0 || roll == 4)
                offset = 16;


            fires.add(new Fire(x + offset));
            fires.add(new Fire(x + SHIP_WIDTH - offset));
        }
    }

    private boolean isRight () {
        return Gdx.input.isKeyPressed(Input.Keys.D) || (Gdx.input.isTouched() && game.camera.getInputInGameWorld().x >= BattleShip.WIDTH / 4);
    }

    private boolean isLeft () {
        return Gdx.input.isKeyPressed(Input.Keys.A) || (Gdx.input.isTouched() && game.camera.getInputInGameWorld().x < BattleShip.WIDTH / 4);
    }

    private boolean isJustRight () {
        return Gdx.input.isKeyJustPressed(Input.Keys.D) || (Gdx.input.justTouched() && game.camera.getInputInGameWorld().x >= BattleShip.WIDTH / 4);
    }

    private boolean isJustLeft () {
        return Gdx.input.isKeyJustPressed(Input.Keys.A) || (Gdx.input.justTouched() && game.camera.getInputInGameWorld().x < BattleShip.WIDTH / 4);
    }

    @Override
    public void resize (int width, int height) {

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

    }

}
