package com.energeticecho.cubical;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.androidapps.framework.Game;
import com.androidapps.framework.impl.GLGame;
import com.androidapps.framework.math.OverlapTester;
import com.androidapps.framework.math.Vector2;
import com.energeticecho.cubical.Assets;

public class World {
	
	//TODO: implement world listener
	public interface WorldListener {
		public void pickUpMineral();
		
		public void hit();
	}
	
	//levels are parsed with the following codes:
	// T - Reg tile
	// U - up Spike
	// V - down spike
	// R - right Spike
	// J - left spike
	// D - Diamond
	// S - Silver
	// G - Gold
	// P - Player spawn point
	// C - Checkpoint
	// L - cam left limit
	// l - cam right limit
	// M - cam minimum
	// F - ending flag
	// # - level number
	
	//some constants
	public boolean isXColliding = false;
	public boolean isYColliding = false;
	public boolean yCol = false;
	public boolean xColliding = false;
	
	//Experimental feature necessary for fade effect
	public boolean playerAlive = true;
	
	//re-spawn point
	public Vector2 respawnPoint;
	
	//constants
	public static float WORLD_WIDTH = 47;
	float tempWidth = 0;
	public static float WORLD_HEIGHT = 447;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -10);
	
	//vars
	int silverComplete;
	int goldComplete;
	int diamondComplete;
	int cash = 0;
	
	//all on screen objects
	List<Diamond> diamondBlocks;
	List<Silver> silverBlocks;
	List<Gold> goldBlocks;
	List<RegTile> regTiles;
	List<Spike> upSpikes;
	List<Spike> downSpikes;
	List<Spike> rightSpikes;
	List<Spike> leftSpikes;
	List<Checkpoint> checkpoints;
	List<EndingFlag> eflags;
	Player player;
	
	//main world camera left/right limits
	float leftXLimit;
	float rightXLimit;
	
	//min world limit
	float minXLimit;
	
	//correct collisions
	float correctionFactor = 0.2f;
	
	//level world/number
	int worldNumberG;//*****NOTE: Not initialized, must exist in level map for program to operate successfully
	int levelNumberG;
	
	//init all objects generate level
	public World(Game game, int levelPack, int levelNumber) {
		diamondBlocks = new ArrayList<Diamond>();
		silverBlocks = new ArrayList<Silver>();
		goldBlocks = new ArrayList<Gold>();
		regTiles = new ArrayList<RegTile>();
		upSpikes = new ArrayList<Spike>();
		downSpikes = new ArrayList<Spike>();
		rightSpikes = new ArrayList<Spike>();
		leftSpikes = new ArrayList<Spike>();
		checkpoints = new ArrayList<Checkpoint>();
		eflags = new ArrayList<EndingFlag>();
		
		//init player missing
		
		generateLevel(game, levelPack, levelNumber);
	}
	
	//generate level by parsing file
	public synchronized void generateLevel(Game game, int levelPack, int levelNumber) {
		InputStream in = null;
		String levelCode = levelPack + "-" + levelNumber;
		try {
			in = game.getFileIO().readAsset("level" + levelCode + ".txt");
			List<String> layers = readLines(in);
			//set world height
			WORLD_HEIGHT = layers.size();
			
			for(float i = 0, y = 0.5f + layers.size() - 1; i < layers.size(); i++, y -= 1) {
				String layer = layers.get((int)i);
				
				for(float j = 0, x = 0.5f; j < layer.length(); j++, x += 1) {
					if(x > tempWidth) {
						tempWidth = x + 0.5f;
					}
					char tile = layer.charAt((int)j);
					
					if(tile == 'T') {
						RegTile regTile = new RegTile(x, y);
						regTiles.add(regTile);
					} else if(tile == 'L') {
						RegTile regTile = new RegTile(x, y);
						regTiles.add(regTile);
						leftXLimit = x - 0.5f;
					} else if(tile == 'l') {
						RegTile regTile = new RegTile(x, y);
						regTiles.add(regTile);
						rightXLimit = x + 0.5f;
					} else if(tile == 'M') {
						RegTile regTile = new RegTile(x, y);
						regTiles.add(regTile);
						minXLimit = x - 0.5f;
					} else if(tile == 'U') {
						Spike upSpike = new Spike(x, y);
						upSpikes.add(upSpike);
					} else if(tile == 'V') {
						Spike downSpike = new Spike(x, y);
						downSpikes.add(downSpike);
					} else if(tile == 'R') {
						Spike rightSpike = new Spike(x, y);
						rightSpikes.add(rightSpike);
					} else if(tile == 'J') {
						Spike leftSpike = new Spike(x, y);
						leftSpikes.add(leftSpike);
					} else if(tile == 'D') {
						Diamond diamond = new Diamond(x, y);
						diamondBlocks.add(diamond);
					} else if(tile == 'G') {
						Gold gold = new Gold(x, y);
						goldBlocks.add(gold);
					} else if(tile == 'S') {
						Silver silver = new Silver(x, y);
						silverBlocks.add(silver);
					} else if(tile == 'P') {
						player = new Player(x, y);
						player.velocity.y = 1;
						respawnPoint = new Vector2(x, y);
					} else if(tile == 'C') {
						Checkpoint checkpoint = new Checkpoint(x, y);
						checkpoints.add(checkpoint);
					} else if(tile == 'F') {
						EndingFlag eflag = new EndingFlag(x, y);
						eflags.add(eflag);
					} else if(tile == '#') {
						worldNumberG = (int)Math.ceil(y);
						levelNumberG = (int)Math.ceil(x);
					}
				}
			}	
			
			//set max found width across map to permanent width for current world
			WORLD_WIDTH = tempWidth;
		} catch (Exception e) {
			
		}
	}
	
	//takes file and turns it into array of strings, each string being a line of the text in the file
	static List<String> readLines(InputStream in) throws IOException {
		List<String> lines = new ArrayList<String>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while((line = reader.readLine()) != null) {
			lines.add(line);
		}
		return lines;
	}
	
	//update player + world
	public void update(float deltaTime, float accelX, boolean touchDown) {
		if(playerAlive == true) {
			updatePlayer(deltaTime, accelX, touchDown);
			checkCollisions(deltaTime);
		}
	}
	
	//update player + accurately display physics on screen
	private synchronized void updatePlayer(float deltaTime, float accelX, boolean touchDown) {
			
			try {
			player.velocity.x = (float) (-accelX / 9.8 * Player.PLAYER_MOVE_VELOCITY);
			} catch(Exception e) {}

			
			if(touchDown) {
				player.velocity.y += Player.PLAYER_FLY_VELOCITY;
			}
			
			try {
				if(player.velocity.y > 7) {
					player.velocity.y = 7;
				} else if(player.velocity.y < -7) {
					player.velocity.y = -7;
				}
			
				player.update(deltaTime);
			} catch(Exception e) {}
	}
	
	//master collision class delegates collisions to private methods
	private void checkCollisions(float deltaTime) {
		checkRegTileCollisions(deltaTime);
		checkSpikeCollisions();
		checkMineralCollisions();
		checkCheckpointCollisions();
		checkEFlagCollisions();
	}
	
	//EXPERIMENTAL STAGE checks collisions on tiles
	private void checkRegTileCollisions(float deltaTime) {
		int len = regTiles.size();
		for(int i = 0; i < len; i ++) {
			RegTile tile = regTiles.get(i);
			
			//overlap between player and tile
			
			//experiment with these values to get close to perfect collisions *************************
			try {
				if(OverlapTester.overlapRectangles(player.bounds, tile.bounds)) {
					
					//check y - collisions
					if(player.position.y > tile.position.y + (tile.bounds.height / 2) && player.position.x - (player.bounds.width / 2) + correctionFactor < tile.position.x + (tile.bounds.width / 2) && player.position.x + (player.bounds.width / 2) - correctionFactor > tile.position.x - (tile.bounds.width / 2)) {
						player.velocity.y = 0;
						player.position.y = tile.position.y + 1;
						if(player.velocity.x > 3) {
							player.velocity.x -= 0.2f; //prev. optimal was .3
						}
					} else if(player.position.y < tile.position.y - (tile.bounds.height / 2) && player.position.x - (player.bounds.width / 2) + correctionFactor < tile.position.x + (tile.bounds.width / 2) && player.position.x + (player.bounds.width / 2) - correctionFactor > tile.position.x - (tile.bounds.width / 2)) {
						player.velocity.y = 0;
						player.position.y = tile.position.y - 1;
						if(player.velocity.x > 3) {
							player.velocity.x -= 0.2f;
						}
					}
					
					//check x - collision
					if(player.position.x > tile.position.x + (tile.bounds.width / 2) + correctionFactor && player.position.y - (player.bounds.height / 2) + correctionFactor < tile.position.y + (tile.bounds.height / 2) && player.position.y + (player.bounds.height / 2) - correctionFactor > tile.position.y - (tile.bounds.height / 2)) {
						player.velocity.x = 0;
						xColliding = true;
						player.position.x = tile.position.x + 1; //animate possibly
						if(player.velocity.y > 0) {
							//player.velocity.y -= 0.2f;
						}
					} else if(player.position.x < tile.position.x - (tile.bounds.width / 2) - correctionFactor && player.position.y - (player.bounds.height / 2) + correctionFactor < tile.position.y + (tile.bounds.height / 2) && player.position.y + (player.bounds.height / 2) - correctionFactor > tile.position.y - (tile.bounds.height / 2)) {
						player.velocity.x = 0;
						xColliding = true;
						player.position.x = tile.position.x - 1; //animate possibly
						if(player.velocity.y > 0) {
							//player.velocity.y -= 0.2f;
						}
					}
				} 
			} catch (Exception e) {}
		}
		recheckTileCollisions(); //fix frame-skipping 
	}
	
	//make sure frame-skipping does not occur
	private void recheckTileCollisions() {
		int len = regTiles.size();
		for(int i = 0; i < len; i ++) {
			RegTile tile = regTiles.get(i);
			
			try{
				if(OverlapTester.overlapRectangles(player.bounds, tile.bounds)) {
					//check-x cols and fix
					if(player.position.x > tile.position.x - tile.bounds.width/2 && player.position.x < tile.position.x + tile.bounds.width/2 && player.position.y < tile.position.y + tile.bounds.height/2 && player.position.y > tile.position.y - tile.bounds.height/2) {
						if(player.position.x < tile.position.x) {
							player.position.x = tile.position.x - 1;
						} else if(player.position.x > tile.position.x) {
							player.position.x = tile.position.x + 1;
						}
					} 
				}
			} catch (Exception e) {}
		}
	}
	
	//checks player rect bounds with circle bounds of spikes
	private void checkSpikeCollisions() {
		
		//check collisions with up spikes
		int len = upSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike spike = upSpikes.get(i);
			
			if(OverlapTester.overlapCircleRectangle(spike.bounds, player.bounds)) {
				playerAlive = false;
			}
		}
		
		//check collisions with down spikes
		len = downSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike spike = downSpikes.get(i);
			
			if(OverlapTester.overlapCircleRectangle(spike.bounds, player.bounds)) {
				playerAlive = false;
			}
		}
		
		//check collisions with right spikes
		len = rightSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike spike = rightSpikes.get(i);
			
			if(OverlapTester.overlapCircleRectangle(spike.bounds, player.bounds)) {
				playerAlive = false;
			}
		}
		
		//check collisions with left spikes
		len = leftSpikes.size();
		for(int i = 0; i < len; i ++) {
			Spike spike = leftSpikes.get(i);
			
			if(OverlapTester.overlapCircleRectangle(spike.bounds, player.bounds)) {
				playerAlive = false;
			}
		}
	}
	
	//checks for collisions with player and three minerals
	private void checkMineralCollisions() {
		//check collisions with silver
		int len = silverBlocks.size();
		for(int i = 0; i < len; i ++) {
			Silver silver = silverBlocks.get(i);
			try {
				if(OverlapTester.overlapRectangles(silver.bounds, player.bounds)) {
					silverComplete ++;
					silverBlocks.remove(i);
					len = silverBlocks.size();
					cash += 10;
				}
			} catch (Exception e) {}
		}
		
		//check collisions with gold
		len = goldBlocks.size();
		for(int i = 0; i < len; i ++) {
			Gold gold = goldBlocks.get(i);
			
			try {
				if(OverlapTester.overlapRectangles(gold.bounds, player.bounds)) {
					goldComplete ++;
					goldBlocks.remove(i);
					len = goldBlocks.size();
					cash += 50;
				} 
			} catch (Exception e) {}
		}
		
		//check collisions with diamonds
		len = diamondBlocks.size();
		for(int i = 0; i < len; i ++) {
			Diamond diamond = diamondBlocks.get(i);
			
			try {
				if(OverlapTester.overlapRectangles(diamond.bounds, player.bounds)) {
					diamondComplete ++;
					diamondBlocks.remove(i);
					len = diamondBlocks.size();
					cash += 150;
				}
			} catch (Exception e) {}
		}
	}
	
	//check for collisions with checkpoints
	private void checkCheckpointCollisions() {
		int len = checkpoints.size();
		
		for(int i = 0; i < len; i ++) {
			Checkpoint checkpoint = checkpoints.get(i);
			
			try {
				if(OverlapTester.overlapRectangles(checkpoint.bounds, player.bounds)) {
					respawnPoint = new Vector2(checkpoint.position.x, checkpoint.position.y);
					for(int c = 0; c < len; c++) {
						checkpoints.get(c).active = false;
					}
					checkpoint.active = true;
				} 
			} catch (Exception e) {}
		}
	}
	
	//check if player hit ending flag
	private void checkEFlagCollisions() {
		int len = eflags.size();
		
		for(int i = 0; i < len; i ++) {
			EndingFlag eflag = eflags.get(i);
			
			if(OverlapTester.overlapRectangles(eflag.bounds, player.bounds)) {
				player.position.set(2,3);
				LevelScreen.nextLevel = 1;
			}
		}
	}
}
