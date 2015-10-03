package com.harunakdogan.fourhundredms.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.harunakdogan.fourhundredms.core.MenuScreen;

public class FourHundredMilliseconds extends Game{
	
	public PlayScreen playScreen;
	public MenuScreen menuScreen;
	public InfoScreen infoScreen;
	
	@Override
	public void create() {
	
	playScreen = new PlayScreen(this);
	menuScreen = new MenuScreen(this);	
	infoScreen = new InfoScreen(this);
	
	setScreen(menuScreen);
	
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void pause() {

		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	@Override
	public Screen getScreen() {
		return super.getScreen();
	}

}
