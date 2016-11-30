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

public class MainMenuScreen extends GLScreen {
	//create vars
	Camera2D guiCam;
	SpriteBatcher batcher;
	Vector2 touchPoint;
	Rectangle playBounds;
	Rectangle settingsBounds;
	Rectangle optionsBounds;
	
	public MainMenuScreen(Game game) {
		super(game);
		
		//init vars
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 10);
		touchPoint = new Vector2();
		playBounds = new Rectangle(0, 304, 320, 64);
		settingsBounds = new Rectangle(0, 176, 320, 128);
		optionsBounds = new Rectangle(0, 112, 320, 64);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> events = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		//scan for touch events
		int len = events.size();
		for(int i = 0; i < len; i ++) {
			TouchEvent event = events.get(i);
			
			if(event.type != TouchEvent.TOUCH_UP) {
				continue;
			}
			
			//do proper action based on touch point
			guiCam.touchToWorld(touchPoint.set(event.x, event.y));
			if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new levelPackSelectScreen(game));
			}
			if(OverlapTester.pointInRectangle(settingsBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new SettingsScreen(game));
			}
			if(OverlapTester.pointInRectangle(optionsBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new OptionsScreen(game));
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		//this part will need some corrections
		
		//properly set up graphics for main menu screen
		batcher.beginBatch(Assets.darkBackground);
		batcher.drawSprite(160, 240, 320, 480, Assets.darkBackgroundRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items1);
		batcher.drawSprite(160, 448, 288, 64, Assets.logoRegion);
		batcher.drawSprite(130, 240, 300, 256, Assets.mainMenuRegion);
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
