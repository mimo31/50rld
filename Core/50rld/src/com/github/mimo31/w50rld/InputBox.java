package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.mimo31.w50rld.StringDraw.TextAlign;

/**
 * Represents a Box with a request text and a field for user text input.
 * @author mimo31
 *
 */
public class InputBox extends Box {

	// the request to display to the player
	private final String request;
	
	// input that the user has already typed in
	private String input = "";
	
	// function the is invoked the user submits their input
	private final Consumer<String> submitFunction;
	
	// function that returns whether a character is valid
	private final Function<Integer, Boolean> charFilter;
	
	/**
	 * Creates an InputBox.
	 * @param x x location of the Box divided by the width of the window
	 * @param y y location of the Box divided by the height of the window
	 * @param request text to show to the player
	 * @param submitFunction function that submits user's input
	 * @param charFilter function that decided whether a typed character is valid
	 */
	public InputBox(float x, float y, String request, Consumer<String> submitFunction, Function<Integer, Boolean> charFilter) {
		super(x, y);
		this.request = request;
		this.submitFunction = submitFunction;
		this.charFilter = charFilter;
	}

	@Override
	public void draw(Graphics2D g, int width, int height) {
		int locX = (int) (this.x * width);
		int locY = (int) (this.y * height);
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int boxHeight = (boxWidth / 6 + borderSize) * 2;
		
		// draw the background
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, boxWidth, boxHeight);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		int contentWidth = boxWidth - 2 * borderSize;
		
		Rectangle requestBounds = new Rectangle(contentX, contentY, contentWidth, boxWidth / 6);
		Rectangle inputBounds = new Rectangle(contentX, contentY + boxWidth / 6, contentWidth, boxWidth / 6);
		
		// draw the request background
		g.setColor(Color.orange);
		g.fill(requestBounds);
		
		// draw the input background
		g.setColor(Color.white);
		g.fill(inputBounds);
		
		// draw the request and input texts
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, borderSize * 2, this.request, TextAlign.LEFT, requestBounds);
		StringDraw.drawMaxString(g, borderSize * 2, this.input, TextAlign.LEFT, inputBounds);
	}

	@Override
	protected Dimension getSize(int width, int height) {
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int boxHeight = (boxWidth / 6 + borderSize) * 2;
		
		return new Dimension(boxWidth, boxHeight);
	}

	@Override
	public void key(KeyEvent event, Runnable closeAction)
	{
		int code = event.getKeyCode();
		if (code == KeyEvent.VK_ENTER)
		{
			// submit the input and close
			this.submitFunction.accept(this.input);
			closeAction.run();
			return;
		}
		if (code == KeyEvent.VK_BACK_SPACE)
		{
			// remove the last character from the input (if there are some characters)
			int inputLength = this.input.length();
			if (inputLength == 0)
			{
				return;
			}
			this.input = this.input.substring(0, inputLength - 1);
			return;
		}
		// append the typed character if it isn't filtered out by the filter function
		char keyChar = event.getKeyChar();
		if (this.charFilter.apply(new Integer(keyChar)).booleanValue())
		{
			this.input = this.input + String.valueOf(keyChar);
		}
	}
	
}
