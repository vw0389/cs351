import junit.framework.TestCase;
import edu.uwm.cs351.Raster;
import edu.uwm.cs351.RasterSet;


//TEST reorder should be fairly balanced when not in sorted order
//TEST xFirst repeatedly should be constant
//TEST getXRange for super narrow range should be logn, when comp == BY_X

public class TestEfficiency extends TestCase {
	RasterSet b;
    
    private static final int POWER = 20;
    private static final int TESTS = 500000;
    
    
	protected void setUp() throws Exception {
		super.setUp();

		try {assert 1/0 == 42 : "OK";}
		catch (ArithmeticException ex) {
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);}
		
		b = new RasterSet();
		int max = (1 << (POWER)); // 2^(POWER) = one million
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				b.add(new Raster(0, i));
			}
		}
	}
	
    @Override
    protected void tearDown() {
    	b = null;
    }

    public void testSize() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals((1<<(POWER-1))-1,b.size());
    	}
    }
    
    public void testIsEmpty() {
    	for (int i=0; i < TESTS; ++i) {
    		assertEquals(false,b.isEmpty());
    	}
    }
    
    public void testContains() {
    	int max = (1 << (POWER));
    	for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			for (int i=1 << (power-1); i < max; i += incr) {
				assertTrue(b.contains(new Raster(0, i)));
			}
		}
    }
    
    public void testXFirst() {
    	for (int i=0; i < TESTS; ++i) {
    		b.xFirst();
    	}
    }
    
    public void testYFirst() {
    	for (int i=0; i < TESTS; ++i) {
    		b.yFirst();
    	}
    }
    
    public void testXRange() {
    	b.xFirst();
    	for (int i=0; i < 1000; ++i) {
    		assertEquals(0, b.xRange(1, Integer.MAX_VALUE).length);
    		assertEquals(0, b.xRange(Integer.MIN_VALUE, -1).length);
    	}
    }
    
    public void testYRange() {
    	b.yFirst();
    	for (int i=0; i < 1000; ++i) {
    		assertEquals(1, b.yRange(64, 64).length);
    		assertEquals(0, b.yRange(Integer.MIN_VALUE, -1).length);
    	}
    }

}
