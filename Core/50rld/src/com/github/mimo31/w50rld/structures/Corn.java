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
 * Represents the Corn Structure. The Structure can be harvested to get Corn when grown.
 * @author mimo31
 *
 */
public class Corn extends Structure implements Plant {

	public Corn()
	{
		super("Corn", false, new StructureAction[]
		{
				new StructureAction("Harvest")
				{

					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						CornData data = (CornData) tile.getTopStructure();
						if (data.grown)
						{
							tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem("Corn"), 1 + (int)(Math.random() * 4)));
						}
						tile.popStructure();
					}
					
				}
		}, -2);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		String textureName;
		if (((CornData)Main.map.getTile(tileX, tileY).getStructure(structureNumber)).grown)
		{
			textureName = "CornS";
		}
		else
		{
			textureName = "CornNotGrown";
		}
		PaintUtils.drawTexture(startx, starty, endx, endy, textureName);
	}
	
	@Override
	public StructureData createStructureData() {
		return new CornData();
	}
	
	/**
	 * Subclass of StructureData handle the data of the Corn Structure.
	 * @author mimo31
	 *
	 */
	public static class CornData extends StructureData {

		private boolean grown;
		private int growTime;
		
		public CornData() {
			super(ObjectsIndex.getStructure("Corn"));
			this.grown = true;
		}
		
		public CornData(boolean grown)
		{
			this();
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
