package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.FuelItem;

/**
 * Represents the mined pile of coal Item. The Item can be used as fuel.
 * @author mimo31
 *
 */
public class Coal extends SimplyDrawnItem implements FuelItem {

	public Coal() {
		super("Coal", "CoalI.png");
	}

	@Override
	public int getBurnTime() {
		return 19200;
	}

}
