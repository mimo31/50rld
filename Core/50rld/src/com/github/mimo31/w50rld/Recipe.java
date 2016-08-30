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
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Sticks"), ObjectsIndex.getItem("Log") }, new int[] { 2, 2 }, ObjectsIndex.getItem("Table"), 1));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Grass Pile") }, new int[] { 2 }, ObjectsIndex.getItem("Cord"), 1));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Sticks"), ObjectsIndex.getItem("Cord")}, new int[] { 1, 1 }, ObjectsIndex.getItem("Handle"), 1));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Log"), ObjectsIndex.getItem("Handle")}, new int[] { 1, 1 }, ObjectsIndex.getItem("Hammer"), 1));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Hammer"), ObjectsIndex.getItem("Grass Pile")}, new int[] { 0, 1 }, ObjectsIndex.getItem("Grass Seeds"), 2));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Rock"), ObjectsIndex.getItem("Handle") }, new int[] { 1, 1 }, ObjectsIndex.getItem("Rock Hammer"), 1));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Brick Form"), ObjectsIndex.getItem("Clay") }, new int[] { 0, 1 }, ObjectsIndex.getItem("Wet Brick"), 1));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Clay"), ObjectsIndex.getItem("Gravel") }, new int[] { 2, 1 }, ObjectsIndex.getItem("Grout"), 4));
		recipes.add(new Recipe(new Item[] { ObjectsIndex.getItem("Iron Blade"), ObjectsIndex.getItem("Handle") }, new int[] { 1, 1 }, ObjectsIndex.getItem("Iron Sword"), 1));
	}
}
