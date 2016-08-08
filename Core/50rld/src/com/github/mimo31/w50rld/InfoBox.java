package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
	public InfoBox(float x, float y, String text) {
		super(x, y);
		this.text = text;
	}

	@Override
	public void draw(Graphics2D g, int width, int height) {
		int locX = (int) (super.x * width);
		int locY = (int) (super.y * height);
		
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int boxHeight = boxWidth / 6 + borderSize * 2;
		
		// draw the background
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, boxWidth, boxHeight);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		
		int contentWidth = boxWidth - 2 * borderSize;
		int contentHeight = boxHeight - 2 * borderSize;
		
		Rectangle textRectangle = new Rectangle(contentX, contentY, contentWidth, contentHeight);
		
		// draw the message background
		g.setColor(Color.white);
		g.fill(textRectangle);
		
		// draw the message text
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, borderSize * 2, this.text, textRectangle);
	}
	
	@Override
	public boolean mouseClicked(MouseEvent event, Runnable removeAction, int width, int height)
	{
		int locX = (int) (super.x * width);
		int locY = (int) (super.y * height);
		
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int boxHeight = boxWidth / 6 + borderSize * 2;
		
		int clickX = event.getX();
		int clickY = event.getY();
		
		// return whether the click was inside the Box
		return clickX >= locX && clickY >= locY && clickX < locX + boxWidth && clickY < locY + boxHeight;
	}
	
	@Override
	protected Dimension getSize(int width, int height)
	{
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int boxHeight = boxWidth / 6 + borderSize * 2;
		
		return new Dimension(boxWidth, boxHeight);
	}
	
	@Override
	public void key(KeyEvent event, Runnable closeAction)
	{
		// close if enter was pressed
		if (event.getKeyCode() == KeyEvent.VK_ENTER)
		{
			closeAction.run();
		}
	}
}
