package edu.uwm.cs351;



import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM
// Victor Weinert.  No help delcared  
// Reviewed the book ( version 4) and the homework handout, as well as java documentation on Linked Lists and the linked lists variations on the cs351 site.
// Finished
/**
 * A linked-list implementation of a Sequence variant
 * 
 * The data structure is a linked list with null and duplicate values allowed.
 * The fields should be:
 * <dl>
 * <dt>precursor</dt> .
 * <dt>head</dt> 
 * <dt>manyNodes</dt> 
 * </dl>
 * The class should define a private wellFormed() method
 * and perform assertion checks in each method.
 */

public class SequentialGallery implements Cloneable{
	/** Collection Fields */
	private static class Node {
		Painting data;
		Node next;
		public Node (Painting painting, Node next) {
			this.data = painting;
			this.next = next;
		}
	}
	// Declare the private static Node class.
	// It should have a constructor but no methods unlike the textbook example.
	// The fields of Node should have "default" access (neither public, nor private)
	

	//Declare the private fields of SequentialGallery needed for sequences
	private Node head = null;
	private int manyNodes = 0;
	private Node precursor = null;
	
	private SequentialGallery(boolean ignored) {} // DO NOT CHANGE THIS

	private static boolean doReport = true; // only to be changed in JUnit test code

	private boolean _report(String s) {
		if (doReport) System.out.println(s);
		return false;
	}
	
	/**
	 * The Invariant
	 */
	private boolean wellFormed() {
		// Invariant:
		// 1. list must not include a cycle.
		// 2. manyNodes is number of nodes in list
		// 3. precursor is either null or points to a node in the list.

		// 0. nothing in the LL, nothing to check.
		if (head == null && manyNodes == 0) {
			return true;
		} 
		
		// We do the first one for you:
		// check that list is not cyclic
		// 1.list must not include a cycle.
		if (head != null) {
			// This check uses an interesting "Tortoise and Hare" algorithm
			Node fast = head.next;
			for (Node p = head; fast != null && fast.next != null; p = p.next) {
				if (p == fast) return _report("list is cyclic!");
				fast = fast.next.next;
			}
		}
		
		boolean found  = false;
		if (head == null && manyNodes != 0 || head != null && manyNodes == 0) {
			return _report("2. head == null && manyNodes != 0 || head != null && manyNodes == 0");
		} else {
			int count = 1;
			Node cursor = head;
			if (precursor == null  || precursor == head) {
				found = true;
			}
			while (cursor.next != null) {
				count++;
				cursor = cursor.next;
				if (precursor != null && precursor == cursor) {
					found = true;
				}
			}
			// 2. manyNodes is number of nodes in list
			if (count != manyNodes) {
				return _report("2. manyNodes != count");
			}
			// 3. precursor is either null or points to a node in the list.
			if (found == false) {
				return _report("3. Precursor pointing to element not in list");
			}
		}

		return true;
	}
	/**
	 * Create an empty SequentialGallery
	 * @param - none
	 * @postcondition
	 *   This Gallery is empty 
	 **/     
	public SequentialGallery()
	{
		assert wellFormed() : "invariant failed in constructor";
	}

	
	/**
	 * Add a new element to this sequence, before the current element. 
	 * @param element
	 *   the new element that is being added
	 * @postcondition
	 *   A new copy of the element has been added to this sequence. If there was
	 *   a current element, then the new element is placed before the current
	 *   element. If there was no current element, then the new element is placed
	 *   at the end of the sequence. In all cases, the new element becomes the
	 *   new current element of this sequence. 
	 **/
	public void insert(Painting element)
	{
		assert wellFormed() : "invariant wrong at start of insert";
		
		if (manyNodes == 0) {
			head = new Node(element, null);
		} else if (precursor == null) {
			Node newNode = new Node(element, head);
			head = newNode;
		} else {
			Node nextNode = precursor.next;
			precursor.next = new Node(element, nextNode);
		}
		
		manyNodes++;
		
		assert wellFormed() : "invariant wrong at end of insert";
	}

