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
import com.github.mimo31.w50rld.structures.Carrots.CarrotsData;

/**
 * Represents the Seeded Carrots Structure. The Structure grows and eventually replaces itself by the Carrots Structure.
 * @author mimo31
 *
 */
public class SeededCarrots extends Structure implements Plant {

	public SeededCarrots() {
		super("Seeded Carrots", false, new StructureAction[] {
				
				new StructureAction("Take Out")
				{

					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Carrot Seeds"), 1));
					}
					
				}
				
		}, -2);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber) {
		String textureName;
		if (((SeededCarrotsData) Main.map.getTile(tileX, tileY).getStructure(structureNumber)).grown)
		{
			textureName = "SeededCarrotsGrown.png";
		}
		else
		{
			textureName = "SeededCarrots.png";
		}
		PaintUtils.drawSquareTexture(g, x, y, width, height, textureName);
	}

	@Override
	public StructureData createStructureData() {
		return new SeededCarrotsData();
	}
	
	/**
	 * Subclass of StructureData to handle the data of the Seeded Carrots Structure.
	 * @author mimo31
	 *
	 */
	private static class SeededCarrotsData extends StructureData {

		private int growTime = 0;
		private boolean grown = false;
		
		public SeededCarrotsData() {
			super(ObjectsIndex.getStructure("Seeded Carrots"));
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
					tile.insertStructure(new CarrotsData(false), structureNumber);
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
