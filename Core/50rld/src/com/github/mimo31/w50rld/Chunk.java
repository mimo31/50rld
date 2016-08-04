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
	
	// array of randomly permuted values from 0 to 255 to generate hashed ore amounts
	private static byte[] hashArray = new byte[256];
	
	// Tiles in this Chunk, one dimensional array of 4096 elements, tiles are stored in this order:
	// (x = 0, y = 0), (x = 1, y = 0), ... (x = 0, y = 1), (x = 1, y = 1)
	Tile[] tiles;
	
	/**
	 * Creates a new Chunk and generates its Tiles based on its location.
	 * @param x The x coordinate of the Chunk.
	 * @param y The y coordinate of the Chunk.
	 */
	public Chunk(int x, int y)
	{
		this.x = x;
		this.y = y;
		
		// collect the coordinates of every tile in this chunk
		int[] allXCoors = new int[4096];
		int[] allYCoors = new int[4096];
		for (int i = 0; i < 64; i++)
		{
			for (int j = 0; j < 64; j++)
			{
				// coordinates of this Tile on the entire map
				int xMapLocation = x * 64 + j;
				int yMapLocation = y * 64 + i;
				
				// add the coordinates to the arrays
				allXCoors[i * 64 + j] = xMapLocation;
				allYCoors[i * 64 + j] = yMapLocation;
			}
		}
		
		// get biome noises for all biomes on all those coordinates
		double[][] biomeNoises = new double[4096][4];
		for (int i = 0; i < 4; i++)
		{
			biomeNoises[i] = Noise.biomeNoises[i].getNoise(allXCoors, allYCoors);
		}
		
		// get ore noise for all ores on all those coordinates
		double[][] oreNoises = new double[4096][3];
		for (int i = 0; i < 3; i++)
		{
			oreNoises[i] = Noise.oreNoises[i].getNoise(allXCoors, allYCoors);
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
				// else calculate the amount using the getOreAmount function
				byte coal = (oreNoises[0][tileChunkIndex] < Constants.COAL_ORE_LIMIT) ? 0 : getOreAmount(globalXCoor, globalYCoor, 0);
				byte iron = (oreNoises[1][tileChunkIndex] < Constants.IRON_ORE_LIMIT) ? 0 : getOreAmount(globalXCoor, globalYCoor, 1);
				byte gold = (oreNoises[2][tileChunkIndex] < Constants.GOLD_ORE_LIMIT) ? 0 : getOreAmount(globalXCoor, globalYCoor, 2);
				
				// find the strongest biome noise
				double strongestBiomeNoise = 0;
				int strongestBiomeNoiseNumber = 0;
				for (int k = 0; k < 4; k++)
				{
					double currentBiomeNoise = biomeNoises[k][tileChunkIndex];
					if (currentBiomeNoise > strongestBiomeNoise)
					{
						strongestBiomeNoise = currentBiomeNoise;
						strongestBiomeNoiseNumber = i;
					}
				}
				
				// determine the SurfaceType of the Tile based on the strongest noise
				SurfaceType surfaceType = null;
				switch (strongestBiomeNoiseNumber)
				{
					case 0:
						surfaceType = SurfaceType.WATER;
						break;
					case 1:
					case 2:
						surfaceType = SurfaceType.DIRT;
					case 3:
						surfaceType = SurfaceType.SAND;
				}
				
				// assign the Tile
				this.tiles[tileChunkIndex] = new Tile(coal, iron, gold, surfaceType, (byte) 0);
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
	 * Calculates the amount of ore of specific number that should be found on a specified location.
	 * @param x the x coordinate of the location
	 * @param y the y coordinate of the location
	 * @param oreNumber the number of the ore
	 * @return the amount of ore of number oerNumber that should be found at (x, y) (if any)
	 */
	private static byte getOreAmount(int x, int y, int oreNumber)
	{
		// an array of bytes constructed from x, y and oreNumber to hash in order to obtain the ore amount
		int[] inputBytes = new int[4 + 4 + 1];
		
		// populate the array
		for (int i = 0; i < 4; i++)
		{
			inputBytes[i] = (x >> (8 * i)) | 255;
		}
		for (int i = 0; i < 4; i++)
		{
			inputBytes[4 + i] = (y >> (8 * i)) | 255;
		}
		inputBytes[8] = oreNumber | 255;
		
		// get the hash from the array
		int currentHash = hashArray[inputBytes[0]];
		for (int i = 1; i < 9; i++)
		{
			currentHash = hashArray[(currentHash + inputBytes[i]) | 255];
		}
		
		return (byte) currentHash;
	}
	
	/**
	 * Initializes the hash array for creating ore amount based on the seed in Main.
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
			hashArray[i] = list.get(i).byteValue();
		}
	}
}
