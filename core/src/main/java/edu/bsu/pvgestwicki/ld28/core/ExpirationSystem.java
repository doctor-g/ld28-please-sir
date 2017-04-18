package edu.bsu.pvgestwicki.ld28.core;

import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;

public final class ExpirationSystem extends AbstractSystem {

	protected ExpirationSystem(World world) {
		super(world, SystemPriority.NORMAL);
	}

	@Override
	protected boolean isInterested(Entity entity) {
		return entity.has(VictorianWorld.msUntilRemoval);
	}

	@Override
	protected void update(int delta, Entities entities) {
		for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
			int entityID = entities.get(ii);
			int msUntilRemoval = VictorianWorld.msUntilRemoval.get(entityID);
			msUntilRemoval -= delta;
			if (msUntilRemoval <= 0) {
				world.entity(entityID).destroy();
			} else {
				VictorianWorld.msUntilRemoval.set(entityID, msUntilRemoval);
			}
		}
	}

}
