package com.github.mimo31.w50rld;

import java.util.List;
import java.util.function.Function;

/**
 * An abstract class providing a method needed to generate Biome Tiles.
 * @author mimo31
 *
 */
public abstract class Biome {

	// scale of the Biome - larger scale, larger but more distant areas pieces on the map
	public final int scale;
	
	// scales of the medium structure noises used to generate medium structures
	public final int[] mediumStructuresScales;
	
	// the total occurrence of the Biome on the map, the number does not scale linearly with the actual occurrence
	public final double occurrence;
	
	// the name of the Biome
	public final String name;
	
	/**
	 * Returns the StructureData List of Structures that should be placed on a Biome Tile based on the provided biome depth, method to get small structure data, and medium structure noises.
	 * @param biomeDepth biome depth at the Tile
	 * @param getSmallStructureData function that returns small structure data byte given the number of small structure data
	 * @param mediumStructureNoises array of the length of mediumStructuresScales containing the values of medium structure noises at the Tile
	 * @return
	 */
	public abstract List<StructureData> getTileStructures(double biomeDepth, Function<Byte, Byte> getSmallStructureData, double[] mediumStructureNoises);
	
	protected Biome(String name)
	{
		this.name = name;
		this.scale = Constants.BIOME_SCALE;
		this.mediumStructuresScales = new int[0];
		this.occurrence = 1;
	}
	
	protected Biome(int scale, int[] mediumStructuresScales, double occurence, String name)
	{
		this.name = name;
		this.scale = scale;
		this.mediumStructuresScales = mediumStructuresScales;
		this.occurrence = occurence;
	}
}
