package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.structures.Bush.BushData;

/**
 * Represents the Seeded Bush Structure. The Structure grows until it replaces itself with a Bush Structure.
 * @author mimo31
 *
 */
public class SeededBush extends SeededPlantStructure {

	public SeededBush() {
		super("Seeded Bush", "SeededBush.png", "SeededBushGrown.png", 60000, "Bush Seed");
	}

	@Override
	protected StructureData createPlantStructureData() {
		return new BushData(false);
	}

	
}
