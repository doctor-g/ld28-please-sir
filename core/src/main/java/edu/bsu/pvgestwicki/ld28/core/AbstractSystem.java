package edu.bsu.pvgestwicki.ld28.core;

import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;

public abstract class AbstractSystem extends edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.System {

	protected AbstractSystem(World world, SystemPriority priority) {
		super(world, priority.toInt());
	}

}
