package edu.bsu.pvgestwicki.ld28.core;

import static playn.core.PlayN.graphics;
import static playn.core.PlayN.pointer;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.Pointer.Event;
import playn.core.util.Clock;
import pythagoras.f.Point;
import react.Signal;
import react.SignalView;
import react.Slot;
import react.UnitSlot;
import react.Value;
import react.ValueView.Listener;
import tripleplay.game.UIAnimScreen;
import tripleplay.ui.Label;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.util.Layers;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;

public class PlayingScreen extends UIAnimScreen {

	private GroupLayer gameLayer;
	private VictorianWorld world;

	private Value<Integer> health = Value.create(10);
	private int elapsedMS = 0;
	private Label timeLabel = new Label();
	private ImageLayer bumbleLayer;
	private Signal<Integer> onGameOver = Signal.create();

	private AbstractSystem moverSystem;
	private AbstractSystem orphanSystem;
	private AbstractSystem inputSystem;
	private AbstractSystem timeLabelUpdateSystem;
	private AbstractSystem spriteSystem;

	public SignalView<Integer> onGameOver() {
		return onGameOver;
	}

	@Override
	public void wasAdded() {
		world = new VictorianWorld();

		initBackground();
		createGameLayer();
		createMrBumble();

		moverSystem = new MoverSystem(world);
		spriteSystem = new SpriteSystem(world, gameLayer);
		CollisionSystem collisionSys = new CollisionSystem(world);
		collisionSys.onOrphanHitBumble().connect(new UnitSlot() {
			@Override
			public void onEmit() {
				health.update(health.get() - 1);
				anim.tweenScale(bumbleLayer)//
						.from(1f)//
						.to(1f)//
						.using(Interpolators.SHAKING)//
						.in(150f);
				if (health.get() > 0) {
					Audio.playMore();
				} else {
					Audio.playLongMore();
				}
			}
		});
		new ExpirationSystem(world);
		new GravityDecaySystem(world);
		timeLabelUpdateSystem = new AbstractSystem(world, SystemPriority.NORMAL) {
			@Override
			protected boolean isInterested(Entity entity) {
				return false;
			}

			@Override
			protected void update(int delta, Entities entities) {
				int seconds = elapsedMS / 1000;
				timeLabel.text.update("Time: " + seconds);
			}
		};

		initInputSystem();
		initHUD();
		initDeathWatcher();

		// Delay before starting the orphan system because of screen transitions
		anim.delay(1000).then().action(new Runnable() {
			@Override
			public void run() {
				orphanSystem = new OrphanGeneratingSystem(world, graphics()
						.width() * 2 / 3);
			}
		});
	}

	private void createGameLayer() {
		Point origin = new Point(graphics().width() / 2,
				graphics().height() * 2 / 3);
		gameLayer = graphics().createGroupLayer();
		gameLayer.setTranslation(origin.x, origin.y);
		layer.add(gameLayer);
	}

	private void createMrBumble() {
		Entity e = world.create(true).add(//
				VictorianWorld.position,//
				VictorianWorld.sprite,//
				VictorianWorld.type,//
				VictorianWorld.radius);

		Image bumbleImage = ImageAsset.LEFT_BUMBLE.image;
		bumbleLayer = graphics().createImageLayer(bumbleImage);
		bumbleLayer.setOrigin(bumbleImage.width() / 2, 50);

		VictorianWorld.position.set(e.id, new Point());
		VictorianWorld.sprite.set(e.id, bumbleLayer);
		VictorianWorld.type.set(e.id, Type.BUMBLE);
		VictorianWorld.radius.set(e.id, bumbleImage.width() / 2);
	}

	private void initBackground() {
		ImageLayer bgLayer = graphics().createImageLayer(
				ImageAsset.BACKGROUND.image);
		layer.add(bgLayer);
	}

	private void initInputSystem() {
		inputSystem = new AbstractSystem(world, SystemPriority.NORMAL) {
			private final int radius = 50;
			private Point p = new Point();

			{
				pointer().setListener(new Pointer.Adapter() {
					@Override
					public void onPointerStart(Event event) {
						Audio.playTick();

						p.set(event.x(), event.y());
						Layers.transform(p, layer, gameLayer, p);
						createGruel(p.x, p.y);

						bumbleLayer
								.setImage(p.x < 0 ? ImageAsset.LEFT_BUMBLE.image
										: ImageAsset.RIGHT_BUMBLE.image);
					}
				});
			}

			@Override
			protected boolean isInterested(Entity entity) {
				return false;
			}

			private void createGruel(float x, float y) {
				Entity e = world.create(true).add(
						//
						VictorianWorld.position, //
						VictorianWorld.sprite,//
						VictorianWorld.type,//
						VictorianWorld.radius,//
						VictorianWorld.msUntilRemoval,
						VictorianWorld.gravityWell);

				Image img = ImageAsset.BOWL.image;
				ImageLayer layer = graphics().createImageLayer(
						ImageAsset.BOWL.image);
				layer.setOrigin(img.width() / 2, img.height() / 2);

				anim.tweenAlpha(layer).from(1f).to(0f)
						.in(Settings.GRUEL_DURATION_MS);

				VictorianWorld.position.set(e.id, new Point(x, y));
				VictorianWorld.sprite.set(e.id, layer);
				VictorianWorld.radius.set(e.id, radius);
				VictorianWorld.type.set(e.id, Type.GRUEL);
				VictorianWorld.msUntilRemoval.set(e.id,
						Settings.GRUEL_DURATION_MS);
				VictorianWorld.gravityWell.set(e.id, Settings.STARTING_GRAVITY);
			}

			@Override
			public void setEnabled(boolean enabled) {
				super.setEnabled(enabled);
				if (!enabled) {
					pointer().setListener(null);
				}
			}
		};
	}

	private void initHUD() {
		Root root = iface//
				.createRoot(new AbsoluteLayout(), SimpleStyles.newSheet(),
						layer)//
				.setSize(graphics().width(), graphics().height());

		final Label label = new Label("Health: " + health.get());
		root.add(AbsoluteLayout.at(label, new Point(10, 10)));
		root.add(AbsoluteLayout.at(timeLabel, new Point(10, 30)));

		health.connect(new Listener<Integer>() {
			@Override
			public void onChange(Integer value, Integer oldValue) {
				label.text.update("Health: " + value);
			}
		});
	}

	private void initDeathWatcher() {
		health.connect(new Slot<Integer>() {
			@Override
			public void onEmit(Integer health) {
				if (health == 0) {
					anim.tweenAlpha(bumbleLayer)//
							.from(1)//
							.to(0)//
							.in(1000)//
							.easeOut();

					orphanSystem.setEnabled(false);
					moverSystem.setEnabled(false);
					inputSystem.setEnabled(false);
					timeLabelUpdateSystem.setEnabled(false);
					spriteSystem.setEnabled(false);
					Audio.stopMusic();
					onGameOver.emit(elapsedMS);
				}
			}
		});
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		Audio.update(delta);
		world.update(delta);
		elapsedMS += delta;
	}

	@Override
	public void paint(Clock clock) {
		super.paint(clock);
		world.paint(clock);
	}

}
