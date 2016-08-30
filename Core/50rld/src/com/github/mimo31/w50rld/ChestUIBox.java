package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.StringDraw.TextAlign;

/**
 * Represents a Box with the UI of a Chest.
 * @author mimo31
 *
 */
public class ChestUIBox extends Box {

	// items of the Chest that are about to be changed by the player
	private final ItemStack[] items;
	
	// whether the player is currently selecting an Item from the inventory to move to the Chest
	private boolean selectingFromInventory = false;
	
	// the row of the Chest that is selected, -1 if no row is selected
	private int rowSelected = -1;
	
	
	public ChestUIBox(float x, float y, ItemStack[] items) {
		super(x, y);
		this.items = items;
	}

	@Override
	public void draw(Graphics2D g, int width, int height) {
		int locX = (int) (super.x * width);
		int locY = (int) (super.y * height);
		
		int borderSize = width / 6 / 64;
		
		int gridSize = width / 36;
	
		int contentWidth = /* 4 Item slots */gridSize * 4 + /* to border on the sides */gridSize / 2 * 2 + /* 3 spaces between the slots */gridSize / 4 * 3;
		int contentHeight = contentWidth + gridSize; // the width plus the height of the headline
		
		// fill the Box's borders
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, contentWidth + 2 * borderSize, contentHeight + 2 * borderSize);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		
		// bounds of the headline
		Rectangle headlineBounds = new Rectangle(contentX, contentY, contentWidth, gridSize);
		
		g.setColor(Color.orange);
		g.fill(headlineBounds);
		
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, borderSize * 2, "Chest", TextAlign.LEFT, headlineBounds);
		
		// draw the background gray if selecting from the inventory, white otherwise
		g.setColor(this.selectingFromInventory ? Color.lightGray : Color.white);
		g.fillRect(contentX, contentY + gridSize, contentWidth, contentWidth);
		
		// draw the Item slots
		for (int i = 0; i < 4; i++)
		{
			Color borderColor = this.rowSelected == i ? Color.red : Color.black;
			// y coordinate of the slots in this row
			int drawY = contentY + gridSize + gridSize / 2 + (gridSize / 4 + gridSize) * i;
			for (int j = 0; j < 4; j++)
			{
				ItemStack currentStack = this.items[j + i * 4];
				int drawX = contentX + gridSize / 2 + (gridSize / 4 + gridSize) * j;
				ItemStack.drawWithBorder(g, drawX, drawY, gridSize, gridSize, borderColor, currentStack);
			}
		}
	}

	@Override
	protected Dimension getSize(int width, int height) {
		int borderSize = width / 6 / 64;
		
		int gridSize = width / 36;
		
		int contentWidth = gridSize * 4 + gridSize / 2 * 2 + gridSize / 4 * 3;
		int contentHeight = contentWidth + gridSize;
		
		return new Dimension(contentWidth + 2 * borderSize, contentHeight + 2 * borderSize);
	}

	@Override
	public void key(KeyEvent event, Runnable closeAction)
	{
		int keyCode = event.getKeyCode();
		if (this.selectingFromInventory)
		{
			// if the player is selecting from the inventory and pressed a valid number from 1 to 8
			if ((keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8) || (keyCode >= KeyEvent.VK_NUMPAD1 && keyCode <= KeyEvent.VK_NUMPAD8))
			{
				// the number of the inventory slot the player has selected
				int slotSelected = (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8) ? keyCode - KeyEvent.VK_1 : keyCode - KeyEvent.VK_NUMPAD1;
				
				// the Item the player wants to move to the Chest
				Item item = Main.getInventorySlot(slotSelected).getItem();
				
				// if they selected an empty slot, stop selecting from inventory and return
				if (item == null)
				{
					this.selectingFromInventory = false;
					return; 
				}
				
				// function to submit the number of items to move as the number to the move between inventories function
				Consumer<String> submitFunction = (str) ->
				{
					InventoryUtils.tryMoveItemsBetweenInventories(Main.getInventory(), this.items, item, str);
				};
				
				// ask the player how many items they want to move
				InputBox box = new InputBox(7 / 16f, 1 / 2f, "How many items would you like to move to the chest?", submitFunction, InputBox.DIGIT_FILTER);
			
				Main.addBox(box);
			
				this.selectingFromInventory = false;
			}
			
			// if the player is selecting from the inventory and pressed the back space, then stop selecting from the inventory
			else if (keyCode == KeyEvent.VK_BACK_SPACE)
			{
				this.selectingFromInventory = false;
			}
		}
		else
		{
			// if the player pressed the I key, stop start selecting from the inventory
			if (keyCode == KeyEvent.VK_I)
			{
				this.selectingFromInventory = true;
				this.rowSelected = -1;
				return;
			}
			// the player pressed a number from 1 to 4
			if ((keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_4) || (keyCode >= KeyEvent.VK_NUMPAD1 && keyCode <= KeyEvent.VK_NUMPAD4))
			{
				// the number the player pressed
				int slotSelected = (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_4) ? keyCode - KeyEvent.VK_1 : keyCode - KeyEvent.VK_NUMPAD1;
				
				// if they haven't a row selected make the corresponding one so
				if (this.rowSelected == -1)
				{
					this.rowSelected = slotSelected;
				}
				else
				{
					// the Item selected
					Item item = this.items[this.rowSelected * 4 + slotSelected].getItem();
					
					if (item == null)
					{
						this.rowSelected = -1;
						return; 
					}
					
					// function that submits the number of items to the move between inventories function
					Consumer<String> submitFunction = (str) ->
					{
						InventoryUtils.tryMoveItemsBetweenInventories(this.items, Main.getInventory(), item, str);
					};
					
					// ask the player how many items do they want to move
					InputBox box = new InputBox(7 / 16f, 1 / 2f, "How many items would you like to move to the inventory?", submitFunction, InputBox.DIGIT_FILTER);
					Main.addBox(box);
					this.rowSelected = -1;
				}
			}
		}
	}
}
