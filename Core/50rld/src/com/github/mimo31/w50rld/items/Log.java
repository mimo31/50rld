package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.FuelItem;
import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents a Log Item. The Item can be used as fuel.
 * @author mimo31
 *
 */
public class Log extends Item implements FuelItem {

	public Log()
	{
		super("Log", new ItemAction[0]);
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Log.png");
	}

	@Override
	public int getBurnTime() {
		return 6400;
	}

}