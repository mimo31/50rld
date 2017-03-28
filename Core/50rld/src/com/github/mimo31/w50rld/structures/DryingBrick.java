package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Drying Brick Structure. After some time, the Structure replaces itself with the Dried Brick Structure.
 * @author mimo31
 *
 */
public class DryingBrick extends Structure {

	public DryingBrick() {
		super("Drying Brick", false, new StructureAction[] {
				
				new StructureAction("Take")
				{
					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						ItemStack wetBrick = new ItemStack(ObjectsIndex.getItem("Wet Brick"), 1);
						tile.addInventoryItems(wetBrick);
					}
				}
				
		}, -2);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawTexture(startx, starty, endx, endy, "DryingBrick");
	}

	@Override
	public StructureData createStructureData()
	{
		return new DryingBrickData();
	}
	
	/**
	 * A subclass of StructureData to handle the structure data of a DryingBrick structure.
	 * @author mimo31
	 *
	 */
	private static class DryingBrickData extends StructureData
	{

		// the number of milliseconds the brick has been already drying
		private int growTime = 0;
		
		public DryingBrickData() {
			super(ObjectsIndex.getStructure("Drying Brick"));
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber)
		{
			// the probability that the brick will dry out in this update
			// see com.github.mimo31.w50rld.structures.SeededGrass for more information
			double dryProbability = 1 - Math.pow(0.5, deltaTime * (deltaTime + 2 * this.growTime) * Math.pow(60000, -2));
			
			if (Math.random() < dryProbability)
			{
				// change Tile's top Structure to the Grass Structure
				Tile tile = Main.map.getTile(tileX, tileY);
				tile.popStructure();
				tile.pushStructure(ObjectsIndex.getStructure("Dried Brick"));
				return;
			}
			this.growTime += deltaTime;
		}
	}
}
