package com.github.mimo31.w50rld;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

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
		
		// create the window and override the paint with our paint paint method
		JFrame frame = new JFrame("50rld");
		
		// create a component to paint
		JComponent component = new JComponent(){
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 371375858304466268L;

			@Override
			public void paint(Graphics graphics)
			{
				Main.paint((Graphics2D) graphics, this.getWidth(), this.getHeight());
			}
			
		};
		
		// add the component to the frame
		frame.add(component);
		
		// configure the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		// create a timer that calls our update method and repaints when triggered
		Timer updateTimer = new Timer(16, new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Container contentPane = frame.getContentPane();
				update(contentPane.getWidth(), contentPane.getHeight(), 16);
				frame.repaint();
			}
			
		});
		
		// add key listener
		frame.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				Main.keyReleased(e);
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				Main.keyPressed(e);
			}
		});
		
		// set minimal window size
		frame.setMinimumSize(new Dimension(400, 400));
		
		// start the update timer
		updateTimer.start();
		
		// make the window visible
		frame.setVisible(true);
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
			inventory[i].draw(g, healthXEnd, paintY, width - healthXEnd, nextPaintY);
		}
	}
	
	/**
	 * Handles key releases for the game.
	 * @param e KeyEvent
	 */
	public static void keyReleased(KeyEvent e)
	{
		int code = e.getKeyCode();
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
		}
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
	 * Changes the player's coordinates according to the arrow key that is pressed.
	 */
	private static void movePlayer()
	{
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
	}
}
