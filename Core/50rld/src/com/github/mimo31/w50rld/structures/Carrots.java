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
 * Represents the Carrots Structure. The Structure can be harvested to get Carrots and Carrot Seeds when grown.
 * @author mimo31
 *
 */
public class Carrots extends Structure implements Plant {

	public Carrots() {
		super("Carrots", false, new StructureAction[] {
				
				new StructureAction("Harvest") {
					
					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						CarrotsData data = (CarrotsData) tile.getTopStructure();
						if (data.grown)
						{
							tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Carrot Seeds"), 1 + (int)(Math.random() * 2)));
							tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Carrot"), 1 + (int)(Math.random() * 3)));
						}
						else
						{
							tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Carrot Seeds"), 1));
						}
						tile.popStructure();
					}
				}
				
		}, -2);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		String textureName;
		if (((CarrotsData)Main.map.getTile(tileX, tileY).getStructure(structureNumber)).grown)
		{
			textureName = "Carrots";
		}
		else
		{
			textureName = "CarrotsNotGrown";
		}
		PaintUtils.drawTexture(startx, starty, endx, endy, textureName);
	}

	@Override
	public StructureData createStructureData() {
		return new CarrotsData();
	}
	
	/**
	 * Subclass of StructureData to handle the data of the Carrots Structure.
	 * @author mimo31
	 *
	 */
	public static class CarrotsData extends StructureData {

		private boolean grown;
		private int growTime = 0;
		
		public CarrotsData()
		{
			super(ObjectsIndex.getStructure("Carrots"));
			this.grown = true;
		}
		
		public CarrotsData(boolean grown)
		{
			super(ObjectsIndex.getStructure("Carrots"));
			this.grown = grown;
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber)
		{
			if (this.grown)
			{
				return;
			}
			if (Math.random() < Main.calculateGrowProbability(this.growTime, 60000, deltaTime))
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
