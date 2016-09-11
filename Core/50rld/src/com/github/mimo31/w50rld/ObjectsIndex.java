package com.github.mimo31.w50rld;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.github.mimo31.w50rld.biomes.*;
import com.github.mimo31.w50rld.entities.*;
import com.github.mimo31.w50rld.items.*;
import com.github.mimo31.w50rld.metals.*;
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
	private final static List<Item> items = new ArrayList<Item>();
	
	// alphabetically sorted index of all game Structures
	private final static List<Structure> structures = new ArrayList<Structure>();
	
	// all the recipes, not sorted
	private final static List<Recipe> recipes = new ArrayList<Recipe>();
	
	// all the table recipes sorted by the names of the required items
	private final static List<TableRecipe> tableRecipes = new ArrayList<TableRecipe>();
	
	// all the molten metal recipes sorted primarily by the mold name and secondarily by the metal name
	public final static List<MoltenMetalRecipe> moltenMetalRecipes = new ArrayList<MoltenMetalRecipe>();

	// alphabetically sorted index of all game Biomes 
	public final static List<Biome> biomes = new ArrayList<Biome>();
	
	// all medium structure noises sorted for Biome generation
	// this list's n element is an array of all medium structure noises for the nth Biome (or null if the Biome has not medium structure noises)
	public final static List<Noise[]> mediumStructureNoises = new ArrayList<Noise[]>();
	
	// alphabetically sorted index of all game Entities 
	public final static List<Entity> entities = new ArrayList<Entity>();
	
	// alphabetically sorted index of all game Metals
	public final static List<Metal> metals = new ArrayList<Metal>();
	
	/**
	 * Load all the known Items and Structures into the indexes. Should be called (only) when initializing.
	 */
	public static void loadIndexes()
	{
		// add all Structures
		structures.add(new Bush());
		structures.add(new Carrots());
		structures.add(new com.github.mimo31.w50rld.structures.Chest());
		structures.add(new com.github.mimo31.w50rld.structures.Clay());
		structures.add(new com.github.mimo31.w50rld.structures.Corn());
		structures.add(new DeathGround());
		structures.add(new com.github.mimo31.w50rld.structures.Dirt());
		structures.add(new DriedBrick());
		structures.add(new DryingBrick());
		structures.add(new Grass());
		structures.add(new com.github.mimo31.w50rld.structures.MeltingFurnace());
		structures.add(new com.github.mimo31.w50rld.structures.Sand());
		structures.add(new SeededBush());
		structures.add(new SeededCarrots());
		structures.add(new SeededCorn());
		structures.add(new SeededGrass());
		structures.add(new SeededTree());
		structures.add(new com.github.mimo31.w50rld.structures.Table());
		structures.add(new Tree());
		structures.add(new Water());
		
		// add all Items
		items.add(new BladeMold());
		items.add(new Brick());
		items.add(new BrickForm());
		items.add(new BushSeed());
		items.add(new Carrot());
		items.add(new CarrotSeeds());
		items.add(new com.github.mimo31.w50rld.items.Chest());
		items.add(new com.github.mimo31.w50rld.items.Clay());
		items.add(new Coal());
		items.add(new Cord());
		items.add(new com.github.mimo31.w50rld.items.Corn());
		items.add(new CornSeeds());
		items.add(new DeadAnt());
		items.add(new com.github.mimo31.w50rld.items.Dirt());
		items.add(new FurnaceBase());
		items.add(new FurnaceWall());
		items.add(new GoldOre());
		items.add(new GrassPile());
		items.add(new GrassSeeds());
		items.add(new Gravel());
		items.add(new Grout());
		items.add(new Hammer());
		items.add(new Handle());
		items.add(new IronBlade());
		items.add(new IronOre());
		items.add(new IronSword());
		items.add(new Log());
		items.add(new com.github.mimo31.w50rld.items.MeltingFurnace());
		items.add(new Rock());
		items.add(new RockHammer());
		items.add(new com.github.mimo31.w50rld.items.Sand());
		items.add(new Sticks());
		items.add(new com.github.mimo31.w50rld.items.Table());
		items.add(new TreeSeed());
		items.add(new WetBrick());
		items.add(new WoodBlend());
		
		// add all biomes
		biomes.add(new Death());
		biomes.add(new Desert());
		biomes.add(new Forest());
		biomes.add(new Lake());
		biomes.add(new Plain());
		
		// initialize the medium structure noises
		int noisesTotal = 0;
		for (int i = 0, n = biomes.size(); i < n; i++)
		{
			Biome currentBiome = biomes.get(i);
			if (currentBiome.mediumStructuresScales.length == 0)
			{
				// no medium structure noises
				mediumStructureNoises.add(null);
			}
			else
			{
				Noise[] noises = new Noise[currentBiome.mediumStructuresScales.length];
				for (int j = 0; j < currentBiome.mediumStructuresScales.length; j++)
				{
					// the seed is made to be unique - no shared by the n biomes before (+ n), not be 0 (reserved) (+ 1), 
					// not shared by the medium structure noises of the previous biomes (+ noisesTotal), and not shared by the previous medium structure noises of the current biome (+ j)
					noises[j] = new Noise(Main.SEED + 1 + n + noisesTotal + j, currentBiome.mediumStructuresScales[j]);
				}
				noisesTotal += noises.length;
				mediumStructureNoises.add(noises);
			}
		}
		
		// add all entities
		entities.add(new Ant());
		
		// add all metals
		metals.add(new Gold());
		metals.add(new Iron());
		
		// add all Recipes
		Recipe.addAllRecipes(recipes);
		TableRecipe.addAllRecipes(tableRecipes);
		MoltenMetalRecipe.addAllRecipes(moltenMetalRecipes);
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
	
	/**
	 * Returns a TableRecipe matching the required items.
	 * @param requiredItems required items of the TableRecipe to find
	 * @return a TableRecipe matching the required items
	 */
	public static TableRecipe getTableRecipe(Item[] requiredItems)
	{
		// function that creates a key (String[5]) from an item (TableRecipe)
		Function<TableRecipe, String[]> keyFromItem = (recipe) -> 
		{
			String[] itemNames = new String[5];
			for (int i = 0; i < 5; i++)
			{
				itemNames[i] = recipe.requiredItems[i] == null ? null : recipe.requiredItems[i].name;
			}
			return itemNames;
		};
		
		// comparator that compares two keys (String[5])
		BiFunction<String[], String[], Integer> comparator = (names0, names1) ->
		{
			// go through the 5 Strings and compare the pairs:
			// 	if a pair is equal, go to the next one
			// 	else return based on the String that is alphabetically higher
			for (int i = 0; i < 5; i++)
			{
				String name0 = names0[i];
				String name1 = names1[i];
				if (name0 == name1)
				{
					continue;
				}
				if (name0 == null)
				{
					return new Integer(-1);
				}
				if (name1 == null)
				{
					return new Integer(1);
				}
				int stringCompareValue = name0.compareTo(name1);
				if (stringCompareValue == 0)
				{
					continue;
				}
				return new Integer(stringCompareValue);
			}
			return new Integer(0);
		};
		
		// create the key we're looking for from the required items
		String[] keyToFind = new String[5];
		for (int i = 0; i < 5; i++)
		{
			keyToFind[i] = requiredItems[i] == null ? null : requiredItems[i].name;
		}
		
		// search the index with binary search
		return binarySearch(comparator, keyToFind, tableRecipes, keyFromItem);
	}
	
	/**
	 * Returns an Entity with the corresponding name. If not found, returns null.
	 * @param name the name of the Entity.
	 * @return the Entity with the name or null if not found
	 */
	public static Entity getEntity(String name)
	{
		return binarySearch((s1, s2) -> s1.compareTo(s2), name, entities, entity -> entity.name);
	}
	
	/**
	 * Returns a MoltenMetalRecipe with the specified molten metal and mold item required.
	 * @param moltenMetal molten metal required by the recipe
	 * @param mold mold item required by the recipe
	 * @return the MoltenMetalRecipe recipe according to the requirements or null if no such recipe is in the index
	 */
	public static MoltenMetalRecipe getMoltenMetalRecipe(Metal moltenMetal, Item mold)
	{
		// comparator of to MoltenMetalRecipes
		BiFunction<MoltenMetalRecipe, MoltenMetalRecipe, Integer> comparator = (recipe0, recipe1) -> {
			int moldNameComparison = recipe0.mold.name.compareTo(recipe1.mold.name);
			if (moldNameComparison != 0)
			{
				return moldNameComparison;
			}
			return recipe0.moltenMetal.name.compareTo(recipe1.moltenMetal.name);
		};
		
		// binarySerach the index
		return binarySearch(comparator, new MoltenMetalRecipe(mold, moltenMetal, 0, null), moltenMetalRecipes, recipe -> recipe);
	}
	
	/**
	 * Returns a Metal with the specified name.
	 * @param name name of the Metal
	 * @return Metal with the specified name
	 */
	public static Metal getMetal(String name)
	{
		return binarySearch((s1, s2) -> s1.compareTo(s2), name, metals, metal -> metal.name);
	}
}
