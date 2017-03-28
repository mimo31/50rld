package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Point;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

/**
 * Represents a Box where the user can combine two items in their inventory to create another item.
 * @author mimo31
 *
 */
public class CombineBox extends Box {

	// selected input Items to the recipe, null if not selected
	private Item item0;
	private Item item1;
	
	// required number of Items for the recipe, 0 if no recipe found
	private int item0Count = 0;
	private int item1Count = 0;
	
	// Item produces by the recipe
	private Item resultItem;
	private int resultItemCount = 0;
	
	// selected input slot to select an Item from the inventory for it
	private int slotSelected = -1;
	
	// Recipe corresponding to the input Items
	private Recipe currentRecipe = null;
	
	public CombineBox(float x, float y, CornerAlign align) {
		super(x, y);
		super.align(align);
	}

	@Override
	public void draw() {
		// box with a 6 x 6 tile grid
		
		float contentWidth = 6 * Constants.BOX_TILE_SIZE;
		
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = 6 * tileHeight;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + contentHeight;
		
		// draw the borders
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		// draw the content background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, contentHeight - tileHeight);
		
		// fill the headline background
		PaintUtils.setDrawColor(Color.orange);
		PaintUtils.drawRectangle(contentX, contentY + 5 * tileHeight, contentWidth, tileHeight);
		
		// draw the headline
		TextDraw.drawText("Combine:", contentX, contentY + 5 * tileHeight, contentWidth, tileHeight, TextAlign.LEFT, Constants.BOX_BORDER_SIZE);
		
		// x coordinates for the input slots
		float item0X = contentX + Constants.BOX_TILE_SIZE;
		float item1X = contentX + 4 * Constants.BOX_TILE_SIZE;
		
		// draw the required numbers of input Items
		TextDraw.drawText(this.item0Count == 0 ? "" : String.valueOf(this.item0Count), item0X, contentY + 4 * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, TextAlign.MIDDLE, Constants.BOX_BORDER_SIZE);
		TextDraw.drawText(this.item1Count == 0 ? "" : String.valueOf(this.item1Count), item1X, contentY + 4 * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, TextAlign.MIDDLE, Constants.BOX_BORDER_SIZE);
		
		// the the input slots
		Item.drawWithBorder(item0X, contentY + 3 * tileHeight, item0X + Constants.BOX_TILE_SIZE, contentY + 4 * tileHeight, this.slotSelected == 0 ? Color.black : Color.gray, this.item0);
		Item.drawWithBorder(item1X, contentY + 3 * tileHeight, item1X + Constants.BOX_TILE_SIZE, contentY + 4 * tileHeight, this.slotSelected == 1 ? Color.black : Color.gray, this.item1);
		
		// x coordinate of the result slot
		float resultItemX = contentX + 2.5f * Constants.BOX_TILE_SIZE;
		
		// draw the number of result Items
		TextDraw.drawText(this.resultItemCount == 0 ? "" : String.valueOf(this.resultItemCount), resultItemX, contentY + tileHeight * 2, Constants.BOX_TILE_SIZE, tileHeight, TextAlign.MIDDLE, Constants.BOX_BORDER_SIZE);
		
		// draw the result Item slot
		Item.drawWithBorder(resultItemX, contentY + tileHeight, resultItemX + Constants.BOX_TILE_SIZE, contentY + tileHeight * 2, this.resultItem == null ? Color.gray : Color.black, this.resultItem);
	}

	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_TILE_SIZE + 6 * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return (2 * Constants.BOX_TILE_SIZE + 6 * Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}
	
	@Override
	public boolean mouseClicked(Point location, Runnable closeAction)
	{
		float clickX = location.x * 2f / Gui.width - 1;
		float clickY = 1 - location.y * 2f / Gui.height;
		
		float contentWidth = 6 * Constants.BOX_TILE_SIZE;
		
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = 6 * tileHeight;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + contentHeight;
		
		// check if the box was clicked
		if (this.x <= clickX && this.y <= clickY && clickX < this.x + boxWidth && clickY < this.y + boxHeight)
		{
			// check if the content part was clicked
			if (this.x + Constants.BOX_BORDER_SIZE <= clickX && this.y + borderHeight <= clickY && clickX < this.x + boxWidth - Constants.BOX_BORDER_SIZE && clickY < this.y + boxHeight - borderHeight)
			{
				// click location with respect to the content part
				float inCliX = clickX - this.x - Constants.BOX_BORDER_SIZE;
				float inCliY = clickY - this.y - borderHeight;
				
				// check if any of the slots was clicked
				if (inCliX >= Constants.BOX_TILE_SIZE && inCliX < 2 * Constants.BOX_TILE_SIZE && inCliY >= 3 * tileHeight && inCliY < 4 * tileHeight)
				{
					this.slotSelected = 0;
				}
				else if (inCliX >= 4 * Constants.BOX_TILE_SIZE && inCliX < 5 * Constants.BOX_TILE_SIZE && inCliY >= 3 * tileHeight && inCliY < 4 * tileHeight)
				{
					this.slotSelected = 1;
				}
				else if (inCliX >= 2.5f * Constants.BOX_TILE_SIZE && inCliX < 3.5f * Constants.BOX_TILE_SIZE && inCliY >= tileHeight && inCliY < 2 * tileHeight)
				{
					this.tryConfirmRecipe(closeAction);
				}
			}
			return true;
		}
		return false;
	}
	
