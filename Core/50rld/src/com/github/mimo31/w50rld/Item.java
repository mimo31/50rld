package com.github.mimo31.w50rld;

import java.awt.Graphics2D;

/**
 * Represents an Item in some kind of inventory. Its subclasses are intended to only be instantiated once during the game - when loading indexes.
 * @author mimo31
 *
 */
public abstract class Item {

	// name of the Item
	public final String name;
	
	/**
	 * Draws the Item.
	 * @param g graphics to draw through
	 * @param x x coordinate of the location to draw
	 * @param y y coordinate of the location to draw
	 * @param width width of the rectangle to draw
	 * @param height height of the rectangle to draw
	 */
	public abstract void draw(Graphics2D g, int x, int y, int width, int height);

	public Item(String name)
	{
		this.name = name;
	}
}

