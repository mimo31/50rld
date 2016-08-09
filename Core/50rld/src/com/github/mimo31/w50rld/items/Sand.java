package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Sand Item.
 * @author mimo31
 *
 */
public class Sand extends Item {

	public Sand()
	{
		super("Sand", new ItemAction[]{ new ItemAction("Place")
		{

			@Override
			public boolean actionPredicate(int tileX, int tileY) {
				return Main.map.getTile(tileX, tileY).getSmoothness() >= 0;
			}

			@Override
			public boolean action(int tileX, int tileY) {
				Tile tile = Main.map.getTile(tileX, tileY);
				tile.pushStructure(ObjectsIndex.getStructure("Sand"));
				return true;
			}
			
		} });
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "SandI.png");
	}

}
