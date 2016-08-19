package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Grass Seeds Item.
 * @author mimo31
 *
 */
public class GrassSeeds extends Item {

	public GrassSeeds()
	{
		super("Grass Seeds", new ItemAction[]
		{
			new ItemAction("Seed")
			{
				
				@Override
				public boolean actionPredicate(int tileX, int tileY)
				{
					// check whether the Tile's top Structure is the Dirt Structure
					return Main.map.getTile(tileX, tileY).getTopStructure().structure == ObjectsIndex.getStructure("Dirt");
				}
				
				@Override
				public boolean action(int tileX, int tileY) {
					// add a Seeded Grass Structure to the top of the Tile
					Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure("Seeded Grass"));
					return true;
				}
				
			}
		});
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "GrassSeeds.png");
	}
	
}
