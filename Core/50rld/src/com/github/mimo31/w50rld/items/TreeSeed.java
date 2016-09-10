package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;

/**
 * Represents the Tree Seed Item. The Item can be used to seed trees.
 * @author mimo31
 *
 */
public class TreeSeed extends SimplyDrawnItem {

	public TreeSeed() {
		super("Tree Seed", "TreeSeed.png", new ItemAction[]
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
						Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure("Seeded Tree"));
						return true;
					}
					
				}
			});
	}

}
