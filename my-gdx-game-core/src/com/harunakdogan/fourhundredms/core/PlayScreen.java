package com.harunakdogan.fourhundredms.core;

import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class PlayScreen implements Screen {

	public Timer timerGame, timerKademe, timer400;
	public Random rnd;
	public FourHundredMilliseconds game_;
	public Animator animator;
	public SpriteBatch sb;
	public BitmapFont font, font2;
	public CharSequence scoreChar, sayacChar, stepChar, timerChar;
	public Sprite comboSprite;
	public Combo combo;
	public DiscGenerator discGenerator;
	public Preferences prefs;
	public TweenManager tweenManager;
	public Sound breakSound;
	public Texture background, background2, targetRect, targetRect2, leftPad,
			menzil, menzil2, bottomPad, pause;

	public float speed, mnzY, backSpeed;
	public boolean valid = false, inTouch = false, gameOver = false,
			isRunning = true, correctCatch = false;
	public int APP_HEIGHT, APP_WIDTH, targetX, targetY, leftX, leftY, mnzX,
			mnzMin, mnzMax, mnzSpeed, mnzHeight, leftHeight, sayac,
			kademe, stepTime, score, highScore, gameTime;

	public PlayScreen(FourHundredMilliseconds game) {
		game_ = game;

		// Skor tutuldu
		prefs = Gdx.app.getPreferences("My Preferences");
		highScore = prefs.getInteger("score", 0);

		// Disk parçalanma sesi
		breakSound = Gdx.audio.newSound(Gdx.files.internal("sounds/break.mp3"));
		breakSound.setVolume(1, 1);

		// Combo'larý göster
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new TweenAccessor());

		APP_WIDTH = Gdx.app.getGraphics().getWidth();
		APP_HEIGHT = Gdx.app.getGraphics().getHeight();

		speed = 0.4f;
		sayac = 0;
		kademe = (APP_HEIGHT / 6);
		mnzHeight = APP_HEIGHT / 4;
		leftHeight = APP_HEIGHT / 6;
		mnzSpeed = APP_HEIGHT / 600;
		gameTime = 120;

		font = new BitmapFont(Gdx.files.internal("fonts/purple.fnt"), false);
		font.getData().setScale(3, 3);

		font2 = new BitmapFont(Gdx.files.internal("fonts/purple.fnt"), false);
		font2.getData().setScale(1, 1);

		sb = new SpriteBatch();
		rnd = new Random();
		discGenerator = new DiscGenerator();
		combo = new Combo();
		animator = new Animator(new Texture("textures/birdanim.png"), 3, 0.2f);

		background = new Texture("textures/background.png");
		background2 = new Texture("textures/background.png");
		targetRect = new Texture("textures/bottomL.png");
		targetRect2 = new Texture("textures/bottomL2.png");
		leftPad = new Texture("textures/leftL.png");
		menzil = new Texture("textures/menzil.png");
		menzil2 = new Texture("textures/menzil_2.png");
		bottomPad = new Texture("textures/starship.png");
		pause = new Texture("textures/pause.png");

		targetX = APP_WIDTH + 300;
		targetY = 0;
		leftX = 0;
		leftY = 300;
		mnzX = (APP_WIDTH / 4);
		mnzY = 110;
		mnzMin = APP_HEIGHT / 6;
		mnzMax = (APP_HEIGHT - APP_HEIGHT / 6);

		timerGame = new Timer();
		timerKademe = new Timer();
		timer400 = new Timer();

		// Oyun süresini düþür
		timerGame.scheduleTask(new Task() {

			@Override
			public void run() {

				gameTime--;

				if (gameTime <= 0) {
					gameOver = true;
					timerGame.stop();
					game_.setScreen(game_.menuScreen);
				}
			}
		}, 0, 1);

		// 16 saniye boyunce sýnýrý aþaðýda tut
		timerKademe.scheduleTask(new Task() {

			@Override
			public void run() {

				stepTime--;

				if (stepTime == 0) {
					if (mnzMax <= (APP_HEIGHT - 2 * kademe)) {
						mnzMax += kademe;
						timerKademe.stop();
					}
				}
			}
		}, 0, 1);

		// 400 milisaniyede bir disk üret
		timer400.scheduleTask(new Task() {

			@Override
			public void run() {

				discGenerator.generate(
						(int) (rnd
								.nextInt((int) (APP_WIDTH * 0.75f - APP_WIDTH / 5))
								+ APP_WIDTH * 0.25f + APP_WIDTH / 20),
						(int) APP_HEIGHT);
			}
		}, 0, speed);

		timerGame.stop();
		timerKademe.stop();
		timer400.stop();

		comboSprite = new Sprite(combo.c16);
		comboSprite.setSize(APP_WIDTH * 0.6f, APP_HEIGHT / 14);
		comboSprite.setPosition(APP_WIDTH * 0.32f, APP_HEIGHT / 2);

		Tween.set(comboSprite, TweenAccessor.ALPHA).target(0)
				.start(tweenManager);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		scoreChar = "" + score;
		sayacChar = "" + sayac;
		stepChar = "" + stepTime;
		timerChar = "" + gameTime;

		// Sýnýrlara gelen menzili geri yansýt
		if (mnzY <= mnzMin) {
			mnzSpeed = -mnzSpeed;
			mnzY = mnzMin + 1;
		} else if (mnzY + mnzHeight >= mnzMax) {
			mnzSpeed = -mnzSpeed;
			mnzY = mnzMax - mnzHeight - 1;
		}

	
		sb.begin();

		sb.draw(background, MenuScreen.back1x, MenuScreen.back1y, APP_WIDTH,
				APP_HEIGHT);
		sb.draw(background2, MenuScreen.back2x, MenuScreen.back2y, APP_WIDTH,
				APP_HEIGHT);
		sb.draw(pause, APP_WIDTH / 40, APP_HEIGHT - APP_HEIGHT / 10,
				APP_WIDTH / 8, APP_HEIGHT / 12);

		// Sol tutacak menzile deyiyorsa menzil aktif
		if (leftY + leftHeight / 4 >= mnzY
				&& leftY + leftHeight * 0.75f <= mnzY + mnzHeight) {
			sb.draw(menzil2, mnzX, mnzY, APP_WIDTH * (0.75f), mnzHeight);
			valid = true;
		} else {
			sb.draw(menzil, mnzX, mnzY, APP_WIDTH * (0.75f), mnzHeight);
			valid = false;
		}

		// Animasyon sürüyorsa ekrana çiz
		if (animator.animate) {
			sb.draw(animator.getTexture(), animator.x, animator.y,
					APP_WIDTH / 8, APP_HEIGHT / 12);
		}

		// Duruma göre hedef çerçeveyi deðiþtir
		if (inTouch) {
			if (correctCatch)
				sb.draw(targetRect, targetX, mnzY, APP_WIDTH / 4, mnzHeight);
			else
				sb.draw(targetRect2, targetX, mnzY, APP_WIDTH / 4, mnzHeight);
		}

		sb.draw(leftPad, leftX, leftY, (APP_WIDTH / 4), leftHeight);

		// Kaçýrýlan diski sil
		for (Disc disc : discGenerator.getDiscs()) {
			if (disc.y < 0) {
				discGenerator.getDiscs().remove(disc);
				if (sayac > 0)
					sayac = 0;

				break;
			}
		}

		for (Disc disc : discGenerator.getDiscs()) {
			disc.y -= (APP_HEIGHT / 120) * 80 * delta;
			sb.draw(disc, disc.x, disc.y, APP_WIDTH / 8, APP_HEIGHT / 12);
		}
		
		mnzY += mnzSpeed * 100 * delta;
		
		backSpeed = (APP_HEIGHT / 300) * 100 * delta;
		MenuScreen.back1y -= backSpeed;
		MenuScreen.back2y -= backSpeed;
		
		sb.draw(bottomPad, APP_WIDTH / 4, 0, APP_WIDTH * 0.75f, APP_HEIGHT / 6);
		font.draw(sb, scoreChar, APP_WIDTH / 2, mnzMax + font.getCapHeight()
				+ (APP_HEIGHT / 60) * 2);
		font2.draw(sb, sayacChar, APP_WIDTH / 4,
				mnzY + mnzHeight + font2.getCapHeight() * 2);
		font2.draw(sb, timerChar, APP_WIDTH - 2 * font2.getLineHeight(),
				APP_HEIGHT - font2.getCapHeight());

		// 16 saniyelik sayaç aktifse çiz
		if (stepTime > 0) {
			font2.draw(sb, stepChar, APP_WIDTH - font2.getLineHeight(), mnzY
					+ mnzHeight + font2.getCapHeight() * 2);
		}

		animator.update(delta);
		tweenManager.update(delta);
		comboSprite.draw(sb);

		sb.end();

		// Arkaplaný güncelle
		if (MenuScreen.back1y < -APP_HEIGHT)
			MenuScreen.back1y = MenuScreen.back2y + APP_HEIGHT;
		if (MenuScreen.back2y < -APP_HEIGHT)
			MenuScreen.back2y = MenuScreen.back1y + APP_HEIGHT;

	}

	@Override
	public void show() {

		Gdx.input.setInputProcessor(new InputHandler() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer,
					int button) {

				inTouch = false;

				return true;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {

				// Sol tutacaðý konumlandýr
				if (screenX < APP_WIDTH / 4
						&& screenY >= APP_HEIGHT - (mnzMax - leftHeight / 2)
						&& screenY <= APP_HEIGHT - (mnzMin + leftHeight / 2)) {
					leftY = APP_HEIGHT - screenY - leftHeight / 2;
				}

				return true;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer,
					int button) {

				// Alt alana dokunulmuþsa
				if (screenY > APP_HEIGHT * 5 / 6 && screenX >= APP_WIDTH / 4) {
					targetX = screenX - (APP_WIDTH / 4) / 2;
					inTouch = true;

					// Seçilen disk kýrýlýr
					for (Disc disc : discGenerator.getDiscs()) {
						if (disc.x > targetX
								&& disc.x + APP_WIDTH / 8 < targetX
										+ (APP_WIDTH / 4) && disc.y > mnzY
								&& disc.y + APP_WIDTH / 8 < mnzY + mnzHeight
								&& valid) {
							animator.setAnimation(disc.x, disc.y);
							discGenerator.getDiscs().remove(disc);
							score++;
							sayac++;
							correctCatch = true;
							breakSound.play();
							break;
						}
						correctCatch = false;
					}

					// Iskalanmýþsa eksi puan
					if (!correctCatch) {
						score--;
					}

					// Oyun duraklatýlýr
				} else if (screenX > APP_WIDTH / 40
						&& screenX < APP_WIDTH / 40 + APP_WIDTH / 8
						&& screenY > APP_HEIGHT / 60
						&& screenY < APP_HEIGHT / 10) {
					isRunning = false;
					timerGame.stop();
					timer400.stop();
					discGenerator.discs.removeAll(discGenerator.discs);
					game_.setScreen(game_.menuScreen);
				}

				// Combo sayacýna göre ödüller verilir
				switch (sayac) {
				case 256:
					comboSprite.setTexture(combo.c256);
					score += 50;
					gameTime += 25;
					break;
				case 128:
					comboSprite.setTexture(combo.c128);
					gameTime += 25;
					break;
				case 64:
					comboSprite.setTexture(combo.c64);
					gameTime += 10;
					break;
				case 32:
					comboSprite.setTexture(combo.c32);
					gameTime += 4;
					break;
				case 16:
					comboSprite.setTexture(combo.c16);
					break;
				default:
					if (sayac > 16 && sayac % 16 == 0) {
						comboSprite.setTexture(combo.c16_);
					}
					break;
				}

				// 16'nýn her katýnda standart yapýlacaklar
				if (sayac > 0 && sayac % 16 == 0) {
					stepTime = 16;
					timerKademe.start();
					mnzMax = (APP_HEIGHT - 2 * kademe);

					Tween.set(comboSprite, TweenAccessor.ALPHA).target(0)
							.start(tweenManager);
					Tween.to(comboSprite, TweenAccessor.ALPHA, 2).target(1)
							.start(tweenManager);
					Tween.to(comboSprite, TweenAccessor.ALPHA, 2).target(0)
							.delay(2).start(tweenManager);
				}

				return true;
			}

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}
		});

	}

	@Override
	public void dispose() {

		background.dispose();
		background2.dispose();
		targetRect.dispose();
		targetRect2.dispose();
		leftPad.dispose();
		menzil.dispose();
		menzil2.dispose();
		bottomPad.dispose();
		sb.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		// Rekor güncellenir
		if (score > prefs.getInteger("score", 0)) {
			highScore = score;
			prefs.putInteger("score", score);
			prefs.flush();
		}

	}

	public void reset() {

		discGenerator.discs.removeAll(discGenerator.discs);
		gameOver = false;
		gameTime = 120;
		score = 0;
		mnzY = mnzMin + 1;
		mnzSpeed = APP_HEIGHT / 600;
		leftY = mnzMin + mnzHeight / 2;
		sayac = 0;
		mnzMax = APP_HEIGHT - game_.playScreen.kademe;
		timerGame.start();
		timer400.start();
	}
}
