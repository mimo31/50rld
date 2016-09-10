package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents an Item in some kind of inventory. Its subclasses are intended to only be instantiated once during the game - when loading indexes.
 * @author mimo31
 *
 */
public abstract class Item {

	// name of the Item
	public final String name;
	
	// action that can be performed with the Item
	public final ItemAction[] actions;
	
	/**
	 * Draws the Item.
	 * @param g graphics to draw through
	 * @param x x coordinate of the location to draw
	 * @param y y coordinate of the location to draw
	 * @param width width of the rectangle to draw
	 * @param height height of the rectangle to draw
	 */
	public abstract void draw(Graphics2D g, int x, int y, int width, int height);

	public Item(String name, ItemAction[] actions)
	{
		this.name = name;
		this.actions = actions;
	}
	
	/**
	 * Represent an action that can be done with an Item.
	 * @author mimo31
	 *
	 */
	public static abstract class ItemAction {
		
		// name of the action
		public final String name;
		
		public ItemAction(String name)
		{
			this.name = name;
		}
		
		/**
		 * Determines whether the action can be applied on a specific Tile.
		 * @param tileX x coordinate of the Tile
		 * @param tileY y coordinate of the Tile
		 * @return whether the action can be applied
		 */
		public boolean actionPredicate(int tileX, int tileY)
		{
			return true;
		}

		/**
		 * The method to invoke when this action is chosen.
		 * @param tileX x coordinate of the Tile to apply the action to
		 * @param tileY y coordinate of the Tile to apply the action to
		 * @return whether the Item should be removed after this action
		 */
		public abstract boolean action(int tileX, int tileY);
		
	}
	
	/**
	 * Represents an ItemAction where a specified Structure is placed on the Tile with some specified minimal smoothness.
	 * @author mimo31
	 *
	 */
	public static class PlaceItemAction extends ItemAction {
		
		// name of the Structure that will be placed
		private final String structureName;
		
		// the minimal smoothness of the Tile so that the Structure can be placed
		private final float minimalSmoothness;
		
		public PlaceItemAction(String name, String structureName, float minimalSmoothness)
		{
			super(name);
			this.structureName = structureName;
			this.minimalSmoothness = minimalSmoothness;
		}

		@Override
		public boolean actionPredicate(int tileX, int tileY)
		{
			return Main.map.getTile(tileX, tileY).getSmoothness() >= this.minimalSmoothness;
		}
		
		@Override
		public boolean action(int tileX, int tileY)
		{
			Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure(this.structureName));
			return true;
		}
		
	}
	
	/**
	 * Represents an ItemAction where a specified Structure is placed on the Tile with some specified top Structure.
	 * @author mimo31
	 *
	 */
	public static class SurfacePlaceAction extends ItemAction {

		// name of the Structure that will be placed
		private final String structureName;
		
		// name of the Structure on which the new Structure can be placed
		private final String structureBelowName;
		
		public SurfacePlaceAction(String name, String structureName, String structureBelowName)
		{
			super(name);
			this.structureName = structureName;
			this.structureBelowName = structureBelowName;
		}

		@Override
		public boolean actionPredicate(int tileX, int tileY)
		{
			return Main.map.getTile(tileX, tileY).getTopStructure().structure == ObjectsIndex.getStructure(this.structureBelowName);
		}
		
		@Override
		public boolean action(int tileX, int tileY)
		{
			Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure(this.structureName));
			return true;
		}
	}
	
	/**
	 * Draw an Item with a border around it.
	 * @param g the graphics to draw through
	 * @param x x coordinate to draw to
	 * @param y y coordinate to draw to
	 * @param width width of the rectangle to draw to
	 * @param height height of the rectangle to draw to
	 * @param borderColor color of the border
	 * @param item Item to draw
	 */
	public static void drawWithBorder(Graphics2D g, int x, int y, int width, int height, Color borderColor, Item item)
	{
		// draw the background - only the border will be actually visible from it
		g.setColor(borderColor);
		g.fillRect(x, y, width, height);
		
		// width of the border
		int borderSize = Math.min(width, height) / 12;
		
		// draw the background for the item
		g.setColor(Color.white);
		g.fillRect(x + borderSize, y + borderSize, width - 2 * borderSize, height - 2 * borderSize);
		
		// draw the Item if not null
		if (item != null)
		{
			item.draw(g, x + borderSize, y + borderSize, width - 2 * borderSize, height - 2 * borderSize);
		}
	}
}

