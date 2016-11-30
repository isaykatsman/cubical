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

public class midLevelSelectScreen extends GLScreen {
	//create vars
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
	
	public midLevelSelectScreen(Game game) {
		super(game);
		
		//init vars
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 10);
		touchPoint = new Vector2();
		level1 = new Rectangle(80, 64, 64, 64);
		level2 = new Rectangle(144, 64, 64, 64);
		level3 = new Rectangle(208, 64, 64, 64);
		level4 = new Rectangle(272, 64, 64, 64);
		level5 = new Rectangle(336, 64, 64, 64);
		level6 = new Rectangle(80, 0, 64, 64);
		level7 = new Rectangle(144, 0, 64, 64);
		level8 = new Rectangle(208, 0, 64, 64);
		level9 = new Rectangle(272, 0, 64, 64);
		level10 = new Rectangle(336, 0, 64, 64);
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
			
			//correctly chose dusk level based on touch point
			guiCam.touchToWorld(touchPoint.set(event.x, event.y));
			if(OverlapTester.pointInRectangle(level1, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 1)); //game, levelpack, level #
			}
			if(OverlapTester.pointInRectangle(level2, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 2));
			}
			if(OverlapTester.pointInRectangle(level3, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 3));
			}
			if(OverlapTester.pointInRectangle(level4, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 4));
			}
			if(OverlapTester.pointInRectangle(level5, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 5));
			}
			if(OverlapTester.pointInRectangle(level6, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 6));
			}
			if(OverlapTester.pointInRectangle(level7, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 7));
			}
			if(OverlapTester.pointInRectangle(level8, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 8));
			}
			if(OverlapTester.pointInRectangle(level9, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 9));
			}
			if(OverlapTester.pointInRectangle(level10, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new LevelScreen(game, 2, 10));
			}
			if(OverlapTester.pointInRectangle(backBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				game.setScreen(new levelPackSelectScreen(game));
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
		
		//draw graphics for level select screen
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
