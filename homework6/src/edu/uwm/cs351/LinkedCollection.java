package edu.uwm.cs351;


import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.xml.soap.Node;

import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM
// HW #6 Victor Weinert
// No help declared

// Resources used:
// https://docs.oracle.com/javase/8/docs/api/
// https://en.wikipedia.org/wiki/Generics_in_Java
// https://docs.oracle.com/javase/tutorial/java/generics/types.html
// http://pabst.cs.uwm.edu/classes/cs351/homework/homework6.html
// http://pabst.cs.uwm.edu/classes/cs351/handout/linked-list-variations.html
// https://en.wikipedia.org/wiki/Linked_list
// http://opendatastructures.org/versions/edition-0.1g/ods-python/3_Linked_Lists.html
// http://cslibrary.stanford.edu/103/
// CS351 Textbook
// Cs351 Lab6

/**
 * A cyclic doubly-linked list implementation of the Java Collection interface
 *  You will be required to override all of the collection methods aside from retainAll() and
 *  the overloaded method toArray(T[]). Note that toArray() will still need to be completed.
 *  All these methods should be declared "@Override".
 * <p>
 * The data structure is a cyclic doubly-linked list with one dummy node.
 * The fields should be:
 * <dl>
 * <dt>count</dt> Number of true elements in the collection.
 * <dt>version</dt> Version number (used for fail-fast semantics).
 *  <dt>dummy</dt> A reference to the dummy node.
 * </dl>
 * It should be cyclicly linked without any null pointers.
 * The code should have very few special cases (if statements regarding the data structure).
 * The class should define a private {@link #wellFormed()} method
 * and perform assertion checks in each method.
 * You should use a version stamp to implement <i>fail-fast</i> semantics
 * for the iterator.
 * @param <E>
 */
@SuppressWarnings({ "rawtypes", "unused", "unchecked" })
public class LinkedCollection<E> implements Collection<E> {


	/** 
	 * A data class used for the linked structure for the linked list implementation of LinkedCollection
	 */
	// Declare the private static generic Node class with fields data, prev and next.
	// The class should be private, static and generic.
	// Please use a different name for its generic type parameter Node<> <--
	// It should have a constructor or two (at least the default constructor) but no methods.
	// The no-argument constructor can construct a dummy node if you would like.
	// The fields of Node should have "default" access (neither public, nor private)
	// Remember the dummy node should have a type-cast reference to itself for its data
	// So we should have dummy.data == dummy
	
	private static class Node<T> {
		
		Node next, prev;
		T data;
		
		public Node (T data, Node next, Node prev) {
			this.data = data; //need to double check perfect understanding
			this.next = next;
			this.prev = prev;
		}
		
		public Node () {
			this.data = (T)this;
			this.prev = this;
			this.next = this;
		}
		
	}
	
	private int count;
	private int version;
	private Node dummy;


	// Declare the private fields of Sequences:
	// One for the dummy, one for the size, one for version
	// Do not declare any other fields.
	private LinkedCollection(boolean ignored) {} // DO NOT CHANGE THIS

	private boolean report(String s) {
		System.out.println(s);
		return false;
	}

	private boolean wellFormed() {
		
		if (dummy == null || dummy.data != dummy || dummy.prev == null || dummy.next == null) {
			return report("1. plus dummy tests");
		}
		
		
		if (dummy.next.prev != dummy || dummy.prev.next != dummy) {
			return report("links to outside of list, dummy");
		}
		
		Node cursor = dummy.next;
		int counter = 0;
		
		while (cursor != dummy) {
			counter++;
			if (cursor.next == null || cursor.prev == null) {
				return report("null links");
			}
			if (cursor.next.prev != cursor || cursor.prev.next != cursor) {
				return report("links to outside of list");
			}
			if (counter > count) {
				return report ("3/4");
			}
			cursor = cursor.next;
		}
		
		if (counter != count ) return report("3.");
		// 1. dummy node is not null.  Its data should be itself, cast (unsafely) data = (T)this;.
		// 2. each link must be correctly double linked.
		// 3. size is number of nodes in list, other than the dummy.
		// 4. the list must cycle back to the dummy node.
		// Implement multiple checks
		// The solution does all the checks with only one loop.
		// In particular, if you check "prev" links, you can avoid getting stuck
		// in an illegal cycle (one that doesn't contain the dummy)

		// If no problems found, then return true:
		return true;
	}
	public LinkedCollection() {
		
		count = 0;
		dummy = new Node();
		version = 0;
		
		assert wellFormed() : "invariant failed in constructor";
	}

