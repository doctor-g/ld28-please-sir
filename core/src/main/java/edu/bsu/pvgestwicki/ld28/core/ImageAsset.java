package edu.bsu.pvgestwicki.ld28.core;

import playn.core.Image;

import static playn.core.PlayN.assets;

public enum ImageAsset {

	OLIVER("images/oliver.png"), //
	BACKGROUND("images/bg.png"), //
	ORPHAN("images/orphan.png"), //
	LEFT_BUMBLE("images/lbumble.png"), //
	RIGHT_BUMBLE("images/rbumble.png"), //
	BIG_BUMBLE("images/bigbumble.png"), //
	SPEECH_BUBBLE("images/bubble.png"), //
	BOWL("images/bowl.png");

	public final Image image;

	private ImageAsset(String path) {
		this.image = assets().getImage(path);
	}

}
