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
import java.awt.Graphics;
import java.util.Arrays;

public class Raster {
	private Pixel[][] pixels;
	private final int x;
	private final int y;
	
	// Construct an x by y raster
	// It should be filled with white (default) pixels
	public Raster(int x, int y) {
		
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
		pixels = new Pixel[x][y];
		if (this.x == 0 || this.y == 0) {

		} else {
			for (int i = 0; i < x; i++) {
				for (int j = 0; j < y; j++) {
					pixels[i][j] = new Pixel(i, j);
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		//deephash() or comparison of each element of these arrays could be done
		//deephash() does not account for x and y dimensions, so a Raster with dimensions (0,0), (0,1) and (1,0) were being computed as equal, when they are not.
		//this is why the raster.toString() method is utilized
		if (o == null ) {
			return false;
		} else if (!( o instanceof Raster )) {
			return false;
		}  else  if (!(this.toString().equals(o.toString()))){
			return false;
		} else {
			return true;
		}
		
	}
	
	@Override
	public String toString() {
		
		//return a String representation of the raster
		String rs;
		
		rs = x + " by " + y + " raster:";
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				rs = rs + " " + pixels[i][j].toString();
			}
		}
		return rs;
	}
	
	@Override
	public int hashCode() {
		return Arrays.deepHashCode(pixels);
	}
	
	/** Add a Pixel to the raster
	 * Don't allow adding a null
	 * @param p
	 */
	public boolean addPixel(Pixel p) {
		if (p == null) {
			throw new IllegalArgumentException();
		}
		// px/py variables are sort of silly, 
		// however they could be useful in a cpu constrained environment, 
		// due to only being accessed once, instead of a max of 5 times, if the pixel is added
		int px = p.loc().x;
		int py = p.loc().y;
		
		if (px < 0 || py < 0) {
			throw new IllegalArgumentException();
		}else if (px >= this.x || py >= this.y) {
			throw new IllegalArgumentException();
		} else if (this.pixels[px][py].hashCode() == p.hashCode()) {
			return false;
		} else {
		
			this.pixels[px][py] = p;
			return true;
		}
		
		// add the pixel to the raster at the correct location
		//Throw IllegalArgumentException if necessary
	}
	
	/** Get a pixel from the raster
	 * @return the pixel at x,y
	 */
	public Pixel getPixel(int x, int y) {
		if (x >= this.x || y >= this.y || x < 0 || y < 0) {
			throw new IllegalArgumentException();
		} else {
			return this.pixels[x][y];
		}
		
		//Throw IllegalArgumentException if necessary
	}
	
	/** Draw the raster using a graphics context and a width for pixels
	 * @param g
	 * @param width
	 */
	public void draw(Graphics g, int width) {
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				pixels[i][j].draw(g, width);;
			}
		}
		// have all the pixels draw themselves
	}
}
