package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;

import java.awt.Color;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

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
	
	
	public ChestUIBox(float x, float y, ItemStack[] items, CornerAlign align) {
		super(x, y);
		this.items = items;
		super.align(align);
	}

	@Override
	public void draw() {
		float contentWidth = (/* 4 Item slots */4 + /* two borders on the sides */2 * (1 / 2f) + /* 3 spaces between the slots */3 * (1 / 4f)) * Constants.BOX_TILE_SIZE;
		
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = (contentWidth / Constants.BOX_TILE_SIZE + 1) * tileHeight;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + contentHeight;
		
		// fill the Box's borders
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		PaintUtils.setDrawColor(Color.orange);
		PaintUtils.drawRectangle(contentX, contentY + contentHeight - tileHeight, contentWidth, tileHeight);
		
		TextDraw.drawText("Chest", contentX, contentY + contentHeight - tileHeight, contentWidth, tileHeight, TextAlign.LEFT, Constants.BOX_BORDER_SIZE);
		
		// draw the background gray if selecting from the inventory, white otherwise
		PaintUtils.setDrawColor(this.selectingFromInventory ? Color.lightGray : Color.white);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, contentHeight - tileHeight);
		
		// draw the Item slots
		for (int i = 0; i < 4; i++)
		{
			Color borderColor = this.rowSelected == i ? Color.red : Color.black;
			// y coordinate of the slots in this row
			float drawY = contentY + tileHeight / 2 + (3 - i) * tileHeight * 1.25f;
			for (int j = 0; j < 4; j++)
			{
				ItemStack currentStack = this.items[j + i * 4];
				float drawX = contentX + Constants.BOX_TILE_SIZE * 0.5f + 1.25f * j * Constants.BOX_TILE_SIZE;
				ItemStack.drawWithBorder(drawX, drawY, drawX + Constants.BOX_TILE_SIZE, drawY + tileHeight, borderColor, currentStack);
			}
		}
	}
	
	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_BORDER_SIZE + (4 + 2 * (1 / 2f) + 3 * (1 / 4f)) * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return (2 * Constants.BOX_BORDER_SIZE + (4 + 2 * (1 / 2f) + 3 * (1 / 4f) + 1) * Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}

	@Override
	public void keyReleased(int keyCode, Runnable closeAction)
	{
		if (this.selectingFromInventory)
		{
			// if the player is selecting from the inventory and pressed a valid number from 1 to 8
			if ((keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8) || (keyCode >= GLFW_KEY_KP_1 && keyCode <= GLFW_KEY_KP_8))
			{
				// the number of the inventory slot the player has selected
				int slotSelected = (keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8) ? keyCode - GLFW_KEY_1 : keyCode - GLFW_KEY_KP_1;
				
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
				InputBox box = new InputBox(-1 / 8f, 0, "How many items to move?", submitFunction, InputBox.DIGIT_INTERPRETER, CornerAlign.TOPLEFT);
			
				Main.addBox(box);
			
				this.selectingFromInventory = false;
			}
			
			// if the player is selecting from the inventory and pressed the back space, then stop selecting from the inventory
			else if (keyCode == GLFW_KEY_BACKSPACE)
			{
				this.selectingFromInventory = false;
			}
		}
		else
		{
			// if the player pressed the I key, stop start selecting from the inventory
			if (keyCode == GLFW_KEY_I)
			{
				this.selectingFromInventory = true;
				this.rowSelected = -1;
				return;
			}
			// the player pressed a number from 1 to 4
			if ((keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_4) || (keyCode >= GLFW_KEY_KP_1 && keyCode <= GLFW_KEY_KP_4))
			{
				// the number the player pressed
				int slotSelected = (keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_4) ? keyCode - GLFW_KEY_1 : keyCode - GLFW_KEY_KP_1;
				
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
					InputBox box = new InputBox(-1 / 8f, 0, "How many items to move?", submitFunction, InputBox.DIGIT_INTERPRETER, CornerAlign.TOPLEFT);
					Main.addBox(box);
					this.rowSelected = -1;
				}
			}
		}
	}
}
