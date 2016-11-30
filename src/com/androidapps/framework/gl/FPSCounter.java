package com.androidapps.framework.gl;

import android.util.Log;

public class FPSCounter {
	long startTime = System.nanoTime();
	int frames = 0;
	
	public String logFrame() {
		frames++;
		if (System.nanoTime() - startTime >= 1000000000) {
			int tempFrames = frames;
			Log.d("FPSCounter", "fps: " + frames);
			frames = 0;
			startTime = System.nanoTime();
			return new String("" + tempFrames);
		} else {
			return "";
		}
	}
	
	public String returnFrame() {
		return new String("" + frames);
	}
}
