package edu.uwm.cs351;

import java.awt.Color;
import java.util.Comparator;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Raster;

// Victor Weinert Homework 08
// No help declared.

// Resources:
// The book
// https://en.wikipedia.org/wiki/Binary_search_tree
// http://pabst.cs.uwm.edu/classes/cs351/homework/homework8.html
// http://pabst.cs.uwm.edu/classes/cs351/handout/navigating-trees.html
// referenced lab
// java docs @ https://docs.oracle.com/javase/8/docs/

/**
 * List of Rasters in a set-like ADT
 * The Rasters are stored in a binary search tree
 * The tree is organized using one of two comparators
 */
public class RasterSet{
	
	private static class Node {
		Raster raster;
		Node left, right;
		Node (Raster r) { raster = r; }
	}
	
	private Node root;
	private int manyNodes;
	private Comparator<Raster> comp;
	
	private static final Comparator<Pixel> P_COMP = new Comparator<Pixel>() {
		public int compare(Pixel p1, Pixel p2) {
			Color c1 = p1.color();
			Color c2 = p2.color();
			int c = c1.getRed() - c2.getRed();
			if (c != 0) return c;
			c = c1.getGreen() - c2.getGreen();
			if (c != 0) return c;
			c = c1.getBlue() - c2.getBlue();
			if (c != 0) return c;
			c = c1.getAlpha() - c2.getAlpha();
			return c;
		}
	};
	
	private static final Comparator<Raster> BY_X = new Comparator<Raster> () {
		public int compare(Raster r1, Raster r2) {
			int c = r1.x() - r2.x();
			if (c != 0) return c;
			c = r1.y() - r2.y();
			if (c != 0) return c;
			for(int i=0; i<r1.x(); i++)
				for(int j=0; j<r1.y(); j++) {
					c = P_COMP.compare(r1.getPixel(i, j), (r2.getPixel(i, j)));
					if (c != 0) return c;
				}
			return 0;
		}
	};
	
	private static final Comparator<Raster> BY_Y = new Comparator<Raster> () {
		public int compare(Raster r1, Raster r2) {
			int c = r1.y() - r2.y();
			if (c != 0) return c;
			c = r1.x() - r2.x();
			if (c != 0) return c;
			for(int i=0; i<r1.x(); i++)
				for(int j=0; j<r1.y(); j++) {
					c = P_COMP.compare(r1.getPixel(i, j), (r2.getPixel(i, j)));
					if (c != 0) return c;
				}
			return 0;
			// Similar to the above comparator.
			// The raster with a bigger y dimension is bigger
			// If equal, go by x dimension
			// If equal, compare pixel by pixel
		}
	};
	
	/**
	 * Check the invariant.  
	 * Returns false if any problem is found.  It uses
	 * {@link #report(String)} to report any problem.
	 * @return whether invariant is currently true.
	 */
	private boolean wellFormed() {
		int n = checkInRange(root, null, null);
		if (n < 0) return false; 
		if (n != manyNodes) return report("manyNodes is " + manyNodes + " but should be " + n);
		return true;
	}
	
	private static boolean doReport = true;
	
	/**
	 * Used to report an error found when checking the invariant.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private boolean report(String error) {
		if (doReport) System.out.println("Invariant error found: " + error);
		return false;
	}

	private int reportNeg(String error) {
		report(error);
		return -1;
	}
	
	/**
	 * Check that all rasters in the subtree are in the parameter resolution range,
	 * and none of them are null.
	 * Report any errors.  If there is an error return a negative number.
	 * (Write "return _reportNeg(...);" when detecting a problem.)
	 * Otherwise return the number of nodes in the subtree.
	 * Note that the range should be updated when doing recursive calls.
	 * 
	 * @param n the root of the subtree to check
	 * @param lo all dates in the subtree rooted at n must be later than this
	 * @param hi all dates in the subtree rooted at n must be before this
	 * @return number of nodes in the subtree
	 */
	private int checkInRange(Node n, Raster lo, Raster hi)
	{
		// 1. n's raster must not be null.
		// 2. n's raster must fall within the given bounds (according to the current comparator).
		// 3. recursively check the children (with updated bounds)
		// 4. return the size of the subtree rooted at n,
		//     or -1 if there is a problem (either here, or already detected further down the tree).
		//     Use reportNeg where appropriate.
		if (n == null) return 0;
		if (n.raster == null) return reportNeg("1. null raster");
		if ( lo != null) {
			if (comp.compare(lo, n.raster) >= 0) {
				return reportNeg("2. raster must fall within the given bounds (according to the current comparator).");
			}
		}
		if (hi != null) {
			if (comp.compare(n.raster, hi) >= 0) {
				return reportNeg("2. raster must fall within the given bounds (according to the current comparator).");
			}
		}
		int left = checkInRange(n.left, lo, n.raster);
		if ( left < 0) return reportNeg("");
		int right = checkInRange(n.right, n.raster, hi);
		if ( right < 0) return reportNeg("");
		return 1 + left + right;
	}
	
