import edu.uwm.cs351.Pixel;
import edu.uwm.cs351.Raster;
import edu.uwm.cs.junit.LockedTestCase;
import java.awt.Color;

public class TestRaster extends LockedTestCase{
	protected void assertNotEquals(Object x, Object y) {
		if (x == null) {
			assertFalse(x == y);
		} else {
			assertFalse(x + " should not equal " + y, x.equals(y));
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
	
	protected Raster rBig() {
		Raster ras = new Raster(10,10);
		for (int x=0; x<10; x++)
			for(int y=0; y<10; y++)
				if((x*y % 2) == 1)
					ras.getPixel(x, y).invert();
		return ras;
	}
	
	/// test: the locked tests
	
		public void test() {
			Raster r = new Raster(2,2);
			assertEquals(Tb(1399208723), r.addPixel(new Pixel(0, 0, Color.BLACK)));
			assertEquals(Tb(1216949804), r.addPixel(new Pixel(0, 0, Color.BLACK)));
			assertEquals(Tb(92951991), r.addPixel(new Pixel(0, 1, Color.WHITE)));
			
		}
	
	/// test0X: toString tests
	
		public void test00() {
			Raster r = new Raster(0,0);
			assertEquals("0 by 0 raster:", r.toString());
		}
		
		public void test01() {
			Raster r1 = new Raster(0,1);
			assertEquals("0 by 1 raster:", r1.toString());
			Raster r2 = new Raster(1,0);
			assertEquals("1 by 0 raster:", r2.toString());
		}
		
		public void test02() {
			Raster r = new Raster(1,1);
			assertEquals("1 by 1 raster: " + new Pixel(0,0).toString(), r.toString());
		}
		
		public void test03() {
			Raster r = new Raster(1,2);
			assertEquals("1 by 2 raster: " + new Pixel(0,0).toString()
					+ " " + new Pixel(0,1).toString(), r.toString());
		}
		
		public void test04() {
			Raster r = new Raster(2,1);
			assertEquals("2 by 1 raster: " + new Pixel(0,0).toString()
					+ " " + new Pixel(1,0).toString(), r.toString());
		}
		
		public void test05() {
			Raster r = new Raster(2,2);
			assertEquals("2 by 2 raster: " + new Pixel(0,0).toString()
					+ " " + new Pixel(0,1).toString()
					+ " " + new Pixel(1,0).toString()
					+ " " + new Pixel(1,1).toString()
					, r.toString());
		}
		
		public void test06() {
			Raster r = new Raster(3,3);
			assertEquals("3 by 3 raster: " + new Pixel(0,0).toString()
					+ " " + new Pixel(0,1).toString()
					+ " " + new Pixel(0,2).toString()
					+ " " + new Pixel(1,0).toString()
					+ " " + new Pixel(1,1).toString()
					+ " " + new Pixel(1,2).toString()
					+ " " + new Pixel(2,0).toString()
					+ " " + new Pixel(2,1).toString()
					+ " " + new Pixel(2,2).toString()
					, r.toString());
		}
		
		public void test07() {
			assertException(IllegalArgumentException.class, () -> new Raster(-8, -8));
			assertException(IllegalArgumentException.class, () -> new Raster(-1, 2));
			assertException(IllegalArgumentException.class, () -> new Raster(3, -1));
		}
		
	//test1x: equals tests
		
		public void test10() {
			assertTrue(new Raster(0,0).equals(new Raster(0,0)));
		}
		
		public void test11() {
			assertFalse(new Raster(0,0).equals(new Raster(0,1)));
			assertFalse(new Raster(0,0).equals(new Raster(1,0)));
		}
		
		public void test12() {
			assertTrue(new Raster(1,1).equals(new Raster(1,1)));
		}
		
		public void test14() {
			assertFalse(new Raster(0,0).equals(null));
		}
		
		public void test15() {
			assertFalse(new Raster(0,0).equals(new Pixel(0,0)));
		}
		
		public void test17() {
			Raster r1 = rBig();
			Raster r2 = rBig();
			assertTrue(r1.equals(r2));
		}
		
	//test2x: addPixel tests
		
		
		public void test20() {
			Raster r = new Raster(1,1);
			assertFalse(r.addPixel(new Pixel(0,0,Color.WHITE)));
		}
		
		public void test21() {
			Raster r = new Raster(1,1);
			assertTrue(r.addPixel(new Pixel(0,0,Color.BLACK)));
		}
		
		public void test22() {
			Raster r1 = new Raster(1,1);
			Raster r2 = new Raster(1,1);
			r2.addPixel(new Pixel(0,0,Color.WHITE));
			assertTrue(r1.equals(r2));
		}
		
		public void test23() {
			Raster r1 = new Raster(1,1);
			Raster r2 = new Raster(1,1);
			r2.addPixel(new Pixel(0,0,Color.BLACK));
			assertFalse(r1.equals(r2));
		}
		
		public void test24() {
			Raster r1 = rBig();
			Raster r2 = rBig();
			r2.addPixel(new Pixel(0,0,Color.RED));
			assertFalse(r1.equals(r2));
		}
		
		public void test25() {
			Raster r1 = rBig();
			Raster r2 = rBig();
			r2.addPixel(new Pixel(9,9,Color.RED));
			assertFalse(r1.equals(r2));
		}
		
		public void test26() {
			Raster r = new Raster(1,1);
			assertException(IllegalArgumentException.class, () -> r.addPixel(null));

		}
		
		public void test27() {
			Raster r1 = new Raster(0,0);
			assertException(IllegalArgumentException.class, () -> r1.addPixel(new Pixel(0,0)));
			Raster r2 = new Raster(1,0);
			assertException(IllegalArgumentException.class, () -> r2.addPixel(new Pixel(0,0)));
			Raster r3 = new Raster(0,1);
			assertException(IllegalArgumentException.class, () -> r3.addPixel(new Pixel(0,0)));
		}
		
		public void test28() {
			Raster r = new Raster(1,1);
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(1,0)));
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(0,1)));
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(1,1)));
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(-1,0)));
		}
		
		public void test29() {
			Raster r = rBig();
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(-1,0)));
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(0,-1)));
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(10,1)));
			assertException(IllegalArgumentException.class, () -> r.addPixel(new Pixel(4,10)));
		}
		
	//test3x: getPixel tests
		
		public void test30() {
			assertEquals(new Pixel(0,0), new Raster(1,1).getPixel(0,0));
		}
		
		public void test31() {
			Raster r = new Raster(1,1);
			r.addPixel(new Pixel(0,0,Color.BLACK));
			assertEquals(new Pixel(0,0,Color.BLACK), r.getPixel(0,0));
		}
		
		public void test32() {
			Raster r = new Raster(2,2);
			Pixel p1 = new Pixel(0,1,Color.RED);
			Pixel p2 = new Pixel(1,0,Color.GREEN);
			Pixel p3 = new Pixel(1,1,Color.BLUE);
			r.addPixel(p1);
			r.addPixel(p2);
			r.addPixel(p3);
			assertEquals(p1, r.getPixel(0,1));
			assertEquals(p2, r.getPixel(1,0));
			assertEquals(p3, r.getPixel(1,1));
			assertEquals(new Pixel(0,0), r.getPixel(0, 0));
		}
		
		public void test33() {
			Raster r = rBig();
			assertException(IllegalArgumentException.class, () -> r.getPixel(-1,0));
			assertException(IllegalArgumentException.class, () -> r.getPixel(0,-1));
			assertException(IllegalArgumentException.class, () -> r.getPixel(10,1));
			assertException(IllegalArgumentException.class, () -> r.getPixel(4,10));
		}
}
