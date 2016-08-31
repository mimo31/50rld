package com.github.mimo31.w50rld;

/**
 * An Interface for an Item that can be melted into a molten metal.
 * @author mimo31
 *
 */
public interface Meltable {

	int getVolume();
	
	Metal getMetal();
	
}
