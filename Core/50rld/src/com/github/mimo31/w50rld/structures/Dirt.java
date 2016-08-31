package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Dirt Structure.
 * @author mimo31
 *
 */
public class Dirt extends Structure {

	public Dirt()
	{
		super("Dirt", true, new StructureAction[] { new StructureAction("Dig Out")
		{
			@Override
			public void action(int tileX, int tileY) {
				Tile tile = Main.map.getTile(tileX, tileY);
				tile.popStructure();
				ItemStack stack = new ItemStack();
				stack.setItem(ObjectsIndex.getItem("Dirt"));
				stack.setCount(1);
				tile.addInventoryItems(stack);
			}
		}}, 0);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "DirtS.png");
	}

}
