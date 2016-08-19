package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.github.mimo31.w50rld.Item.ItemAction;
import com.github.mimo31.w50rld.Structure.StructureAction;

/**
 * Controls the main flow of the program.
 * @author mimo31
 *
 */
public class Main {

	// the root directory for saved data and resources
	public static final String rootDirectory = "50rld";
	
	// current player's location
	public static int playerX;
	public static int playerY;
	
	// game seed
	public static final long SEED = (long) (Math.random() * Long.MAX_VALUE);
	
	// the zoom of the map (size of one Tile is width * 2 ^ (9 - zoom))
	public static int zoom = 2;
	
	// the map
	public static Map map = new Map();
	
	// the arrow keycode that is currently pressed and we are therefore moving that direction (0 in no key is pressed)
	private static int arrowDown = 0;
	
	// time when the arrow key was pressed
	private static long downAt = 0;
	// time when the last move was done
	private static long lastMove = 0;
	
	// current number of health points
	private static int health = 16;
	
	// player's inventory
	private static ItemStack[] inventory = new ItemStack[8];
	
	// opened OptionBoxes
	private static List<Box> boxes = new ArrayList<Box>();
	
	
	public static void main(String[] args)
	{
		// initialize everything
		try
		{
			initialize();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Paints the current state of the game through the g parameter. Should not perform extensive non drawing actions.
	 * @param g graphics to use
	 * @param width width of the rectangle to paint
	 * @param height height of the rectangle to paint
	 */
	public static void paint(Graphics2D g, int width, int height)
	{
		// the size of a Tile on the screen
		float tileSize = (float) (width * Math.pow(2, zoom - 9));
		
		// coordinates of the rectangle of the Map that can be seen on the screen
		float mapViewWidth = width * 7 / 8f / tileSize;
		float mapViewHeight = height / tileSize;
		float mapViewCornerX = playerX - (mapViewWidth - 1) / 2;
		float mapViewCornerY = playerY - (mapViewHeight - 1) / 2;
		
		// convert the coordinates to ints
		int mapX = (int)Math.floor(mapViewCornerX);
		int mapY = (int)Math.floor(mapViewCornerY);
		int mapWidth = (int)Math.ceil(mapViewWidth);
		int mapHeight = (int)Math.ceil(mapViewHeight);

		// get the Tiles in the rectangle
		Tile[] tiles = map.getTiles(mapX, mapY, mapWidth + 1, mapHeight + 1);
		
		// iterate through all the tiles of the rectangle and paint them
		for (int i = mapY; i <= mapY + mapHeight; i++)
		{
			for (int j = mapX; j <= mapX + mapWidth; j++)
			{
				Tile currentTile = tiles[(i - mapY) * (mapWidth + 1) + (j - mapX)];
				
				// calculate the location where the Tile will be painted
				int paintX = (int)((j - mapViewCornerX) * tileSize);
				int paintY = (int)((i - mapViewCornerY) * tileSize);
				int nextPaintX = (int)((j + 1 - mapViewCornerX) * tileSize);
				int nextPaintY = (int)((i + 1 - mapViewCornerY) * tileSize);
				
				currentTile.paint(g, paintX, paintY, nextPaintX - paintX, nextPaintY - paintY);

				// draw the player
				if (j == playerX && i == playerY)
				{
					g.setColor(Color.black);
					int playerSquareX = (paintX + nextPaintX) / 2;
					int playerSquareY = (paintY + nextPaintY) / 2;
					g.fillRect(playerSquareX, playerSquareY, nextPaintX - playerSquareX, nextPaintY - playerSquareY);
				}
			}
		}
		
		// draw the health bar
		
		// x coordinate of the left end of the health bar
		int healthXStart = width * 7 / 8;
		
		// x coordinate of the right end of the health bar
		int healthXEnd = width * 15 / 16;
		
		// draw the health background
		g.setColor(new Color(153, 0, 0));
		g.fillRect(healthXStart, 0, healthXEnd - healthXStart, height);
		
		// y coordinate of the bottom end of the heart
		int heartYEnd = healthXEnd - healthXStart;
		
		// draw the heart
		g.drawImage(ResourceHandler.getImage("Heart.png", heartYEnd), healthXStart, 0, null);
		
		// draw the amount of health points
		g.setColor(Color.red);
		int healthBarHeight = (height - heartYEnd) * health / Constants.MAX_HEALTH;
		g.fillRect(healthXStart, height - healthBarHeight, heartYEnd, healthBarHeight);
		
		// draw the bar to break the amount of health points
		g.setColor(Color.black);
		for (int i = 1; i < Constants.MAX_HEALTH; i++)
		{
			g.fillRect(healthXStart, heartYEnd + (height - heartYEnd) * i / Constants.MAX_HEALTH - height / 512, heartYEnd, height / 256);
		}
		
		// draw the inventory
		for (int i = 0; i < 8; i++)
		{
			int paintY = height * i / 8;
			int nextPaintY = height * (i + 1) / 8;
			inventory[i].draw(g, healthXEnd, paintY, width - healthXEnd, nextPaintY - paintY);
		}
		
		// draw the boxes
		for (int i = 0, n = boxes.size(); i < n; i++)
		{
			boxes.get(i).draw(g, width, height);
		}
	}
	
	/**
	 * Handles key releases for the game.
	 * @param e KeyEvent
	 */
	public static void keyReleased(KeyEvent e)
	{
		Dimension windowSize = Gui.getContentSize();
		int width = windowSize.width;
		int height = windowSize.height;
		
		int code = e.getKeyCode();
		int numberOfBoxes = boxes.size();
		
		// if at least one Box is opened, trigger its key function or remove it if the key was Escape
		if (numberOfBoxes != 0)
		{
			if (code == KeyEvent.VK_ESCAPE)
			{
				// remove the Box
				boxes.remove(numberOfBoxes - 1);
			}
			else
			{
				// pass the event to the Box
				Box topBox = boxes.get(numberOfBoxes - 1);
				boxes.get(numberOfBoxes - 1).key(e, () -> boxes.remove(topBox));
			}
			return;
		}
		switch (code)
		{
			// handle zooming in and out
			case KeyEvent.VK_ADD:
			case KeyEvent.VK_PLUS:
				if (zoom != Constants.MAX_ZOOM)
				{
					zoom++;
				}
				break;
			case KeyEvent.VK_SUBTRACT:
			case KeyEvent.VK_MINUS:
				if (zoom != 0)
				{
					zoom--;
				}
				break;
			
			// handle arrow releases - stop moving
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
				arrowDown = (code == arrowDown) ? 0 : arrowDown;
				break;
			
			// display the action window
			case KeyEvent.VK_A:
				Tile currentTile = map.getTile(playerX, playerY);
				if (currentTile.hasItems())
				{
					// there are Items laying on this Tile - show an OptionBox to let the player grab some of them
					
					List<ItemStack> items = currentTile.getItems();
					
					// declare arrays for options and actions for the OptionBox
					String[] options = new String[items.size()];
					Runnable[] actions = new Runnable[options.length];
					
					// populate the arrays
					for (int i = 0; i < options.length; i++)
					{
						ItemStack currentStack = items.get(i);
						
						// include the name of the Item and the number of items in the option text
						options[i] = currentStack.getItem().name + " - " + String.valueOf(currentStack.getCount());
						
						actions[i] = () -> {
							// try adding the item in the inventory, if no items were added, show an InfoBox
							if (!Main.tryAddInventoryItems(currentStack))
							{
								InfoBox box = new InfoBox(7 / 16f, 1 / 2f, "No space in the inventory!");
								
								// fit the box in the window
								box.tryFitWindow(width, height);
								
								boxes.add(box);
							}
							// if all items were added to the inventory, remove the ItemStack from the Tile
							else if (currentStack.getCount() == 0)
							{
								currentTile.removeStack(currentStack);
							}
						};
					}
					
					// add the OptionBox
					
					OptionBox box = new OptionBox(options, actions, 7 / 16f, 1 / 2f, "Grab items");
					
					// fit the box in the window
					box.tryFitWindow(width, height);
					
					boxes.add(box);
				}
				else
				{
					// get the top structure of the Tile on which the player is currently standing on
					Structure topStructure = currentTile.getTopStructure().structure;
					
					// crate arrays for options and actions of the OptionBox
					String[] options = new String[topStructure.actions.length];
					Runnable[] actions = new Runnable[options.length];
					
					// populate those arrays
					for (int i = 0; i < actions.length; i++)
					{
						StructureAction currentAction = topStructure.actions[i];
						options[i] = currentAction.name;
						actions[i] = () -> currentAction.action(playerX, playerY);
					}
					
					// create an OptionBox in the middle of the map
					boxes.add(new OptionBox(options, actions, 7 / 16f, 1 / 2f, topStructure.name));
				}
				break;
			// show a Combine Box
			case KeyEvent.VK_C:
				CombineBox box = new CombineBox(7 / 16f, 1 / 2f);
				box.tryFitWindow(width, height);
				boxes.add(box);
				break;
		}
		
		// check if the player wants to see the actions of an inventory slot
		if ((code >= KeyEvent.VK_1 && code <= KeyEvent.VK_8) || (code >= KeyEvent.VK_NUMPAD1 && code <= KeyEvent.VK_NUMPAD8))
		{
			// inventory slot the player chose
			int slot;
			if (code >= KeyEvent.VK_1 && code <= KeyEvent.VK_8)
			{
				slot = code - KeyEvent.VK_1;
			}
			else
			{
				slot = code - KeyEvent.VK_NUMPAD1;
			}
			
			// show the actions
			showItemActions(slot, width, height);
		}
	}
	
	/**
	 * Shows an OptionBox specific to actions of an Item in a specified inventory slot.
	 * @param slot number of the slot
	 * @param width width of the window
	 * @param height height of the window
	 */
	private static void showItemActions(int slot, int width, int height)
	{
		// return if no is there
		if (inventory[slot].getCount() == 0)
		{
			return;
		}
		
		Item item = inventory[slot].getItem();
		
		List<ItemAction> validActions = new ArrayList<ItemAction>();
		
		for (int i = 0; i < item.actions.length; i++)
		{
			if (item.actions[i].actionPredicate(playerX, playerY))
			{
				validActions.add(item.actions[i]);
			}
		}
		
		// options for the OptionBox
		String[] options = new String[1 + validActions.size()];
		
		// actions for the OptionBox
		Runnable[] actions = new Runnable[options.length];
		
		options[0] = "Drop";
		
		actions[0] = () -> {
			// show an InputBox to ask the user for the amount of Items to drop
			String request = "Enter the number of items to drop: ";
			
			// submit by calling the drop function
			Consumer<String> submitFunction = (inputString) -> { drop(slot, inputString, width, height); };
			
			// allow only digits
			Function<Integer, Boolean> charFilter = InputBox.DIGIT_FILTER;
			
			InputBox box = new InputBox(31 / 32f, 1 / 16f + slot / 8f /* coordinates of the center of the slot */,
					request, submitFunction, charFilter);
			box.tryFitWindow(width, height);
			boxes.add(box);
		};
		
		for (int i = 0, n = validActions.size(); i < n; i++)
		{
			ItemAction currentAction = validActions.get(i);
			options[i + 1] = currentAction.name;
			actions[i + 1] = () -> {
				if (currentAction.action(playerX, playerY))
				{
					inventory[slot].setCount(inventory[slot].getCount() - 1);
				}
			};
		}
		
		// headline for the OptionBox
		String itemName = item.name;
		
		// show the OptionBox
		OptionBox box = new OptionBox(options, actions, 31 / 32f, 1 / 16f + slot / 8f /* coordinates of the center of the slot */,
				itemName); 
		box.tryFitWindow(width, height);
		boxes.add(box);
	}
	
	/**
	 * Handles key types for the game.
	 * @param e KeyEvent
	 */
	public static void keyPressed(KeyEvent e)
	{
		int code = e.getKeyCode();
		if (arrowDown == 0 && (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN || code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT))
		{
			arrowDown = code;
			downAt = System.currentTimeMillis();
			lastMove = 0;
			movePlayer();
		}
	}
	
	/**
	 * Tries to add Items into the inventory.
	 * Decreases the count in the passed stack according to the amount of items add into the inventory.
	 * @param items items to add.
	 * @return true if successfully add at least some items into the inventory, else false
	 */
	public static boolean tryAddInventoryItems(ItemStack items)
	{
		int count = items.getCount();
		
		// the number of items at the start to compare whether some were added at the end
		int startCount = count;
		
		// no items, return
		if (count == 0)
		{
			return false;
		}
		
		Item item = items.getItem();
		
		// check each inventory slot whether it contains the same item as we are trying to add
		for (int i = 0; i < Main.inventory.length; i++)
		{
			ItemStack currentStack = Main.inventory[i];
			
			// check if the current inventory stack / slot contains something
			int stackCount = currentStack.getCount();
			if (stackCount == 0)
			{
				continue;
			}
			
			// add as much items to this slot as possible
			if (currentStack.getItem() == item)
			{
				int freeSpaces = 32 - stackCount;
				if (freeSpaces < count)
				{
					count -= freeSpaces;
					currentStack.setCount(32);
				}
				else
				{
					currentStack.setCount(stackCount + count);
					items.setCount(0);
					return true;
				}
			}
		}
		
		// check each inventory slot whether it is empty, if yes add add as much items to it as possible
		for (int i = 0; i < Main.inventory.length; i++)
		{
			ItemStack currentStack = Main.inventory[i];
			if (currentStack.getCount() == 0)
			{
				currentStack.setItem(item);
				if (count > 32)
				{
					currentStack.setCount(32);
					count -= 32;
				}
				else
				{
					currentStack.setCount(count);
					items.setCount(0);
					return true;
				}
			}
		}
		
		// set the new count to the stack
		items.setCount(count);
		
		// return true if the number of items has changed
		return count != startCount;
	}
	
	/**
	 * Changes the player's coordinates according to the arrow key that is pressed.
	 */
	private static void movePlayer()
	{
		// don't allow and stop any movement when an OptionBox is opened
		if (!boxes.isEmpty())
		{
			arrowDown = 0;
			return;
		}
		
		// move the player according to the key that is pressed
		switch (arrowDown)
		{
			case KeyEvent.VK_UP:
				playerY--;
				break;
			case KeyEvent.VK_DOWN:
				playerY++;
				break;
			case KeyEvent.VK_LEFT:
				playerX--;
				break;
			case KeyEvent.VK_RIGHT:
				playerX++;
				break;
		}
	}
	
	/**
	 * Handles mouse clicks for the game.
	 * @param event MouseEvent
	 */
	public static void mouseClicked(MouseEvent event)
	{
		Dimension windowSize = Gui.getContentSize();
		int width = windowSize.width;
		int height = windowSize.height;
		
		// the number of Boxes
		int numberOfBoxes = boxes.size();
		
		// if there are some boxes, check whether the top one was clicked
		if (numberOfBoxes != 0)
		{
			// Runnable that removes the current top Box
			Box topBox = boxes.get(numberOfBoxes - 1);
			Runnable closeAction = () -> boxes.remove(topBox);
			
			// if the top box was not clicked, remove it
			if (!topBox.mouseClicked(event, closeAction, width, height))
			{
				closeAction.run();
			}
		}
		else
		{
			// if the player click on the inventory
			if (event.getX() >= width * 15 / 16)
			{
				// number of the slot clicked
				int slotClicked = event.getY() / (height / 8);

				// show actions
				showItemActions(slotClicked, width, height);
			}
		}
	}
	
	/**
	 * Drops a specified number of Items from a specified inventory slot on the current Tile.
	 * Used primarily to accept player's drop submissions.
	 * @param slotNumber number of the slot to drop from
	 * @param numberOfItems text representation of the number of Items to drop
	 * @param width width of the window
	 * @param height height of the window
	 */
	private static void drop(int slotNumber, String numberOfItems, int width, int height)
	{
		// error to display to the user, remains null if no error occurs
		String errorMessage = null;
		
		int inputLength = numberOfItems.length();
		
		// remove eventual zeroes
		int i = 0;
		while ((i + 1) < inputLength && numberOfItems.charAt(i) == '0')
		{
			i++;
		}
		if (i != 0)
		{
			numberOfItems = numberOfItems.substring(i, inputLength - 1);
		}
		
		// subtract the number of zeroes removed
		inputLength -= i;
		
		// the player entered nothing
		if (inputLength == 0)
		{
			errorMessage = "You must enter a number.";
		}
		// the player entered more than two characters
		else if (inputLength != 1 && inputLength != 2)
		{
			errorMessage = "There isn't that many items.";
		}
		else
		{
			// the number of item the players wants to drop
			int itemsToDrop = Integer.parseInt(numberOfItems);
			
			// the stack on which we operate
			ItemStack stack = inventory[slotNumber];
			
			// number of Items available to drop
			int itemsPresent = stack.getCount();
			
			// not enough Items in the stack
			if (itemsToDrop > itemsPresent)
			{
				errorMessage = "There isn't that many items.";
			}
			else
			{
				Tile currentTile = map.getTile(playerX, playerY);
				
				// stack to be sent the Tile to add
				ItemStack stackToAdd = new ItemStack();
				stackToAdd.setItem(stack.getItem());
				stackToAdd.setCount(itemsToDrop);
				
				// add the Items to the Tile
				currentTile.addItems(stackToAdd);
				
				// remove the items from the inventory stack
				stack.setCount(itemsPresent - itemsToDrop);
				return;
			}
		}
		
		// show an InfoBox with the error message
		InfoBox box = new InfoBox(31 / 32f, 1 / 16f + slotNumber / 8f, errorMessage);
		box.tryFitWindow(width, height);
		boxes.add(box);
	}
	
	/**
	 * Adds a Box to the top.
	 * @param box Box to add.
	 */
	public static void addBox(Box box)
	{
		boxes.add(box);
	}
	
	/**
	 * Returns a slot from the inventory.
	 * @param number number of the inventory slot
	 * @return an inventory slot
	 */
	public static ItemStack getInventorySlot(int number)
	{
		return inventory[number];
	}
	
	/**
	 * Tries to change the contents of the inventory by applying a Recipe a specified number of times.
	 * Used primarily to accept player's combine submissions.
	 * @param recipe the Recipe to apply
	 * @param times the number of times to apply the Recipe
	 */
	public static void tryApplyRecipe(Recipe recipe, String times)
	{
		// error to display to the player, if no error occurs, remains null
		String errorMessage = null;
		
		// length of the input the player entered
		int inputLength = times.length();
		
		// remove eventual zeroes
		int i = 0;
		while ((i + 1) < inputLength && times.charAt(i) == '0')
		{
			i++;
		}
		if (i != 0)
		{
			times = times.substring(i, inputLength - 1);
		}
		
		// subtract the number of zeroes removed
		inputLength -= i;
		
		// the player hasn't entered anything
		if (inputLength == 0)
		{
			errorMessage = "You must enter a number.";
		}
		// the player has entered more than 3 digit number, they certainly doesn't have enough Items for that
		else if (inputLength > 3)
		{
			errorMessage = "You don't have enough items.";
		}
		else
		{
			// the number of timer the player wants to apply the Recipe
			int numberOfTimes = Integer.parseInt(times);
			
			// check if the player has enough Items
			for (i = 0; i < recipe.requiredItems.length; i++)
			{
				Item currentItem = recipe.requiredItems[i];
				
				// Items available in the player's inventory
				int itemsAvailable = 0;
				for (int j = 0; j < 8; j++)
				{
					if (currentItem == inventory[j].getItem())
					{
						itemsAvailable += inventory[j].getCount();
					}
				}
				
				// Items required by the Recipe
				int itemsRequired = numberOfTimes * recipe.requiredCounts[i];
				
				// set an error message if there is not enough items
				if (itemsRequired > itemsAvailable)
				{
					errorMessage = "You need " + String.valueOf(itemsRequired) + " " + currentItem.name + " items and you have only " + String.valueOf(itemsAvailable) + ".";
					break;
				}
			}
			
			// check if an error hasn't occurred
			if (errorMessage == null)
			{
				// removed the required Items from the inventory
				for (i = 0; i < recipe.requiredItems.length; i++)
				{
					Item currentItem = recipe.requiredItems[i];
					
					// Items to remove by the Recipe
					int itemsToRemove = numberOfTimes * recipe.requiredCounts[i];
					
					// iterate through the inventory until all required Items are not removed
					for (int j = 0; j < 8; j++)
					{
						if (currentItem == inventory[j].getItem())
						{
							int slotCount = inventory[j].getCount();
							if (slotCount < itemsToRemove)
							{
								itemsToRemove -= slotCount;
								slotCount = 0;
							}
							else
							{
								slotCount -= itemsToRemove;
								itemsToRemove = 0;
							}
							inventory[j].setCount(slotCount);
							if (itemsToRemove == 0)
							{
								break;
							}
						}
					}
				}
				
				// stack of created Items to be added to the inventory
				ItemStack resultStack = new ItemStack();
				resultStack.setCount(numberOfTimes * recipe.resultCount);
				resultStack.setItem(recipe.resultItem);
				
				// add the created Items to the inventory or possibly drop them on the current Tile
				map.getTile(playerX, playerY).addInventoryItems(resultStack);
				
				return;
			}
		}
		
		// show the error message
		InfoBox box = new InfoBox(7 / 16f, 1 / 2f, errorMessage);
		Dimension windowSize = Gui.getContentSize();
		box.tryFitWindow(windowSize.width, windowSize.height);
		
		boxes.add(box);
	}
	
	/**
	 * Updates the state of the game. Should prepare data for the paint method.
	 * @param width width of the window
	 * @param height height of the window
	 * @param delta time spent since the last update in milliseconds
	 */
	public static void update(int width, int height, int delta)
	{
		long currentTime = System.currentTimeMillis();
		
		// the size of a Tile on the screen
		float tileSize = (float) (width * Math.pow(2, zoom - 9));
		
		// calculate the coordinates of the rectangle of the Map that can be seen on the screen
		float mapViewWidth = width * 7 / 8f / tileSize;
		float mapViewHeight = height / tileSize;
		float mapViewCornerX = playerX - (mapViewWidth - 1) / 2;
		float mapViewCornerY = playerY - (mapViewHeight - 1) / 2;
		
		// prepare the Map Tiles in this rectangle
		map.prepareTiles((int)Math.floor(mapViewCornerX), (int)Math.floor(mapViewCornerY), (int)Math.ceil(mapViewWidth), (int)Math.ceil(mapViewHeight));
		
		// move the player if needed
		if (arrowDown != 0 && downAt + Constants.MOVE_START_DELAY <= currentTime && lastMove + Constants.MOVE_INTERVAL <= currentTime)
		{
			movePlayer();
			lastMove = currentTime;
		}
		
		// update all Tile in the update radius
		map.updateTiles(playerX - Constants.UPDATE_RADIUS, playerY - Constants.UPDATE_RADIUS, Constants.UPDATE_RADIUS * 2, Constants.UPDATE_RADIUS * 2, delta);
	}
	
	/**
	 * Initializes everything that the game will need.
	 * @throws IOException 
	 */
	public static void initialize() throws IOException
	{
		// initialize the noises for generating terrain
		Noise.biomeNoises = new Noise[]{ new Noise(SEED + 1, Constants.BIOME_SCALE), new Noise(SEED + 2, Constants.BIOME_SCALE, 1 / 4d),
				new Noise(SEED + 3, Constants.BIOME_SCALE, 1 / 2d), new Noise(SEED + 4, Constants.BIOME_SCALE, 3 / 4d) };
		Noise.oreNoises = new Noise[] { new Noise(SEED + 5, Constants.ORE_SCALE), new Noise(SEED + 6, Constants.ORE_SCALE),
				new Noise(SEED + 7, Constants.ORE_SCALE) };
		
		// initialize the hash array for generating small structures
		Chunk.initializeHashArray();
		
		// initialize player's inventory
		for (int i = 0; i < 8; i++)
		{
			inventory[i] = new ItemStack();
		}
		
		// initialize indexes
		ObjectsIndex.loadIndexes();
		
		// initialize user interface
		Gui.initializeGui();
	}
}
