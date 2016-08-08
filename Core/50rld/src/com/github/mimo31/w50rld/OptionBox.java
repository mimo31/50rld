package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import com.github.mimo31.w50rld.StringDraw.TextAlign;

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
	 * @param x x coordinate divided by the width of the window
	 * @param y y coordinate divided by the height of the window
	 * @param headline headline for the box
	 */
	public OptionBox(String[] options, Runnable[] actions, float x, float y, String headline)
	{
		super(x, y);
		this.options = options;
		this.actions = actions;
		this.headline = headline;
	}
	
	/**
	 * Draws the OptionBox.
	 * @param g graphics to draw through
	 * @param width width of the window
	 * @param height height of the window
	 */
	@Override
	public void draw(Graphics2D g, int width, int height)
	{
		int locX = (int) (this.x * width);
		int locY = (int) (this.y * height);
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int optionHeight = boxWidth / 6;
		
		int boxHeight = (this.options.length + 1) * optionHeight + 2 * borderSize;
		
		// fill the whole box (only the border will remain from this fill)
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, boxWidth, boxHeight);
		
		// coordinates of the box's content (the box without the borders)
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		int contentWidth = boxWidth - 2 * borderSize;
		int contentHeight = boxHeight - 2 * borderSize;
		
		// fill the box's content rectangle
		g.setColor(Color.white);
		g.fillRect(contentX, contentY, contentWidth, contentHeight);
		
		// draw the headline and the option names
		for (int i = 0; i < this.options.length + 1; i++)
		{
			// rectangle enclosing the current option name / headline
			Rectangle optionRect = new Rectangle(contentX, contentY + i * optionHeight, contentWidth, optionHeight);
			
			// text to be drawn - option name / headline
			String optionText;
			if (i == 0)
			{
				// fill the headline's background
				g.setColor(Color.orange);
				g.fill(optionRect);
				
				optionText = this.headline;
				
				// set the graphics's color to draw the option names correctly
				g.setColor(Color.black);
			}
			else
			{
				optionText = this.options[i - 1];
			}
			
			// draw the option name / headline
			StringDraw.drawMaxString(g, 2 * borderSize, optionText, i == 0 ? TextAlign.LEFT : TextAlign.MIDDLE, optionRect, i == 0 ? Font.BOLD: Font.PLAIN);
			
			// draw the indicator that the current option is selected
			if (this.selected == i - 1)
			{
				g.setColor(Color.blue);
				g.fillRect(optionRect.x + optionRect.width / 4, optionRect.y + optionRect.height - borderSize, optionRect.width / 2, borderSize);
				g.setColor(Color.black);
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
	public boolean mouseClicked(MouseEvent event, Runnable removeAction, int width, int height)
	{
		int clickX = event.getX();
		int clickY = event.getY();
		
		int locX = (int) (this.x * width);
		int locY = (int) (this.y * height);
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int optionHeight = boxWidth / 6;
		
		int boxHeight = (this.options.length + 1) * optionHeight + 2 * borderSize;
		
		// check if the click was inside the box
		if (clickX >= locX && clickY >= locY && clickX < locX + boxWidth && clickY < locY + boxHeight)
		{
			// check if the click was in the content (inside the borders)
			if (clickX >= locX + borderSize && clickY >= locY + borderSize && clickX < locX + boxWidth - borderSize && clickY < locY + boxHeight - borderSize)
			{
				// click's y location relative to the top edge of the content rectangle
				int contentClickY = (clickY - locY - borderSize - optionHeight);
				
				// check if the headline was not clicked
				if (contentClickY >= 0)
				{
					// calculate the option number that was clicked
					int option = contentClickY / optionHeight;
					
					// invoke the corresponding action
					this.actions[option].run();
					
					// remove the box
					removeAction.run();
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void key(int keyCode, Runnable removeAction)
	{
		switch (keyCode)
		{
			// move the selection up
			case KeyEvent.VK_W:
				if (this.selected != 0)
				{
					this.selected--;
				}
				break;
				
			// move the selection down
			case KeyEvent.VK_S:
				if (this.selected != this.options.length)
				{
					this.selected++;
				}
				break;
				
			// confirm the selection
			case KeyEvent.VK_ENTER:
				this.actions[this.selected].run();
				removeAction.run();
				break;
		}
	}
}
