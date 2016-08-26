package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;

public class DeathGround extends Structure {

	public DeathGround() {
		super("Death Ground", true, new StructureAction[0], 0);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "DeathGround.png");
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
		public void update(int tileX, int tileY, int deltaTime)
		{
			if (Math.random() < 0.00001)
			{
				Main.entities.add(ObjectsIndex.getEntity("Ant").createEntityData(tileX, tileY));
			}
		}
	}
}