	/**
	 * Place the contents of another SequentialGallery (which may be the
	 * same sequence as this!) into this sequence before the current element (if any).
	 * @param addend
	 *   a sequence whose contents will be placed into this sequence
	 * @precondition
	 *   The parameter, addend, is not null. 
	 * @postcondition
	 *   The elements from addend have been placed into
	 *   this sequence. The current element of this sequence is now
	 *   the first element inserted (if any).  If the added sequence
	 *   is empty, this sequence and the current element (if any) are
	 *   unchanged.
	 * @exception NullPointerException
	 *   Indicates that addend is null. 
	 **/
	public void insertAll(SequentialGallery addend)
	{
		assert wellFormed() : "invariant wrong at start of addAll";
		assert addend.wellFormed() : "invariant of parameter wrong at start of addAll";
		if (addend == null) {
			throw new NullPointerException();
			
		} else if (addend.manyNodes == 0) {
			return;
			
		}
		SequentialGallery clone = addend.clone();
		
		if (this.manyNodes == 0) {
			this.head = clone.head;
			this.manyNodes = clone.manyNodes;
			this.precursor = null;
			
		} else {
			Node cursor = clone.head;
			
			while (cursor.next != null) {
				cursor = cursor.next;
			}
			if (this.precursor == null) {
				cursor.next = this.head;
				this.head = clone.head;
				
			} else {
				cursor.next = precursor.next;
				precursor.next = clone.head;
				
			}
			
			this.manyNodes += clone.manyNodes;
		}
		
		assert wellFormed() : "invariant wrong at end of insertAll";
		assert addend.wellFormed() : "invariant of parameter wrong at end of insertAll";
	}  
	
	/**
	 * Create a new SequentialGallery that contains all the elements from one sequence
	 * followed by another.
	 * @param g1
	 *   the first of two sequences
	 * @param g2
	 *   the second of two sequences
	 * @precondition
	 *   Neither g1 nor g2 is null.
	 * @return
	 *   a new sequence that has the elements of g1 followed by the
	 *   elements of s2 (with no current element)
	 * @exception NullPointerException.
	 *   Indicates that one of the arguments is null.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for the new sequence.
	 **/   
	public static SequentialGallery catenation(SequentialGallery g1, SequentialGallery g2)
	{
		if (g1 == null || g2 == null) {
			throw new NullPointerException();
		}
		
		if (g1.manyNodes == 0 && g2.manyNodes == 0) {
			return new SequentialGallery();
			
		} else if (g1.manyNodes == 0) {
			SequentialGallery g = g2.clone();
			Node m = g.head;
			while (m.next != null ) {

				m = m.next;
			}
			g.precursor = m;
			return g;
		} else if (g2.manyNodes == 0) {
			SequentialGallery g = g1.clone();
			Node m = g.head;
			while (m.next != null ) {
				m = m.next;
			}
			g.precursor = m;
			return g;
		} 
		SequentialGallery g = g1.clone();
		SequentialGallery g2Clone = g2.clone();
		Node end_g1 = g.head;
		while (end_g1.next != null) {
			end_g1 = end_g1.next;
		}
		g.manyNodes += g2Clone.manyNodes;
		g.precursor = null;
		end_g1.next = g2Clone.head;
		while ( end_g1.next != null) {
			end_g1 = end_g1.next;
		}
		g.precursor = end_g1;
		return g;
		
	}
	/**
	 * Accessor method to get the current element of this sequence. 
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @return
	 *   the current element of this sequence
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   getCurrent may not be called.
	 **/
	public Painting getCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		if (hasCurrent() == true) {
			if (precursor == null) {
				return head.data;
			} else {
				return precursor.next.data;
			}
		} else {
			throw new IllegalStateException();
		}
		
