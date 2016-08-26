package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Wet Brick Item. The Item can be obtained by combining a Clay item and a Brick Form item.
 * @author mimo31
 *
 */
public class WetBrick extends Item {

	public WetBrick() {
		super("Wet Brick", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "WetBrick.png");
	}

}
