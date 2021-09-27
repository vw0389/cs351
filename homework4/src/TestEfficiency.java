import edu.uwm.cs351.SequentialGallery;
import edu.uwm.cs351.Painting;
import edu.uwm.cs351.Raster;
import junit.framework.TestCase;

public class TestEfficiency extends TestCase {
	
	private static final int TESTS_CONSTANT = 1000000;
	private static final int TESTS_LINEAR = 1000;
	private static final int POWER = 20; // 1/2 million entries
	
	private SequentialGallery gal;
	
	private Painting p(int i) {
		return new Painting(new Raster(1, 1), "test", i);
	}
	
	protected void setUp() throws Exception {
		try {
			assert gal.size() == POWER : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		gal = new SequentialGallery();
		int max = (1 << (POWER)); // 2^(POWER) = 1 million
		for(int i=0; i<max; i++) {
			gal.insert(p(i));
			gal.advance();
		}
	}
	
	public void testSingleIteration() {
		gal.start();
		for(int i=0; i<TESTS_CONSTANT; i++) {
			assertTrue(gal.hasCurrent());
			assertEquals(p(i%gal.size()), gal.getCurrent());
			gal.advance();
			if(!gal.hasCurrent())
				gal.start();
		}
	}
	
	public void testInsert() {
		gal.start();
		for(int i=0; i<TESTS_LINEAR; i++)
			gal.insert(p(i));
	}
	
	public void testInsertConst() {
		for(int i=0; i<TESTS_CONSTANT; i++) {
			gal.insert(p(i));
			gal.advance();
		}
	}
	
	public void testRemove() {
		gal.start();
		while(gal.hasCurrent()) {
			for(int i=0; i<5; i++) {
				if(!gal.hasCurrent())
					gal.start();
				gal.advance();
			}
			if(!gal.hasCurrent())
				gal.start();
			gal.removeCurrent();
		}
	}
	
	public void testInsertAll() {
		gal.insertAll(gal);
	}
	
	public void testClone() {
		gal.clone();
	}
}
