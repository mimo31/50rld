package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.Box.CornerAlign;
import com.github.mimo31.w50rld.ChestUIBox;
import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Chest Structure.
 * @author mimo31
 *
 */
public class Chest extends Structure {

	public Chest() {
		super("Chest", false, new StructureAction[] {

				// shows the interface with the Chest's items
				new StructureAction("Open")
				{

					@Override
					public void action(int tileX, int tileY) {
						Main.addBox(new ChestUIBox(-1 / 8f, 0, ((ChestData) Main.map.getTile(tileX, tileY).getTopStructure()).items, CornerAlign.TOPLEFT));
					}
					
				},
				// removes the Chest Structure and add the Chest Item to the inventory
				new StructureAction("Deconstruct")
				{

					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						ItemStack[] chestItems = ((ChestData) tile.getTopStructure()).items;
						tile.popStructure();
						ItemStack chestStack = new ItemStack();
						chestStack.setCount(1);
						chestStack.setItem(ObjectsIndex.getItem("Chest"));
						tile.addInventoryItems(chestStack);
						for (int i = 0; i < chestItems.length; i++)
						{
							tile.addInventoryItems(chestItems[i]);
						}
					}
					
				}
		}, -2);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawTexture(startx, starty, endx, endy, "Chest");
	}

	@Override
	public StructureData createStructureData()
	{
		return new ChestData();
	}
	
	/**
	 * Subclass of StructureData to hold the items of a Chest.
	 * @author mimo31
	 *
	 */
	private static class ChestData extends StructureData {

		// Chest's items
		private ItemStack[] items = new ItemStack[16];
		
		public ChestData() {
			super(ObjectsIndex.getStructure("Chest"));
			for (int i = 0; i < 16; i++)
			{
				this.items[i] = new ItemStack();
			}
		}
		
	}
}
