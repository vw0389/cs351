import java.awt.Color;
import java.util.function.Supplier;

import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.RasterSeq;
import edu.uwm.cs351.Raster;
import edu.uwm.cs351.Pixel;



public class TestRasterSeq extends LockedTestCase {

	private RasterSeq s;
	Raster r1 = new Raster(1,1);
	Raster r2 = new Raster(2,2);
	Raster r3 = new Raster(3,3);
	Raster r4 = new Raster(4,4);
	Raster r5 = new Raster(5,5);
	
	Raster r[] = { null, r1, r2, r3, r4, r5 };
	
	// Using the above array
	// convert a Raster result to an integer:
	// 0 = null, 1 = r1, 2 = r2 etc.
	// if the expression causes an error, the index is -1.
	// If the raster is not in the array, the result "NONE OF THE ABOVE" is -2.
	// This is only used for the locked tests
	int ix(Supplier<Raster> s) {
		try {
			Raster ra = s.get();
			if (ra == null) return 0;
			for (int i=0; i < r.length; ++i) {
				if (ra == r[i]) return i;
			}
			return -2;
		} catch (RuntimeException ex) {
			return -1;
		}
	}
	
	@Override
	public void setUp() {
		s = new RasterSeq();
		r1.addPixel(new Pixel(0,0,Color.BLACK));
		r2.addPixel(new Pixel(1,1,Color.BLACK));
		r3.addPixel(new Pixel(2,2,Color.BLACK));
		r4.addPixel(new Pixel(3,3,Color.BLACK));
		r5.addPixel(new Pixel(4,4,Color.BLACK));
		try {
			assert 3/((int)r1.x()-1) == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test suite.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			return;
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
	
	// This test tests many things.
	// We recommend that you run the UnlockTests to unlock all its tests at once.
	public void test() {
		assertEquals(Ti(531536310),s.size());
		// In the following, if a Raster index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Raster r1
		// 2 if the result will be the Raster r2
		// 3,4,5 etc.
		// -1 if the code will crash with an exception
		// -2 none of the above (some other Raster)
		assertEquals(Ti(445823101),ix(() -> s.getCurrent()));
		s.addBefore(r1);
		assertEquals(Ti(1237388388),ix(() -> s.getCurrent()));
		s.start();
		assertEquals(Ti(1535398014),ix(() -> s.getCurrent()));
		s.addAfter(null);
		s.advance();
		assertEquals(Ti(620763609),ix(() -> s.getCurrent()));
		s.addBefore(r3);
		assertEquals(Ti(1452014305),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(734972494),ix(() -> s.getCurrent()));
		s.addAfter(new Raster(r2.x(),r2.y()));
		s.advance();
		assertEquals(Ti(1266961586),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(597849451),ix(() -> s.getCurrent()));
		testcont(s);		
		testremove(s);
	}
	
	// Continuation of test:
	private void testcont(RasterSeq s) {
		s.start();
		s.advance();
		s.advance();
		// At this point s is [r1,r3,*null,not-r2)] with * marking current
		assertEquals(0,ix(() -> s.getCurrent()));
		RasterSeq s2 = new RasterSeq();
		assertEquals(Tb(2099559110),s2.atEnd());
		// In the following, if a Raster index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Raster r1
		// 2 if the result will be the Raster r2, etc for 3, 4, 5
		// -1 if the code will crash with an exception
		// -2 none of the above (some other Raster)
		s2.addBefore(r4);
		assertEquals(Tb(294060496),s2.atEnd());
		s2.addAfter(r5);
		assertEquals(Ti(718908870),ix(() -> s2.getCurrent()));
		assertEquals(Tb(1032608240),s2.atEnd());
		s.addAll(s2);
		assertEquals(Ti(1320191489),s.size());
		// What does addAll() say about what is current afterwards?
		assertEquals(Ti(964215254),ix(()->s.getCurrent()));
		assertEquals(Ti(813844137),ix(() -> s2.getCurrent()));
		s.advance();
		assertEquals(Ti(1487764752),ix(()->s.getCurrent()));
		s.advance();
		assertEquals(Ti(273890519),ix(()->s.getCurrent()));
	}
	
	// continuation of test
	private void testremove(RasterSeq s) {
		// s is [r1, r3, null, r4, *r5, not-r2] where * marks current element
		// In the following, if a Raster index is called for ("ix"), answer:
		// 0 if the result will be null
		// 1 if the result will be the Raster r1
		// 2 if the result will be the Raster r2, etc for 3, 4, 5
		// -1 if the code will crash with an exception
		// -2 none of the above (some other Raster)
		s.removeCurrent();
		assertEquals(Ti(393647729),ix(() -> s.getCurrent()));
		s.advance();
		assertEquals(Ti(446513197),ix(() -> s.getCurrent()));
		assertEquals(Tb(1310106214),s.atEnd());
		s.removeCurrent();
		assertEquals(Tb(729291501),s.atEnd());
		assertEquals(Ti(1710366088),ix(() -> s.getCurrent()));
		s.start();
		s.removeCurrent();
		assertEquals(Tb(2036014155), s.hasCurrent());
		s.advance();
		assertEquals(Ti(11333038),ix(() -> s.getCurrent()));
	}
	
	public void test00() {
		assertException(IllegalArgumentException.class,() -> {s = new RasterSeq(-1);});	
	}
	
	public void test01() {
		assertEquals(0,s.size());
	}
	
	public void test02() {
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
	}
	
	public void test03() {
		assertException(IllegalStateException.class,() -> {s.getCurrent();});		
	}
	
	public void test04() {
		assertException(IllegalStateException.class, () -> {s.advance();});
	}
	
	public void test05() {
		assertException(IllegalStateException.class, () -> {s.removeCurrent();});
	}
	
	public void test06() {
		s.start();
		assertFalse(s.hasCurrent());
	}
	
	public void test10() {
		s.addBefore(null);
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
	}
	
	public void test11() {
		s.addBefore(r2);
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
	}
	
	public void test12() {
		s.addAfter(r3);
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test13() {
		s.addAfter(r1);
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		assertFalse(s.atEnd());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent());
		assertFalse(s.atEnd());
		s.advance();
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent());
		assertEquals(1,s.size());
		assertFalse(s.atEnd());
	}
	
	public void test14() {
		s.addBefore(r1);
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent());
		assertSame(r1,s.getCurrent());
		assertFalse(s.atEnd());
		s.advance();
		assertEquals(1,s.size());
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
		s.start();
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent());
		assertEquals(1,s.size());
		assertFalse(s.atEnd());
	}
	
	public void test15() {
		s.addBefore(r4);
		s.start();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
		assertEquals(0,s.size());
	}
	
	public void test16() {
		s.addAfter(r5);
		s.advance();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
		assertEquals(0,s.size());
	}

	public void test17() {
		s.addBefore(r1);
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
		assertEquals(1,s.size());
		assertException(IllegalStateException.class,() -> { s.removeCurrent(); });
	}
	
	public void test18() {
		s.addAfter(r2);
		s.advance();
		s.advance();
		assertException(IllegalStateException.class, () -> { s.advance(); });
	}
	
	public void test19() {
		s.addBefore(r3);
		assertException(IllegalStateException.class,() -> { s.getCurrent(); });
	}
	
	public void test20() {
		s.addAfter(r1);
		s.addBefore(r2);
		assertEquals(2,s.size());
		assertFalse(s.atEnd());
		assertFalse(s.hasCurrent());
		s.advance();
		assertSame(r1,s.getCurrent());
	}
	
	public void test21() {
		s.addBefore(r3);
		s.addBefore(r4);
		s.start();
		assertEquals(2,s.size());
		assertFalse(s.atEnd());
		assertSame(r3,s.getCurrent());
	}
	
	public void test22() {
		s.addBefore(r1);
		s.addAfter(r2);
		assertEquals(2,s.size());
		assertFalse(s.atEnd());
		s.advance();
		assertSame(r2,s.getCurrent());
	}
	
	public void test23() {
		s.addAfter(r1);
		s.addAfter(r2);
		s.advance();
		assertEquals(2,s.size());
		assertFalse(s.atEnd());
		assertSame(r2,s.getCurrent());
	}
	
	public void test24() {
		s.addAfter(r1);
		s.addAfter(r2);
		assertFalse(s.hasCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test25() {
		s.addBefore(r3);
		s.addAfter(r4);
		s.start();
		assertSame(r3,s.getCurrent());
		s.advance();
		assertSame(r4,s.getCurrent());
	}
	
	public void test26() {
		s.addAfter(r5);
		s.addBefore(r1);
		s.start();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertFalse(s.atEnd());
	}
	
	public void test27() {
		s.addBefore(r2);
		s.addAfter(r3);
		s.advance();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
	}
	
	public void test28() {
		s.addBefore(r4);
		s.addBefore(r5);
		s.start();
		s.removeCurrent();
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(r5,s.getCurrent());
	}
	
	public void test29() {
		s.addAfter(r1);
		s.addBefore(r2);
		s.start();
		s.removeCurrent();
		s.advance();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertTrue(s.atEnd());
		assertEquals(0,s.size());
	}
	
	public void test30() {
		s.addBefore(r1);
		s.addBefore(null);
		s.addBefore(r2);
		s.start();
		assertSame(r1,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertNull(s.getCurrent());
	}
	
	public void test31() {
		s.addBefore(r3);
		s.addBefore(r4);
		s.start();
		s.addAfter(r5);
		s.advance();
		assertSame(r5,s.getCurrent());
		assertEquals(3,s.size());
		s.advance();
		assertSame(r4,s.getCurrent());
		assertEquals(3,s.size());
	}
	
	public void test32() {
		s.addBefore(r1);
		s.addBefore(r2);
		assertTrue(s.atEnd());
		s.addAfter(r3);
		s.advance();
		assertSame(r3,s.getCurrent());
	}
	
	public void test33() {
		s.addBefore(r4);
		s.addBefore(r5);
		s.addAfter(null);
		s.advance();
		assertFalse(s.atEnd());
		assertNull(s.getCurrent());
		assertEquals(3,s.size());
	}
	
	public void test34() {
		s.addAfter(r1);
		s.addAfter(r2);
		s.addBefore(r3);
		s.advance();
		assertSame(r2,s.getCurrent());
		s.advance();
		assertSame(r1,s.getCurrent());
		s.removeCurrent();
		s.addBefore(r4);
		s.start();
		s.advance();
		s.advance();
		assertSame(r4,s.getCurrent());
		s.removeCurrent();
		assertTrue(s.atEnd());
		assertException(IllegalStateException.class,() -> { s.getCurrent(); });
	}
	
	public void test35() {
		s.addAfter(null);
		s.addBefore(r4);
		s.advance();
		assertNull(s.getCurrent());
		assertFalse(s.atEnd());
		s.removeCurrent();
		assertTrue(s.atEnd());
	}
	
	public void test36() {
		s.addAfter(r4);
		s.addAfter(r5);
		s.start();
		s.addAfter(r1);
		s.start();
		s.removeCurrent();
		s.advance();
		assertSame(r1,s.getCurrent());
		s.advance();
		s.removeCurrent();
		s.addAfter(r2);
		s.advance();
		assertSame(r2,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
	}
	
	public void test37() {
		s.addAfter(r2);
		s.addBefore(r3);
		s.addBefore(r4);
		s.start();
		s.removeCurrent();
		s.addBefore(r1);
		assertFalse(s.hasCurrent());
		s.advance();
		assertSame(r4,s.getCurrent());
		s.advance();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		assertEquals(2,s.size());
	}
	
	public void test38() {
		s.addAfter(r5);
		s.addBefore(r1);
		s.start();
		s.removeCurrent();
		s.addAfter(r2);
		s.advance();
		assertSame(r2,s.getCurrent());
		s.advance();
		assertSame(r5,s.getCurrent());
	}
	
	public void test39() {
		s.addAfter(r1);
		s.addAfter(r2);
		s.addAfter(r3);
		s.addAfter(r4);
		s.addAfter(r5);
		s.start();
		assertSame(r5,s.getCurrent());
		s.addAfter(r1);
		s.addAfter(r2);
		s.addAfter(r3);
		s.addAfter(r4);
		s.addAfter(r5);
		s.addAfter(r1);
		s.addAfter(r2);
		assertEquals(12,s.size());
		s.advance();
		s.removeCurrent();
		assertFalse(s.hasCurrent());
		s.start();
		s.removeCurrent();
		assertEquals(10,s.size());
		s.start();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r5,s.getCurrent()); s.advance();
		assertSame(r4,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r4,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());
	}

	public void test40() {
		RasterSeq se = new RasterSeq();
		s.addAll(se);
		assertEquals(0,s.size());
	}

	public void test41() {
		RasterSeq se = new RasterSeq();
		s.addBefore(r1);
		s.start();
		s.addAll(se);
		assertEquals(r1,s.getCurrent());
	}

	public void test42() {
		RasterSeq se = new RasterSeq();
		s.addBefore(r2);
		s.addAll(se);
		assertFalse(s.hasCurrent());
	}

	public void test43() {
		RasterSeq se = new RasterSeq();
		s.addBefore(r3);
		s.addAfter(r4);
		s.advance();
		s.addAll(se);
		assertSame(r4,s.getCurrent());
	}

	public void test44() {
		RasterSeq se = new RasterSeq();
		se.addBefore(r1);
		se.start();
		s.addAll(se);
		assertFalse(s.hasCurrent());
		assertTrue(se.hasCurrent());
		assertEquals(1,s.size());
		assertEquals(1,se.size());
		s.start();
		assertSame(r1,s.getCurrent());
		assertSame(r1,se.getCurrent());
	}
	
	public void test45() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r1);
		se.advance();
		s.addAfter(r2);
		s.advance();
		s.addAll(se);
		assertTrue(s.hasCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertSame(r2,s.getCurrent());
		s.advance();
		assertSame(r1,s.getCurrent());
	}
	
	public void test46() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r1);
		se.advance();
		s.addBefore(r2);
		s.addAll(se);
		assertFalse(s.hasCurrent());
		assertEquals(2,s.size());
		assertEquals(1,se.size());
		assertTrue(se.hasCurrent());
		assertSame(r1,se.getCurrent());
		s.start();
		assertSame(r2,s.getCurrent());
		s.advance();
		assertSame(r1,s.getCurrent());
	}
	
	public void test47() {
		RasterSeq se = new RasterSeq();
		se.addBefore(r1);
		s.addAfter(r3);
		s.addBefore(r2);
		s.start();
		s.addAll(se);
		assertTrue(s.hasCurrent());
		assertSame(r2,s.getCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertFalse(se.hasCurrent());
		s.advance();
		assertSame(r1,s.getCurrent());
		s.advance();
		assertSame(r3,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());	
	}
	
	public void test48() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r1);
		se.advance();
		s.addBefore(r2);
		s.addAfter(r3);
		s.advance();
		s.addAll(se);
		assertTrue(s.hasCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(r3,s.getCurrent());
		s.advance();
		assertSame(r1,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
		s.start();
		assertSame(r2,s.getCurrent());
	}
	
	public void test49() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r1);
		se.advance();
		s.addBefore(r2);
		s.addBefore(r3);
		assertFalse(s.hasCurrent());
		s.addAll(se);
		assertFalse(s.hasCurrent());
		assertEquals(3,s.size());
		assertEquals(1,se.size());
		assertSame(r1,se.getCurrent());
		s.start();
		assertSame(r2,s.getCurrent());
		s.advance();
		assertSame(r3,s.getCurrent());
		s.advance();
		assertSame(r1,s.getCurrent());
	}

	public void test50() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r2);
		se.addBefore(r1);	
		se.start();
		s.addAfter(r4);
		s.addAfter(r3);
		s.addAll(se);
		s.start();
		assertTrue(s.hasCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r4,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());	
	}

	public void test51() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r2);
		se.addBefore(r1);
		se.advance();
		s.addBefore(r3);
		s.addBefore(r4);
		s.addAll(se);
		assertFalse(s.hasCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		assertSame(r2,se.getCurrent()); se.advance();
		assertFalse(se.hasCurrent());
		// check s
		s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());	
		s.start();
		assertSame(r3,s.getCurrent());
	}

	public void test52() {
		RasterSeq se = new RasterSeq();
		se.addBefore(r1);
		se.addBefore(r2);
		s.addBefore(r3);
		s.addBefore(r4);
		assertFalse(s.hasCurrent());
		assertFalse(se.hasCurrent());
		s.addAll(se);
		assertFalse(s.hasCurrent());
		assertFalse(se.hasCurrent());
		assertEquals(4,s.size());
		assertEquals(2,se.size());
		s.start();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r4,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());	
	}

	public void test53() {
		RasterSeq se = new RasterSeq();
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		se.addBefore(r3);
		se.addBefore(r4);
		se.addBefore(r5);
		// se has 24 elements
		s.addBefore(r1);
		s.addAfter(r2);
		s.advance();
		s.addAll(se);
		assertEquals(26,s.size());
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		s.addAll(se);
		assertEquals(50,s.size());
		assertSame(r4,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		s.start();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r4,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r4,s.getCurrent()); s.advance();
		assertSame(r5,s.getCurrent());
	}
	
	public void test54() {
		RasterSeq se = new RasterSeq();
		s.addAfter(r5);
		s.addAfter(r4);
		se.addAfter(r3);
		se.addAfter(r2);
		se.addAfter(r1);
		s.addAll(se);
		assertEquals(5,s.size());
		assertFalse(s.hasCurrent());
		s.addAfter(r5);
		assertEquals(6,s.size());
		s.advance();
		assertSame(r5,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r4,s.getCurrent()); s.advance();
		assertSame(r5,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());
	}
	
	public void test56() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r3);
		se.addAfter(r2);
		se.addAfter(r1);
		s.addAll(se);
		assertFalse(se.hasCurrent());
		assertFalse(se.atEnd());
		se.advance();
		assertEquals(r1,se.getCurrent());
	}
	
	public void test57() {
		RasterSeq se = new RasterSeq();
		se.addBefore(r3);
		se.addBefore(r2);
		se.addBefore(r1);
		s.addAll(se);
		assertFalse(se.hasCurrent());
		assertTrue(se.atEnd());
	}
	
	public void test60() {
		s.addAll(s);
		assertFalse(s.hasCurrent());
		assertEquals(0,s.size());
	}
	
	
	public void test61() {
		s.addAfter(r1);
		s.advance();
		s.addAll(s);
		assertEquals(2,s.size());
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
	}
	
	public void test62() {
		s.addBefore(r1);
		s.addAll(s);
		assertEquals(2,s.size());
		assertFalse(s.hasCurrent());
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent());
		s.advance();
		assertFalse(s.hasCurrent());
	}
	
	public void test63() {
		s.addAfter(r1);
		s.advance();
		s.removeCurrent();
		assertEquals(0,s.size());
		assertFalse(s.hasCurrent());
	}
	
	public void test64() {
		s.addAfter(r2);
		s.addBefore(r1);
		s.start();
		s.addAll(s);
		assertEquals(4,s.size());
		assertTrue(s.hasCurrent());
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());		
	}
	
	public void test65() {
		s.addAfter(r1);
		s.addBefore(r2);
		s.addAll(s);
		assertEquals(4,s.size());
		assertFalse(s.hasCurrent());
		s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());		
	}

	public void test66() {
		s.addAfter(r1);
		s.addAfter(r2);
		assertFalse(s.hasCurrent());
		s.addAll(s);
		assertFalse(s.hasCurrent());
		assertEquals(4,s.size());
		s.start();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());		
	}

	public void test67() {
		s.addAfter(r2);
		s.addAfter(r1);
		s.start();
		s.addAll(s);
		s.addAfter(r3);
		s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());
		s.start();
		assertSame(r1,s.getCurrent());
		s.advance();
		s.addAll(s);
		assertEquals(10,s.size());
		assertTrue(s.hasCurrent());
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r3,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertSame(r2,s.getCurrent()); s.advance();
		assertFalse(s.hasCurrent());		
	}
	
	public void test68() {
		RasterSeq se = new RasterSeq();
		se.addAfter(r2);
		se.addBefore(r1);	
		se.advance();
		s.addBefore(r3);
		s.addAfter(r4);
		s.advance();
		s.addAll(se);
		s.advance();
		s.addAfter(r5);
		s.advance();
		s.advance();
		assertTrue(s.hasCurrent());
		assertSame(r2,s.getCurrent());
		assertEquals(5,s.size());
		assertEquals(2,se.size());
		assertSame(r2,se.getCurrent());
		se.advance();
		assertFalse(se.hasCurrent());
		se.start();
		assertSame(r1,se.getCurrent());
	}
	
	public void test70() {
		RasterSeq c = s.clone();
		assertFalse(c.hasCurrent());
		assertEquals(0, c.size());
	}
	
	public void test71() {
		s.addAfter(r1);
		s.advance();
		RasterSeq c = s.clone();
		
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(r1,s.getCurrent()); s.advance();
		assertSame(r1,c.getCurrent()); c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
	}
	
	public void test72() {
		s.addBefore(r1);
		RasterSeq c = s.clone();
		
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
	}

	public void test73() {
		s.addBefore(r1);
		s.addAfter(r2);
		s.advance();
		RasterSeq c = s.clone();
		
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(r2,s.getCurrent());
		assertSame(r2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
	}
	
	public void test74() {
		s.addBefore(r1);
		s.addBefore(r2);
		s.addAfter(r3);
		s.advance();
		RasterSeq c = s.clone();
		assertSame(r3,s.getCurrent());
		assertSame(r3,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
		s.start();
		c.start();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(r1,s.getCurrent());
		assertSame(r1,c.getCurrent());
		s.advance();
		c.advance();
		assertTrue(s.hasCurrent());
		assertTrue(c.hasCurrent());
		assertSame(r2,s.getCurrent());
		assertSame(r2,c.getCurrent());
	}
	
	public void test75() {
		s.addBefore(r1);
		s.start();
		RasterSeq c = s.clone();
		s.addBefore(r2);
		s.start();
		assertSame(r2,s.getCurrent());
		assertSame(r1,c.getCurrent());
		c = s.clone();
		s.addBefore(r3);
		s.start();
		assertSame(r3,s.getCurrent());
		assertSame(r2,c.getCurrent());
		assertEquals(2,c.size());
	}
	
	public void test76() {
		s.addAfter(r3);
		s.addAfter(r2);
		s.addBefore(r1);
		s.advance();
		s.removeCurrent();
		
		RasterSeq c = s.clone();
		
		assertEquals(2,c.size());
		
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
		
		s.advance();
		c.advance();
		
		assertSame(r3,s.getCurrent());
		assertSame(r3,c.getCurrent());
	}

	public void test77() {
		s.addAfter(r4);
		s.addBefore(r5);
		s.addAfter(r1);
		s.advance();
		s.removeCurrent();
		
		RasterSeq c = s.clone();
		assertFalse(s.hasCurrent());
		c.advance();
		assertSame(r4,c.getCurrent());
		assertFalse(s.hasCurrent());
	}
	
	public void test78() {
		s.addBefore(r2);
		s.addAfter(r3);
		s.advance();
		
		RasterSeq c = s.clone();
		assertEquals(r3,c.getCurrent());
		c.advance();
		assertTrue(c.atEnd());
	}
	
	public void test79() {
		s.addBefore(r5);
		s.addBefore(r4);
		s.start();
		
		RasterSeq c = s.clone();
		c.removeCurrent();
		
		assertFalse(c.hasCurrent());
		assertEquals(r5,s.getCurrent());
	}
	
	public void test80() {
		s.addBefore(r1);
		s.addAfter(r2);
		s.advance();
		
		RasterSeq c = s.clone();
		s.addBefore(r3);
		c.addBefore(r4);
		s.start();
		s.advance();
		c.start();
		c.advance();
		
		assertSame(r3,s.getCurrent());
		assertSame(r4,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(r2,s.getCurrent());
		assertSame(r2,c.getCurrent());
		s.advance();
		c.advance();
		assertFalse(s.hasCurrent());
		assertFalse(c.hasCurrent());
		
		s.start();
		c.start();
		assertSame(r1,s.getCurrent());
		assertSame(r1,c.getCurrent());
		s.advance();
		c.advance();
		assertSame(r3,s.getCurrent());
		assertSame(r4,c.getCurrent());
	}

}
