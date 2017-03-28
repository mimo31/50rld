package com.github.mimo31.w50rld;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;

import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

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
	
	// function that creates characters from pressed keycodes, returns 0 if the key should not be accepted at all
	private final Function<Integer, Integer> charInterpreter;
	
	/**
	 * Character Filter that allows only digits.
	 */
	public static final Function<Integer, Integer> DIGIT_INTERPRETER = (code) -> {
		int val = code.intValue();
		if (val <= GLFW_KEY_KP_9 && val >= GLFW_KEY_KP_0)
		{
			return new Integer('0' + val - GLFW_KEY_KP_0);
		}
		else if (val <= GLFW_KEY_9 && val >= GLFW_KEY_0)
		{
			return new Integer('O' + val - GLFW_KEY_0);
		}
		return new Integer(0);
	};
	
	/**
	 * Creates an InputBox.
	 * @param x x location of the Box divided by the width of the window
	 * @param y y location of the Box divided by the height of the window
	 * @param request text to show to the player
	 * @param submitFunction function that submits user's input
	 * @param charFilter function that decided whether a typed character is valid
	 */
	public InputBox(float x, float y, String request, Consumer<String> submitFunction, Function<Integer, Integer> charInterpreter, CornerAlign align) {
		super(x, y);
		this.request = request;
		this.submitFunction = submitFunction;
		this.charInterpreter = charInterpreter;
		super.align(align);
	}

	@Override
	public void draw() {
		// box with a 6 x 2 tile grid

		float contentWidth = 6 * Constants.BOX_TILE_SIZE;
		
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = 2 * tileHeight;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + contentHeight;
		
		// draw the background
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		// draw the request background
		PaintUtils.setDrawColor(Color.orange);
		PaintUtils.drawRectangle(contentX, contentY + tileHeight, contentWidth, tileHeight);
		
		// draw the input background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, tileHeight);
		
		// draw the request and input texts
		TextDraw.drawText(this.request, contentX, contentY + tileHeight, contentWidth, tileHeight, TextAlign.LEFT, Constants.BOX_BORDER_SIZE);
		TextDraw.drawText(this.input, contentX, contentY, contentWidth, tileHeight, TextAlign.LEFT, Constants.BOX_BORDER_SIZE);
	}

	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_BORDER_SIZE + 6 * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return 2 * (Constants.BOX_BORDER_SIZE + Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}
	
	@Override
	public void keyReleased(int keyCode, Runnable closeAction)
	{
		if (keyCode == GLFW_KEY_ENTER || keyCode == GLFW_KEY_KP_ENTER)
		{
			// submit the input and close
			this.submitFunction.accept(this.input);
			closeAction.run();
			return;
		}
		if (keyCode == GLFW_KEY_BACKSPACE)
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
		// interpret the pressed key
		int c = this.charInterpreter.apply(new Integer(keyCode));
		if (c != 0)
		{
			this.input = this.input + ((char) c);
		}
	}
}
