package com.energeticecho.cubical;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import com.androidapps.framework.gl.AmbientLight;
import com.androidapps.framework.gl.Camera2D;
import com.androidapps.framework.gl.SpriteBatcher;
import com.androidapps.framework.impl.GLGraphics;

public class WorldRenderer {
	//define constants + vars
	static final float FRUSTRUM_WIDTH = 10;
	static final float FRUSTRUM_HEIGHT = 15;
	GLGraphics glGraphics;
	World world;
	Camera2D cam;
	SpriteBatcher batcher;
	
	//fade vars
	AmbientLight ambientLight;
	float f = 5;
	float prevf = f;
	boolean fadeBackIn = false;
	
	//camera animation vars
	float camXChange = 1;
	float simCamX;
	boolean animOutFinished = false;
	boolean animInFinished = false;
	
	//zoom vars
	float camZoom = 1;
	float camZoomChange = 1;
	boolean zoomOutFinished = false;
	boolean zoomInFinished = false;
	
	//init vars
	public WorldRenderer(GLGraphics glGraphics, SpriteBatcher batcher, World world) {
		this.glGraphics = glGraphics;
		this.world = world;
		this.cam = new Camera2D(glGraphics, FRUSTRUM_WIDTH, FRUSTRUM_HEIGHT);
		this.batcher = batcher;
		
		simCamX = (world.leftXLimit + 5);
		
		ambientLight = new AmbientLight();
		ambientLight.setColor(f, f, f, 1);
	}
	
	//render + camera animations + delegations to other private classes
	public void render(float deltaTime) {
		
		cameraAnimations(deltaTime);
		
		cam.position.y = world.player.position.y;
		cam.position.x = simCamX;
		cam.zoom = camZoom;

		if(cam.position.y < 8.5) {
			cam.position.y = 8.5f;
		}
		
		if(simCamX < world.minXLimit) {
			simCamX = world.minXLimit;
		}
		
		/*if(camZoom < 1) {
			camZoom = 1;
		} else if (camZoom > 2){
			camZoom = 2;
		}*/
		
		
		cam.setViewportAndMatrices();
		renderBackground();
		renderObjects(deltaTime);
	}
	
	//perform all camera animation checks + transitions
	private void cameraAnimations(float deltaTime) {
		//back-in animations + inside of main world
		if(world.player.position.x > world.leftXLimit && world.player.position.x < world.rightXLimit) {
			//back-in from left
			if(world.player.position.x < (world.leftXLimit) + 5 && simCamX <= (world.leftXLimit) + 5 && animInFinished == false) {
				camXChange += 12 * deltaTime;
				simCamX = (world.leftXLimit) + camXChange;
			//back in from right
			} else if(world.player.position.x > (world.leftXLimit) + 5 && simCamX >= (world.leftXLimit) + 5 && animInFinished == false) {
				camXChange -= 12 * deltaTime;
				simCamX = (world.rightXLimit) + camXChange;
			} else {
				simCamX = (world.leftXLimit) + 5;
				camXChange = 0;
				
				//handle camera move bools
				animInFinished = true;
				animOutFinished = false; //either place inside or outside of else
			}
		//back-out animations + outside of world
			
		//back-out from left side
		} else if (world.player.position.x < world.leftXLimit && simCamX > world.player.position.x && animOutFinished == false) {
			camXChange -= 12 * deltaTime;
			simCamX = (world.leftXLimit) + 5 + camXChange;
		//back-out from right side
		} else if (world.player.position.x > world.rightXLimit && simCamX < world.player.position.x && animOutFinished == false) {
			camXChange += 12 * deltaTime;
			simCamX = (world.rightXLimit) - 5 + camXChange;
		} else {
			simCamX = world.player.position.x;
			camXChange = 0; //set cam change value to 0 so that position stops changing
			
			//handle camera move bools
			animOutFinished = true;
			animInFinished = false;
		}
		
		//ZOOM ANIMATIONS
		//
		//
		//
		//back-in animations + inside of main world
		if(world.player.position.x > (world.leftXLimit) && world.player.position.x < world.rightXLimit) {
			//back-in from left
			if(world.player.position.x < (world.leftXLimit + 5) && zoomInFinished == false && camZoom >= 1) {
				//handle zoom
				camZoom -= 2 * deltaTime;
				//back in from right
			} else if(world.player.position.x > (world.leftXLimit + 5) && zoomInFinished == false && camZoom >= 1) {
				camZoom -= 2 * deltaTime;
			} else {
				camZoom = 1; //set final zoom value
				camZoomChange = 0;
				
				//handle camera zoom bools
				zoomInFinished = true;
				zoomOutFinished = false;
			}
			//back-out animations + outside of world
				
			//back-out from left side
			} else if (world.player.position.x < world.leftXLimit && zoomOutFinished == false && camZoom <= 2) {
				camZoom += 2 * deltaTime;
			//back-out from right side
			} else if (world.player.position.x > world.rightXLimit && zoomOutFinished == false && camZoom <= 2) {
				camZoom += 2 * deltaTime;
			} else {	
				camZoom = 2; //set final zoom value
				camZoomChange = 0; //set zoom value to 0 so that zoom stops changing
				
				//handle camera zoom bools
				zoomOutFinished = true;
				zoomInFinished = false;
			}
	}
	
