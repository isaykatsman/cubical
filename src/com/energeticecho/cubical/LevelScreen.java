package com.energeticecho.cubical;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.energeticecho.cubical.Assets;
import com.energeticecho.cubical.World;
import com.energeticecho.cubical.WorldRenderer;
import com.energeticecho.cubical.World.WorldListener;
import com.androidapps.framework.Game;
import com.androidapps.framework.Input.TouchEvent;
import com.androidapps.framework.gl.Camera2D;
import com.androidapps.framework.gl.FPSCounter;
import com.androidapps.framework.gl.SpriteBatcher;
import com.androidapps.framework.impl.GLScreen;
import com.androidapps.framework.math.Rectangle;
import com.androidapps.framework.math.Vector2;

public class LevelScreen extends GLScreen {
	//TODO: FINISH CLASS
	//CURRENTLY UNDER CONSTRUCTION
	
	//constants
	static final int GAME_RUNNING = 0;
	static final int GAME_PAUSED = 1;
	static final int GAME_OVER = 2;
	
	//global vars
	int state;
	Camera2D guiCam;
	Vector2 touchPoint;
	SpriteBatcher batcher;
	World world;
	WorldListener worldListener;
	WorldRenderer renderer;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	Rectangle leftBounds;
	Rectangle rightBounds;
	Rectangle shotBounds;
	
	//progress constants
	int totalSilver;
	int totalGold;
	int totalDiamond;
	
	//check-back vars
	int lastSilverComplete;
	int lastGoldComplete;
	int lastDiamondComplete;
	int cash;

	int lastLives;
	int lastWaves;
	
	//progress strings
	String progressSilver;
	String progressGold;
	String progressDiamond;
	String cashString;
	
	//next-level reached
	static int nextLevel = 0;
	
	FPSCounter fpsCounter;
	
	float accelX = 0;
	
	//helping var
	int lvl = 0;
	
	public LevelScreen(Game game, int levelPack, int levelNumber) {
		super(game);
		
		//init all vars
		state = GAME_RUNNING;
		guiCam = new Camera2D(glGraphics, 320, 480);
		touchPoint = new Vector2();
		batcher = new SpriteBatcher(glGraphics, 4000);
		world = new World(game, levelPack, levelNumber);
		//TODO implement WorldListener
		//world.setWorldListener(worldListener);
		
		renderer = new WorldRenderer(glGraphics, batcher, world);
		pauseBounds = new Rectangle(480 - 64, 320 - 64, 64, 64);
		resumeBounds = new Rectangle(240 - 80, 160, 160, 32);
		quitBounds = new Rectangle(240 - 80, 160 - 32, 160, 32);
		shotBounds = new Rectangle(480 - 64, 0, 64, 64);
		leftBounds = new Rectangle(0, 0, 64, 64);
		rightBounds = new Rectangle(64, 0, 64, 64);
		
		lastSilverComplete = 0;
		lastGoldComplete = 0;
		lastDiamondComplete = 0;
		cash = 0;
		
		totalSilver = world.silverBlocks.size();
		totalGold = world.goldBlocks.size();
		totalDiamond = world.diamondBlocks.size();
		
		progressSilver = "S: " + lastSilverComplete + "/" + totalSilver;
		progressGold = "G: " + lastGoldComplete + "/" + totalGold; 
		progressDiamond = "D: " + lastDiamondComplete + "/" + totalDiamond;
		cashString = "$" + cash;
		
		fpsCounter = new FPSCounter();
	}

	@Override
	public void update(float deltaTime) {
		
		//next level mechanic
		if(LevelScreen.nextLevel == 1) {
			LevelScreen.nextLevel = 0;
			lvl = world.levelNumberG + 1;
			Log.d("LevelScreen1", "" + lvl);
			game.setScreen(new darknessLevelSelectScreen(game, lvl));
		}
		
		//TODO: ADD ALL STATES
		switch(state) {
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		}
	}
	
	private void updateRunning(float deltaTime) {
		
		//regulate x velocity
		accelX = game.getInput().getAccelX();
		if(accelX > 4) {
			accelX = 4;
		} else if(accelX < -4) {
			accelX = -4;
		}
		
		//update all progress strings
		if(lastSilverComplete != world.silverComplete) {
			lastSilverComplete = world.silverComplete;
			progressSilver = "S: " + lastSilverComplete + "/" + totalSilver;
		}
		
		if(lastGoldComplete != world.goldComplete) {
			lastGoldComplete = world.goldComplete;
			progressGold = "G: " + lastGoldComplete + "/" + totalGold;
		}
		
		if(lastDiamondComplete != world.diamondComplete) {
			lastDiamondComplete = world.diamondComplete;
			progressDiamond = "D: " + lastDiamondComplete + "/" + totalDiamond;
		}
		
		if(cash != world.cash) {
			cash = world.cash;
			cashString = "$" + cash;
		}
		
		//update world
		world.update(deltaTime, accelX, game.getInput().isTouchDown(0));
	}

	@Override
	public void present(float deltaTime) {
		//TODO: PRESENT ALL PAUSE GRAPHICS
		
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		renderer.render(deltaTime);
		
		guiCam.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		batcher.beginBatch(Assets.items2);
		switch(state) {
		case GAME_RUNNING:
			presentRunning();
			break;
		}
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
		fpsCounter.logFrame();
	}
	
	private void presentRunning() {
		Assets.font.drawText(batcher, progressSilver, 10, 480 - 20);
		Assets.font.drawText(batcher, progressGold, 10, 480 - 20 - 20);
		Assets.font.drawText(batcher, progressDiamond, 10, 480 - 20 - 20 - 20);
		Assets.font.drawText(batcher, cashString, 320 - 150, 480 - 20);
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
