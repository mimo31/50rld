package com.github.mimo31.w50rld;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.mimo31.w50rld.Box.CornerAlign;
import com.github.mimo31.w50rld.Item.ItemAction;
import com.github.mimo31.w50rld.Structure.StructureAction;
import com.github.mimo31.w50rld.TextDraw.TextAlign;

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
	
	// all the entities on the map
	public static final List<EntityData> entities = new ArrayList<EntityData>();
	
	// the arrow keycode that is currently pressed and we are therefore moving that direction (0 in no key is pressed)
	private static int arrowDown = 0;
	
	// time when the arrow key was pressed
	private static long downAt = 0;
	// time when the last move was done
	private static long lastMove = 0;
	
	// current number of health points
	private static int health = 16;
	
	// player's inventory
	private static final ItemStack[] inventory = new ItemStack[8];
	
	// opened Boxes
	private static List<Box> boxes = new ArrayList<Box>();
	
	// players hit state
	private static float hitState = -1;
	
	// whether the dead screen is shown
	public static boolean deadScreen = false;
	
	// whether the selected inventory slot is currently being chosen
	private static boolean selectingItem = false;
	
	// the selected inventory slot
	private static int selectedItem = 0;
	
	// if set to false, the game loop will break the next time
	public static boolean running = true;
	
	public static void main(String[] args)
	{
		try
		{
			// initialize everything
			initialize();
			
			// run everything
			gameLoop();
		}
		catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Renders the current state of the game through with the current OpenGL context. Should not perform extensive non drawing actions.
	 */
	public static void render()
	{
		// get ready for drawing
		glfwSwapBuffers(Gui.window);
		glClearColor(1f, 1f, 1f, 1f);
		glClear(GL_COLOR_BUFFER_BIT);
		
		int width = Gui.width;
		int height = Gui.height;
		
		// the width of a Tile on the canvas
		float tileWidth = (float) (Math.pow(2, zoom - 9)) * 2;
		
		// the width of a Tile on the canvas
		float tileHeight = tileWidth * width / height;
		
		// coordinates of the rectangle of the Map that can be seen on the screen
		float mapViewWidth = 7 / 4f / tileWidth; // the decimal number of tiles that fit in on the screen in the horizontal direction
		float mapViewHeight = 2 / tileHeight; // the decimal number of tiles that fit in on the screen in the vertical direction
		float mapViewCornerX = playerX - (mapViewWidth - 1) / 2; // the decimal gamemap coordinates of the view of the map
		float mapViewCornerY = playerY - (mapViewHeight - 1) / 2;
		
		// convert the coordinates to ints
		int mapX = (int)Math.floor(mapViewCornerX);
		int mapY = (int)Math.floor(mapViewCornerY);
		int mapWidth = (int)Math.ceil(mapViewWidth);
		int mapHeight = (int)Math.ceil(mapViewHeight);

		// get the Tiles in the rectangle
		Tile[] tiles = map.getTiles(mapX, mapY, mapWidth + 1, mapHeight + 1);
		
		
		// iterate through all the tiles of the rectangle and draw them
		for (int i = mapY; i <= mapY + mapHeight; i++)
		{
			for (int j = mapX; j <= mapX + mapWidth; j++)
			{
				Tile currentTile = tiles[(i - mapY) * (mapWidth + 1) + (j - mapX)];
				
				// calculate the location where the Tile will be painted
				float paintX = (j - mapViewCornerX) * tileWidth - 1;
				float paintY = (i - mapViewCornerY) * tileHeight - 1;
				float nextPaintX = (j + 1 - mapViewCornerX) * tileWidth - 1;
				float nextPaintY = (i + 1 - mapViewCornerY) * tileHeight - 1;
								
				currentTile.draw(paintX, paintY, nextPaintX, nextPaintY, j, i);

				// draw the player
				if (j == playerX && i == playerY && !deadScreen)
				{
					float drawX = paintX + (nextPaintX - paintX) / 4;
					float drawY = paintY + (nextPaintY - paintY) / 4;
					float endX = paintX + (nextPaintX - paintX) * 3 / 4;
					float endY = paintY + (nextPaintY - paintY) * 3 / 4;
					//float drawSize = Math.max((nextPaintX - paintX) / 2, (nextPaintY - paintY) / 2);
					
					PaintUtils.drawTexture(drawX, drawY, endX, endY, "Player");
					
					if (hitState != -1)
					{
						// draw the transparent red square over the player
						glColor4f(1, 0, 0, 1 - hitState);
						PaintUtils.drawRectangleP(drawX, drawY, endX, endY);
					}
				}
			}
		}

		// draw the entities
		for (int i = 0, n = entities.size(); i < n; i++)
		{
			EntityData currentEntity = entities.get(i);
			
			// if the entity can be seen in the map view
			if (currentEntity.x + 1 > mapX && currentEntity.y + 1 > mapY && currentEntity.x - 1 < mapX + mapWidth && currentEntity.y - 1 < mapY + mapHeight)
			{
				// create correct transformation matrices for drawing the entity
				
				glPushMatrix();
				
				// translate the drawing to the correct place on the screen
				glTranslatef((currentEntity.x - mapViewCornerX) * tileWidth - 1, (currentEntity.y - mapViewCornerY) * tileHeight - 1, 0);
				// scale the drawing to make the entity as big as tiles
				glScalef(tileWidth / 2, tileHeight / 2, 1);
				// rotate the drawing according to the rotation of the entity
				glRotatef((float)(currentEntity.rotation * 180 / Math.PI) - 90, 0, 0, 1f);
				
				// draw the entity
				currentEntity.draw();

				// get the drawing to the previous state 
				glPopMatrix();
			}
		}
		
		// draw the dead screen
		if (deadScreen)
		{
			glColor4f(1f, 0, 0, 0.5f);
			// draw red over the map view
			PaintUtils.drawRectangle(-1, -1, 7 / 4f, 2);
			
			// draw the message
			TextDraw.drawText("You died.", 7 / 16f - 1, -1 / 2f, 7 / 8f, 1 / 4f, TextAlign.MIDDLE, 0);
		}
		
		// draw the health bar
		
		// x coordinate of the left end of the health bar
		float healthXStart = 7 / 4f - 1;
		
		// x coordinate of the right end of the health bar
		float healthXEnd = 15 / 8f - 1;
		
		// draw the health background
		glColor3f(153 / 255f, 0, 0);
		PaintUtils.drawRectangle(healthXStart, -1, healthXEnd - healthXStart, 2);
		
		// y coordinate of the bottom end of the heart
		float heartYStart = 1 - (healthXEnd - healthXStart) * width / height;
		
		// draw the heart
		PaintUtils.drawTexture(healthXStart, heartYStart, healthXEnd, 1, "Heart");
		
		// draw the amount of health points
		glColor3f(1f, 0, 0);
		float healthBarHeight = (heartYStart + 1) * health / Constants.MAX_HEALTH;
		PaintUtils.drawRectangle(healthXStart, -1, healthXEnd - healthXStart, healthBarHeight);
		
		// draw the bars to break the amount of health points
		glColor3f(0, 0, 0);
		for (int i = 1; i < Constants.MAX_HEALTH; i++)
		{
			PaintUtils.drawRectangle(healthXStart, -1 + (1 + heartYStart) * i / Constants.MAX_HEALTH - 1 / 512f, healthXEnd - healthXStart, 1 / 256f);
		}
		
		// draw the inventory
		for (int i = 0; i < 8; i++)
		{
			float paintY = 1 - i / 4f - 1 / 4f;
			inventory[i].draw(healthXEnd, paintY, 1, paintY + 1 / 4f, selectedItem == i ? new Color(191, 255, 191) : Color.white);
		}
		
		// draw the boxes
		for (int i = 0, n = boxes.size(); i < n; i++)
		{
			boxes.get(i).draw();
		}
	}
	
	/**
	 * Handles key releases for the game.
	 * @param code GLFW code of the key
	 */
	public static void keyReleased(int code)
	{
		int numberOfBoxes = boxes.size();

		// copy selectingItem's value, so that the current value can be used throughout this method but the selectingItem variable is false by default
		boolean nowSelectingItem = selectingItem;
		selectingItem = false;
		
		if (deadScreen)
		{
			// if the dead screen is shown and the enter key is released
			if (code == GLFW_KEY_ENTER)
			{
				// hide the dead screen
				deadScreen = false;
				
				// move player back to the origin
				playerX = 0;
				playerY = 0;
				
				// heal the player
				health = Constants.MAX_HEALTH;
			}
			
			// return because other actions are not allowed while the dead screen is shown
			return;
		}
		
		// if at least one Box is opened, trigger its key function or remove it if the key was Escape
		if (numberOfBoxes != 0)
		{
			if (code == GLFW_KEY_ESCAPE)
			{
				// remove the Box
				boxes.remove(numberOfBoxes - 1);
			}
			else
			{
				// pass the event to the Box
				Box topBox = boxes.get(numberOfBoxes - 1);
				boxes.get(numberOfBoxes - 1).keyReleased(code, () -> boxes.remove(topBox));
			}
			return;
		}
		switch (code)
		{
			// handle zooming in and out
			case GLFW_KEY_KP_ADD:
				if (zoom != Constants.MAX_ZOOM)
				{
					zoom++;
				}
				break;
			case GLFW_KEY_KP_SUBTRACT:
				if (zoom != 0)
				{
					zoom--;
				}
				break;
			
			// handle arrow releases - stop moving
			case GLFW_KEY_UP:
			case GLFW_KEY_DOWN:
			case GLFW_KEY_LEFT:
			case GLFW_KEY_RIGHT:
				arrowDown = (code == arrowDown) ? 0 : arrowDown;
				break;
			
			// display the action window
			case GLFW_KEY_A:
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
								InfoBox box = new InfoBox(-1 / 8f, 0, "No space in the inventory!", CornerAlign.TOPLEFT);
								
								// fit the box in the window
								box.tryFitWindow();
								
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
					
					OptionBox box = new OptionBox(options, actions, -1 / 8f, 0, "Grab items", CornerAlign.TOPLEFT);
					
					// fit the box in the window
					box.tryFitWindow();
					
					boxes.add(box);
				}
				else if (currentTile.hasStructures())
				{
					// get the top structure of the Tile on which the player is currently standing on
					Structure topStructure = currentTile.getTopStructure().structure;
					
					// create arrays for options and actions of the OptionBox
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
					boxes.add(new OptionBox(options, actions, -1 / 8f, 0, topStructure.name, CornerAlign.TOPLEFT));
				}
				else if (currentTile.getDepth() < 32)
				{
					// provide an interface with an action of taking a Rock by hand
					Runnable[] actions = new Runnable[] {() -> {
						ItemStack rocks = new ItemStack();
						rocks.setCount(1);
						rocks.setItem(ObjectsIndex.getItem("Rock"));
						currentTile.addInventoryItems(rocks);
						currentTile.incrementDepth();
					}
					};
					boxes.add(new OptionBox(new String[] { "Take A Rock" }, actions, -1 / 8f, 0, "Rock", CornerAlign.TOPLEFT));
				}
				break;
			// show a Combine Box
			case GLFW_KEY_C:
				CombineBox box = new CombineBox(-1 / 8f, 0, CornerAlign.TOPLEFT);
				box.tryFitWindow();
				boxes.add(box);
				break;
			// hit the surrounding entities
			case GLFW_KEY_X:
				Item itemSelected = inventory[selectedItem].getItem();
				
				WeaponItem weapon = itemSelected instanceof WeaponItem ? (WeaponItem) itemSelected : null;
				
				// hit power of the player
				float hitPower = weapon != null ? weapon.getHitPower() : 1;
				
				// hit radius of the player
				float hitRadius = weapon != null ? weapon.getHitRadius() : 1;
				
				// for all entities: if it is in the hit radius, hit it
				for (int i = 0, n = entities.size(); i < n; i++)
				{
					EntityData currentEntity = entities.get(i);
					
					float xDistance = currentEntity.x - playerX - 0.5f;
					if (xDistance > hitRadius || -xDistance > hitRadius)
					{
						continue;
					}
					float yDistance = currentEntity.y - playerY - 0.5f;
					if (yDistance > hitRadius || -yDistance > hitRadius)
					{
						continue;
					}
					
					double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
					if (distance < hitRadius)
					{
						currentEntity.hit(hitPower);
					}
				}
				break;
			// start the selecting of an inventory slot
			case GLFW_KEY_S:
				selectingItem = true;
				break;
		}
		
		// check if the player wants to see the actions of an inventory slot
		if ((code >= GLFW_KEY_1 && code <= GLFW_KEY_8) || (code >= GLFW_KEY_KP_1 && code <= GLFW_KEY_KP_8))
		{
			// inventory slot the player chose
			int slot;
			if (code >= GLFW_KEY_1 && code <= GLFW_KEY_8)
			{
				slot = code - GLFW_KEY_1;
			}
			else
			{
				slot = code - GLFW_KEY_KP_1;
			}
			
			// select if currently selecting, else show actions
			if (nowSelectingItem)
			{
				selectedItem = slot;
			}
			else
			{
				showItemActions(slot);
			}
		}
	}
	
	/**
	 * Returns the player's inventory.
	 * @return player's inventory
	 */
	public static ItemStack[] getInventory()
	{
		return inventory;
	}
	
	/**
	 * Shows an OptionBox specific to actions of an Item in a specified inventory slot.
	 * @param slot number of the slot
	 */
	private static void showItemActions(int slot)
	{
		// return if nothing is there
		if (inventory[slot].getCount() == 0)
		{
			return;
		}
		
		Item item = inventory[slot].getItem();
		
		List<ItemAction> validActions = new ArrayList<ItemAction>();

		if (item instanceof Meal)
		{
			// add the Eat action
			validActions.add(new ItemAction("Eat") {

				@Override
				public boolean action(int tileX, int tileY) {
					health += ((Meal) item).healthGain();
					if (health > Constants.MAX_HEALTH)
					{
						health = Constants.MAX_HEALTH;
					}
					return true;
				}
					
			});
		}
		
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
		
		options[options.length - 1] = "Drop";
		
		actions[actions.length - 1] = () -> {
			// show an InputBox to ask the user for the amount of Items to drop
			String request = "Number of items: ";
			
			// submit by calling the drop function
			Consumer<String> submitFunction = (inputString) -> { drop(slot, inputString); };
			
			InputBox box = new InputBox(15 / 16f, 1 - slot / 4f - 1 / 8f /* coordinates of the center of the slot */,
					request, submitFunction, InputBox.DIGIT_INTERPRETER, CornerAlign.TOPRIGHT);
			box.tryFitWindow();
			boxes.add(box);
		};
		
		for (int i = 0, n = validActions.size(); i < n; i++)
		{
			ItemAction currentAction = validActions.get(i);
			options[i] = currentAction.name;
			actions[i] = () -> {
				if (currentAction.action(playerX, playerY))
				{
					inventory[slot].setCount(inventory[slot].getCount() - 1);
				}
			};
		}
		
		// headline for the OptionBox
		String itemName = item.name;
		
		// show the OptionBox
		OptionBox box = new OptionBox(options, actions, 15 / 16f, 1 - slot / 4f - 1 / 8f /* coordinates of the center of the slot */,
				itemName, CornerAlign.TOPRIGHT); 
		box.tryFitWindow();
		boxes.add(box);
	}
	
	/**
	 * Handles key types for the game.
	 * @param code key code of the pressed key
	 */
	public static void keyPressed(int code)
	{
		// don't allow anything while the dead screen is shown
		if (deadScreen)
		{
			return;
		}
		if (arrowDown == 0 && (code == GLFW_KEY_UP || code == GLFW_KEY_DOWN || code == GLFW_KEY_LEFT || code == GLFW_KEY_RIGHT))
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
	 * @return true if successfully added at least some items into the inventory, else false
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
			case GLFW_KEY_UP:
				playerY++;
				break;
			case GLFW_KEY_DOWN:
				playerY--;
				break;
			case GLFW_KEY_LEFT:
				playerX--;
				break;
			case GLFW_KEY_RIGHT:
				playerX++;
				break;
		}
	}
	
	/**
	 * Handles mouse clicks for the game.
	 * @param location the cursor position relative to the content pane at the moment of the click
	 */
	public static void mouseClicked(Point location)
	{
		int width = Gui.width;
		int height = Gui.height;
		
		// the number of Boxes
		int numberOfBoxes = boxes.size();
		
		// if there are some boxes, check whether the top one was clicked
		if (numberOfBoxes != 0)
		{
			// Runnable that removes the current top Box
			Box topBox = boxes.get(numberOfBoxes - 1);
			Runnable closeAction = () -> boxes.remove(topBox);
			
			// if the top box was not clicked, remove it
			if (!topBox.mouseClicked(location, closeAction))
			{
				closeAction.run();
			}
		}
		else
		{
			// if the player click on the inventory
			if (location.x >= width * 15 / 16)
			{
				// number of the slot clicked
				int slotClicked = location.y / (height / 8);

				// show actions
				showItemActions(slotClicked);
			}
		}
	}
	
	/**
	 * Drops a specified number of Items from a specified inventory slot on the current Tile.
	 * Used primarily to accept player's drop submissions.
	 * @param slotNumber number of the slot to drop from
	 * @param numberOfItems text representation of the number of Items to drop
	 */
	private static void drop(int slotNumber, String numberOfItems)
	{
		// error to display to the user, remains null if no error occurs
		String errorMessage = null;
		
		numberOfItems = StringUtils.trimZeroes(numberOfItems);
		
		int inputLength = numberOfItems.length();
		
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
		InfoBox box = new InfoBox(15 / 16f, 1 - slotNumber / 4f - 1 / 8f, errorMessage, CornerAlign.TOPRIGHT);
		box.tryFitWindow();
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
	 * Takes health points from the player.
	 * @param hitPower the amount of health points to take from the player
	 */
	public static void hitPlayer(int hitPower)
	{
		// decrement hp
		health -= hitPower;
		if (health <= 0)
		{
			// the player died
			health = 0;
			deadScreen = true;
			
			Tile currentTile = map.getTile(playerX, playerY);
			
			// drop player's items on their current Tile
			for (int i = 0; i < 8; i++)
			{
				if (inventory[i].getCount() != 0)
				{
					currentTile.addItems(inventory[i]);
					inventory[i].setCount(0);
				}
			}
			
			// close all boxes
			boxes.clear();
			
			// stop any movement
			arrowDown = 0;
			
			// cancel item selection
			selectingItem = false;
		}
		else
		{
			// reset the hit state
			hitState = 0;
		}
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
		
		times = StringUtils.trimZeroes(times);
		
		// length of the input the player entered
		int inputLength = times.length();
		
		// the player hasn't entered anything
		if (inputLength == 0)
		{
			errorMessage = "Enter a number.";
		}
		// the player has entered more than 3 digit number, they certainly doesn't have enough Items for that
		else if (inputLength > 3)
		{
			errorMessage = "Not enough items.";
		}
		else
		{
			// the number of timer the player wants to apply the Recipe
			int numberOfTimes = Integer.parseInt(times);
			
			// check if the player has enough Items
			for (int i = 0; i < recipe.requiredItems.length; i++)
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
					errorMessage = "Need " + String.valueOf(itemsRequired) + " " + currentItem.name + " items. (have " + String.valueOf(itemsAvailable) + ")";
					break;
				}
			}
			
			// check if an error hasn't occurred
			if (errorMessage == null)
			{
				// removed the required Items from the inventory
				for (int i = 0; i < recipe.requiredItems.length; i++)
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
		InfoBox box = new InfoBox(-1 / 8f, 0, errorMessage, CornerAlign.TOPLEFT);
		box.tryFitWindow();
		
		boxes.add(box);
	}
	
	public static double calculateGrowProbability(int growTime, int growTimeFactor, int deltaTime)
	{
		// probability of something growing in the time interval
		// this formula is based on this expression:
		// 	the probability that the seeds had already grown up given the time they are in the ground in ms:
		//		P(x) = 1 - 0.5^((x / 60000)^(2))
		//		https://www.wolframalpha.com/input/?i=plot+1+-+0.5%5E((x+%2F+60000)%5E(2))+from+x+%3D+0+to+120000
		// the formula below is then (P(growTime + deltaTime) - P(growTime)) / (1 - P(growTime)) simplified
		return 1 - Math.pow(0.5, deltaTime * (deltaTime + 2 * growTime) * Math.pow(growTimeFactor, -2));
	}
	
	/**
	 * Updates the state of the game. Should prepare data for the render method.
	 * @param delta time spent since the last update in milliseconds
	 */
	public static void update(int delta)
	{
		long currentTime = System.currentTimeMillis();
		
		// the size of a Tile on the screen
		float tileSize = (float) (Gui.width * Math.pow(2, zoom - 9));
		
		// calculate the coordinates of the rectangle of the Map that can be seen on the screen
		float mapViewWidth = Gui.width * 7 / 8f / tileSize;
		float mapViewHeight = Gui.height / tileSize;
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
		
		// update all Tiles in the update radius
		map.updateTiles(playerX - Constants.UPDATE_RADIUS, playerY - Constants.UPDATE_RADIUS, Constants.UPDATE_RADIUS * 2, Constants.UPDATE_RADIUS * 2, delta);
		
		// update entities
		for (int i = 0, n = entities.size(); i < n; i++)
		{
			EntityData currentEntity = entities.get(i);
			if (currentEntity.update(delta))
			{
				entities.remove(i);
				i--;
				n--;
				
				// drops the drops from the entity
				ItemStack[] drops = currentEntity.entity.getDrops();
				if (drops.length == 0)
				{
					continue;
				}
				int dropX = (int) Math.floor(currentEntity.x);
				int dropY = (int) Math.floor(currentEntity.y);
				Tile dropTile = map.getTile(dropX, dropY);
				for (int j = 0; j < drops.length; j++)
				{
					if (drops[j].getCount() != 0)
					{
						dropTile.addItems(drops[j]);
					}
				}
			}
		}
		
		// update the hit state
		if (hitState != -1)
		{
			hitState += delta / 1024d;
			if (hitState > 1)
			{
				hitState = -1;
			}
		}
	}
	
	/**
	 * Initializes everything that the game will need.
	 * @throws IOException 
	 */
	public static void initialize() throws IOException
	{
		// initialize the hash array for generating small structures
		Chunk.initializeHashArray();
		
		// initialize player's inventory
		for (int i = 0; i < 8; i++)
		{
			inventory[i] = new ItemStack();
		}
		
		// initialize indexes
		ObjectsIndex.loadIndexes();

		// initialize the noises for generating terrain
		Noise.biomeNoises = new Noise[ObjectsIndex.biomes.size()];
		for (int i = 0; i < Noise.biomeNoises.length; i++)
		{
			Noise.biomeNoises[i] = new Noise(SEED + (i + 1), ObjectsIndex.biomes.get(i).scale, i / (double)Noise.biomeNoises.length);
		}
		Noise.oreNoises = new Noise[] { new Noise(SEED + 5, Constants.ORE_SCALE), new Noise(SEED + 6, Constants.ORE_SCALE),
				new Noise(SEED + 7, Constants.ORE_SCALE) };
		
		// initialize user interface
		Gui.initializeGui();
	}
	
	/**
	 * Calls the update and render methods repeatedly with the correct timing until Main.running is set to false.
	 * @throws InterruptedException
	 */
	public static void gameLoop() throws InterruptedException
	{
		// thread that takes the input from the console (see the Console class)
		Thread consoleThread = new Thread(() -> {
			try {
				Console.operateConsole();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		// start the console thread
		consoleThread.start();
		
		// time taken by the last run through the loop (to be passed to the update method)
		long lastDifference = 0;
		// start time of the current run through the loop
		long startTime = System.nanoTime();
		
		// the game loop
		while (running)
		{
			// poll the GLFW event
			glfwPollEvents();
			
			// call the update method
			update((int)lastDifference / 1000000);
			
			// call the render method
			render();
			
			// if the object isn't locked, perform all the user admin actions
			synchronized (Console.adminActions)
			{
				while (Console.adminActions.size() != 0)
				{
					Console.adminActions.get(0).run();
					Console.adminActions.remove(0);
				}
			}
			
			// wait the remaining time, if needed
			long endTime = System.nanoTime();
			long toWait = 1000000000 / Constants.TARGET_FPS - (endTime - startTime);
			if (toWait > 0)
			{
				Thread.sleep(toWait / 1000000, (int)(toWait % 1000000));
			}
			endTime = System.nanoTime();
			lastDifference = endTime - startTime;
			startTime = endTime;
			
			// if the window should close, set running to false
			if (glfwWindowShouldClose(Gui.window))
			{
				running = false;
			}
		}
		
		// wait for the console thread to end (it should, since running has been set to false)
		consoleThread.join();
	}
}
