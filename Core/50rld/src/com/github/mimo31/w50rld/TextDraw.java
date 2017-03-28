package com.github.mimo31.w50rld;

import static org.lwjgl.opengl.GL11.*;

import com.github.mimo31.w50rld.ResourceHandler.Texture;

/**
 * Class for drawing text in rectangles using the custom font loaded a texture.
 * @author mimo31
 *
 */
public class TextDraw {

	// texture of the font
	// it is grid with width 12 chars and height 8 chars
	// each char has width of 32 pixels, height 96 pixels
	private static Texture fontAtlas;
	
	/**
	 * Distinguishes the possible aligns of text in a rectangle.
	 * @author mimo31
	 *
	 */
	public enum TextAlign
	{
		MIDDLE, UP, UPRIGHT, RIGHT, DOWNRIGHT, DOWN, DOWNLEFT, LEFT, UPLEFT
	}
	
	/**
	 * Prepares the font texture. Should be call when initializing.
	 */
	public static void loadFont()
	{
		fontAtlas = ResourceHandler.getTexture("FontAtlas");
	}
	
	/**
	 * Draws a letter from the font atlas. GL_TEXTURE_2D must be enabled, the font atlas texture binded and white color set.
	 * @param c char to draw
	 * @param x canvas x location of the bottom left corner
	 * @param y canvas y location of the bottom left corner
	 * @param width width of the drawn character in canvas width
	 * @param height height of the drawn character in canvas height
	 */
	private static void drawLetter(char c, float x, float y, float width, float height)
	{
		// check, if the character is in the valid range (range of what is in the font)
		if (c < ' ' || c > '~')
		{
			c = 127;
		}
		
		int charnum = c - ' ';
		
		// location of the glyph in the font atlas
		float texturex = (charnum % 12) / 12f + 1 / 12f / 32f / 2;
		float texturey = ((charnum / 12) / 8f) + 1 / 8f / 96f / 2;
		
		// draw the glyph
		glBegin(GL_QUADS);
		glTexCoord2f(texturex, texturey);
		glVertex2f(x, y + height);
		
		glTexCoord2f(texturex + 1 / 12f - 1 / 12f / 32f, texturey);
		glVertex2f(x + width, y + height);
		
		glTexCoord2f(texturex + 1 / 12f - 1 / 12f / 32f, texturey + 1 / 8f - 1 / 8f / 96f);
		glVertex2f(x + width, y);
		
		glTexCoord2f(texturex, texturey + 1 / 8f - 1 / 8f / 96f);
		glVertex2f(x, y);
		glEnd();
	}
	
	/**
	 * Draws the text with the largest possible size in a specified rectangle.
	 * @param s text to draw
	 * @param x x location of the rectangle in canvas coordinates
	 * @param y y location of the rectangle in canvas coordinates
	 * @param width width of the rectangle in canvas coordinates
	 * @param height height of the rectangle in canvas coordinates
	 * @param align align of the text in the rectangle
	 * @param borderSize size of the border in canvas coordinates
	 */
	public static void drawText(String s, float x, float y, float width, float height, TextAlign align, float borderSize)
	{
		// subtract the border from the rectangle
		x += borderSize;
		y += borderSize;
		width -= 2 * borderSize;
		height -= 2 * borderSize;
		
		// size of the rectangle on the screen
		float screenwidth = width * Gui.width / 2;
		float screenheight = height * Gui.height / 2;
		
		// length of the text
		int len = s.length();
		
		// width of a character in canvas width
		float charwidth;
		if (screenwidth / screenheight > len / 2f)
		{
			charwidth = screenheight / 2 / Gui.width * 2;
		}
		else
		{
			charwidth = screenwidth / len / Gui.width * 2;
		}
		
		// height of a character in canvas height
		float charheight = charwidth * 2 * Gui.width / Gui.height;
		
		// x and y coordinates in various aligns
		final float up = y + height - charheight;
		final float down = y;
		final float left = x;
		final float right = x + width - len * charwidth;
		final float middlex = x + (width - len * charwidth) / 2;
		final float middley = y + (height - charheight) / 2;
		
		// x and y coordinates for the selected align
		float drawx = 0;
		float drawy = 0;
		switch (align)
		{
		case MIDDLE:
			drawx = middlex;
			drawy = middley;
			break;
		case UP:
			drawx = middlex;
			drawy = up;
			break;
		case UPRIGHT:
			drawx = right;
			drawy = up;
			break;
		case RIGHT:
			drawx = right;
			drawy = middley;
			break;
		case DOWNRIGHT:
			drawx = right;
			drawy = down;
			break;
		case DOWN:
			drawx = middlex;
			drawy = down;
			break;
		case DOWNLEFT:
			drawx = left;
			drawy = down;
			break;
		case LEFT:
			drawx = left;
			drawy = middley;
			break;
		case UPLEFT:
			drawx = left;
			drawy = up;
			break;
		}
		
		// draw all the characters
		glColor3f(1, 1, 1);
		glEnable(GL_TEXTURE_2D);
		fontAtlas.bind();
		for (int i = 0; i < len; i++)
		{
			drawLetter(s.charAt(i), drawx + i * charwidth + charwidth / 6, drawy, charwidth * 2 / 3, charheight);
		}
		fontAtlas.unbind();
		glDisable(GL_TEXTURE_2D);
	}
}
