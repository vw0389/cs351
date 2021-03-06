package edu.uwm.cs351;

import edu.uwm.cs351.util.Profile;
import edu.uwm.cs351.util.ProfileLink;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;
import snapshot.Snapshot;
//Victor Weinert HW14 
//no help declared

//Resources used:
//book
//https://en.wikipedia.org/wiki/Binary_heap
//https://docs.oracle.com/javase/8/docs/api/

public class PriorityQueue {
	/**
	 * An priority queue using an array-based ternary tree heap
	 */
	private final int INITIAL_CAPACITY = 7;
	private ProfileLink[] data;
	private int manyItems;
	private final Comparator<ProfileLink> COMP = new Comparator<ProfileLink>() {
		public int compare(ProfileLink a, ProfileLink b) {return a.cost - b.cost;}
	};


	private static boolean doReport = true;
	private boolean report(String message) {
		if (doReport) System.err.println("Invariant error: " + message);
		return false;
	}

	protected PriorityQueue(boolean ignored) {} // don't change: used by invariant checker
	
	
	private boolean wellFormed() {
		// Check invariant
		// 1. data array can't be null
		// 2. manyItems must be between 0 and data's length
		// 3. no null elements
		// 4. all children are larger than their parents
		if (this.data == null) {
			return report("1");
		}
		if (manyItems < 0 || manyItems > data.length) {
			return report("2");
		}
		for (int i = 0; i < manyItems; i++) {
			if (data[i] == null) {
				return report("3");
			}
			if (!checkChildren(i)) {
				return report("4");
			}
			
		}
		return true;
	}
	// optional helper method to check the children of a given node
	//returns true if children are out of manyItems bound or all are larger than value @ data[i]
	private boolean checkChildren(int i) {
		int childLeft,childMid, childRight,compoutput;
		childLeft = calcLeftChild(i);
		childMid = childLeft + 1;
		childRight = childMid + 1;
		if (childLeft < manyItems && data[childLeft] != null) {
			compoutput = this.COMP.compare(data[i], data[childLeft]);
			if (compoutput > 0) return false;
		}
		if (childRight < manyItems && data[childMid] != null) {
			compoutput = this.COMP.compare(data[i], data[childMid]);
			if (compoutput > 0) return false;
		}
		if (childRight < manyItems && data[childRight] != null) {
			compoutput = this.COMP.compare(data[i], data[childRight]);
			if (compoutput > 0) return false;
		}
		return true;
	}
	/**
	 * Create a new PriorityQueue
	 */
	public PriorityQueue() {
		data = new ProfileLink[INITIAL_CAPACITY];
		assert wellFormed() : "invariant broken at end of constructor";
	}
	
	/**
	 * Add a ProfileLink to the PriorityQueue
	 * @param l the ProfileLink to be added
	 * @return true
	 * @throws IllegalArgumentException for null ProfileLink
	 */
	public boolean add(ProfileLink l) {
		assert this.wellFormed();
		if ( l == null ) {
			throw new IllegalArgumentException();
		} else {
			ensureCapacity(manyItems+1);
			data[manyItems] = l;
			bubbleUp(manyItems);
			manyItems++;
		}
		
		assert this.wellFormed();
		return true;
		// add a ProfileLink to the end of the array, and then bubble it up
	}
	/**
	 * Remove and return the smallest ProfileLink from the PriorityQueue
	 * @return the smallest ProfileLink, now removed
	 * @throws NoSuchElementException if empty
	 */
	public ProfileLink remove() {
		assert this.wellFormed();
		ProfileLink stuff;
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		} else if (manyItems == 1) {
			manyItems--;
			stuff = data[0];
		} else {
			stuff = data[0];
			data[0] = data[manyItems-1];
			manyItems--;
			bubbleDown(0);
		}
		
