package edu.bsu.pvgestwicki.ld28.core;

import pythagoras.f.Point;
import pythagoras.f.Vector;
import react.SignalView;
import react.UnitSignal;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;

public final class CollisionSystem extends AbstractSystem {

	private final UnitSignal onOrphanHitBumble = new UnitSignal();

	protected CollisionSystem(World world) {
		super(world, SystemPriority.NORMAL);
	}

	public SignalView<Void> onOrphanHitBumble() {
		return onOrphanHitBumble;
	}

	@Override
	protected boolean isInterested(Entity entity) {
		return entity.has(VictorianWorld.position)
				&& entity.has(VictorianWorld.radius)
				&& entity.has(VictorianWorld.type);
	}

	@Override
	protected void update(int delta, Entities entities) {
		for (int ii = 1, ll = entities.size(); ii < ll; ii++) {
			int entityA = entities.get(ii);
			for (int jj = 0; jj < ii; jj++) {
				int entityB = entities.get(jj);
				checkCollision(entityA, entityB);
			}
		}
	}

	private int maskA;
	private int maskB;
	private final Point positionA = new Point(), positionB = new Point();

	private void checkCollision(int entityA, int entityB) {
		maskA = VictorianWorld.type.get(entityA);
		maskB = VictorianWorld.type.get(entityB);
		if (maskA != maskB) {
			VictorianWorld.position.get(entityA, positionA);
			VictorianWorld.position.get(entityB, positionB);
			float distanceSq = positionA.distanceSq(positionB);

			float radiusA = VictorianWorld.radius.get(entityA);
			float radiusB = VictorianWorld.radius.get(entityB);
			float radiusSum = radiusA + radiusB;

			if (distanceSq <= radiusSum * radiusSum) {
				collide(entityA, entityB);
			}
		}
	}

	private void collide(int entityA, int entityB) {
		int theOrphan;
		switch (maskA | maskB) {
		case Type.BUMBLE_ORPHAN_COLLISION:
			theOrphan = VictorianWorld.type.get(entityA) == Type.ORPHAN ? entityA
					: entityB;
			world.entity(theOrphan).destroy();
			onOrphanHitBumble.emit();
			break;
		case Type.GRUEL_ORPHAN_COLLISION:
			if (VictorianWorld.type.get(entityA) == Type.ORPHAN) {
				updateVelocity(entityA, entityB);
			} else {
				updateVelocity(entityB, entityA);
			}
			break;
		}
	}

	private final Point positionOrphan = new Point();
	private final Point positionGruel = new Point();
	private final Point difference = new Point();
	private final Vector velocity = new Vector();
	private final Vector differenceVector = new Vector();

	private void updateVelocity(int orphanID, int gruelID) {
		VictorianWorld.position.get(orphanID, positionOrphan);
		VictorianWorld.position.get(gruelID, positionGruel);
		VictorianWorld.velocity.get(orphanID, velocity);

		float gravityStrength = VictorianWorld.gravityWell.get(gruelID);

		positionGruel.subtract(positionOrphan, difference);
		differenceVector.set(difference.x, difference.y);
		differenceVector.normalizeLocal().scaleLocal(gravityStrength);

		velocity.addLocal(differenceVector.x, differenceVector.y);
		VictorianWorld.velocity.set(orphanID, velocity);
	}
}
