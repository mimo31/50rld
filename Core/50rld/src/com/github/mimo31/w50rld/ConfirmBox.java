package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.Color;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

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
	public void draw() {
		// box with a 5 x 1 tile grid
		float contentWidth = 5 * Constants.BOX_TILE_SIZE;
		
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = tileHeight;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + contentHeight;
		
		// fill the border
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		// draw the headline
		PaintUtils.setDrawColor(Color.orange);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, contentHeight);
		TextDraw.drawText(this.request, contentX, contentY, contentWidth, contentHeight, TextAlign.LEFT, Constants.BOX_BORDER_SIZE);
	}

	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_BORDER_SIZE + 5 * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return (2 * Constants.BOX_BORDER_SIZE + Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}

	@Override
	public void keyReleased(int keyCode, Runnable removeAction)
	{
		switch (keyCode)
		{
			// if the Y key or the N key was pressed, submit true or false accordingly
			case GLFW_KEY_Y:
				this.submitFunction.accept(true);
				removeAction.run();
				break;
			case GLFW_KEY_N:
				this.submitFunction.accept(false);
				removeAction.run();
				break;
		}
	}
}
