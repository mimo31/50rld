package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import com.github.mimo31.w50rld.StringDraw.TextAlign;

/**
 * Represents an Item with a count. Represents one slot in an inventory.
 * @author mimo31
 *
 */
public class ItemStack {

	// the number of items in this stack
	private int count = 0;
	
	// Item type stored
	private Item item = null;
	
	public ItemStack()
	{
		
	}
	
	public ItemStack(Item item, int count)
	{
		this.item = item;
		this.count = count;
	}
	
	/**
	 * Sets the stack's item to the Item specified.
	 * @param item the Item to set
	 */
	public void setItem(Item item)
	{
		this.item = item;
	}
	
	/**
	 * Returns the stack's item.
	 * @return stack's item
	 */
	public Item getItem()
	{
		return this.count == 0 ? null : this.item;
	}
	
	/**
	 * Sets the count of items in the stack.
	 * @param count the count of items
	 */
	public void setCount(int count)
	{
		this.count = (byte) count;
	}
	
	/**
	 * Returns the count of items in the stack.
	 * @return the count of items
	 */
	public int getCount()
	{
		return (this.item == null) ? 0 : this.count;
	}
	
	/**
	 * Draws an inventory slot with the Item and the count.
	 * @param g Graphics to draw through
	 * @param x x coordinate of the location to draw
	 * @param y y coordinate of the location to draw
	 * @param width width of the rectangle to draw
	 * @param height height of the rectangle to draw
	 * @param background color to use for the background
	 */
	public void draw(Graphics2D g, int x, int y, int width, int height, Color background)
	{
		// fill the background with white
		g.setColor(background);
		g.fillRect(x, y, width, height);
		
		// if the stack contains no Items, return
		if (this.item == null || this.count == 0)
		{
			return;
		}
		
		// size of the draw square for the Item
		int itemSize = Math.min(width, height - height / 3);
		
		// draw the Item
		this.item.draw(g, x + (width - itemSize) / 2, y + (height - height / 3 - itemSize) / 2, itemSize, itemSize);
		
		// draw the number of Items, if not 1
		g.setColor(Color.black);
		if (this.count != 1)
		{
			Rectangle rect = new Rectangle(x, y + height - height / 3, width, height / 3);
			StringDraw.drawMaxString(g, width / 16, String.valueOf(this.count), TextAlign.MIDDLE, rect, Font.BOLD);
		}
	}
	
	/**
	 * Draws an ItemStack with a border around it.
	 * @param g graphics to draw through
	 * @param x x coordinate of the rectangle to draw in
	 * @param y y coordinate of the rectangle to draw in
	 * @param width width of the rectangle to draw in
 	 * @param height height of the rectangle to draw in
	 * @param borderColor color of the border
	 * @param stack ItemStack to draw
	 */
	public static void drawWithBorder(Graphics2D g, int x, int y, int width, int height, Color borderColor, ItemStack stack)
	{
		// draw the background - only the border will be actually visible from it
		g.setColor(borderColor);
		g.fillRect(x, y, width, height);
		
		// width of the border
		int borderSize = Math.min(width, height) / 12;
		
		// draw the background for the item
		g.setColor(Color.white);
		g.fillRect(x + borderSize, y + borderSize, width - 2 * borderSize, height - 2 * borderSize);
		
		// draw the Stack if not null
		if (stack != null)
		{
			stack.draw(g, x + borderSize, y + borderSize, width - 2 * borderSize, height - 2 * borderSize, Color.white);
		}
	}
}
