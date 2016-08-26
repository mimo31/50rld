package com.github.mimo31.w50rld.biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.mimo31.w50rld.Biome;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;

/**
 * Represents the Lake Biome - each Tile contains two Water Structures.
 * @author mimo31
 *
 */
public class Lake extends Biome {

	@Override
	public List<StructureData> getTileStructures(double biomeDepth, Function<Byte, Byte> getSmallStructureData, double[] mediumStructureNoises) {
		List<StructureData> structures = new ArrayList<StructureData>();
		Structure waterStructure = ObjectsIndex.getStructure("Water");
		structures.add(waterStructure.createStructureData());
		structures.add(waterStructure.createStructureData());
		return structures;
	}

}
