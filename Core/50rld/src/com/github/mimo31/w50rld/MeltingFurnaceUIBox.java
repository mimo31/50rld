package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.TextDraw.TextAlign;
import com.github.mimo31.w50rld.structures.MeltingFurnace.MeltingFurnaceData;

/**
 * Represents a Box with the UI a Melting Furnace.
 * @author mimo31
 *
 */
public class MeltingFurnaceUIBox extends Box {

	// the furnace to work with
	private final MeltingFurnaceData furnace;
	
	// whether the player is currently selecting fuel
	private boolean selectingFuel;
	
	// whether the player is currently selecting the items to melt
	private boolean selectingMetal;
	
	// whether the player is currently selecting the mold to use for casting
	private boolean selectingMold;
	
	public MeltingFurnaceUIBox(float x, float y, MeltingFurnaceData furnace, CornerAlign align) {
		super(x, y);
		this.furnace = furnace;
		super.align(align);
	}

	@Override
	public void draw() {
		// box with a 7 x 8 grid
		
		float contentWidth = 7 * Constants.BOX_TILE_SIZE;
		
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = 8 * tileHeight;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + contentHeight;
		
		// fill the border
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		// draw the headline
		PaintUtils.setDrawColor(Color.orange);
		PaintUtils.drawRectangle(contentX, contentY + 7 * tileHeight, contentWidth, tileHeight);
		TextDraw.drawText("Melting Furnace", contentX, contentY + 7 * tileHeight, contentWidth, tileHeight, TextAlign.LEFT, Constants.BOX_BORDER_SIZE);
		
		// fill the background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, 7 * tileHeight);
		
		// fill the molten metal tank's border
		glColor3f(0, 0, 0);
		PaintUtils.drawRectangle(contentX + Constants.BOX_TILE_SIZE * 3, contentY + tileHeight * 1, Constants.BOX_TILE_SIZE, tileHeight * 5);

		float tankBorderWidth = Constants.BOX_TILE_SIZE / 8;
		float tankBorderHeight = tileHeight / 8;
		float tankContentX = contentX + Constants.BOX_TILE_SIZE * 3 + tankBorderWidth;
		float tankContentBottomY = contentY + tileHeight + tankBorderHeight;
		float tankContentWidth = Constants.BOX_TILE_SIZE - 2 * tankBorderWidth;
		float tankContentHeight = tileHeight * 5 - tankBorderHeight * 2;
		float metalBarHeight = this.furnace.metalVolume * tankContentHeight / 1200;
		
		// fill the tank's background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(tankContentX, tankContentBottomY + metalBarHeight, tankContentWidth, tankContentHeight - metalBarHeight);
		
		// fill the molten metal bar
		if (this.furnace.moltenMetal != null)
		{
			PaintUtils.setDrawColor(this.furnace.moltenMetal.color);
			PaintUtils.drawRectangle(tankContentX, tankContentBottomY, tankContentWidth, metalBarHeight);
		}
		
		// fill the currently melting metal bar
		if (this.furnace.meltState != 0)
		{
			Meltable meltingItem = (Meltable)this.furnace.meltingItems.getItem();
			float newMetalBarHeight = Math.min(meltingItem.getVolume(), 1200 - this.furnace.metalVolume) * tankContentHeight / 1200;
			Color metalColor = meltingItem.getMetal().color;
			glColor4f(metalColor.getRed() / 255f, metalColor.getGreen() / 255f, metalColor.getBlue() / 255f, this.furnace.meltState);
			PaintUtils.drawRectangle(tankContentX, tankContentBottomY + metalBarHeight, tankContentWidth * 0.75f, newMetalBarHeight);
		}
		
		// fill the temperature display's border
		glColor3f(0, 0, 0f);
		PaintUtils.drawRectangle(contentX + 5 * Constants.BOX_TILE_SIZE, contentY + tileHeight * 1, Constants.BOX_TILE_SIZE, tileHeight * 3);
	
		float temperatureDisplayContentHeight = tileHeight * 3 - 2 * tankBorderHeight;
		float temperatureDisplayContentX = contentX + Constants.BOX_TILE_SIZE * 5 + tankBorderWidth;
		
		float temperatureOverMaximum = (this.furnace.temperature - 295) / (1000 - 295);
		float temperatureBarHeight = temperatureDisplayContentHeight * temperatureOverMaximum;
		
		float meltingLimitBarHeight = Math.max(tileHeight / 16, 4 / Gui.height);
		float meltingLimitBarY = contentY + tileHeight + tankBorderHeight + temperatureDisplayContentHeight * (700 - 295) / (1000 - 295) - meltingLimitBarHeight / 2;
		
		// fill the temperature display's background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(temperatureDisplayContentX, contentY + tileHeight + tankBorderHeight + temperatureBarHeight, tankContentWidth, temperatureDisplayContentHeight - temperatureBarHeight);
		
		// fill the temperature bar
		glColor3f(1, 0, 0);
		PaintUtils.drawRectangle(temperatureDisplayContentX, contentY + tileHeight + tankBorderHeight, tankContentWidth, temperatureBarHeight);
	
		// fill the melting limit bar with transparent green
		glColor4f(0, 1, 0, 0.5f);
		PaintUtils.drawRectangle(temperatureDisplayContentX, meltingLimitBarY, tankContentWidth, meltingLimitBarHeight);
		
		// draw the items to melt stack
		ItemStack.drawWithBorder(contentX + Constants.BOX_TILE_SIZE, contentY + 5 * tileHeight, contentX + 2 * Constants.BOX_TILE_SIZE, contentY + 6 * tileHeight, this.selectingMetal ? Color.black : Color.gray, this.furnace.meltingItems);
	
		// draw the fuel items stack
		ItemStack.drawWithBorder(contentX + 5 * Constants.BOX_TILE_SIZE, contentY + 5 * tileHeight, contentX + 6 * Constants.BOX_TILE_SIZE, contentY + 6 * tileHeight, this.selectingFuel ? Color.black : Color.gray, this.furnace.fuel);
		
		// draw the mold item
		Item.drawWithBorderS(contentX + Constants.BOX_TILE_SIZE, contentY + tileHeight, Constants.BOX_TILE_SIZE, tileHeight, this.selectingMold ? Color.black : Color.gray, this.furnace.mold);
	}

	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_BORDER_SIZE + 7 * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return (2 * Constants.BOX_BORDER_SIZE + 8 * Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}

