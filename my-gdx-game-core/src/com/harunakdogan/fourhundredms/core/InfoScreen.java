package com.harunakdogan.fourhundredms.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.harunakdogan.fourhundredms.core.MenuScreen;

public class InfoScreen implements Screen {

	public FourHundredMilliseconds game_;
	public SpriteBatch sb;
	public Stage stage;
	public Table table;
	public Label label;
	public TextureAtlas atlas;
	public Skin skin;
	public Texture back;

	public InfoScreen(FourHundredMilliseconds game) {

		game_ = game;
		sb = new SpriteBatch();
		stage = new Stage();
		table = new Table();
		atlas = new TextureAtlas(
				Gdx.files.internal("atlasses/buttonSkin.atlas"));
		skin = new Skin(Gdx.files.internal("json/menuSkin.json"), atlas);
		label = new Label(
				"Developer:\n    Harun Akdogan\n    akdogan_41090@hotmail.com\n\nDeveloped using:\n    LibGDX Framework by Badlogic Games\n\nGame Music:\n    Ryan Baillargeon - vstom4",
				skin);
		label.setFontScale(0.7f, 0.7f);
		label.getStyle().fontColor = Color.BLUE;
		table.add(label);
		table.row();

		table.setBounds(0, 0, MenuScreen.APP_WIDTH, MenuScreen.APP_HEIGHT);

		stage.addActor(table);

		back = new Texture("textures/back.png");
	}

	@Override
	public void show() {

		Gdx.input.setInputProcessor(new InputHandler() {

			@Override
			public boolean touchDown(int x, int y, int pointer, int button) {

				if (x > MenuScreen.APP_WIDTH - MenuScreen.APP_WIDTH / 40
						- MenuScreen.APP_WIDTH / 8
						&& x < MenuScreen.APP_WIDTH - MenuScreen.APP_WIDTH / 40
								- MenuScreen.APP_WIDTH / 8
								+ MenuScreen.APP_WIDTH / 8
						&& y > MenuScreen.APP_HEIGHT / 60
						&& y < MenuScreen.APP_HEIGHT * 0.1f) {
					game_.setScreen(game_.menuScreen);

				}

				return super.touchDown(x, y, pointer, button);
			}
		});
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();
		stage.draw();

		sb.begin();
		sb.draw(back, MenuScreen.APP_WIDTH - MenuScreen.APP_WIDTH / 40
				- MenuScreen.APP_WIDTH / 8, MenuScreen.APP_HEIGHT * 0.9f,
				MenuScreen.APP_WIDTH / 8, MenuScreen.APP_HEIGHT / 12);
		sb.end();
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

	@Override
	public void dispose() {
	}
}
