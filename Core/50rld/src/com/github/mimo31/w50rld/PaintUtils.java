package com.github.mimo31.w50rld;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import com.github.mimo31.w50rld.ResourceHandler.Texture;

/**
 * Provides various static methods to draw things that frequently appear in the game.
 * @author mimo31
 *
 */
public class PaintUtils {

	/**
	 * Draws a texture (typically of a Structure or an Item) of a specified name in a rectangle.
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param endx canvas x location of the top right corner
	 * @param endy canvas y location of the top right corner
	 * @param name name of the Texture
	 */
	public static void drawTexture(float startx, float starty, float endx, float endy, String name)
	{
		glColor3f(1, 1, 1);
		
		drawTextureNSetCol(startx, starty, endx, endy, name);
	}
	
	/**
	 * Draws a texture (typically of a Structure or an Item) of a specified name in a rectangle.
	 * The color behind the texture should be set before calling this function. (useful when the texture should be partially transparent)
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param endx canvas x location of the top right corner
	 * @param endy canvas y location of the top right corner
	 * @param name name of the Texture
	 */
	public static void drawTextureNSetCol(float startx, float starty, float endx, float endy, String name)
	{
		Texture texture = ResourceHandler.getTexture(name);
		
		glEnable(GL_TEXTURE_2D);
		texture.bind();
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(startx, endy);
		
		glTexCoord2f(0, 1);
		glVertex2f(startx, starty);
		
		glTexCoord2f(1, 1);
		glVertex2f(endx, starty);
		
		glTexCoord2f(1, 0);
		glVertex2f(endx, endy);
		glEnd();
		
		texture.unbind();
	}
	
	/**
	 * Draws a filled rectangle.
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param width width of the rectangle in canvas width
	 * @param height height of the rectangle in canvas height
	 */
	public static void drawRectangle(float x, float y, float width, float height)
	{
		float endx = x + width;
		float endy = y + height;
		glBegin(GL_QUADS);
		glVertex2f(x, endy);		
		glVertex2f(x, y);
		glVertex2f(endx, y);
		glVertex2f(endx, endy);
		glEnd();
	}

	/**
	 * Draws a filled rectangle.
	 * @param startx canvas x location of the bottom left corner
	 * @param starty canvas y location of the bottom left corner
	 * @param endx canvas x location of the top right corner
	 * @param endy canvas y location of the top right corner
	 */
	public static void drawRectangleP(float x1, float y1, float x2, float y2)
	{
		glBegin(GL_QUADS);
		glVertex2f(x1, y2);		
		glVertex2f(x1, y1);
		glVertex2f(x2, y1);
		glVertex2f(x2, y2);
		glEnd();
	}
	
	/**
	 * Sets the color in the current context.
	 * @param c color to set
	 */
	public static void setDrawColor(Color c)
	{
		glColor4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	}
	
}
