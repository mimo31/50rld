package com.github.mimo31.w50rld;

import java.awt.Graphics2D;

/**
 * Represents a Structure on the ground. Its subclasses are intended to only be instantiated once during the game - when loading indexes.
 */
public abstract class Structure {

	// name of the Structure
	public final String name;
	
	// indicates whether the structure takes up the whole Tile when drawn
	public final boolean overdraws;
	
	/**
	 * Draws the Structure.
	 * @param g graphics to draw through
	 * @param x x coordinate of the location to draw
	 * @param y y coordinate of the location to draw
	 * @param width width of the rectangle to draw
	 * @param height height of the rectangle to draw
	 */
	public abstract void draw(Graphics2D g, int x, int y, int width, int height);

	public Structure(String name, boolean overdraws)
	{
		this.name = name;
		this.overdraws = overdraws;
	}
	
	public Structure(String name)
	{
		this(name, false);
	}
}
