package edu.uwm.cs351;
// Victor Weinert homework 1 cs351/751
// No help declared with the exception of links below and documentation usage, attained and viewed via zeal.
// I do school work in VMs, which is why the author and committer are "user <user@localhost>"
// Looked up several concepts I had forgotten over time, namely constructor chaining, instanceof operator usage,and review of RGB color model represention
// https://stackoverflow.com/questions/17640560/java-constructor-chaining
// https://stackoverflow.com/questions/7526817/use-of-instanceof-in-java#7526896
// https://en.wikipedia.org/wiki/SRGB
// https://en.wikipedia.org/wiki/RGB
// https://zealdocs.org/
// https://docs.oracle.com/javase/8/docs/
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;


/**
 * A single pixel
 * <dl>
 * <dt>loc<dd> an immutable field holding a Point representing the Pixel's location, cannot be null
 * <dt>color<dd> the pixel's color, cannot be null
 * </dl>
 */
public class Pixel {
	private Color color;
	private final Point loc;
	private static final Color DEFAULT_COLOR = Color.WHITE;
	
	
	// Test the constructors for the Pixel objects
	// make sure you throw IllegalArgumentExceptions where appropriate
	// if no Color is given, use the default color
	/**
	 * Create a pixel by specifying two coordinates
	 * @param x
	 * @param y
	 */
	public Pixel(int x, int y) {
		
		this(new Point(x,y), DEFAULT_COLOR);
		
	}
	
	/**
	 * Create a pixel by specifying two coordinates and a color
	 * @param x
	 * @param y
	 * @param color
	 */
	public Pixel(int x, int y, Color color) {
		
		this(new Point(x,y), color);
	}
	
	/**
	 * Create a pixel by specifying a point
	 * @param loc
	 */
	public Pixel(Point loc) {
		
		this(loc, DEFAULT_COLOR);
		
	}
	
	//Recommendation: use the following constructor in the other constructors
	/**
	 * Create a pixel by specifying a point and a color
	 * @param loc
	 * @param color
	 */
	public Pixel(Point loc, Color color) {
		if (color == null || loc == null) {
			throw new IllegalArgumentException();
		} 
		
		this.loc = loc;
		this.color = color;
	}
	
	/// Overrides
	// no need to give a documentation comment if overridden documentation still is valid.
	
	@Override
	public boolean equals(Object o) {
		//in current form for clarity, hashCode() and toString() are somewhat redundant
		if (o == null) {
			return false;
		} else if (!( o instanceof Pixel )) {
			return false;
		} else if (this.hashCode() != o.hashCode()) {
			return false;
		} else if (this.toString().equals(o.toString())) {
			return true;
		} else { 
			return false;
		}
		
	}
	
	@Override
	public int hashCode() {
//		int lesser =  loc.y ^ (loc.x << 8);
//		return lesser ^ (red << 26) ^ (green << 21) ^ (blue << 16);
		return this.toString().hashCode();
		// This is very lazy and coincidentally doesn't work
		// Return some combination of x and y that distinguishes similar coordinates
	}
	

	@Override
	public String toString() {
		return  "<" + Integer.toString(this.loc.x) + "," + Integer.toString(this.loc.y) + "," + this.color.toString() + ">";
		
		// Return a string of the form <3,2,red>,
		// assuming "red" might be the return from a Color's toString method
	}
	
	
	/// three simple methods
	/** Return the Point location, but a copy, so it can't be changed
	 * @return location of the pixel
	 * 
	 */
	public Point loc()
	{
		return new Point(this.loc.x,this.loc.y);
		
	}
	
	/** Return the Java Color value of the pixel
	 * Color is immutable, so we don't have to worry about it being changed
	 * @return the color value
	 */
	public Color color()
	{
		return this.color;
		
	}
	
	/** Set the Java Color value of the pixel
	 * Don't allow setting to null
	 * @param color
	 */
	public void setColor(Color color)
	{
		if (color == null ) {
			throw new IllegalArgumentException();
		}
		this.color = color;
	}


	/**
	 * Invert the r,g,b values of the pixel
	 */
	public void invert()
	{
		this.color = new Color(255 - this.color.getRed(),255 - this.color.getGreen(),255 - this.color.getBlue());
		
		//You can invert a color by subtracting each of its red, green, and blue components from 255.
	}

	
	/**
	 * Create a polygon (for rendering in AWT) for the pixel based on the loc field
	 * @param width of the pixel
	 * @return polygon for the pixel
	 */
	public Polygon toPolygon(int width) {
		Point[] ps = {
				new Point(loc.x*width,loc.y*width),
				new Point(loc.x*width + width,loc.y*width),
				new Point(loc.x*width + width,loc.y*width + width),
				new Point(loc.x*width,loc.y*width + width)			
		};
		Polygon result = new Polygon();
		for (Point p : ps)
			result.addPoint(p.x,p.y);
		
		return result;
	}
	
	/**
	 * Render the tile in a graphics context.
	 * fill the Pixel with its color
	 * @param g context to use.
	 */
	public void draw(Graphics g, int width) {
		Polygon square = toPolygon(width);
		g.setColor(color);
		g.fillPolygon(square);
	}
	
}
