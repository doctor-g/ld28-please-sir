package edu.bsu.pvgestwicki.ld28.core;

import pythagoras.f.FloatMath;
import tripleplay.util.Interpolator;

public final class Interpolators {

	public static final Interpolator SHAKING = new Interpolator() {
		@Override
		public float apply(float start, float range, float dt, float t) {
			return 1 + FloatMath.sin(dt / 20) / 15;
		}
	};
	
}
