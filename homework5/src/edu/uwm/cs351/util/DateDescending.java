package edu.uwm.cs351.util;

import java.util.Comparator;

import edu.uwm.cs351.Painting;

/**
 * The class Date.
 * 
 * Return a negative integer if p1's date > p2's date,
 *  	  a positive integer if p1's date < p2's date,
 *  	  and zero if dates are equal.
 */
public class DateDescending implements Comparator<Painting> {

	/*
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Painting p1, Painting p2) {
		return p2.getYear() - p1.getYear();
		// Implement compare for two dates.
		
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() { return "AttendanceDescending";}
	
	private static Comparator<Painting> instance = new DateDescending();
	
	/**
	 * Gets a single instance of AttendanceDescending comparator.
	 * @return a single instance of AttendanceDescending comparator
	 */
	public static Comparator<Painting> getInstance() { return instance; }
}
