package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.Box;
import com.github.mimo31.w50rld.StringDraw.TextAlign;

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
	
	public CombineBox(float x, float y) {
		super(x, y);
	}

	@Override
	public void draw(Graphics2D g, int width, int height) {
		int locX = (int) (super.x * width);
		int locY = (int) (super.y * height);
		
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int rowHeight = boxWidth / 6;
		
		int boxHeight = rowHeight * 6 + borderSize * 2;
		
		// draw the borders
		g.setColor(Color.magenta);
		g.fillRect(locX, locY, boxWidth, boxHeight);
		
		int contentX = locX + borderSize;
		int contentY = locY + borderSize;
		
		int contentWidth = boxWidth - 2 * borderSize;
		int contentHeight = boxHeight - 2 * borderSize;
		
		// draw the content background
		g.setColor(Color.white);
		g.fillRect(contentX, contentY + rowHeight, contentWidth, contentHeight - rowHeight);
		
		// bounds of the headline
		Rectangle headlineBounds = new Rectangle(contentX, contentY, contentWidth, rowHeight);
		
		// fill the headline background
		g.setColor(Color.orange);
		g.fill(headlineBounds);
		
		// draw the headline
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, borderSize * 2, "Combine:", TextAlign.LEFT, headlineBounds);
		
		// x coordinates for the input slots
		int item0X = contentX + contentWidth / 6;
		int item1X = contentX + contentWidth / 6 + contentWidth / 2;
		
		// width of an Item slot
		int itemWidth = contentWidth / 6;
		
		// draw the required numbers of input Items
		StringDraw.drawMaxString(g, borderSize * 2, this.item0Count == 0 ? "" : String.valueOf(this.item0Count),
				new Rectangle(item0X, contentY + rowHeight, itemWidth, rowHeight));
		StringDraw.drawMaxString(g, borderSize * 2, this.item1Count == 0 ? "" : String.valueOf(this.item1Count),
				new Rectangle(item1X, contentY + rowHeight, itemWidth, rowHeight));
		
		// the the input slots
		Item.drawWithBorder(g, item0X, contentY + 2 * rowHeight, itemWidth, rowHeight, this.slotSelected == 0 ? Color.black : Color.gray, this.item0);
		Item.drawWithBorder(g, item1X, contentY + 2 * rowHeight, itemWidth, rowHeight, this.slotSelected == 1 ? Color.black : Color.gray, this.item1);
		
		// x coordinate of the result slot
		int resultItemX = contentX + contentWidth / 2 - itemWidth / 2;
		
		// draw the number of result Items
		g.setColor(Color.black);
		StringDraw.drawMaxString(g, borderSize * 2, this.resultItemCount == 0 ? "" : String.valueOf(this.resultItemCount),
				new Rectangle(resultItemX, contentY + rowHeight * 4, itemWidth, rowHeight));
		
		// draws the result Item slot
		Item.drawWithBorder(g, resultItemX, contentY + rowHeight * 5, itemWidth, rowHeight, this.resultItem == null ? Color.gray : Color.black, this.resultItem);
	}

	@Override
	protected Dimension getSize(int width, int height) {
		int boxWidth = width / 6;
		
		int borderSize = boxWidth / 64;
		
		int rowHeight = boxWidth / 6;
		
		int boxHeight = rowHeight * 6 + borderSize * 2;
		
		return new Dimension(boxWidth, boxHeight);
	}

	@Override
	public void key(KeyEvent event, Runnable closeAction)
	{
		int code = event.getKeyCode();
		
		// check if the player has a slot selected and selects an inventory slot
		if (this.slotSelected != -1 && ((code >= KeyEvent.VK_1 && code <= KeyEvent.VK_8) || (code >= KeyEvent.VK_NUMPAD1 && code <= KeyEvent.VK_NUMPAD8)))
		{
			// get the number of the inventory slot the player selected
			int inventorySlotSelected;
			if (code >= KeyEvent.VK_1 && code <= KeyEvent.VK_9)
			{
				inventorySlotSelected = code - KeyEvent.VK_1;
			}
			else
			{
				inventorySlotSelected = code - KeyEvent.VK_NUMPAD1;
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
		if (this.slotSelected != -1 && (code == KeyEvent.VK_BACK_SPACE || code == KeyEvent.VK_DELETE))
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
		
		switch (code)
		{
			// the player selects the first input slot
			case KeyEvent.VK_1:
			case KeyEvent.VK_NUMPAD1:
				if (this.slotSelected == -1)
				{
					this.slotSelected = 0;
				}
				break;
			// the player selects the second input slot
			case KeyEvent.VK_2:
			case KeyEvent.VK_NUMPAD2:
				if (this.slotSelected == -1)
				{
					this.slotSelected = 1;
				}
				break;
			// the player confirms the Recipe
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_C:
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
					InputBox box = new InputBox(super.x, super.y, "How many times do you want to use this combination?", submitFunction, InputBox.DIGIT_FILTER);
					
					// add the Input Box
					Main.addBox(box);
				}
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
