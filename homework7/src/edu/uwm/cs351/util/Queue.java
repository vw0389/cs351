package edu.uwm.cs351.util;

import java.util.NoSuchElementException;
import edu.uwm.cs.junit.LockedTestCase;

// Victor Weinert HW7
// No help declared

// Resources used:
// The book
// https://en.wikipedia.org/wiki/Queue_(abstract_data_type)
// https://en.wikipedia.org/wiki/Floor_and_ceiling_functions
// http://pabst.cs.uwm.edu/classes/cs351/homework/homework7.html
// java doc via zeal. online doc @ https://docs.oracle.com/javase/8/docs/api/
// Specifically looked at Math.floorMod operation, decided against using it.
// Referred to lab 7 for ensureCapacity.

// Note:
// Modified TestQueue. More enqueues and dequeues for debugging.


/*
 * This class implements a generic Queue, but does not use Java's Queue interface.
 * It uses a circular array data structure.
 * It does not allow null elements.
 */

public class Queue<E> implements Cloneable {

	/** Constants */
	private static final int DEFAULT_CAPACITY = 1; // force early resizing

	/** Fields */
	private E[] data;
	private int front;
	private int manyItems;

	private boolean report(String s) {
		System.out.println("invariant error: " + s);
		return false;
	}

	/** Invariant */
	private boolean wellFormed() {
		// 0. the data array cannot be null
		if (data == null || data.length == 0) return report("0. data is null");
		// 1. front must be a valid index within the bounds of the array
		if (front < 0 || front >= data.length) return report("1. front must be a valid index within the bounds of the array");
		// 2. manyItems must not be negative or more than the length of the array
		if (manyItems < 0 || manyItems > data.length) return report("2. manyItems must not be negative or more than the length of the array");
		// 3. If manyItems is not equal to 0, there are no null values in the range that holds elements
		// NB: This range *may* wrap around the array.
		if (manyItems != 0) {
			int things = manyItems;
			
			for (int i = front; things > 0 && i >= 0; i--) {
				if (data[i] == null) return report("3. there are null values in the range that holds elements");
				
				things--;
			}
			if (things > 0) {
				
				for (int i = data.length - 1; things != 0; i--) {
					if (data[i] == null) return report("3. there are null values in the range that holds elements");
					
					things--;
					
				}
			}
		}
		
		return true;
	}

	/**
	 * a private helper method to compute where the back of the queue is
	 * AKA where enqueue should put the element
	 * NB: Do not use / or % operations.
	 * @return the index after the last element
	 */
	private int getBack(){
		int i;
		if (manyItems == 0 || manyItems == data.length) {
			i = front;
		} else if (front + 1 - manyItems >= 0) {
			i = front - manyItems;
		} else {
			i = front + manyItems - 1;
		}
		if (i < 0) {
			i = data.length - 1;
		}
		return i;
	}

	/**
	 * Helper function to advance through the circular array.
	 * Keep in mind the direction the queue goes in the array.
	 * NB: Do not use / or % operations.
	 * @param i the index
	 * @return the next index after i
	 */
	private int nextIndex(int i) {
		if (i == 0 ) {
			return data.length - 1;
		} else { 
			return i - 1;
		}
		// No loops allowed, one "if" permitted.
	}

	private Queue(boolean ignored) {} // do not change: used by invariant checker.

	/** Create an empty Queue with capacity DEFAULT_CAPACITY. */
	public Queue() {
		data = makeArray(DEFAULT_CAPACITY);
		manyItems = 0;
		front = 0;
		assert this.wellFormed();
		//the constructor. Use makeArray.
	}

	@SuppressWarnings("unchecked")
	private E[] makeArray(int s) {
		return (E[]) new Object[s];
	}

	/**
	 * Determine whether the queue is empty.
	 * @return true if queue is empty
	 */
	public boolean isEmpty() {
		assert this.wellFormed();
		return (manyItems == 0);
	}

	/**
	 * Compute how many elements are in the queue.
	 * @return how many elements are in this queue
	 */
	public int size() {
		assert this.wellFormed();
		return manyItems;
	}

	/**
	 * Add an element to the queue,
	 * @param x the element to add, must not be null
	 * @exception IllegalArgumentException if x is null
	 */
	public void enqueue(E x) {
		// (no loops, one if)
		assert this.wellFormed();
		if (x == null) throw new IllegalArgumentException();
		
		ensureCapacity(manyItems+1);
		int i = getBack();

		data[i] = x;
		
		manyItems++;
		
		assert this.wellFormed();
	}

