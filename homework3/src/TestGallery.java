
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.Gallery;
import edu.uwm.cs351.Painting;
import edu.uwm.cs351.Raster;



public class TestGallery extends LockedTestCase {
	
	private Gallery g1, g2;
	private Iterator<Painting> it, it2;
	
	private Painting p1 = new Painting(new Raster(10, 10), "nice pixels", 100);
	private Painting p2 = new Painting(new Raster(10, 10), "beautiful artistic pixels", 1000);
	private Painting p3 = new Painting(new Raster(10, 10), "ugly pixels", 30);
	private Painting p4 = new Painting(new Raster(10, 10), "worthless pixels", 0);
	private Painting p5 = new Painting(new Raster(10, 10), "worthless pixels", 0);
	private Painting p5b = new Painting(new Raster(10, 10), "worthless pixels", 0);
	
	private Painting p[] = new Painting[5];
	
	// Using the above array
	// convert a Painting result to an integer:
	// 0 = null
	// 1 = some non-null Painting object
	// -1 = IllegalArgumentException
	// -2 = NoSuchElementException
	// -3 = IllegalStateException
	// -4 = ConcurrentModificationException
	// -5 = Some other exception, or other value
	// This is only used for the locked tests
	int ex(Supplier<Painting> s) {
		try {
			Painting pa = s.get();
			if (pa == null) return 0;
			return 1;
		} catch (IllegalArgumentException ex) {
			return -1;
		} catch (NoSuchElementException ex) {
			return -2;
		} catch (IllegalStateException ex) {
			return -3;
		} catch (ConcurrentModificationException ex) {
			return -4;
		} catch (RuntimeException ex) {
			return -5;
		}
	}
	
	@Override
	protected void setUp() {
		try {
			assert g1.size() == g2.size();
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
		}
		g1 = new Gallery();
		g2 = new Gallery();
	}
	

	
	protected void testGallery(Gallery gal, String name, Painting... paints)
	{
		assertEquals(name + ".size()",paints.length,gal.size());
		Iterator<Painting> it = gal.iterator();
		while (it.hasNext()) {
			Painting p = it.next();
			boolean found = false;
			for(int i=0; i<paints.length; i++)
				if(p.equals(paints[i]));
					found = true;
			assertTrue(found);
		}
		
	}
	
	protected void assertException(Class<? extends Throwable> c, Runnable r) {
		try {
			r.run();
			assertFalse("Exception should have been thrown",true);
		} catch (RuntimeException ex) {
			assertTrue("should throw exception of " + c + ", not of " + ex.getClass(), c.isInstance(ex));
		}
	}
	
	public void test() {
		//g1 is empty
		it = g1.iterator();
		assertEquals(false, it.hasNext());
		// 0 = null
		// 1 = some non-null Painting object
		// -1 = IllegalArgumentException
		// -2 = NoSuchElementException
		// -3 = IllegalStateException
		// -4 = ConcurrentModificationException
		// -5 = Some other exception
		assertEquals(-2, ex(() -> it.next()));
		assertEquals(true, g1.add(p1));
		assertEquals(1, g1.size());
		it = g1.iterator();
		assertEquals(true, it.hasNext());
		assertEquals(1, ex(() -> it.next()));
		assertEquals(false, it.hasNext());
		it.remove();
		assertException(IllegalStateException.class,() -> {it.remove();});
		testcont(g1);
	}
	
	private void testcont(Gallery g1) {
		g1.clear();
		g1.add(p1);
		it = g1.iterator();
		g1.add(p2);
		// 0 = null
		// 1 = some non-null Painting object
		// -1 = IllegalArgumentException
		// -2 = NoSuchElementException
		// -3 = IllegalStateException
		// -4 = ConcurrentModificationException
		// -5 = Some other exception, or other value
		assertEquals(-4, ex(() -> it.next()));
		g1.add(p3);
		it = g1.iterator();
		assertEquals(false, g1.add(p2));
		assertEquals(1, ex(() -> p[0] = it.next()));
		it.remove();
		assertEquals(1, ex(() -> p[1] = it.next()));
		g1.remove(p[0]);
		assertEquals(1, ex(() -> p[2] = it.next()));
		g1.remove(p[1]);
		assertEquals(-4, ex(() -> it.next()));
		
	}
	
	//test add + remove stale
	
	public void test00x()
	{
		assertEquals(true, g1.isEmpty());
		it = g1.iterator();
		assertEquals(false, it.hasNext());
		Painting p1 = new Painting(new Raster(10, 10), "nice pixels", 100);
		g1.add(p1);
		it = g1.iterator();
		assertEquals(true, it.hasNext());
		assertEquals("Which Painting should be next?", p1, it.next());
		assertEquals(false, it.hasNext());
	
		g1.add(p2);
		testGallery(g1,"{p1,p2}",p1,p2);
		g1.add(p3);
		testGallery(g1,"{p1,p2,p3}",p1,p2,p3);
		g1.add(p4);
		testGallery(g1,"{p1,p2,p3,p4}",p1,p2,p3,p4);
		
		g1.clear();
		testGallery(g1,"after clear");
		it = g1.iterator();
		assertEquals(false, it.hasNext());
	}
	
//test0x: size, add, clear, isEmpty
	
