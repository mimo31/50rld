package com.github.mimo31.w50rld.items;

/**
 * Represents a Dirt Item.
 * @author mimo31
 *
 */
public class Dirt extends SimplyDrawnItem {

	public Dirt()
	{
		super("Dirt", "DirtI", new ItemAction[]{ new PlaceItemAction("Place", "Dirt", 0) });
	}

}
