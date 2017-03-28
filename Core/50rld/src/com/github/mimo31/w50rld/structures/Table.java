package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.Box.CornerAlign;
import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.TableUIBox;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Table Structure.
 * @author mimo31
 *
 */
public class Table extends Structure {

	public Table()
	{
		super("Table", false, new StructureAction[]
		{
			
			// shows the Table interface
			new StructureAction("Use")
			{

				@Override
				public void action(int tileX, int tileY) {
					Main.addBox(new TableUIBox(-1 / 8f, 0, CornerAlign.TOPLEFT));
				}
				
			},
			
			// removes the Table Structure and add a Table Item to the inventory
			new StructureAction("Deconstruct")
			{

				@Override
				public void action(int tileX, int tileY) {
					Tile tile = Main.map.getTile(tileX, tileY);
					tile.popStructure();
					
					ItemStack tableStack = new ItemStack();
					tableStack.setCount(1);
					tableStack.setItem(ObjectsIndex.getItem("Table"));
					
					tile.addInventoryItems(tableStack);
				}
				
			}
		}, -2);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber)
	{
		PaintUtils.drawTexture(startx, starty, endx, endy, "TableS");
	}
	
}
