package com.github.mimo31.w50rld.items;

/**
 * Represents a Sand Item.
 * @author mimo31
 *
 */
public class Sand extends SimplyDrawnItem {

	public Sand()
	{
		super("Sand", "SandI", new ItemAction[]{ new PlaceItemAction("Place", "Sand", 0) } );
	}

}
