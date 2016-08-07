package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Represents an Item with a count. Represents one slot in an inventory.
 * @author mimo31
 *
 */
public class ItemStack {

	// the number of items in this stack
	private byte count = 0;
	
	// Item type stored
	private Item item = null;
	
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
		return this.item;
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
	 */
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		g.setColor(Color.white);
		g.fillRect(x, y, width, height);
		if (this.item == null || this.count == 0)
		{
			return;
		}
		this.item.draw(g, x, y, width, height);
		g.setColor(Color.black);
		if (this.count != 1)
		{
			StringDraw.drawMaxString(g, width / 16, String.valueOf(this.count), new Rectangle(x, y + height * 3 / 4, width, height / 4));
		}
	}
}
