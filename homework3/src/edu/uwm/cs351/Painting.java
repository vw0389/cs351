package edu.uwm.cs351;

/**
 * A wrapper class for Raster that includes a description and a monetary value
 * 
 *
 */
public class Painting {
	private final Raster raster;
	private String description;
	private final int value;
	
	public Painting(Raster raster, String description, int value)
	{
		this.raster = raster;
		this.description = description;
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public Raster getRaster()
	{
		return raster;
	}
	/**
	 * Describes the painting in a readable format
	 */
	public String description()
	{
		return description;
	}

	@Override
	public String toString() {
		return description();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		Painting p = (Painting) obj;
		if(!(this.raster.equals(p.raster)))
		{
			return false;
		}
		if(!(this.value == p.value))
		{
			return false;
		}
		if(!(this.description.equals(p.description)))
		{
			return false;
		}
		return true;
	}
	

	
	
}
