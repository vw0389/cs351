// This is an assignment for students to complete after reading Chapter 3 of
// "Data Structures and Other Objects Using Java" by Michael Main.
// HW2 Victor Weinert, CS751-001 no help declared
// Reviewed Chapter 3 of the book, as well as handout
package edu.uwm.cs351;

import edu.uwm.cs.junit.LockedTestCase;

/******************************************************************************
 * This class is a homework assignment;
 * A RasterSeq is a collection of Rasters.
 * The sequence can have a special "current element," which is specified and 
 * accessed through four methods of the sequence class 
 * (start, getCurrent, advance and hasCurrent).
 * When there is no current element, there is a current non-element position, which may be
 * between elements, before the first element, or after the last element.
 * The semantics of the current element are slightly changed from the textbook.
 * Please read the assignment handout.
 *
 * @note
 *   (1) The capacity of one a sequence can change after it's created, but
 *   the maximum capacity is limited by the amount of free memory on the 
 *   machine. The constructor, addAfter, addBefore, clone, 
 *   and concatenation will result in an
 *   OutOfMemoryError when free memory is exhausted.
 *   <p>
 *   (2) A sequence's capacity cannot exceed the maximum integer 2,147,483,647
 *   (Integer.MAX_VALUE). Any attempt to create a larger capacity
 *   results in a failure due to an arithmetic overflow. 
 *   
 *   NB: Neither of these conditions require any work for the implementors (students).
 *
 * @see
 *   <A HREF="../../../../edu/colorado/collections/DoubleArraySeq.java">
 *   Java Source Code for the original class by Michael Main
 *   </A>
 *
 ******************************************************************************/
public class RasterSeq implements Cloneable
{
	// Implementation of the RasterSeq class:
	//   1. The number of elements in the sequences is in the instance variable 
	//      manyItems.  The elements may be Raster objects or nulls.
	//   2. For any sequence, the elements of the
	//      sequence are stored in data[0] through data[manyItems-1], and we
	//      don't care what's in the rest of data.
	//   3. We remember whether we have a current element, or we do not for some
	//		reason (see the handout).  We use a boolean to remember this.
	//   4. If there is a current element, then it lies in data[currentIndex];
	//      if there is no current element, then currentIndex can be any index
	//      in the array or the same as manyItems.
	//	5.	If there is no current element, then the current non-element position
	//		is before any element at currentIndex, and after any element at
	//		currentIndex - 1.
	//		

	private Raster[ ] data;
	private int manyItems;
	private boolean hasCurrentElement;
	private int currentIndex; 

	private static int INITIAL_CAPACITY = 1;

	private static boolean _doReport = true; // changed only by invariant tester
	
	private boolean _report(String error) {
		if (_doReport) System.out.println("Invariant error: " + error);
		else System.out.println("Caught problem: " + error);
		return false;
	}

	private boolean wellFormed() {
		// Check the invariant.
		
		// 1. The data array is never null
		if (data == null) {
			return _report("data == null (Invariant 1)"); // test the NEGATION of the condition
		}

		// 2. The data array is at least as long as the number of items
		//    claimed by the sequence.
		if ( manyItems > data.length) {
			return _report("manyItems > data.length (Invariant 2)");
		}
		
		// 3. currentIndex is never negative and never more than the number of
		//    items claimed by the sequence.
		if ( currentIndex < 0 || currentIndex > manyItems) {
			return _report("currentIndex < 0 || currentIndex > manyItems (Invariant 3)");
		}
		
		// 4. if isCurrent is true, then current index must be a valid index
		//    (it cannot be equal to the number of items)
		if (hasCurrentElement == true && currentIndex == manyItems) {
			return _report ("hasCurrentElement == true && currentIndex == manyItems (Invariant 4)");
		}
		
		// If no problems discovered, return true
		return true;
	}

	// This is only for testing the invariant.  Do not change!
	private RasterSeq(boolean testInvariant) { }
	
	/**
	 * Initialize an empty sequence with an initial capacity of INITIAL_CAPACITY.
	 * The addAfter and addBefore methods work
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param - none
	 * @postcondition
	 *   This sequence is empty and has an initial capacity of INITIAL_CAPACITY
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for initial array.
	 **/   
	public RasterSeq( )
	{
		// NB: NEVER assert the invariant at the START of the constructor.
		
		this(INITIAL_CAPACITY);
		
		//wellFormed() is called at the end of the main constructor, thus making this one redundant
		//assert wellFormed() : "Invariant false at end of constructor";
	}


