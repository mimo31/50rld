package com.github.mimo31.w50rld;

/**
 * Provides methods for manipulating ItemStack arrays as inventories.
 * @author mimo31
 *
 */
public class InventoryUtils {

	/**
	 * Moves a specified number of specified Items from one inventory to another.
	 * @param inventoryFrom inventory to move from
	 * @param inventoryTo inventory to move to
	 * @param item Item to move
	 * @param count number of items to move
	 */
	public static void tryMoveItemsBetweenInventories(ItemStack[] inventoryFrom, ItemStack[] inventoryTo, Item item, String count)
	{
		// error message that will be displayed if something goes wrong
		String errorMessage = null;
		
		// remove leading zeroes
		count = StringUtils.trimZeroes(count);
		
		int inputLength = count.length();
		// no input whatsoever
		if (inputLength == 0)
		{
			errorMessage = "You must enter a number.";
		}
		// if the input it so long, so that there couldn't be that many items
		else if (inputLength > Math.ceil(Math.log10(inventoryFrom.length * 32)))
		{
			errorMessage = "There isn't that many items.";
		}
		else
		{
			// the number of items to move between the inventories
			int itemsToMove = Integer.parseInt(count);
			
			// the number of items available to move from the from inventory
			int itemsAvailable = 0;
			for (int i = 0; i < inventoryFrom.length; i++)
			{
				ItemStack currentSlot = inventoryFrom[i];
				if (currentSlot.getItem() == item)
				{
					itemsAvailable += currentSlot.getCount();
				}
			}
			
			// no enough items available to move
			if (itemsAvailable < itemsToMove)
			{
				errorMessage = "There isn't that many items.";
			}
			else
			{
				// the number of free positions in the to inventory
				int spaceAvailable = 0;
				
				for (int i = 0; i < inventoryTo.length; i++)
				{
					ItemStack currentSlot = inventoryTo[i];
					Item slotItem = currentSlot.getItem();
					if (slotItem == null)
					{
						spaceAvailable += 32;
					}
					else if (slotItem == item)
					{
						spaceAvailable += 32 - currentSlot.getCount();
					}
					if (spaceAvailable >= itemsToMove)
					{
						break;
					}
				}
				
				// not enough space in the to inventory
				if (spaceAvailable < itemsToMove)
				{
					errorMessage = "There is not enough space.";
				}
				else
				{
					// all conditions are met, move the items
					
					// items remaining to move
					int remainingItems = itemsToMove;
					
					// take the items from the from inventory
					for (int i = inventoryFrom.length - 1; i >= 0; i--)
					{
						ItemStack currentSlot = inventoryFrom[i];
						if (currentSlot.getItem() == item)
						{
							int itemsInSlot = currentSlot.getCount();
							if (itemsInSlot >= remainingItems)
							{
								itemsInSlot -= remainingItems;
								currentSlot.setCount(itemsInSlot);
								break;
							}
							else
							{
								remainingItems -= itemsInSlot;
								currentSlot.setCount(0);
							}
						}
					}
					
					// reset the remaining counter
					remainingItems = itemsToMove;
					
					// insert the items to the to inventory
					for (int i = 0; i < inventoryTo.length; i++)
					{
						ItemStack currentSlot = inventoryTo[i];
						if (currentSlot.getItem() == item)
						{
							int itemsInSlot = currentSlot.getCount();
							if (32 - itemsInSlot >= remainingItems)
							{
								itemsInSlot += remainingItems;
								currentSlot.setCount(itemsInSlot);
								break;
							}
							else
							{
								remainingItems -= 32 - itemsInSlot;
								currentSlot.setCount(32);
							}
						}
						else if (currentSlot.getCount() == 0)
						{
							currentSlot.setItem(item);
							if (remainingItems < 32)
							{
								currentSlot.setCount(remainingItems);
								break;
							}
							else
							{
								remainingItems -= 32;
								currentSlot.setCount(32);
							}
						}
					}
					
					// return to avoid displaying the (null) error message
					return;
				}
			}
		}
		
		// display the error message
		InfoBox box = new InfoBox(7 / 16f, 1 / 2f, errorMessage);
		Main.addBox(box);
	}
	
	/**
	 * Returns whether the specified inventory at least a specified number of Items of specified type.
	 * @param inventory inventory to scan
	 * @param minimalCount the least number of items needed
	 * @param item the Item type to look for
	 * @return whether the inventory has at least the number of items specified
	 */
	public static boolean hasEnoughItems(ItemStack[] inventory, int minimalCount, Item item)
	{
		int itemCount = 0;
		for (int i = 0; i < inventory.length; i++)
		{
			if (inventory[i].getItem() == item)
			{
				itemCount += inventory[i].getCount();
				if (itemCount >= minimalCount)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Removes the specified number of items of specified type from the specified inventory.
	 * @param inventory inventory to manipulate
	 * @param count the number of items to remove
	 * @param item the Item type to remove
	 */
	public static void takeItems(ItemStack[] inventory, int count, Item item)
	{
		for (int i = inventory.length - 1; i >= 0; i--)
		{
			if (inventory[i].getItem() == item)
			{
				int slotCount = inventory[i].getCount();
				if (slotCount >= count)
				{
					inventory[i].setCount(slotCount - count);
					return;
				}
				else
				{
					count -= slotCount;
					inventory[i].setCount(0);
				}
			}
		}
		
		// the method still hasn't returned, so there was not enough items
		throw new RuntimeException("There is not that many items of this type in the inventory.");
	}
}
