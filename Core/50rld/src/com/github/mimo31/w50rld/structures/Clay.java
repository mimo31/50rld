package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.Tile;

public class Clay extends Structure {

	public Clay() {
		super("Clay", true, new StructureAction[] {
				
				new StructureAction("Dig Out")
				{

					@Override
					public void action(int tileX, int tileY) {
						Tile currentTile = Main.map.getTile(tileX, tileY);
						currentTile.popStructure();
						ItemStack clayStack = new ItemStack();
						clayStack.setCount(1);
						clayStack.setItem(ObjectsIndex.getItem("Clay"));
						currentTile.addInventoryItems(clayStack);
					}
					
				}
				
		}, 0);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawTexture(startx, starty, endx, endy, "ClayS");
	}

}
