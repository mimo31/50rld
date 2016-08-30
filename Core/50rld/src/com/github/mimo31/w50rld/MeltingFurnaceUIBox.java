package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.StringDraw.TextAlign;
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
	
	public MeltingFurnaceUIBox(float x, float y, MeltingFurnaceData furnace) {
		super(x, y);
		this.furnace = furnace;
	}

	@Override
	public void draw(Graphics2D g, int width, int height) {
		int locX = (int) (super.x * width);
		int locY = (int) (super.y * height);
		
		int borderSize = width / 6 / 64;
		
		int gridSize = width / 36;
		
		int contentWidth = gridSize * 7;
		
		int contentHeight = gridSize * 8;
		
		// fill the border
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, contentWidth + 2 * borderSize, contentHeight + 2 * borderSize);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		
		// draw the headline
		g.setColor(Color.orange);
		Rectangle headlineBounds = new Rectangle(contentX, contentY, contentWidth, gridSize);
		g.fill(headlineBounds);
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, borderSize * 2, "Melting Furnace", TextAlign.LEFT, headlineBounds);
		
		// fill the background
		g.setColor(Color.white);
		g.fillRect(contentX, contentY + gridSize, contentWidth, contentHeight - gridSize);
		
		// fill the molten metal tank's border
		g.setColor(Color.black);
		g.fillRect(contentX + gridSize * 3, contentY + gridSize * 2, gridSize, gridSize * 5);

		int tankBorderSize = gridSize / 8;
		int tankContentX = contentX + gridSize * 3 + tankBorderSize;
		int tankContentTopY = contentY + gridSize * 2 + tankBorderSize;
		int tankContentBottomY = contentY + gridSize * 7 - tankBorderSize;
		int tankContentWidth = gridSize - 2 * tankBorderSize;
		int tankContentHeight = gridSize * 5 - tankBorderSize * 2;
		int metalBarHeight = this.furnace.metalVolume * tankContentHeight / 1200;
		
		// fill the tank's background
		g.setColor(Color.white);
		g.fillRect(tankContentX, tankContentTopY, tankContentWidth, tankContentHeight - metalBarHeight);
		
		// fill the molten metal bar
		if (this.furnace.moltenMetal != null)
		{
			g.setColor(this.furnace.moltenMetal.color);
			g.fillRect(tankContentX, tankContentBottomY - metalBarHeight, tankContentWidth, metalBarHeight);
		}
		
		// fill the currently melting metal bar
		if (this.furnace.meltState != 0)
		{
			Meltable meltingItem = (Meltable)this.furnace.meltingItems.getItem();
			int newMetalBarHeight = Math.min(meltingItem.getVolume(), 1200 - this.furnace.metalVolume) * tankContentHeight / 1200;
			Color metalColor = meltingItem.getMetal().color;
			g.setColor(new Color(metalColor.getRed(), metalColor.getGreen(), metalColor.getBlue(), (int) (255 * this.furnace.meltState)));
			g.fillRect(tankContentX, tankContentBottomY - metalBarHeight - newMetalBarHeight, tankContentWidth * 3 / 4, newMetalBarHeight);
		}
		
		// fill the temperature display's border
		g.setColor(Color.black);
		g.fillRect(contentX + gridSize * 5, contentY + gridSize * 4, gridSize, gridSize * 3);
	
		int temperatureDisplayContentHeight = gridSize * 3 - 2 * tankBorderSize;
		int temperatureDisplayContentX = contentX + gridSize * 5 + tankBorderSize;
		
		float temperatureOverMaximum = (this.furnace.temperature - 295) / (1000 - 295);
		int temperatureBarHeight = (int) (temperatureDisplayContentHeight * temperatureOverMaximum);
		
		int meltingLimitBarHeight = Math.max(gridSize / 16, 2);
		int meltingLimitBarY = contentY + gridSize * 7 - tankBorderSize - (int) (temperatureDisplayContentHeight * (700 - 295) / (1000 - 295)) - meltingLimitBarHeight / 2;
		
		// fill the temperature display's background
		g.setColor(Color.white);
		g.fillRect(temperatureDisplayContentX, contentY + gridSize * 4 + tankBorderSize, tankContentWidth, temperatureDisplayContentHeight - temperatureBarHeight);
		
		// fill the temperature bar
		g.setColor(Color.red);
		g.fillRect(temperatureDisplayContentX, contentY + gridSize * 7 - tankBorderSize - temperatureBarHeight, tankContentWidth, temperatureBarHeight);
	
		// fill the melting limit bar with transparent green
		g.setColor(new Color(0, 255, 0, 127));
		g.fillRect(temperatureDisplayContentX, meltingLimitBarY, tankContentWidth, meltingLimitBarHeight);
		
		// draw the items to melt stack
		ItemStack.drawWithBorder(g, contentX + gridSize, contentY + 2 * gridSize, gridSize, gridSize, this.selectingMetal ? Color.black : Color.gray, this.furnace.meltingItems);
	
		// draw the fuel items stack
		ItemStack.drawWithBorder(g, contentX + gridSize * 5, contentY + 2 * gridSize, gridSize, gridSize, this.selectingFuel ? Color.black : Color.gray, this.furnace.fuel);
		
		// draw the mold item
		Item.drawWithBorder(g, contentX + gridSize, contentY + gridSize * 6, gridSize, gridSize, this.selectingMold ? Color.black : Color.gray, this.furnace.mold);
	}

	@Override
	protected Dimension getSize(int width, int height) {
		int borderSize = width / 6 / 64;
		int gridSize = width / 36;
		return new Dimension(gridSize * 7 + 2 * borderSize, gridSize * 8 + 2 * borderSize);
	}

	@Override
	public void key(KeyEvent event, Runnable removeAction)
	{
		int keyCode = event.getKeyCode();
		// is a key with a number was pressed
		if ((keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8) || (keyCode >= KeyEvent.VK_NUMPAD1 && keyCode <= KeyEvent.VK_NUMPAD8))
		{
			int slotSelected;
			if ((keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8))
			{
				slotSelected = keyCode - KeyEvent.VK_1;
			}
			else
			{
				slotSelected = keyCode - KeyEvent.VK_NUMPAD1;
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
							errorMessage = "You must enter a number.";
						}
						else if (inputLength > 2)
						{
							errorMessage = "There isn't enough space for that many items.";
						}
						else
						{
							// the number of items the player wants to add as fuel
							int inputCount = Integer.parseInt(str);
							
							// check if there is that many items in the inventory
							if (!InventoryUtils.hasEnoughItems(Main.getInventory(), inputCount, itemSelected))
							{
								errorMessage = "You don't have that many items.";
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
									errorMessage = "There isn't enough space for that many items.";
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
						InfoBox box = new InfoBox(7 / 16f, 1 / 2f, errorMessage);
						Main.addBox(box);
					};
					
					// ask the player for the number of the items they want to add as fuel.
					InputBox box = new InputBox(7 / 16f, 1 / 2f, "How many " + itemSelected.name + " items would you like to add as fuel?", submitFunction, InputBox.DIGIT_FILTER);
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
								errorMessage = "You must enter a number.";
							}
							else if (inputLength > 2)
							{
								errorMessage = "There isn't enough space for that many items.";
							}
							else
							{
								// the number of items the player wants to melt
								int itemCount = Integer.parseInt(str);
								if (!InventoryUtils.hasEnoughItems(Main.getInventory(), itemCount, itemSelected))
								{
									errorMessage = "You don't have that many items.";
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
										errorMessage = "There isn't enought space for that many items.";
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
							InfoBox box = new InfoBox(7 / 16f, 1 / 2f, errorMessage);
							Main.addBox(box);
						};
						
						// ask the player for the number of item they want to melt
						InputBox box = new InputBox(7 / 16f, 1 / 2f, "How many " + itemSelected.name + " items would you like to melt?", submitFunction, InputBox.DIGIT_FILTER);
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
		else if (keyCode == KeyEvent.VK_I)
		{
			this.selectingMetal = true;
			this.selectingFuel = false;
			this.selectingMold = false;
		}
		// start selecting the mold
		else if (keyCode == KeyEvent.VK_M)
		{
			this.selectingMold = true;
			this.selectingFuel = false;
			this.selectingMetal = false;
		}
		// start selecting the fuel items
		else if (keyCode == KeyEvent.VK_F)
		{
			this.selectingFuel = true;
			this.selectingMetal = false;
			this.selectingMold = false;
		}
		// clear (after confirmation) the molten metal storage
		else if (keyCode == KeyEvent.VK_C)
		{
			// stop selecting anything
			this.selectingFuel = false;
			this.selectingMetal = false;
			this.selectingMold = false;
			
			if (this.furnace.moltenMetal != null)
			{
				// add a Box that clears the molten metal tank if confirmed
				ConfirmBox box = new ConfirmBox(7 / 16f, 1 / 2f, "Are you sure that you want to remove all the liquid " + this.furnace.moltenMetal.name + "?", confirmed -> {
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
