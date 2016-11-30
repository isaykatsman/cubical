package com.energeticecho.cubical;

import com.androidapps.framework.GameObject;

public class Checkpoint extends GameObject {
	public static final float CHECKPOINT_WIDTH = 1;
	public static final float CHECKPOINT_HEIGHT = 2;
	
	public boolean active = false;
	
	public Checkpoint(float x, float y) {
		super(x, y, CHECKPOINT_WIDTH, CHECKPOINT_HEIGHT);
	}
}
