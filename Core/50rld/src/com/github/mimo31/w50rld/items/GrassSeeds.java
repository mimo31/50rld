package com.github.mimo31.w50rld.items;

/**
 * Represents the Grass Seeds Item.
 * @author mimo31
 *
 */
public class GrassSeeds extends SimplyDrawnItem {

	public GrassSeeds()
	{
		super("Grass Seeds", "GrassSeeds.png", new ItemAction[] { new SurfacePlaceAction("Seed", "Seeded Grass", "Dirt") });
	}
	
}
