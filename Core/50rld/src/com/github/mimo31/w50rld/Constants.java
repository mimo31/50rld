package com.github.mimo31.w50rld;

/**
 * Defines various constants for the game.
 * @author mimo31
 *
 */
public class Constants {

	// scale for the biome generation, the bigger the scale, the bigger the biomes
	public static final int BIOME_SCALE = 64;
	
	// scale for the ore generation, the bigger the scale, the bigger the ore deposits and the bigger spaces between them
	public static final int ORE_SCALE = 32;
	
	// ore limits for the ore generation, if the noise function exceeds the limit at some point, a deposit will appear there
	public static final double COAL_ORE_LIMIT = 0.6;
	public static final double IRON_ORE_LIMIT = 0.6;
	public static final double GOLD_ORE_LIMIT = 0.7;
	
	// maximal possible zoom
	public static final int MAX_ZOOM = 5;
	
	// number of milliseconds that will pass between the user presses an arrow key and the player starts continuously moving
	public static final int MOVE_START_DELAY = 500;
	
	// minimal number of milliseconds that passes between the movement when the player is continuously moving
	public static final int MOVE_INTERVAL = 25;
	
}
