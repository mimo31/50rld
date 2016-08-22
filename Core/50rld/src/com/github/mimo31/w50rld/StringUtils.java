package com.github.mimo31.w50rld;

/**
 * Provided methods for String manipulation.
 * @author mimo31
 *
 */
public class StringUtils {

	/**
	 * Cuts off leading zeroes from a String. If there are no other characters, leaves the one last zero.
	 * @param input string to remove the zeroes from
	 * @return string with cut off zeroes
	 */
	public static String trimZeroes(String input)
	{
		int inputLength = input.length();
		int i = 0;
		
		// traverse the String until the current character is not zero or the last but one character is accessed
		while ((i + 1) < inputLength && input.charAt(i) == '0')
		{
			i++;
		}
		if (i != 0)
		{
			return input.substring(i, inputLength - 1);
		}
		return input;
	}
	
}