	public void test00() {
		assertEquals(0, g1.size());
		assertTrue(g1.isEmpty());
	}
	
	public void test01() {
		g1.add(p1);
		assertEquals(1, g1.size());
		assertFalse(g1.isEmpty());
	}
	
	public void test02() {
		g1.add(p1);
		g1.add(p2);
		assertEquals(2, g1.size());
	}
	
	public void test03() {
		assertTrue(g1.add(p1));
		assertFalse(g1.add(p1));
		assertEquals(1, g1.size());
	}
	
	public void test04() {
		assertTrue(g1.add(p1));
		assertTrue(g1.add(p2));
		assertTrue(g1.add(p3));
		assertTrue(g1.add(p4));
		assertFalse(g1.add(p1));
		assertFalse(g1.add(p2));
		assertFalse(g1.add(p3));
		assertFalse(g1.add(p4));
		assertEquals(4, g1.size());
	}
	
	public void test05() {
		g1.clear();
		assertEquals(0, g1.size());
		assertTrue(g1.isEmpty());
	}
	
	public void test06() {
		g1.add(p1);
		g1.add(p2);
		g1.clear();
		assertEquals(0, g1.size());
		assertTrue(g1.isEmpty());
		g1.add(p1);
		assertEquals(1, g1.size());
		assertFalse(g1.isEmpty());
	}
	
//test1x: iterator methods
	
	public void test10() {
		it = g1.iterator();
		assertFalse(it.hasNext());
	}
	
	public void test11() {
		g1.add(p1);
		it = g1.iterator();
		assertTrue(it.hasNext());
		assertTrue(it.hasNext());
		assertTrue(it.hasNext());
		assertEquals(p1,it.next());
		assertFalse(it.hasNext());
		testGallery(g1,"{p1}", p1);
	}
	
	public void test12() {
		g1.add(p1);
		it = g1.iterator();
		it.next();
		it.remove();
		assertFalse(it.hasNext());
		testGallery(g1,"{}");
	}
	
	public void test13() {
		g1.add(p1);
		g1.add(p2);
		it = g1.iterator();
		it.next();
		assertTrue(it.hasNext());
		it.next();
		assertEquals(false, it.hasNext());
		assertException(NoSuchElementException.class,() -> {it.next();});
		//Check that the first element encountered (p[0]) is not in g1
		//And the second (p[2]) is in g1
		testGallery(g1,"{p1,p2} after removing the first", p1, p2);
	}
	
	public void test14() {
		g1.add(p1);
		g1.add(p2);
		g1.add(p3);
		it = g1.iterator();
		p[0] = it.next();
		p[1] = it.next();
		it.remove();
		assertException(IllegalStateException.class,() -> {it.remove();});
		p[2] = it.next();
		assertEquals(false, it.hasNext());
		testGallery(g1,"{p1,p2,p3} after removing the second",p[0],p[2]);
	}
	
	public void test15() {
		g1.add(p3);
		g1.add(p1);
		g1.add(p2);
		it = g1.iterator();
		p[0] = it.next();
		it.remove();
		assertEquals(true, it.hasNext());
		p[1] = it.next();
		p[2] = it.next();
		assertEquals(false, it.hasNext());
		testGallery(g1,"{p3,p1,p2} after removing the first",p[1],p[2]);
	}

	public void test16() {
		g1.add(p3);
		g1.add(p2);
		g1.add(p1);
		it = g1.iterator();
		p[0] = it.next();
		p[1] = it.next();
		p[2] = it.next();
		it.remove();
		assertEquals(false, it.hasNext());
		testGallery(g1,"{p3,p2,p1} after removing the last",p[0],p[1]);	
		it = g1.iterator();
		assertEquals(true, it.hasNext());
		p[3] = it.next();
		assertEquals(true, it.hasNext());
		p[4] = it.next();
		assertEquals(false, it.hasNext());
		it.remove();
		testGallery(g1,"{p3,p2,p1} after remove the last twice",p[3]);	
	}

	public void test17() {
		g1.add(p1);
		g1.add(p2);
		g1.add(p3);
		it = g1.iterator();
		while(it.hasNext()) {
			if(it.next().equals(p2))
				it.remove();
		}
		assertFalse(it.hasNext());
		testGallery(g1,"{p1,p2,p3} after removing p2",p1,p3);
	}

