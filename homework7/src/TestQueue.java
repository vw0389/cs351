
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import edu.uwm.cs.junit.LockedTestCase;
import edu.uwm.cs351.util.Queue;


public class TestQueue extends LockedTestCase {

	Queue<Integer> queue;

	@Override
	public void setUp(){
		try {
			assert 1/0 == 42 : "OK";
			System.err.println("Assertions must be enabled to use this test case.");
			System.err.println("In Eclipse: add -ea in the VM Arguments box under Run>Run Configurations>Arguments");
			assertFalse("Assertions must be -ea enabled in the Run Configuration>Arguments>VM Arguments",true);
		} catch (ArithmeticException ex) {
			// GOOD
		}
		queue = new Queue<>();
	}

	public void test00() {
		queue = new Queue<Integer>();
		assertTrue(queue.isEmpty());
		queue.enqueue(16);
		assertEquals(16, (int) queue.front());
		queue.enqueue(15);
		queue.enqueue(14);
		queue.dequeue();
		
	}

	public void test02() {
		queue = new Queue<Integer>();
		queue.enqueue(78);
		queue.enqueue(28);
		assertEquals(78, (int) queue.front());
	}
	
	public void test03() {
		queue.enqueue(42);
		assertFalse(queue.isEmpty());
		assertEquals(42, (int) queue.front());
		assertEquals(42, (int) queue.dequeue());
		assertTrue(queue.isEmpty());
	}
	
	public void test04() {
		queue.enqueue(2);
		queue.enqueue(53);
		assertEquals(2, (int) queue.front());
		
		queue.dequeue();
		queue.enqueue(9);
		assertEquals(Ti(784286663), (int) queue.dequeue());
		assertEquals(Ti(666513343), (int) queue.dequeue());
		queue.enqueue(5);
		queue.enqueue(6);
	}

	public void test05() {
		class Animal {
			private int agitation = 0;
			private String sound;
			public Animal(String sound) { this.sound = sound;}
			public String poke() { return ++agitation >= 3 ? sound.toUpperCase() +"!!!" : sound; }
		}
		Queue<Animal> animals = new Queue<>();

		animals.enqueue(new Animal("Moo"));
		animals.enqueue(new Animal("Ruff"));
		assertEquals(Ts(854091938), animals.dequeue().poke());
		
		animals.enqueue(new Animal("Baa"));
		assertEquals(Ts(782705085), animals.dequeue().poke());
		assertEquals("Baa", animals.front().poke());
		assertEquals("Baa", animals.front().poke());
		assertEquals(Ts(1694540270), animals.dequeue().poke());
		assertTrue(animals.isEmpty());
	}
	
	public void test08() {
		Queue<String> queue = new Queue<String>();
		queue.enqueue("Catelyn");
		queue.enqueue("Bill");
		queue.enqueue("Xander");
		queue.enqueue("Barb");
		
		assertEquals(Ts(1433139746), queue.front());
		assertEquals(Ts(1404734286), queue.dequeue());
		assertEquals(Ts(1053347783), queue.front());
		assertEquals("Bill", queue.dequeue());
		assertEquals("Xander", queue.dequeue());
		assertEquals("Barb", queue.dequeue());
	}
	
	public void test09() {
		Queue<Queue<String>> whoa = new Queue<>();
		whoa.enqueue(new Queue<>());
		whoa.front().enqueue("alpha");
		
		whoa.enqueue(new Queue<>());
		whoa.front().enqueue("bravo");
		
		whoa.enqueue(new Queue<>());
		whoa.front().enqueue("charlie");

		assertEquals(Ts(2054193437), whoa.front().dequeue());
		assertEquals(Ti(2032969321), whoa.dequeue().size());
		assertEquals(Ti(399486267), whoa.dequeue().size());
		assertEquals(Ti(1934792069), whoa.size());
	}
	
	public void test10() {
		for (int power = 0; power < 14; power++) {
			queue = new Queue<>();
			int i;
			for (i = 0; i < 1 << power; i++)
				queue.enqueue(i);
			testQueue(queue, IntStream.rangeClosed(0,i-1).toArray());
		}
	}

	public void test11() {
		queue.enqueue(0);
		testQueue(queue, 0);
		queue = new Queue<>();
		queue.enqueue(0);
		queue.enqueue(1);
		queue.dequeue();
		queue.dequeue();
		queue.enqueue(2);
		queue.enqueue(3);
		queue.enqueue(4);
		queue.enqueue(5);
		queue.enqueue(6);
		testQueue(queue, 2, 3, 4, 5, 6);
	}
	
	public void test12() {
		try {
			queue.enqueue(null);
			assertFalse("adding a null element should throw an IllegalArgumentException",true);
		}
		catch (RuntimeException ex) {
			assertTrue("wrong exception... " + ex, ex instanceof IllegalArgumentException);
		}
	}


	protected void testQueue(Queue<Integer> queue, int... elements)
	{
		assertEquals("size() is off... ",elements.length,queue.size());
		Integer current = null;
		int i = 0;
		while (!queue.isEmpty()) {
			try {
				assertEquals(elements[i], (int) queue.front());
				current = queue.dequeue();
				assertEquals(elements[i], (int) current);
			}
			catch (NoSuchElementException e) {
				assertTrue("expected "+elements[i]+" but received NoSuchElement exception...",false);
			}
			catch (IndexOutOfBoundsException e) {
				assertTrue("expected empty queue but received "+current,false);
			}
			catch (Exception e) {
				assertTrue("exception should not have been thrown... "+e.getMessage(),false);
			}
			++i;
		}
		assertTrue(queue.isEmpty());
		try {
			queue.front();
			assertFalse("front on empty queue should throw exception... ",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception... " + ex, ex instanceof NoSuchElementException);
		}
		try {
			queue.dequeue();
			assertFalse("dequeue on empty queue should throw exception",true);
		} catch (RuntimeException ex) {
			assertTrue("wrong exception... " + ex, ex instanceof NoSuchElementException);
		}
	}
}
