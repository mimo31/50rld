package com.github.mimo31.w50rld;

import java.util.List;

/**
 * Represents a Recipe that takes a number of input Items and produces a result Item.
 * @author mimo31
 *
 */
public class Recipe {

	// Items required
	public final Item[] requiredItems;

	// counts of Items required
	public final int[] requiredCounts;
	
	// Item produced
	public final Item resultItem;
	
	// number of Items produced
	public final int resultCount;
	
	public Recipe(Item[] requiredItems, int[] requiredCounts, Item resultItem, int resultCount)
	{
		this.requiredItems = requiredItems;
		this.requiredCounts = requiredCounts;
		this.resultItem = resultItem;
		this.resultCount = resultCount;
	}
	
	/**
	 * Adds all known Recipes to a list.
	 * @param recipes the list to add the Recipes to
	 */
	public static void addAllRecipes(List<Recipe> recipes)
	{
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Wood Blend") }, new int[] { 1 }, ObjectsIndex.getItem("Sticks"), 1 ));
	}
}