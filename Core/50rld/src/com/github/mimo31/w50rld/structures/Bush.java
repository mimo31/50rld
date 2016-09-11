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
 * Represents a Bush structure.
 * @author mimo31
 *
 */
public class Bush extends Structure implements Plant {

	public Bush()
	{
		super("Bush", false, new StructureAction[]{ new StructureAction("Remove") {
			
			@Override
			public void action(int tileX, int tileY) {
				Tile currentTile = Main.map.getTile(tileX, tileY);
				
				currentTile.popStructure();
				
				ItemStack blends = new ItemStack();
				blends.setCount(1 + (int) (Math.random() * 2));
				blends.setItem(ObjectsIndex.getItem("Wood Blend"));
				currentTile.addInventoryItems(blends);
				currentTile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Bush Seed"), 1 + (int)(Math.random() * 2)));
			}
			
		} }, -2);
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber)
	{
		String textureName;
		
		BushData structure = (BushData) Main.map.getTile(tileX, tileY).getStructure(structureNumber);
		if (structure.grown)
		{
			textureName = "Bush.png";
		}
		else
		{
			textureName = "BushNotGrown.png";
		}
		
		PaintUtils.drawSquareTexture(g, x, y, width, height, textureName);
	}

	@Override
	public StructureData createStructureData()
	{
		return new BushData();
	}
	
	/**
	 * Subclass of StructureData to handle the data of a Bush structure.
	 * @author mimo31
	 *
	 */
	static class BushData extends StructureData {

		// time the Bush has been growing
		private int growTime = 0;
		
		// whether the Bush has already grown
		boolean grown = true;
		
		public BushData() {
			super(ObjectsIndex.getStructure("Bush"));
		}

		public BushData(boolean grown)
		{
			super(ObjectsIndex.getStructure("Bush"));
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
