package com.github.mimo31.w50rld.biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.mimo31.w50rld.Biome;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;

/**
 * Represents the Desert Biome - each Tile contains two Sand Structures.
 * @author mimo31
 *
 */
public class Desert extends Biome {

	public Desert() {
		super("Desert");
	}

	@Override
	public List<StructureData> getTileStructures(double biomeDepth, Function<Byte, Byte> getSmallStructureData, double[] mediumStructureNoises) {
		List<StructureData> structures = new ArrayList<StructureData>();
		Structure sandStructure = ObjectsIndex.getStructure("Sand");
		structures.add(sandStructure.createStructureData());
		structures.add(sandStructure.createStructureData());
		return structures;
	}

}
