package com.github.mimo31.w50rld;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

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
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param endx canvas x location of the top right corner
	 * @param endy canvas y location of the top right corner
	 */
	public abstract void draw(float startx, float starty, float endx, float endy);

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
	 * Draws an Item with a border around it.
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param endx canvas x location of the top right corner
	 * @param endy canvas y location of the top right corner
	 * @param borderColor color of the border
	 * @param item Item to draw
	 */
	public static void drawWithBorder(float startx, float starty, float endx, float endy, Color borderColor, Item item)
	{
		// draw the background - only the border will be actually visible from it
		PaintUtils.setDrawColor(borderColor);
		PaintUtils.drawRectangleP(startx, starty, endx, endy);
		
		// canvas sizes of the whole item including the border
		float width = endx - startx;
		float height = endy - starty;
		
		// screen width of the border 
		float borderSize = Math.min(width * Gui.width / 2, height * Gui.height / 2) / 12;
		
		// coordinates of the item inside the border
		float itemx1 = startx + borderSize / Gui.width * 2;
		float itemy1 = starty + borderSize / Gui.height * 2;
		float itemx2 = endx - borderSize / Gui.width * 2;
		float itemy2 = endy - borderSize / Gui.height * 2;

		// draw just a white background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangleP(itemx1, itemy1, itemx2, itemy2);
		
		// draw the item if not null
		if (item != null)
		{
			item.draw(itemx1, itemy1, itemx2, itemy2);
		}
	}
	
	/**
	 * Draws an Item with a border around it.
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param width rectangle width in canvas width
	 * @param height rectangle height in canvas height
	 * @param borderColor color of the border
	 * @param item Item to draw
	 */
	public static void drawWithBorderS(float startx, float starty, float width, float height, Color borderColor, Item item)
	{
		drawWithBorder(startx, starty, startx + width, starty + height, borderColor, item);
	}
}

