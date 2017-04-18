package edu.bsu.pvgestwicki.ld28.core;

import static com.google.common.base.Preconditions.checkNotNull;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;
import playn.core.GroupLayer;
import playn.core.Layer;
import playn.core.util.Clock;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;

public final class SpriteSystem extends AbstractSystem {

	private GroupLayer stage;

	protected SpriteSystem(World world, GroupLayer stage) {
		super(world, SystemPriority.NORMAL);
		this.stage = checkNotNull(stage);
	}

	@Override
	protected boolean isInterested(Entity entity) {
		return entity.has(VictorianWorld.position)
				&& entity.has(VictorianWorld.sprite);
	}

	private final Point position = new Point();
	private final Point oldPosition = new Point();

	@Override
	protected void paint(Clock clock, Entities entities) {
		super.paint(clock, entities);
		float alpha = clock.alpha();
		for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
			int entityID = entities.get(ii);
			VictorianWorld.position.get(entityID, position);
			Layer.HasSize layer = VictorianWorld.sprite.get(entityID);

			// Move the entity if it has an old position
			if (hasOldPosition(entityID)) {
				VictorianWorld.oldPosition.get(entityID, oldPosition);
				float newX = MathUtil.lerp(oldPosition.x, position.x, alpha);
				float newY = MathUtil.lerp(oldPosition.y, position.y, alpha);
				layer.setTranslation(newX, newY);
			} else {
				layer.setTranslation(position.x, position.y);
			}
		}
	}

	private boolean hasOldPosition(int entityID) {
		return world.entity(entityID).has(VictorianWorld.oldPosition);
	}

	@Override
	protected void wasAdded(Entity entity) {
		super.wasAdded(entity);
		if (hasOldPosition(entity.id)) {
			VictorianWorld.oldPosition.get(entity.id, position);
		} else {
			VictorianWorld.position.get(entity.id, position);
		}
		stage.addAt(VictorianWorld.sprite.get(entity.id), //
				position.x, position.y);
	}

	@Override
	protected void wasRemoved(Entity entity, int index) {
		super.wasRemoved(entity, index);
		stage.remove(VictorianWorld.sprite.get(entity.id));
	}

}
