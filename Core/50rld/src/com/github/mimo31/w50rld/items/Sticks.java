package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.FuelItem;
import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.WeaponItem;

/**
 * Represents a Sticks Item. The Item can be used as a very basic weapon and as fuel.
 * @author mimo31
 *
 */
public class Sticks extends Item implements WeaponItem, FuelItem {

	public Sticks() {
		super("Sticks", new ItemAction[0]);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Sticks.png");
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
