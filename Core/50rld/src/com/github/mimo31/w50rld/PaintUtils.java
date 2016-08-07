package com.github.mimo31.w50rld;

import java.awt.Graphics2D;

/**
 * Provides various static methods to draw things that frequently appear in the game.
 * @author mimo31
 *
 */
public class PaintUtils {

	/**
	 * Draws a square texture (typically of a Structure or an Item) of a specified name.
	 * @param g graphics to draw through
	 * @param x x of the rectangle
	 * @param y y of the rectangle
	 * @param width width of the rectangle
	 * @param height height of the rectangle
	 * @param name filename of the image
	 */
	public static void drawSquareTexture(Graphics2D g, int x, int y, int width, int height, String name)
	{
		g.drawImage(ResourceHandler.getImage(name, Math.max(width, height)), x, y, null);
	}
	
}
