package com.energeticecho.cubical;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.androidapps.framework.Game;
import com.androidapps.framework.Input.TouchEvent;
import com.androidapps.framework.gl.Camera2D;
import com.androidapps.framework.gl.SpriteBatcher;
import com.androidapps.framework.impl.GLScreen;
import com.androidapps.framework.math.OverlapTester;
import com.androidapps.framework.math.Rectangle;
import com.androidapps.framework.math.Vector2;

public class darknessLevelSelectScreen extends GLScreen {
	//define vars
	Camera2D guiCam;
	SpriteBatcher batcher;
	Vector2 touchPoint;
	Rectangle level1;
	Rectangle level2;
	Rectangle level3;
	Rectangle level4;
	Rectangle level5;
	Rectangle level6;
	Rectangle level7;
	Rectangle level8;
	Rectangle level9;
	Rectangle level10;
	Rectangle backBounds;
	
	//used for navigation to another level
	int levelRequest;
	
	public darknessLevelSelectScreen(Game game, int levelR) {
		super(game);
		
		//init vars
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 10);
		touchPoint = new Vector2();
		level1 = new Rectangle(0, 240, 64, 64);
		level2 = new Rectangle(64, 240, 64, 64);
		level3 = new Rectangle(128, 240, 64, 64);
		level4 = new Rectangle(192, 240, 64, 64);
		level5 = new Rectangle(256, 240, 64, 64);
		level6 = new Rectangle(0, 176, 64, 64);
		level7 = new Rectangle(64, 176, 64, 64);
		level8 = new Rectangle(128, 176, 64, 64);
		level9 = new Rectangle(192, 176, 64, 64);
		level10 = new Rectangle(256, 176, 64, 64);
		backBounds = new Rectangle(0, 176, 64, 64);
		
		this.levelRequest = levelR;
	}
	
	@Override
	public synchronized void update(float deltaTime) {
		if(levelRequest == 0) {
			List<TouchEvent> events = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();
			
			//scanning touch events
			int len = events.size();
			for(int i = 0; i < len; i ++) {
				TouchEvent event = events.get(i);
				
				if(event.type != TouchEvent.TOUCH_UP) {
					continue;
				}
				
				//properly select level based on touch point
				guiCam.touchToWorld(touchPoint.set(event.x, event.y));
				if(OverlapTester.pointInRectangle(level1, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 1)); //game, levelpack, level #
				}
				if(OverlapTester.pointInRectangle(level2, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 2));
				}
				if(OverlapTester.pointInRectangle(level3, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 3));
				}
				if(OverlapTester.pointInRectangle(level4, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 4));
				}
				if(OverlapTester.pointInRectangle(level5, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 5));
				}
				if(OverlapTester.pointInRectangle(level6, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 6));
				}
				if(OverlapTester.pointInRectangle(level7, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 7));
				}
				if(OverlapTester.pointInRectangle(level8, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 8));
				}
				if(OverlapTester.pointInRectangle(level9, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 9));
				}
				if(OverlapTester.pointInRectangle(level10, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new LevelScreen(game, 1, 10));
				}
				if(OverlapTester.pointInRectangle(backBounds, touchPoint)) {
					Assets.playSound(Assets.click);
					game.setScreen(new levelPackSelectScreen(game));
				}
			}
		} else {
			Log.d("darkLvlSelect", "Requested Level: " + levelRequest);
			game.setScreen(new LevelScreen(game, 1, levelRequest));
			//levelRequest = 0;
		}
	}
	
	@Override
	public void present(float deltaTime) {
	    if(levelRequest == 0) {
			GL10 gl = glGraphics.getGL();
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			guiCam.setViewportAndMatrices();
			
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			//code beyond this point may need correction
			
			//set up level select screen

			batcher.beginBatch(Assets.darkBackground);
			batcher.drawSprite(160, 240, 320, 480, Assets.darkBackgroundRegion);
			batcher.endBatch();
			
			gl.glEnable(GL10.GL_BLEND);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			
			batcher.beginBatch(Assets.items1);
			batcher.drawSprite(160, 448, 288, 64, Assets.darknessTextRegion);
			batcher.drawSprite(32, 32, 64, 64, Assets.leftRegion);
			batcher.endBatch();
			
			batcher.beginBatch(Assets.items3);
			batcher.drawSprite(160, 240, 320, 128, Assets.levelSelectRegion);
			batcher.endBatch();
			
			gl.glDisable(GL10.GL_BLEND);
			gl.glDisable(GL10.GL_TEXTURE_2D);
		}
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