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
 * Represents a Grass Structure.
 * @author mimo31
 *
 */
public class Grass extends Structure implements Plant {

	public Grass()
	{
		super("Grass", true, new StructureAction[]{ new StructureAction("Tear off") {
			
			@Override
			public void action(int tileX, int tileY) {
				Tile currentTile = Main.map.getTile(tileX, tileY);
				
				currentTile.popStructure();
				
				ItemStack pile = new ItemStack();
				pile.setCount(Math.random() < 3 / 4d ? 1 : 0);
				pile.setItem(ObjectsIndex.getItem("Grass Pile"));
				currentTile.addInventoryItems(pile);
			}
			
		} }, -1);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Grass.png");
	}
	
	@Override
	public StructureData createStructureData()
	{
		return new GrassData();
	}
	
	/**
	 * Subclass of StructureData to handle data of the Grass Structure.
	 * @author mimo31
	 *
	 */
	private static class GrassData extends StructureData 
	{

		public GrassData() {
			super(ObjectsIndex.getStructure("Grass"));
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber)
		{
			// randomly decide whether the grass should be spread to adjacent tiles
			if (Math.random() < 0.0001)
			{
				// a random value that decides to which Tile the grass should spread
				double random = Math.random();
				
				Tile tile;
				if (random < 0.25)
				{
					tile = Main.map.getTile(tileX + 1, tileY);
				}
				else if (random < 0.5)
				{
					tile = Main.map.getTile(tileX, tileY + 1);
				}
				else if (random < 0.75)
				{
					tile = Main.map.getTile(tileX - 1, tileY);
				}
				else
				{
					tile = Main.map.getTile(tileX, tileY - 1);
				}
				
				int numberOfStructures = tile.getStructureCount();
				
				if (numberOfStructures == 0)
				{
					return;
				}
				
				Structure topStructure = tile.getTopStructure().structure;
				
				// if the Tile has Dirt on top of it
				if (topStructure == ObjectsIndex.getStructure("Dirt"))
				{
					tile.pushStructure(ObjectsIndex.getStructure("Grass"));
				}
				// if there is a plant on the Tile and there is Dirt under the plant
				else if (numberOfStructures > 1 && topStructure instanceof Plant && topStructure != ObjectsIndex.getStructure("Grass") && tile.getStructure(numberOfStructures - 2).structure == ObjectsIndex.getStructure("Dirt"))
				{
					tile.insertStructure(ObjectsIndex.getStructure("Grass"), numberOfStructures - 1);
				}
			}
		}
	}
}
