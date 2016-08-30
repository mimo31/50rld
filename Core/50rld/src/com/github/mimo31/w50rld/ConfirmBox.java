package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.StringDraw.TextAlign;

/**
 * Represents a Box with a questing to with the player can answer yes or no.
 * @author mimo31
 *
 */
public class ConfirmBox extends Box {

	// the question
	private final String request;
	
	// the function to which to submit the player's answer
	private final Consumer<Boolean> submitFunction;
	
	public ConfirmBox(float x, float y, String request, Consumer<Boolean> submitFunction) {
		super(x, y);
		this.request = request;
		this.submitFunction = submitFunction;
	}

	@Override
	public void draw(Graphics2D g, int width, int height) {
		int locX = (int) (super.x * width);
		int locY = (int) (super.y * height);
		
		int borderSize = width / 6 / 64;
		
		int gridSize = width / 36;
		
		int contentWidth = gridSize * 5;
		
		int contentHeight = gridSize;
		
		// fill the border
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, contentWidth + 2 * borderSize, contentHeight + 2 * borderSize);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		
		// draw the headline
		g.setColor(Color.orange);
		Rectangle headlineBounds = new Rectangle(contentX, contentY, contentWidth, gridSize);
		g.fill(headlineBounds);
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, borderSize * 2, this.request, TextAlign.LEFT, headlineBounds);
	}

	@Override
	protected Dimension getSize(int width, int height) {
		int borderSize = width / 6 / 64;
		int gridSize = width / 36;
		return new Dimension(gridSize * 5 + 2 * borderSize, gridSize + 2 * borderSize);
	}

	@Override
	public void key(KeyEvent event, Runnable removeAction)
	{
		int keyCode = event.getKeyCode();
		switch (keyCode)
		{
			// if the Y key or the N key was pressed, submit true or false accordingly
			case KeyEvent.VK_Y:
				this.submitFunction.accept(true);
				removeAction.run();
				break;
			case KeyEvent.VK_N:
				this.submitFunction.accept(false);
				removeAction.run();
				break;
		}
	}
}
