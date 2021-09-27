package edu.uwm.cs351;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

// This is a Homework Assignment for CS 351 at UWM
// Victor Weinert HW3, no help declared
// Referenced handout, java documentation to reference AbstractCollection. 
// Little frustrated about the inefficiency of my iterator's remove() method.  Looking forward to seeing a more efficient solution.
/**
 * An array implementation of the Java Collection interface
 * We use java.util.AbstractCollection to implement most methods.
 * This class will not allow null elements or duplicate elements.

 * You will  be required to override the abstract methods clear(), add(Painting)
 * size(), and iterator().  All these methods should be declared "@Override".
 * 
 * The data structure is a dynamic sized array.
 * The fields should be:
 * <dl>
 * <dt>data</dt> The data array.
 * <dt>manyItems</dt> Number of true elements in the collection.
 * <dt>version</dt> Version number (used for fail-fast semantics)
 * </dl>
 * The class should define a private wellFormed() method
 * and perform assertion checks in each method.
 * You should use a version stamp to implement <i>fail-fast</i> semantics
 * for the iterator.
 */
public class Gallery extends AbstractCollection<Painting> implements Collection<Painting>, Iterable<Painting>, Cloneable{

	/** Static Constants */
	private static final int INITIAL_CAPACITY = 1;

	/** Collection Fields */
	private int manyItems;
	private int version;
	private Painting[] data;
	
	private Gallery(boolean ignored) {} // DO NOT CHANGE THIS.... what is it?

	private static boolean doReport = true; // only to be changed in JUnit test code
	
	private boolean report(String s) {
		if (doReport) System.out.println(s);
		return false;
	}
	
	// The invariant:
	private boolean wellFormed() {
		// 0. data is not null
		if (data == null) {
			return report("0. data is null");
		}
		// 1. count is a valid index of data
		if (data.length < manyItems) {
			return report("1. data.length < manyItems");
		}
		if (manyItems < 0) {
			return report("manyitems < 0");
		}
		// 2. there are no null values in the array
		for (int i = 0; i < manyItems; i++) {
			if (data[i] == null) {
				return report("2. null values in the array");
			}
			int count = 0;
			for (int j = i; j < manyItems; j++) {
				if (data[i].equals(data[j])) {
					count++;
				}
			}
			if (count > 1) {
				
				return report("3. duplicate elements in the array (optional)");
			}
		}
		// 3. (optional) you might also test that there are no duplicate elements,
		//		but we don't require it for this assignment.
		
		return true;
	}
	
	/**
	 * Initialize an empty Gallery with an initial
	 * capacity of INITIAL_CAPACITY. The add method works
	 * efficiently (without needing more memory) until this capacity is reached.
	 * @postcondition
	 *   This Gallery is empty and has an initial
	 *   capacity of INITIAL_CAPACITY.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for an array with this many elements.
	 *   new Picture[initialCapacity].
	 **/   
	public Gallery()
	{
		data =  new Painting[INITIAL_CAPACITY];
		version = 0;
		manyItems = 0;
		// implement constructor
		// assert wellFormed() after body
		assert wellFormed();
	}
	
	/**
	 *  
	 **/ 
	private void ensureCapacity(int minimumCapacity)
	{
		if (data.length >= minimumCapacity) return;
		int newCapacity = Math.max(data.length*2, minimumCapacity);
		Painting[] newData = new Painting[newCapacity];
		for (int i=0; i < manyItems; i++) 
			newData[i] = data[i];
		data = newData;
	}
	

	/*
	 * @see java.util.AbstractCollection#add(E)
	 * We override this because it is not implemented in AbstractCollection!
	 * 
	 * NB: We are able to parameterize this method with Painting
	 * 	   because we have extended AbstractCollection with type parameter <Picture>.
	 * 
	 * 	Our data structure does not allow for duplicate paintings. 
	 * 	The contains() method can save you some work if your iterator is functional.
	 * 
	 * This should only change the version if it changes the data structure.
	 */
	@Override
	public boolean add(Painting n){
		assert wellFormed();
		
		if (this.contains(n)){
			return false;
		} else {
			ensureCapacity(manyItems + 1);
			data[manyItems] = n;
			manyItems++;
			version++;
			
		}
		//implement add(Picture b)
		
		assert wellFormed();
		return true;
		// assert wellFormed() after body
	}
	
