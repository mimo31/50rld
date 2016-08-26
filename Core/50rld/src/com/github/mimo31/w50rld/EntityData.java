package com.github.mimo31.w50rld;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

/**
 * Represents one single entity on the map.
 * @author mimo31
 *
 */
public abstract class EntityData {

	// Entity type
	public final Entity entity;
	
	// x location
	public float x;
	
	// y location
	public float y;
	
	// rotation in radians
	public double rotation;
	
	// current number of health points
	public float hp;
	
	// whether the entity is already dead but didn't disappeared yet
	public boolean dead = false;
	
	// state of the color after the entity has been hit, -1 if it hasn't been hit
	private float hitState = -1;
	
	public EntityData(Entity entity, int x, int y)
	{
		this.entity = entity;
		this.x = x + 0.5f;
		this.y = y + 0.5f;
		this.rotation = Math.random() * 2 * Math.PI;
		this.hp = entity.hp;
	}
	
	/**
	 * Hits the entity with the specified amount of hit power.
	 * @param hitPower the number of health points to subtract
	 */
	public void hit(float hitPower)
	{
		// if it is dead, do nothing
		if (this.dead)
		{
			return;
		}
		
		// if the new amount of health points is less that 0, set dead to true
		if (this.hp - hitPower <= 0)
		{
			this.hp = 0;
			this.dead = true;
		}
		
		// else subtract the hit power
		else
		{
			this.hp -= hitPower;
		}
		
		// reset hit state
		this.hitState = 0;
	}
	
	/**
	 * Updates the entity.
	 * @param deltaTime time to cover in the update
	 * @return whether the entity should be removed
	 */
	public boolean update(int deltaTime)
	{
		// if the entity was hit
		if (this.hitState != -1)
		{
			// increment the hit state
			this.hitState += deltaTime / 1024d;
			if (this.hitState > 1)
			{
				if (this.dead)
				{
					// remove the entity
					return true;
				}
				else
				{
					this.hitState = -1;
				}
			}
		}
		
		// update the subclass
		this.updateSub(deltaTime);
		
		// don't remove
		return false;
	}
	
	/**
	 * Method that updates the subclass of this class. It is called in the update method.
	 * @param deltaTime time to cover in the update
	 */
	protected abstract void updateSub(int deltaTime);
	
	
	/**
	 * Draws the entity according to the subclass as a part of drawing the whole entity.
	 * @param g graphics to draw through
	 * @param width width of the entity
	 */
	protected abstract void drawSub(Graphics2D g, int width);
	
	/**
	 * Draws the entity.
	 * @param g graphics to draw through
	 * @param width width of the entity
	 */
	public void draw(Graphics2D g, int width)
	{
		// transparency of the red rectangle over the entity and the entity itself when dying
		float transparency = 1 - this.hitState;
		
		if (this.dead)
		{
			// draw the entity transparent
			Composite originalComposite = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
			this.drawSub(g, width);
			g.setComposite(originalComposite);
		}
		else
		{
			// draw the entity (opaque)
			this.drawSub(g, width);
		}
		
		if (this.hitState != -1)
		{
			// draw the transparent red rectangle over the entity
			Color transparentRed = new Color(255, 0, 0, (int)(255 * transparency));
			g.setColor(transparentRed);
			g.fillRect(0, 0, width, width);
		}
	}
}
