package com.github.mimo31.w50rld;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a TableRecipe applicable in a Table. Hold 5 required Items and a result Item and its produced count.
 * A permutation of required items is considered as another TableRecipe.
 * @author mimo31
 *
 */
public class TableRecipe {

	// items required
	public final Item[] requiredItems;
	
	// item that will be produced
	public final Item resultItem;
	
	// the number of produced items
	public final int resultCount;
	
	public TableRecipe(Item[] requiredItems, Item resultItem, int resultCount)
	{
		this.requiredItems = requiredItems;
		this.resultItem = resultItem;
		this.resultCount = resultCount;
	}
	
	/**
	 * Creates a Recipe object out of the TableRecipe by merging multiple identical required items to one with an increased count.
	 * @return a Recipe object created out of the TableRecipe
	 */
	public Recipe toRecipe()
	{
		// list of all required items merged to stacks, so that every distinct item has only one stack
		List<ItemStack> requiredItemsList = new ArrayList<ItemStack>();
		
		// populate the list by going through all the items and if the item is already in the list, incrementing its count, else adding the item to the list
		for (int i = 0; i < 5; i++)
		{
			if (this.requiredItems[i] == null)
			{
				continue;
			}
			boolean added = false;
			for (int j = 0, n = requiredItemsList.size(); j < n; j++)
			{
				ItemStack currentStack = requiredItemsList.get(j);
				if (this.requiredItems[i] == currentStack.getItem())
				{
					currentStack.setCount(currentStack.getCount() + 1);
					added = true;
					break;
				}
			}
			if (!added)
			{
				ItemStack newStack = new ItemStack();
				newStack.setCount(1);
				newStack.setItem(this.requiredItems[i]);
				requiredItemsList.add(newStack);
			}
		}
		
		// split the list of ItemStack into an array of Items and an array of counts
		Item[] requiredItemsArray = new Item[requiredItemsList.size()];
		int[] requiredCounts = new int[requiredItemsArray.length];
		for (int i = 0; i < requiredItemsArray.length; i++)
		{
			ItemStack currentStack = requiredItemsList.get(i);
			requiredItemsArray[i] = currentStack.getItem();
			requiredCounts[i] = currentStack.getCount();
		}
		return new Recipe(requiredItemsArray, requiredCounts, this.resultItem, this.resultCount);
	}
	
	/**
	 * Adds all known TableRecipes to a list in a sorted order.
	 * @param recipes
	 */
	public static void addAllRecipes(List<TableRecipe> recipes)
	{
		Item logItem = ObjectsIndex.getItem("Log");
		Item sticksItem = ObjectsIndex.getItem("Sticks");
		Item brickItem = ObjectsIndex.getItem("Brick");
		Item furnaceWallItem = ObjectsIndex.getItem("Furnace Wall");
		Item furnaceBaseItem = ObjectsIndex.getItem("Furnace Base");

		recipes.add(new TableRecipe(new Item[] { brickItem, brickItem, ObjectsIndex.getItem("Grout"), brickItem, brickItem }, furnaceWallItem, 1));
		recipes.add(new TableRecipe(new Item[] { furnaceWallItem, furnaceWallItem, ObjectsIndex.getItem("Rock"), furnaceWallItem, furnaceBaseItem }, ObjectsIndex.getItem("Melting Furnace"), 1));
		recipes.add(new TableRecipe(new Item[] { furnaceWallItem, furnaceWallItem, sticksItem, furnaceWallItem, null }, furnaceBaseItem, 1));
		recipes.add(new TableRecipe(new Item[] { logItem, sticksItem, null, sticksItem, logItem }, ObjectsIndex.getItem("Chest"), 1));
		recipes.add(new TableRecipe(new Item[] { sticksItem, sticksItem, null, sticksItem, sticksItem }, ObjectsIndex.getItem("Brick Form"), 1));
	}
}
