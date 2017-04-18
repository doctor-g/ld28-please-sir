package edu.bsu.pvgestwicki.ld28.core;

import playn.core.Game;
import playn.core.util.Clock;
import react.Slot;
import react.UnitSlot;
import tripleplay.game.ScreenStack;
import tripleplay.game.trans.FadeTransition;

public class LD28Game extends Game.Default {

	private static final boolean SKIP_INTRO = false;

	private static final int RATE = 33;
	private ScreenStack screenStack = new ScreenStack();
	private Clock.Source clock = new Clock.Source(RATE);

	public LD28Game() {
		super(RATE);
	}

	@Override
	public void init() {
		LoadingScreen loadingScreen = new LoadingScreen();
		screenStack.push(loadingScreen);
		loadingScreen.onFinishedLoading().connect(new UnitSlot() {
			@Override
			public void onEmit() {
				if (!SKIP_INTRO) {
					showIntroScreen();
				} else {
					playGame();
				}
			}
		});
	}

	private void showIntroScreen() {
		DialogScreen intro = new DialogScreen();
		intro.onIntroFinished().connect(new UnitSlot() {
			@Override
			public void onEmit() {
				playGame();
			}
		});
		screenStack.push(intro, new FadeTransition(screenStack));
	}

	private void showIntroScreenWithScore(int elapsedMS) {
		DialogScreen intro = new DialogScreen(elapsedMS);
		intro.onIntroFinished().connect(new UnitSlot() {
			@Override
			public void onEmit() {
				playGame();
			}
		});
		screenStack.push(intro, new FadeTransition(screenStack));
	}

	private void playGame() {
		PlayingScreen playingScreen = new PlayingScreen();
		playingScreen.onGameOver().connect(new Slot<Integer>() {
			@Override
			public void onEmit(Integer elapsedMS) {
				showIntroScreenWithScore(elapsedMS);
			}
		});
		screenStack.push(playingScreen, new FadeTransition(screenStack));
	}

	@Override
	public void update(int delta) {
		clock.update(delta);
		screenStack.update(delta);
	}

	@Override
	public void paint(float alpha) {
		clock.paint(alpha);
		screenStack.paint(clock);
	}
}
