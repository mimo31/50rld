package com.github.mimo31.w50rld;

import java.awt.Point;

/**
 * Represents a Box on the screen.
 * Can handle mouseClicked and keyReleased events.
 * Contains a function that draws the box.
 * @author mimo31
 *
 */
public abstract class Box {

	/**
	 * Distinguishes between the 4 corners of a Box.
	 * Used to select the corner for the align function.
	 * @author mimo31
	 *
	 */
	public enum CornerAlign
	{
		TOPLEFT, TOPRIGHT, BOTTOMRIGHT, BOTTOMLEFT
	}
	
	// the x coordinate of the box divided by the width of the window
	protected float x;

	// the y coordinate of the box divided by the height of the window
	protected float y;
	
	/**
	 * Handler for mouse clicks.
	 * @param location the cursor position relative to the content pane at the moment of the click
	 * @param removeAction action that can be run when it's decided to remove the box
	 * @return whether the box was clicked
	 */
	public boolean mouseClicked(Point location, Runnable removeAction)
	{
		// check if the click location was inside the Box
		
		float clickX = location.x * 2f / Gui.width - 1;
		float clickY = 1 - location.y * 2f / Gui.height;
		
		float width = this.getWidth();
		float height = this.getHeight();
		
		return this.x <= clickX && this.y <= clickY && clickX < this.x + width && clickY < this.y + height;
	}
	
	/**
	 * Handler for key events.
	 * @param keycode the GLFW code of the pressed key
	 * @param removeAction action that can be run when it's decided to remove the box
	 */
	public void keyReleased(int keycode, Runnable removeAction)
	{
		
	}
	
	/**
	 * Draws the Box.
	 */
	public abstract void draw();
	
	public Box(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Moves the Box from the BOTTOMLEFT align to a different align.
	 * Should be called after the constructor is finished.
	 * @param align
	 */
	protected void align(CornerAlign align)
	{
		if (align == CornerAlign.TOPLEFT || align == CornerAlign.TOPRIGHT)
		{
			this.y -= this.getHeight();
		}
		if (align == CornerAlign.TOPRIGHT || align == CornerAlign.BOTTOMRIGHT)
		{
			this.x -= this.getWidth();
		}
	}
	
	/**
	 * @return width of the Box in canvas width
	 */
	protected abstract float getWidth();
	
	/**
	 * @return height of the Box in canvas height
	 */
	protected abstract float getHeight();
	
	/**
	 * Tries to change Box's location, so that it's whole in the window.
	 * @return whether the location was successfully changed, so that it's whole in the window
	 */
	public boolean tryFitWindow()
	{
		float width = this.getWidth();
		float height = this.getHeight();
		
		boolean fitted = true;
		
		// if it exceeds the width of the window, move it, so that it only touches the right edge
		if (this.x + width > 1)
		{
			this.x = 1 - width;
			if (this.x < -1)
			{
				// it now exceeds the left edge, so it's impossible to fit
				fitted = false;
				this.x = 0;
			}
		}
		
		// if the exceeds the height of the window, move it, so that it only touches the bottom edge
		if (this.y + height > 1)
		{
			this.y = 1 - height;
			if (this.y < -1)
			{
				// it now exceeds the top edge, so it's impossible to fit
				fitted = false;
			}
		}
		
		return fitted;
	}
}
