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
import com.github.mimo31.w50rld.structures.Bush.BushData;

/**
 * Represents the Seeded Bush Structure. The Structure grows until it replaces itself with a Bush Structure.
 * @author mimo31
 *
 */
public class SeededBush extends Structure implements Plant {

	public SeededBush() {
		super("Seeded Bush", false, new StructureAction[]
			{
				
				new StructureAction("Take Out")
				{

					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Bush Seed"), 1));
					}
					
				}
				
			}, -2);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber) {
		String textureName;
		SeededBushData structure = (SeededBushData) Main.map.getTile(tileX, tileY).getStructure(structureNumber);
		if (structure.grown)
		{
			textureName = "SeededBushGrown.png";
		}
		else
		{
			textureName = "SeededBush.png";
		}
		PaintUtils.drawSquareTexture(g, x, y, width, height, textureName);
	}

	@Override
	public StructureData createStructureData()
	{
		return new SeededBushData();
	}
	
	/**
	 * Subclass of DataStructure to handle the data of a Seeded Bush structure.
	 * @author mimo31
	 *
	 */
	private static class SeededBushData extends StructureData {

		// the time the Bush has been growing
		private int growTime = 0;
		
		// whether it is in the bigger / grown stage
		private boolean grown;
		
		public SeededBushData() {
			super(ObjectsIndex.getStructure("Seeded Bush"));
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber)
		{
			double growProbability = Main.calculateGrowProbability(this.growTime, 60000, deltaTime);
			
			if (Math.random() < growProbability)
			{
				if (this.grown)
				{
					BushData bushStructure = (BushData) ObjectsIndex.getStructure("Bush").createStructureData();
					bushStructure.grown = false;
					
					Tile tile = Main.map.getTile(tileX, tileY);
					tile.popStructure();
					tile.pushStructure(bushStructure);
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
