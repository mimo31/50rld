package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Dried Brick Structure. The Structure appears some time after a Drying Brick structure is placed.
 * @author mimo31
 *
 */
public class DriedBrick extends Structure {

	public DriedBrick() {
		super("Dried Brick", false, new StructureAction[] {
				
				new StructureAction("Take")
				{
					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						ItemStack wetBrick = new ItemStack(ObjectsIndex.getItem("Brick"), 1);
						tile.addInventoryItems(wetBrick);
					}
				}
				
		}, -2);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawTexture(startx, starty, endx, endy, "DriedBrick");
	}

}
