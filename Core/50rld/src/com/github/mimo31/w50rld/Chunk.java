package com.github.mimo31.w50rld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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
		int numberOfBiomes = ObjectsIndex.biomes.size();
		double[][] biomeNoises = new double[4096][numberOfBiomes];
		for (int i = 0; i < numberOfBiomes; i++)
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
				byte coal = (oreNoises[0][tileChunkIndex] < Constants.COAL_ORE_LIMIT) ? -128 : getSmallStructureData(globalXCoor, globalYCoor, 0);
				byte iron = (oreNoises[1][tileChunkIndex] < Constants.IRON_ORE_LIMIT) ? -128 : getSmallStructureData(globalXCoor, globalYCoor, 1);
				byte gold = (oreNoises[2][tileChunkIndex] < Constants.GOLD_ORE_LIMIT) ? -128 : getSmallStructureData(globalXCoor, globalYCoor, 2);
				
				// find the strongest biome noise
				double strongestBiomeNoise = 0;
				int strongestBiomeNoiseNumber = 0;
				double noiseSum = 0;
				for (int k = 0; k < numberOfBiomes; k++)
				{
					double currentBiomeNoise = biomeNoises[k][tileChunkIndex] * ObjectsIndex.biomes.get(k).occurrence;
					noiseSum += currentBiomeNoise;
					if (currentBiomeNoise > strongestBiomeNoise)
					{
						strongestBiomeNoise = currentBiomeNoise;
						strongestBiomeNoiseNumber = k;
					}
				}
				
				// calculate biome depth
				double depth = strongestBiomeNoise - (noiseSum - strongestBiomeNoise) / (numberOfBiomes - 1);
				
				// assign the Tile
				this.tiles[tileChunkIndex] = new Tile(coal, iron, gold, (byte) 0);
				
				// create a variable that is effectively final to make it able to use it in a lambda
				int biomeNumberSelected = strongestBiomeNoiseNumber;
				
				Biome selectedBiome = ObjectsIndex.biomes.get(strongestBiomeNoiseNumber);
				
				// function that gets small structure data by combining the biome number selected and the small structure number passed
				Function<Byte, Byte> smallStructureFunction = b -> getSmallStructureData(globalXCoor, globalYCoor, biomeNumberSelected ^ (b.byteValue() << 24));
				
				// values of the biome's medium structure noises at this Tile
				double[] mediumStructureNoiseValues = new double[selectedBiome.mediumStructuresScales.length];
				
				// list of the medium structure noises
				Noise[] mediumStructureNoises = ObjectsIndex.mediumStructureNoises.get(biomeNumberSelected);
				
				// get the noise values
				for (int k = 0; k < mediumStructureNoiseValues.length; k++)
				{
					mediumStructureNoiseValues[k] = mediumStructureNoises[k].getNoise(globalXCoor, globalYCoor);
				}
				
				// structure to be placed at this Tile
				List<StructureData> structures = selectedBiome.getTileStructures(depth, smallStructureFunction, mediumStructureNoiseValues);
				
				// check for nulls in structures
				for (int k = 0, n = structures.size(); k < n; k++)
				{
					StructureData structure = structures.get(k);
					if (structure == null)
					{
						throw new RuntimeException("One of the StructureData objects returned in the generation of a " + selectedBiome.name + "Biome is null.");
					}
					if (structure.structure == null)
					{
						throw new RuntimeException("One of the StructureData objects returned in the generation of a " + selectedBiome.name + "Biome has a null structure field.");
					}
				}
				
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
	 * @return small structure data at (x, y)
	 */
	private static byte getSmallStructureData(int x, int y, int structureNumber)
	{
		// an array of bytes constructed from x, y, structureNumber and one more byte created by combining the other ones to hash in order to obtain the small structure data
		int[] inputBytes = new int[4 + 4 + 4 + 1];
		
		// populate the array
		for (int i = 0; i < 4; i++)
		{
			int shift = (8 * i);
			inputBytes[i] = (x >> shift) & 255;
			inputBytes[4 + i] = (y >> shift) & 255;
			inputBytes[8 + i] = (structureNumber >> shift) & 255;
		}
		
		// assign the 13th mixed byte
		for (int i = 0; i < 12; i++) {
			inputBytes[12] ^= inputBytes[i];
		}
		
		// get the hash from the array
		int currentHash = hashArray[inputBytes[0]];
		for (int i = 1; i < 13; i++)
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
