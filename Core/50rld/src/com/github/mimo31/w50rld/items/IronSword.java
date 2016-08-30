package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.WeaponItem;

/**
 * Represents the Iron Sword Item. The Item can be used as a weapon.
 * @author mimo31
 *
 */
public class IronSword extends Item implements WeaponItem {

	public IronSword() {
		super("Iron Sword", new ItemAction[0]);
	}

	@Override
	public float getHitPower() {
		return 2.5f;
	}

	@Override
	public float getHitRadius() {
		return 3;
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "IronSword.png");
	}

}
