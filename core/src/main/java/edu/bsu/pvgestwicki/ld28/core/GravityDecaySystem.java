package edu.bsu.pvgestwicki.ld28.core;

import pythagoras.f.FloatMath;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;

public final class GravityDecaySystem extends AbstractSystem {

	protected GravityDecaySystem(World world) {
		super(world, SystemPriority.NORMAL);
	}

	@Override
	protected boolean isInterested(Entity entity) {
		return entity.has(VictorianWorld.gravityWell)
				&& entity.has(VictorianWorld.msUntilRemoval);
	}

	@Override
	protected void update(int deltaMS, Entities entities) {
		for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
			int entityId = entities.get(ii);
			float gravity = VictorianWorld.gravityWell.get(entityId);
			int msUntilRemoval = VictorianWorld.msUntilRemoval.get(entityId);
			gravity = FloatMath.lerp(0, Settings.STARTING_GRAVITY,
					(float) msUntilRemoval / Settings.GRUEL_DURATION_MS);
			VictorianWorld.gravityWell.set(entityId, gravity);
		}
	}

}
