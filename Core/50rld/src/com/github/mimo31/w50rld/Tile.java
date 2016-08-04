package com.github.mimo31.w50rld;

/**
 * Represents one Tile on the map.
 * 
 * @author mimo31
 *
 */
public class Tile {

	public enum SurfaceType
	{
		WATER, DIRT, SAND
	}
	
	// amount of iron
	byte iron;
	
	// amount of coal
	byte coal;
	
	// amount of gold
	byte gold;
	
	// type of surface
	SurfaceType surface;
	
	// depth of the hole, if any
	byte depth;
	
	/**
	 * Creates a new Tile object with the specified fields.
	 * @param iron the amount of iron in this tile
	 * @param coal the amount of coal in this tile
	 * @param gold the amount of gold in this tile
	 * @param surface the type of surface on this tile
	 * @param depth the depth of the hole at this tile
	 */
	public Tile(byte iron, byte coal, byte gold, SurfaceType surface, byte depth)
	{
		this.iron = iron;
		this.coal = coal;
		this.gold = gold;
		this.surface = surface;
		this.depth = depth;
	}
}
