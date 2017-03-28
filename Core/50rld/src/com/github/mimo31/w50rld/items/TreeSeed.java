package com.github.mimo31.w50rld.items;

/**
 * Represents the Tree Seed Item. The Item can be used to seed trees.
 * @author mimo31
 *
 */
public class TreeSeed extends SimplyDrawnItem {

	public TreeSeed() {
		super("Tree Seed", "TreeSeed", new ItemAction[] { new SurfacePlaceAction("Seed", "Seeded Tree", "Dirt") });
	}

}
