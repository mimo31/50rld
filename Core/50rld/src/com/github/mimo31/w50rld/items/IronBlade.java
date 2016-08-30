package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Iron Blade Item. The Item can be used to make the Iron Sword.
 * @author mimo31
 *
 */
public class IronBlade extends Item {

	public IronBlade() {
		super("Iron Blade", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "IronBlade.png");
	}

	
}
