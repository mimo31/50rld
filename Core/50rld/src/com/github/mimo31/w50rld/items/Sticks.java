package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.FuelItem;
import com.github.mimo31.w50rld.WeaponItem;

/**
 * Represents a Sticks Item. The Item can be used as a very basic weapon and as fuel.
 * @author mimo31
 *
 */
public class Sticks extends SimplyDrawnItem implements WeaponItem, FuelItem {

	public Sticks() {
		super("Sticks", "Sticks");
	}
	
	@Override
	public float getHitPower() {
		return 1;
	}

	@Override
	public float getHitRadius() {
		return 2;
	}

	@Override
	public int getBurnTime() {
		return 700;
	}

}
