package com.github.mimo31.w50rld.structures;

import java.awt.Graphics2D;

import com.github.mimo31.w50rld.FuelItem;
import com.github.mimo31.w50rld.Item;
import com.github.mimo31.w50rld.ItemStack;
import com.github.mimo31.w50rld.Main;
import com.github.mimo31.w50rld.Meltable;
import com.github.mimo31.w50rld.MeltingFurnaceUIBox;
import com.github.mimo31.w50rld.Metal;
import com.github.mimo31.w50rld.MoltenMetalRecipe;
import com.github.mimo31.w50rld.ObjectsIndex;
import com.github.mimo31.w50rld.PaintUtils;
import com.github.mimo31.w50rld.Structure;
import com.github.mimo31.w50rld.StructureData;
import com.github.mimo31.w50rld.Tile;

/**
 * Represents the Melting Furnace Structure. The Structure can be used to melt metals and cast the molten metal with molds.
 * @author mimo31
 *
 */
public class MeltingFurnace extends Structure {

	public MeltingFurnace() {
		super("Melting Furnace", false, new StructureAction[] {
				
				new StructureAction("Use")
				{

					@Override
					public void action(int tileX, int tileY) {
						MeltingFurnaceData furnaceData = (MeltingFurnaceData)Main.map.getTile(tileX, tileY).getTopStructure();
						Main.addBox(new MeltingFurnaceUIBox(7 / 16f, 1 / 2f, furnaceData));
					}
					
				},
				new StructureAction("Deconstruct")
				{
					
					@Override
					public void action(int tileX, int tileY) {
						Tile tile = Main.map.getTile(tileX, tileY);
						tile.popStructure();
						tile.addItems(new ItemStack(ObjectsIndex.getItem("Melting Furnace"), 1));
					}
					
				}
				
		}, -2);
	}

	@Override
	public void draw(Graphics2D g, int x, int y, int width, int height, int tileX, int tileY, int structureNumber) {
		PaintUtils.drawSquareTexture(g, x, y, width, height, "MeltingFurnaceS.png");
	}

	@Override
	public StructureData createStructureData()
	{
		return new MeltingFurnaceData();
	}
	
	/**
	 * Subclass of StructureData to handle structure data of the Melting Furnace Structure.
	 * @author mimo31
	 *
	 */
	public static class MeltingFurnaceData extends StructureData {

		// the molten metal currently stored, or null if no metal is stored
		public Metal moltenMetal = null;
		
		// the stack of items to melt
		public final ItemStack meltingItems = new ItemStack(null, 0);
		
		// the item used as a mold
		public Item mold = null;
		
		// the stack of fuel to burn
		public final ItemStack fuel = new ItemStack(null, 0);
		
		// the temperature in the furnace
		public float temperature = 295;
		
		// the volume of the metal currently stored [ml]
		public int metalVolume = 0;
		
		// the remaining burn time of the fuel item that is burning
		public int remainingBurnTime = 0;
		
		// the state from 0 to 1 of the melting of the item that is being melted
		public float meltState = 0;
		
		public MeltingFurnaceData() {
			super(ObjectsIndex.getStructure("Melting Furnace"));
		}
		
		public void update(int tileX, int tileY, int deltaTime)
		{
			// decrement the temperature but not below 295 K
			this.temperature = Math.max(this.temperature - 0.001f * deltaTime, 295);
			
			// if the fuel item will not be burnt in this update
			if (this.remainingBurnTime > deltaTime)
			{
				// decrement the remaining burn time 
				this.remainingBurnTime -= Math.min((1000 - this.temperature) * 100, deltaTime);
				
				// increment the temperature but not above 1000 K
				this.temperature = Math.min(deltaTime * 0.01f + this.temperature, 1000);
			}
			else
			{
				// increment the temperature by the remaining burn time but not above 1000 K
				this.temperature = Math.min(this.remainingBurnTime * 0.01f + this.temperature, 1000);
				
				// if the number of remaining fuel item is not 0, decrement it and reset the remaining burn time, else set the remaining burn time to 0
				int fuelCount = this.fuel.getCount();
				if (fuelCount != 0)
				{
					this.remainingBurnTime = ((FuelItem)this.fuel.getItem()).getBurnTime();
					this.fuel.setCount(fuelCount - 1);
				}
				else
				{
					this.remainingBurnTime = 0;
				}
			}
			// if there are some items to melt, the temperature is high enough and the metal tank is not full
			if (this.meltingItems.getCount() != 0 && this.temperature >= 700 && this.metalVolume != 1200)
			{
				// increment the melt state
				this.meltState += deltaTime * 0.0001f;
				
				if (this.meltState >= 1)
				{
					Meltable meltable = (Meltable) this.meltingItems.getItem();
					this.moltenMetal = meltable.getMetal();
					this.metalVolume = Math.min(this.metalVolume + meltable.getVolume(), 1200);
					this.meltingItems.setCount(this.meltingItems.getCount() - 1);
					
					// if there are more items to melt, subtract 1 from the melt state, else set melt state to 0
					this.meltState = this.meltingItems.getCount() == 0 || this.metalVolume == 1200 ? 0 : this.meltState - 1;
				}
			}
			// if the temperature is not high enough, set the melt state to 0
			else if (this.temperature < 700)
			{
				this.meltState = 0;
			}
			// if there is a mold and some molten metal
			if (this.mold != null && this.moltenMetal != null)
			{
				// get possible recipe
				MoltenMetalRecipe recipe = ObjectsIndex.getMoltenMetalRecipe(this.moltenMetal, this.mold);
				
				// if such recipe was found
				if (recipe != null)
				{
					// if there is enough molten metal, subtract the amount of molten metal required and put the result item on the ground at this Tile
					if (recipe.metalVolumeRequired <= this.metalVolume)
					{
						this.metalVolume -= recipe.metalVolumeRequired;
						if (this.metalVolume == 0)
						{
							this.moltenMetal = null;
						}
						Main.map.getTile(tileX, tileY).addItems(new ItemStack(recipe.resultItem, 1));
					}
				}
			}
		}
	}
	
}
