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
	
	public Entity(String name, float hp)
	{
		this.name = name;
		this.hp = hp;
	}
	
	/**
	 * Creates an entity of this Entity type with the specified location.
	 * @param x x coordinate of the location of the entity
	 * @param y y coordinate of the location of the entity
	 * @return the created entity
	 */
	public abstract EntityData createEntityData(int x, int y);
}
