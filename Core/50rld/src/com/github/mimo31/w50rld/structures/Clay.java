package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

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
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "ClayS.png");
	}

}
