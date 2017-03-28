package com.github.mimo31.w50rld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads the console and prepares the actions that the user requires to do.
 * @author mimo31
 */
public class Console
{
	/**
	 * List of the actions required by the user. They should be performed on the main thread.
	 */
	public static List<Runnable> adminActions = new ArrayList<Runnable>();
	
	/**
	 * Read the user's requests from the console. Stops when Main.running is set to false.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void operateConsole() throws IOException, InterruptedException
	{
		// scanner that read the input
		Scanner scanner = new Scanner(System.in);

		// console read loop
		while (Main.running)
		{
			// delay to make less overhead
			Thread.sleep(20);
			
			// if there is noting to read, go through the loop again
			if (System.in.available() == 0)
			{
				continue;
			}
			
			// command that was read
			String line = scanner.nextLine();
			
			// look for the fitting command
			if (line.startsWith("give "))
			{
				// the command without the give word
				String rest = line.substring(5);
				
				// the number of items to give
				int itemCount;
				
				// the part of the command describing the item name
				String itemString;
				
				// if the command rest starts with a digit, handle the string up to the next space as the count of the items
				if (rest.length() != 0 && '0' <= rest.charAt(0) && '9' >= rest.charAt(0))
				{
					// find the index of the first space
					// (spaceIndex remains -1 if there is no space)
					int spaceIndex = -1;
					for (int i = 0; i < rest.length(); i++)
					{
						if (rest.charAt(i) == ' ')
						{
							spaceIndex = i;
							break;
						}
					}
					if (spaceIndex == -1 || spaceIndex > 3)
					{
						// if the first space is too far (the number can't be longer than 3 digits)
						System.out.println("Unable to parse the give command.");
						continue;
					}
					// parse the item count
					itemCount = 0;
					int tenPow = 1;
					for (int i = spaceIndex - 1; i >= 0; i--)
					{
						itemCount += (rest.charAt(i) - '0') * tenPow;
						tenPow *= 10;
					}
					
					// the rest of the command without the count part is the name of the item
					itemString = rest.substring(spaceIndex + 1);
				}
				else
				{
					itemCount = 1;
					itemString = rest;
				}
				
				// look for an Item of that name
				Item item = ObjectsIndex.getItem(itemString);
				if (item == null)
				{
					System.out.println("Can't find item " + itemString + "!");
					continue;
				}
				
				// add the give action to the admin action list
				final int countVal = itemCount;
				synchronized(adminActions) {
					adminActions.add(() -> Main.tryAddInventoryItems(new ItemStack(item, countVal)));
					System.out.println("Given " + countVal + " " + item.name + " Item" + ((countVal == 1) ? "" : "s") + ".");
				}
				continue;
			}
			
			// no matches found
			System.out.println("Unable to parse input!");
			continue;
		}
		scanner.close();
	}
	
}
