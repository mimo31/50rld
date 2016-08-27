package com.github.mimo31.w50rld;

/**
 * Interface providing methods that must be implemented by an Item that can be used as a weapon.
 * @author mimo31
 *
 */
public interface WeaponItem {

	/**
	 * The number of health points to subtract from the enemy.
	 * @return the number of health points to take down
	 */
	float getHitPower();
	
	/**
	 * The number of Tiles away from the player where the enemies are still being hit.
	 * @return the radius of the hit area
	 */
	float getHitRadius();
	
}
