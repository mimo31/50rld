package com.github.mimo31.w50rld.entities;

import com.github.mimo31.w50rld.Entity;
import com.github.mimo31.w50rld.EntityData;
import com.github.mimo31.w50rld.Main;

/**
 * A subclass of EntityData implementing the update method to make the Entity follow the player.
 * @author mimo31
 *
 */
public abstract class ApproachingEntityData extends EntityData {

	// the maximum number of Tiles the player can be away to be followed
	private final float detectionRange;
	
	// maximum speed
	private final float speed;
	
	// whether the entity is currently moving
	public boolean moving;
	
	// the last time the entity attacked the player
	private long lastAttack = 0;
	
	// the number of hp the entity takes of the player health when attacking
	private final int hitPower;
	
	// the minimum number of millisecond between two attacks on the player
	private final int attackInterval;
	
	// the distance to move in the current random move
	private float moveDistance;
	
	// whether the entity is currently randomly moving
	private boolean moveTargetSet = false;
	
	public ApproachingEntityData(Entity entity, int x, int y, float speed, float detectionRange, int hitPower, int attackInterval) {
		super(entity, x, y);
		this.detectionRange = detectionRange;
		this.speed = speed;
		this.hitPower = hitPower;
		this.attackInterval = attackInterval;
	}

	@Override
	protected void updateSub(int deltaTime) {
		if (this.dead)
		{
			this.moving = false;
			return;
		}
		
		// not moving as default
		this.moving = false;
		
		// return if the player is out of the detection range
		double playerXDistance = Main.playerX + 0.5 - super.x;
		double playerYDistance = Main.playerY + 0.5 - super.y;
		double playerDistance = Math.sqrt(Math.pow(playerXDistance, 2) + Math.pow(playerYDistance, 2));
		if (!Main.deadScreen && playerDistance <= this.detectionRange)
		{
			// rotate to the player
			super.rotation = Math.atan2(playerYDistance, playerXDistance);
			
			// distance that the entity will travel limited by the distance to the player
			double distanceToMove = Math.min(deltaTime * this.speed / 512d, playerDistance - 0.7);
			
			// if the distance to move is tiny, return to avoid tiny shakes
			if (distanceToMove > 0.0001)
			{
				// move
				super.x += Math.cos(super.rotation) * distanceToMove;
				super.y += Math.sin(super.rotation) * distanceToMove;
				
				this.moving = true;
			}
		}
		else
		{
			if (this.moveTargetSet)
			{
				double distanceToMove = Math.min(deltaTime * this.speed / 1024d, this.moveDistance);
				if (distanceToMove > 0.0001)
				{
					// move
					super.x += Math.cos(super.rotation) * distanceToMove;
					super.y += Math.sin(super.rotation) * distanceToMove;
					
					this.moveDistance -= distanceToMove;
					
					this.moving = true;
				}
				else
				{
					this.moving = false;
					this.moveTargetSet = false;
				}
			}
			if (Math.pow(0.9999, deltaTime * this.speed) < Math.random())
			{
				this.rotation = Math.random() * 2 * Math.PI;
				this.moveDistance = (float) (Math.random() * 4);
				this.moveTargetSet = true;
			}
		}
		
		// if the player is near, hit can occur
		if (playerDistance < 1)
		{
			long currentTime = System.currentTimeMillis();
			if (this.attackInterval + this.lastAttack < currentTime)
			{
				Main.hitPlayer(this.hitPower);
				this.lastAttack = currentTime;
			}
		}
	}
}
