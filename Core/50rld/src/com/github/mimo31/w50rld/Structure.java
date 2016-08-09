package com.github.mimo31.w50rld;

import java.awt.Graphics2D;

/**
 * Represents a Structure on the ground. Its subclasses are intended to only be instantiated once during the game - when loading indexes.
 */
public abstract class Structure {

	// name of the Structure
	public final String name;
	
	// indicates whether the structure takes up the whole Tile when drawn
	public final boolean overdraws;
	
	// action that can be done on this structure by the player
	public final StructureAction[] actions;
	
	// represents the smoothness of the top of the Structure, 
	// useful when deciding whether another Structure can be placed on top of it
	public final float smoothness;
	
	/**
	 * Draws the Structure.
	 * @param g graphics to draw through
	 * @param x x coordinate of the location to draw
	 * @param y y coordinate of the location to draw
	 * @param width width of the rectangle to draw
	 * @param height height of the rectangle to draw
	 */
	public abstract void draw(Graphics2D g, int x, int y, int width, int height);

	public Structure(String name, boolean overdraws, StructureAction[] actions)
	{
		this(name, overdraws, actions, 0);
	}
	
	public Structure(String name, boolean overdraws, StructureAction[] actions, float smoothness)
	{
		this.name = name;
		this.overdraws = overdraws;
		this.actions = actions;
		this.smoothness = smoothness;
	}
	
	public Structure(String name, StructureAction[] actions)
	{
		this(name, false, actions);
	}
	
	/**
	 * Represents an action that can be done with a Structure by the player.
	 * @author mimo31
	 *
	 */
	public static abstract class StructureAction {	
		
		// name of the action that will be displayed to the player
		final String name;
		
		public StructureAction(String name)
		{
			this.name = name;
		}

		// action to perform
		public abstract void action(int tileX, int tileY);
	}
}
