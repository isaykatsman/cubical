package com.energeticecho.cubical;

import com.androidapps.framework.GameObject;

public class Diamond extends GameObject {
	public static final float DIAMOND_WIDTH = 0.9f;
	public static final float DIAMOND_HEIGHT = 0.9f;

	public Diamond(float x, float y) {
		super(x, y, DIAMOND_WIDTH, DIAMOND_HEIGHT);
	}
}
