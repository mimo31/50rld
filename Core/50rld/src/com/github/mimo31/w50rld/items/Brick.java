package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Brick Item. The Item is made by drying Wet Bricks.
 * @author mimo31
 *
 */
public class Brick extends Item {

	public Brick() {
		super("Brick", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Brick.png");
	}

}
