package com.energeticecho.cubical;

import com.androidapps.framework.Game;
import com.androidapps.framework.Music;
import com.androidapps.framework.Sound;
import com.androidapps.framework.gl.Font;
import com.androidapps.framework.gl.Texture;
import com.androidapps.framework.gl.TextureRegion;
import com.androidapps.framework.impl.GLGame;

public class Assets {
	//defining all assets
	public static Texture mainMenuSettingsOptionsBackground;
	public static TextureRegion mmSettingsOptiongsBgRegion;
	public static Texture darkBackground;
	public static TextureRegion darkBackgroundRegion;
	public static Texture muskBackground;
	public static TextureRegion muskBackgroundRegion;
	public static Texture lightBackground;
	public static TextureRegion lightBackgroundRegion;
	
	public static Texture items1; 
	public static Texture items2;
	public static Texture items3;
	
	public static TextureRegion logoRegion;
	public static TextureRegion gameOverRegion;
	public static TextureRegion mainMenuRegion;
	public static TextureRegion settingTextRegion;
	public static TextureRegion optionTextRegion;
	public static TextureRegion levelPackSelectRegion; //all level packs on screen
	public static TextureRegion endlessRegion;
	public static TextureRegion levelEndRegion;
	public static TextureRegion pauseMenuRegion;
	public static TextureRegion hpRegion;
	public static TextureRegion staminaRegion;
	public static TextureRegion hpfillRegion;
	public static TextureRegion staminafillRegion;
	
	public static TextureRegion darknessTextRegion;
	public static TextureRegion midTextRegion;
	public static TextureRegion lightTextRegion;
	public static TextureRegion levelSelectRegion;
	
	public static TextureRegion pauseButtonRegion;
	public static TextureRegion creditsButtonRegion;
	public static TextureRegion leftRegion;
	public static TextureRegion soundEnabledRegion;
	public static TextureRegion soundDisabledRegion;
	public static TextureRegion musicEnabledRegion;
	public static TextureRegion musicDisabledRegion;
	public static TextureRegion lockRegion;
	public static TextureRegion playerRegion;
	public static TextureRegion playerEyeRegion;
	public static TextureRegion fuelItemRegion;
	//public static TextureRegion energyItemRegion;
	public static TextureRegion checkPointOnRegion;
	public static TextureRegion checkPointOffRegion;
	
	public static TextureRegion darkTile;
	public static TextureRegion muskTile;
	public static TextureRegion lightTile;
	public static TextureRegion puddle;
	public static TextureRegion diamond;
	public static TextureRegion gold;
	public static TextureRegion silver;
	public static TextureRegion vertSpike;
	public static TextureRegion horizSpike;
	public static TextureRegion restTile;
	
	//font
	public static Font font;
	
	//sounds
	public static Sound click;
	public static Sound pickUpMineral;
	public static Sound hit;
	
	//music
	public static Music darkSoundtrack;
	public static Music shadowedSoundtrack;
	public static Music lightSoundtrack;
	
	//loads textures initializes texregions same with sounds and music
	public static void load(GLGame game) {
		darkBackground = new Texture(game, "darkBackgroundPortraitAtlas.png", true);
		darkBackgroundRegion = new TextureRegion(darkBackground, 0, 0, 320, 480);
		
		items1 = new Texture(game, "items1Atlas.png", true);
		mainMenuRegion = new TextureRegion(items1, 0, 0, 232, 184);
		settingTextRegion = new TextureRegion(items1, 0, 62, 232, 62);
		darknessTextRegion = new TextureRegion(items1, 265, 256, 246, 64);
		//must do later
		creditsButtonRegion = new TextureRegion(items1, 0, 0, 1, 1);
		optionTextRegion = new TextureRegion(items1, 0, 150, 232, 62);
		pauseMenuRegion = new TextureRegion(items1, 232, 0, 261, 184);
		logoRegion = new TextureRegion(items1, 0, 200, 296, 65);
		levelPackSelectRegion = new TextureRegion(items1, 265, 256, 246, 256);
		leftRegion = new TextureRegion(items1, 0, 390, 64, 64);
		musicDisabledRegion = new TextureRegion(items1, 64, 390, 64, 64);
		musicEnabledRegion = new TextureRegion(items1, 128, 390, 64, 64);
		soundDisabledRegion = new TextureRegion(items1, 0, 454, 64, 64);
		soundEnabledRegion = new TextureRegion(items1, 64, 454, 64, 64);
		lockRegion = new TextureRegion(items1, 128, 454, 64, 64);
		
		items2 = new Texture(game, "items2.png", false);
		levelEndRegion = new TextureRegion(items2, 0, 0, 251, 258);
		gameOverRegion = new TextureRegion(items2, 224, 254, 287, 257);
		font = new Font(items2, 256, 0, 16, 16, 20);
		
		items3 = new Texture(game, "items3Atlas.png", true);
		levelSelectRegion = new TextureRegion(items3, 0, 0, 320, 128);
		checkPointOffRegion = new TextureRegion(items3, 0, 128, 32, 64);
		checkPointOnRegion = new TextureRegion(items3, 40, 128, 32, 64);
		darkTile = new TextureRegion(items3, 80, 128, 32, 32);
		diamond = new TextureRegion(items3, 0, 200, 32, 32);
		gold = new TextureRegion(items3, 32, 200, 32, 32);
		silver = new TextureRegion(items3, 64, 200, 32, 32);
		fuelItemRegion = new TextureRegion(items3, 100, 200, 32, 32);
		restTile = new TextureRegion(items3, 142, 200, 32, 32);
		horizSpike = new TextureRegion(items3, 187, 200, 32, 32);
		vertSpike = new TextureRegion(items3, 223, 200, 32, 32);
		hpfillRegion = new TextureRegion(items3, 264, 200, 64, 32);
		staminafillRegion = new TextureRegion(items3, 264, 232, 64, 32);
		hpRegion = new TextureRegion(items3, 0, 232, 128, 32);
		staminaRegion = new TextureRegion(items3, 0, 264, 128, 32);
		puddle = new TextureRegion(items3, 0, 296, 136, 36);
		playerRegion = new TextureRegion(items3, 0, 336, 64, 64);
		playerEyeRegion = new TextureRegion(items3, 64, 336, 8, 8);
		
		//sounds; unfinished
		click = game.getAudio().newSound("click.ogg");
	}
	
	//reloads lost textures
	public static void reload() {
		mainMenuSettingsOptionsBackground.reload();
		darkBackground.reload();
		muskBackground.reload();
		lightBackground.reload();
		
		items1.reload(); 
		items2.reload();
		items3.reload();
	}
	
	//plays a sound
	public static void playSound(Sound sound) {
		if(Settings.soundEnabled) {
			sound.play(1);
		}
	}
	
	//plays indicated music track
	public static void playMusic(Music music) {
		if(Settings.musicEnabled) {
			music.play();
		}
	}


}
