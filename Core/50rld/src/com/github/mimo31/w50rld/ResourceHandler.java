package com.github.mimo31.w50rld;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Stores name-object pairs. Enables accessing the objects by name in O(log(n)) thanks to binary search.
 * Created for storing game resources loaded into memory
 * 
 * @author mimo31
 *
 * @param <T> type of objects to store
 */
public class ResourceHandler<T> {

	// stored name-object pairs
	private List<NamedResource<T>> resources = new ArrayList<NamedResource<T>>();
	
	/**
	 * Inserts the specified resources to the structure with the specified name.
	 * @param resource the resource to insert
	 * @param name name of the resource
	 */
	public void insert(T resource, String name)
	{
		int start = 0;
		int end = this.resources.size();
		
		// shrink the range of possible indices until there is only one
		while (start != end)
		{
			// index in the middle of the range
			int middle = start + (end - start) / 2;
			
			// compare the middle resource name to the name of the resource to insert
			int order = name.compareTo(this.resources.get(middle).name);
			if (order < 0)
			{
				end = middle;
			}
			else
			{
				start = middle + 1;
			}
		}
		this.resources.add(start, new NamedResource<T>(resource, name));
	}
	
	/**
	 * Finds the resource with the specified name. Returns null when to found.
	 * @param name name of the resource
	 * @return the resource or null when not found
	 */
	public T get(String name)
	{
		int start = 0;
		int end = this.resources.size();

		// shrink the range of possible indices until there is only one
		while (start < end)
		{
			// index in the middle of the range
			int middle = start + (end - start) / 2;
			NamedResource<T> middleResource = this.resources.get(middle);
			
			// compare the middle resource name to the name of the resource to insert
			int order = name.compareTo(middleResource.name);
			if (order == 0)
			{
				// resource found, return it
				return middleResource.resource;
			}
			else if (order < 0)
			{
				end = middle;
			}
			else
			{
				start = middle + 1;
			}
		}
		
		// not found
		return null;
	}
	
	/**
	 * A name-object pair to make manipulation easier.
	 * 
	 * @author mimo31
	 *
	 * @param <T> type of the object (resource) to store
	 */
	private static class NamedResource<T> {
		
		T resource;
		String name;
		
		private NamedResource(T resource, String name)
		{
			this.resource = resource;
			this.name = name;
		}
	}
	
	/**
	 * Creates an Image compatible with the graphics device.
	 * @param width width of the image
	 * @param height height of the image
	 * @return a compatible Image
	 */
	private static BufferedImage createCompatibleImage(int width, int height)
	{
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    GraphicsDevice device = env.getDefaultScreenDevice();
	    GraphicsConfiguration config = device.getDefaultConfiguration();
	    return config.createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	}
	
	/**
	 * Holds one BufferedImage in various sizes to avoid scaling the image multiple times.
	 * 
	 * @author mimo31
	 *
	 */
	private static class Texture {
		
		// the original image for creating scaled copies
		final BufferedImage original;
		
		// scaled copies sorted from the smallest to the largest
		final List<BufferedImage> images = new ArrayList<BufferedImage>();

		private Texture(BufferedImage original)
		{
			// converts the Image to a compatible image
			this.original = createCompatibleImage(original.getWidth(), original.getHeight());
			this.original.getGraphics().drawImage(original, 0, 0, null);
		}
		
		/**
		 * Looks for the image in specified width, if not found, scales the original and stores the scaled copy.
		 * Returns the proper copy in both cases
		 * @param width the required width of the image
		 * @return the scaled copy of the original
		 */
		private BufferedImage getImage(int width)
		{
			
			// check if it is the same size as the original
			if (width == this.original.getWidth())
			{
				return original;
			}
			
			// search for this size in the list
			int start = 0;
			int end = this.images.size();
			while (start < end)
			{
				int middle = start + (end - start) / 2;
				BufferedImage middleImage = this.images.get(middle);
				int middleImageWidth = middleImage.getWidth();
				if (middleImageWidth == width)
				{
					// found - return it
					return middleImage;
				}
				else if (width < middleImageWidth)
				{
					end = middle;
				}
				else
				{
					start = middle + 1;
				}
			}
			// this size not found in the list - scale the original:
			
			// calculate the height of the scaled instance
			int newHeight = original.getHeight() * width / original.getWidth();
			
			// scale the original into an Image
			Image tempResize = this.original.getScaledInstance(width, newHeight, Image.SCALE_SMOOTH);
			
			// create a BufferedImage to put the copy in
			BufferedImage resizedOriginal = createCompatibleImage(width, newHeight);
			
			// create a Graphics for it to draw the scaled instance
			Graphics2D g = resizedOriginal.createGraphics();
			
			// draw the scaled instance
			g.drawImage(tempResize, 0, 0, null);
			
			// clear the table
			g.dispose();
			tempResize.flush();
			
			// insert the image to the list
			this.images.add(start, resizedOriginal);
			
			return resizedOriginal;
		}
		
	}
	
	// handler for textures - images
	private static ResourceHandler<Texture> textures = new ResourceHandler<Texture>();
	
	/**
	 * Returns an image of the specified name from the image directory. Scales it to match the specified width.
	 * Caches the image for speed up.
	 * Returns null if the width is 0.
	 * @param name name of the image file
	 * @param width width of the image to return
	 * @return the (possibly) scaled image, or null if width is zero
	 */
	public static BufferedImage getImage(String name, int width)
	{
		if (width == 0)
		{
			return null;
		}
		
		// get the texture
		Texture texture = textures.get(name);
		
		if (texture == null)
		{
			// texture does not exist - load the image and add the new texture
			try {
				texture = new Texture(ImageIO.read(new File(Main.rootDirectory + "\\Resources\\Images\\" + name)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			textures.insert(texture, name);
		}
		
		// get the right size from the texture and return
		return texture.getImage(width);
	}
	
}
