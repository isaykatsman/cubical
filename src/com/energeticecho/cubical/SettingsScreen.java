package com.energeticecho.cubical;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.androidapps.framework.Game;
import com.androidapps.framework.Input.TouchEvent;
import com.androidapps.framework.gl.Camera2D;
import com.androidapps.framework.gl.SpriteBatcher;
import com.androidapps.framework.impl.GLScreen;
import com.androidapps.framework.math.OverlapTester;
import com.androidapps.framework.math.Rectangle;
import com.androidapps.framework.math.Vector2;

public class SettingsScreen extends GLScreen {
	//create vars
	Camera2D guiCam;
	SpriteBatcher batcher;
	Vector2 touchPoint;
	Rectangle soundBounds;
	Rectangle musicBounds;
	Rectangle backBounds;
	
	public SettingsScreen(Game game) {
		super(game);
		
		//init vars
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 10);
		touchPoint = new Vector2();
		soundBounds = new Rectangle(75, 208, 64, 64);
		musicBounds = new Rectangle(182, 202, 64, 64);
		backBounds = new Rectangle(0, 0, 64, 64);
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> events = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		//scan touch events
		int len = events.size();
		for(int i = 0; i < len; i ++) {
			TouchEvent event = events.get(i);
			
			if(event.type != TouchEvent.TOUCH_UP) {
				continue;
			}
			
			//correctly navigate based on touch point
			guiCam.touchToWorld(touchPoint.set(event.x, event.y));
			if(OverlapTester.pointInRectangle(backBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new MainMenuScreen(game));
			}
			if(OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
				Settings.soundEnabled = !Settings.soundEnabled;
				Assets.playSound(Assets.click);
				Settings.save(game.getFileIO());
			}
			if(OverlapTester.pointInRectangle(musicBounds, touchPoint)) {
				Settings.musicEnabled = !Settings.musicEnabled;
				if(Settings.musicEnabled) {
					//Assets.darkSoundtrack.play();
				} else {
					//Assets.darkSoundtrack.pause();
				}
				Settings.save(game.getFileIO());
			}
		}
	}
	
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		//code beyond this point may need correction
		
		//correctly present settings screen graphically
		batcher.beginBatch(Assets.darkBackground);
		batcher.drawSprite(160, 240, 320, 480, Assets.darkBackgroundRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items1);
		batcher.drawSprite(150, 448, 288, 64, Assets.settingTextRegion);
		batcher.drawSprite(32, 32, 64, 64, Assets.leftRegion);
		batcher.drawSprite(107, 240, 64, 64, Settings.soundEnabled ? Assets.soundEnabledRegion : Assets.soundDisabledRegion);
		batcher.drawSprite(214, 235, 64, 64, Settings.musicEnabled ? Assets.musicEnabledRegion : Assets.musicDisabledRegion);
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	@Override
	public void pause() {		
	}

	@Override
	public void resume() {		
	}

	@Override
	public void dispose() {		
	} 
}
