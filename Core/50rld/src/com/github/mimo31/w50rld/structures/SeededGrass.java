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
 * Represents the Seeded Grass Structure.
 * @author mimo31
 *
 */
public class SeededGrass extends Structure implements Plant {

	public SeededGrass() {
		super("Seeded Grass", false, new StructureAction[] {
				
				new StructureAction("Take out")
				{

					@Override
					public void action(int tileX, int tileY) {
						// remove the Seeded Grass Structure from the Tile and add 1 Grass Seeds Item to the inventory
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						ItemStack seeds = new ItemStack();
						seeds.setCount(1);
						seeds.setItem(ObjectsIndex.getItem("Grass Seeds"));
						tile.addInventoryItems(seeds);
					}
					
				}
				
		}, -2);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawTexture(startx, starty, endx, endy, "SeededGrass");
	}

	@Override
	public StructureData createStructureData()
	{
		return new SeededGrassData();
	}
	
	/**
	 * Subclass of StructureData to handle structure data of the Seeded Grass Structure.
	 * 
	 * @author mimo31
	 *
	 */
	public static class SeededGrassData extends StructureData {
		
		// the number of milliseconds the seeds are in the ground
		private int growTime = 0;
		
		public SeededGrassData()
		{
			super(ObjectsIndex.getStructure("Seeded Grass"));
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber)
		{
			double growProbability = Main.calculateGrowProbability(this.growTime, 60000, deltaTime);;
			
			// whether the seeds will actually grow up in this update
			boolean grow = Math.random() < growProbability;
			if (grow)
			{
				// change Tile's top Structure to the Grass Structure
				Tile tile = Main.map.getTile(tileX, tileY);
				tile.popStructure();
				tile.pushStructure(ObjectsIndex.getStructure("Grass"));
				return;
			}
			this.growTime += deltaTime;
		}
	}
}