	@Override
	public boolean add(E x) {
		assert wellFormed() : "invariant broken at start of add()";
		
		Node newNode = new <E>Node(x, dummy, dummy.prev);
		dummy.prev.next = newNode;
		dummy.prev  = newNode;
		
		count++;
		version++;
		
		assert wellFormed() : "invariant broken at end of add()";
		
		return true;
	}

	@Override
	public void clear() {
		assert wellFormed() : "invariant broken at start of clear()";
		
		if (count > 0) {
			dummy.next = dummy.prev = dummy;
			
			count = 0;
			version++;
			
			assert wellFormed() : "invariant broken at end of clear()";
		}
		return;
	}

	@Override
	public int size() {
		assert wellFormed() : "invariant broken at start of size()";
		
		return count;
	}
	
	@Override
	public String toString() {
		return "{Collection of size: " + Integer.toString(size()) + "}";
	}

	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		assert wellFormed() : "invariant broken at start of addAll()";
		
		boolean modified = false;
		
		if (!c.isEmpty()) {
			modified = true;
			if (this != c) {
				
				Iterator it = c.iterator();
				while (it.hasNext()) {
					this.add((E) it.next());
				}

			} else {
				Object[] myArray = this.toArray();
				for (int i = 0; i < myArray.length; i++) {
					this.add((E) myArray[i]);
				}
			}
		}
		
		if (modified) {
			version++;
			assert wellFormed() : "invariant broken at end of addAll()";
		}
		