	/**
	 * Create an empty tree of rasters.
	 * By default, the set will be in x-first mode.
	 */
	public RasterSet() {
		root = null;
		manyNodes = 0;
		comp = BY_X;
		// Implement the constructor (BEFORE the assertion!)
		assert wellFormed() : "invariant false at end of constructor";
	}

	/** Gets the size of this tree.
	 * @return the count of nodes in this tree
	 */
	public int size() {
		assert wellFormed() : "invariant false at start of size()";
		return manyNodes;
	}

	/** Checks if the set is empty.
	 * @return true iff the set is empty
	 */	
	public boolean isEmpty() {
		assert wellFormed() : "invariant false at start of isEmpty()";
		return (manyNodes == 0);
	}
	
	/** Clear the set
	 */	
	public void clear() {
		assert wellFormed() : "invariant false at start of clear()";
		root = null;
		manyNodes = 0;
	}
	/**
	 * Check if the set contains some raster
	 * @param r raster to find
	 * @return true if the raster exists in the RasterSet
	 * @throws IllegalArgumentException if the raster is null
	 */
	public boolean contains(Raster r) {
		assert wellFormed() : "invariant false at start of contains()";
		if (r == null) throw new IllegalArgumentException("nulls are not allowed");
		boolean found = false;
		Node pointy = root;
		while (!found) {
			if (pointy == null) return found;
			int value  = comp.compare(pointy.raster, r);
			if (value == 0) {
				found = true;
			} else if (value > 0) {
				pointy = pointy.left;
			} else {
				pointy = pointy.right;
			}
		}
		return found;
		//   You can assume compare returning 0 means equality
	}
	
	
	/**
	 * Add a new raster to the RasterSet unless the set
	 * already contains an equal raster, in which case return false.
	 * @param a raster to add (must not be null)
	 * @return true if the raster was added, false otherwise
	 * @throws IllegalArgumentException if the raster is null
	 */
	public boolean add(Raster a) {
		assert wellFormed() : "invariant false at start of add()";
		
		if (a == null) throw new IllegalArgumentException("nulls are not allowed");
		
		boolean added = false;
		if (root == null) {
			root = new Node(a);
			manyNodes++;
			added = true;
		}
		
		Node pointy = root;
		
		while (!added) {
			int value = this.comp.compare(pointy.raster, a);
			if (value == 0) return false;
			if (pointy.left != null && value > 0) {
				pointy = pointy.left;
			} else if (pointy.right != null && value < 0) {
				pointy = pointy.right;
			} else if (value > 0) {
				pointy.left = new Node(a);
				manyNodes++;
				added = true;
			} else {
				pointy.right = new Node(a);
				manyNodes++;
				added = true;
			}
		}
		
		assert wellFormed() : "invariant false at end of add()";
		return added;
	}

	
	// For the next two methods, we assume that pictures with bigger x also might have bigger y.
	// So we definitely want to avoid adding in sorted order when resorting!
	// To accomplish this, make sure you add a parent node before adding its children.
	// Use the resort() helper method to avoid too much code duplication!
	
	/**
	 * change the tree to be ordered first by y dimension
	 * @return false if already in y-first mode
	 */
	public boolean yFirst(){
		assert wellFormed() : "invariant false at the start of yFirst()";
		//use private helper methods to prevent duplicate code
		if (this.comp.equals(BY_Y)) return false;
		this.comp = BY_Y;
		this.resort();
		assert wellFormed():  "invariant false at the end of yFirst()"; //public method that modifies data structure, hence needs to assert valid state of ADT
		return true;
	}
	
