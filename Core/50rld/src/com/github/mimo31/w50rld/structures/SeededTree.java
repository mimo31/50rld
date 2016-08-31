package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Plant;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.Tile;
import com.github.mimo31.w50rld.structures.Tree.TreeData;

/**
 * Represents the Seeded Tree Structure. The Structure grow until it replaces itself with a Tree Structure.
 * @author mimo31
 *
 */
public class SeededTree extends Structure implements Plant {

	public SeededTree() {
		super("Seeded Tree", false, new StructureAction[] {
				
				new StructureAction("Take Out")
				{

					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Tree Seed"), 1));
					}
					
				}
				
		}, -2);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber) {
		String textureName;
		if (((SeededTreeData) Main.map.getTile(tileX, tileY).getStructure(structureNumber)).grown)
		{
			textureName = "SeededTreeGrown.png";
		}
		else
		{
			textureName = "SeededTree.png";
		}
		PaintUtils.drawSquareTexture(g, x, y, width, height, textureName);
	}

	@Override
	public StructureData createStructureData()
	{
		return new SeededTreeData();
	}
	
	/**
	 * Subclass of StructureData to handle structure data of the Seeded Tree Structure.
	 * @author mimo31
	 *
	 */
	private static class SeededTreeData extends StructureData {

		// whether the seed is in the grown state
		private boolean grown = false;
		
		// the time the seed has been growing (when it enters the grown state, growTime is reset)
		private int growTime = 0;
		
		public SeededTreeData() {
			super(ObjectsIndex.getStructure("Seeded Tree"));
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime)
		{
			// probability that the seed will grow in this update
			double growProbability = 1 - Math.pow(0.5, deltaTime * (deltaTime + 2 * this.growTime) * Math.pow(60000, -2));
			
			if (Math.random() < growProbability)
			{
				if (this.grown)
				{
					// replace the structure with a Tree structure
					Tile tile = Main.map.getTile(tileX, tileY);
					tile.popStructure();
					TreeData treeStructure = (TreeData) ObjectsIndex.getStructure("Tree").createStructureData();
					
					// make the Tree not grown
					treeStructure.grown = false;
					tile.pushStructure(treeStructure);
				}
				else
				{
					this.grown = true;
					this.growTime = 0;
				}
			}
			
			this.growTime += deltaTime;
		}
	}
}
