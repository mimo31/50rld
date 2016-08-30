package com.github.mimo31.w50rld;

import java.util.List;

/**
 * Represents a recipe consisting of a mold item and a specified volume molten metal.
 * When the recipe is applied, the specified volume of molten metal is used, but the mold item is not.
 * @author mimo31
 *
 */
public class MoltenMetalRecipe {

	// the mold needed for casting
	public final Item mold;
	
	// the type of Metal needed
	public final Metal moltenMetal;
	
	// the volume of Metal needed [ml]
	public final int metalVolumeRequired;
	
	// the Item that is produced by the recipe
	public final Item resultItem;
	
	public MoltenMetalRecipe(Item mold, Metal moltenMetal, int metalVolumeRequired, Item resultItem)
	{
		this.mold = mold;
		this.moltenMetal = moltenMetal;
		this.metalVolumeRequired = metalVolumeRequired;
		this.resultItem = resultItem;
	}
	
	/**
	 * Adds all known MoltenMetalRecipe to a list.
	 * @param recipes list to add the recipes to
	 */
	public static void addAllRecipes(List<MoltenMetalRecipe> recipes)
	{
		recipes.add(new MoltenMetalRecipe(ObjectsIndex.getItem("Blade Mold"), ObjectsIndex.getMetal("Iron"), 720, ObjectsIndex.getItem("Iron Blade")));
	}
}
