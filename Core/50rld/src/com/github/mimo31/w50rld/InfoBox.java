package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

/**
 * Represents a Box with text.
 * @author mimo31
 * 
 */
public class InfoBox extends Box {

	// text to draw inside the Box
	final String text;
	
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
		
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, boxWidth, boxHeight);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		
		int contentWidth = boxWidth - 2 * borderSize;
		int contentHeight = boxHeight - 2 * borderSize;
		
		Rectangle textRectangle = new Rectangle(contentX, contentY, contentWidth, contentHeight);
		
		g.setColor(Color.white);
		g.fill(textRectangle);
		
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
		
		return clickX >= locX && clickY >= locY && clickX < locX + boxWidth && clickY < locY + boxHeight;
	}
}
