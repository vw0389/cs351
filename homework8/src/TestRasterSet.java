import java.awt.Color;
import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.RasterSet;
import edu.uwm.cs351.Pixel;
import edu.uwm.cs351.Raster;



public class TestRasterSet extends LockedTestCase {

	RasterSet r;
	Raster[] a, a0, a1, a2, a3, a4;

	/*private Raster new Raster(String year) {
		return (year == null)? null: new Raster(0, 0); }*/
	
	private <T> void assertException(Class<?> excClass, Runnable f) {
		try {
			f.run();
			assertFalse("Should have thrown an exception, not returned",true);
		} catch (RuntimeException ex) {
			if (!excClass.isInstance(ex)) {
				assertFalse("Wrong kind of exception thrown: "+ ex.getClass().getSimpleName(),true);
			}
		}		
	}
	
	private <T> boolean sameContents(T[] a, T[] b) {
		if(a.length != b.length)
			return false;
		for(T t : a) {
			boolean found = false;
			for(int i=0; i<b.length; i++)
				if (t==b[i])
					found = true;
			if (!found) return false;
		}
		for(T t : b) {
			boolean found = false;
			for(int i=0; i<a.length; i++)
				if (t==a[i])
					found = true;
			if (!found) return false;
		}
		return true;
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		try {
			assert r.size() == 42;
			assertTrue("Assertions not enabled.  Add -ea to VM Args Pane in Arguments tab of Run Configuration",false);
		} catch (NullPointerException ex) {
			assertTrue(true);
		}
		r = new RasterSet();
		a0 = new Raster[0];
		a1 = new Raster[1];
		a2 = new Raster[2];
		a3 = new Raster[3];
		a4 = new Raster[4];
	}
	
	//test0x: size, isEmpty, add

	public void test00() {
		assertEquals(0,r.size());
		assertTrue(r.isEmpty());
	}

	public void test01() {
		assertTrue(r.add(new Raster(1,1)));
		assertEquals(1, r.size());
		assertFalse(r.isEmpty());
	}
	
	public void test02() {
		assertException(IllegalArgumentException.class,() -> r.add(null));
	}
	
	public void test03() {
		assertTrue(r.add(new Raster(1,1)));
		assertTrue(r.add(new Raster(2,1)));
		assertTrue(r.add(new Raster(1,2)));
		assertEquals(3, r.size());
		assertFalse(r.isEmpty());
	}
	
	public void test04() {
		assertTrue(r.add(new Raster(1,1)));
		assertFalse(r.add(new Raster(1,1)));
		assertTrue(r.add(new Raster(2,1)));
		assertTrue(r.add(new Raster(1,2)));
		assertFalse(r.add(new Raster(2,1)));
		assertFalse(r.add(new Raster(1,2)));
		assertFalse(r.add(new Raster(1,2)));
		assertEquals(3, r.size());
		assertFalse(r.isEmpty());
	}
	
	public void test05() {
		a4[0] = new Raster(9, 9);
		a4[1] = new Raster(9, 9);
		a4[2] = new Raster(9, 9);
		a4[3] = new Raster(9, 9);
		a4[0].addPixel(new Pixel(4, 4, Color.GREEN));
		a4[1].addPixel(new Pixel(4, 4, Color.RED));
		a4[2].addPixel(new Pixel(4, 4, Color.BLUE));
		a4[3].addPixel(new Pixel(4, 4, Color.BLUE));
		assertTrue(r.add(a4[0]));
		assertTrue(r.add(a4[1]));
		assertTrue(r.add(a4[2]));
		assertFalse(r.add(a4[3]));
		assertEquals(3, r.size());
	}
	
	//test1x: contains, clear
	
	public void test10() {
		r.add(new Raster(5,5));
		assertTrue(r.contains(new Raster(5,5)));
		
		r.add(new Raster(1,2));
		assertTrue(r.contains(new Raster(1,2)));
		
		r.add(new Raster(3,2));
		assertTrue(r.contains(new Raster(3,2)));
		
		r.add(new Raster(6,6));
		assertTrue(r.contains(new Raster(6,6)));
	}
	
	public void test11() {
		assertException(IllegalArgumentException.class,() -> r.contains(null));
	}
	