	/**
	 * Return (but do not remove) the front element of this queue.
	 * @return element at front of queue
	 * @exception NoSuchElementException if the queue is empty
	 */
	public E front() {
		assert this.wellFormed();
		if (manyItems ==  0) throw new NoSuchElementException();
		return data[front];
		
		
	}

	/**
	 * Remove and return the front element from the queue.
	 * @return element formerly at front of queue
	 * @exception NoSuchElementException if the queue is empty
	 */
	public E dequeue() {
		assert this.wellFormed();
		// (no loops, "if" only for error)
		if (isEmpty()) throw new NoSuchElementException("empty queue");
		
		E thing = data[front];
		front = nextIndex(front);
		manyItems--;
		
		
		assert this.wellFormed();
		return thing;
	}
	
	@Override
	public String toString() {
		assert this.wellFormed();
		if (isEmpty()) return "";
		
		String builder = "";
		
		int breaker = getBack();
		for (int j = front; j >= 0 && j != breaker; j--) {
			builder += data[j].toString();
		}
		if (breaker > front) {
			for (int i = data.length -1; i != breaker + 1; i--) {
				builder += data[i].toString();
			}
		}
		if (manyItems != 0) {
			int things = manyItems;
			
			for (int i = front; things > 0 && i >= 0; i--) {
				builder += data[i].toString();
				
				things--;
			}
			if (things > 0) {
				
				for (int i = data.length - 1; things != 0; i--) {
					builder += data[i].toString();
					
					things--;
					
				}
			}
		}
		//Return a String that contains the toString of each element, front to back
		return builder ;
	}


	@Override
	@SuppressWarnings("unchecked")
	public Queue<E> clone()
	{
		Queue<E> result = null;
		try {
			result = (Queue<E>) super.clone( );
		}
		catch (CloneNotSupportedException e) {  
			// Shouldn't happen
		}
		result.data = data.clone();
		return result;
	}

	/**
	 * Ensure that the capacity of the array is such that
	 * at least minCap elements can be in queue.  If necessary,
	 * the capacity is doubled and the elements are arranged
	 * in the queue correctly. There is generally more
	 * than one valid arrangement for your data in the array.
	 * @param minCap the minimum capacity
	 */
	private void ensureCapacity(int minCap) {
		if (data.length >= minCap) return;
		int newLength;
		if (data.length*2 < minCap){
			newLength = minCap;
		} else {
			newLength = data.length*2;
		}
		E newData[] = makeArray(newLength);
		int things = manyItems;
		int index = newData.length - 1;
		for (int i = front; things >= 0 && i >= 0; i--) {
			newData[index] = data[i];
			index--;
			things--;
		}
		if (things > 0) {
			for (int i = data.length - 1; things != 0; i--) {
				newData[index] = data[i];
				index--;
				things--;
			}
		}
		this.data = newData;
		front = newData.length - 1;
	}

	// I put the values from testInternals.tst here
	public static class TestInvariant extends LockedTestCase {
		private Queue<Object> self;

		protected void setUp() {
			self = new Queue<Object>(false);
		}

		public void test00() {
			//data array is null
			assertFalse(self.wellFormed());
		}

		public void test01() {
			self.data = new Object[0];
			//Think about why this isn't well formed. Which field gives us a problem?
			assertFalse(self.wellFormed());
			self.data = new Object[DEFAULT_CAPACITY];
			assertTrue(self.wellFormed());
		}

		public void test02() {
			self.data = new Object[DEFAULT_CAPACITY];

			self.front = -1;
			assertFalse(self.wellFormed());
			
			self.front = DEFAULT_CAPACITY;
			assertFalse(self.wellFormed());
			
			self.front = 0;
			assertTrue(self.wellFormed());
			
		}

		public void test03() {
			self.data = new Object[] { null, null, null, null };
			//manyItems is 0

			self.front = 1;
			assertEquals(Tb(696236041), self.wellFormed());

			self.front = 2;
			assertEquals(true, self.wellFormed());
			
			self.front = 3;
			assertEquals(true, self.wellFormed());
			
			self.front = 0;
			assertEquals(true, self.wellFormed());
		}
		
