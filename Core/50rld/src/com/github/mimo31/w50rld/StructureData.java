package com.github.mimo31.w50rld;

/**
 * Represents an object unique to every Structure on the map. Holds a reference to the Structure it is implementing 
 * and possibly some more variables in its subclasses.
 * @author mimo31
 *
 */
public class StructureData {

	public final Structure structure;
	
	public StructureData(Structure structure)
	{
		if (structure == null)
		{
			throw new NullPointerException("The structure is null.");
		}
		this.structure = structure;
	}
	
	/**
	 * Updates the Structure.
	 * @param tileX x coordinate of the Tile the structure is on
	 * @param tileY y coordinate of the Tile the structure is on
	 * @param deltaTime time difference to cover in the update
	 */
	public void update(int tileX, int tileY, int deltaTime)
	{
		
	}
	
}