	/**
	 * change the tree to be ordered first by x dimension
	 * @return false if already in x-first mode
	 */
	public boolean xFirst(){
		assert wellFormed() : "invariant false at the start of xFirst()";
		//use private helper methods to prevent duplicate code
		if (this.comp.equals(BY_X)) return false;
		this.comp = BY_X;
		this.resort();
		assert wellFormed() : "invariant false at the end of xFirst()"; //public method that modifies data structure, hence needs to assert valid state of ADT
		return true;
	}
	

	private void resort() {
		Node oldRoot = root;
		root = null;
		manyNodes = 0;
		treeTraverser(oldRoot);
		//  Implement this private helper method.
		//  It should resort the elements in the tree
		//  according to the current comparator.
		//  Hint: write a recursive helper method
		//  that will add all the elements into
		//  the new tree recursively.
	}
	
	private void treeTraverser(Node n) {
		if (n == null) return;
		this.add(n.raster);
		treeTraverser(n.left);
		treeTraverser(n.right);
	}

	
	
	/**
	 * Method that returns an array holding all the elements in the given x range
	 * A raster is in the range if its x dimension as at least lo and at most hi
	 * The array's length is equal to the number of rasters in the range
	 * @return the array holding the rasters in the given x range
	 */
	public Raster[] xRange(int lo, int hi) {
		assert wellFormed() : "invariant false at the start of xRange()";

		Raster[] result = new  Raster[manyNodes];
		if (manyNodes == 0) {
			result = new Raster[0];
			return result;
		}
		// Implement this method, using copyXRange
		copyXRange(result, 0, root, lo, hi);
		result = trimFromNull(result);
		return result;
	}
	
	/**
	 * Method that returns an array holding all the elements in the given y range
	 * A raster is in the range if its y dimension as at least lo and at most hi
	 * The array's length is equal to the number of rasters in the range
	 * @return the array holding the rasters in the given y range
	 */
	public Raster[] yRange(int lo, int hi) {
		assert wellFormed() : "invariant false at the start of xRange()";
		Raster[] result = new  Raster[manyNodes];
		if (manyNodes == 0) {
			result = new Raster[0];
			return result;
		}
		// Implement this method, using copyYRange
		copyYRange(result, 0, root, lo, hi);
		result = trimFromNull(result);
		return result;
	}
	
	// Private helper method you may use to trim down your array
	// This only works if all valid data appears BEFORE any null data
	private Raster[] trimFromNull(Raster[] array) {
		int size = 0;
		while(size < array.length && array[size] != null)
			size++;
		Raster[] result = new Raster[size];
		for(int i=0; i<size; i++)
			result[i] = array[i];
		return result;
	}
	
	//Recursive helper method to copy the in-range elements of a subtree into an array starting at a certain index
	// array: the array to copy into
	// index: the index currently being copied into
	// n: the node rooting the subtree of interest
	// lo: the inclusive lower bound for x
	// hi: the inclusive upper bound for x
	// returns the index to be used as recursion continues
	private int copyXRange(Raster[] array, int index, Node n, int lo, int hi) {
		if (n == null)
			return index;
		if (this.comp == BY_Y) {
			index = copyXRange(array, index, n.left, lo, hi);
			index = copyXRange(array, index, n.right, lo, hi);
			if (n.raster.x() >= lo && n.raster.x() <= hi) {
				array[index] = n.raster;
				index++;
			}
		} else {
			if (n.raster.x() >= lo && n.raster.x() <= hi) {
				array[index] = n.raster;
				index++;
				index = copyXRange(array, index, n.left, lo, hi);
				index = copyXRange(array, index, n.right, lo, hi);
			} else if (n.raster.x() < lo) {
				index = copyXRange(array, index, n.right, lo, hi);
			} else if (n.raster.x() > hi) {
				index = copyXRange(array, index, n.left, lo, hi);
			}
		}
			
		
		//  implement this recursive method.
		//   If the tree is currently x-first,
		//   you should avoid certain subtrees.
		//   Otherwise, check every node.
		return index;
	}
	
