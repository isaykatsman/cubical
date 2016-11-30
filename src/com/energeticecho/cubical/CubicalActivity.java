package com.energeticecho.cubical;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.androidapps.framework.Screen;
import com.androidapps.framework.impl.GLGame;

public class CubicalActivity extends GLGame {
	boolean firstTimeCreate = true;
	
	//returns first visible screen
	@Override
	public Screen getStartScreen() {
		return new MainMenuScreen(this);
	}
	
	//inits application
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		super.onSurfaceCreated(gl, config);
		if(firstTimeCreate) {
			Settings.load(getFileIO());
			Assets.load(this);
			firstTimeCreate = false;
		} else {
			Assets.reload();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if(Settings.musicEnabled) {
			//Assets.darkSoundtrack.pause();
		}
	}

}