	@Override
	public void keyReleased(int keyCode, Runnable removeAction)
	{
		// is a key with a number was pressed
		if ((keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8) || (keyCode >= GLFW_KEY_KP_1 && keyCode <= GLFW_KEY_KP_8))
		{
			int slotSelected;
			if ((keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8))
			{
				slotSelected = keyCode - GLFW_KEY_1;
			}
			else
			{
				slotSelected = keyCode - GLFW_KEY_KP_1;
			}
			
			// the item from the inventory that was selected
			Item itemSelected = Main.getInventorySlot(slotSelected).getItem();
			
			if (this.selectingFuel)
			{
				if (itemSelected instanceof FuelItem)
				{
					Consumer<String> submitFunction = str -> {
						str = StringUtils.trimZeroes(str);
						int inputLength = str.length();
						
						String errorMessage = null;
						
						if (inputLength == 0)
						{
							errorMessage = "Enter a number.";
						}
						else if (inputLength > 2)
						{
							errorMessage = "Not enough fuel space.";
						}
						else
						{
							// the number of items the player wants to add as fuel
							int inputCount = Integer.parseInt(str);
							
							// check if there is that many items in the inventory
							if (!InventoryUtils.hasEnoughItems(Main.getInventory(), inputCount, itemSelected))
							{
								errorMessage = "Not enough items.";
							}
							else
							{
								// the amount of space available in the fuel stack
								int spaceAvailable;
								
								// if the selected fuel item is the same as the fuel item currently in the fuel stack,
								// then the new items will be added there,
								// else they will replace the old items
								if (this.furnace.fuel.getItem() == itemSelected)
								{
									spaceAvailable = 32 - this.furnace.fuel.getCount();
								}
								else
								{
									spaceAvailable = 32;
								}
								
								// check if there is enough space for that many items
								if (inputCount > spaceAvailable)
								{
									errorMessage = "There isn't enough fuel space.";
								}
								else
								{
									// take the items from the inventory
									InventoryUtils.takeItems(Main.getInventory(), inputCount, itemSelected);
									
									// add the items to the fuel
									if (this.furnace.fuel.getItem() == itemSelected)
									{
										this.furnace.fuel.setCount(32 - spaceAvailable + inputCount);
									}
									else
									{
										// add the old items to the inventory
										Main.map.getTile(Main.playerX, Main.playerY).addInventoryItems(this.furnace.fuel);
										
										this.furnace.fuel.setCount(inputCount);
										this.furnace.fuel.setItem(itemSelected);
									}
									return;
								}
							}
						}
						
						// show the error (if any)
						InfoBox box = new InfoBox(-1 / 8f, 0, errorMessage, CornerAlign.TOPLEFT);
						Main.addBox(box);
					};
					
					// ask the player for the number of the items they want to add as fuel.
					InputBox box = new InputBox(-1 / 8f, 0, "How many " + itemSelected.name + " items?", submitFunction, InputBox.DIGIT_INTERPRETER, CornerAlign.TOPLEFT);
					Main.addBox(box);
				}
				
				// stop selecting fuel
				this.selectingFuel = false;
			}
			else if (this.selectingMetal)
			{
				if (itemSelected instanceof Meltable)
				{
					Meltable meltable = (Meltable)itemSelected;
					
					// if the there is no molten metal
					// or if the molten metal is the same as the one produced by melting the the chosen item
					if (this.furnace.moltenMetal == null || meltable.getMetal() == this.furnace.moltenMetal)
					{
						Consumer<String> submitFunction = str ->
						{
							str = StringUtils.trimZeroes(str);
							int inputLength = str.length();
							String errorMessage = null;
							if (inputLength == 0)
							{
								errorMessage = "Enter a number.";
							}
							else if (inputLength > 2)
							{
								errorMessage = "Not enough space for items to melt.";
							}
							else
							{
								// the number of items the player wants to melt
								int itemCount = Integer.parseInt(str);
								if (!InventoryUtils.hasEnoughItems(Main.getInventory(), itemCount, itemSelected))
								{
									errorMessage = "Not enough items.";
								}
								else 
								{
									// the amount of space available in the melting items stack
									int spaceAvailable;
									
									if (this.furnace.meltingItems.getItem() == itemSelected)
									{
										spaceAvailable = 32 - this.furnace.meltingItems.getCount();
									}
									else
									{
										spaceAvailable = 32;
									}
									
									// check if there is enough space for the items
									if (spaceAvailable < itemCount)
									{
										errorMessage = "Not enough space for items to melt.";
									}
									else
									{
										// remove the items from the inventory
										InventoryUtils.takeItems(Main.getInventory(), itemCount, itemSelected);
										
										// add the to the melting items stack
										if (this.furnace.meltingItems.getItem() == itemSelected)
										{
											this.furnace.meltingItems.setCount(32 - spaceAvailable + itemCount);
										}
										else
										{
											// add the old items to the inventory
											Main.map.getTile(Main.playerX, Main.playerY).addInventoryItems(this.furnace.meltingItems);
											
											this.furnace.meltingItems.setCount(itemCount);
											this.furnace.meltingItems.setItem(itemSelected);
										}
										return;
									}
								}
							}
							
							// show the error message (if any)
							InfoBox box = new InfoBox(-1 / 8f, 0, errorMessage, CornerAlign.TOPLEFT);
							Main.addBox(box);
						};
						
						// ask the player for the number of item they want to melt
						InputBox box = new InputBox(-1 / 8f, 0, "How many " + itemSelected.name + " items?", submitFunction, InputBox.DIGIT_INTERPRETER, CornerAlign.TOPLEFT);
						Main.addBox(box);
					}
				}
				
				// stop selecting the items to melt
				this.selectingMetal = false;
			}
			else if (this.selectingMold)
			{
				if (itemSelected != null)
				{
					// decrement the number of items in the selected inventory slot
					Main.getInventorySlot(slotSelected).setCount(Main.getInventorySlot(slotSelected).getCount() - 1);
					
					// add the old mold to the inventory (if there is an mold)
					if (this.furnace.mold != null)
					{
						Main.map.getTile(Main.playerX, Main.playerY).addInventoryItems(new ItemStack(this.furnace.mold, 1));
					}
					
					
					this.furnace.mold = itemSelected;
				}
				
				// stop selecting the mold
				this.selectingMold = false;
			}
		}
		// start selecting the items to melt
		else if (keyCode == GLFW_KEY_I)
		{
			this.selectingMetal = true;
			this.selectingFuel = false;
			this.selectingMold = false;
		}
		// start selecting the mold
		else if (keyCode == GLFW_KEY_M)
		{
			this.selectingMold = true;
			this.selectingFuel = false;
			this.selectingMetal = false;
		}
		// start selecting the fuel items
		else if (keyCode == GLFW_KEY_F)
		{
			this.selectingFuel = true;
			this.selectingMetal = false;
			this.selectingMold = false;
		}
		// clear (after confirmation) the molten metal storage
		else if (keyCode == GLFW_KEY_C)
		{
			// stop selecting anything
			this.selectingFuel = false;
			this.selectingMetal = false;
			this.selectingMold = false;
			
			if (this.furnace.moltenMetal != null)
			{
				// add a Box that clears the molten metal tank if confirmed
				ConfirmBox box = new ConfirmBox(-1 / 8f, 0, "Really want to remove all the liquid: " + this.furnace.moltenMetal.name + "?", confirmed -> {
					if (confirmed)
					{
						this.furnace.moltenMetal = null;
						this.furnace.metalVolume = 0;
					}
				});
				Main.addBox(box);
			}
		}
	}
}
