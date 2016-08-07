package com.github.mimo31.w50rld;

import java.util.ArrayList;
import java.util.List;

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
	
	/**
	 * Load all the known items and structures into the indexes. Should be called (only) when initializing.
	 */
	public static void loadIndexes()
	{
		structures.add(new Bush());
		structures.add(new Grass());
		structures.add(new Tree());
	}
	
	/**
	 * Look for a structure with a specified name in the index. If not found, returns null.
	 * @param name of the structure to look for
	 * @return the structure with the specified name, null if not found
	 */
	public static Structure getStructure(String name)
	{
		// binary search the index
		int start = 0;
		int end = structures.size();
		while (start < end)
		{
			int middle = start + (end - start) / 2;
			Structure middleStructure = structures.get(middle);
			int order = name.compareTo(middleStructure.name);
			if (order == 0)
			{
				return middleStructure;
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
}
