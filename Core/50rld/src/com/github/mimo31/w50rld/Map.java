package com.github.mimo31.w50rld;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Map of the game world - an organize structure of Chunks.
 * 
 * @author mimo31
 *
 */
public class Map {
	
	// all of the Chunks that were generated
	private List<Chunk> chunks;
	
	/**
	 * Creates a new Map and initializes its Chunk list.
	 */
	public Map()
	{
		this.chunks = new ArrayList<Chunk>();
	}
	
	/**
	 * Returns a Tile at a specified location. (Generates a new Chunk if needed.)
	 * @param x the x coordinate of the Tile
	 * @param y the y coordinate of the Tile
	 * @return The Tile at (x, y).
	 */
	public Tile getTile(int x, int y)
	{
		// coordinates of the Chunk
		int chunkX = (int)Math.floor(x / 64d);
		int chunkY = (int)Math.floor(y / 64d);
		
		// coordinates within the Chunk
		int inChunkX = x - chunkX * 64;
		int inChunkY = y - chunkY * 64;
		
		// search for the Chunk
		Chunk chunk = null;
		for (int i = 0, n = this.chunks.size(); i < n; i++)
		{
			Chunk currentChunk = this.chunks.get(i);
			if (chunkX == currentChunk.x && chunkY == currentChunk.y)
			{
				chunk = currentChunk;
				break;
			}
		}
		
		// Chunk not found - generate it
		if (chunk == null)
		{
			chunk = new Chunk(chunkX, chunkY);
			this.chunks.add(chunk);
		}
		
		// return the Tile from the Chunk
		return chunk.getTile(inChunkX, inChunkY);
	}
	
	/**
	 * Ensures that all the Tile in the specified rectangle on the Map will have generated their Chunks generated.
	 * @param x the x coordinate of the top-left corner of the rectangle (in the global coordinate system)
	 * @param y the y coordinate of the top-left corner of the rectangle
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 */
	public void prepareTiles(int x, int y, int width, int height)
	{
		// Chunk coordinates of the leftmost and the topmost Chunks
		int minChunkX = (int)Math.floor(x / 64f);
		int minChunkY = (int)Math.floor(y / 64f);
		
		// Chunk coordinates of the rightmost and the bottom-most Chunks
		int maxChunkX = (int)Math.floor((x + width - 1) / 64f);
		int maxChunkY = (int)Math.floor((y + height - 1) / 64f);
		
		// iterate through all the required Chunks
		for (int i = minChunkY; i <= maxChunkY; i++)
		{
			for (int j = minChunkX; j <= maxChunkX; j++)
			{
				// search for the required Chunk
				boolean found = false;
				for (int k = 0, n = this.chunks.size(); k < n; k++)
				{
					Chunk currentChunk = this.chunks.get(k);
					if (currentChunk.x == j && currentChunk.y == i)
					{
						found = true;
						break;
					}
				}
				
				// Chunk not found -> create it
				if (!found)
				{
					this.chunks.add(new Chunk(j, i));
				}
			}
		}
		
	}
	
	/**
	 * Returns an array of Tiles in the specified rectangle on the Map.
	 * The Tiles are store in this order: (x = m, y = n), (x = m + 1, y = n), ...(x = m, y = n + 1), (x = m + 1, y = n + 1)
	 * @param x the x coordinate of the top-left corner of the rectangle
	 * @param y the y coordinate of the top-left corner of the rectangle
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @return an array of all the Tiles in the rectangle
	 */
	public Tile[] getTiles(int x, int y, int width, int height)
	{
		// Chunk coordinates of the leftmost and the topmost Chunks
		int minChunkX = (int)Math.floor(x / 64f);
		int minChunkY = (int)Math.floor(y / 64f);
		
		// Chunk coordinates of the rightmost and the bottom-most Chunks
		int maxChunkX = (int)Math.floor((x + width - 1) / 64f);
		int maxChunkY = (int)Math.floor((y + height - 1) / 64f);
		
		// array to store the Tiles
		Tile[] tiles = new Tile[width * height];
		
		// iterate through every required Chunk
		for (int i = minChunkY; i <= maxChunkY; i++)
		{
			for (int j = minChunkX; j <= maxChunkX; j++)
			{
				// search for the required Chunk
				Chunk chunk = null;
				for (int k = 0, n = this.chunks.size(); k < n; k++)
				{
					Chunk currentChunk = this.chunks.get(k);
					if (currentChunk.x == j && currentChunk.y == i)
					{
						chunk = currentChunk;
						break;
					}
				}
				
				// Chunk not found -> create it
				if (chunk == null)
				{
					chunk = new Chunk(j, i);
					this.chunks.add(chunk);
				}
				
				// iterate through the Tiles of the Chunk either form the origin of the Chunk or from the origin of our Map rectangle
				for (int k = Math.max(y, i * 64), n = Math.min(y + height, (i + 1) * 64); k < n; k++)
				{
					for (int l = Math.max(x, j * 64), m = Math.min(x + width, (j + 1) * 64); l < m; l++)
					{
						// assign the Tile to its location in the array
						tiles[(k - y) * width + (l - x)] = chunk.getTile(l - j * 64, k - i * 64);
					}
				}
			}
		}
		
		return tiles;
	}
	
	/**
	 * Updates all Tile Structures in a specified rectangle.
	 * @param x x coordinate of the rectangle to update
	 * @param y y coordinate of the rectangle to update
	 * @param width width of the rectangle to update
	 * @param height height of the rectangle to update
	 * @param deltaTime time difference to cover in the update
	 */
	public void updateTiles(int x, int y, int width, int height, int deltaTime)
	{
		Tile[] tilesToUpdate = this.getTiles(x, y, width, height);
		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				tilesToUpdate[i * width + j].updateStructures(j + x, i + y, deltaTime);
			}
		}
	}
}
