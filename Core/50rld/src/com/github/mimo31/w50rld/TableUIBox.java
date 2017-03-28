package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.TextDraw.TextAlign;

/**
 * Represents a Box with the UI of a Table.
 * @author mimo31
 *
 */
public class TableUIBox extends Box {

	// required items
	private Item[] requiredItems = new Item[5];
	
	// result item
	private Item resultItem = null;
	
	// result items count
	private int resultCount = 0;
	
	// required item selected slot, -1 if not slot is selected
	private int selectedItem = -1;
	
	// Recipe based on the required items
	private TableRecipe currentRecipe = null;
	
	public TableUIBox(float x, float y, CornerAlign align) {
		super(x, y);
		super.align(align);
	}

	@Override
	public void draw()
	{
		// box with a 7 x 6 grid
		
		float contentWidth = 7 * Constants.BOX_TILE_SIZE;
		
		float boxWidth = 2 * Constants.BOX_BORDER_SIZE + contentWidth;
		
		float tileHeight = Constants.BOX_TILE_SIZE * Gui.width / Gui.height;
		
		float contentHeight = 6 * tileHeight;
		
		float borderHeight = Constants.BOX_BORDER_SIZE * Gui.width / Gui.height;
		
		float boxHeight = 2 * borderHeight + contentHeight;
		
		// fill the Box's borders
		PaintUtils.setDrawColor(Color.magenta);
		PaintUtils.drawRectangle(this.x, this.y, boxWidth, boxHeight);
		
		float contentX = this.x + Constants.BOX_BORDER_SIZE;
		float contentY = this.y + borderHeight;
		
		// fill the headline background
		PaintUtils.setDrawColor(Color.orange);
		PaintUtils.drawRectangle(contentX, contentY + 5 * tileHeight, contentWidth, tileHeight);
		
		// draw the headline
		TextDraw.drawText("Table", contentX, contentY + 5 * tileHeight, contentWidth, tileHeight, TextAlign.LEFT, Constants.BOX_BORDER_SIZE);
		
		// fill the Box's background
		glColor3f(1, 1, 1);
		PaintUtils.drawRectangle(contentX, contentY, contentWidth, 5 * tileHeight);
		
		// draw the required items slots
		Item.drawWithBorderS(contentX + 2 * Constants.BOX_TILE_SIZE, contentY + 3.5f * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, this.selectedItem == 0 ? Color.black : Color.gray, this.requiredItems[0]);
		Item.drawWithBorderS(contentX + 0.5f * Constants.BOX_TILE_SIZE, contentY + 2f * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, this.selectedItem == 1 ? Color.black : Color.gray, this.requiredItems[1]);
		Item.drawWithBorderS(contentX + 2 * Constants.BOX_TILE_SIZE, contentY + 2f * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, this.selectedItem == 2 ? Color.black : Color.gray, this.requiredItems[2]);
		Item.drawWithBorderS(contentX + 3.5f * Constants.BOX_TILE_SIZE, contentY + 2f * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, this.selectedItem == 3 ? Color.black : Color.gray, this.requiredItems[3]);
		Item.drawWithBorderS(contentX + 2 * Constants.BOX_TILE_SIZE, contentY + 0.5f * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, this.selectedItem == 4 ? Color.black : Color.gray, this.requiredItems[4]);
	
		// draw the triangle arrow between the required items and the result item
		float arrowX = contentX + 4.75f * Constants.BOX_TILE_SIZE;
		float halfArrowHeight = tileHeight / 3f / (float)Math.sqrt(3);
		float arrowMiddleY = contentY + 2.5f * tileHeight;
		
		glColor3f(0, 0, 0);
		glBegin(GL_TRIANGLES);
		glVertex2f(arrowX, arrowMiddleY - halfArrowHeight);
		glVertex2f(arrowX + 0.5f * Constants.BOX_TILE_SIZE, arrowMiddleY);
		glVertex2f(arrowX, arrowMiddleY + halfArrowHeight);
		glEnd();
		
		// draw the result items count
		if (this.resultCount > 1)
		{
			TextDraw.drawText(String.valueOf(this.resultCount), contentX + 5 * Constants.BOX_TILE_SIZE, contentY + 3 * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, TextAlign.MIDDLE, Constants.BOX_BORDER_SIZE);
		}
		
		// draw the result item slot
		Item.drawWithBorderS(contentX + 5.5f * Constants.BOX_TILE_SIZE, contentY + 2f * tileHeight, Constants.BOX_TILE_SIZE, tileHeight, this.resultItem != null ? Color.black : Color.gray, this.resultItem);
	}

