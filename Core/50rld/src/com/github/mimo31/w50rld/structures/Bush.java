package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Bush structure.
 * @author mimo31
 *
 */
public class Bush extends Structure {

	public Bush()
	{
		super("Bush", true, new StructureAction[]{ new StructureAction("Remove") {
			
			@Override
			public void action(int tileX, int tileY) {
				Tile currentTile = Main.map.getTile(tileX, tileY);
				currentTile.popStructure();
				currentTile.pushStructure(ObjectsIndex.getStructure("Grass"));
			}
			
		} });
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Bush.png");
	}

}
