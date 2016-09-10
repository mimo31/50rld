package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;

/**
 * Represents the Grass Seeds Item.
 * @author mimo31
 *
 */
public class GrassSeeds extends SimplyDrawnItem {

	public GrassSeeds()
	{
		super("Grass Seeds", "GrassSeeds.png", new ItemAction[]
		{
			new ItemAction("Seed")
			{
				
				@Override
				public boolean actionPredicate(int tileX, int tileY)
				{
					// check whether the Tile's top Structure is the Dirt Structure
					return Main.map.getTile(tileX, tileY).getTopStructure().structure == ObjectsIndex.getStructure("Dirt");
				}
				
				@Override
				public boolean action(int tileX, int tileY) {
					// add a Seeded Grass Structure to the top of the Tile
					Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure("Seeded Grass"));
					return true;
				}
				
			}
		});
	}
	
}
