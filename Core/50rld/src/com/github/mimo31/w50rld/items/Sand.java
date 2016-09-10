package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Sand Item.
 * @author mimo31
 *
 */
public class Sand extends SimplyDrawnItem {

	public Sand()
	{
		super("Sand", "SandI.png", new ItemAction[]{ new ItemAction("Place")
		{

			@Override
			public boolean actionPredicate(int tileX, int tileY) {
				return Main.map.getTile(tileX, tileY).getSmoothness() >= 0;
			}

			@Override
			public boolean action(int tileX, int tileY) {
				Tile tile = Main.map.getTile(tileX, tileY);
				tile.pushStructure(ObjectsIndex.getStructure("Sand"));
				return true;
			}
			
		} });
	}

}
