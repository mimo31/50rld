package com.github.mimo31.w50rld;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.Color;
import java.awt.Point;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

/**
 * Represents a box with options with associated actions which are, when the associated option is chosen, invoked.
 * @author mimo31
 *
 */
public class OptionBox extends Box {

	// names of the options
	final private String[] options;
	
	// actions to execute when the associated option is chosen
	final private Runnable[] actions;
	
	// the headline of the box
	final private String headline;
	
	// currently selected option
	private int selected = 0;
	
	/**
	 * Creates an OptionBox object.
	 * @param options names of the options
	 * @param actions actions to execute
	 * @param x x coordinate on the canvas
	 * @param y y coordinate on the canvas
	 * @param headline headline for the box
	 */
	public OptionBox(String[] options, Runnable[] actions, float x, float y, String headline, CornerAlign align)
	{
		super(x, y);
		this.options = options;
		this.actions = actions;
		this.headline = headline;
		super.align(align);
	}
	
	/**
	 * Draws the OptionBox.
	 * @param g graphics to draw through
	 * @param width width of the window
	 * @param height height of the window
	 */
	@Override
	public void draw()
	{
		float contentWidth = 6 * Constants.BOX_TILE_SIZE;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = (this.options.length + 1) * tileHeight;
		
		float boxWidth = contentWidth + 2 * Constants.BOX_BORDER_SIZE;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = contentHeight + 2 * borderHeight;
		
		// fill the whole box (only the border will remain from this fill)
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		// coordinates of the box's content (the box without the borders)
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		// fill the box's content rectangle
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, contentHeight);
		
		// draw the headline and the option names
		for (int i = 0; i < this.options.length + 1; i++)
		{
			// rectangle enclosing the current option name / headline
			float drawY = contentY + (this.options.length - i) * tileHeight;
			
			// text to be drawn - option name / headline
			String optionText;
			if (i == 0)
			{
				// fill the headline's background
				PaintUtils.setDrawColor(Color.ORANGE);
				PaintUtils.drawRectangle(contentX, drawY, contentWidth, tileHeight);
				
				optionText = this.headline;
			}
			else
			{
				optionText = this.options[i - 1];
			}
			
			// draw the option name / headline
			TextDraw.drawText(optionText, contentX, drawY, contentWidth, tileHeight, i == 0 ? TextAlign.LEFT : TextAlign.MIDDLE, Constants.BOX_BORDER_SIZE);
			
			// draw the indicator that the current option is selected
			if (this.selected == i - 1)
			{
				glColor3f(0, 0, 1);
				PaintUtils.drawRectangle(contentX + contentWidth / 4, drawY, contentWidth / 2, Constants.BOX_BORDER_SIZE);
			}
		}
	}
	
	/**
	 * Handles mouse clicks. Should be called on every click - it will resolve clicks out of the box automatically.
	 * @param event MouseEvent
	 * @param removeAction action to invoke when an option is chosen and the box should be closed.
	 * @param width width of the window
	 * @param height height of the window
	 * @return true if the box was clicked, else false
	 */
	@Override
	public boolean mouseClicked(Point location, Runnable removeAction)
	{
		float clickX = location.x * 2f / Gui.width - 1;
		float clickY = 1 - location.y * 2f / Gui.height;
		
		float contentWidth = 6 * Constants.BOX_TILE_SIZE;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = (this.options.length + 1) * tileHeight;
		
		float boxWidth = contentWidth + 2 * Constants.BOX_BORDER_SIZE;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = contentHeight + 2 * borderHeight;
		
		// check if the click was inside the box
		if (clickX >= this.x && clickY >= this.y && clickX < this.x + boxWidth && clickY < this.y + boxHeight)
		{
			// check if the click was in the content (inside the borders)
			if (clickX >= this.x + Constants.BOX_BORDER_SIZE && clickY >= this.y + borderHeight && clickX < this.x + boxWidth - Constants.BOX_BORDER_SIZE && clickY < this.y + boxHeight - borderHeight)
			{
				// click's y location relative to the top edge of the content rectangle
				float contentClickY = clickY - this.y - borderHeight;
				
				int invertedOption = (int)Math.floor(contentClickY / tileHeight);
				
				// check if the headline was not clicked
				if (invertedOption < this.options.length)
				{
					// invoke the corresponding action
					this.actions[this.options.length - 1 - invertedOption].run();
					
					// remove the box
					removeAction.run();
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void keyReleased(int keyCode, Runnable removeAction)
	{
		switch (keyCode)
		{
			// move the selection up
			case GLFW_KEY_W:
			case GLFW_KEY_UP:
				if (this.selected != 0)
				{
					this.selected--;
				}
				break;
				
			// move the selection down
			case GLFW_KEY_S:
			case GLFW_KEY_DOWN:
				if (this.selected != this.options.length - 1)
				{
					this.selected++;
				}
				break;
				
			// confirm the selection
			case GLFW_KEY_ENTER:
			case GLFW_KEY_KP_ENTER:
				if (this.actions.length != 0)
				{
					this.actions[this.selected].run();
				}
				removeAction.run();
				break;
		}
	}
	
	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_BORDER_SIZE + 6 * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return (2 * Constants.BOX_BORDER_SIZE + (this.options.length + 1) * Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}
}