	//Recursive helper method to copy the in-range elements of a subtree into an array starting at a certain index
	// array: the array to copy into
	// index: the index currently being copied into
	// n: the node rooting the subtree of interest
	// lo: the inclusive lower bound for y
	// hi: the inclusive upper bound for y
	// returns the index to be used as recursion continues
	private int copyYRange(Raster[] array, int index, Node n, int lo, int hi) {
		if (n == null)
			return index;
		if (this.comp == BY_X) {
			index = copyYRange(array, index, n.left, lo, hi);
			index = copyYRange(array, index, n.right, lo, hi);
			if (n.raster.y() >= lo && n.raster.y() <= hi) {
				array[index] = n.raster;
				index++;
				
			}
		} else {
			if (n.raster.y() >= lo && n.raster.y() <= hi) {
				array[index] = n.raster;
				index++;
				index = copyYRange(array, index, n.left, lo, hi);
				index = copyYRange(array, index, n.right, lo, hi);
			}
			if (n.raster.y() < lo) {
				index = copyYRange(array, index, n.right, lo, hi);
			} else if (n.raster.y() > hi) {
				index = copyYRange(array, index, n.left, lo, hi);
			}
		}

		// implement this recursive method.
		// If the tree is currently y-first,
		// you should avoid certain subtrees.
		// Otherwise, check every node.
		return index;
	}
	
	

	
	
	public abstract static class TestInternals extends LockedTestCase {

		RasterSet r;
		
		@Override
		protected void setUp() throws Exception {
			super.setUp();
			r = new RasterSet();
		}

		public void testCheckInRange() {
			Node n1 = new Node(new Raster(1,2));
			Node n2 = new Node(new Raster(2,2));
			Node n3 = new Node(new Raster(3,2));
			Node n4 = new Node(new Raster(4,2));
			Node n5 = new Node(new Raster(5,2));
			Node n6 = new Node(new Raster(6,2));
			
			n3.left = n2;
			n2.left = n1;
			n3.right = n5;
			n5.left = n4;
			
			//in x-first mode by default
			//checkInRange returns -1 if there is a problem in the subtree
			//  or the number of nodes in the subtree if there is no problem
			
			n5.raster = null;
			assertEquals(Ti(655969864), r.checkInRange(n3, null, null));
			n5.raster = new Raster(5,2);
			assertEquals(Ti(293619758), r.checkInRange(n3, null, null));
			
			n5.left=n6;
			n6.left=n4;
			assertEquals(Ti(1364295889), r.checkInRange(n3, null, null));
			n6.left=null;
			n5.right=n6;
			n5.left=n4;
			assertEquals(Ti(1647263813), r.checkInRange(n3, null, null));
			
			Node n2a = new Node(new Raster(3,1));
			n1.left=n2a;
			assertEquals(Ti(407023338), r.checkInRange(n3, null, null));
			n1.left=null;
			n2.right=n2a;
			assertEquals(Ti(538984530), r.checkInRange(n3, null, null));
		}
		
		public void testCheckInRange2() {
			Node n1 = new Node(new Raster(1,1));
			Node n2 = new Node(new Raster(1,1));
			Node n3 = new Node(new Raster(1,1));
			
			n1.left = n2;
			assertEquals(Ti(1682293725), r.checkInRange(n1, null, null));
			n1.left=null;
			n1.right = n2;
			assertEquals(Ti(786243852), r.checkInRange(n1, null, null));
			n1.left=n3;
			assertEquals(Ti(962099047), r.checkInRange(n1, null, null));
			n1.left = n1.right = null;
			assertEquals(Ti(1937740294), r.checkInRange(n1, null, null));
		}
		
		
		public void testCheckInRange3() {
			Node n1 = new Node(new Raster(1,2));
			Node n2 = new Node(new Raster(2,2));
			Node n3 = new Node(new Raster(3,2));
			Node n4 = new Node(new Raster(4,2));
			Node n5 = new Node(new Raster(5,2));
			Node n6 = new Node(new Raster(1,3));
			
			r.comp = BY_Y;
			
			n1.right = n2;
			n2.right = n3;
			n3.right = n4;
			n4.right = n5;
			n5.left=n6;
			assertEquals(Ti(975601647), r.checkInRange(n1, null, null));
			n5.left=null;
			n1.left=n6;
			assertEquals(Ti(1626558804), r.checkInRange(n1, null, null));
			n1.left=n2;
			n1.right=null;
			assertEquals(-1, r.checkInRange(n1, null, null));
			n2.right=null;
			n1.left=null;
			n1.right=n3;
			n3.left=n2;
			assertEquals(5, r.checkInRange(n1, null, null));
		}
		