		// This method shouldn't modify any fields, hence no assertion at end
	}


	/**
	 * Accessor method to determine whether this sequence has a specified 
	 * current element that can be retrieved with the 
	 * getCurrent method. 
	 * @param - none
	 * @return
	 *   true (there is a current element) or false (there is no current element at the moment)
	 **/
	public boolean hasCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of getCurrent()";
		if (precursor == null && head != null) {
			return true;
		} else if (precursor != null && precursor.next != null) {
			return true;
		} else {
			return false;
		}
		
		// This method shouldn't modify any fields, hence no assertion at end
	}

	/**
	 * Remove the current element from this sequence.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true.
	 * @postcondition
	 *   The current element has been removed from this sequence, and the 
	 *   following element (if there is one) is now the new current element. 
	 *   If there was no following element, then there is now no current 
	 *   element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   removeCurrent may not be called. 
	 **/
	public void removeCurrent( )
	{
		assert wellFormed() : "invariant wrong at start of removeCurrent()";
		
		if (hasCurrent() == false) {
			throw new IllegalStateException();
		} else if (precursor == null && head != null){
			head = head.next;
		} else {
			Node nextNode = precursor.next.next;
			precursor.next = nextNode;
		}
		manyNodes--;
		
		// See textbook pp.176-78, 181-184
		assert wellFormed() : "invariant wrong at end of removeCurrent()";
	}


	/**
	 * Determine the number of elements in this sequence.
	 * @param - none
	 * @return
	 *   the number of elements in this sequence
	 **/ 
	public int size()
	{
		assert wellFormed() : "invariant wrong at start of size()";
		return manyNodes;
	}


	/**
	 * Set the current element at the front of this sequence.
	 * @param - none
	 * @postcondition
	 *   The front element of this sequence is now the current element (but 
	 *   if this sequence has no elements at all, then there is no current 
	 *   element).
	 **/ 
	public void start( )
	{
		assert wellFormed() : "invariant wrong at start of start()";
		
		if (manyNodes == 0) {
			return;
		} else {
			precursor = null;
		}
		
		assert wellFormed() : "invariant wrong at end of start()";
	}

	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Node cursor = (precursor == null) ? head : precursor.next;
		sb.append("{");
		boolean first = true;
		for (Node p = head; p != null; p = p.next) {
			if (first) first = false;
			else sb.append(",");
			if (p == cursor) sb.append("*");
			sb.append(p.data);
		}
		sb.append("}");
		return sb.toString();

	}
	
	/**
	 * Move forward, so that the current element is now the next element in
	 * this sequence.
	 * @param - none
	 * @precondition
	 *   hasCurrent() returns true. 
	 * @postcondition
	 *   If the current element was already the end element of this sequence 
	 *   (with nothing after it), then there is no longer any current element. 
	 *   Otherwise, the new element is the element immediately after the 
	 *   original current element.
	 * @exception IllegalStateException
	 *   Indicates that there is no current element, so 
	 *   advance may not be called.
	 **/
	public void advance( )
	{
		assert wellFormed() : "invariant wrong at start of advance()";
		
		if (hasCurrent() == false) {
			throw new IllegalStateException();
		} else  if (precursor == null){
			precursor = head;
		} else {
			precursor = precursor.next;
		}
		
		assert wellFormed() : "invariant wrong at end of advance()";
	}
	
	/**
	 * Generate a copy of this sequence.
	 * @param - none
	 * @return
	 *   The return value is a copy of this sequence. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 *   Whatever was current in the original object is now current in the clone.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public SequentialGallery clone( )
	{  	 
		assert wellFormed() : "invariant wrong at start of clone()";

		SequentialGallery result = new SequentialGallery();;

		try
		{
			result = (SequentialGallery) super.clone( );
		}
		catch (CloneNotSupportedException e)
		{  
			// This exception should not occur. But if it does, it would probably
			// indicate a programming error that made super.clone unavailable.
			// The most common error would be forgetting the "Implements Cloneable"
			// clause at the start of this class.
			throw new RuntimeException
			("This class does not implement Cloneable");
		}
		if (this.manyNodes != 0) {
			result.manyNodes = this.manyNodes;
			Node cloneCursor = new Node(this.head.data, null);
			result.head = cloneCursor;
			Node cursor = this.head;
			if (this.head == this.precursor) {
				result.precursor = cloneCursor;
			}
			while (cursor.next != null) {
				cloneCursor.next = new Node(cursor.next.data, null);
				if (cursor.next == this.precursor) {
					result.precursor = cloneCursor.next;
				}
				cursor = cursor.next;
				cloneCursor = cloneCursor.next;
			}
		}
		
		
		// Now do the hard work of cloning the list.
		// See pp. 193-197, 228
		// Setting precursor correctly is tricky.

		assert wellFormed() : "invariant wrong at end of clone()";
		assert result.wellFormed() : "invariant wrong for result of clone()";
		return result;
	}
	
	public static class TestInvariant extends TestCase {
		
		protected SequentialGallery self;
		private Painting h1 = new Painting(new Raster(10, 10), "nice pixels", 100);
		private Painting h2 = new Painting(new Raster(10, 10), "extra nice pixels", 1000);
		
		@Override
		protected void setUp() {
			self = new SequentialGallery(false);
			//iterator = self.new MyIterator(false);
			//Gallery self = new Gallery();
			doReport = false;
		}
		
		public void testNull() {
			assertTrue("empty gallery allowed", self.wellFormed());
		}

		public void test00() {
			self.manyNodes = 1;
			assertFalse(self.wellFormed());
			self.manyNodes = 0;

			assertTrue(self.wellFormed());
		}
		
		public void test01() {
			self.head = new Node(h1,null);
			assertFalse(self.wellFormed());
			self.manyNodes = 2;
			assertFalse(self.wellFormed());
			self.manyNodes = 1;

			assertTrue(self.wellFormed());
		}
		
		public void test02() {
			self.head = new Node(h1,null);
			self.manyNodes = 1;
			self.precursor = new Node(h1,null);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h2,self.head);
			assertFalse(self.wellFormed());
			self.precursor = self.head;

			assertTrue(self.wellFormed());
		}
		
		public void test03() {
			self.head = new Node(h1,null);
			self.head.next = self.head;
			self.manyNodes = 1;
			assertFalse(self.wellFormed());
			self.manyNodes = 2;
			assertFalse(self.wellFormed());
			self.manyNodes = 3;
			assertFalse(self.wellFormed());
			self.manyNodes = 0;
			assertFalse(self.wellFormed());
			self.manyNodes = -1;
			assertFalse(self.wellFormed());
		}
		
		public void test05() {
			self.head = new Node(h1,null);
			self.head = new Node(h2,self.head);
			self.head = new Node(null,self.head);
			self.head = new Node(h2,self.head);
			self.head = new Node(h1,self.head);
			self.manyNodes = 1;
			assertFalse(self.wellFormed());
			self.manyNodes = 2;
			assertFalse(self.wellFormed());
			self.manyNodes = 3;
			assertFalse(self.wellFormed());
			self.manyNodes = 4;
			assertFalse(self.wellFormed());
			self.manyNodes = 0;
			assertFalse(self.wellFormed());
			self.manyNodes = -1;
			assertFalse(self.wellFormed());
			self.manyNodes = 5;

			assertTrue(self.wellFormed());
		}
		
		public void test06() {
			Node n1,n2,n3,n4,n5;
			self.head = n5 = new Node(h1,null);
			self.head = n4 = new Node(h2,self.head);
			self.head = n3 = new Node(null,self.head);
			self.head = n2 = new Node(h2,self.head);
			self.head = n1 = new Node(h1,self.head);
			self.manyNodes = 5;

			self.precursor = new Node(h1,null);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h1,n1);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h1,n2);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h2,n3);
			assertFalse(self.wellFormed());
			self.precursor = new Node(null,n4);
			assertFalse(self.wellFormed());
			self.precursor = new Node(h2,n5);
			assertFalse(self.wellFormed());
			
			self.precursor = n1;
			assertTrue(self.wellFormed());
			self.precursor = n2;
			assertTrue(self.wellFormed());
			self.precursor = n3;
			assertTrue(self.wellFormed());
			self.precursor = n4;
			assertTrue(self.wellFormed());
			self.precursor = n5;
			assertTrue(self.wellFormed());
		}
	}
}
