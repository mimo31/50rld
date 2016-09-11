package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Meal;

/**
 * Represents the Corn Item. The Item can be eaten to gain one hp.
 * @author mimo31
 *
 */
public class Corn extends SimplyDrawnItem implements Meal {

	public Corn() {
		super("Corn", "CornI.png");
	}

	@Override
	public int healthGain() {
		return 1;
	}

}
