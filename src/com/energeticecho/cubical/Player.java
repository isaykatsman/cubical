package com.energeticecho.cubical;

import com.androidapps.framework.DynamicGameObject;

public class Player extends DynamicGameObject {
	//TODO: Finish adding player states
	public static final int PLAYER_HIT = 0;
	
	//init constants
	public static float PLAYER_MOVE_VELOCITY = 17;
	public static final float PLAYER_FLY_VELOCITY = 0.5f;
	public static final float PLAYER_WIDTH = 1;
	public static final float PLAYER_HEIGHT = 1;
	
	int state;

	public Player(float x, float y) {
		super(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
	}
	
	//update player with gravity, update bounds
	public void update(float deltaTime) {
		velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
		position.add(velocity.x * deltaTime, velocity.y * deltaTime);
		bounds.lowerLeft.set(position).sub(PLAYER_WIDTH / 2, PLAYER_HEIGHT / 2);
	}
	
	//TODO: Finish handling player hits
	public void hitTile() {
		//nothing to do here
	}
	
	public void hitSpike() {
		velocity.set(0, 0);
		state = PLAYER_HIT;
	}
	
	public void hitMineral() {
		//nothing to do here
	}

}
