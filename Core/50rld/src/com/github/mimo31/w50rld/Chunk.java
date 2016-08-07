package com.github.mimo31.w50rld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.mimo31.w50rld.Tile.SurfaceType;

/**
 * Represents a 64x64 grid of Tiles on the map.
 * 
 * @author mimo31
 *
 */
public class Chunk {

	// x coordinate of the Chunk
	public final int x;
	
	// y coordinate of the Chunk
	public final int y;
	
	// array of randomly permuted values from 0 to 255 to generate hashed small structures
	private static int[] hashArray = new int[256];
	
	// Tiles in this Chunk, one dimensional array of 4096 elements, tiles are stored in this order:
	// (x = 0, y = 0), (x = 1, y = 0), ... (x = 0, y = 1), (x = 1, y = 1)
	private Tile[] tiles;
	
	/**
	 * Creates a new Chunk and generates its Tiles based on its location.
	 * @param x The x coordinate of the Chunk.
	 * @param y The y coordinate of the Chunk.
	 */
	public Chunk(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.tiles = new Tile[4096];
		
		// get biome noises for all biomes on all those coordinates
		double[][] biomeNoises = new double[4096][4];
		for (int i = 0; i < 4; i++)
		{
			biomeNoises[i] = Noise.biomeNoises[i].getNoiseRectangle(x * 64, y * 64, 64, 64);
		}
		
		// get ore noise for all ores on all those coordinates
		double[][] oreNoises = new double[4096][3];
		for (int i = 0; i < 3; i++)
		{
			oreNoises[i] = Noise.oreNoises[i].getNoiseRectangle(x * 64, y * 64, 64, 64);
		}
		
		for (int i = 0; i < 64; i++)
		{
			for (int j = 0; j < 64; j++)
			{
				// coordinates of this Tile on the entire map
				int globalXCoor = j + x * 64;
				int globalYCoor = i + y * 64;
				
				// the index of this Tile within this Chunk
				int tileChunkIndex = i * 64 + j;
				
				// get the ore amounts - if the noise function is lower than the limit, return 0,
				// else calculate the amount using the getSmallStructureData function
				byte coal = (oreNoises[0][tileChunkIndex] < Constants.COAL_ORE_LIMIT) ? 0 : getSmallStructureData(globalXCoor, globalYCoor, 0);
				byte iron = (oreNoises[1][tileChunkIndex] < Constants.IRON_ORE_LIMIT) ? 0 : getSmallStructureData(globalXCoor, globalYCoor, 1);
				byte gold = (oreNoises[2][tileChunkIndex] < Constants.GOLD_ORE_LIMIT) ? 0 : getSmallStructureData(globalXCoor, globalYCoor, 2);
				
				// find the strongest biome noise
				double strongestBiomeNoise = 0;
				int strongestBiomeNoiseNumber = 0;
				double noiseSum = 0;
				for (int k = 0; k < 4; k++)
				{
					double currentBiomeNoise = biomeNoises[k][tileChunkIndex];
					noiseSum += currentBiomeNoise;
					if (currentBiomeNoise > strongestBiomeNoise)
					{
						strongestBiomeNoise = currentBiomeNoise;
						strongestBiomeNoiseNumber = k;
					}
				}
				
				// calculate biome depth
				double depth = strongestBiomeNoise - (noiseSum - strongestBiomeNoise) / 3;
				
				// structures to be placed on this Tile
				List<Structure> structures = null;
				
				// determine the SurfaceType of the Tile based on the strongest noise
				SurfaceType surfaceType = null;
				switch (strongestBiomeNoiseNumber)
				{
					case 0:
						surfaceType = SurfaceType.WATER;
						break;
					case 1:
						surfaceType = SurfaceType.DIRT;
						structures = new ArrayList<Structure>();
						
						// scale the depth to make the work with it easier
						depth *= 3;
						
						// calculate the probabilities of grass or bush appearing
						int grassP = depth > 0.5 ? 0 : (int)((1 - Math.pow(depth * 2, 1 / 3d)) * 256);
						int bushP = depth > 0.75 ? 0 : (int)((1 - depth * 4 / 3) * 256);
						
						// get the small structure data
						int strData = getSmallStructureData(globalXCoor, globalYCoor, 4) + 128;
						
						// decide which structure will be placed on this Tile
						if (strData < grassP)
						{
							structures.add(ObjectsIndex.getStructure("Grass"));
						}
						else if (strData < bushP)
						{
							structures.add(ObjectsIndex.getStructure("Bush"));
						}
						else
						{
							structures.add(ObjectsIndex.getStructure("Tree"));
						}
						break;
					case 2:
						surfaceType = SurfaceType.DIRT;
						structures = new ArrayList<Structure>();
						// decide whether to put a Grass or a Bush
						if (getSmallStructureData(globalXCoor, globalYCoor, 3) + 128 < Constants.BUSH_IN_GRASS_PROB * 256)
						{
							structures.add(ObjectsIndex.getStructure("Bush"));
						}
						else
						{
							structures.add(ObjectsIndex.getStructure("Grass"));
						}
						break;
					case 3:
						surfaceType = SurfaceType.SAND;
						break;
				}
				
				// assign the Tile
				this.tiles[tileChunkIndex] = new Tile(coal, iron, gold, surfaceType, (byte) 0);
				
				// assign Tile's Structures
				this.tiles[tileChunkIndex].setStructures(structures);
			}
		}
	}
	
	/**
	 * Returns the Tile at the specified coordinates within the Chunk.
	 * @param x Tile's x coordinate within the Chunk.
	 * @param y Tile's y coordinate within the Chunk.
	 * @return
	 */
	public Tile getTile(int x, int y)
	{
		return this.tiles[y * 64 + x];
	}
	
	/**
	 * Calculates a value determined by Tile's coordinates and a structureNumber.
	 * Used to calculate ore amounts, appearance of structures like Bushes or trees and more.
	 * @param x the x coordinate of the location
	 * @param y the y coordinate of the location
	 * @param structureNumber the number of the structure
	 * @return the amount of ore of number oerNumber that should be found at (x, y) (if any)
	 */
	private static byte getSmallStructureData(int x, int y, int structureNumber)
	{
		// an array of bytes constructed from x, y and oreNumber to hash in order to obtain the ore amount
		int[] inputBytes = new int[4 + 4 + 1];
		
		// populate the array
		for (int i = 0; i < 4; i++)
		{
			inputBytes[i] = (x >> (8 * i)) & 255;
		}
		for (int i = 0; i < 4; i++)
		{
			inputBytes[4 + i] = (y >> (8 * i)) & 255;
		}
		inputBytes[8] = structureNumber & 255;
		
		// get the hash from the array
		int currentHash = hashArray[inputBytes[0]];
		for (int i = 1; i < 9; i++)
		{
			currentHash = hashArray[(currentHash + inputBytes[i]) & 255];
		}
		
		return (byte) currentHash;
	}
	
	/**
	 * Initializes the hash array for creating small structures based on the seed in Main.
	 */
	public static void initializeHashArray()
	{
		// create a Random object using the seed in the Main class
		Random r = new Random(Main.SEED);
		
		// create a randomly permuted list of arrays from 0 to 256
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < 256; i++)
		{
			list.add(r.nextInt(i + 1), i);
		}
		
		// convert the list to the array
		for (int i = 0; i < 256; i++)
		{
			hashArray[i] = list.get(i).intValue();
		}
	}
}
