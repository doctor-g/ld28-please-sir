package edu.bsu.pvgestwicki.ld28.core;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;
import playn.core.CanvasImage;
import playn.core.Gradient;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import pythagoras.f.FloatMath;
import pythagoras.f.Point;
import react.SignalView;
import react.UnitSignal;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.util.Colors;

public class DialogScreen extends UIAnimScreen {

	private ImageLayer oliverLayer;
	private ImageLayer bumbleLayer;
	private ImageLayer speechBubbleLayer;
	private final UnitSignal onIntroFinished = new UnitSignal();
	private Root root;

	public DialogScreen() {
		createScene();
	}

	public DialogScreen(int elapsedMS) {
		createScene();
		createScoreBox(elapsedMS);
		anim.tweenScale(bumbleLayer)//
				.from(1f)//
				.to(1f)//
				.using(Interpolators.SHAKING)//
				.in(500);
	}

	private void createScene() {
		CanvasImage image = graphics().createImage(graphics().width(),
				graphics().height());
		Gradient gradient = graphics().createLinearGradient(0, 0, 0,
				image.height(), new int[] { Colors.BLACK, Colors.WHITE },
				new float[] { 0, 1 });
		image.canvas().setFillGradient(gradient);
		image.canvas().fillRect(0, 0, image.width(), image.height());
		ImageLayer bgLayer = graphics().createImageLayer(image);
		layer.add(bgLayer);

		final GroupLayer stage = graphics().createGroupLayer();
		layer.add(stage);

		bumbleLayer = graphics().createImageLayer(ImageAsset.BIG_BUMBLE.image);
		bumbleLayer.setTranslation(380, 100);
		stage.add(bumbleLayer);

		oliverLayer = graphics().createImageLayer(ImageAsset.OLIVER.image);
		oliverLayer.setTranslation(130, 210);
		stage.add(oliverLayer);

		speechBubbleLayer = graphics().createImageLayer(
				ImageAsset.SPEECH_BUBBLE.image);
		stage.add(speechBubbleLayer);

		int stageOriginX = 380 + 56, stageOriginY = 100 + 36;
		stage.setOrigin(stageOriginX, stageOriginY);
		stage.setTranslation(stageOriginX, stageOriginY);

		pointer().setListener(new Pointer.Adapter() {
			@Override
			public void onPointerEnd(Event event) {
				pointer().setListener(null);
				Audio.startMusic();
				if (root != null) { // fade out the old score label
					anim.tweenAlpha(root.layer).to(0)//
							.in(1000f);
				}
				anim.tweenRotation(stage)//
						.from(0)//
						.to(FloatMath.PI * 6)//
						.in(2000f)//
						.easeIn();
				anim.tweenScale(stage)//
						.from(1)//
						.to(8)//
						.in(2000f);
				anim.delay(1000f)//
						.then()//
						.tweenAlpha(stage)//
						.from(1f)//
						.to(0f)//
						.in(1000f)//
						.then()//
						.action(new Runnable() {
							@Override
							public void run() {
								onIntroFinished.emit();
							}
						});
			}
		});
	}

	private void createScoreBox(int elapsedMS) {
		root = iface.createRoot(new AbsoluteLayout(), SimpleStyles.newSheet(),
				layer)//
				.setSize(graphics().width(), graphics().height());

		Label scoreLabel = new Label("Time: " + (elapsedMS / 1000))//
				.addStyles(Style.COLOR.is(Colors.WHITE));

		root.add(AbsoluteLayout.at(scoreLabel, new Point(10, 30)));
	}

	public SignalView<Void> onIntroFinished() {
		return onIntroFinished;
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		Audio.update(delta);
		speechBubbleLayer.setTranslation(110, 100);
	}

}
