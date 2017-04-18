package edu.bsu.pvgestwicki.ld28.core;

import static com.google.common.base.Preconditions.checkArgument;
import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.random;
import playn.core.Image;
import playn.core.Layer;
import pythagoras.f.FloatMath;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;
import pythagoras.f.Vector;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;

public class OrphanGeneratingSystem extends AbstractSystem {

	private static final Image ORPHAN_IMAGE = assets().getImage(
			"images/orphan.png");
	private static final float ORPHAN_SPEED_PPS = 100;

	private static final float EIGHTH_OF_A_CIRCLE = (float) Math.PI / 4;
	private static final float MIN_ANGLE = (float) Math.PI - EIGHTH_OF_A_CIRCLE;
	private static final float MAX_ANGLE = MathUtil.TWO_PI + EIGHTH_OF_A_CIRCLE;
	private static final float ANGLE_DIFFERENCE = MAX_ANGLE - MIN_ANGLE;

	private final float distanceFromOriginToOrphan;
	private float orphansPerSecond = 1;
	private float timeUntilNextOrphan = 1000 / orphansPerSecond;

	protected OrphanGeneratingSystem(World world,
			float distanceFromOriginToOrphan) {
		super(world, SystemPriority.NORMAL);
		checkArgument(distanceFromOriginToOrphan > 0);
		this.distanceFromOriginToOrphan = distanceFromOriginToOrphan;
	}

	@Override
	protected boolean isInterested(Entity entity) {
		return false;
	}

	@Override
	protected void update(int deltaMS, Entities entities) {
		orphansPerSecond += deltaMS / 1000f / 8;
		timeUntilNextOrphan -= deltaMS;
		while (timeUntilNextOrphan <= 0) {
			createOrphan();
			timeUntilNextOrphan += 1000 / orphansPerSecond;
		}
	}

	private void createOrphan() {
		Entity orphan = world.create(true).add(//
				VictorianWorld.position,//
				VictorianWorld.oldPosition,//
				VictorianWorld.velocity,//
				VictorianWorld.sprite, //
				VictorianWorld.type,//
				VictorianWorld.radius);

		float theta = random() * ANGLE_DIFFERENCE + MIN_ANGLE;
		float cos = FloatMath.cos(theta);
		float sin = FloatMath.sin(theta);
		float x = cos * distanceFromOriginToOrphan;
		float y = sin * distanceFromOriginToOrphan;
		float dx = -cos * ORPHAN_SPEED_PPS;
		float dy = -sin * ORPHAN_SPEED_PPS;

		Layer.HasSize layer = graphics().createImageLayer(ORPHAN_IMAGE);
		layer.setOrigin(ORPHAN_IMAGE.width() / 2, ORPHAN_IMAGE.height() / 2);

		int id = orphan.id;
		VictorianWorld.position.set(id, new Point(x, y));
		VictorianWorld.oldPosition.set(id, new Point(x, y));
		VictorianWorld.velocity.set(id, new Vector(dx, dy));
		VictorianWorld.sprite.set(id, layer);
		VictorianWorld.type.set(id, Type.ORPHAN);
		VictorianWorld.radius.set(id, ORPHAN_IMAGE.width() / 2);
	}
}
