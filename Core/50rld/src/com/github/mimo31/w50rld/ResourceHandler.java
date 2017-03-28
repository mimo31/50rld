package com.github.mimo31.w50rld;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
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
	private List<NamedResource<T>> resources;

	public ResourceHandler(int capacity)
	{
		this.resources = new ArrayList<NamedResource<T>>(capacity);
	}
	
	public ResourceHandler()
	{
		this.resources = new ArrayList<NamedResource<T>>();
	}
	
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
	 * Represents a texture loaded into the graphics memory.
	 */
	public static class Texture {

		// native size of the texture
		private int width, height;
		
		// texture identifier
		private int texture;

		public Texture(File file)
		{
			int[] pixels = null;
			try 
			{
				// load the image
				BufferedImage image = ImageIO.read(new FileInputStream(file));
				this.width = image.getWidth();
				this.height = image.getHeight();
				
				// get the pixel data from the image
				pixels = new int[this.width * this.height];
				image.getRGB(0, 0, this.width, this.height, pixels, 0, this.width);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			// restructured data
			int[] data = new int[this.width * this.height];
			
			// flip the positions of values in pixels
			for (int i = 0; i < this.width * this.height; i++)
			{
				int a = (pixels[i] & 0xff000000) >> 24;
				int r = (pixels[i] & 0xff0000) >> 16;
				int g = (pixels[i] & 0xff00) >> 8;
				int b = (pixels[i] & 0xff) >> 0;
				
				data[i] = a << 24 | b << 16 | g << 8 | r;
			}

			// set the texture properties
			this.texture = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, this.texture);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, createIntBuffer(data));
		}

		/**
		 * Creates an IntBuffer from an int array.
		 * @return the created IntBuffer
		 */
		private static IntBuffer createIntBuffer(int[] array)
		{
			IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
			result.put(array).flip();
			return result;
		}

		/**
		 * Bind the texture for OpenGL drawing.
		 */
		public void bind()
		{
			glBindTexture(GL_TEXTURE_2D, this.texture);
		}

		/**
		 * Unbinds the texture from OpenGL drawing.
		 */
		public void unbind()
		{
			glBindTexture(GL_TEXTURE_2D, 0);
		}
	}


	// handler for textures
	private static ResourceHandler<Texture> textures;

	/**
	 * Returns the requested texture.
	 * All the textures has been already loaded to the memory, so this method only looks there.
	 * Throws a RuntimeException, if the texture could not be found.
	 * @param name name of the file (the filename without the extension)
	 * @return texture with the specified name
	 */
	public static Texture getTexture(String name)
	{
		// get the texture
		Texture texture = textures.get(name);

		if (texture == null)
		{
			// texture does not exist - throw an exception
			throw new RuntimeException("The requested texture \"" + name + "\" could not be found in the memory.");
		}

		// get the right size from the texture and return
		return texture;
	}

	
	/**
	 * Loads all the textures it can find in the appropriate folder.
	 */
	public static void loadTextures()
	{
		// file with the textures
		File textureDirectory = new File(Main.rootDirectory + "/Resources/Images");
		
		// get all the texture files from the texture file
		File[] txFiles = textureDirectory.listFiles((File file) ->
		{
			String name = file.getName();
			int len = name.length();
			if (len < 5)
			{
				return false;
			}
			return name.charAt(len - 1) == 'g' && name.charAt(len - 2) == 'n' && name.charAt(len - 3) == 'p' && name.charAt(len - 4) == '.';
		});
		
		textures = new ResourceHandler<Texture>(txFiles.length);
		
		// load and insert each of the textures
		for (int i = 0; i < txFiles.length; i++)
		{
			String name = txFiles[i].getName();
			name = name.substring(0, name.length() - 4);
			textures.insert(new Texture(txFiles[i]), name);
		}
	}
	
}
