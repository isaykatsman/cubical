package com.androidapps.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import com.androidapps.framework.impl.GLGraphics;
import com.androidapps.framework.math.Vector2;

public class Camera2D {
	public final Vector2 position;
	public float zoom;
	public final float frustrumWidth;
	public final float frustrumHeight;
	final GLGraphics glGraphics;
	
	public Camera2D(GLGraphics glGraphics, float frustrumWidth, float frustrumHeight) {
		this.glGraphics = glGraphics;
		this.frustrumWidth = frustrumWidth;
		this.frustrumHeight = frustrumHeight;
		this.position = new Vector2(frustrumWidth / 2, frustrumHeight / 2);
		this.zoom = 1.0f;
	}
	
	public void setViewportAndMatrices() {
		GL10 gl = glGraphics.getGL();
		gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(position.x - frustrumWidth * zoom / 2, position.x + frustrumWidth * zoom / 2, position.y - frustrumHeight * zoom / 2, position.y + frustrumHeight * zoom / 2, 1, -1);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public void touchToWorld(Vector2 touch) {
		touch.x = (touch.x / (float) glGraphics.getWidth()) * frustrumWidth * zoom /*+ position.x - frustrumWidth * zoom / 2*/;
		touch.y = (1 - touch.y / (float) glGraphics.getHeight()) * frustrumHeight * zoom /*+ position.y - frustrumHeight * zoom / 2*/;
		touch.add(position).sub(frustrumWidth * zoom / 2, frustrumHeight * zoom / 2);
	}
}
