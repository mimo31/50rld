package com.github.mimo31.w50rld.items;

/**
 * Represents the Table Item.
 * @author mimo31
 *
 */
public class Table extends SimplyDrawnItem {

	public Table()
	{
		super("Table", "TableI.png", new ItemAction[] { new PlaceItemAction("Construct", "Table", -1) });
	}
	
}
