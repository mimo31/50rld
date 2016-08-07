package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

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
	private byte iron;
	
	// amount of coal
	private byte coal;
	
	// amount of gold
	private byte gold;
	
	// type of surface
	private SurfaceType surface;
	
	// depth of the hole, if any
	private byte depth;
	
	// laying items, if any
	private List<ItemStack> items = null;
	
	// structures on the Tile, the first item in this list is the Structure farthest from the surface
	private List<Structure> structures = null;
	
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
	
	/**
	 * Paints the Tile through the specified Graphics2D on a location at (x, y) with a specified width and height.
	 * @param g graphics to paint
	 * @param x x coordinate of the location to paint at
	 * @param y y coordinate of the location to paint at
	 * @param width width of the Tile to paint
	 */
	public void paint(Graphics2D g, int x, int y, int width, int height)
	{
		// find out the index of the first Structure to draw at the Tile
		// (because it is the last overdraw Structure, so it would overdraw the prior anyway
		int firstStructureIndexToDraw = -1;
		if (this.structures != null)
		{
			for (int i = this.structures.size() - 1; i >= 0; i++)
			{
				if (this.structures.get(i).overdraws)
				{
					firstStructureIndexToDraw = i;
					break;
				}
			}
		}
		
		if (firstStructureIndexToDraw == -1)
		{
			// no Structure is declared overdraw, so draw the underlying ground
			Color color = null;
			switch (this.surface)
			{
				case WATER:
					color = Color.blue;
					break;
				case DIRT:
					color = new Color(166, 104, 42);
					break;
				case SAND:
					color = Color.yellow;
					break;
			}
			g.setColor(color);
			g.fillRect(x, y, width, height);
		}
		
		// draw all the Structures that need to be drawn
		if (this.structures != null)
		{
			for (int i = (firstStructureIndexToDraw == -1) ? 0 : firstStructureIndexToDraw, n = this.structures.size(); i < n; i++)
			{
				this.structures.get(i).draw(g, x, y, width, height);
			}
		}
	}
	
	/**
	 * Sets the Tile's structures property.
	 * @param structures Structure list to set
	 */
	public void setStructures(List<Structure> structures)
	{
		this.structures = structures;
	}
}