	/**
	 * Initialize an empty sequence with a specified initial capacity. Note that
	 * the addAfter and addBefore methods work
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @param initialCapacity
	 *   the initial capacity of this sequence
	 * @precondition
	 *   initialCapacity is non-negative.
	 * @postcondition
	 *   This sequence is empty and has the given initial capacity.
	 * @exception IllegalArgumentException
	 *   Indicates that initialCapacity is negative.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Particle[initialCapacity].
	 **/   
	public RasterSeq(int initialCapacity)
	{
		if (initialCapacity < 0) {
			throw new IllegalArgumentException();
		}
		data = new Raster[initialCapacity];
		hasCurrentElement = false;
		currentIndex = 0;
		manyItems = 0;
		
		assert wellFormed() : "Invariant false at end of constructor";
	}

	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size( )
	{
		assert wellFormed() : "invariant failed at start of size";
		return manyItems;
		// size() should not modify anything, so we omit testing the invariant at the end
	}

	/**
	 * The first element (if any) of this sequence is now current.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence (if any) is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant failed at start of start";
		
		if (manyItems == 0) {
			return;
		} else {
			currentIndex = 0;
			hasCurrentElement = true;
		}
		
		assert wellFormed() : "invariant failed at end of start";
	}

	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element (a Raster or null) that can be retrieved with the 
	 * getCurrent method. This depends on the status of the cursor.
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean hasCurrent( )
	{
		assert wellFormed() : "invariant failed at start of isCurrent";

		return hasCurrentElement;
	}

	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @return
	 *   the current element of this sequence, possibly null
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Raster getCurrent( )
	{
		assert wellFormed() : "invariant failed at start of getCurrent";
		if (hasCurrentElement == false) {
			throw new IllegalStateException();
		} else {
			return data[currentIndex];
		}
		
	}

	/**
	 * Return true if we are at the end of the sequence.
	 * This can happen in several different ways:
	 * <ol>
	 * <li> The sequence is empty.
	 * <li> The last element has just been removed.
	 * <li> We advanced from the last element.
	 * <li> All elements were added via addBefore, and we have not called
	 * 		start since adding anything.
	 * </ol>
	 * The code you write has no need to distinguish these situations.
	 * @precondition true
	 * @return true if at the end of the sequence
	 */
	public boolean atEnd() {
		assert wellFormed() : "Invariant failed at start of atEnd";
		if (manyItems == 0) {
			return true;
		} else if (manyItems == currentIndex && hasCurrentElement == false) {
			return true;
		} else if (manyItems == currentIndex + 1 && hasCurrentElement == false) {
			return true;
		}
		return false;
		
		// TODO: Implement this code correctly
		
		// Our solution makes use of no conditionals (if/while/for/&&/||).
	}
	
