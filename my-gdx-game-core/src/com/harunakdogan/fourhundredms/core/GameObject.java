package com.harunakdogan.fourhundredms.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameObject extends Sprite {

	public int x,y;
	public int width, height;
	
	public GameObject(Texture texture, int x, int y, int width, int height) {
		super(texture);
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}