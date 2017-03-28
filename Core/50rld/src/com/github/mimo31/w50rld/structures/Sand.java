package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Sand Structure.
 * @author mimo31
 *
 */
public class Sand extends Structure {

	public Sand()
	{
		super("Sand", true, new StructureAction[] { new StructureAction("Dig Out")
		{
			@Override
			public void action(int tileX, int tileY) {
				Tile tile = Main.map.getTile(tileX, tileY);
				tile.popStructure();
				tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Sand"), 1));
				// tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Melting Furnace"), 1));
			}
		}}, 0);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber)
	{
		PaintUtils.drawTexture(startx, starty, endx, endy, "SandS");
	}

}
