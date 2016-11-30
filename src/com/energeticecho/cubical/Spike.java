package com.energeticecho.cubical;

import com.androidapps.framework.GameObject;

public class Spike extends spikeCollisionObject {
	public static final float SPIKE_RADIUS = 0.25f;

	public Spike(float x, float y) {
		super(x, y, SPIKE_RADIUS);
	}
}
