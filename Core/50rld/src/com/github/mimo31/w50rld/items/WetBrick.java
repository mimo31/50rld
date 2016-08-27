package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Wet Brick Item. The Item can be obtained by combining a Clay item and a Brick Form item and can be used to create Bricks by drying.
 * @author mimo31
 *
 */
public class WetBrick extends Item {

	public WetBrick() {
		super("Wet Brick", new ItemAction[]
				{
					new ItemAction("Place")
					{

						@Override
						public boolean actionPredicate(int tileX, int tileY) {
							return Main.map.getTile(tileX, tileY).getSmoothness() >= -1;
						}

						@Override
						public boolean action(int tileX, int tileY) {
							Tile tile = Main.map.getTile(tileX, tileY);
							tile.pushStructure(ObjectsIndex.getStructure("Drying Brick"));
							return true;
						}
						
					}
				});
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "WetBrick.png");
	}

}
