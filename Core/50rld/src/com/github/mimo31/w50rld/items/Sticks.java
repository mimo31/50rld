package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents a Sticks Item.
 * @author mimo31
 *
 */
public class Sticks extends Item {

	public Sticks() {
		super("Sticks", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Sticks.png");
	}

}
