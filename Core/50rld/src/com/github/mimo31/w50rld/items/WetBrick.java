package com.github.mimo31.w50rld.items;

/**
 * Represents the Wet Brick Item. The Item can be obtained by combining a Clay item and a Brick Form item and can be used to create Bricks by drying.
 * @author mimo31
 *
 */
public class WetBrick extends SimplyDrawnItem {

	public WetBrick() {
		super("Wet Brick", "WetBrick.png", new ItemAction[] { new PlaceItemAction("Place", "Drying Brick", -1) });
	}

}
