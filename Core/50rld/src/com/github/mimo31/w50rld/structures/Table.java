package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

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
					Main.addBox(new TableUIBox(7 / 16f, 1 / 2f));
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
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber)
	{
		PaintUtils.drawSquareTexture(g, x, y, width, height, "TableS.png");
	}
	
}
