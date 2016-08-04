package com.github.mimo31.w50rld;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Uses Perlin Noise to create structures.
 * @author mimo31
 *
 */
public class Noise {

	// an array of 256 bytes to generate gradient vectors when generating noise, every value appears exactly once
	final byte[] hashArray = new byte[256];
	
	// the scale of the noise, the bigger the scale, the bigger the structures of the noise
	final int scale;
	
	// noises for the four biomes: water, tree, grass, and sand respectively 
	public static Noise[] biomeNoises;
	
	// noises for the three ores: iron, coal, and gold respectively
	public static Noise[] oreNoises;
	
	/**
	 * Creates a noise object with its hashArray based on the seed.
	 * @param seed
	 */
	public Noise(long seed, int scale)
	{
		Random r = new Random(seed);
		
		// create a list of values from 0 to 256, randomly permuted
		List<Integer> list = new ArrayList<Integer>(256); 
		for (int i = 0; i < 256; i++)
		{
			list.add(r.nextInt(i + 1), i);
		}
		
		// convert the list to the hashArray
		for (int i = 0; i < 256; i++)
		{
			hashArray[i] = list.get(i).byteValue();
		}
		
		this.scale = scale;
	}
	
	/**
	 * Calculates the noise value of this noise object at the specified locations. xs.length should equal ys.length
	 * @param xs array of the x coordinates
	 * @param ys array of the y coordinates
	 * @return the values of the noise at (x, y) pairs
	 */
	public double[] getNoise(int[] xs, int[] ys)
	{
		// create a list of required GradientVectors (the Points in the list represents the locations of the required vectors)
		List<Point> requiredVectors = new ArrayList<Point>();
		for (int i = 0; i < xs.length; i++)
		{
			// location of the GredientVector corresponding to the current point in the top left corner
			int gradX = (int)Math.floor(xs[i] / (double)this.scale);
			int gradY = (int)Math.floor(ys[i] / (double)this.scale);
			
			requiredVectors.add(new Point(gradX, gradY));
			requiredVectors.add(new Point(gradX + 1, gradY));
			requiredVectors.add(new Point(gradX, gradY + 1));
			requiredVectors.add(new Point(gradX + 1, gradY + 1));
		}

		// prepare the required GradientVectors
		List<GradientVector> gradientVectors = new ArrayList<GradientVector>();
		for (int i = 0, n = requiredVectors.size(); i < n; i++)
		{
			Point currentVecLocation = requiredVectors.get(i);
			
			// look for this vector location earlier in the requiredVectors list
			boolean found = false;
			for (int j = 0; j < i; j++)
			{
				Point currentComparingVectorLocation = requiredVectors.get(j);;
				if (currentVecLocation.x == currentComparingVectorLocation.x && currentVecLocation.y == currentComparingVectorLocation.y)
				{
					found = true;
					break;
				}
			}
			if (!found)
			{
				// the corresponding GradientVector is not in the list -> create it and add it there
				gradientVectors.add(getGradientVector(currentVecLocation.x, currentVecLocation.y));
			}
		}
		
		// calculate the noise values
		double[] noises = new double[xs.length];
		for (int i = 0; i < xs.length; i++)
		{
			// location of the GredientVector corresponding to the current point in the top left corner
			int gradX = (int)Math.floor(xs[i] / (double)this.scale);
			int gradY = (int)Math.floor(ys[i] / (double)this.scale);
			
			// get the required GradientVectors from the list
			GradientVector grad0 = searchGradientVectors(new Point(gradX, gradY), gradientVectors);
			GradientVector grad1 = searchGradientVectors(new Point(gradX + 1, gradY), gradientVectors);
			GradientVector grad2 = searchGradientVectors(new Point(gradX, gradY + 1), gradientVectors);
			GradientVector grad3 = searchGradientVectors(new Point(gradX + 1, gradY + 1), gradientVectors);
			
			// calculate the distances from the top-left gradient
			double xDist = xs[i] / (double)this.scale - gradX;
			double yDist = ys[i] / (double)this.scale - gradY;
			
			// calculate the dot products
			double dot0 = xDist * grad0.dirX + yDist * grad0.dirY;
			double dot1 = (xDist - 1) * grad1.dirX + yDist * grad1.dirY;
			double dot2 = xDist * grad2.dirX + (yDist - 1) * grad2.dirY;
			double dot3 = (xDist - 1) * grad3.dirX + (yDist - 1) * grad3.dirY;
			
			// calculate the transformed distances
			double xTDist = 3 * Math.pow(xDist, 2) - 2 * Math.pow(xDist, 3);
			double yTDist = 3 * Math.pow(yDist, 2) - 2 * Math.pow(yDist, 3);
			
			// calculate the averages on the x axes
			double average02 = (1 - yTDist) * dot0 + yTDist * dot2;
			double average13 = (1 - yTDist) * dot1 + yTDist * dot3;
			
			// calculate the average on the y axis
			double average = (1 - xTDist) * average02 + xTDist * average13;
			
			// and that's, finally, after just a little bit of scaling and translation, our noise value
			noises[i] = average / 2 + 0.5;
		}
		
		return noises;
	}
	
