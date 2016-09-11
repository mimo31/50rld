package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Meal;

/**
 * Represents the Carrot Item. The Item can be eaten to gain one hp.
 * @author mimo31
 *
 */
public class Carrot extends SimplyDrawnItem implements Meal {

	public Carrot() {
		super("Carrot", "Carrot.png");
	}

	@Override
	public int healthGain() {
		return 1;
	}

}