	/**
	 * Move forward, so that the next element is now the current element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   atEnd() returns false.
	 * @postcondition
	 *   If the current element was already the last element of this sequence 
	 *   (with nothing after it), then we will be at the end. 
	 *   Otherwise, the next element is current (next after the current
	 *   element, or after the current non-element position).
	 * @exception IllegalStateException
	 *   If at the end.
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant failed at start of advance";
		if (atEnd() == true) {
			throw new IllegalStateException("atEnd() == true, cannot advance");
		} else if (currentIndex == 0 && hasCurrentElement == false){
			this.start();
			
			// TODO: Finish Implementing this code correctly
		}else if (hasCurrentElement == false) {
			hasCurrentElement = true;
		} else if (hasCurrentElement == true && currentIndex + 1 < manyItems){
			currentIndex = currentIndex + 1;
		}
		
		assert wellFormed() : "invariant failed at end of advance";
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   isCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence.
	 *   There is no longer any current element. The current non-element position
	 *   is now in the place that the removed element previously occupied.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert wellFormed() : "invariant failed at start of removeCurrent";
		if (hasCurrentElement == false) {
			throw new IllegalStateException("hasCurrentElement == false, remove was attempted");
		} else {
			data[currentIndex] = null;
			hasCurrentElement = false;
		}
		
		int j = currentIndex;
		for (int i = currentIndex + 1; i < manyItems; i++) {
			data[j] = data[i];
		}
		manyItems  =  manyItems - 1;
		// TODO: Implement this correctly.
		// You will need to shift elements in the array.
		assert wellFormed() : "invariant failed at end of removeCurrent";
	}

	/**
	 * Add a new element to this sequence, before the current element (if any).
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added, it is allowed to be null
	 * @postcondition
	 *   The element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the current non-element position. In all cases, which element
	 *   is current is unchanged. The new position, whether element or not,
	 *   will be after the newly added element.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause the sequence to fail with an
	 *   arithmetic overflow.
	 **/
	public void addBefore(Raster element)
	{
		assert wellFormed() : "invariant failed at start of addBefore";
		ensureCapacity(manyItems + 1);
		if (hasCurrentElement == false) {
			data[currentIndex] = element;
			
		} else {
			for (int i = manyItems; i > currentIndex; i--) {
				data[i] = data [i -1];
				
			}
			data[currentIndex] = element;
			currentIndex = currentIndex + 1;
		}
		manyItems = manyItems + 1;
		// TODO: Implement this code.
		assert wellFormed() : "invariant failed at end of addBefore";
	}

	/**
	 * Add a new element to this sequence, after the current element if any.
	 * If the new element would take this sequence beyond its current capacity,
	 * then the capacity is increased before adding the new element.
	 * @param element
	 *   the new element that is being added, may be null
	 * @postcondition
	 *   The element has been added to this sequence. If there was
	 *   a current element, then the new element is placed after the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the current non-element position.
	 *   In all cases, which element is current is unchanged. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for increasing the sequence's capacity.
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause the sequence to fail with an
	 *   arithmetic overflow.
	 **/
	public void addAfter(Raster element)
	{
		assert wellFormed() : "invariant failed at start of addAfter";
		
		ensureCapacity(manyItems + 1);
		if (hasCurrentElement == false) {
			data[currentIndex] = element;
		} else if (manyItems == 0) {  //beginning
			data[currentIndex] = element;
			
		}else if (atEnd()) { //end
			data[currentIndex + 1] = element;
			
		} else { //middle
			for(int i = manyItems - 1; i > currentIndex; i--) {
				data[i] = data[i - 1];
			}
			data[currentIndex + 1] = element;
		}
		manyItems++;
		// TODO: test this code.
		assert wellFormed() : "invariant failed at end of addAfter";
	}


	/**
	 * Place the contents of another sequence into this sequence, 
	 * following the current position.
	 * @param addend
	 *   a sequence whose contents will be placed into this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed at the current location,
	 *   after the current element if there is one.
	 *   The current element of this sequence if any, remains unchanged.
	 *   If there was no current element, the current non-element position
	 *   is before the newly added elements.
	 *   The addend is unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory to increase the size of this sequence.
	 * @note
	 *   An attempt to increase the capacity beyond
	 *   Integer.MAX_VALUE will cause an arithmetic overflow
	 *   that will cause the sequence to fail.
	 **/
	public void addAll(RasterSeq addend)
	{
		assert wellFormed() : "invariant failed at start of addAll";
		if (addend == null) {
			throw new NullPointerException();
		}
		// clone the addend to make sure we don't change it
		RasterSeq cloned = addend.clone();
		
		// TODO: Implement this code.
		
		int totalItems = this.manyItems + cloned.manyItems;
		ensureCapacity(totalItems);
		int j = 0;
		for (int i = this.manyItems; i < totalItems - 1; i++) {
			this.data[i] = addend.data[j];
			j++;
		}
		
		// Recall that you can freely access private fields of the addend.
		assert wellFormed() : "invariant failed at end of addAll";
	}   


