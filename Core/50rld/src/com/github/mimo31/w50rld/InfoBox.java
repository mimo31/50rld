package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

/**
 * Represents a Box with text.
 * @author mimo31
 * 
 */
public class InfoBox extends Box {

	// text to draw inside the Box
	final String text;
	
	/**
	 * Creates an InfoBox.
	 * @param x the x location of the Box
	 * @param y the y location of the Box
	 * @param text
	 */
	public InfoBox(float x, float y, String text, CornerAlign align) {
		super(x, y);
		this.text = text;
		super.align(align);
	}

	@Override
	public void draw()
	{
		// box with a 6 x 1 tile grid

		float contentWidth = 6 * Constants.BOX_TILE_SIZE;
				
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + tileHeight;
		
		// draw the background
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		// draw the message background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, tileHeight);
		
		// draw the message text
		TextDraw.drawText(this.text, contentX, contentY, contentWidth, tileHeight, TextAlign.MIDDLE, Constants.BOX_BORDER_SIZE);
	}
	
	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_BORDER_SIZE + 6 * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return (2 * Constants.BOX_BORDER_SIZE + Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}
	
	@Override
	public void keyReleased(int keyCode, Runnable closeAction)
	{
		// close if enter was pressed
		if (keyCode == GLFW_KEY_ENTER || keyCode == GLFW_KEY_KP_ENTER)
		{
			closeAction.run();
		}
	}
}
