package com.github.mimo31.w50rld;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Main {

	// the root directory for saved data and resources
	public static final String rootDirectory = "50rld";
	
	// current player's location
	public static int playerX;
	public static int playerY;
	
	// game seed
	public static final long SEED = (long) (Math.random() * Long.MAX_VALUE);
	
	// the zoom of the map (size of one Tile is width * 2 ^ (8 - zoom))
	public static int zoom = 0;
	
	// the map
	public static Map map = new Map();
	
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
		float tileSize = (float) (width * Math.pow(2, zoom - 8));
		
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
				Tile currentTile = tiles[(i - mapY) * mapWidth + (j - mapX)];
				
				// calculate the location where the Tile will be painted
				int paintX = (int)((j - mapViewCornerX) * tileSize);
				int paintY = (int)((i - mapViewCornerY) * tileSize);
				int nextPaintX = (int)((j + 1 - mapViewCornerX) * tileSize);
				int nextPaintY = (int)((i + 1 - mapViewCornerY) * tileSize);
				
				currentTile.paint(g, paintX, paintY, nextPaintX - paintX, nextPaintY - paintY);
			}
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
		// the size of a Tile on the screen
		float tileSize = (float) (width * Math.pow(2, zoom - 8));
		
		// calculate the coordinates of the rectangle of the Map that can be seen on the screen
		float mapViewWidth = width * 7 / 8f / tileSize;
		float mapViewHeight = height / tileSize;
		float mapViewCornerX = playerX - (mapViewWidth - 1) / 2;
		float mapViewCornerY = playerY - (mapViewHeight - 1) / 2;
		
		// prepare the Map Tiles in this rectangle
		map.prepareTiles((int)Math.floor(mapViewCornerX), (int)Math.floor(mapViewCornerY), (int)Math.ceil(mapViewWidth), (int)Math.ceil(mapViewHeight));
	}
	
	/**
	 * Initializes everything that the game will need.
	 * @throws IOException 
	 */
	public static void initialize() throws IOException
	{
		Path rootPath = Paths.get(rootDirectory);
		if (Files.exists(rootPath))
		{
			Files.createDirectory(rootPath);
		}
		else
		{
			
		}
		Noise.biomeNoises = new Noise[]{ new Noise(SEED + 1, Constants.BIOME_SCALE), new Noise(SEED + 2, Constants.BIOME_SCALE, 1 / 4d),
				new Noise(SEED + 3, Constants.BIOME_SCALE, 1 / 2d), new Noise(SEED + 4, Constants.BIOME_SCALE, 3 / 4d) };
		Noise.oreNoises = new Noise[] { new Noise(SEED + 5, Constants.ORE_SCALE), new Noise(SEED + 6, Constants.ORE_SCALE),
				new Noise(SEED + 7, Constants.ORE_SCALE) };
		Chunk.initializeHashArray();
	}
}