	private void tryConfirmRecipe(Runnable closeAction)
	{
		// check if there is such Recipe
		if (this.resultItem != null)
		{
			// submit function for the Input Box to appear
			Consumer<String> submitFunction = (strInput) ->
			{
				// try to apply the Recipe
				Main.tryApplyRecipe(this.currentRecipe, strInput);
				
				// close this Combine Box
				closeAction.run();
			};
			
			// Input Box that ask the player how many times do they want to apply the Recipe
			InputBox box = new InputBox(super.x, super.y, "How many times?", submitFunction, InputBox.DIGIT_INTERPRETER, CornerAlign.BOTTOMLEFT);
			
			// add the Input Box
			Main.addBox(box);
		}
	}
	
	@Override
	public void keyReleased(int keyCode, Runnable closeAction)
	{
		// check if the player has a slot selected and selects an inventory slot
		if (this.slotSelected != -1 && ((keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8) || (keyCode >= GLFW_KEY_KP_1 && keyCode <= GLFW_KEY_KP_8)))
		{
			// get the number of the inventory slot the player selected
			int inventorySlotSelected;
			if (keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8)
			{
				inventorySlotSelected = keyCode - GLFW_KEY_1;
			}
			else
			{
				inventorySlotSelected = keyCode - GLFW_KEY_KP_1;
			}
			
			// the selected inventory slot
			ItemStack slot = Main.getInventorySlot(inventorySlotSelected);
			
			// if the slot is empty, just unselect the input slot
			if (slot.getCount() == 0)
			{
				this.slotSelected = -1;
				return;
			}
			
			// set the selected input slot's Item to the selected inventory slot's Item
			if (this.slotSelected == 0)
			{
				this.item0 = slot.getItem();
			}
			else
			{
				this.item1 = slot.getItem();
			}
			
			// unselect the input slot
			this.slotSelected = -1;
			
			// update the Recipe
			this.updateRecipe();
			
			return;
		}
		
		// check if the player has an input slot selected and presses backspace or delete 
		if (this.slotSelected != -1 && (keyCode == GLFW_KEY_BACKSPACE || keyCode == GLFW_KEY_DELETE))
		{
			// set the selected input slot's Item to nothing - null
			if (this.slotSelected == 0)
			{
				this.item0 = null;
			}
			else
			{
				this.item1 = null;
			}
			
			// unselect the input slot
			this.slotSelected = -1;
			
			// update the Recipe
			this.updateRecipe();
			
			return;
		}
		
		switch (keyCode)
		{
			// the player selects the first input slot
			case GLFW_KEY_1:
			case GLFW_KEY_KP_1:
				if (this.slotSelected == -1)
				{
					this.slotSelected = 0;
				}
				break;
			// the player selects the second input slot
			case GLFW_KEY_2:
			case GLFW_KEY_KP_2:
				if (this.slotSelected == -1)
				{
					this.slotSelected = 1;
				}
				break;
			// the player confirms the Recipe
			case GLFW_KEY_ENTER:
			case GLFW_KEY_KP_ENTER:
			case GLFW_KEY_C:
				this.tryConfirmRecipe(closeAction);
				break;
		}
	}
	
	/**
	 * Looks for a Recipe with the selected Items as required Items. Updates field according to the found Recipe.
	 */
	private void updateRecipe()
	{
		// array of selected Items
		Item[] requiredItems;
		
		// populate the array with 1 or 2 elements
		if (this.item0 == null)
		{
			if (this.item1 == null)
			{
				// both selected Items are null, no such Recipe
				this.item0Count = 0;
				this.item1Count = 0;
				this.resultItemCount = 0;
				this.resultItem = null;
				this.currentRecipe = null;
				return;
			}
			else
			{
				requiredItems = new Item[] { this.item1 };
			}
		}
		else
		{
			if (this.item1 == null)
			{
				requiredItems = new Item[] { this.item0 };
			}
			else
			{
				requiredItems = new Item[] { this.item0, this.item1 };
			}
		}
		
		// found Recipe
		Recipe recipe = ObjectsIndex.getRecipe(requiredItems);
		
		this.currentRecipe = recipe;
		
		// recipe is null - no Recipe found
		if (recipe == null)
		{
			this.item0Count = 0;
			this.item1Count = 0;
			this.resultItemCount = 0;
			this.resultItem = null;
			return;
		}
		
		// set the result Item and the result Item count according to the Recipe
		this.resultItem = recipe.resultItem;
		this.resultItemCount = recipe.resultCount;
		
		// set the required Items count according to the Recipe
		if (this.item1 == null)
		{
			this.item0Count = recipe.requiredCounts[0];
			this.item1Count = 0;
		}
		else if (this.item0 == null)
		{
			this.item0Count = 0;
			this.item1Count = recipe.requiredCounts[0];
		}
		else 
		{
			this.item0Count = (recipe.requiredItems[0] == this.item0) ? recipe.requiredCounts[0] : recipe.requiredCounts[1];
			this.item1Count = (recipe.requiredItems[0] == this.item0) ? recipe.requiredCounts[1] : recipe.requiredCounts[0];
		}
	}
}