		public void testCheckInRange4() {
			Node black = new Node(new Raster(2,2));
			Node red = new Node(new Raster(2,2));
			Node green = new Node(new Raster(2,2));
			Node blue = new Node(new Raster(2,2));
			Node alpha = new Node(new Raster(2,2));
			black.raster.addPixel(new Pixel(0,0,new Color(0,0,0,0)));
			red.raster.addPixel(new Pixel(0,0,new Color(255,0,0,0)));
			green.raster.addPixel(new Pixel(0,0,new Color(0,255,0,0)));
			blue.raster.addPixel(new Pixel(0,0,new Color(0,0,255,0)));
			alpha.raster.addPixel(new Pixel(0,0,new Color(0,0,0,255)));
			
			blue.right = red;
			red.left = green;
			blue.left = alpha;
			alpha.left = black;
			assertEquals(Ti(1774745317), r.checkInRange(blue, null, null));
			
			red.left = null;
			red.right = green;
			assertEquals(Ti(550542902), r.checkInRange(blue, null, null));
			
			red.right = null;
			alpha.left = null;
			alpha.right = black;
			assertEquals(-1, r.checkInRange(blue, null, null));
			
			alpha.right = null;
			blue.left = red;
			blue.right = alpha;
			assertEquals(-1, r.checkInRange(blue, null, null));
			
			alpha.right = red;
			assertEquals(2, r.checkInRange(alpha, null, null));
		}
		
