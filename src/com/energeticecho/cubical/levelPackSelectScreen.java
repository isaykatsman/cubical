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

public class levelPackSelectScreen extends GLScreen {
	//defining vars
	Camera2D guiCam;
	SpriteBatcher batcher;
	Vector2 touchPoint;
	Rectangle darknessBounds;
	Rectangle midBounds;
	Rectangle lightBounds;
	Rectangle endlessBounds;
	Rectangle backBounds;
	
	public levelPackSelectScreen(Game game) {
		super(game);
		
		//init vars
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 10);
		touchPoint = new Vector2();
		darknessBounds = new Rectangle(0, 288, 320, 128);
		midBounds = new Rectangle(0, 224, 320, 128);
		lightBounds = new Rectangle(0, 160, 320, 128);
		endlessBounds = new Rectangle(0, 96, 320, 64);
		backBounds = new Rectangle(0, 0, 64, 64);
	}
	
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> events = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		//scanning touch events
		int len = events.size();
		for(int i = 0; i < len; i ++) {
			TouchEvent event = events.get(i);
			
			if(event.type != TouchEvent.TOUCH_UP) {
				continue;
			}
			
			//choosing correct level pack / game mode
			guiCam.touchToWorld(touchPoint.set(event.x, event.y));
			if(OverlapTester.pointInRectangle(darknessBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new darknessLevelSelectScreen(game, 0));
			}
			if(OverlapTester.pointInRectangle(midBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new midLevelSelectScreen(game));
			}
			if(OverlapTester.pointInRectangle(lightBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new lightLevelSelectScreen(game));
			}
			if(OverlapTester.pointInRectangle(endlessBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				//game.setScreen(new endlessScreen(game));
			}
			if(OverlapTester.pointInRectangle(backBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new MainMenuScreen(game));
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
		
		//rendering level pack select screen
		batcher.beginBatch(Assets.darkBackground);
		batcher.drawSprite(160, 240, 320, 480, Assets.darkBackgroundRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		//please correct this area when possible
		batcher.beginBatch(Assets.items1);
		batcher.drawSprite(190, 240, 320, 400, Assets.levelPackSelectRegion);
		batcher.drawSprite(32, 32, 64, 64, Assets.leftRegion);
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
