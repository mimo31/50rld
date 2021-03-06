package com.github.mimo31.w50rld.structures;

import com.github.mimo31.w50rld.Constants;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;

/**
 * Represents the Death Ground Structure.
 * @author mimo31
 *
 */
public class DeathGround extends Structure {

	public DeathGround() {
		super("Death Ground", true, new StructureAction[0], 0);
	}

	@Override
	public void draw(float startx, float starty, float endx, float endy, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawTexture(startx, starty, endx, endy, "DeathGround");
	}
	
	@Override
	public StructureData createStructureData()
	{
		return new DeathGroundData();
	}

	private static class DeathGroundData extends StructureData 
	{

		public DeathGroundData() {
			super(ObjectsIndex.getStructure("Death Ground"));
		}
		
		@Override
		public void update(int tileX, int tileY, int deltaTime, int structureNumber)
		{
			if (Math.random() < 0.00001 && Math.sqrt(Math.pow(tileX - Main.playerX, 2) + Math.pow(tileY - Main.playerY, 2)) >= Constants.NO_SPAWN_DISTANCE)
			{
				Main.entities.add(ObjectsIndex.getEntity("Ant").createEntityData(tileX, tileY));
			}
		}
	}
}
