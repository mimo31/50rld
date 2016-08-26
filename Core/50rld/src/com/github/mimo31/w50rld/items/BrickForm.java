package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Brick Form Item. The Item is used to form Bricks.
 * @author mimo31
 *
 */
public class BrickForm extends Item {

	public BrickForm() {
		super("Brick Form", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "BrickForm.png");
	}

}
