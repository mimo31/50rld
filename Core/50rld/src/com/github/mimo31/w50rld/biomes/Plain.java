package com.github.mimo31.w50rld.biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.mimo31.w50rld.Biome;
import com.github.mimo31.w50rld.Constants;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;

/**
 * Represents the Plain Biome - each Tile contains two Dirt Structures, Grass and a Bush or a vegetable plant.
 * @author mimo31
 *
 */
public class Plain extends Biome {

	public Plain() {
		super("Plain");
	}

	@Override
	public List<StructureData> getTileStructures(double biomeDepth, Function<Byte, Byte> getSmallStructureData, double[] mediumStructureNoises) {
		List<StructureData> structures = new ArrayList<StructureData>();
		Structure dirtStructure = ObjectsIndex.getStructure("Dirt");
		structures.add(dirtStructure.createStructureData());
		structures.add(dirtStructure.createStructureData());
		structures.add(ObjectsIndex.getStructure("Grass").createStructureData());
		if (getSmallStructureData.apply(new Byte((byte) 0)).byteValue() + 128 < Constants.BUSH_IN_GRASS_PROB * 256)
		{
			structures.add(ObjectsIndex.getStructure("Bush").createStructureData());
		}
		else if (getSmallStructureData.apply((byte)1) == -128 && getSmallStructureData.apply((byte)2) < -120)
		{
			// place Carrots
			structures.add(ObjectsIndex.getStructure("Carrots").createStructureData());
		}
		else if (getSmallStructureData.apply((byte)3) == -128 && getSmallStructureData.apply((byte)4) < -120)
		{
			// place Corn
			structures.add(ObjectsIndex.getStructure("Corn").createStructureData());
		}
		return structures;
	}

}
