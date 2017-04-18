package edu.bsu.pvgestwicki.ld28.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Entity;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.System;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;

public class WorldTest {

	@Test
	public void testSystemWithNoInterestsStillUpdates() {
		World world = new World();
		TestSystem system = new TestSystem(world);
		world.update(1000);
		assertTrue(system.updateWasCalled);
	}

	class TestSystem extends System {

		boolean updateWasCalled = false;

		protected TestSystem(World world) {
			super(world, 0);
		}

		@Override
		protected boolean isInterested(Entity entity) {
			return false;
		}

		@Override
		protected void update(int delta, Entities entities) {
			updateWasCalled = true;
		}

	}
}
