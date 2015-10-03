package com.harunakdogan.fourhundredms.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.harunakdogan.fourhundredms.core.FourHundredMilliseconds;

/**
 * @author Harun
 * 
 **/

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 400;
		config.height = 600;
		config.title = "TeleBird";
		new LwjglApplication(new FourHundredMilliseconds(), config);
	}
}