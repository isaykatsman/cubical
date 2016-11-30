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

public class OptionsScreen extends GLScreen {
	//create vars
	Camera2D guiCam;
	SpriteBatcher batcher;
	Vector2 touchPoint;
	Rectangle creditsBounds;
	Rectangle backBounds;
	
	public OptionsScreen(Game game) {
		super(game);
		
		//init vars
		guiCam = new Camera2D(glGraphics, 320, 480);
		batcher = new SpriteBatcher(glGraphics, 10);
		touchPoint = new Vector2();
		creditsBounds = new Rectangle(128, 32, 224, 64); //correct later
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
			
			//perform correct action based on touchpoint
			guiCam.touchToWorld(touchPoint.set(event.x, event.y));
			if(OverlapTester.pointInRectangle(creditsBounds, touchPoint)) {
				Assets.playSound(Assets.click);
				//game.setScreen(new CreditsScreen(game));
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
		
		//correctly draw out graphics for options screen
		batcher.beginBatch(Assets.darkBackground);
		batcher.drawSprite(160, 240, 320, 480, Assets.darkBackgroundRegion);
		batcher.endBatch();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		batcher.beginBatch(Assets.items1);
		batcher.drawSprite(120, 420, 288, 64, Assets.optionTextRegion);
		batcher.drawSprite(32, 32, 64, 64, Assets.leftRegion);
		batcher.drawSprite(160, 0, 0, 0, Assets.creditsButtonRegion); //fix later
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
