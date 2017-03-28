package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Meltable;
import com.github.mimo31.w50rld.Metal;
import com.github.mimo31.w50rld.ObjectsIndex;

/**
 * Represents the mined Gold Ore Item. The Item can be melted into 360 ml of Gold.
 * @author mimo31
 *
 */
public class GoldOre extends SimplyDrawnItem implements Meltable {

	public GoldOre() {
		super("Gold Ore", "GoldOreI");
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
