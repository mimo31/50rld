package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.FuelItem;
import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the mined pile of coal Item. The Item can be used as fuel.
 * @author mimo31
 *
 */
public class Coal extends Item implements FuelItem {

	public Coal() {
		super("Coal", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "CoalI.png");
	}

	@Override
	public int getBurnTime() {
		return 19200;
	}

}
