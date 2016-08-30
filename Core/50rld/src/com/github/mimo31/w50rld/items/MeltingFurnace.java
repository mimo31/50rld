package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents the Melting Furnace Item.
 * @author mimo31
 *
 */
public class MeltingFurnace extends Item {

	public MeltingFurnace() {
		super("Melting Furnace", new ItemAction[]
				{
					new ItemAction("Construct")
					{
						@Override
						public boolean actionPredicate(int tileX, int tileY)
						{
							return Main.map.getTile(tileX, tileY).getSmoothness() >= -1;
						}
						
						@Override
						public boolean action(int tileX, int tileY) {
							Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure("Melting Furnace"));
							return true;
						}
						
					}
				});
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "MeltingFurnaceI.png");
	}

}