	//render background
	public void renderBackground() {
		batcher.beginBatch(Assets.darkBackground);
		batcher.drawSprite(cam.position.x, cam.position.y, FRUSTRUM_WIDTH * camZoom, FRUSTRUM_HEIGHT * camZoom, Assets.darkBackgroundRegion);
		batcher.endBatch();
	}
	
	//render all objects delegates to other private classes
	public void renderObjects(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		gl.glEnable(GL10.GL_LIGHTING);
		ambientLight.enable(gl);
		
		fadeEffectCheck(deltaTime);
		
		batcher.beginBatch(Assets.items3);
		renderTiles();
		renderMinerals();
		renderSpikes();
		renderCheckpoints();
		renderEFlags();
		renderPlayer(); //always rendered last, over everything
		batcher.endBatch();
		
		gl.glDisable(GL10.GL_BLEND);
	}
	
	//performs fade effect checks, and fade effect itself
	private void fadeEffectCheck(float deltaTime) {
		if(prevf != f) {
			ambientLight.setColor(f, f, f, 1);
			prevf = f;
		}
		
		if(world.playerAlive == false) {
			if(f > 0) {
				f -= deltaTime * 10;
			} 
		}
		
		if(f <= 0) {
			world.playerAlive = true;
			world.player.velocity.x = 0;
			world.player.velocity.y = 0;
			world.player.position.x = world.respawnPoint.x;
			world.player.position.y = world.respawnPoint.y;
			fadeBackIn = true;
		}
		
		if(fadeBackIn) {
			if(f <= 5) {
				f += deltaTime * 10;
			} else {
				fadeBackIn = false;
			}
		}
	}
	
	//renders player
	private void renderPlayer() {
		batcher.drawSprite(world.player.position.x, world.player.position.y, 1, 1, Assets.playerRegion);
		batcher.drawSprite(world.player.position.x - 0.2f, world.player.position.y + 0.2f, 0.2f, 0.2f, Assets.playerEyeRegion);
	}
	
	//renders tiles
	private void renderTiles() {
		int len = world.regTiles.size();
		for(int i = 0; i < len; i ++) {
			RegTile regTile = world.regTiles.get(i);
			batcher.drawSprite(regTile.position.x, regTile.position.y, 1, 1, Assets.darkTile);
		}
	}
	
	//renders all minerals
	private void renderMinerals() {
		//diamonds
		int len = world.diamondBlocks.size();
		for(int i = 0; i < len; i ++) {
			Diamond diamond = world.diamondBlocks.get(i);
			batcher.drawSprite(diamond.position.x, diamond.position.y, 1, 1, Assets.diamond);
		}

		//gold
		len = world.goldBlocks.size();
		for(int i = 0; i < len; i ++) {
			Gold gold = world.goldBlocks.get(i);
			batcher.drawSprite(gold.position.x, gold.position.y, 1, 1, Assets.gold);
		}
		
		//silver
		len = world.silverBlocks.size();
		for(int i = 0; i < len; i ++) {
			Silver silver = world.silverBlocks.get(i);
			batcher.drawSprite(silver.position.x, silver.position.y, 1, 1, Assets.silver);
		}
	}
	
	//renders spikes
	private void renderSpikes() {
		//up
		int len = world.upSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike upSpike = world.upSpikes.get(i);
			batcher.drawSprite(upSpike.position.x, upSpike.position.y, 1, 1, Assets.vertSpike);
		}
		
		//down
		len = world.downSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike downSpike = world.downSpikes.get(i);
			batcher.drawSprite(downSpike.position.x, downSpike.position.y, 1, -1, Assets.vertSpike);
		}
		
		//right
		len = world.rightSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike rightSpike = world.rightSpikes.get(i);
			batcher.drawSprite(rightSpike.position.x, rightSpike.position.y, 1, 1, Assets.horizSpike);
		}
		
		//left
		len = world.leftSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike leftSpike = world.leftSpikes.get(i);
			batcher.drawSprite(leftSpike.position.x, leftSpike.position.y, -1, 1, Assets.horizSpike);
		}
	}
	
	//render checkpoints
	private void renderCheckpoints() {
		int len = world.checkpoints.size();
		for(int i = 0; i < len; i ++) { 
			Checkpoint checkpoint = world.checkpoints.get(i);
			batcher.drawSprite(checkpoint.position.x, checkpoint.position.y + 0.5f, 1, 2, checkpoint.active ? Assets.checkPointOnRegion : Assets.checkPointOffRegion);
		}
	}
	
	//render ending flag(s), more than one flag for secret exits
	private void renderEFlags() {
		int len = world.eflags.size();
		for(int i = 0; i < len; i ++) {
			EndingFlag eflag = world.eflags.get(i);
			batcher.drawSprite(eflag.position.x, eflag.position.y, 6, 1, Assets.puddle);
		}
	}
}
