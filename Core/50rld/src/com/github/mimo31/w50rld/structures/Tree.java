package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Tree Structure.
 * @author mimo31
 *
 */
public class Tree extends Structure {

	public Tree()
	{
		super("Tree", true, new StructureAction[]{ new StructureAction("Chop down") {
			
			@Override
			public void action(int tileX, int tileY) {
				Tile currentTile = Main.map.getTile(tileX, tileY);
				currentTile.popStructure();
				
				ItemStack logs = new ItemStack();
				logs.setCount(1 + (int) (Math.random() * 3));
				logs.setItem(ObjectsIndex.getItem("Log"));
				currentTile.addInventoryItems(logs);
				
				ItemStack blends = new ItemStack();
				blends.setCount(1 + (int) (Math.random() * 2));
				blends.setItem(ObjectsIndex.getItem("Wood Blend"));
				currentTile.addInventoryItems(blends);
				
				currentTile.pushStructure(ObjectsIndex.getStructure("Grass"));
			}
			
		} }, -2);
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Tree.png");
	}
	
}
