package com.github.mimo31.w50rld.items;

/**
 * Represents the Corn Seeds Item. The Item can be seeded to grow corn.
 * @author mimo31
 *
 */
public class CornSeeds extends SimplyDrawnItem {

	public CornSeeds() {
		super("Corn Seeds", "CornSeeds.png", new ItemAction[] { new SurfacePlaceAction("Seed", "Seeded Corn", "Dirt") });
	}

}