	@Override
	protected float getWidth()
	{
		return 2 * Constants.BOX_BORDER_SIZE + 7 * Constants.BOX_TILE_SIZE;
	}
	
	@Override
	protected float getHeight()
	{
		return (2 * Constants.BOX_BORDER_SIZE + 6 * Constants.BOX_TILE_SIZE) * Gui.width / Gui.height;
	}

	@Override
	public void keyReleased(int keyCode, Runnable closeAction)
	{
		if (this.selectedItem != -1)
		{
			// if an slot is selected and the player pressed a key with number from 1 to 8, set the slot's item to the item in the inventory at the specified number,
			// update the recipe and unselect the slot
			if ((keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8) || (keyCode >= GLFW_KEY_KP_1 && keyCode <= GLFW_KEY_KP_8))
			{
				int chosenSlot = (keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_8) ? keyCode - GLFW_KEY_1 : keyCode - GLFW_KEY_KP_1;
				this.requiredItems[this.selectedItem] = Main.getInventorySlot(chosenSlot).getItem();
				this.updateRecipe();
				this.selectedItem = -1;
				return;
			}
			
			// if a slot is selected and the player presses back space or delete, remove the slot's item, update the recipe and unselect the slot
			if (keyCode == GLFW_KEY_DELETE || keyCode == GLFW_KEY_BACKSPACE)
			{
				this.requiredItems[this.selectedItem] = null;
				this.updateRecipe();
				this.selectedItem = -1;
				return;
			}
		}
		else
		{
			// if no slot is selected and the player pressed a key with a number from 1 to 5, select the corresponding slot
			if ((keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_5) || (keyCode >= GLFW_KEY_KP_1 && keyCode <= GLFW_KEY_KP_5))
			{
				int chosenSlot = (keyCode >= GLFW_KEY_1 && keyCode <= GLFW_KEY_5) ? keyCode - GLFW_KEY_1 : keyCode - GLFW_KEY_KP_1;
				this.selectedItem = chosenSlot;
				return;
			}
		}
		
		// if this method still hasn't returned and the player pressed the C key or the Enter key and the recipe is not null,
		// confirm the recipe selection by asking them how many times do they want to apply the recipe and trying to apply when the respond
		if ((keyCode == GLFW_KEY_C || keyCode == GLFW_KEY_ENTER || keyCode == GLFW_KEY_KP_ENTER) && this.currentRecipe != null)
		{
			Consumer<String> submitFunction = (str) -> 
			{
				Main.tryApplyRecipe(this.currentRecipe.toRecipe(), str);
				
				closeAction.run();
			};
			
			InputBox box = new InputBox(super.x, super.y, "How many times?", submitFunction, InputBox.DIGIT_INTERPRETER, CornerAlign.BOTTOMLEFT);
			
			Main.addBox(box);
		}
	}
	
	/**
	 * Updates the result item, result count and current recipe based on the required items.
	 */
	private void updateRecipe()
	{
		// query the index and set the field accordingly to the returned recipe
		TableRecipe foundRecipe = ObjectsIndex.getTableRecipe(this.requiredItems);
		this.currentRecipe = foundRecipe;
		if (foundRecipe == null)
		{
			this.resultCount = 0;
			this.resultItem = null;
		}
		else
		{
			this.resultCount = foundRecipe.resultCount;
			this.resultItem = foundRecipe.resultItem;
		}
	}
}