	/*
	 * @see java.util.AbstractCollection#clear()
	 * We override this because the implementation in AbstractCollection
	 * uses the iterator, and we can do this more efficiently.
	 * This should only change the version if it changes the data structure.
	 */
	@Override
	public void clear(){
		
		assert wellFormed();
		
		if (manyItems > 0) {
			manyItems = 0;
			version++;
		} else {
			return;
		}
		
		assert wellFormed();
		
		// only update version if data structure changes
	}
	
	/*
	 * @see java.util.AbstractCollection#size()
	 * We override this because it is not implemented in AbstractCollection.
	 */
	public int size(){
		assert wellFormed();
		return manyItems;
		//implement size(), very easy
		
		// We don't have to check invariant at end of size().
		// Explain why in a comment.
		/*
		*  It is an "accessor" method that does not modify the state, 
		*  since nothing is changed, there is no point in asserting wellFormed() again
		*  
		*/	
		}
	
	// We don't override isEmpty, because AbstractCollection's implementation
	// uses size() to check, and the size method is O(1).
	// We can't do any better than that.
		
	// There are a couple of other AbstractCollection methods we will 
	// not override, but can still use, notably contains(Object), and remove(Object).
	// Looking at our implementation, why is it just fine to use the AbstractCollection's
	// iterator-based implementation for these methods?
	// Explain in a comment.
	/*
	 * Gallery.this.remove(Object) also uses our iterator which has been overridden, therefore can access our ADT correctly
	 * Same goes for the rest of the methods we don't override but still use
	 * i.e. Gallery.this.remove(Object) uses our iterator to traverse our data[], find the (Object) and remove it.
	 * Since our iterator updates the version and manyItems, remove(Object) can be safely uses.
	 * contains(Object) also traverses the data[] using our iterator to find occurrences of (Object)
	 * e.g. Since we implemented our own iterator, these methods work fine.
	 */
	
	
	/*
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator<Painting> iterator() {
		assert wellFormed();
		// assert wellFormed() before body
		return new MyIterator();
		// return the new custom iterator. There is no real work done here.
		
		// NB: We don't have to check invariant at end of iterator().
		// Explain why in a comment.
		/*
		 * That particular wellFormed() call goes to the "outer" invariant or the one for the gallery.
		 * The gallery needs to be tested before an iterator is created.  The Gallery ADT is not modified therefore it does not need to be called after.
		 * The created iterator has it's own wellFormed() that calls the Gallery.wellFormed()
		 * So calling the outer invariant twice is redundant.
		 */
	}
	
	private class MyIterator implements Iterator<Painting> {
		
		private int myVersion;
		private int currentIndex;
		private boolean hasCurrent;

		MyIterator(boolean ignored) {} // DO NOT CHANGE THIS
		
		/*
		 * Iterator design: currentIndex should always be one less than the index that holds
		 * the next element that will be returned by next(). If there is no next element,
		 * then currentIndex should be one less than the first non-element index.
		 * This is true regardless of whether the iterator has a current element.
		 * (Think about where currentIndex should start for this to be true)
		 */
		
		private boolean wellFormed() {
			
			// Invariant for recommended fields:
			
			// NB: Don't check 1 unless the version matches. you mean 0?
			if (Gallery.this.version != this.myVersion) {
				return true;
			} // iterator operations check version and throw the relevant exceptions
			if (!Gallery.this.wellFormed()) {
				return report("outer invariant failed");
			}
			
			// 0. The outer invariant holds
			//		NB: To access the parent PictureCollection of this iterator, use "Gallery.this"
			//			e.g. Gallery.this.data[0]
			
			// 1. currentIndex is between -1 (inclusive) and manyItems (exclusive)
			if  (currentIndex < -1) {
				return report("currentIndex < -1");
			} 
			if (currentIndex >= manyItems) {
				return report("currentIndex >= manyItems");
			}
			
			// 2. if currentIndex is -1, there is no current element in the iterator
			if (currentIndex == -1 && hasCurrent == true) {
				return report("currentIndex == -1 && hasCurrent == true");
			}
			
			return true;
		}	
		
		/**
		 * Instantiates a new MyIterator.
		 */
		public MyIterator() {
			currentIndex = -1;
			myVersion = Gallery.this.version;
			hasCurrent = false;
			//initialize myVersion and currentIndex, ?hasCurrent?
			assert wellFormed() : "invariant fails in iterator constructor";
		}

		/**
		 * Returns true if the iteration has more elements. (In other words, returns true
		 * if next() would return an element rather than throwing an exception.) 
		 * 
		 * @return true if the iteration has more elements
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 */
		@Override
		public boolean hasNext() {
			assert wellFormed() : "invariant fails at beginning of iterator hasNext()";
			if (this.myVersion != Gallery.this.version) {
				throw new ConcurrentModificationException();
			} 
			if (Gallery.this.manyItems > currentIndex + 1) {
				return true;
			} else {
				return false;
			}
			
		}

		/**
		 * Returns the next element in the iteration. 
		 * 
		 * @return the next element in the iteration
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 * @throws NoSuchElementException if the iteration has no more elements
		 */
		@Override
		public Painting next() {
			assert wellFormed() : "invariant fails at beginning of iterator next()";
			if (this.myVersion != Gallery.this.version) {
				throw new ConcurrentModificationException();
			} else if (!hasNext()) {
				throw new NoSuchElementException();
			} else {
				currentIndex++;
				hasCurrent = true;
				assert wellFormed() : "invariant fails at end of iterator next()";
				return Gallery.this.data[currentIndex];
			}
			
		}

		/**
		 * Removes from the underlying collection the last element returned by this iterator.
		 * 
		 * @throws ConcurrentModificationException if iterator version doesn't match collection version
		 * @throws IllegalStateException if there are no elements or there is no current element
		 */
		@Override
		public void remove() {
			assert wellFormed() : "invariant fails at beginning of iterator remove()";
			if (this.myVersion != Gallery.this.version) {
				throw new ConcurrentModificationException();
			} else if (hasCurrent == false) {
				throw new IllegalStateException();
			} else if (Gallery.this.manyItems > 1){
				for (int i = currentIndex + 1; i < manyItems; i++) {

					data[i - 1] = data[i];

				}
			}		
			Gallery.this.manyItems--;
			Gallery.this.version++;
			myVersion++;
			currentIndex--;
			hasCurrent = false;
			
			
			// inefficient. Couldn't come up with a more efficient solution.
			// possibly using an iterator instead of a for loop?
			// The collection is not sorted, so we don't have to shift everything.
			assert wellFormed() : "invariant fails at end of iterator remove()";
		}
	}
	
	/**
	 * Generate a copy of this Gallery.
	 * @param - none
	 * @return
	 *   The return value is a copy of this Gallery. Subsequent changes to the
	 *   copy will not affect the original, nor vice versa.
	 * @exception OutOfMemoryError
	 *   Indicates insufficient memory for creating the clone.
	 **/ 
	public Gallery clone( ) { 
		assert wellFormed() : "invariant failed at start of clone";
		Gallery result;

		try
		{
			result = (Gallery) super.clone( );
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

		// all that is needed is to clone the data array.
		// (Exercise: Why is this needed?)
		result.data = data.clone( );

		assert wellFormed() : "invariant failed at end of clone";
		assert result.wellFormed() : "invariant on result failed at end of clone";
		return result;
	}
	
	public static class TestInternals extends TestCase {
		
		protected Gallery self;
		protected Gallery.MyIterator iterator;
		
		private Painting n1 = new Painting(new Raster(10, 10), "nice pixels", 100);
		private Painting n2 = new Painting(new Raster(10, 10), "extra nice pixels", 1000);
		private Painting n3 = new Painting(new Raster(10, 10), "ugly pixels", 30);
		private Painting n4 = new Painting(new Raster(10, 10), "worthless pixels", 0);
		
		@Override
		protected void setUp() {
			self = new Gallery(false);
			iterator = self.new MyIterator(false);
			doReport = false;
		}
		
		// outer invariant 0 - null data
		public void test01() {
			assertFalse("null data", self.wellFormed());
		}
		
		// outer invariant 2 - null element in manyItems
		public void test02() {
			self.data = new Painting[2];
			assertTrue(self.wellFormed());
			self.manyItems = -1;
			assertFalse(self.wellFormed());
			self.manyItems = 2;
			self.data[0] = null;
			self.data[1] = n1;
			assertFalse("null element not allowed",self.wellFormed());
			self.data[0] = n2;
			assertTrue("good collection of length 2",self.wellFormed());
		}
		
		// outer invariants 1, 2 - manyItems tests
		public void test03() {
			self.data = new Painting[4];
			self.manyItems = 1;
			assertFalse("null element in collection",self.wellFormed());
			self.manyItems = 0;
			self.data[0] = n1;
			self.data[1] = n2;
			assertTrue("good empty collection",self.wellFormed());
			self.manyItems = 1;
			assertTrue("good one element collection",self.wellFormed());
			self.data[2] = n4;
			self.data[3] = n3;
			self.manyItems = 4;
			assertTrue("good four element collection",self.wellFormed());
			self.manyItems = 5;
			assertFalse("manyItems 5 in data array of length 4",self.wellFormed());
		}
		
		// inner invariant 0 - outer invariant broken
		public void test04() {
			self.data = new Painting[2];
			self.manyItems = -1;
			assertFalse("outer invariant should fail",iterator.wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test05() {
			self.data = new Painting[2];
			self.manyItems = 0;
			iterator.hasCurrent = false;
			iterator.currentIndex = -2;
			self.version = 1;
			iterator.myVersion = 1;
			assertFalse("currentIndex too small",iterator.wellFormed());
			iterator.currentIndex = -1;
			assertTrue("currentIndex -1 should be allowed",iterator.wellFormed());
			iterator.currentIndex = 1;
			assertFalse("currentIndex too big",iterator.wellFormed());
			++self.version;
			assertTrue("versions don't match",iterator.wellFormed()); 
			iterator.currentIndex = 0;
			iterator.hasCurrent = true;
			assertTrue("versions don't match",iterator.wellFormed());
			++iterator.myVersion;
			assertFalse("currentIndex too big",iterator.wellFormed());
		}
		
		// iterator invariant 1, 2, invariant only enforced if versions match
		public void test06() {
			self.data = new Painting[10];
			self.version = 456;
			self.data[0] = n1;
			self.data[1] = n2;
			self.manyItems = 2;
			iterator.currentIndex = 0;
			iterator.hasCurrent = true;
			assertTrue(self.wellFormed());
			assertTrue(iterator.wellFormed());
			iterator.myVersion = 456;
			assertTrue(iterator.wellFormed());
			iterator.currentIndex = 1;
			assertTrue(iterator.wellFormed());
			iterator.currentIndex = 2;
			assertFalse("currentIndex too big",iterator.wellFormed());
			iterator.currentIndex = -1;
			assertFalse("can't have a current when currentIndex is -1",iterator.wellFormed());
			++iterator.myVersion;
			assertTrue(iterator.wellFormed());
		}
	}
	
	public static class TestEfficiency extends TestCase {
		
		protected void setData(Gallery gal, Painting[] p) {
			gal.data = p;
		}
		
		protected void setManyItems(Gallery gal, int many) {
			gal.manyItems = many;
		}
	
		
		
	}
}
