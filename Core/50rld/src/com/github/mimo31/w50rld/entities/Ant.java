package com.github.mimo31.w50rld.entities;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Entity;
import com.github.mimo31.w50rld.EntityData;
import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;

/**
 * Represent the Ant Entity.
 * @author mimo31
 *
 */
public class Ant extends Entity {

	public Ant() {
		// name "Ant", 3 hp, drops 1 Dead Ant item
		super("Ant", 3, new Item[] { ObjectsIndex.getItem("Dead Ant") }, new int[] { 1 }, new int[] { 1 });
	}

	@Override
	public EntityData createEntityData(int x, int y) {
		return new AntData(x, y);
	}
	
	/**
	 * Represents EntityData for an Ant.
	 * @author mimo31
	 *
	 */
	private static class AntData extends ApproachingEntityData {
		
		public AntData(int x, int y) {
			// Ant Entity, speed 1, detection range 5, hit power 1, attack interval 3000 ms
			super(ObjectsIndex.getEntity("Ant"), x, y, 1, 5, 1, 3000);
		}

		@Override
		protected void drawSub(Graphics2D g, int width) {
			// draw the moving texture it is moving and the time modulo 150 is less the 75 to swap the textures every 75 milliseconds
			String textureName = super.moving && System.currentTimeMillis() % 150 < 75 ? "AntMov.png" : "Ant.png";
			PaintUtils.drawSquareTexture(g, 0, 0, width, width, textureName);
		}
	}
}
