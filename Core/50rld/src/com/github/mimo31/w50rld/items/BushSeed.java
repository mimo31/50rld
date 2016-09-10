package com.github.mimo31.w50rld.items;

/**
 * Represents the Bush Seed Item. The Item can be used to seed Bushes.
 * @author mimo31
 *
 */
public class BushSeed extends SimplyDrawnItem {

	public BushSeed() {
		super("Bush Seed", "BushSeed.png", new ItemAction[] { new SurfacePlaceAction("Seed", "Seeded Bush", "Dirt") });
	}

}
