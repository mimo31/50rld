package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Blade Mold Item. The Item is used as mold when casting blades in the Melting Furnace.
 * @author mimo31
 *
 */
public class BladeMold extends Item {

	public BladeMold() {
		super("Blade Mold", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "BladeMold.png");
	}

}
