package com.harunakdogan.fourhundredms.core;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;

public class DiscGenerator {

	ArrayList<Disc> discs;

	public DiscGenerator() {
		
		discs = new ArrayList<Disc>();
	}

	public void generate(int x, int y) {

		Disc disc = new Disc(new Texture("textures/bird.png"), x, y);
		discs.add(disc);
	}

	public ArrayList<Disc> getDiscs() {
		return discs;
	}
}
