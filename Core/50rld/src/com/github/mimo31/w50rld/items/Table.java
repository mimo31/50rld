package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;

/**
 * Represents the Table Item.
 * @author mimo31
 *
 */
public class Table extends SimplyDrawnItem {

	public Table()
	{
		super("Table", "TableI.png", new ItemAction[]
		{
			new ItemAction("Construct")
			{
				
				@Override
				public boolean actionPredicate(int tileX, int tileY)
				{
					return Main.map.getTile(tileX, tileY).getSmoothness() >= -1;
				}
				
				@Override
				public boolean action(int tileX, int tileY)
				{
					Main.map.getTile(tileX, tileY).pushStructure(ObjectsIndex.getStructure("Table"));
					return true;
				}
				
			}
		});
	}
	
}
