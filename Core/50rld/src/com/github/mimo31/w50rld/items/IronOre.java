package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Meltable;
import com.github.mimo31.w50rld.Metal;
import com.github.mimo31.w50rld.ObjectsIndex;

/**
 * Represents the mined Iron Ore Item. The Item can be melted into 360 ml of Iron.
 * @author mimo31
 *
 */
public class IronOre extends SimplyDrawnItem implements Meltable {

	public IronOre() {
		super("Iron Ore", "IronOreI");
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
