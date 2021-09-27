import edu.uwm.cs351.Pixel;
import edu.uwm.cs.junit.LockedTestCase;
import java.awt.Color;
import java.awt.Point;

public class TestPixel extends LockedTestCase {
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
	
	/// test: the locked tests
	
		public void test() {
			Pixel p = new Pixel(0,0);
			//what is the default color for a pixel?
			assertEquals(Tb(340861853), p.color().equals(Color.BLACK));
			assertEquals(Tb(109607826), p.color().equals(Color.WHITE));
			//what is the toString format for a pixel?
			assertEquals(Ts(489400785) + p.color().toString() + ">", p.toString());
			p = new Pixel(5, 5, new Color(12, 8, 64));
			//enter the hashCode for p in binary according to the design described in the handout
			//the String object is just for the locked test
			String hashOfP = Ts(571848904);
			p.setColor(Color.WHITE);
			p.invert();
			//enter the color of p
			String colorOfP = Ts(906647067);
			
		}
	
	/// test0X: toString tests
	
		public void test00() {
			assertEquals("<0,0," + Color.WHITE.toString() + ">", new Pixel(0, 0).toString());
		}
		
		public void test01() {
			assertEquals("<1,3," + Color.WHITE.toString() + ">", new Pixel(1, 3).toString());
		}
		
		public void test02() {
			assertEquals("<-10,6," + Color.WHITE.toString() + ">", new Pixel(-10, 6).toString());
		}
		
		public void test03() {
			assertEquals("<300,-4," + Color.WHITE.toString() + ">", new Pixel(300, -4).toString());
		}
		
		public void test04() {
			assertEquals("<3,2," + Color.CYAN.toString() + ">", new Pixel(3, 2, Color.CYAN).toString());
		}
		
		public void test05() {
			assertEquals("<0,14," + Color.RED.toString() + ">", new Pixel(new Point(0, 14), Color.RED).toString());	
		}
		
		public void test06() {
			assertEquals("<7000,-7000," + Color.WHITE.toString() + ">", new Pixel(new Point(7000, -7000)).toString());	
		}
		
		public void test07() {
			assertException(IllegalArgumentException.class, () -> new Pixel(null));
		}
		
		public void test08() {
			assertException(IllegalArgumentException.class, () -> new Pixel(0,0,null));
		}
		
		public void test09() {
			assertException(IllegalArgumentException.class, () -> new Pixel(new Point(0,0),null));
			assertException(IllegalArgumentException.class, () -> new Pixel(null,Color.GREEN));
			assertException(IllegalArgumentException.class, () -> new Pixel(null,null));
		}
		
	/// test1X: equals tests
		
		public void test10() {
			assertTrue(new Pixel(-4,4).equals(new Pixel(-4,4)));
		}
		
		public void test11() {
			assertFalse(new Pixel(-4,4).equals(new Pixel(4,4)));
		}
		
		public void test12() {
			assertFalse(new Pixel(-4,4,Color.WHITE).equals(new Pixel(4,-4, Color.BLACK)));
		}
		
		public void test13() {
			assertFalse(new Pixel(3,2).equals(new Pixel(3,-2,Color.WHITE)));
		}
		
		public void test14() {
			assertFalse(new Pixel(0,0).equals(null));
		}
		
		public void test15() {
			assertFalse(new Pixel(-4,4).equals(new Point(-4, 4)));
		}
		
		public void test16() {
			assertTrue(new Pixel(10,10).equals(new Pixel(new Point(10,10), Color.WHITE)));
		}
		
		public void test17() {
			Object o1 = new Pixel(2,-3, Color.BLUE);
			Object o2 = new Pixel(new Point(2, -3), Color.BLUE);
			assertTrue(o1.equals(o2));
		}
		
		public void test18() {
			Pixel p1 = new Pixel(16,215);
			Pixel p2 = new Pixel(31,-266);
			assertFalse(p1.equals(p2));
			assertFalse(p2.equals(p1));
		}
		
	/// test2X: hash code
		
		public void test20() {
			assertEquals(new Pixel(2,2).hashCode(), new Pixel(2,2).hashCode());
		}
		
		public void test21() {
			assertEquals(new Pixel(-4,4).hashCode(), new Pixel(new Point(-4,4),Color.WHITE).hashCode());
		}
		
		public void test22() {
			assertNotEquals(new Pixel(1,0),new Pixel(0,0));
			assertNotEquals(new Pixel(1,0),new Pixel(0,1));
			assertNotEquals(new Pixel(1,0),new Pixel(-1,0));
			assertNotEquals(new Pixel(0,1),new Pixel(0,0));
			assertNotEquals(new Pixel(0,1),new Pixel(-1,0));
			assertNotEquals(new Pixel(0,1),new Pixel(0,-1));
			assertNotEquals(new Pixel(-1,0),new Pixel(0,-1));
			assertNotEquals(new Pixel(-1,0),new Pixel(0,0));
			assertNotEquals(new Pixel(0,-1),new Pixel(0,0));
			assertNotEquals(new Pixel(0,-1),new Pixel(1,0));
			assertNotEquals(new Pixel(1,1),new Pixel(0,2));
			assertNotEquals(new Pixel(1,1),new Pixel(0,1));
			assertNotEquals(new Pixel(1,1),new Pixel(1,0));
			assertNotEquals(new Pixel(1,1),new Pixel(0,2));
			assertNotEquals(new Pixel(1,1),new Pixel(0,0));
			assertNotEquals(new Pixel(1,1),new Pixel(0,-1));
			assertNotEquals(new Pixel(1,1),new Pixel(-1,0));
			assertNotEquals(new Pixel(1,1),new Pixel(-1,-1));
		}
		
