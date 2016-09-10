package com.github.mimo31.w50rld.items;

/**
 * Represents the Melting Furnace Item.
 * @author mimo31
 *
 */
public class MeltingFurnace extends SimplyDrawnItem {

	public MeltingFurnace() {
		super("Melting Furnace", "MeltingFurnaceI.png", new ItemAction[] { new PlaceItemAction("Construct", "Melting Furnace", -1) });
	}

}
