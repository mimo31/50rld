package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.WeaponItem;

/**
 * Represents the Hammer Item. The Item can be used as a basic weapon.
 * @author mimo31
 *
 */
public class Hammer extends SimplyDrawnItem implements WeaponItem {

	public Hammer()
	{
		super("Hammer", "Hammer.png");
	}

	@Override
	public float getHitPower() {
		return 1.5f;
	}

	@Override
	public float getHitRadius() {
		return 2;
	}
	
}
