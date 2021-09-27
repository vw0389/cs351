package edu.uwm.cs351.util;

import java.util.Comparator;

import edu.uwm.cs351.Painting;

/**
 * The class Value.
 * 
 * Return a negative integer if p1's value > p2's value,
 *  	  a positive integer if p1's value < p2's value,
 *  	  and zero if values are equal.
 */
public class ValueDescending implements Comparator<Painting> {

	/*
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Painting p1, Painting p2) {
		return p2.getValue() - p1.getValue();
		// Implement compare for two values.
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return "Value";}
	
	private static Comparator<Painting> instance = new ValueDescending();
	
	/**
	 * Gets a single instance of Value comparator.
	 * @return a single instance of Value comparator
	 */
	public static Comparator<Painting> getInstance() { return instance; }
}
