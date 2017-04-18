package edu.bsu.pvgestwicki.ld28.core;

import static playn.core.PlayN.assets;
import playn.core.Sound;
import tripleplay.sound.MultiClip;
import tripleplay.sound.SoundBoard;

public class Audio {

	private static final SoundBoard SB = new SoundBoard();

	public static final Sound music = assets().getMusic("music/victorian");
	public static final MultiClip more = new MultiClip(SB, "sfx/more", 4, 0.6f);
	public static final Sound moreLong = assets().getSound("sfx/longmore");
	public static final MultiClip plop = new MultiClip(SB, "sfx/tick", 4, 0.2f);

	static {
		music.setLooping(true);
	}

	public static void startMusic() {
		music.play();
	}

	public static void stopMusic() {
		music.stop();
	}

	public static void playMore() {
		more.reserve().play();
	}

	public static void playLongMore() {
		moreLong.play();
	}

	public static void playTick() {
		plop.reserve().play();
	}

	public static void update(int deltaMS) {
		SB.update(deltaMS);
	}
}