	/**
	 * Change the current capacity of this sequence.
	 * @param minimumCapacity
	 *   the callers desired capacity.  The end result must have this
	 *   or a greater capacity.
	 * @postcondition
	 *   This sequence's capacity has been changed to at least minimumCapacity.
	 *   If the capacity was already at or greater than minimumCapacity,
	 *   then the capacity is left unchanged.
	 *   If the size is changed, it must be at least twice as big as before.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for: new array of minimumCapacity elements.
	 **/
	private void ensureCapacity(int minimumCapacity)
	{
		if (minimumCapacity <= data.length) {
			return;
		}
		Raster[ ] newData;
		if (minimumCapacity > (data.length * 2)) {
			newData = new Raster[minimumCapacity];
		} else {
			newData = new Raster[data.length * 2];
		}
		for (int i = 0; i < manyItems; i++) {
			newData[i] = data[i];
		}
		data = newData;
		
		// This is a private method: don't check invariants
	}

	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public RasterSeq clone( )
	{  // Clone a ParticleSeq object.
		assert wellFormed() : "invariant failed at start of clone";
		RasterSeq answer;

		try
		{
			answer = (RasterSeq) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  // This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}

		answer.data = data.clone( ); // all that's needed for Homework #2

		assert wellFormed() : "invariant failed at end of clone";
		assert answer.wellFormed() : "invariant failed for clone";
		
		return answer;
	}

	public String toString() {
		// for debugging
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		boolean first = true;
		int i;
		for (i=0; i < manyItems; ++i) {
			if (first) first = false;
			else sb.append(",");
			if (i == currentIndex) {
				sb.append("*");
				if (hasCurrentElement == false)
					sb.append(", ");
			}
			sb.append(data[i]);
		}
		sb.append("|");
		if (i == currentIndex) sb.append("*");
		first = true;
		for (; i < data.length; ++i) {
			if (first) first = false;
			else sb.append(",");
			sb.append(data[i]);
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static class TestInvariant extends LockedTestCase {
		private RasterSeq hs;
		Raster r1 = new Raster(1,1);
		Raster r2 = new Raster(2,2);
		Raster r3 = new Raster(3,3);
		Raster r4 = new Raster(4,4);
		Raster r5 = new Raster(5,5);
		
		@Override
		public void setUp() {
			hs = new RasterSeq(false);
			_doReport = false;
		}
		
		//test0x: test invariant
		
		public void test01() {
			assertFalse(hs.wellFormed());
		}
		
		public void test02() {
			hs.data = new Raster[3];
			hs.manyItems = -1;
			assertFalse(hs.wellFormed());
		}
		
		public void test03() {
			hs.data = new Raster[3];
			hs.manyItems = 4;
			assertFalse(hs.wellFormed());
		}
		
		public void test04() {
			hs.data = new Raster[10];
			hs.manyItems = 4;
			_doReport = true;
			assertTrue(hs.wellFormed());
		}
		
		public void test05() {
			hs.data = new Raster[5];
			hs.manyItems = 4;
			hs.currentIndex = -1;
			assertFalse(hs.wellFormed());
		}
		
		public void test06() {
			hs.data = new Raster[3];
			hs.manyItems = 3;
			hs.currentIndex = 3;
			_doReport = true;
			assertTrue(hs.wellFormed());
		}
		
		public void test07() {
			hs.data = new Raster[5];
			hs.manyItems = 3;
			hs.currentIndex = 4;
			assertFalse(hs.wellFormed());
		}
		
		public void test08() {
			hs.data = new Raster[3];
			hs.manyItems = 2;
			hs.currentIndex = 2;
			_doReport = true;
			assertTrue(hs.wellFormed());
		}
		
		public void test09() {
			hs.data = new Raster[10];
			hs.manyItems = 5;
			hs.currentIndex = 5;
			hs.data[5] = new Raster(2,2);
			hs.hasCurrentElement = true;
			assertFalse(hs.wellFormed());
		}
		
		//test1x: ensureCapacity tests
		
		public void test10() {
			hs.data = new Raster[0];
			hs.ensureCapacity(0);
			assertEquals(0, hs.data.length);
		}
		
		public void test11() {
			hs.data = new Raster[0];
			hs.ensureCapacity(1);
			assertEquals(1, hs.data.length);
		}
		
		public void test12() {
			hs.data = new Raster[0];
			hs.ensureCapacity(2);
			assertEquals(2, hs.data.length);
		}
		
		public void test13() {
			hs.data = new Raster[0];
			hs.ensureCapacity(-1);
			assertEquals(0, hs.data.length);
		}
		
		public void test14() {
			hs.data = new Raster[0];
			hs.ensureCapacity(100);
			assertEquals(100, hs.data.length);
		}
		
		public void test15() {
			hs.data = new Raster[1];
			hs.ensureCapacity(0);
			assertEquals(1, hs.data.length);
		}
		
		public void test16() {
			hs.data = new Raster[1];
			hs.ensureCapacity(1);
			assertEquals(1, hs.data.length);
		}
		
		public void test17() {
			hs.data = new Raster[1];
			hs.ensureCapacity(2);
			assertEquals(2, hs.data.length);
		}
		
		public void test18() {
			hs.data = new Raster[2];
			hs.ensureCapacity(0);
			assertEquals(2, hs.data.length);
		}
		
		public void test19() {
			hs.data = new Raster[2];
			hs.ensureCapacity(2);
			assertEquals(2, hs.data.length);
		}
		
		public void test20() {
			hs.data = new Raster[2];
			hs.ensureCapacity(3);
			assertEquals(4, hs.data.length);
		}
		
		public void test21() {
			hs.data = new Raster[2];
			hs.ensureCapacity(4);
			assertEquals(4, hs.data.length);
		}
		
		public void test22() {
			hs.data = new Raster[2];
			hs.ensureCapacity(5);
			assertEquals(5, hs.data.length);
		}
		
		public void test23() {
			hs.data = new Raster[3];
			hs.ensureCapacity(0);
			assertEquals(3, hs.data.length);
		}
		
		public void test24() {
			hs.data = new Raster[3];
			hs.ensureCapacity(3);
			assertEquals(3, hs.data.length);
		}
		
		public void test25() {
			hs.data = new Raster[3];
			hs.ensureCapacity(4);
			assertEquals(6, hs.data.length);
		}
		
		public void test26() {
			hs.data = new Raster[3];
			hs.ensureCapacity(6);
			assertEquals(6, hs.data.length);
		}
		
		public void test27() {
			hs.data = new Raster[3];
			hs.ensureCapacity(7);
			assertEquals(7, hs.data.length);
		}
		
		public void test28() {
			hs.data = new Raster[100];
			hs.ensureCapacity(0);
			assertEquals(100, hs.data.length);
		}
		
		public void test29() {
			hs.data = new Raster[100];
			hs.ensureCapacity(101);
			assertEquals(200, hs.data.length);
		}
		
		public void test30() {
			hs.data = new Raster[100];
			hs.ensureCapacity(201);
			assertEquals(201, hs.data.length);
		}
		
		public void test31() {
			hs.data = new Raster[100];
			hs.ensureCapacity(255);
			assertEquals(255, hs.data.length);
		}
		
		public void test40() {
			hs.data = new Raster[5];
			hs.manyItems = 5;
			hs.hasCurrentElement = false;
			hs.currentIndex = 4;
			hs.data[0] = r1;
			hs.data[1] = r2;
			hs.data[2] = r3;
			hs.data[3] = r4;
			hs.data[4] = r5;
			
			hs.ensureCapacity(0);
			assertEquals(r1, hs.data[0]);
			assertEquals(r2, hs.data[1]);
			assertEquals(r3, hs.data[2]);
			assertEquals(r4, hs.data[3]);
			assertEquals(r5, hs.data[4]);
			assertEquals(5, hs.manyItems);
			assertEquals(false, hs.hasCurrentElement);
			assertEquals(4, hs.currentIndex);
			
			hs.ensureCapacity(6);
			assertEquals(r1, hs.data[0]);
			assertEquals(r2, hs.data[1]);
			assertEquals(r3, hs.data[2]);
			assertEquals(r4, hs.data[3]);
			assertEquals(r5, hs.data[4]);
			assertEquals(5, hs.manyItems);
			assertEquals(false, hs.hasCurrentElement);
			assertEquals(4, hs.currentIndex);
			
			hs.ensureCapacity(50);
			assertEquals(r1, hs.data[0]);
			assertEquals(r2, hs.data[1]);
			assertEquals(r3, hs.data[2]);
			assertEquals(r4, hs.data[3]);
			assertEquals(r5, hs.data[4]);
			assertEquals(5, hs.manyItems);
			assertEquals(false, hs.hasCurrentElement);
			assertEquals(4, hs.currentIndex);
		}
	}
}

