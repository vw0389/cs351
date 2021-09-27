import java.awt.Color;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.SequentialGallery;
import edu.uwm.cs351.Painting;
import edu.uwm.cs351.Pixel;
import edu.uwm.cs351.Raster;


public class TestGallery extends LockedTestCase {

	private Painting p1 = new Painting(new Raster(10, 10), "nice pixels", 100);
	private Painting p2 = new Painting(new Raster(10,10), "beautiful artistic pixels", 1000);
	private Painting p3 = new Painting(new Raster(11, 11), "ugly pixels", 30);
	private Painting p4 = new Painting(new Raster(5, 5), "worthless pixels", 0);
	private Painting p5 = new Painting(new Raster(7, 7), "terrible art", 5);
	private Painting p[] = {null, p1, p2, p3, p4, p5};
	private SequentialGallery g1, g2;
	SequentialGallery r = new SequentialGallery();
	
	// Using the above array
	// convert a Raster result to an integer:
	// 0 = null, 1 = r1, 2 = r2 etc.
	// if the expression causes an error, the index is -1.
	// If the raster is not in the array, the result "NONE OF THE ABOVE" is -2.
	// This is only used for the locked tests
	int ix(Supplier<Painting> s) {
		try {
			Painting pa = s.get();
			if (pa == null) return 0;
			for (int i=0; i < p.length; ++i) {
				if (pa == p[i]) return i;
			}
			return -2;
		} catch (RuntimeException ex) {
			return -1;
		}
	}


