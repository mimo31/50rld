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
}
