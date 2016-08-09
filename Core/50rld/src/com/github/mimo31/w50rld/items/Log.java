package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents a Log Item.
 * @author mimo31
 *
 */
public class Log extends Item {

	public Log()
	{
		super("Log", new ItemAction[0]);
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Log.png");
	}

}