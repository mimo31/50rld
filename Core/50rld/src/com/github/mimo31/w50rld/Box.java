package com.github.mimo31.w50rld;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Represents a Box on the screen.
 * Can handle mouseClicked and key events.
 * Contains a function that draws the box.
 * @author mimo31
 *
 */
public abstract class Box {

	// the x coordinate of the box divided by the width of the window
	protected float x;

	// the y coordinate of the box divided by the height of the window
	protected float y;
	
	/**
	 * Handler for mouse clicks.
	 * @param event mouseEvent
	 * @param removeAction action that can be run when it's decided to remove the box
	 * @param width width of the window
	 * @param height height of the window
	 * @return whether the box was clicked
	 */
	public boolean mouseClicked(MouseEvent event, Runnable removeAction, int width, int height)
	{
		return false;
	}
	
	/**
	 * Handler for key events.
	 * @param event KeyEvent
	 * @param removeAction action that can be run when it's decided to remove the box
	 */
	public void key(KeyEvent event, Runnable removeAction)
	{
		
	}
	
	/**
	 * Draw the Box.
	 * @param g graphics to draw through.
	 * @param width width of the window
	 * @param height height of the window
	 */
	public abstract void draw(Graphics2D g, int width, int height);
	
	public Box(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the size of the Box.
	 * @param width width of the window
	 * @param height height of the window
	 * @return size of the Box
	 */
	protected abstract Dimension getSize(int width, int height);
	
	/**
	 * Tries to change Box's location, so that it's whole in the window.
	 * @param width width of the window
	 * @param height height of the window
	 * @return whether the location was successfully changed, so that it's whole in the window
	 */
	public boolean tryFitWindow(int width, int height)
	{
		int locX = (int) (this.x * width);
		int locY = (int) (this.y * height);
		
		Dimension boxSize = this.getSize(width, height);
		
		boolean fitted = true;
		
		// if it exceeds the width of the window, move it, so that it only touches the right edge
		if (locX + boxSize.width > width)
		{
			locX = width - boxSize.width;
			if (locX < 0)
			{
				// it now exceeds the left edge, so it's impossible to fit
				fitted = false;
				this.x = 0;
			}
			else
			{
				this.x = locX / (float) width;
			}
		}
		
		// if the exceeds the height of the window, move it, so that it only touches the bottom edge
		if (locY + boxSize.height > height)
		{
			locY = height - boxSize.height;
			if (locY < 0)
			{
				// it now exceeds the top edge, so it's impossible to fit
				fitted = false;
				this.y = 0;
			}
			else
			{
				this.y = locY / (float) height;
			}
		}
		
		return fitted;
	}
}
