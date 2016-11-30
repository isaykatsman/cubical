package com.androidapps.framework.math;

public class Triangle {
	public final Vector2 center;
	public float width, height;
	
	public Triangle(float x, float y, float width, float height) {
		center = new Vector2(x, y);
		this.width = width;
		this.height = height;
	}
}
