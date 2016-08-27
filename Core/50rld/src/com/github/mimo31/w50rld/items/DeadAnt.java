package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Dead Ant Item. That is the Item Ant entities drop.
 * @author mimo31
 *
 */
public class DeadAnt extends Item {

	public DeadAnt() {
		super("Dead Ant", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "DeadAnt.png");
	}
}
