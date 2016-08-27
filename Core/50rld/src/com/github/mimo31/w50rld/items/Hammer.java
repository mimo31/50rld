package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.WeaponItem;

/**
 * Represents the Hammer Item. The Item can be used as a basic weapon.
 * @author mimo31
 *
 */
public class Hammer extends Item implements WeaponItem {

	public Hammer()
	{
		super("Hammer", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Hammer.png");
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
