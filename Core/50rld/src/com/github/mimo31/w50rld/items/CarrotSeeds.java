package com.github.mimo31.w50rld.items;

/**
 * Represents the Carrot Seeds Item. The Item can be seeded to grow carrots.
 * @author mimo31
 *
 */
public class CarrotSeeds extends SimplyDrawnItem {

	public CarrotSeeds() {
		super("Carrot Seeds", "CarrotSeeds.png", new ItemAction[] { new SurfacePlaceAction("Seed", "Seeded Carrots", "Dirt") });
	}

}
