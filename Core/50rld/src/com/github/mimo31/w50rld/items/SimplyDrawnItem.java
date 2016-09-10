package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represents an Item that is drawn using the same texture all the time.
 * @author mimo31
 *
 */
public class SimplyDrawnItem extends Item {

	// the name of the Item's texture
	private final String textureName;
	
	public SimplyDrawnItem(String name, String textureName, ItemAction[] actions) {
		super(name, actions);
		this.textureName = textureName;
	}

	public SimplyDrawnItem(String name, String textureName)
	{
		super(name, new ItemAction[0]);
		this.textureName = textureName;
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, this.textureName);
	}
	
}