		public void testCheckInRange5 () {
			r.comp = BY_X;
			
			Node n00 = new Node(new Raster(2,2));
			Node n11 = new Node(new Raster(2,2));
			n00.raster.addPixel(new Pixel(0,0,new Color(0,0,0,0)));
			n00.raster.addPixel(new Pixel(0,1,new Color(255,255,255,0)));
			n00.raster.addPixel(new Pixel(1,0,new Color(255,255,255,0)));
			n00.raster.addPixel(new Pixel(1,1,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(0,0,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(0,1,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(1,0,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(1,1,new Color(0,0,0,0)));
			
			n00.right = n11;
			assertEquals(Ti(2016345343), r.checkInRange(n00, null, null));
			
			n00.right = null;
			n11.right = n00;
			assertEquals(Ti(1135182922), r.checkInRange(n11, null, null));
			
			n00.raster.addPixel(new Pixel(0,0,new Color(255,255,255,0)));
			assertEquals(2, r.checkInRange(n11, null, null));
		}
		
		public void testCheckInRange6 () {
			r.comp = BY_Y;
			
			Node n00 = new Node(new Raster(2,2));
			Node n11 = new Node(new Raster(2,2));
			n00.raster.addPixel(new Pixel(0,0,new Color(0,0,0,0)));
			n00.raster.addPixel(new Pixel(0,1,new Color(255,255,255,0)));
			n00.raster.addPixel(new Pixel(1,0,new Color(255,255,255,0)));
			n00.raster.addPixel(new Pixel(1,1,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(0,0,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(0,1,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(1,0,new Color(255,255,255,0)));
			n11.raster.addPixel(new Pixel(1,1,new Color(0,0,0,0)));
			
			n00.right = n11;
			assertEquals(2, r.checkInRange(n00, null, null));
			
			n00.right = null;
			n11.right = n00;
			assertEquals(-1, r.checkInRange(n11, null, null));
			
			n00.raster.addPixel(new Pixel(0,0,new Color(255,255,255,0)));
			assertEquals(2, r.checkInRange(n11, null, null));
			
		}
		
		public void testResort() {
			Node n16 = new Node(new Raster(1,6));
			Node n25 = new Node(new Raster(2,5));
			Node n34 = new Node(new Raster(3,4));
			Node n43 = new Node(new Raster(4,3));
			Node n52 = new Node(new Raster(5,2));
			Node n61 = new Node(new Raster(6,1));
			RasterSet r = new RasterSet();
			r.manyNodes = 6;
			r.root = n34;
			
			
			n34.right = n43;
			n34.left = n25;
			n25.left = n16;
			n34.right = n43;
			n43.right = n52;
			n52.right = n61;
			
			assertTrue(r.wellFormed());
			
			r.comp = BY_Y;
			assertFalse(r.wellFormed());

			r.resort();
			assertTrue(r.wellFormed());
			
			r.comp = BY_X;
			assertFalse(r.wellFormed());
			
			r.resort();
			assertTrue(r.wellFormed());
		}
		
		public void testEmpty() {
			r.manyNodes = 1;
			assertFalse(r.wellFormed());
			r.manyNodes = 0;
			assertTrue(r.wellFormed());
			assertTrue(r.comp == BY_X);
		}
		
		public void test1() {
			r.root = new Node(new Raster(1,1));
			assertFalse(r.wellFormed());
			r.manyNodes = 1;
			assertTrue(r.wellFormed());
			r.manyNodes = 2;
			assertFalse(r.wellFormed());
			r.manyNodes = 1;
			r.root.raster = null;
			assertFalse(r.wellFormed());
		}
		
		public void test2() {
			Node n1 = new Node(new Raster(1,1));
			Node n1a = new Node(new Raster(1,1));
			Node n2 = new Node(new Raster(1,2));
			r.manyNodes = 2;
			assertFalse(r.wellFormed());
			r.root = n1;
			assertFalse(r.wellFormed());
			n1.right = n1a;
			assertEquals(false, r.wellFormed());
			n1.right = n1;
			assertEquals(false, r.wellFormed());
			n1.right = n2;
			assertEquals(true, r.wellFormed());
			
			n2.left = n1;
			assertFalse(r.wellFormed());
			r.root = n2;
			assertFalse(r.wellFormed());
			n1.right = null;
			assertTrue(r.wellFormed());
			
			n2.right = n2;
			assertFalse(r.wellFormed());
		}
		
		public void test3() {
			Node n1 = new Node(new Raster(1,1));
			Node n2 = new Node(new Raster(1,2));
			Node n3 = new Node(new Raster(1,3));
			n2.left = n1;
			n2.right = n3;
			r.root = n2;
			
			r.manyNodes = 3;			
			assertEquals(true, r.wellFormed());
			r.manyNodes = 1;
			assertFalse(r.wellFormed());
		}
		
		public void test4() {
			Node n1 = new Node(new Raster(1,2));
			Node n2 = new Node(new Raster(2,2));
			Node n3 = new Node(new Raster(3,2));
			Node n4 = new Node(new Raster(4,2));
			Node n5 = new Node(new Raster(5,2));
			Node n6 = new Node(new Raster(6,2));
			Node n7 = new Node(new Raster(7,2));
			Node n8 = new Node(new Raster(8,2));
			Node n9 = new Node(new Raster(9,2));
			
			r.root = n5;
			n5.left = n3;
			n3.right = n4;
			n3.left = n1;
			n1.right = n2;
			n5.right = n8;
			n8.left = n7;
			n7.left = n6;
			n8.right = n9;
			r.manyNodes = 9;
			assertEquals(true, r.wellFormed());
			
			r.manyNodes = 10;
			assertFalse(r.wellFormed());
			
			n1.left = new Node(new Raster(1,5));
			assertFalse(r.wellFormed());
			n1.left = new Node(new Raster(1,0));
			assertTrue(r.wellFormed());
			n1.left = null;
			
			n2.left = new Node(new Raster(0,5));
			assertFalse(r.wellFormed());
			n2.left = null;
			n2.right = new Node(new Raster(3,5));
			assertFalse(r.wellFormed());
			n2.right = new Node(new Raster(3,1));
			assertTrue(r.wellFormed());
			n2.right = null;
			
			--r.manyNodes;
			assertTrue(r.wellFormed());
			++r.manyNodes;
			
			n4.left = new Node(new Raster(2,5));
			assertFalse(r.wellFormed());
			n4.left = null;
			n4.right = new Node(new Raster(5,5));
			assertFalse(r.wellFormed());
			n4.right = null;
			
			n6.left = new Node(new Raster(4,5));
			assertFalse(r.wellFormed());
			n6.left = null;
			n6.right = new Node(new Raster(7,5));
			assertFalse(r.wellFormed());
			n6.right = null;
			
			n7.right = new Node(new Raster(8,5));
			assertFalse(r.wellFormed());
			n7.right = null;
			
			--r.manyNodes;
			assertTrue(r.wellFormed());
			++r.manyNodes;
			
			n9.left = new Node(new Raster(7,5));
			assertFalse(r.wellFormed());
			n9.left = null;
			n9.right = new Node(new Raster(17,1));
			assertTrue(r.wellFormed());
			n9.right = null;
			
			--r.manyNodes;
			assertTrue(r.wellFormed());			
		}
	}

}
