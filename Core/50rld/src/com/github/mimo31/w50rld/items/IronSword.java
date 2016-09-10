package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.WeaponItem;

/**
 * Represents the Iron Sword Item. The Item can be used as a weapon.
 * @author mimo31
 *
 */
public class IronSword extends SimplyDrawnItem implements WeaponItem {

	public IronSword() {
		super("Iron Sword", "IronSword.png");
	}

	@Override
	public float getHitPower() {
		return 2.5f;
	}

	@Override
	public float getHitRadius() {
		return 3;
	}

}
