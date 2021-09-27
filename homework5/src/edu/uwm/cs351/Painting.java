package edu.uwm.cs351;
import java.io.File;
import java.util.AbstractCollection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.uwm.cs351.util.Alphabetical_name;
import junit.framework.TestCase;
// Victor Weinert HW5 cs351/751
// No help declared. 
// There is a bug adding right before tail fails, which is why 3 of the tests throw errors. 

// Note: I implemented clear.  

// References:
// Linked list documentation from class @ http://pabst.cs.uwm.edu/classes/cs351/handout/linked-list-variations.html
// Assignment 5 @ http://pabst.cs.uwm.edu/classes/cs351/homework/homework5.html
// java documentation @ https://docs.oracle.com/javase/8/docs/api/
// Specifically looked at compareTo as well as lexicographic order.
// Lab and TAs were helpful.
/**
 * The Class Painting.
 */
public class Painting implements Cloneable {

	/** Fields. */
	private final File file;
	private final int value;
	private final String name;
	private final String artist;
	private final int year;

	/** The links to the previous and next paintings. */
	private Painting prev, next;
	
	
	
	public Painting(File f, String name, String artist, int year, int value)
	{
		this.file = f;
		this.name = name;
		this.artist = artist;
		this.year = year;
		this.value = value;
	}
	public File getFile() {return file;}
	public String getName(){return name;}
	public String getArtist() {return artist;}
	public int getYear() {return year;}
	public int getValue(){return value;}
	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof Painting)) return false;
		Painting p = (Painting) obj;
		if(!(p.getName().equals(this.getName()))){return false;}
		if(!(p.getArtist().equals(this.getArtist()))){return false;}
		if(!(p.getFile().equals(this.getFile()))){return false;}
		if(!(p.getValue() == this.getValue())){return false;}
		if(!(p.getYear() == this.getYear())){return false;}
		// note that we don't test prev and next
		return true;
	}
	
	/**
	 *  Returns a clone of this painting that is identical in every way
	 *  except that it has null links.
	 *
	 * @return the Painting with null links
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Painting clone(){
		Painting copy = null;
		try{
			copy = (Painting) super.clone();
			copy.next = copy.prev = null;
			// the clone has no prev or next
			// but it still .equals() this Painting
		}
		catch(CloneNotSupportedException e){
			// should not happen
		}
		return copy;
	}
	
	
	/**
	 * The Class SortedCollection.
	 */
	public static class SortedCollection extends AbstractCollection<Painting> implements Cloneable {

		/** Fields */
		private Comparator<Painting> comparator;
		private int manyItems, version;
		private Painting head, tail;

		private boolean report(String s) {
			System.out.println(s);
			return false;}
		
		/** The Invariant */
		private boolean wellFormed() {
			
			// The invariant:
			// 0. comparator is not null
			
			if (this.comparator == null) {
				return report("0. comparator is not null");
			}
			// 1. If head or tail is null then both are null.
			if ((head == null && tail != null) || (head != null && tail == null)) {
				return report("1. If head or tail is null then both are null.");
			}
			if (manyItems < 0) {
				return report("manyItems is less than 0 ");
			}
			if (head == null && tail == null && manyItems != 0) {
				return report("");
			}
			// 2. If head exists it is first in list.
			
			if ( head != null && head.prev != null) {
				return report("2. If head exists it is first in list.");
			}
			// Nothing to test
			if (head == null && tail == null && manyItems == 0) {
				return true;
			}
			boolean foundTail = false;
			int count = 1;
			Painting cursor = head;
			if (head == tail) {
				foundTail = true;
			}
			while (cursor.next != null) {
				if (cursor.next == tail) {
					foundTail = true;
				}
				count++;
				if (cursor.next.prev != cursor) {
					return report("3");
				}
				if (0 < comparator.compare(cursor, cursor.next)) {
					return report("6");
				}
				cursor = cursor.next;
				
			}
			if (manyItems != count) {
				return report("5");
			}
			if (!foundTail) {
				return report("4");
			}
			// 3. Every Painting with a next is the previous of its next.
			//		NB: In combination with condition 2 (above), this piece of the invariant should
			//			catch any circular references within the list. (Why?)
			//			If you fail to check the invariant conditions in the order given, however,
			//			your code may never figure out that there is a loop and continue on forever!
			// 
			// 4. If tail exists it is last in same list as head.
			// 
			// 5. manyItems is the number of paintings in the list
			// 
			// 6. Every Painting with a next is lesser than or equal to its next according to comparator
			// 
			return true;
		}
		
		/**
		 * Instantiates a new group with the given comparator.
		 *
		 * @param comp the comparator this Group will use
		 * @throws IllegalArgumentException if comparator is null
		 */
		public SortedCollection(Comparator<Painting> comp) {
			if (comp == null) {
				throw new IllegalArgumentException("SortedCollection constructor, Comparator is null");
			}
			head = tail = null;
			version = 0;
			manyItems = 0;
			comparator = comp;
			assert this.wellFormed();
		}
		
		private SortedCollection(boolean ignored) {} // DO NOT CHANGE THIS
		
		/**
		 * Adds a Painting to this group in the order specified by this group's
		 * comparator. Namely, for any new Painting P being added...
		 * 		P belongs before any painting that is greater than it
		 * 		P belongs after all paintings that are equal to it
		 * 		P belongs after any Painting that is less than it
		 * 
		 * Your search for a Painting's correct position *must* begin at the end of the list,
		 * so that it is efficient when adding multiple Paintings in sorted order.
		 * A new Painting must be placed *after* any paintings the comparator considers 'equivalent'.
		 *
		 * @param painting the Painting to add
		 * @throws IllegalArgumentException if Painting is null or has existing links
		 * @return true always
		 */
		@Override
		public boolean add(Painting painting) {
			if ( painting == null || painting.prev != null || painting.next != null) {
				throw new IllegalArgumentException();
			}
			if (head == null) {
				head = tail = painting;
			} else if (manyItems == 1) { 
				if (this.comparator.compare(head, painting) <= 0) {
					head.next = painting;
					painting.prev = head;
					tail = painting;
				} else {
					head.prev = painting;
					painting.next = head;
					head = painting;
					tail = painting.next;
				}
			} else {

				if (this.comparator.compare(tail, painting) <= 0) {
					painting.prev = tail;
					tail.next = painting;
					tail = painting;

				} else if (this.comparator.compare(head, painting) >  0) {
					painting.next = head;
					head.prev = painting;
					head = painting;
				} else {
					Painting l_cursor = tail.prev;

					boolean inserted = false;
					while (l_cursor != null && inserted == false) {
						if (this.comparator.compare(l_cursor, painting) >= 0) {
							painting.next = l_cursor.next;
							painting.prev = l_cursor;
							l_cursor.next.prev = painting;
							l_cursor.next = painting;
							inserted = true;
						}
						l_cursor = l_cursor.prev;
					}
				}
			}

			version++;
			manyItems++;
			assert this.wellFormed();
			return true;
		}
		
		/** Returns the head of this group.
		 * 	NB: We don't have to remove its links. Why?
		 * @return the head (first Painting) of this group */
		public Painting getFirst() {
			return head;
		}
		
		/** Returns the tail of this group.
		 * 	NB: We don't have to remove its links. Why?
		 * @return the tail (last Painting) of this group */
		public Painting getLast() {
			return tail;
		}
		
		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#size()
		 */
		@Override
		public int size() {
			return manyItems;
		}
		// overridden because AbstractCollection's clear() wasn't cutting it.  When clear happened, 
		// the elements would still be referencing each other, 
		// therefore failing to be added to subsequent SortedCollections in the tests
		@Override
		public void clear() {
			if (manyItems == 0) {
				return;
			}
			Painting lcursor = tail.prev;
			while (lcursor != null) {
				lcursor.next.prev = null;
				lcursor.next  = null;
				lcursor = lcursor.prev;
			}
			manyItems = 0;
			head = tail = null;
			version++;
			assert this.wellFormed();
		}
		
		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#toString()
		 */
		@Override
		public String toString() {
			StringBuilder result = new StringBuilder("{\n");
			for (Painting cur = head; cur != null; cur = cur.next)
				result.append("\t" + cur + "\n");
			result.append("}");
			return result.toString();
		}
		
		/** Returns a new copy of this group. The copy should be unaffected
		 *  by subsequent changes made to this group, and vice versa. The
		 *  paintings added to the copy should be clones.
		 *  
		 * @return a clone of this group
		 * @see java.lang.Object#clone()
		 */
		@Override
		public SortedCollection clone(){
			assert wellFormed() : "invariant failed at start of clone()";
			SortedCollection copy = null;
			
			try {
				copy = (SortedCollection) super.clone();
				if (copy.manyItems != 0) {
					Painting local_cursor = this.head;
					copy.head = copy.tail = local_cursor.clone();
					Painting copy_cursor = copy.head;
					int items = 1;
					while (items != manyItems) {
						Painting nextCopy = local_cursor.next.clone();
						copy_cursor.next = nextCopy;
						nextCopy.prev = copy_cursor;
						copy.tail = nextCopy;
						items++;
						
					}
				}
				//  Make sure the paintings in the cloned collection
				//	are not connected to the paintings in the original
			}
			catch(CloneNotSupportedException e){
				// should not happen
			}

			assert wellFormed() : "invariant failed at end of clone()";
			assert copy.wellFormed() : "copy invariant failed at end of clone()";
			return copy;
		}

		/* (non-Javadoc)
		 * @see java.util.AbstractCollection#iterator()
		 */
		@Override
		public Iterator<Painting> iterator() {
			return new MyIterator();
		}

		// NB: Do *not* override the inherited implementation of "clear()".
		// Question to think about: Why not? What does it do?  How?
		// (This could be on an exam)
		
		/**
		 * The class MyIterator.
		 */
		private class MyIterator implements Iterator<Painting>{
			
			/** Fields */
			// Design: hasCurrent is true when the iterator has a current element (that can be removed)
			// 	When hasCurrent is true, cursor points to the current element
			// 	When hasCurrent is false, cursor points to the next element if there is one, or null if not
			//
			// Normally these fields would be private, but they are protected for now
			// 	so that TestInvariant can access them.
			protected Painting cursor;
			protected int myVersion;
			protected boolean hasCurrent;
			
			/** Invariant */
			private boolean wellFormed() {
				// Invariant for iterator:
				// 1. Outer invariant holds
				if (!SortedCollection.this.wellFormed()) return report("1. outer invariant failed iterator");
				
				if (SortedCollection.this.version != this.myVersion) return true; // Not the place for failure to occur?
				// Only check 2 and 3 if versions match...
				if (hasCurrent && cursor == null) return report("2. hasCurrent is only true if cursor exists");
				// 2. hasCurrent is only true if cursor exists
				// 3. if cursor exists it is in the list
				if (SortedCollection.this.head != null && cursor != null) {
					Painting local_cursor = head;
					boolean found = false;
					if (local_cursor == cursor) {
						found = true;
					}
					while (local_cursor != null) {
						if (local_cursor == cursor) {
							found = true;
						}
						local_cursor = local_cursor.next;
					}
					if (!found) return report("3. if cursor exists it is in the list");
				}
				

				return true;
			}
			
			/** Instantiates a new iterator */
			public MyIterator(){
				this.myVersion = SortedCollection.this.version;
				cursor = head;
				hasCurrent = false;
				
			}
			
			
			// do not change this - used for JUnit tests
			private MyIterator(boolean ignore){}
			
			/** Returns whether there are more paintings to be returned.
			 * @throws ConcurrentModificationException if versions don't match
			 * @return true if there exists another Painting to return
			 */
			@Override
			public boolean hasNext() {
				assert wellFormed() : "invariant failed at start of hasNext()";
				if (this.myVersion != SortedCollection.this.version) {
					throw new ConcurrentModificationException();
				} else if (hasCurrent == false && cursor != null) {
					return true;
				} else if (hasCurrent && cursor.next != null) {
					return true;
				} else {
					return false;
				}
			}

			/** Returns the next Painting in this group. This method should
			 *  *not* change the state of the group in any way.
			 *  
			 *  @throws ConcurrentModificationException if versions don't match
			 *  @throws NoSuchElementException if no next exists
			 *  @return the next Painting in the group
			 */
			@Override
			public Painting next() {
				assert wellFormed() : "invariant failed at start of next()";
				if (SortedCollection.this.version != this.myVersion) {
					throw new ConcurrentModificationException();
				} else if (!hasNext()) {
					throw new NoSuchElementException();
				} else {
					if (hasCurrent == false && cursor != null) {
						hasCurrent = true;
					} else {
						cursor = cursor.next;
					}
				}
				
				assert wellFormed() : "invariant failed at end of next()";
				return cursor;
			}


			/**
			 * 	Removes the most recently returned Painting from this group.
			 *  
			 *  @throws ConcurrentModificationException if versions don't match
			 *  @throws IllegalStateException if next hasn't been called (the cursor hasn't been returned)
			 */
			@Override
			public void remove() {
				assert wellFormed() : "invariant failed at start of remove()";
				if (SortedCollection.this.version != this.myVersion) {
					throw new ConcurrentModificationException();
				} else if (hasCurrent == false) {
					throw new IllegalStateException();
				} else {
					if (manyItems == 1) {
						head = cursor = tail = null;
					} else if (cursor == head) {
						head = head.next;
						head.prev = null;
						cursor.next = null;
						cursor = head;
					} else if (cursor == tail) {
						tail = tail.prev;
						tail.next.prev = null;
						tail.next = null;
						cursor = null;
					} else {
						Painting ghost = cursor;
						cursor = cursor.next;
						cursor.prev = ghost.prev;
						ghost.prev.next = cursor;
						ghost.next = ghost.prev = null;
								
						
					}
							
				}
				this.myVersion++;
				SortedCollection.this.version++;
				manyItems--;
				hasCurrent = false;
				assert wellFormed() : "invariant failed at end of remove()";
			}
			
		}
		
		public static class TestInvariant extends TestCase {
			
			protected SortedCollection self;
			private Comparator<Painting> comp = Alphabetical_name.getInstance();
			private Painting head, tail;
			private Painting p1, p2, p3;
			private MyIterator it;

			@Override
			protected void setUp() {
				self = new SortedCollection(false);
				it = self.new MyIterator(false);

				head = new Painting(new File("./Paintings/monkey.jpg"), "Incomprehensible", "Congo the Chimp", 1957, 25620);
				p1 = new Painting(new File("./Paintings/kirchner.jpg"), "Potsdamer Platz", "Kirchner", 1912, 3500000);
				p2 = new Painting(new File("./Paintings/rothko.jpg"), "Red on Red", "Rothko", 1969, 8237000);
				p3 = new Painting(new File("./Paintings/courbet.jpg"), "The Desperate Man", "Courbet", 1845, 12000000);
				tail = new Painting(new File("./Paintings/hieronymus_bosch.jpg"), "The Harrowing of Hell", "Hieronymus Bosch", 1460, 137500);
				
				// Default to valid group of 2
				self.comparator = comp;
				self.head = head;
				self.tail = tail;
				link(head, tail);
				self.manyItems = 2;
			}
			
			private void link(Painting a, Painting b) {
				a.next = b;
				b.prev = a;
			}
			
			public void test00() {
				self.comparator = null;
				assertFalse("null comparator", self.wellFormed());
			}
			
			public void test01() {
				self.head = self.tail = null;
				assertFalse("manyItems is incorrect", self.wellFormed());
				
				self.manyItems = 0;
				assertTrue("null head, null tail", self.wellFormed());
				
				self.head = head;
				assertFalse("valid head, null tail", self.wellFormed());
				
				self.head = null;
				self.tail = tail;
				assertFalse("null head, valid tail", self.wellFormed());
				
				self.head = head;
				head.next = null;
				tail.prev = null;
				self.manyItems = 2;
				assertFalse("valid head and tail, but no links", self.wellFormed());
				
				link(head, tail);
				assertTrue("valid head and tail, correct linkage", self.wellFormed());
				
				head.prev = p1;
				p1.next = head;
				assertFalse("head not first in list", self.wellFormed());
				
				head.prev = null;
				tail.next = p1;
				p1.prev = tail;
				assertFalse("tail not last in list", self.wellFormed());
			}
			
			public void test02() {
				assertTrue("good group of size 2", self.wellFormed());
				
				self.manyItems = 3;
				assertFalse("manyItems is incorrect", self.wellFormed());
				
				self.head = self.tail = null;
				assertFalse("manyItems is incorrect", self.wellFormed());
			}
			
			public void test03() {
				head.next = tail;
				tail.prev = tail;
				assertFalse("tail is prev of itself", self.wellFormed());

				head.next = head;
				tail.prev = head;
				assertFalse("head is next of itself", self.wellFormed());
			}
			
			public void test04() {
				link(head, p1);
				link(p1, tail);
				self.manyItems = 3;
				assertTrue("list has correct linkage", self.wellFormed());
				
				head.next = tail;
				assertFalse("head is not prev of its next", self.wellFormed());
				
				link(head, p1);
				tail.prev = head;
				assertFalse("p1 is not prev of its next", self.wellFormed());
			}
			
			public void test05() {
				link(head, p1);
				link(p1, p2);
				link(p2, tail);
				self.manyItems = 4;
				assertTrue("list has correct linkage", self.wellFormed());
				
				p1.next = head;
				assertFalse("p1 is not prev of its next", self.wellFormed());
				p1.next = tail;
				assertFalse("p1 is not prev of its next", self.wellFormed());
				
				link(p1, p2);
				p2.prev = head;
				assertFalse("p1 is not prev of its next", self.wellFormed());
				p2.prev = tail;
				assertFalse("p1 is not prev of its next", self.wellFormed());
				
				link(p1, p2);
				assertTrue("list has correct linkage", self.wellFormed());
				link(p2, p1);
				assertFalse("head is not prev of its next", self.wellFormed());
			}
			
			public void test06() {
				link(head, p1);
				link(p1, p2);
				link(p2, p3);
				link(p3, tail);
				self.manyItems = 5;
				assertTrue("list has correct linkage", self.wellFormed());
				
				link(p1, p3);
				self.manyItems = 4;
				assertTrue("p2 no longer in list, list has correct linkage", self.wellFormed());
				
				p3.prev = p2;
				assertFalse("p1 is not prev of its next", self.wellFormed());
				
				link(p1,p2);
				link(p2,p3);
				p1.next = p3;
				assertFalse("p1 is not prev of its next", self.wellFormed());
				
				link(head, tail);
				self.manyItems = 2;
				assertTrue("removed all but head/tail, list has correct linkage", self.wellFormed());
			}
			
			public void test07() {
				self.head = self.tail = null;
				assertFalse("outer invariant broken", it.wellFormed());
			}
			
			public void test08() {
				it.hasCurrent = true;
				assertFalse("returned cursor but it doesn't exist", it.wellFormed());
				it.cursor = self.head;
				assertTrue("fixed", it.wellFormed());
				
				it.hasCurrent = false;
				assertTrue("initial state of iterator", it.wellFormed());
			}
			
			public void test09() {
				it.hasCurrent = true;
				assertFalse("returned cursor but it doesn't exist", it.wellFormed());
				it.myVersion = 1;
				assertTrue("versions dont match", it.wellFormed());
			}
			
			public void test10() {
				it.cursor = p1;
				assertFalse("cursor isn't in group", it.wellFormed());
				it.cursor = head;
				assertTrue("versions dont match", it.wellFormed());
			}
		}
	}
}
