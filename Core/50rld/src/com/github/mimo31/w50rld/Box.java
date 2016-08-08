package com.github.mimo31.w50rld;

import java.awt.Graphics2D;
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
	final protected float x;

	// the y coordinate of the box divided by the height of the window
	final protected float y;
	
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
	 * @param keyCode code the key
	 * @param removeAction action that can be run when it's decided to remove the box
	 */
	public void key(int keyCode, Runnable removeAction)
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
}
