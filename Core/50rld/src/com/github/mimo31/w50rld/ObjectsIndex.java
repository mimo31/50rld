package com.github.mimo31.w50rld;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.mimo31.w50rld.items.*;
import com.github.mimo31.w50rld.structures.*;

/**
 * Holds an index of all game objects like Items and Structures. 
 * To create an object in the game, get<objectType>(String name) should be called.
 * That method called with the same argument always returns a reference to one single instance of the object stored in the index.
 * Actions like checking that two object in the game are the same can therefore be done by checking by reference.
 * @author mimo31
 *
 */
public class ObjectsIndex {

	// alphabetically sorted index of all game Items 
	private static List<Item> items = new ArrayList<Item>();
	
	// alphabetically sorted index of all game Structures
	private static List<Structure> structures = new ArrayList<Structure>();
	
	private static List<Recipe> recipes = new ArrayList<Recipe>();
	
	/**
	 * Load all the known Items and Structures into the indexes. Should be called (only) when initializing.
	 */
	public static void loadIndexes()
	{
		// add all Items
		structures.add(new Bush());
		structures.add(new com.github.mimo31.w50rld.structures.Dirt());
		structures.add(new Grass());
		structures.add(new com.github.mimo31.w50rld.structures.Sand());
		structures.add(new Tree());
		structures.add(new Water());
		
		// add all Structures
		items.add(new com.github.mimo31.w50rld.items.Dirt());
		items.add(new GrassPile());
		items.add(new Log());
		items.add(new com.github.mimo31.w50rld.items.Sand());
		items.add(new Sticks());
		items.add(new WoodBlend());
		
		// add all Recipes
		Recipe.addAllRecipes(recipes);
	}
	
	/**
	 * Looks for a Structure with a specified name in the index. If not found, returns null.
	 * @param name of the Structure to look for
	 * @return the Structure with the specified name, null if not found
	 */
	public static Structure getStructure(String name)
	{
		return binarySearch((String s1, String s2) -> s1.compareTo(s2), name, structures, (Structure structure) -> structure.name);
	}
	
	/**
	 * Looks for an Item with a specified name in the index. If not found, returns null.
	 * @param name of the Item to look for
	 * @return the Item with the specified name, null if not found
	 */
	public static Item getItem(String name)
	{
		return binarySearch((String s1, String s2) -> s1.compareTo(s2), name, items, (Item item) -> item.name);
	}
	
	/**
	 * Generic algorithm for binary search.
	 * Take a list of items T and a functions that can figure out a key K from an item T.
	 * It's assumed that the list is sorted by the keys, so that the comparator (another parameter) returns
	 * - a positive Integer when its first argument follows its second argument in the list,
	 * - 0 if its first and second argument are the same,
	 * - a negative Integer if its first argument precedes its second argument.
	 * @param comparator a function that compares two keys
	 * @param keyToSearch key to find
	 * @param items list to search in
	 * @param keyFromItem a function that gets a key out of an item
	 * @return the item with the associated key or null if not found
	 */
	private static <T, K> T binarySearch(BiFunction<K, K, Integer> comparator, K keyToSearch, List<T> items, Function<T, K> keyFromItem)
	{
		int start = 0;
		int end = items.size();
		while (start < end)
		{
			int middle = start + (end - start) / 2;
			T middleItem = items.get(middle);
			int order = comparator.apply(keyToSearch, keyFromItem.apply(middleItem)).intValue();
			if (order == 0)
			{
				return middleItem;
			}
			else if (order < 0)
			{
				end = middle;
			}
			else 
			{
				start = middle + 1;
			}
		}
		return null;
	}
	
	/**
	 * Returns a Recipe whose required Items are the same as the specified required Items (or possibly a permutation of them).
	 * @param requiredItems Item to be required by the Recipe
	 * @return the Recipe with required Items as specified
	 */
	public static Recipe getRecipe(Item[] requiredItems)
	{
		// iterate through all the recipe and look for a match
		for (int i = 0, n = recipes.size(); i < n; i++)
		{
			Recipe currentRecipe = recipes.get(i);
			
			// if the numbers for required items differ, then this Recipes can't be the one we are looking for
			if (requiredItems.length != currentRecipe.requiredItems.length)
			{
				continue;
			}
			boolean matches = true;
			
			// iterate through all the required Items and check if there is a corresponding item
			for (int j = 0; j < requiredItems.length; j++)
			{
				boolean found = false;
				for (int k = 0; k < currentRecipe.requiredItems.length; k++)
				{
					if (requiredItems[j] == currentRecipe.requiredItems[k])
					{
						found = true;
						break;
					}
				}
				if (!found)
				{
					matches = false;
					break;
				}
			}
			
			// if there was found a match for every required Item, then this must the Recipe we are looking for
			if (matches)
			{
				return currentRecipe;
			}
		}
		
		// not Recipe was already returned - no Recipe matches - return null
		return null;
	}
}
