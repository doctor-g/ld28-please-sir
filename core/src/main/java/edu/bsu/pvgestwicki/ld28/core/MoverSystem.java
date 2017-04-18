package edu.bsu.pvgestwicki.ld28.core;

import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;
import pythagoras.f.Point;
import pythagoras.f.Vector;

public final class MoverSystem extends AbstractSystem {

	protected MoverSystem(World world) {
		super(world, SystemPriority.NORMAL);
	}

	@Override
	protected boolean isInterested(Entity entity) {
		return entity.has(VictorianWorld.position)
				&& entity.has(VictorianWorld.oldPosition)
				&& entity.has(VictorianWorld.velocity);
	}

	private final Point position = new Point();
	private final Vector velocity = new Vector();

	@Override
	protected void update(int deltaMS, Entities entities) {
		for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
			int entityID = entities.get(ii);
			VictorianWorld.position.get(entityID, position);
			VictorianWorld.velocity.get(entityID, velocity);

			VictorianWorld.oldPosition.set(entityID, position);

			velocity.scaleLocal(deltaMS / 1000f);
			position.addLocal(velocity.x, velocity.y);
			VictorianWorld.position.set(entityID, position);
		}
	}

}
