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
	private List<StructureData> structures = null;
	
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
	 * @param tileX x coordinate of the Tile
	 * @param tileY y coordinate of the Tile
	 */
	public void paint(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY)
	{
		// find out the index of the first Structure to draw at the Tile
		// (because it is the last overdraw Structure, so it would overdraw the prior anyway
		int firstStructureIndexToDraw = -1;
		if (this.structures != null)
		{
			for (int i = this.structures.size() - 1; i >= 0; i--)
			{
				if (this.structures.get(i).structure.overdraws)
				{
					firstStructureIndexToDraw = i;
					break;
				}
			}
		}
		
		if (firstStructureIndexToDraw == -1)
		{
			// no Structure is declared overdraw, so draw the underlying rock or end
			PaintUtils.drawSquareTexture(g, x, y, width, height, this.depth == 32 ? "End.png" : "RockS.png");
			
			// draw the ores if present
			if (this.depth != 32)
			{
				if (this.getIronAmount() != 0)
				{
					PaintUtils.drawSquareTexture(g, x, y, width, height, "IronOreS.png");
				}
				if (this.getCoalAmount() != 0)
				{
					PaintUtils.drawSquareTexture(g, x, y, width, height, "CoalS.png");
				}
				if (this.getGoldAmount() != 0)
				{
					PaintUtils.drawSquareTexture(g, x, y, width, height, "GoldOreS.png");
				}
			}
		}
		
		// draw all the Structures that need to be drawn
		if (this.structures != null)
		{
			for (int i = (firstStructureIndexToDraw == -1) ? 0 : firstStructureIndexToDraw, n = this.structures.size(); i < n; i++)
			{
				this.structures.get(i).structure.draw(g, x, y, width, height, tileX, tileY, i);
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
	public void setStructures(List<StructureData> structures)
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
		if (structure == null)
		{
			throw new NullPointerException("The structure passed is null.");
		}
		if (this.structures == null)
		{
			this.structures = new ArrayList<StructureData>();
		}
		this.structures.add(structure.createStructureData());
	}
	
	/**
	 * Adds a Structure to the top.
	 * @param structure structure to add
	 */
	public void pushStructure(StructureData structure)
	{
		if (this.structures == null)
		{
			this.structures = new ArrayList<StructureData>();
		}
		this.structures.add(structure);
	}
	
	/**
	 * Return the Structure at the top. Return null if no Structures are present.
	 * @return Tile's top Structure or null if no Structures are present
	 */
	public StructureData getTopStructure()
	{
		int numberOfStructures;
		if (this.structures != null && (numberOfStructures = this.structures.size()) != 0)
		{
			return this.structures.get(numberOfStructures - 1);
		}
		return null;
	}
	
	/**
	 * Add Items to lie on the Tile. Ignores empty stacks.
	 * @param items Items to add
	 */
	public void addItems(ItemStack items)
	{
		if (items.getCount() == 0)
		{
			return;
		}
		
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
	
	/**
	 * Returns the smoothness of the Tile. That is the smoothness of the top Structure or 1 as the smoothness of the bare rock when no Structure is present.
	 * @return the smoothness of the Tile
	 */
	public float getSmoothness()
	{
		int structureCount = this.structures.size();
		if (structureCount == 0)
		{
			return 1;
		}
		return this.structures.get(structureCount - 1).structure.smoothness;
	}
	
	/**
	 * Call the Update method of all the Structures at this Tile.
	 * @param tileX x coordinate of the Tile
	 * @param tileY y coordinate of the Tile
	 * @param deltaTime time difference to cover in the update
	 */
	public void updateStructures(int tileX, int tileY, int deltaTime)
	{
		for (int i = 0, n = this.structures.size(); i < n; i++)
		{
			this.structures.get(i).update(tileX, tileY, deltaTime, i);
		}
	}
	
	/**
	 * Returns whether there are some structures on the Tile.
	 * @return whether there are some structures
	 */
	public boolean hasStructures()
	{
		return !this.structures.isEmpty();
	}
	
	/**
	 * Returns the depth of the hole at the Tile. That is the amount of rock mined.
	 * @return the depth of the hole
	 */
	public int getDepth()
	{
		return this.depth;
	}
	
	/**
	 * Increments the depth of the hole by one.
	 */
	public void incrementDepth()
	{
		this.depth++;
	}
	
	/**
	 * Returns the amount of iron currently available at the top.
	 * @return the amount of iron at the top
	 */
	public int getIronAmount()
	{
		int totalAmount = (this.iron + 128) / 8;
		int middleDistance = Math.abs(8 - this.depth) + 1 + (this.depth < 8 ? 1 : 0);
		return middleDistance > totalAmount ? 0 : (middleDistance + 16 > totalAmount ? 1 : 2);
	}
	
	/**
	 * Returns the amount of coal currently available at the top.
	 * @return the amount of coal at the top
	 */
	public int getCoalAmount()
	{
		int totalAmount = (this.coal + 128) / 8;
		int middleDistance = Math.abs(16 - this.depth) + 1 + (this.depth < 16 ? 1 : 0);
		return middleDistance > totalAmount ? 0 : (middleDistance + 16 > totalAmount ? 1 : 2);
	}
	
	/**
	 * Returns the amount of gold currently available at the top.
	 * @return the amount of gold at the top
	 */
	public int getGoldAmount()
	{
		int totalAmount = (this.gold + 128) / 8;
		int middleDistance = Math.abs(24 - this.depth) + 1 + (this.depth < 24 ? 1 : 0);
		return middleDistance > totalAmount ? 0 : (middleDistance + 16 > totalAmount ? 1 : 2);
	}
	
	/**
	 * Returns the structure at the specified location of the list.
	 * @param structureNumber structure's location in the structure list
	 * @return structure at the specified location
	 */
	public StructureData getStructure(int structureNumber)
	{
		if (structureNumber >= this.structures.size() || structureNumber < 0)
		{
			throw new RuntimeException("Invalid structure number.");
		}
		return this.structures.get(structureNumber);
	}
	
	/**
	 * Returns the number of structures at this Tile.
	 * @return number of structures
	 */
	public int getStructureCount()
	{
		return this.structures.size();
	}
	
	/**
	 * Inserts a structure to a specified location of the structure list.
	 * @param structure structure to insert
	 * @param position position to insert to
	 */
	public void insertStructure(Structure structure, int position)
	{
		if (structure == null)
		{
			throw new NullPointerException("The structure is null.");
		}
		this.structures.add(position, structure.createStructureData());
	}
	
	/**
	 * Inserts a structure to a specified location of the structure list.
	 * @param structure structure to insert
	 * @param position position to insert to
	 */
	public void insertStructure(StructureData structure, int position)
	{
		if (structure == null)
		{
			throw new NullPointerException("The structure is null.");
		}
		this.structures.add(position, structure);
	}
	
	/**
	 * Removes a structure at a specified position.
	 * @param position of the structure to remove
	 */
	public void removeStructure(int position)
	{
		if (this.structures.size() <= position)
		{
			throw new IndexOutOfBoundsException("There are only " + String.valueOf(this.structures.size()) + " structures, so there is no structure at position " + String.valueOf(position) + ".");
		}
		this.structures.remove(position);
	}
}
