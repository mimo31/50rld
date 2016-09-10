package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.FuelItem;

/**
 * Represents a Log Item. The Item can be used as fuel.
 * @author mimo31
 *
 */
public class Log extends SimplyDrawnItem implements FuelItem {

	public Log()
	{
		super("Log", "Log.png");
	}

	@Override
	public int getBurnTime() {
		return 6400;
	}

}