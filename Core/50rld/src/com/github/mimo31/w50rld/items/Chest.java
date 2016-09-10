package com.github.mimo31.w50rld.items;

/**
 * Represents a Chest Item.
 * @author mimo31
 *
 */
public class Chest extends SimplyDrawnItem {

	public Chest() {
		super("Chest", "Chest.png", new ItemAction[] {
				
				new PlaceItemAction("Construct", "Chest", -1)
				
		});
	}

}
