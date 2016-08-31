package com.github.mimo31.w50rld.biomes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.github.mimo31.w50rld.Biome;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.StructureData;

/**
 * Represents the Death Biome where creatures can spawn.
 * @author mimo31
 *
 */
public class Death extends Biome {

	public Death()
	{
		// one medium structure noise for Clay generation
		super(128, new int[] { 8 }, 0.8, "Death");
	}
	
	@Override
	public List<StructureData> getTileStructures(double biomeDepth, Function<Byte, Byte> getSmallStructureData, double[] mediumStructureNoises) {
		List<StructureData> structures = new ArrayList<StructureData>();
		if (mediumStructureNoises[0] > 0.67)
		{
			structures.add(ObjectsIndex.getStructure("Clay").createStructureData());
		}
		else
		{
			structures.add(ObjectsIndex.getStructure("Death Ground").createStructureData());
		}
		return structures;
	}

}
