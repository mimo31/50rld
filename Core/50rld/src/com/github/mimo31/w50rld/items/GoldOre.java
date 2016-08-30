package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.Meltable;
import com.github.mimo31.w50rld.Metal;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the mined Gold Ore Item. The Item can be melted into 360 ml of Gold.
 * @author mimo31
 *
 */
public class GoldOre extends Item implements Meltable {

	public GoldOre() {
		super("Gold Ore", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "GoldOreI.png");
	}

	@Override
	public int getVolume() {
		return 360;
	}

	@Override
	public Metal getMetal() {
		return ObjectsIndex.getMetal("Gold");
	}
	
}