		assert this.wellFormed();
		return stuff;
		//    remove and return the first Profile Link
		//    replace it with the last ProfileLink
		//    and then bubble it down
	}
	
	/**
	 * Return the size of the PriorityQueue
	 * @return the number of elements
	 */
	public int size() {
		assert this.wellFormed();
		return manyItems;
		// return the size of the queue
	}
	
	/**
	 * Check if the PriorityQueue is empty
	 * @return whether the PriorityQueue is empty
	 */
	public boolean isEmpty() {
		assert this.wellFormed();
		return (manyItems == 0);
		// return whether the queue is empty
	}
	
	@Override
	public String toString() {
		return Arrays.toString(data);
	}
	
	private void ensureCapacity(int minimumCapacity)
	{
		ProfileLink [] newArray;
		if(data.length < minimumCapacity)
		{
			int newCapacity = data.length *2;
			if(newCapacity < minimumCapacity)
				newCapacity = minimumCapacity;
			newArray = new ProfileLink[newCapacity];
			for(int i=0; i<manyItems; i++)
				newArray[i] = data[i];
			data = newArray;
		}
	}
	//state when called: data[current] == new element
	private void bubbleUp(int current) {
		if (current == 0) {
			return;
		}
		int parent;
		if (current % 3 == 0) {
			parent = current / 3 - 1;
		} else {
			parent = current / 3;
		}
		int compVal = COMP.compare(data[current], data[parent]);
		if (compVal < 0) {
			swap(parent,current);
			bubbleUp(parent);
			
		}
		return;
	}
	private void swap(int i, int j) {
		ProfileLink temp = data[i];
		data[i] = data[j];
		data[j] = temp;
	}
	private void bubbleDown(int i ) {
		int left,mid,right;
		left = calcLeftChild(i);
		mid = left + 1;
		right = mid + 1;
		boolean leftCanidate = false, midCanidate = false, rightCanidate = false;
		if (left < manyItems) {
			int compLeft = COMP.compare(data[i], data[left]);
			if (compLeft > 0) {
				leftCanidate = true;
			}
		}
		if (mid < manyItems) {
			int compMid = COMP.compare(data[i], data[mid]);
			if (compMid > 0) {
				midCanidate = true;
			}
		}
		if (right < manyItems) {
			int compRight = COMP.compare(data[i], data[right]);
			if (compRight > 0) {
				rightCanidate = true;
			}
		}
		if (!leftCanidate && !midCanidate && !rightCanidate) {
			return; //base case.
		} else if (leftCanidate) {
			swap(i,left);
			bubbleDown(left);
		} else if (midCanidate) {
			swap(i,mid);
			bubbleDown(mid);
		} else {
			swap(i,right);
			bubbleDown(right);
		}
		return;
		//find child that violates invariant, and swap (provided they are in bounds)
		//if all do, go left.
		
	}
	private int calcLeftChild(int i ) {
		return i * 3 + 1;
	}
	//TODO: optional helper methods
	//    bubbleDown
	//    bubbleUp
	//    other useful utility methods?
	
	public static class TestInvariant extends TestCase {
		private static final String[] TO_LOG = new String[] {"./src/edu/uwm/cs351/PriorityQueue.java"};
		private static boolean firstRun = true;
		
		public void log() {
			System.out.println("running");
			Snapshot.capture(TO_LOG);
		}
		
		PriorityQueue self;
		Profile a = new Profile("a");
		Profile b = new Profile("b");
		Profile c = new Profile("c");
		
		private ProfileLink l(int i) {
			return new ProfileLink(null, null, i);
		}

		@Override
		public void setUp() {
			if(firstRun) {
				log();
				firstRun = false;
			}
			self = new PriorityQueue(false);
		}
		
		public void testNull() {
			assertFalse(self.wellFormed());
			self.manyItems = 0;
			assertFalse(self.wellFormed());
		}

		public void testEmpty() {
			self.data = new ProfileLink[0];
			self.manyItems = 0;
			assertTrue(self.wellFormed());
			self.manyItems = -1;
			assertFalse(self.wellFormed());
			self.manyItems = 1;
			assertFalse(self.wellFormed());
		}
		
		public void testNullElements() {
			self.data = new ProfileLink[1];
			self.manyItems = 1;
			assertFalse(self.wellFormed());
			self.data = new ProfileLink[3];
			self.data[0] = new ProfileLink(a, b);
			self.data[2] = new ProfileLink(b, c);
			self.manyItems = 2;
			assertFalse(self.wellFormed());
			self.manyItems = 3;
			assertFalse(self.wellFormed());
			self.manyItems = 1;
			assertTrue(self.wellFormed());
		}
		
		public void testSmall() {
			self.data = new ProfileLink[2];
			self.data[0] = l(1);
			self.data[1] = l(1);
			self.manyItems = 2;
			assertTrue(self.wellFormed());
			self.data[1] = l(2);
			assertTrue(self.wellFormed());
			self.data[1] = l(0);
			assertFalse(self.wellFormed());
		}
		
		public void testMedium() {
			self.data = new ProfileLink[4];
			self.data[0] = l(3);
			self.data[1] = l(8);
			self.data[2] = l(5);
			self.data[3] = l(12);
			self.manyItems = 4;
			assertTrue(self.wellFormed());
			self.data[1] = l(2);
			assertFalse(self.wellFormed());
			self.data[1] = l(12);
			self.data[2] = l(1);
			assertFalse(self.wellFormed());
			self.data[2] = l(11);
			self.data[3] = l(1);
			assertFalse(self.wellFormed());
			self.data[3] = l(4);
			assertTrue(self.wellFormed());
		}
		
		public void testLarge() {
			self.data = new ProfileLink[21];
			
			self.data[0] = l(5);
			
			self.data[1] = l(11);
			self.data[2] = l(88);
			self.data[3] = l(14);

			self.data[4] = l(12);
			self.data[5] = l(36);
			self.data[6] = l(22);
			self.data[7] = l(89);
			self.data[8] = l(100);
			self.data[9] = l(99);
			self.data[10] = l(53);
			self.data[11] = l(15);
			self.data[12] = l(15);

			self.data[13] = l(13);
			self.data[14] = l(13);
			self.data[15] = l(14);
			self.data[16] = l(65);
			self.data[17] = l(39);
			self.data[18] = l(44);
			
			self.manyItems = 19;
			assertTrue(self.wellFormed());
			self.data[7] = l(86);
			assertFalse(self.wellFormed());
			self.data[7] = l(89);
			self.data[8] = l(86);
			assertFalse(self.wellFormed());
			self.data[8] = l(100);
			self.data[9] = l(86);
			assertFalse(self.wellFormed());
			self.data[9] = l(99);
			

			self.data[16] = l(35);
			assertFalse(self.wellFormed());
			self.data[16] = l(65);
			self.data[17] = l(35);
			assertFalse(self.wellFormed());
			self.data[17] = l(39);
			self.data[18] = l(35);
			assertFalse(self.wellFormed());
			self.data[18] = l(44);
			
			self.data[4] = l(14);
			assertFalse(self.wellFormed());
			self.data[4] = l(12);
			self.data[5] = l(40);
			assertFalse(self.wellFormed());
			self.data[5] = l(36);
			
			self.data[0] = l(11);
			assertTrue(self.wellFormed());
		}
	}

}
