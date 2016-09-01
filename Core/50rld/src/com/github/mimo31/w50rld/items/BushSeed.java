package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Bush Seed Item. The Item can be used to seed Bushes.
 * @author mimo31
 *
 */
public class BushSeed extends Item {

	public BushSeed() {
		super("Bush Seed", new ItemAction[]
			{
				
				new ItemAction("Seed")
				{

					@Override
					public boolean actionPredicate(int tileX, int tileY)
					{
						return Main.map.getTile(tileX, tileY).getTopStructure().structure == ObjectsIndex.getStructure("Dirt");
					}
					
					@Override
					public boolean action(int tileX, int tileY) {
						Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure("Seeded Bush"));
						return true;
					}
					
				}
				
			});
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "BushSeed.png");
	}

}