	public void test12() {
		r.add(new Raster(5,5));
		r.add(new Raster(1,2));
		r.add(new Raster(3,2));
		r.add(new Raster(6,6));
		r.add(new Raster(1,2));
		
		assertTrue(r.contains(new Raster(6,6)));
		assertTrue(r.contains(new Raster(5,5)));
		assertTrue(r.contains(new Raster(1,2)));
		assertTrue(r.contains(new Raster(3,2)));
		assertFalse(r.contains(new Raster(0,0)));
		Raster r1 = new Raster(1,2);
		assertTrue(r.contains(r1));
		r1.addPixel(new Pixel(0,1,Color.RED));
		assertFalse(r.contains(r1));
	}
	
	public void test13() {
		r.add(new Raster(5,5));
		r.add(new Raster(1,2));
		r.add(new Raster(3,2));
		r.add(new Raster(6,6));
		r.add(new Raster(1,2));
		r.clear();
		assertFalse(r.contains(new Raster(6,6)));
		assertFalse(r.contains(new Raster(5,5)));
		assertEquals(0, r.size());
		assertTrue(r.isEmpty());
	}
	
	public void test14() {
		r.clear();
		assertFalse(r.contains(new Raster(5,5)));
		assertEquals(0, r.size());
		assertTrue(r.isEmpty());
	}
	
	//test2x: xFirst, yFirst
	
	public void test20() {
		r.yFirst();
		r.yFirst();
		
		r.add(new Raster(5,5));
		r.add(new Raster(1,2));
		r.add(new Raster(3,2));
		r.add(new Raster(6,6));
		r.add(new Raster(1,2));
		
		assertTrue(r.contains(new Raster(6,6)));
		assertTrue(r.contains(new Raster(5,5)));
		assertTrue(r.contains(new Raster(1,2)));
		assertTrue(r.contains(new Raster(3,2)));
		assertFalse(r.contains(new Raster(0,0)));
	}
	
	public void test21() {
		r.add(new Raster(1,1));
		r.yFirst();
		r.add(new Raster(1,2));
		r.xFirst();
		r.add(new Raster(2,2));
		r.yFirst();
		r.add(new Raster(1,2));
		r.xFirst();
		r.add(new Raster(2,1));
		r.yFirst();
		
		assertTrue(r.contains(new Raster(1,1)));
		r.xFirst();
		assertTrue(r.contains(new Raster(1,2)));
		r.yFirst();
		assertTrue(r.contains(new Raster(1,2)));
		r.xFirst();
		assertTrue(r.contains(new Raster(1,1)));
		r.yFirst();
		assertFalse(r.contains(new Raster(0,0)));
	}
	
	//test3x: xRange, yRange
	
	public void test30() {
		//a0 is an empty array of size 0
		assertTrue(sameContents(a0, r.xRange(Integer.MIN_VALUE, Integer.MAX_VALUE)));
		assertTrue(sameContents(a0, r.yRange(Integer.MIN_VALUE, Integer.MAX_VALUE)));
	}
	
	public void test31() {
		a4[0] = new Raster(0,0);
		a4[1] = new Raster(2,0);
		a4[2] = new Raster(4,0);
		a4[3] = new Raster(6,0);
		for(Raster ra : a4)
			r.add(ra);
		
		assertTrue(sameContents(a4, r.xRange(-1, 7)));
		assertTrue(sameContents(a4, r.yRange(-1, 1)));
		
		a2[0] = a4[1];
		a2[1] = a4[2];
		assertTrue(sameContents(a2, r.xRange(1, 5)));
		
		assertEquals(4, r.size());
	}
	
	public void test32() {
		a4[0] = new Raster(0,0);
		a4[1] = new Raster(1,0);
		a4[2] = new Raster(2,0);
		a4[3] = new Raster(3,0);
		for(Raster ra : a4)
			r.add(ra);
		
		assertTrue(sameContents(a4, r.xRange(0, 3)));
		assertTrue(sameContents(a4, r.yRange(0, 0)));
		
		a2[0] = a4[1];
		a2[1] = a4[2];
		assertTrue(sameContents(a2, r.xRange(1, 2)));
	}
	
	public void test33() {
		a4[0] = new Raster(5,5);
		a4[1] = new Raster(5,2);
		a4[2] = new Raster(5,9);
		a4[3] = new Raster(5,4);
		for(Raster ra : a4)
			r.add(ra);
		
		assertTrue(sameContents(a4, r.xRange(5, 5)));
		assertTrue(sameContents(a4, r.yRange(2, 9)));
		
		a1[0] = a4[2];
		assertTrue(sameContents(a1, r.yRange(9, Integer.MAX_VALUE)));
	}
	
