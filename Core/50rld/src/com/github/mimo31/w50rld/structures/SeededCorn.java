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
import com.github.mimo31.w50rld.structures.Corn.CornData;

/**
 * Represents the Seeded Corn Structure. The Structure grows and eventually replaces itself by the Corn Structure.
 * @author mimo31
 *
 */
public class SeededCorn extends Structure implements Plant {

	public SeededCorn() {
		super("Seeded Corn", false, new StructureAction[]
		{
				new StructureAction("Take Out")
				{

					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Corn Seeds"), 1));
						tile.popStructure();
					}
					
				}
		}, -2);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber) {
		String textureName;
		if (((SeededCornData)Main.map.getTile(tileX, tileY).getStructure(structureNumber)).grown)
		{
			textureName = "SeededCornGrown.png";
		}
		else
		{
			textureName = "SeededCorn.png";
		}
		PaintUtils.drawSquareTexture(g, x, y, width, height, textureName);
	}

	@Override
	public StructureData createStructureData() {
		return new SeededCornData();
	}
	
	/**
	 * Subclass of the StructureData to handle the data of the Seeded Corn Structure.
	 * @author mimo31
	 *
	 */
	private static class SeededCornData extends StructureData {

		private boolean grown;
		private int growTime;
		
		public SeededCornData() {
			super(ObjectsIndex.getStructure("Seeded Corn"));
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber) {
			this.growTime += deltaTime;
			
			if (Math.random() < Main.calculateGrowProbability(this.growTime, 60000, deltaTime))
			{
				if (this.grown)
				{
					Tile tile = Main.map.getTile(tileX, tileY);
					tile.removeStructure(structureNumber);
					tile.insertStructure(new CornData(false), structureNumber);
				}
				else
				{
					this.grown = true;
					this.growTime = 0;
				}
			}
		}
	}
}
