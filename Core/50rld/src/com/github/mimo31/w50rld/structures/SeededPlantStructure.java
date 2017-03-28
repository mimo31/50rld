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
 * Represents a Structure that is a one or more seeds seeded into the ground.
 * @author mimo31
 *
 */
public abstract class SeededPlantStructure extends Structure implements Plant {

	// the name of the texture to draw when the seeds are not yet grown
	private final String textureName;
	
	// the name of the texture to draw when the seeds are already grown
	private final String grownTextureName;
	
	// the grow time factor that describes how much time does it take for the seeds to grow
	// which is the same as the time it take for the grown seeds to be replaced by the plant
	private final int growTimeFactor;
	
	public SeededPlantStructure(String name, String textureName, String grownTextureName, int growTimeFactor, String seedsItemName) {
		super(name, false, new StructureAction[] {
				
				new StructureAction("Take Out") {
					
					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						tile.addInventoryItems(new ItemStack(ObjectsIndex.getItem(seedsItemName), 1));
					}
				}
				
		}, -2);
		this.textureName = textureName;
		this.grownTextureName = grownTextureName;
		this.growTimeFactor = growTimeFactor;
	}

	/**
	 * Returns a newly created StructureData to use as the plant that replaces the seeds.
	 * @return
	 */
	protected abstract StructureData createPlantStructureData();
	
	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		String textureName;
		
		if (((SeededPlantData)Main.map.getTile(tileX, tileY).getStructure(structureNumber)).grown)
		{
			textureName = this.grownTextureName;
		}
		else
		{
			textureName = this.textureName;
		}
		
		PaintUtils.drawTexture(startx, starty, endx, endy, textureName);
	}
	
	@Override
	public StructureData createStructureData() {
		return new SeededPlantData(this);
	}
	
	/**
	 * Subclass of StructureData to handle the data of a Seeded Plant Structure.
	 * @author mimo31
	 *
	 */
	private static class SeededPlantData extends StructureData {

		// whether the seeds are already grown
		private boolean grown;
		
		// the number of milliseconds the seeds have been growing
		private int growTime = 0;
		
		// the Seeded Plant Structure of this seeds
		private SeededPlantStructure plantStructure;
		
		public SeededPlantData(SeededPlantStructure plantStructure) {
			super(plantStructure);
			this.plantStructure = plantStructure;
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber) {
			this.growTime += deltaTime;
			
			if (Math.random() < Main.calculateGrowProbability(this.growTime, this.plantStructure.growTimeFactor, deltaTime))
			{
				if (this.grown)
				{
					Tile tile = Main.map.getTile(tileX, tileY);
					tile.removeStructure(structureNumber);
					tile.insertStructure(this.plantStructure.createPlantStructureData(), structureNumber);
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
