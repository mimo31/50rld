package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.structures.Tree.TreeData;

/**
 * Represents the Seeded Tree Structure. The Structure grows until it replaces itself with a Tree Structure.
 * @author mimo31
 *
 */
public class SeededTree extends SeededPlantStructure {

	public SeededTree() {
		super("Seeded Tree", "SeededTree", "SeededTreeGrown", 60000, "Tree Seed");
	}

	@Override
	protected StructureData createPlantStructureData() {
		return new TreeData(false);
	}

	
}
