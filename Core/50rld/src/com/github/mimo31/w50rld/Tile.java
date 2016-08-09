package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one Tile on the map.
 * 
 * @author mimo31
 *
 */
public class Tile {
	
	// amount of iron
	private byte iron;
	
	// amount of coal
	private byte coal;
	
	// amount of gold
	private byte gold;
	
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
	public Tile(byte iron, byte coal, byte gold, byte depth)
	{
		this.iron = iron;
		this.coal = coal;
		this.gold = gold;
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
			// no Structure is declared overdraw, so draw the underlying rock
			g.setColor(Color.gray);
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
		
		// draw items icon
		if (this.items != null && !this.items.isEmpty())
		{
			g.setColor(Color.red);
			g.fillRect(x, y, width / 4, height / 4);
		}
	}
	
	/**
	 * Returns whether there are some Items lying on the Tile.
	 * @return whether some Items lie on the Tile
	 */
	public boolean hasItems()
	{
		return this.items != null && !this.items.isEmpty();
	}
	
	/**
	 * Returns all Tile's Items.
	 * @return Tile's Items
	 */
	public List<ItemStack> getItems()
	{
		return this.items;
	}
	
	/**
	 * Sets the Tile's structures property.
	 * @param structures Structure list to set
	 */
	public void setStructures(List<Structure> structures)
	{
		this.structures = structures;
	}
	
	/**
	 * Removes the Tile's top Structure.
	 */
	public void popStructure()
	{
		int numberOfStructures;
		if (this.structures != null && (numberOfStructures = this.structures.size()) != 0)
		{
			this.structures.remove(numberOfStructures - 1);
		}
	}
	
	/**
	 * Adds a Structure to the top.
	 * @param structure structure to add
	 */
	public void pushStructure(Structure structure)
	{
		if (this.structures == null)
		{
			this.structures = new ArrayList<Structure>();
		}
		this.structures.add(structure);
	}
	
	/**
	 * Return the Structure at the top. Return null if no Structures are present.
	 * @return Tile's top Structure or null if no Structures are present
	 */
	public Structure getTopStructure()
	{
		int numberOfStructures;
		if (this.structures != null && (numberOfStructures = this.structures.size()) != 0)
		{
			return this.structures.get(numberOfStructures - 1);
		}
		return null;
	}
	
	/**
	 * Add Items to lie on the Tile.
	 * @param items Items to add
	 */
	public void addItems(ItemStack items)
	{
		// if items is null, create a new ArrayList
		if (this.items == null)
		{
			this.items = new ArrayList<ItemStack>();
		}
		
		Item item = items.getItem();
		
		// check whether there already is a stack with this Item, if yes, increment its count and return
		for (int i = 0, n = this.items.size(); i < n; i++)
		{
			ItemStack currentStack = this.items.get(i);
			if (currentStack.getItem() == item)
			{
				currentStack.setCount(currentStack.getCount() + items.getCount());
				return;
			}
		}
		
		// no stack with this Item -> create a new one
		ItemStack newStack = new ItemStack();
		newStack.setItem(item);
		newStack.setCount(items.getCount());
		
		this.items.add(newStack);
	}
	
	/**
	 * Tries to add Items to the player's inventory. If there is not enough space, the Items are let lie on the Tile.
	 * Does not change the state of the passed stack.
	 * @param items Items to add
	 */
	public void addInventoryItems(ItemStack items)
	{
		// create a copy of the stack
		ItemStack newStack = new ItemStack();
		newStack.setCount(items.getCount());
		newStack.setItem(items.getItem());
		
		// tries to add the Items to the inventory
		Main.tryAddInventoryItems(newStack);
		
		// if not all items were added, drop them on the Tile
		if (newStack.getCount() != 0)
		{
			this.addItems(newStack);
		}
	}
	
	/**
	 * Removes a stack of item from this Tile.
	 * @param stack the stack to be removed
	 */
	public void removeStack(ItemStack stack)
	{
		this.items.remove(stack);
	}
}
