package edu.bsu.pvgestwicki.ld28.core;

import static playn.core.PlayN.graphics;
import playn.core.AssetWatcher;
import pythagoras.f.Point;
import react.SignalView;
import react.UnitSignal;
import tripleplay.game.UIScreen;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.util.Colors;

public final class LoadingScreen extends UIScreen {

	private final UnitSignal finishedSignal = new UnitSignal();

	private final AssetWatcher watcher = new AssetWatcher();
	private boolean hasFinished = false;

	public LoadingScreen() {
		for (ImageAsset asset : ImageAsset.values()) {
			watcher.add(asset.image);
		}
		watcher.add(Audio.music);
		watcher.add(Audio.moreLong);

		Root root = iface.createRoot(new AbsoluteLayout(),
				SimpleStyles.newSheet(), layer)//
				.setSize(graphics().width(), graphics().height());

		root.add(AbsoluteLayout.at(//
				new Label("Loading...")//
						.addStyles(Style.COLOR.is(Colors.WHITE)),//
				new Point(300, 240)));
	}

	public SignalView<Void> onFinishedLoading() {
		return finishedSignal;
	}

	@Override
	public void update(int delta) {
		if (watcher.isDone() && !hasFinished) {
			finishedSignal.emit();
			hasFinished = true;
		}
	}
}