	public void test18() {
		g1.add(p1);
		g1.add(p2);
		g1.add(p3);
		it = g1.iterator();
		assertException(IllegalStateException.class,() -> {it.remove();});
		while(it.hasNext()) {
			if(it.next().getValue()<500)
				it.remove();
		}
		testGallery(g1,"{p1,p2,p3} after removing p1 and p3",p2);
	}

	public void test19() {
		it = g1.iterator();
		assertException(NoSuchElementException.class,() -> {it.next();});
		assertException(IllegalStateException.class,() -> {it.remove();});
		g1.add(p1);
		g1.add(p2);
		g1.add(p3);
		it = g1.iterator();
		while(it.hasNext()) {
			it.next();
			it.remove();
		}
		testGallery(g1,"{p1,p2,p3} after removing everything");
		assertException(IllegalStateException.class,() -> {it.remove();});
	}

//test2x iterator interactions
	
	public void test20() 
	{
		it = g1.iterator();
		g1.clear();
		assertFalse(it.hasNext());
	}
	
	public void test21() 
	{
		g1.add(p1);
		it = g1.iterator();
		g1.clear();
		testGallery(g1,"after clear");
		assertException(ConcurrentModificationException.class,() -> {it.hasNext();});
	}
	
	public void test22() 
	{
		g1.add(p1);
		it = g1.iterator();
		it.next();
		it.remove();
		g1.clear();
		testGallery(g1,"after clear");
		assertFalse(it.hasNext());
	}
	
	public void test23() 
	{
		it = g1.iterator();
		g1.add(p1);
		assertException(ConcurrentModificationException.class,() -> {it.next();});
	}
	
	public void test24() 
	{
		g1.add(p1);
		it = g1.iterator();
		g1.add(p1);
		assertTrue(it.hasNext());
	}
	
	public void test26() {
		it = g1.iterator();
		g1.size();
		assertFalse(it.hasNext());
	}
	
	public void test27() {
		g1.add(p1);
		g1.add(p2);
		it = g1.iterator();
		g1.size();
		assertTrue(it.hasNext());
	}
	
	public void test28() {
		g1.add(p1);
		g1.add(p2);
		it = g1.iterator();
		it2 = g1.iterator();
		p[0] = it.next();
		p[1] = it.next();
		assertFalse(it.hasNext());
		assertTrue(it2.hasNext());
		g2.add(it2.next());
		g2.add(it2.next());
		assertFalse(it2.hasNext());
		testGallery(g2,"{p1,p2}", p[0], p[1]);
	}
	
	public void test29() {
		g1.add(p1);
		it = g1.iterator();
		it2 = g1.iterator();
		it.next();
		it2.next();
		assertFalse(it.hasNext());
		assertFalse(it2.hasNext());
		it.remove();
		assertFalse(it.hasNext());
		assertException(ConcurrentModificationException.class,() -> {it2.hasNext();});
	}

	
//test3x: test remove and contains
	
	public void test30() {
		assertFalse(g1.contains(p1));
		assertFalse(g1.contains(null));
		assertFalse(g1.contains(g2));
		assertFalse(g1.contains(g1));
	}
	
	public void test31() {
		g1.add(p5);
		assertFalse(g1.contains(p1));
		assertFalse(g1.contains(null));
		assertTrue(g1.contains(p5));
		assertTrue(g1.contains(p5b));
	}
	
	public void test32() {
		g1.add(p1);
		g1.add(p2);
		g1.add(p3);
		it = g1.iterator();
		while(it.hasNext()) {
			if(it.next().equals(p2))
				it.remove();
		}
		assertTrue(g1.contains(p1));
		assertFalse(g1.contains(p2));
		assertTrue(g1.contains(p3));
		assertFalse(it.hasNext());
	}
	
	public void test33() {
		assertFalse(g1.remove(null));
		assertFalse(g1.remove(null));
		assertFalse(g1.remove(g2));
		assertFalse(g1.remove(g1));
	}
	
	public void test34() {
		g1.add(p5);
		g1.add(p3);
		assertTrue(g1.remove(p5b));
		assertEquals(1, g1.size());
		g1.add(p2);
		assertEquals(2, g1.size());
		assertTrue(g1.remove(p2));
		assertEquals(1, g1.size());
		assertFalse(g1.contains(p5));
		assertFalse(g1.contains(p2));
		assertTrue(g1.contains(p3));
		assertTrue(g1.remove(p3));
		assertTrue(g1.isEmpty());
	}
	
	public void test35() {
		g1.add(p1);
		it = g1.iterator();
		g1.remove(p2);
		assertTrue(it.hasNext());
		g1.remove(p1);
		assertException(ConcurrentModificationException.class,() -> {it.hasNext();});
	}
	
	public void test36() {
		it = g1.iterator();
		g1.add(p1);
		g1.remove(p1);
		assertException(ConcurrentModificationException.class,() -> {it.hasNext();});
	}
}
