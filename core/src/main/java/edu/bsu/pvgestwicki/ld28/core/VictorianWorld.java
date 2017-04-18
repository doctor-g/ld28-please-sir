package edu.bsu.pvgestwicki.ld28.core;

import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.Component;
import edu.bsu.pvgestwicki.ld28.core.tripleplay.entity.World;
import playn.core.Layer;

public class VictorianWorld extends World {

	public static Component.XY position;
	public static Component.XY oldPosition;
	public static Component.XY velocity;
	public static Component.Generic<Layer.HasSize> sprite;
	public static Component.IMask type;
	public static Component.FScalar radius;
	public static Component.IScalar msUntilRemoval;
	public static Component.FScalar gravityWell;

	public VictorianWorld() {
		initializeComponentConstants();
	}

	private void initializeComponentConstants() {
		position = new Component.XY(this);
		oldPosition = new Component.XY(this);
		velocity = new Component.XY(this);
		sprite = new Component.Generic<Layer.HasSize>(this);
		type = new Component.IMask(this);
		radius = new Component.FScalar(this);
		msUntilRemoval = new Component.IScalar(this);
		gravityWell = new Component.FScalar(this);
	}
}
