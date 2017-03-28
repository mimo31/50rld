package com.github.mimo31.w50rld;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

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
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param endx canvas x location of the top right corner
	 * @param endy canvas y location of the top right corner
	 * @param background color to use for the background
	 */
	public void draw(float startx, float starty, float endx, float endy, Color background)
	{
		// fill the background with white
		PaintUtils.setDrawColor(background);
		PaintUtils.drawRectangleP(startx, starty, endx, endy);
		
		// if the stack contains no Items, return
		if (this.item == null || this.count == 0)
		{
			return;
		}
		
		// screen size of the draw square for the Item
		float itemSize = Math.min((endx - startx) * Gui.width / 2, (endy - starty) * 2 / 3 * Gui.height / 2);
		
		float width = endx - startx;
		float height = endy - starty;
		float itemCanWidth = itemSize / Gui.width * 2;
		float itemCanHeight = itemSize / Gui.height * 2;
		
		// draw the Item
		this.item.draw(startx + (width - itemCanWidth) / 2, starty + (height * 2 / 3 - itemCanHeight) / 2 + height / 3, endx - (width - itemCanWidth) / 2, endy - (height * 2 / 3 - itemCanHeight) / 2);
		
		// draw the number of Items, if not 1
		if (this.count != 1)
		{
			TextDraw.drawText(String.valueOf(this.count), startx, starty, width, height / 3, TextAlign.MIDDLE, height / 3 / 8);
			glColor4f(0.2f, 0.2f, 0.2f, 0.2f);
		}
	}
	
	/**
	 * Draws an ItemStack with a border around it.
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param endx canvas x location of the top right corner
	 * @param endy canvas y location of the top right corner
	 * @param borderColor color of the border
	 * @param stack ItemStack to draw
	 */
	public static void drawWithBorder(float startx, float starty, float endx, float endy, Color borderColor, ItemStack stack)
	{
		// draw the background - only the border will be actually visible from it
		PaintUtils.setDrawColor(borderColor);
		PaintUtils.drawRectangleP(startx, starty, endx, endy);
		
		// canvas sizes of the whole stack including the border
		float width = endx - startx;
		float height = endy - starty;
		
		// screen width of the border 
		float borderSize = Math.min(width * Gui.width / 2, height * Gui.height / 2) / 12;
		
		// coordinates of the stack inside the border
		float stackx1 = startx + borderSize / Gui.width * 2;
		float stacky1 = starty + borderSize / Gui.height * 2;
		float stackx2 = endx - borderSize / Gui.width * 2;
		float stacky2 = endy - borderSize / Gui.height * 2;
		
		// draw the Stack if not null
		if (stack != null)
		{
			stack.draw(stackx1, stacky1, stackx2, stacky2, Color.white);
		}
		else
		{
			// draw just a white background
			glColor3f(1, 1, 1);
			PaintUtils.drawRectangleP(stackx1, stacky1, stackx2, stacky2);
		}
	}
}
