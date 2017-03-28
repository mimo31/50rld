package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;

/**
 * Represents a Water Structure.
 * @author mimo31
 *
 */
public class Water extends Structure {

	public Water()
	{
		super("Water", true, new StructureAction[0], -2);
	}
	
	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawTexture(startx, starty, endx, endy, "Water");
	}

}
