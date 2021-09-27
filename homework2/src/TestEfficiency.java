import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import junit.framework.TestCase;
import edu.uwm.cs351.Raster;
import edu.uwm.cs351.RasterSeq;


public class TestEfficiency extends TestCase {

	Raster r1 = new Raster(1,1);
	Raster r2 = new Raster(100,1);
	Raster r3 = new Raster(100,100);
	Raster r4 = new Raster(1,200);
	Raster r5 = new Raster(1,100);
	Raster r6 = new Raster(66,1);
	Raster r7 = new Raster(1,707);
	Raster r8 = new Raster(88,88);

	Raster ra[] = {null, r1, r2, r3, r4, r5, r6, r7, r8};
	
	RasterSeq s;
	Random r;
	
	@Override
	public void setUp() {
		s = new RasterSeq();
		r = new Random();
		try {
			assert 1/(int)(r5.x()-1) == 42 : "OK";
			assertTrue(true);
		} catch (ArithmeticException ex) {
			assertFalse("Assertions must NOT be enabled while running efficiency tests.",true);
		}
	}

	private static final int MAX_LENGTH = 1000000;
	private static final int SAMPLE = 100;
	
	public void testLong() {
		for (int i=0; i < MAX_LENGTH; ++i) {
			s.addAfter(ra[i%6]);
			s.advance();
		}
		
		int sum = 0;
		s.start();
		for (int j=0; j < SAMPLE; ++j) {
			int n = r.nextInt(MAX_LENGTH/SAMPLE);
			for (int i=0; i < n; ++i) {
				s.advance();
			}
			sum += n;
			assertSame(ra[sum%6],s.getCurrent());
		}
	}
	
	private static final int MAX_WIDTH = 100000;
	
	public void testWide() {
		RasterSeq[] a = new RasterSeq[MAX_WIDTH];
		for (int i=0; i < MAX_WIDTH; ++i) {
			a[i] = s = new RasterSeq();
			int n = r.nextInt(SAMPLE);
			for (int j=0; j < n; ++j) {
				s.addAfter(ra[j%6]);
				s.advance();
			}
		}
		
		for (int j = 0; j < SAMPLE; ++j) {
			int i = r.nextInt(a.length);
			s = a[i];
			if (s.size() == 0) continue;
			int n = r.nextInt(s.size());
			s.start();
			for (int k=0; k < n; ++k) {
				s.advance();
			}
			assertSame(ra[n%6],s.getCurrent());
		}
	}
	
	public void testStochastic() {
		List<RasterSeq> ss = new ArrayList<RasterSeq>();
		ss.add(s);
		int max = 1;
		for (int i=0; i < MAX_LENGTH; ++i) {
			if (r.nextBoolean()) {
				s = new RasterSeq();
				s.addBefore(r3);
				ss.add(s);
			} else {
				s.addAll(s); // double size of s
				if (s.size() > max) {
					max = s.size();
					// System.out.println("Reached " + max);
				}
			}
		}
	}
}
