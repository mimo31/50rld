package com.github.mimo31.w50rld.items;

import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.Tile;
import com.github.mimo31.w50rld.WeaponItem;

/**
 * Represents the Rock Hammer Item. The Rock Hammer is used to mine ores from the ground and can be used as a basic weapon.
 * @author mimo31
 *
 */
public class RockHammer extends SimplyDrawnItem implements WeaponItem {

	public RockHammer() {
		super("Rock Hammer", "RockHammer", new ItemAction[]
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
	public float getHitPower() {
		return 1.5f;
	}

	@Override
	public float getHitRadius() {
		return 2;
	}
	
}
