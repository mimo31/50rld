package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.structures.Corn.CornData;

/**
 * Represents the Seeded Corn Structure. The Structure grows and eventually replaces itself by the Corn Structure.
 * @author mimo31
 *
 */
public class SeededCorn extends SeededPlantStructure {

	public SeededCorn() {
		super("Seeded Corn", "SeededCorn", "SeededCornGrown", 60000, "Corn Seeds");
	}

	@Override
	protected StructureData createPlantStructureData() {
		return new CornData(false);
	}

}
