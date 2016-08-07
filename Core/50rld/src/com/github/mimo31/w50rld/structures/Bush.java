package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;

/**
 * Represents a Bush structure.
 * @author mimo31
 *
 */
public class Bush extends Structure {

	public Bush()
	{
		super("bush", true);
	}
	
	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Bush.png");
	}

}
