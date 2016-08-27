package com.github.mimo31.w50rld;

/**
 * An abstract class providing an abstract method for creating an EntityData.
 * @author mimo31
 *
 */
public abstract class Entity {

	// name of the Entity
	public final String name;
	
	// number of health point of the Entity
	public final float hp;
	
	// the Items the Entity can drop when killed
	public final Item[] drops;
	
	// the minimal number of each Item the Entity must drop when killed
	public final int[] minDropAmounts;

	// the maximal number of each Item the Entity can drop when killed
	public final int[] maxDropAmounts;
	
	public Entity(String name, float hp, Item[] drops, int[] minDropAmounts, int[] maxDropAmounts)
	{
		this.name = name;
		this.hp = hp;
		this.drops = drops;
		this.minDropAmounts = minDropAmounts;
		this.maxDropAmounts = maxDropAmounts;
	}
	
	/**
	 * Creates an entity of this Entity type with the specified location.
	 * @param x x coordinate of the location of the entity
	 * @param y y coordinate of the location of the entity
	 * @return the created entity
	 */
	public abstract EntityData createEntityData(int x, int y);

	/**
	 * Returns the items that should be left on the ground after the entity is killed.
	 * It's a random selection, so its isn't same all the time.
	 * @return an array of Items of counts to drop when the entity is killed
	 */
	public ItemStack[] getDrops()
	{
		if (this.drops == null)
		{
			return new ItemStack[0];
		}
		ItemStack[] drops = new ItemStack[this.drops.length];
		for (int i = 0; i < drops.length; i++)
		{
			drops[i] = new ItemStack(this.drops[i],
					/* a random number between this.minDropAmounts[i] and this.maxDropAmounts[i] */
					this.minDropAmounts[i] + (int) (Math.random() * this.maxDropAmounts[i]));
		}
		return drops;
	}
}
