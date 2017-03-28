package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Plant;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Tree Structure.
 * @author mimo31
 *
 */
public class Tree extends Structure implements Plant {

	public Tree()
	{
		super("Tree", false, new StructureAction[]{ new StructureAction("Chop down") {
			
			@Override
			public void action(int tileX, int tileY) {
				Tile currentTile = Main.map.getTile(tileX, tileY);
				TreeData treeStructure = (TreeData) currentTile.getTopStructure();
				currentTile.popStructure();
				
				if (treeStructure.grown)
				{
					ItemStack logs = new ItemStack();
					logs.setCount(1 + (int) (Math.random() * 3));
					logs.setItem(ObjectsIndex.getItem("Log"));
					currentTile.addInventoryItems(logs);
					
					currentTile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Tree Seed"), 1 + (int) (Math.random() * 2)));
				}
				
				ItemStack blends = new ItemStack();
				blends.setCount(1 + (treeStructure.grown ? (int) (Math.random() * 2) : 0));
				blends.setItem(ObjectsIndex.getItem("Wood Blend"));
				currentTile.addInventoryItems(blends);
			}
			
		} }, -2);
	}
	
	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber)
	{
		// decide whether to draw the not grown texture or the normal texture
		String textureName;
		Tile tile = Main.map.getTile(tileX, tileY);
		StructureData structure = tile.getStructure(structureNumber);
		if (((TreeData) structure).grown)
		{
			textureName = "Tree";
		}
		else
		{
			textureName = "TreeNotGrown";
		}
		PaintUtils.drawTexture(startx, starty, endx, endy, textureName);
	}
	
	@Override
	public StructureData createStructureData()
	{
		return new TreeData(true);
	}
	
	/**
	 * Subclass of StructureData to handle the data of the Tree Structure.
	 * @author mimo31
	 *
	 */
	static class TreeData extends StructureData {

		// the time the tree has been growing
		private int growTime = 0;
		
		// whether the tree has already grown
		boolean grown;
		
		public TreeData(boolean grown) {
			super(ObjectsIndex.getStructure("Tree"));
			this.grown = grown;
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber)
		{
			if (!this.grown)
			{
				double growProbability = Main.calculateGrowProbability(this.growTime, 60000, deltaTime);;
				if (Math.random() < growProbability)
				{
					this.grown = true;
					this.growTime = 0;
				}
				else
				{
					this.growTime += deltaTime;
				}
			}
		}
		
	}
}
