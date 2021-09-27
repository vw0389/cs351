import java.util.Iterator;

import edu.uwm.cs351.Gallery;
import edu.uwm.cs351.Painting;
import edu.uwm.cs351.Raster;


public class TestEfficiency extends Gallery.TestEfficiency {
	//The (encapsulation-violating) data setters
	// are inherited from Gallery.TestEfficiency
	
	private static final int TESTS_CONSTANT = 10000000;
	private static final int TESTS_LINEAR = 100;
	private static final int POWER = 20; // 1/2 million entries
	
	private Gallery gal;
	
	private Painting p(int i) {
		return new Painting(new Raster(1, 1), "test", i);
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		try {
			assert gal.size() == POWER : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		gal = new Gallery();
		int max = (1 << (POWER)); // 2^(POWER) = 1 million
		Painting[] p = new Painting[max];
		for(int i=0; i<max; i++)
			p[i]=p(i);
		setData(gal, p);
		setManyItems(gal, max);
	}
	
	public void testAdd() {
		for (int i=0; i < TESTS_LINEAR; ++i) {
			assertFalse(gal.add(new Painting(new Raster(1, 1), "test", i)));
			assertTrue(gal.add(new Painting(new Raster(1, 1), "new", i)));
		}
	}

	public void testSize() {
		for (int i=0; i < TESTS_CONSTANT; ++i) {
			assertTrue(gal.size() > 100000);
		}
	}
	
	public void testRemove() {
		for(Iterator<Painting> it = gal.iterator(); it.hasNext(); ) {
			it.next();
			it.remove();
		}
	}
}

