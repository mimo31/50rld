package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents a Grass Pile Item.
 * @author mimo31
 *
 */
public class GrassPile extends Item {

	public GrassPile()
	{
		super("Grass Pile", new ItemAction[0]);
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "GrassPile.png");
	}

}
