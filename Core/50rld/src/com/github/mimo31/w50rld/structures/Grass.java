package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents a Grass Structure.
 * @author mimo31
 *
 */
public class Grass extends Structure {

	public Grass()
	{
		super("Grass", true, new StructureAction[]{ new StructureAction("Tear off") {
			
			@Override
			public void action(int tileX, int tileY) {
				Tile currentTile = Main.map.getTile(tileX, tileY);
				currentTile.popStructure();
			}
			
		} });
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Grass.png");
	}
	
}
