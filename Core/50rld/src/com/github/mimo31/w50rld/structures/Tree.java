package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;

/**
 * Represents a Tree Structure.
 * @author mimo31
 *
 */
public class Tree extends Structure {

	public Tree()
	{
		super("tree", true);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "Tree.png");
	}
	
}
