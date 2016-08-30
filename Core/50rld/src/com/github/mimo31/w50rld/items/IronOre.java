package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.Meltable;
import com.github.mimo31.w50rld.Metal;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the mined Iron Ore Item. The Item can be melted into 360 ml of Iron.
 * @author mimo31
 *
 */
public class IronOre extends Item implements Meltable {

	public IronOre() {
		super("Iron Ore", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "IronOreI.png");
	}

	@Override
	public int getVolume() {
		return 360;
	}

	@Override
	public Metal getMetal() {
		return ObjectsIndex.getMetal("Iron");
	}
	
}
