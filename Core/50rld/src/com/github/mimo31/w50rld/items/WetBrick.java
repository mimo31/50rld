package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Wet Brick Item. The Item can be obtained by combining a Clay item and a Brick Form item and can be used to create Bricks by drying.
 * @author mimo31
 *
 */
public class WetBrick extends SimplyDrawnItem {

	public WetBrick() {
		super("Wet Brick", "WetBrick.png", new ItemAction[]
				{
					new ItemAction("Place")
					{

						@Override
						public boolean actionPredicate(int tileX, int tileY) {
							return Main.map.getTile(tileX, tileY).getSmoothness() >= -1;
						}

						@Override
						public boolean action(int tileX, int tileY) {
							Tile tile = Main.map.getTile(tileX, tileY);
							tile.pushStructure(ObjectsIndex.getStructure("Drying Brick"));
							return true;
						}
						
					}
				});
	}

}