	private void createPaintings() {
		p2.getRaster().addPixel(new Pixel(3, 1, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(3, 2, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(3, 3, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(6, 1, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(6, 2, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(6, 3, Color.BLACK));
		
		p2.getRaster().addPixel(new Pixel(1, 5, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(2, 6, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(3, 7, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(4, 7, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(5, 7, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(6, 7, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(7, 6, Color.BLACK));
		p2.getRaster().addPixel(new Pixel(8, 5, Color.BLACK));
	}
	@Override
	protected void setUp() {
		createPaintings();
		
		try {
			assert g1.size() == g2.size();
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		g1 = new SequentialGallery();
		g2 = new SequentialGallery();
	}

	protected <T> void assertException(Class<?> excClass, Supplier<T> producer) {
		try {
			T result = producer.get();
			assertFalse("Should have thrown an exception, not returned " + result,true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	protected <T> void assertException(Runnable f, Class<?> excClass) {
		try {
			f.run();
			assertFalse("Should have thrown an exception, not returned",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}

	/**
	 * Return the Raster as an integer
	 * <dl>
	 * <dt>-1<dd><i>(an exception was thrown)
	 * <dt>0<dd>null
	 * <dt>1<dd>e1
	 * <dt>2<dd>e2
	 * <dt>3<dd>e3
	 * <dt>4<dd>e4
	 * <dt>5<dd>e5
	 * </dl>
	 * @return integer encoding of Raster supplied
	 */
	protected String asString(Supplier<Painting> g) {

		
		try {
			Painting p = g.get();
			if (p == null) return "null";
			return p.description();
		} catch (RuntimeException ex) {
			return "Exception";
		}
	}
	
	public void test() {
		//r is empty
		// 0 = null, 1 = r1, 2 = r2 etc.
		// if the expression causes an error, the result is -1.
		// if the painting is not in the array, the result is -2.
		assertEquals(Tb(1916579737), r.hasCurrent());
		assertEquals(Ti(2004979093), ix(() -> r.getCurrent()));
		r.insert(p1);
		assertEquals(Tb(879625953), r.hasCurrent());
		assertEquals(Ti(896639195), ix(() -> r.getCurrent()));
		r.insert(null);
		r.start();
		assertEquals(Tb(472689940), r.hasCurrent());
		assertEquals(Ti(223044328), ix(() -> r.getCurrent()));
		r.advance();
		assertEquals(Tb(1163290105), r.hasCurrent());
		assertEquals(Ti(1255132094), ix(() -> r.getCurrent()));
		r.advance();
		assertEquals(Tb(1904584912), r.hasCurrent());
		assertEquals(Ti(1955765018), ix(() -> r.getCurrent()));
		r.insert(p2);
		assertEquals(Ti(828310133), ix(() -> r.getCurrent()));
		r.advance();
		assertEquals(Ti(1821502712), ix(() -> r.getCurrent()));
	}
	
	public void testCont() {
		//g1, g2 are empty
		// 0 = null, 1 = r1, 2 = r2 etc.
		// if the expression causes an error, the result is -1.
		// if the painting is not in the array, the result is -2.
		g1.insert(p3);
		g1.insert(p2);
		g1.insert(p1);
		g1.start();
		g1.removeCurrent();
		assertEquals(Ti(195832929), ix(() -> g1.getCurrent()));
		g1.advance();
		assertEquals(Ti(413860540), ix(() -> g1.getCurrent()));
		g1.advance();
		assertEquals(Ti(1127907365), ix(() -> g1.getCurrent()));
		g2 = g1.clone();
		assertEquals(Ti(900318901), ix(() -> g2.getCurrent()));
		g1.start();
		g2.start();
		g2.advance();
		g2.removeCurrent();
		g1.removeCurrent();
		assertEquals(Ti(1558026944), ix(() -> g1.getCurrent()));
		assertEquals(Ti(349322890), ix(() -> g2.getCurrent()));
	}

	public void test00() {
		// Nothing inserted yet:
		assertEquals(0,r.size());
		assertFalse(r.hasCurrent());
		r.start();
		assertFalse(r.hasCurrent());
	}
	//insert tests
	public void test01() {
		// Initially empty.
		assertEquals("Exception",asString(() -> r.getCurrent()));
		r.insert(p1);
		assertEquals("nice pixels",asString(() -> r.getCurrent()));
		r.start();
		assertEquals("nice pixels",asString(() -> r.getCurrent()));
		r.advance();
		assertEquals("Exception",asString(() -> r.getCurrent()));
	}
	
	public void test02() {
		// Initially empty.
		r.insert(p4);
		r.insert(p5);
		assertEquals("terrible art",asString(() -> r.getCurrent()));
		r.advance();
		assertEquals("worthless pixels",asString(() -> r.getCurrent()));
		r.advance();
		assertEquals("Exception",asString(() -> r.getCurrent()));
	}
	
	public void test03() {
		// Initially empty
		r.insert(p3);
		r.advance();
		r.insert(p2);
		assertEquals("beautiful artistic pixels",asString(() -> r.getCurrent()));
		r.advance();
		assertEquals("Exception",asString(() -> r.getCurrent()));
		r.start();
		assertEquals("ugly pixels",asString(() -> r.getCurrent()));
	}
	
	public void test05() {
		// Initially empty
		r.insert(null);
		assertEquals(1,r.size());
		assertEquals(true,r.hasCurrent());
		assertEquals("null",asString(() -> r.getCurrent()));
		r.advance();
		assertEquals("Exception",asString(() -> r.getCurrent()));
	}
	
	public void test06() {
		r.insert(p1);
		r.insert(p2);
		r.start();
		r.advance();
		assertSame(p1,r.getCurrent());
		SequentialGallery g2 = new SequentialGallery();
		g2.insert(p4);
		r.insertAll(g2);
		assertEquals(3,r.size());
		assertEquals("worthless pixels",asString(() -> r.getCurrent()));
		r.advance();
		assertEquals("nice pixels",asString(() -> r.getCurrent()));
		r.advance();
		assertEquals("Exception",asString(() -> r.getCurrent()));
	}
	//exception tests
	public void test07() {
		r.start();
		try {
			r.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
	}
	
	public void test08() {
		r.start();
		try {
			r.removeCurrent();
			assertFalse("empty.removeCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
	}
	
	public void test09() {
		try {
			r.advance();
			assertFalse("s.advance() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
	}
	//more tests
	public void test10() {
		r.insert(p1);
		assertEquals(1,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.start();
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertEquals(1,r.size());
		assertFalse(r.hasCurrent());
		r.start();
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertEquals(1,r.size());
	}

	public void test11() {
		r.insert(p1);
		r.removeCurrent();
		assertFalse(r.hasCurrent());
		assertEquals(0,r.size());	
		r.insert(p2);
		r.start();
		assertSame(p2,r.getCurrent());
		assertEquals(1,r.size());
	}
	
	public void test12() {
		r.insert(p2);
		r.start();
		r.advance();
		try {
			r.advance();
			assertFalse("s.advance() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		assertFalse(r.hasCurrent());
		assertEquals(1,r.size());
	}


	public void test13() {
		r.insert(p2);
		r.advance();
		try {
			r.removeCurrent();
			assertFalse("s.removeCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		assertFalse(r.hasCurrent());
		assertEquals(1,r.size());
	}

	public void test14() {
		r.insert(p2);
		r.advance();
		try {
			r.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		assertFalse(r.hasCurrent());
		assertEquals(1,r.size());
	}

	public void test20() {
		r.insert(p1);
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.insert(p2);
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		assertEquals(2,r.size());
		r.start();
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
		assertEquals(2,r.size());
		r.start();
		assertSame(p2,r.getCurrent());
		r.advance();
		r.start();
		assertSame(p2,r.getCurrent());
	}
	
	public void test21() {
		r.insert(p1);
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		r.insert(p2);
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		assertEquals(2,r.size());
		r.start();
		assertSame(p1,r.getCurrent());
		r.advance();
		assertSame(p2,r.getCurrent());
		assertTrue(r.hasCurrent());
	}
	//remove tests
	public void test22() {
		r.insert(p2);
		r.insert(p1);
		r.start();
		r.removeCurrent();
		assertTrue(r.hasCurrent());
		assertEquals(1,r.size());
		assertEquals(p2.description(),r.getCurrent().description());
		r.advance();
		r.insert(p3);
		assertTrue(r.hasCurrent());
		assertEquals(2,r.size());
		assertSame(p3,r.getCurrent());
		r.start();
		assertSame(p2,r.getCurrent());
	}

	public void test23() {
		r.insert(p2);
		r.insert(p1);
		r.start();
		r.advance();
		r.removeCurrent();
		assertFalse(r.hasCurrent());
		assertEquals(1,r.size());
		try {
			r.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex,ex instanceof IllegalStateException);
		}
		r.insert(p3);
		assertTrue(r.hasCurrent());
		assertEquals(2,r.size());
		assertSame(p3,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
		r.start();
		assertTrue(r.hasCurrent());
		assertEquals(p1.description(),r.getCurrent().description());
	}
	
	public void test30() {
		r.insert(p1);
		r.insert(p2);
		r.insert(p3);
		assertEquals(3,r.size());
		r.start();
		assertTrue(r.hasCurrent());
		assertSame(p3,r.getCurrent());
		assertTrue(r.hasCurrent());
		assertSame(p3,r.getCurrent());
		r.advance();
		assertSame(p2,r.getCurrent());
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
		assertEquals(3,r.size());
		r.start();
		assertSame(p3,r.getCurrent());
		r.advance();
		r.start();
		assertSame(p3,r.getCurrent());
	}
	
	public void test31() {
		r.insert(p1);
		r.advance();
		r.insert(p2);
		r.insert(p3);
		assertEquals(3,r.size());
		r.start();
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p3,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
		assertEquals(3,r.size());
		r.start();
		assertSame(p1,r.getCurrent());
	}

	public void test32() {
		r.insert(p2);
		r.advance();
		r.insert(p3);
		r.start();
		r.insert(p1);
		assertEquals(3,r.size());
		r.start();
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p3,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
		assertEquals(3,r.size());
	}
	
	public void test33() {
		r.insert(p3);
		r.insert(p2);
		r.insert(p1);
		assertEquals(p1.description(),r.getCurrent().description());
		r.removeCurrent();
		assertEquals(2,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		r.advance();
		assertSame(p3,r.getCurrent());
	}

	public void test34() {
		r.insert(p3);
		r.insert(p2);
		r.insert(p1);
		r.start(); // REDUNDANT
		r.advance();
		assertSame(p2,r.getCurrent());
		r.removeCurrent();
		assertEquals(2,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p3,r.getCurrent());
	}
	
	public void test35() {
		r.insert(p3);
		r.insert(p2);
		r.insert(p1);
		r.start(); // REDUNDANT
		r.advance();
		r.advance();
		assertSame(p3,r.getCurrent());
		r.removeCurrent();
		assertEquals(2,r.size());
		assertFalse(r.hasCurrent());
		try {
			r.getCurrent();
			assertFalse("s.getCurrent() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex, ex instanceof IllegalStateException);
		}
		try {
			r.advance();
			assertFalse("s.advance() should not return",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception thrown: " + ex, ex instanceof IllegalStateException);
		}
		r.start();
		assertSame(p1,r.getCurrent());
	}
 
	public void test39() {
		r.insert(p1);
		r.insert(p2);
		r.insert(p3);
		r.insert(p4);
		r.insert(p5);
		assertSame(p5,r.getCurrent());
		r.insert(p1);
		r.insert(p2);
		r.insert(p3);
		r.insert(p4);
		r.insert(p5);
		r.insert(p1);
		r.insert(p2);
		assertEquals(12,r.size());
		r.removeCurrent();
		assertTrue(r.hasCurrent());
		r.start();
		r.removeCurrent();
		assertSame(p5,r.getCurrent());
		assertEquals(10,r.size());
		r.start();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p1,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());
	}
	
	public void test40() {
		r.insert(null);
		r.advance();
		r.insert(p2);
		r.advance();
		r.insert(null);
		
		assertEquals(3,r.size());
		assertTrue(r.hasCurrent());
		assertSame(null,r.getCurrent());
		r.start();
		assertTrue(r.hasCurrent());
		assertSame(null,r.getCurrent());
		r.removeCurrent();
		assertEquals(2,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(null,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
	}

	
	public void test41() {
		SequentialGallery ge = new SequentialGallery();
		r.insertAll(ge);
		assertFalse(r.hasCurrent());
		assertEquals(0,r.size());
		r.insert(p1);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertEquals(1,r.size());
		assertEquals(0,ge.size());
		assertSame(p1,r.getCurrent());
		r.advance();
		r.insertAll(ge);
		assertFalse(r.hasCurrent());
		assertEquals(1,r.size());
		assertEquals(0,ge.size());
		r.insert(p2);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertSame(p2,r.getCurrent());
		assertEquals(2,r.size());
		assertEquals(0,ge.size());
		r.start();
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertEquals(2,r.size());
		assertEquals(0,ge.size());
	}
	
	public void test42() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p1);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertTrue(ge.hasCurrent());
		assertEquals(1,r.size());
		assertEquals(1,ge.size());
		r.start();
		assertSame(p1,r.getCurrent());
		assertSame(p1,ge.getCurrent());
	}
	
	public void test43() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p1);
		r.insert(p2);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertEquals(2,r.size());
		assertEquals(1,ge.size());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertSame(p2,r.getCurrent());
	}
	
	public void test44() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p1);
		r.insert(p2);
		r.advance();
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertEquals(2,r.size());
		assertEquals(1,ge.size());
		assertTrue(ge.hasCurrent());
		assertSame(p1,ge.getCurrent());
		r.start();
		assertSame(p2,r.getCurrent());
		r.advance();
		assertSame(p1,r.getCurrent());
	}
	
	public void test45() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p1);
		ge.advance();
		r.insert(p3);
		r.insert(p2);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertEquals(3,r.size());
		assertEquals(1,ge.size());
		assertFalse(ge.hasCurrent());
		r.advance();
		assertSame(p2,r.getCurrent());
		r.advance();
		assertSame(p3,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());	
	}
	
	public void test46() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p1);
		r.insert(p2);
		r.advance();
		r.insert(p3);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertEquals(3,r.size());
		assertEquals(1,ge.size());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertSame(p3,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
		r.start();
		assertSame(p2,r.getCurrent());
	}
	
	public void test47() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p1);
		r.insert(p2);
		r.advance();
		r.insert(p3);
		r.advance();
		assertFalse(r.hasCurrent());
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertEquals(3,r.size());
		assertEquals(1,ge.size());
		assertSame(p1,ge.getCurrent());
		r.start();
		assertSame(p2,r.getCurrent());
		r.advance();
		assertSame(p3,r.getCurrent());
		r.advance();
		assertSame(p1,r.getCurrent());
	}

	public void test48() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p2);
		ge.insert(p1);	
		r.insert(p4);
		r.insert(p3);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertEquals(4,r.size());
		assertEquals(2,ge.size());
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());	
	}

	public void test49() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p2);
		ge.insert(p1);
		ge.advance();
		r.insert(p3);
		r.advance();
		r.insert(p4);
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertEquals(4,r.size());
		assertEquals(2,ge.size());
		assertSame(p2,ge.getCurrent()); ge.advance();
		assertFalse(ge.hasCurrent());
		// check r
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());	
		r.start();
		assertSame(p3,r.getCurrent());
	}

	public void test50() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p2);
		ge.insert(p1);
		ge.advance();
		ge.advance();
		r.insert(p3);
		r.advance();
		r.insert(p4);
		r.advance();
		assertFalse(r.hasCurrent());
		assertFalse(ge.hasCurrent());
		r.insertAll(ge);
		assertTrue(r.hasCurrent());
		assertFalse(ge.hasCurrent());
		assertEquals(4,r.size());
		assertEquals(2,ge.size());
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		r.start();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());	
	}

	public void test51() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		ge.insert(p3);
		ge.insert(p4);
		ge.insert(p5);
		// ge has 24 elements
		r.insert(p1);
		r.advance();
		r.insert(p2);
		r.insertAll(ge);
		assertEquals(26,r.size());
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		r.insertAll(ge);
		assertEquals(50,r.size());
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();		
		r.start();
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		// interruption
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); r.advance();
		assertSame(p4,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		// done with all 24 copies most recently inserted
		// now back to the original
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p5,r.getCurrent()); // etc.
	}
	
	public void test52() {
		r.insertAll(r);
		assertFalse(r.hasCurrent());
		assertEquals(0,r.size());
	}
	
	
	public void test53() {
		r.insert(p1);
		r.insertAll(r);
		assertEquals(2,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
	}
	
	public void test54() {
		r.insert(p1);
		r.advance();
		r.insertAll(r);
		assertEquals(2,r.size());
		assertSame(p1,r.getCurrent());
		r.advance();
		assertFalse(r.hasCurrent());
	}
	
	public void test55() {
		r.insert(p1);
		r.removeCurrent();
		assertEquals(0,r.size());
		assertFalse(r.hasCurrent());
	}
	
	public void test56() {
		r.insert(p2);
		r.insert(p1);
		r.insertAll(r);
		assertEquals(4,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());		
	}
	
	public void test57() {
		r.insert(p1);
		r.advance();
		r.insert(p2);
		r.insertAll(r);
		assertEquals(4,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());	
		r.start();
		assertSame(p1,r.getCurrent());
	}

	public void test58() {
		r.insert(p1);
		r.advance();
		r.insert(p2);
		r.advance();
		assertFalse(r.hasCurrent());
		r.insertAll(r);
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());
		assertEquals(4,r.size());
		r.start();
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());		
	}

	public void test59() {
		r.insert(p1);
		r.advance();
		r.insert(p2);
		r.insertAll(r);
		r.removeCurrent();
		r.insert(p3);
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());
		r.start();
		assertSame(p1,r.getCurrent()); r.advance();
		r.advance();
		r.insertAll(r);
		assertEquals(8,r.size());
		assertTrue(r.hasCurrent());
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p3,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertSame(p2,r.getCurrent()); r.advance();
		assertFalse(r.hasCurrent());		
	}
	
	public void test60() {
		SequentialGallery ge = new SequentialGallery();
		ge.insert(p1);
		ge.advance();
		ge.insert(p2);	
		r.insert(p3);
		r.advance();
		r.insert(p4);
		r.insertAll(ge); // r = ugly, *nice, beautiful artistic, worthless
		r.advance();
		r.advance();
		r.insert(p5); // r = ugly, nice, beautiful artistic, *terrible art, worthless
		r.advance();
		assertTrue(r.hasCurrent());
		assertSame(p4,r.getCurrent());
		assertEquals(5,r.size());
		assertEquals(2,ge.size());
		assertSame(p2,ge.getCurrent());
		ge.advance();
		assertFalse(ge.hasCurrent());
		ge.start();
		assertSame(p1,ge.getCurrent());
	}
	
	//clone tests
	public void test61() {
		SequentialGallery c = r.clone();
		assertFalse(c.hasCurrent());
		assertEquals(0, c.size());
	}
	
	public void test62() {
		r.insert(p1);
		SequentialGallery c = r.clone();
		
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p1,r.getCurrent()); r.advance();
		assertSame(p1,c.getCurrent()); c.advance();
		assertFalse(r.hasCurrent());
		assertFalse(c.hasCurrent());
	}
	
	public void test63() {
		r.insert(p1);
		r.advance();
		SequentialGallery c = r.clone();
		
		assertFalse(r.hasCurrent());
		assertFalse(c.hasCurrent());
	}

	public void test64() {
		SequentialGallery c = r.clone();
		assertFalse(c.hasCurrent());
		
		r.insert(p1);
		c = r.clone();
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertSame(p1,c.getCurrent());
		
		r.advance();
		r.insert(p2);
		c = r.clone();
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p2,r.getCurrent());
		assertSame(p2,c.getCurrent());
		r.advance();
		c.advance();
		assertFalse(r.hasCurrent());
		assertFalse(c.hasCurrent());
		
		r.insert(p3);
		assertTrue(r.hasCurrent());
		assertFalse(c.hasCurrent());
		
		c = r.clone();
		assertSame(p3,r.getCurrent());
		assertSame(p3,c.getCurrent());
		r.advance();
		c.advance();
		assertFalse(r.hasCurrent());
		assertFalse(c.hasCurrent());
		r.start();
		c.start();
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertSame(p1,c.getCurrent());
		r.advance();
		c.advance();
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p2,r.getCurrent());
		assertSame(p2,c.getCurrent());
		
		r.start();
		c = r.clone();
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p1,r.getCurrent());
		assertSame(p1,c.getCurrent());
		r.advance();
		c.advance();
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p2,r.getCurrent());
		assertSame(p2,c.getCurrent());
		r.advance();
		c.advance();
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(p3,r.getCurrent());
		assertSame(p3,c.getCurrent());		
	}
	
	public void test65() {
		r.insert(p1);
		r.advance();
		r.insert(p3);
		r.insert(p2);
		r.removeCurrent();
		
		SequentialGallery c = r.clone();
		
		assertEquals(2,c.size());
		
		assertTrue(r.hasCurrent());
		assertTrue(c.hasCurrent());
		
		assertSame(p3,r.getCurrent());
		assertSame(p3,c.getCurrent());
	}

	public void test66() {
		r.insert(p1);
		r.advance();
		r.insert(p2);
		
		SequentialGallery c = r.clone();
		r.insert(p3);
		c.insert(p4);
		
		assertSame(p3,r.getCurrent());
		assertSame(p4,c.getCurrent());
		r.advance();
		c.advance();
		assertSame(p2,r.getCurrent());
		assertSame(p2,c.getCurrent());
		r.advance();
		c.advance();
		assertFalse(r.hasCurrent());
		assertFalse(c.hasCurrent());
		
		r.start();
		c.start();
		assertSame(p1,r.getCurrent());
		assertSame(p1,c.getCurrent());
		r.advance();
		c.advance();
		assertSame(p3,r.getCurrent());
		assertSame(p4,c.getCurrent());
	}
	
	//catenation tests
	public void test70() {
		SequentialGallery g1 = SequentialGallery.catenation(r, r);
		assertEquals(0,g1.size());
		
		r.insert(p1);
		g1 = SequentialGallery.catenation(g1,r);
		
		assertSame(p1,r.getCurrent());
		assertEquals(1,g1.size());
		assertFalse(g1.hasCurrent());
		g1.start();
		assertSame(p1,g1.getCurrent());
	}
	
	public void test71() {
		r.insert(p2);
		SequentialGallery g1 = new SequentialGallery();
		g1.insert(p4);
		
		SequentialGallery g2 = SequentialGallery.catenation(r,g1);
		assertFalse(g2.hasCurrent());
		assertSame(p2,r.getCurrent());
		assertSame(p4,g1.getCurrent());
		
		assertEquals(2,g2.size());
		g2.start();
		assertSame(p2,g2.getCurrent());
		g2.advance();
		assertSame(p4,g2.getCurrent());
	}
	
	public void test72() {
		SequentialGallery g1 = new SequentialGallery();
		SequentialGallery g2 = new SequentialGallery();
		
		g1.insert(p1);
		g1.insert(p3);
		g2.insert(p4);
		g2.insert(p2);
		
		r = SequentialGallery.catenation(g1, g2);
		assertFalse(r.hasCurrent());
		assertEquals(4,r.size());
		r.start();
		assertSame(p3,r.getCurrent());
		r.advance();
		assertSame(p1,r.getCurrent());
		r.advance();
		assertSame(p2,r.getCurrent());
	}
	
	public void test73() {
		r.insert(p3);
		r.insert(null);
		r.insert(p1);
		SequentialGallery g1 = new SequentialGallery();
		g1.insert(p2);
		
		SequentialGallery g2 = SequentialGallery.catenation(r,g1);
		assertEquals(4,g2.size());
		assertFalse(g2.hasCurrent());
		g2.start();
		assertSame(p1,g2.getCurrent());
		g2.advance();
		assertSame(null,g2.getCurrent());
		g2.advance();
		assertSame(p3,g2.getCurrent());
		g2.advance();
		assertSame(p2,g2.getCurrent());
		g2.advance();
		assertFalse(g2.hasCurrent());
	}
	
}
