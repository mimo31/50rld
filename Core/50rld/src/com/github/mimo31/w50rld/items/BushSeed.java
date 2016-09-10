package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;

/**
 * Represents the Bush Seed Item. The Item can be used to seed Bushes.
 * @author mimo31
 *
 */
public class BushSeed extends SimplyDrawnItem {

	public BushSeed() {
		super("Bush Seed", "BushSeed.png", new ItemAction[]
			{
				
				new ItemAction("Seed")
				{

					@Override
					public boolean actionPredicate(int tileX, int tileY)
					{
						return Main.map.getTile(tileX, tileY).getTopStructure().structure == ObjectsIndex.getStructure("Dirt");
					}
					
					@Override
					public boolean action(int tileX, int tileY) {
						Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure("Seeded Bush"));
						return true;
					}
					
				}
				
			});
	}

}