		public void test04() {
			self.data = new Object[] { null, null, null, null };
			//front is 0
			
			self.manyItems = -1;
			assertEquals(false, self.wellFormed());
			
			self.manyItems = 5;
			assertEquals(false, self.wellFormed());

			self.manyItems = 1;
			assertEquals(false, self.wellFormed());
		}
		
		public void test05() {
			self.data = new Object[] { new Integer(5), new Integer(3), new Integer(2), new Integer(4) };
			self.front = 3;
			self.manyItems = 4;
			assertEquals(true, self.wellFormed());
			self.front = 1;
			assertEquals(true, self.wellFormed());
			self.manyItems = 0;
			assertEquals(true, self.wellFormed());
		}

		public void test06() {
			self.data = new Object[] { null, null, 6, null };
			self.manyItems = 1;
			//we do not care about array data outside of the queue range
			
			self.front = 1;
			assertEquals(false, self.wellFormed());

			self.front = 3;
			assertEquals(false, self.wellFormed());

			self.front = 2;
			assertEquals(true, self.wellFormed());
		}

		public void test07() {
			self.data = new Object[] { 2, null, 6, 0 };
			self.manyItems = 2;
			
			//remember the queue goes right-to-left from the front
			self.front = 0;
			assertEquals(true, self.wellFormed());
			
			self.front = 1;
			assertEquals(false, self.wellFormed());
			
			self.front = 2;
			assertEquals(false, self.wellFormed());
			
			self.front = 3;
			assertEquals(true, self.wellFormed());
		}

		public void test08() {
			self.data = new Object[] { 2, null, 6, 0 };

			self.front = 0;
			self.manyItems = 3;
			assertEquals(true, self.wellFormed());
			
		}

		public void test09() {
			self.data = new Object[] { 2, null, null, 0 };

			self.front = 2;
			self.manyItems = 2;
			assertEquals(false, self.wellFormed());

			self.front = 3;
			self.manyItems = 1;
			assertEquals(true, self.wellFormed());

			self.front = 0;
			self.manyItems = 2;
			assertEquals(true, self.wellFormed());
		}

		public void test10() {
			self.data = new Object[] { null };
			self.front = 0;
			self.ensureCapacity(1);
			assertEquals(1, self.data.length);
		}

		public void test11() {
			self.data = new Object[] { 1, null };
			self.front = 0;
			self.manyItems = 1;
			self.ensureCapacity(1);
			assertEquals(2, self.data.length);
			self.ensureCapacity(2);
			assertEquals(2, self.data.length);
			self.ensureCapacity(3);
			assertEquals(4, self.data.length);
		}

		public void test12() {
			self.data = new Object[] { 1, 2, null };
			self.front = 1;
			self.manyItems = 2;
			self.ensureCapacity(3);
			assertEquals(3, self.data.length);
			self.ensureCapacity(6);
			assertEquals(6, self.data.length);
		}

		public void test13() {
			self.data = new Object[4];
			self.front = 0;
			self.ensureCapacity(300);
			assertEquals(300, self.data.length);
		}

		public void test14() {
			self.data = new Object[100];
			self.front = 0;
			self.ensureCapacity(101);
			assertEquals(200, self.data.length);
		}

		public void test15() {
			self.data = new Object[8];
			assertEquals(6, self.nextIndex(7));
			assertEquals(0, self.nextIndex(1));
			assertEquals(7, self.nextIndex(0));
		}
		
		
		public void test16() {
			self.data = new Object[] { null, null, 8, 3, 0 };
			self.front = 4;
			self.manyItems = 3;
			assertEquals(1, self.getBack());
			
			self.data = new Object[] { 3, 0, null, null, 8 };
			self.front = 1;
			self.manyItems = 3;
			assertEquals(3, self.getBack());
			
			self.data = new Object[] { 0, null, null, 8, 3 };
			self.front = 0;
			self.manyItems = 3;
			assertEquals(2, self.getBack());
			
		}
		
		public void test17() {
			self.data = new Object[] {8, 3, 0 };
			self.front = 2;
			self.manyItems = 0;
			assertEquals(2, self.getBack());
			self.manyItems = 1;
			assertEquals(1, self.getBack());
			self.manyItems = 2;
			assertEquals(0, self.getBack());
			self.manyItems = 3;
			assertEquals(2, self.getBack());
		}
		
		public void test18() {
			self.data = new Object[] {4};
			self.front = 0;
			self.manyItems = 0;
			assertEquals(0, self.getBack());
			self.manyItems = 1;
			assertEquals(0, self.getBack());
		}
	}
}