		return modified;
	}

	@Override
	public boolean contains(Object o) {
		assert wellFormed() : "invariant broken at start of contains()";
		
		Node cursor = dummy.next;
		
		while (cursor != dummy) {
			if (o == null && cursor.data == null) {
				return true;
			} else if (cursor.data != null && cursor.data.equals(o)) {
				return true;
			}
			
			cursor = cursor.next;
		}
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		assert wellFormed() : "invariant broken at start of containsAll()";
		
		Iterator it = c.iterator();
		boolean foundAll = true;
		
		while (it.hasNext()) {
			E thing = (E) it.next();
			if (this.contains(thing)) {
				foundAll = true;
			} else {
				return false;
			}
		}
		
		return foundAll;
	}

	@Override
	public boolean isEmpty() {
		assert wellFormed() : "invariant broken at start of isEmpty()";
		
		return count == 0;
	}

	@Override
	public boolean remove(Object o) {
		assert wellFormed() : "invariant broken at start of remove()";
		
		Node cursor = dummy.next;
		boolean success = false;
		
		while (cursor != dummy && success == false) {
			if (cursor.data == null && o != null || cursor.data != null && o == null) {
				
			} else if (o == null && cursor.data == null || cursor.data.equals(o)) {
				cursor.prev.next = cursor.next;
				cursor.next.prev = cursor.prev;
				version++;
				count--;
				success = true;
			}
			cursor = cursor.next;
		}
		
		assert wellFormed() : "invariant broken at end of remove()";
		
		return success;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		assert wellFormed() : "invariant broken at start of removeAll()";
		
		if (c.isEmpty()) return false;
		
		if (c == this) {
			this.clear();
			assert wellFormed() : "invariant broken at end of removeAll()";
			return true;
		}
		
		boolean removal = false;
		Iterator<E> it1 = (Iterator<E>) c.iterator();
		
		while (it1.hasNext()) {
			E stuff = (E) it1.next();
			while (this.remove(stuff)) {
				removal = true;
			}
		}
		
		assert this.wellFormed();
		
		return removal;
	}


	@Override
	public Object[] toArray() {
		assert wellFormed() : "invariant broken at start of toArray()";
		E[] myArray = (E[])new Object[count];
		
		Iterator<E> it = this.iterator();	
		int i = 0;
		
		while (it.hasNext()) {
			myArray[i] = it.next();
			i++;
		}
		
		return myArray;
	}

	//Java collection requires these two methods,
	//but they do not need to be completed for this assignment
	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}
	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}


	//The Iterator
	@Override
	public Iterator<E> iterator() {
		assert wellFormed() : "invariant broken at start of iterator()";
		return new MyIterator();
	}


	// You need a nested (not static) class to implement the iterator:
	private class MyIterator implements Iterator<E> {
		
		int myVersion;
		boolean hasCurrent;
		Node precursor;
		
		// declare fields: myVersion, precursor, hasCurrent
		// Normally these should be private, but omit "private" so
		// that TestInvariant can access them.

		MyIterator(boolean ignored) {} // DO NOT CHANGE THIS

		private boolean wellFormed() {
			// Invariant for recommended fields:
			// 0. The outer invariant holds, and versions match
			// 1. precursor is never null
			// 2. precursor is in the list
			// 3. if precursor is before the dummy, hasCurrent must be false ?what?
			// NB: Don't check 1,2 unless the version matches.
			// 0.
			if (!LinkedCollection.this.wellFormed()) return false;
			if (myVersion == version) {
				// 1.
				if (precursor == null) {
					return report("1. Iterator");
				} 
				
				boolean found = false;
				Node cursor = dummy;
				
				if (precursor == dummy) found = true;
				
				while (cursor.next != dummy) {
					if (cursor.next == precursor) {
						found = true;
					}
					cursor = cursor.next;
				}
				
				if (found == false) return report("2");
				if (precursor.next == dummy && hasCurrent) {
					return report("3");
				}
				
				// Implement checks 1, 2, and 3
			}

			return true;
		}

		public MyIterator() {
			hasCurrent = false; 
			precursor = dummy;
			myVersion = version;
			
			assert wellFormed() : "invariant fails in iterator constructor";
		}


		public boolean hasNext() {
			assert wellFormed() : "invariant fails at start of hasNext()";
			
			if (myVersion != version) {
				throw new ConcurrentModificationException();
			}
			
			if (precursor.next != dummy) {
				return true;
			} else {
				hasCurrent = false;
				return false;
			}
		}

		public E next() {
			assert wellFormed() : "invariant fails at start of next()";
			
			if (hasNext() == false ) {
				throw new NoSuchElementException();
			} else {
				
				precursor = precursor.next;
				
				assert wellFormed() : "invariant fails at end of next()";
				
				return (E) precursor.data;
			}
		}

		public void remove() {
			assert wellFormed() : "invariant fails at start of remove()";
			
			if (version != myVersion ) {
				throw new ConcurrentModificationException();
			}
			
			if (precursor == dummy) {
				throw new IllegalStateException();
			}
			
			precursor = precursor.prev;
			precursor.next.next.prev = precursor;
			precursor.next = precursor.next.next;
			
			count--;
			version++;
			myVersion++;
			
			assert wellFormed() : "invariant fails at end of remove()";
		}
		
		// Implement constructor and methods.
		// check for invariant in every method
		// check for concurrent modification exception in every method
		// no method should have loops (all constant-time operations).
	}

	public static class TestInvariant extends TestCase {
		private LinkedCollection<String> self;
		protected LinkedCollection<String>.MyIterator iterator;
		String e1 = "e1", e2 = "e2", e3 = "e3", e4 = "e4", e5 = "e5";
		private Node<String> d0, d1, d2;
		private Node<String> n0, n1, n2, n3, n4, n5, n1a, n2a, n3a, n4a, n5a;

		protected <T> Node<T> makeDummy() {
			Node<T> n = new Node<T>();
			n.prev = n;
			n.next = n;
			n.data = (T)n;
			return n;
		}

		protected <T> Node<T> makeNode(T data) {
			Node<T> n = new Node<T>();
			n.prev = null;
			n.next = null;
			n.data = data;
			return n;
		}

		@Override
		protected void setUp() {
			self = new LinkedCollection<String>(false);
			iterator = self.new MyIterator(false);
			d0 = makeDummy();
			d1 = makeDummy();
			d2 = makeDummy();
			n0 = makeNode(null);
			n1 = makeNode("one");
			n2 = makeNode("two");
			n3 = makeNode("three");
			n4 = makeNode("four");
			n5 = makeNode("five");
			n1a = makeNode("one");
			n2a = makeNode("two");
			n3a = makeNode("three");
			n4a = makeNode("four");
			n5a = makeNode("five");
		}

		private <T> void assignData(Node<T> n, Object data) {
			n.data = (T) data;
		}

		@SafeVarargs
		private final void linkUp(Node<String> dummy, Node<String>... nodes) {
			Node<String> last = dummy;
			for (Node<String> n : nodes) {
				n.prev = last;
				last.next = n;
				last = n;
			}
			dummy.prev = last;
			last.next = dummy;
			self.dummy = dummy;
			self.count = nodes.length;
			assertTrue(self.wellFormed());
		}

		public void test00() {
			assertFalse(self.wellFormed());
			self.dummy = d0;
			self.count = 0;
			d0.prev = null;
			assertFalse(self.wellFormed());
			d0.prev = d0;
			d0.next = null;
			assertFalse(self.wellFormed());
			d0.next = d0;
			d0.data = null;
			assertFalse(self.wellFormed());
			d0.data = "Dummy";
			assertFalse(self.wellFormed());
			self.dummy = d1;
			self.count = 1;
			assertFalse(self.wellFormed());
			self.count = 0;
			assertTrue(self.wellFormed());
		}

		public void test10() {
			linkUp(d0,n0);
			self.count = 0;
			assertFalse(self.wellFormed());
			self.count = -1;
			assertFalse(self.wellFormed());
			self.count = 2;
			assertFalse(self.wellFormed());
		}

		public void test11() {
			linkUp(d1,n1);
			assignData(d1,null);
			assertFalse(self.wellFormed());
			assignData(d1,d0);
			assertFalse(self.wellFormed());
			assignData(d1,n1);
			assertFalse(self.wellFormed());
		}

		public void test12() {
			linkUp(d2,n2);
			d2.next = null;
			assertFalse(self.wellFormed());
			d2.next = d2;
			assertFalse(self.wellFormed());
		}

		public void test13() {
			linkUp(d0,n3);
			n3.next = null;
			assertFalse(self.wellFormed());
			n3.next = d1;
			d1.prev = n3;
			d1.next = n3;
			assertFalse(self.wellFormed());
			n3.next = n3;
			assertFalse(self.wellFormed());
			n3.prev = n3;
			assertFalse(self.wellFormed());
		}

		public void test14() {
			linkUp(d1,n1);
			n1.prev = d0;
			d0.next = n1;
			d0.prev = n1;
			assertFalse(self.wellFormed());
			n1.prev = null;
			assertFalse(self.wellFormed());	
			n1.prev = n1;
			assertFalse(self.wellFormed());	
		}

		public void test15() {
			linkUp(d2,n2);
			d2.prev = null;
			assertFalse(self.wellFormed());
			d2.prev = n2a;
			n2a.next = d2;
			n2a.prev = d2;
			assertFalse(self.wellFormed());
			d2.prev = d2;
			assertFalse(self.wellFormed());
		}

		public void test16() {
			linkUp(d0,n5);
			iterator.precursor = null;
			assertFalse(iterator.wellFormed());
			iterator.precursor = d1;
			d1.next = n5;
			d1.prev = n5;
			assertFalse(iterator.wellFormed());
			iterator.precursor = n5a;
			n5a.next = d0;
			n5a.prev = d0;
			assertFalse(iterator.wellFormed());

			iterator.precursor = n5;
			assertTrue(iterator.wellFormed());
		}

		public void test20() {
			linkUp(d0,n1,n2);
			self.count = 0;
			assertFalse(self.wellFormed());
			self.count = 1;
			assertFalse(self.wellFormed());
			self.count = 3;
			assertFalse(self.wellFormed());
		}

		public void test21() {
			linkUp(d1,n3,n4);
			d1.prev = null;
			assertFalse(self.wellFormed());
			d1.prev = n4a;
			n4a.next = d1;
			n4a.prev = n3;
			assertFalse(self.wellFormed());
			d1.prev = n3;
			assertFalse(self.wellFormed());
			d1.prev = d1;
			assertFalse(self.wellFormed());
		}

		public void test22() {
			linkUp(d2,n5,n0);
			n5.prev = null;
			assertFalse(self.wellFormed());
			n5.prev = n0;
			assertFalse(self.wellFormed());
			n5.prev = n5;
			assertFalse(self.wellFormed());
			n5.prev = d0;
			d0.next = n5;
			d0.prev = n0;
			assertFalse(self.wellFormed());
		}

		public void test23() {
			linkUp(d1,n4,n5);
			n5.prev = null;
			assertFalse(self.wellFormed());
			n5.prev = n4a;
			n4a.next = n5;
			n4a.prev = d1;
			assertFalse(self.wellFormed());
			n5.prev = d1;
			assertFalse(self.wellFormed());
		}

		public void test24() {
			linkUp(d0,n1,n2);
			n1.next = n1;
			assertFalse(self.wellFormed());
			n1.next = null;
			assertFalse(self.wellFormed());
			n1.next = d0;
			assertFalse(self.wellFormed());
		}

		public void test25() {
			linkUp(d2,n0,n1);
			n1.next = n1;
			assertFalse(self.wellFormed());
			n1.next = d0;
			d0.prev = n1;
			d0.next = n0;
			assertFalse(self.wellFormed());
			n1.next = n0;
			assertFalse(self.wellFormed());
		}

		public void test26() {
			linkUp(d0,n2,n3);
			iterator.precursor = d1;
			d1.next = n2;
			d1.prev = n3;
			assertFalse(iterator.wellFormed());
			iterator.precursor = n2a;
			n2a.prev = d0;
			n2a.next = n3;
			assertFalse(iterator.wellFormed());
			iterator.precursor = n3a;
			n3a.prev = n2;
			n3a.next = d0;
			iterator.precursor = null;
			assertFalse(iterator.wellFormed());
		}

		public void test30() {
			linkUp(d1,n2,n3,n4);
			self.count = 2;
			assertFalse(self.wellFormed());
			self.count = -3;
			assertFalse(self.wellFormed());
			self.count = 4;
			assertFalse(self.wellFormed());
		}

		public void test31() {
			linkUp(d2,n5,n4,n3);
			n3.next = n5;
			assertFalse(self.wellFormed());
			n3.next = d0;
			d0.next = n5;
			d0.prev = n3;
			assertFalse(self.wellFormed());
			n3.next = n4;
			assertFalse(self.wellFormed());
		}

		public void test50() {
			linkUp(d0,n1,n2,n3,n4,n5);
			assignData(d0,null);
			assertFalse(self.wellFormed());
		}

		public void test51() {
			linkUp(d0,n1,n2,n3,n4,n5);
			d0.next = n1a;
			n1a.prev = d0;
			n1a.next = n2;
			assertFalse(self.wellFormed());
		}

		public void test52() {
			linkUp(d0,n1,n2,n3,n4,n5);
			n4.prev = n3a;
			n3a.next = n4;
			n3a.prev = n2;
			assertFalse(self.wellFormed());
		}

		public void test53() {
			linkUp(d0, n1, n3);
			assertFalse("null cursor",iterator.wellFormed());
			++self.version;
			assertTrue("version bad",iterator.wellFormed());
			iterator.precursor = self.dummy;
			++iterator.myVersion;
			assertTrue("cursor OK",iterator.wellFormed());
			iterator.precursor = n3;
			iterator.hasCurrent = true;
			assertFalse("cannot remove dummy",iterator.wellFormed());
		}


		public void test54() {
			linkUp(d0, n1, n2);
			n2.prev = self.dummy;
			iterator.precursor = n1;
			assertFalse("outer wrong", iterator.wellFormed());
		}
	}


}
