package com.github.mimo31.w50rld.biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.mimo31.w50rld.Biome;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;

/**
 * Represents the Forest Biome - each Tile contains two Dirt Structures and a Grass or a Bush or a Tree.
 * @author mimo31
 *
 */
public class Forest extends Biome {

	public Forest() {
		super("Forest");
	}

	@Override
	public List<StructureData> getTileStructures(double biomeDepth, Function<Byte, Byte> getSmallStructureData, double[] mediumStructureNoises) {
		List<StructureData> structures = new ArrayList<StructureData>();
		Structure dirtStructure = ObjectsIndex.getStructure("Dirt");
		structures.add(dirtStructure.createStructureData());
		structures.add(dirtStructure.createStructureData());
		structures.add(ObjectsIndex.getStructure("Grass").createStructureData());
		
		// scale the depth to make the work with it easier
		biomeDepth *= 3;
		
		// calculate the probabilities of grass or bush appearing
		int grassP = biomeDepth > 0.5 ? 0 : (int)((1 - Math.pow(biomeDepth * 2, 1 / 3d)) * 256);
		int bushP = biomeDepth > 0.75 ? 0 : (int)((1 - biomeDepth * 4 / 3) * 256);
		
		// get the small structure data
		int strData = getSmallStructureData.apply(new Byte((byte) 0)).byteValue() + 128;
		
		// decide which structure will be placed on this Tile
		if (strData >= grassP && strData < bushP)
		{
			structures.add(ObjectsIndex.getStructure("Bush").createStructureData());
		}
		else if (strData >= bushP)
		{
			structures.add(ObjectsIndex.getStructure("Tree").createStructureData());
		}
		return structures;
	}

}
