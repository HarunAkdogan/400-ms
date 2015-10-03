package com.harunakdogan.fourhundredms.core;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen implements Screen {

	public Stage stage;
	public Table table;
	public TextureAtlas atlas;
	public Skin skin;
	public TextButton startButton, exitButton;
	public FourHundredMilliseconds game_;
	public Label hiScoreLabel, yourScoreLabel;
	public Music buttonSound;
	public SpriteBatch sb;
	public TweenManager tweenManager;
	public Sprite nameSprite;

	public boolean soundOn = true;
	public Texture background, background2, resume, musicOn, musicOff, info,
			gameName;
	public static int APP_HEIGHT, APP_WIDTH, back1x, back1y, back2x, back2y,
			resumeX, resumeY, resumeWidth, resumeHeight;
	public float backSpeed;

	public MenuScreen(FourHundredMilliseconds game) {

		game_ = game;

		// Farklý ekranlara uyum saðlamasý açýsýndan bu deðerler alýndý
		APP_HEIGHT = Gdx.app.getGraphics().getHeight();
		APP_WIDTH = Gdx.app.getGraphics().getWidth();
		//

		back1x = back2x = 0;
		back1y = 0;
		back2y = APP_HEIGHT;

		resumeX = APP_WIDTH / 40;
		resumeY = APP_HEIGHT - APP_HEIGHT / 10;
		resumeWidth = APP_WIDTH / 8;
		resumeHeight = APP_HEIGHT / 12;

		background = new Texture("textures/background.png");
		background2 = new Texture("textures/background.png");
		resume = new Texture("textures/resume.png");
		musicOn = new Texture("textures/soundOn.png");
		musicOff = new Texture("textures/soundOff.png");
		info = new Texture("textures/info.png");
		gameName = new Texture("textures/gameName.png");

		sb = new SpriteBatch();

		tweenManager = new TweenManager();

		nameSprite = new Sprite(gameName);
		nameSprite.setSize(APP_WIDTH, APP_HEIGHT / 6);

		Tween.registerAccessor(Sprite.class, new TweenAccessor());

		Tween.set(nameSprite, TweenAccessor.ALPHA).target(0)
				.start(tweenManager);

		// Scene2D elemanlarýný oluþturuldu
		stage = new Stage();
		atlas = new TextureAtlas(
				Gdx.files.internal("atlasses/buttonSkin.atlas"));
		skin = new Skin(Gdx.files.internal("json/menuSkin.json"), atlas);

		buttonSound = Gdx.audio.newMusic(Gdx.files.internal("sounds/vstom4.mp3"));
		buttonSound.setLooping(true);
		buttonSound.setVolume(0.5f);
		buttonSound.play();

		hiScoreLabel = new Label("High Score " + game_.playScreen.highScore,
				skin);
		yourScoreLabel = new Label("", skin);
		hiScoreLabel.setFontScaleX(2);

		yourScoreLabel.setFontScaleX(2);

		table = new Table(skin);
		table.setBounds(0, 0, APP_WIDTH, APP_HEIGHT);

		startButton = new TextButton("NEW", skin);
		exitButton = new TextButton("EXIT", skin);

		startButton.getStyle().font.getData().setScale(3, 3);

		startButton.pack();
		exitButton.pack();

		startButton.pad(30);
		exitButton.pad(30);
		//

		startButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				game_.playScreen.reset();
				game_.setScreen(game_.playScreen);

				super.clicked(event, x, y);
			}
		});

		exitButton.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {

				Gdx.app.exit();
				super.clicked(event, x, y);
			}
		});

		stage.addActor(table);
	}

	@Override
	public void show() {

		// BU ekran görüntülendiðinde input'larý almasý için show methodu
		// içerisine yerleþtirildi
		Gdx.input.setInputProcessor(new InputMultiplexer(stage,
				new InputHandler() {

					@Override
					public boolean touchDown(int x, int y, int pointer,
							int button) {

						// Oyuna geri dönmek için bu alana dokunulur
						if (x < resumeX + resumeWidth && x > resumeX
								&& y < APP_HEIGHT - resumeY
								&& y > APP_HEIGHT - (resumeY + resumeHeight)
								&& !game_.playScreen.isRunning) {
							game_.playScreen.isRunning = true;
							game_.playScreen.timerGame.start();
							game_.playScreen.timer400.start();
							game_.setScreen(game_.playScreen);
							// Ses açýp - kapatmak içi bu alana dokunulur
						} else if (x < APP_WIDTH / 2 + resumeWidth / 2
								&& x > APP_WIDTH / 2 - resumeWidth / 2
								&& y < APP_HEIGHT - resumeY
								&& y > APP_HEIGHT - (resumeY + resumeHeight)) {
							soundOn = !soundOn;
							if (soundOn)
								buttonSound.setVolume(0.5f);
							else
								buttonSound.setVolume(0);
							// Bilgi ekranýna geçmek için bu alana dokunulur
						} else if (x < APP_WIDTH - APP_WIDTH / 40
								&& x > APP_WIDTH - resumeWidth - APP_WIDTH / 40
								&& y < APP_HEIGHT - resumeY
								&& y > APP_HEIGHT - (resumeY + resumeHeight)) {
							game_.setScreen(game_.infoScreen);
						}

						return false;
					}

				}));
		//

		hiScoreLabel.setText("High Score: " + game_.playScreen.highScore);

		// Tablo reset'lenip duruma göre güncellenir
		table.reset();

		table.add("");
		table.row();

		table.add(hiScoreLabel);
		table.row();

		if (game_.playScreen.gameOver) {
			yourScoreLabel.setText("Your Score: " + game_.playScreen.score);
			table.add(yourScoreLabel);
		} else
			table.add("");

		table.row();

		table.add(startButton);
		table.row();
		table.add(exitButton);
		//
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Arkaplan görüntüsünü kaydýr ve düzenle
		backSpeed =  (APP_HEIGHT / 300) * 100 * delta;
		back1y -= backSpeed;
		back2y -= backSpeed;

		if (back1y < -APP_HEIGHT)
			back1y = MenuScreen.back2y + APP_HEIGHT;
		if (back2y < -APP_HEIGHT)
			back2y = MenuScreen.back1y + APP_HEIGHT;

		//

		tweenManager.update(delta);

		sb.begin();

		sb.draw(background, back1x, back1y, APP_WIDTH, APP_HEIGHT);
		sb.draw(background2, back2x, back2y, APP_WIDTH, APP_HEIGHT);

		// Oyun durdurulmuþsa bu simgeyi çiz
		if (!game_.playScreen.isRunning)
			sb.draw(resume, resumeX, resumeY, resumeWidth, resumeHeight);

		// Ses açýksa bunu, deðilse alttakini çiz
		if (soundOn)
			sb.draw(musicOn, APP_WIDTH / 2 - resumeWidth / 2, resumeY,
					resumeWidth, resumeHeight);
		else
			sb.draw(musicOff, APP_WIDTH / 2 - resumeWidth / 2, resumeY,
					resumeWidth, resumeHeight);
		//

		sb.draw(info, APP_WIDTH - resumeWidth - APP_WIDTH / 40, resumeY,
				resumeWidth, resumeHeight);

		// Oyunun isminin yazýlý olduðu geçiþ efektini tablonun baþýna hizala
		if (table.getCells().get(0).getActorY() > APP_HEIGHT / 2)
			nameSprite.setPosition(0, table.getCells().get(0).getActorY());

		nameSprite.draw(sb);

		// Hâlihazýrda çalýþan bir geçiþ yoksa çalýþ
		if (tweenManager.getRunningTweensCount() == 0) {
			Tween.set(nameSprite, TweenAccessor.ALPHA).target(0)
					.start(tweenManager);
			Tween.to(nameSprite, TweenAccessor.ALPHA, 0).target(1)
					.start(tweenManager);
			Tween.to(nameSprite, TweenAccessor.ALPHA, 0).target(0).delay(0.4f)
					.start(tweenManager);
		}
		sb.end();

		stage.act();
		stage.draw();

	}

	@Override
	public void dispose() {
		stage.dispose();
		background.dispose();
		background2.dispose();
		resume.dispose();
		musicOn.dispose();
		musicOff.dispose();
		info.dispose();
		gameName.dispose();
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
	}
}
