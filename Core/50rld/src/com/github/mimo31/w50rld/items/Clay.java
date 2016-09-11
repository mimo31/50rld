package com.github.mimo31.w50rld.items;

/**
 * Represents the Clay Item. The Item can by digged out in the Death Biome from the Clay Structure and used to create bricks.
 * @author mimo31
 *
 */
public class Clay extends SimplyDrawnItem {

	public Clay() {
		super("Clay", "ClayI.png", new ItemAction[] { new PlaceItemAction("Place", "Clay", 0) } );
	}
}
