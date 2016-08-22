package com.github.mimo31.w50rld.items;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Rock Hammer Item. The Rock Hammer is used to mine ores from the ground.
 * @author mimo31
 *
 */
public class RockHammer extends Item {

	public RockHammer() {
		super("Rock Hammer", new ItemAction[]
			{
				new ItemAction("Mine Rock")
						{

							@Override
							public boolean action(int tileX, int tileY) {
								Tile tile = Main.map.getTile(tileX, tileY);
								
								// get the amounts of ore in this Tile and add them to the inventory
								// add iron
								ItemStack oreStack = new ItemStack();
								oreStack.setItem(ObjectsIndex.getItem("Iron Ore"));
								oreStack.setCount(tile.getIronAmount());
								tile.addInventoryItems(oreStack);
								// add coal
								oreStack.setItem(ObjectsIndex.getItem("Coal"));
								oreStack.setCount(tile.getCoalAmount());
								tile.addInventoryItems(oreStack);
								// add gold
								oreStack.setItem(ObjectsIndex.getItem("Gold Ore"));
								oreStack.setCount(tile.getGoldAmount());
								tile.addInventoryItems(oreStack);
								// add gravel
								oreStack.setItem(ObjectsIndex.getItem("Gravel"));
								oreStack.setCount(1);
								tile.addInventoryItems(oreStack);
								
								tile.incrementDepth();
								return false;
							}
						
							@Override
							public boolean actionPredicate(int tileX, int tileY)
							{
								Tile tile = Main.map.getTile(tileX, tileY);
								return !tile.hasStructures() && tile.getDepth() != 32;
							}
						}
				
			});
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "RockHammer.png");
	}
	
}