	/**
	 * Searches for and returns a GradientVector in a GradientVectors list with a location as specified.
	 * @param location location of the vector to search for
	 * @param vectors the list of GradientVectors in search in
	 * @return the GradientVector with the specified location or null if not found
	 */
	private static GradientVector searchGradientVectors(Point location, List<GradientVector> vectors)
	{
		// iterate through the vectors list
		for (int i = 0, n = vectors.size(); i < n; i++)
		{
			GradientVector currentVector = vectors.get(i);
			if (location.x == currentVector.locX && location.y == currentVector.locY)
			{
				return currentVector;
			}
		}
		return null;
	}
	
	/**
	 * Returns the GradientVector corresponding to the specified location based on the hashArray.
	 * @param x x location of the vector
	 * @param y y location of the vector
	 * @return the GradientVector corresponding to (x, y)
	 */
	private GradientVector getGradientVector(int x, int y)
	{
		// input to the hash algorithm
		int i0 = x & 255;
		int i1 = y & 255;
		int i2 = ((x >> 8) & 255) ^ ((y >> 8) & 255);
		
		// output of the hash algorithm
		long out = 0;
		out = (byte) this.hash(this.hash(this.hash(i2) + i1) + i0) | out;
		out = (byte) this.hash(this.hash(this.hash(i2 + 1)	+ i1) + i0) << 8 | out;
		out = (byte) this.hash(this.hash(this.hash(i2) + i1 + 1) + i0) << 16 | out;
		out = (byte) this.hash(this.hash(this.hash(i2 + 1) + i1 + 1) + i0) << 24 | out;
		out = (byte) this.hash(this.hash(this.hash(i2) + i1) + i0 + 1) << 32 | out;
		out = (byte) this.hash(this.hash(this.hash(i2 + 1)	+ i1) + i0 + 1) << 40 | out;
		out = (byte) this.hash(this.hash(this.hash(i2) + i1 + 1) + i0 + 1) << 48 | out;
		out = (byte) this.hash(this.hash(this.hash(i2 + 1) + i1 + 1) + i0 + 1) << 56 | out;
		
		// create an angle for the gradient vector from the hashed bytes 
		double angle = out / (double)Long.MAX_VALUE * 2 * Math.PI;
		
		// create a gradient vector at the specified location with sine and cosine of the pseudo-random angle as its components
		GradientVector vector = new GradientVector(x, y, Math.sin(angle), Math.cos(angle));
		
		return vector;
	}
	
	/**
	 * Hashes the value by returning the valueth index of the hashArray.
	 * Modulo is applied to prevent accessing indices bigger than 255.
	 * @param value the value to be hashed
	 * @return hashed value
	 */
	private int hash(int value)
	{
		return this.hashArray[value % 256];
	}
	
	/**
	 * Represents a GradientVector - its location on the map and its value <-> direction
	 * 
	 * @author mimo31 
	 *
	 */
	private static class GradientVector
	{
		// x coordinate of the place where the vector lies
		final int locX;
		
		// y coordinate of the place where the vector lies
		final int locY;
		
		// x component of the vector
		final double dirX;
		
		// y component of the vector
		final double dirY;
		
		/**
		 * Creates a GradientVector.
		 * @param locX x location of the vector
		 * @param locY y location of the vector
		 * @param dirX x component of the vector
		 * @param dirY y component of the vector
		 */
		public GradientVector(int locX, int locY, double dirX, double dirY)
		{
			this.locX = locX;
			this.locY = locY;
			this.dirX = dirX;
			this.dirY = dirY;
		}
	}
}
