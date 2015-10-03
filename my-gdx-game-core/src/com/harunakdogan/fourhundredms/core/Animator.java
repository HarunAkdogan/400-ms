package com.harunakdogan.fourhundredms.core;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animator {

	public ArrayList<TextureRegion> regions;
	public int animCycles, currentFrame = 0, x, y;
	public float maxFrameTime, frameTime = 0;
	public boolean animate;

	public Animator(Texture texture, int animCycles, float maxFrameTime) {
		regions = new ArrayList<TextureRegion>();
		this.animCycles = animCycles;
		this.maxFrameTime = maxFrameTime;
		frameTime = 0;

		for (int i = 0; i < animCycles; i++) {
			regions.add(new TextureRegion(texture, i * 20, 0, texture
					.getWidth() / animCycles, texture.getHeight()));
		}
	}

	public void update(float dt) {

		if (animate) {

			frameTime += dt;

			if (frameTime >= maxFrameTime){
				currentFrame++;
				frameTime = 0;
			}

			if (currentFrame >= animCycles) {
				currentFrame = 0;
				animate = false;
			}
			y-= 2;
		}
	}

	public void setAnimation(int x, int y) {
		this.x = x;
		this.y = y;
		animate = true;
	}

	public TextureRegion getTexture() {

		return regions.get(currentFrame);
	}

}
