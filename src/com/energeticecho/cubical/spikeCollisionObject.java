package com.energeticecho.cubical;

import com.androidapps.framework.math.Circle;
import com.androidapps.framework.math.Rectangle;
import com.androidapps.framework.math.Vector2;

public class spikeCollisionObject {
	public final Vector2 position;
	public final Circle bounds;
	
	public spikeCollisionObject(float x, float y, float r) {
		this.position = new Vector2(x, y);
		this.bounds = new Circle(x, y, r);
	}
}

