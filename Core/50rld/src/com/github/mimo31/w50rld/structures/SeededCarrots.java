package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.structures.Carrots.CarrotsData;

/**
 * Represents the Seeded Carrots Structure. The Structure grows and eventually replaces itself by the Carrots Structure.
 * @author mimo31
 *
 */
public class SeededCarrots extends SeededPlantStructure {

	public SeededCarrots()
	{
		super("Seeded Carrots", "SeededCarrots.png", "SeededCarrotsGrown.png", 60000, "Carrot Seeds");
	}

	@Override
	protected StructureData createPlantStructureData()
	{
		return new CarrotsData(false);
	}

	
}
