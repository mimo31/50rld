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
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "SeededGrass.png");
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
		public void update(int tileX, int tileY, int deltaTime)
		{
			// probability of the seeds growing in this update
			// this formula is based on this expressions:
			// 	the probability that the seeds had already grown up given the time they are in the ground in ms:
			//		P(x) = 1 - 0.5^((x / 60000)^(2))
			//		https://www.wolframalpha.com/input/?i=plot+1+-+0.5%5E((x+%2F+60000)%5E(2))+from+x+%3D+0+to+120000
			// the formula below is then (P(growTime + deltaTime) - P(growTime)) / (1 - P(growTime)) simplified
			double growProbability = 1 - Math.pow(0.5, deltaTime * (deltaTime + 2 * this.growTime) * Math.pow(60000, -2));
			
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
