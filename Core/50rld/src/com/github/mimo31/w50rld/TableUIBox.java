package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.StringDraw.TextAlign;

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
	
	public TableUIBox(float x, float y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics2D g, int width, int height) {
		int locX = (int) (super.x * width);
		int locY = (int) (super.y * height);
		
		int borderSize = width / 6 / 64;
		
		int gridSize = width / 36;
		
		int contentWidth = gridSize * 7;
		
		int contentHeight = gridSize * 6;
		
		// fill the Box's borders
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, contentWidth + 2 * borderSize, contentHeight + 2 * borderSize);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		
		// fill the headline background
		g.setColor(Color.orange);
		Rectangle headlineBounds = new Rectangle(contentX, contentY, contentWidth, gridSize);
		g.fill(headlineBounds);
		
		// draw the headline
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, 2 * borderSize, "Table", TextAlign.LEFT, headlineBounds);
		
		// fill the Box's background
		g.setColor(Color.white);
		g.fillRect(contentX, contentY + gridSize, contentWidth, contentHeight - gridSize);
		
		// draw the required items slots
		Item.drawWithBorder(g, contentX + 2 * gridSize, contentY + gridSize + gridSize / 2, gridSize, gridSize, this.selectedItem == 0 ? Color.black : Color.gray, this.requiredItems[0]);
		Item.drawWithBorder(g, contentX + gridSize / 2, contentY + 3 * gridSize, gridSize, gridSize, this.selectedItem == 1 ? Color.black : Color.gray, this.requiredItems[1]);
		Item.drawWithBorder(g, contentX + 2 * gridSize, contentY + 3 * gridSize, gridSize, gridSize, this.selectedItem == 2 ? Color.black : Color.gray, this.requiredItems[2]);
		Item.drawWithBorder(g, contentX + 3 * gridSize + gridSize / 2, contentY + 3 * gridSize, gridSize, gridSize, this.selectedItem == 3 ? Color.black : Color.gray, this.requiredItems[3]);
		Item.drawWithBorder(g, contentX + 2 * gridSize, contentY + 4 * gridSize + gridSize / 2, gridSize, gridSize, this.selectedItem == 4 ? Color.black : Color.gray, this.requiredItems[4]);
	
		// draw the triangle arrow between the required items and the result item
		int arrowX = contentX + 4 * gridSize + gridSize / 4 + gridSize / 2;
		int halfArrowHeight = (int) (gridSize / 2d / Math.sqrt(3));
		int arrowMiddleY = contentY + 3 * gridSize + gridSize / 2;
		
		Polygon arrow = new Polygon(new int[] { arrowX, arrowX + gridSize / 2, arrowX }, new int[] { arrowMiddleY + halfArrowHeight, arrowMiddleY, arrowMiddleY - halfArrowHeight }, 3);
		
		g.setColor(Color.black);
		g.fill(arrow);
		
		// draw the result items count
		if (this.resultCount > 1)
		{
			StringDraw.drawMaxString(g, borderSize * 2, String.valueOf(this.resultCount), new Rectangle(contentX + 5 * gridSize, contentY + 2 * gridSize, gridSize, gridSize));
		}
		
		// draw the result item slot
		Item.drawWithBorder(g, contentX + 5 * gridSize + gridSize / 2, contentY + 3 * gridSize, gridSize, gridSize, this.resultItem != null ? Color.black : Color.gray, this.resultItem);
	}

	@Override
	protected Dimension getSize(int width, int height) {
		int borderSize = width / 6 / 64;
		
		int gridSize = width / 36;
		
		return new Dimension(borderSize * 2 + gridSize * 7, borderSize * 2 + gridSize * 6);
	}

	@Override
	public void key(KeyEvent event, Runnable closeAction)
	{
		int keyCode = event.getKeyCode();
		if (this.selectedItem != -1)
		{
			// if an slot is selected and the player pressed a key with number from 1 to 8, set the slot's item to the item in the inventory at the specified number,
			// update the recipe and unselect the slot
			if ((keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8) || (keyCode >= KeyEvent.VK_NUMPAD1 && keyCode <= KeyEvent.VK_NUMPAD8))
			{
				int chosenSlot = (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_8) ? keyCode - KeyEvent.VK_1 : keyCode - KeyEvent.VK_NUMPAD1;
				this.requiredItems[this.selectedItem] = Main.getInventorySlot(chosenSlot).getItem();
				this.updateRecipe();
				this.selectedItem = -1;
				return;
			}
			
			// if a slot is selected and the player presses back space or delete, remove the slot's item, update the recipe and unselect the slot
			if (keyCode == KeyEvent.VK_DELETE || keyCode == KeyEvent.VK_BACK_SPACE)
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
			if ((keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_5) || (keyCode >= KeyEvent.VK_NUMPAD1 && keyCode <= KeyEvent.VK_NUMPAD5))
			{
				int chosenSlot = (keyCode >= KeyEvent.VK_1 && keyCode <= KeyEvent.VK_5) ? keyCode - KeyEvent.VK_1 : keyCode - KeyEvent.VK_NUMPAD1;
				this.selectedItem = chosenSlot;
				return;
			}
		}
		
		// if this method still hasn't returned and the player pressed the C key or the Enter key and the recipe is not null,
		// confirm the recipe selection by asking them how many times do they want to apply the recipe and trying to apply when the respond
		if ((keyCode == KeyEvent.VK_C || keyCode == KeyEvent.VK_ENTER) && this.currentRecipe != null)
		{
			Consumer<String> submitFunction = (str) -> 
			{
				Main.tryApplyRecipe(this.currentRecipe.toRecipe(), str);
				
				closeAction.run();
			};
			
			InputBox box = new InputBox(super.x, super.y, "How many times do you want to use this combination?", submitFunction, InputBox.DIGIT_FILTER);
			
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