		public void test23() {
			assertEquals(new Pixel(1,1),new Pixel(1,1,Color.WHITE));
			assertNotEquals(new Pixel(1,1),new Pixel(1,1,Color.BLACK));
			assertNotEquals(new Pixel(1,1),new Pixel(1,1,Color.MAGENTA));
			assertNotEquals(new Pixel(1,1),new Pixel(1,1,Color.LIGHT_GRAY));
			assertNotEquals(new Pixel(1,1),new Pixel(1,1,Color.YELLOW));
			assertNotEquals(new Pixel(1,1, Color.BLACK),new Pixel(1,1,Color.DARK_GRAY));
			assertNotEquals(new Pixel(1,1, Color.BLACK),new Pixel(1,1,Color.RED));
			assertNotEquals(new Pixel(1,1, Color.DARK_GRAY),new Pixel(1,1,Color.GRAY));
			assertNotEquals(new Pixel(1,1, Color.GRAY),new Pixel(1,1,Color.LIGHT_GRAY));
		}

		public void test28() {
			for (int a1=-5; a1 <= 5; ++a1) {
				for (int a2 = -5; a2 <= 5; ++a2) {
					for (int b1 = -5; b1 <= 5; ++b1) {
						for (int b2 = -5; b2 <= 5; ++b2) {
							if (a1 == a2 && b1 == b2) continue;
							Pixel p1 = new Pixel(a1,b1);
							Pixel p2 = new Pixel(a2,b2);
							int c1 = p1.hashCode();
							int c2 = p2.hashCode();
							if (c1 == c2) {
								assertFalse(p1 + " code of " + c1 + " should not collide with " + p2 + " code of " + c2, true);
							}						
						}
					}
				}
			}
		}
		
		public void test29() {
			for (int r1=0; r1 <= 255; r1 += 24) {
				for (int r2=0; r2 <= 255; r2 += 24) {
					for (int g1=0; g1 <= 255; g1 += 24) {
						for (int g2=0; g2 <= 255; g2 += 24) {
							for (int b1=0; b1 <= 255; b1 += 24) {
								for (int b2=0; b2 <= 255; b2 += 24) {
									if (r1 == r2 && g1 == g2 && b1 == b2) continue;
									Pixel p1 = new Pixel(0,0,new Color(r1, g1, b1));
									Pixel p2 = new Pixel(0,0,new Color(r2, g2, b2));
									int c1 = p1.hashCode();
									int c2 = p2.hashCode();
									if (c1 == c2) {
										assertFalse(p1 + " code of " + c1 + " should not collide with " + p2 + " code of " + c2, true);
									}
								}
							}
						}
					}
				}
			}
		}
		
	/// test3X: loc tests.
		
		public void test30() {
			assertEquals(new Point(0, 0), new Pixel(0, 0).loc());
		}
		
		public void test31() {
			assertEquals(new Point(10, 0), new Pixel(10, 0).loc());
		}
		
		public void test32() {
			assertEquals(new Point(-10, 0), new Pixel(-10, 0).loc());
		}
		
		public void test33() {
			assertEquals(new Point(5, 9), new Pixel(5, 9).loc());
		}
		
		public void test34() {
			assertEquals(new Point(11, 11), new Pixel(new Point(11,11)).loc());
		}
		
		public void test35() {
			Point p = new Point(5, 100);
			assertEquals(p, new Pixel(p).loc());
			assertEquals(p, new Pixel(p, Color.BLUE).loc());
		}
		
		public void test36() {
			assertEquals(new Point(4, 4), new Pixel(4, 4).loc());
			assertEquals(new Point(4, 4), new Pixel(4, 4, Color.YELLOW).loc());
		}
		
		public void test37() {
			Pixel px = new Pixel(0,0);
			Point p = px.loc();
			p.x = 5;
			p.y = 5;
			assertEquals(new Point(5, 5), p);
			assertEquals(new Point(0, 0), px.loc());
		}
		
	/// test4X: color tests.
		
		public void test40() {
			assertEquals(Color.WHITE, new Pixel(0, 0).color());
		}
		
		public void test41() {
			assertEquals(Color.ORANGE, new Pixel(10, 0, Color.ORANGE).color());
		}
		
		public void test42() {
			assertEquals(Color.WHITE, new Pixel(new Point(400,800)).color());
		}
		
		public void test43() {
			assertEquals(Color.PINK, new Pixel(new Point(400,800), Color.PINK).color());
		}
		
		public void test44() {
			Pixel px = new Pixel(new Point(400,800));
			px.setColor(Color.PINK);
			assertEquals(Color.PINK, px.color());
		}
		
		//THIS IS A SILLY TEST
		//THINK ABOUT WHY
		public void test45() {
			Pixel px = new Pixel(new Point(400,800), Color.MAGENTA);
			Color c = px.color();
			c = Color.YELLOW;
			assertFalse(c.equals(px.color()));
		}
		
		public void test46() {
			Pixel px = new Pixel(0,0);
			assertException(IllegalArgumentException.class, () -> px.setColor(null));
		}
		
	// test5x: invert tests
		
		public void test50() {
			Pixel px = new Pixel(0,0);
			px.invert();
			assertEquals(Color.BLACK, px.color());
		}
		
		public void test51() {
			Pixel px = new Pixel(0,0, new Color(10, 0, 0));
			px.invert();
			assertEquals(new Color(245, 255, 255), px.color());
		}
		
		public void test52() {
			for (int r = 0; r <= 255; r += 1)
				for (int g = 0; g <= 255; g += 1)
					for (int b = 0; b <= 255; b += 1) {
						Pixel px = new Pixel(0, 0, new Color(r, g, b));
						px.invert();
						assertEquals(new Color(255 - r, 255 - g, 255 - b), px.color());
					}
		}
		
	
}