	public void test34() {
		r.yFirst();
		a4[0] = new Raster(5,5);
		a4[1] = new Raster(8,2);
		a4[2] = new Raster(3,9);
		a4[3] = new Raster(12,4);
		for(Raster ra : a4)
			r.add(ra);
		
		assertTrue(sameContents(a4, r.xRange(Integer.MIN_VALUE, 14)));
		assertTrue(sameContents(a4, r.yRange(2, 9)));
		
		a1[0] = a4[1];
		assertTrue(sameContents(a1, r.yRange(Integer.MIN_VALUE, 3)));
		
		a2[0] = a4[0];
		a2[1] = a4[2];
		assertTrue(sameContents(a2, r.xRange(3, 5)));
	}
	
	public void test35() {
		r.xFirst();
		a1[0] = new Raster(1, 100);
		a2[0] = new Raster(12, 45);
		a2[1] = new Raster(12, 22);
		a3[0] = new Raster(31, 2);
		a3[1] = new Raster(66, 4);
		a3[2] = new Raster(67, 5);
		a4[0] = new Raster(112, 20);
		a4[1] = new Raster(135, 41);
		a4[2] = new Raster(166, 11);
		a4[3] = new Raster(134, 55);
		Raster[][] toAdd = new Raster[5][];
		toAdd[0] = a0;
		toAdd[1] = a1;
		toAdd[2] = a2;
		toAdd[3] = a3;
		toAdd[4] = a4;
		
		for(Raster[] ra : toAdd)
			for(Raster ras : ra)
				r.add(ras);
		
		
		assertTrue(sameContents(a0, r.xRange(Integer.MIN_VALUE, -1)));
		assertTrue(sameContents(a1, r.yRange(100, Integer.MAX_VALUE)));
		assertTrue(sameContents(a2, r.xRange(12, 12)));
		assertTrue(sameContents(a3, r.yRange(Integer.MIN_VALUE, 9)));
		assertTrue(sameContents(a4, r.xRange(100, Integer.MAX_VALUE)));
		
		r.yFirst();
		r.add(new Raster(55, 44));
		r.add(new Raster(99, 99));
		
		assertTrue(sameContents(a0, r.xRange(Integer.MIN_VALUE, -1)));
		assertTrue(sameContents(a1, r.yRange(100, Integer.MAX_VALUE)));
		assertTrue(sameContents(a2, r.xRange(12, 12)));
		assertTrue(sameContents(a3, r.yRange(Integer.MIN_VALUE, 9)));
		assertTrue(sameContents(a4, r.xRange(100, Integer.MAX_VALUE)));
	}
	
	public void test36() {
		a4[0] = new Raster(10, 10);
		a4[1] = new Raster(9, 4);
		a4[2] = new Raster(10, 5);
		a4[3] = new Raster(10, 12);
		
		a3[0] = a4[0];
		a3[1] = a4[2];
		a3[2] = a4[3];
		
		for(Raster ra : a4)
			r.add(ra);
		
		assertTrue(sameContents(a3, r.xRange(10, 15)));
	}
	
	public void test37() {
		a4[0] = new Raster(10, 10);
		a4[1] = new Raster(9, 12);
		a4[2] = new Raster(12, 10);
		a4[3] = new Raster(10, 6);
		
		a3[0] = a4[0];
		a3[1] = a4[2];
		a3[2] = a4[3];
		
		r.yFirst();
		for(Raster ra : a4)
			r.add(ra);
		
		assertTrue(sameContents(a3, r.yRange(5, 10)));
	}
	
	public void test38() {
		a4[0] = new Raster(9, 9);
		a4[1] = new Raster(9, 9);
		a4[2] = new Raster(9, 9);
		a4[3] = new Raster(9, 9);
		a4[0].addPixel(new Pixel(4, 4, Color.GREEN));
		a4[1].addPixel(new Pixel(4, 4, Color.RED));
		a4[2].addPixel(new Pixel(4, 4, Color.BLUE));
		a4[3].addPixel(new Pixel(4, 4, Color.BLACK));
		for(Raster ra : a4)
			r.add(ra);
		assertTrue(sameContents(a4, r.yRange(9, 9)));
	}
}
