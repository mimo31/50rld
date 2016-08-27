package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Furnace Wall Item. The Item is an important component of furnaces.
 * @author mimo31
 *
 */
public class FurnaceWall extends Item {

	public FurnaceWall() {
		super("Furnace Wall", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "FurnaceWall.png");
	}

}
