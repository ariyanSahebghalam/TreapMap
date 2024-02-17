import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TreapMapUnitTester {

	@Test (timeout = 500)
	public void test01_constructorInterfaceTester_5_pts() {
		Field[] fields = TreapMap.class.getDeclaredFields();
		for (Field f: fields){
			assertTrue("List class contains a public field", 
					!Modifier.isPublic(f.getModifiers()));
		}

		assertTrue ("Number of constructors != 1", 
				TreapMap.class.getDeclaredConstructors().length == 1);

		assertTrue ("List interface not implemented or other interfaces are", 
				TreapMap.class.getInterfaces().length == 1
				&& TreapMap.class.getInterfaces()[0].getName().equals("java.util.Map")
				|| TreapMap.class.getInterfaces().length == 2 
				&& TreapMap.class.getInterfaces()[0].getName().equals("java.util.Map")
				||TreapMap.class.getInterfaces()[0].getName().equals("java.util.Iterable"));
	}

	@Test (timeout = 500)
	public void test02_addTester1_5_pts() {
		Map<Integer, String> map = new TreapMap<>();
		map.put(123, "abc");
		assertTrue(("abc").equals(map.get(123)) || "{123=abc}".equals(map.toString()));
	}

	@Test (timeout = 500)
	public void test03_addTester2_10_pts() {
		TreapMap<Integer, String> map = new TreapMap<>();
		map.put(0, "0");
		map.put(0, "1");
		map.put(1, "2");
		assertTrue("{0=1, 1=2}".equals(map.toString())
				|| map.get(0).equals("1") && map.get(1).equals("2"));
	}

	@Test (timeout = 500)
	public void test04_removeTester1_5_pts() {
		TreapMap<Integer, String> map = new TreapMap<>();
		map.put(1, "2");
		map.remove(0);
		assertTrue(map.size() == 1);
		map.remove(1);
		assertTrue(map.size() == 0);
	}

	@Test (timeout = 500)
	public void test05_removeTester2_10_pts() {
		TreapMap<Integer, String> map = new TreapMap<>();
		map.put(map.size(), "1"); map.put(map.size(), "2"); map.put(map.size(), "3");
		assertTrue(map.size() == 3);
		map.remove(1);
		assertTrue(map.size() == 2);
		assertTrue("{0=1, 2=3}".equals(map.toString()) 
				|| map.get(0).equals("1") && map.get(2).equals("3"));
	}

	@Test (timeout = 2000)
	public void test06_randomPutRemove_20_pts(){
		final int SIZE = 30;
		Random rnd = new Random();
		Map <Integer, Integer> map1 = new TreeMap<>();
		Map <Integer, Integer> map2 = new TreapMap<>();
		//TODO change max i to 30 K
		for (int i = 0; i < SIZE; i++){
			if (rnd.nextDouble() < 0.25){
				int key = rnd.nextInt(SIZE);
				map1.put(key, i);
				map2.put(key, i);
			}
			else{
				if (map1.size() > 0){
					int key = rnd.nextInt(SIZE);
					map1.remove(key);
					map2.remove(key);
				}
			}
			//System.out.println(map1.size() +", " + map2.size());
		}

		Set <Integer> keySet = map1.keySet();
		System.out.println("TreeMap : " + map1);
		System.out.println("TreapMap: " + map2);
		assertTrue(map1.size() == map2.size());
		for (int key: keySet) {
			//System.out.println("get(" + key + "): " + map1.get(key) + " : " + map2.get(key));
			assertTrue("randomPutRemove_20_pts: " + map2.size() + ": " + key + "\n"+ map1 + "\n" + map2, 
					map1.get(key) == (map2.get(key)) || map1.get(key).equals(map2.get(key)));
		}
	}

	@Test (timeout = 500)
	public void test07_getTester1_5_pts() {
		TreapMap<Integer, String> map = new TreapMap<>();
		assertTrue(map.get(1) == null);
	}

	@Test (timeout = 500)
	public void test08_getTester2_5_pts() {
		TreapMap<Integer, String> map = new TreapMap<>();
		map.put(0, "0"); map.put(1, "1");
		assertTrue(map.get(0).equals("0"));
		assertTrue(map.get(1).equals("1"));
	}

	@Test (timeout = 500)
	public void test09_clearTester_5_pts() {
		TreapMap<Integer, String> map = new TreapMap<>();
		assertTrue(map.size() == 0);

		map.put(1, "1"); map.put(2, "2"); map.put(3, "3");
		assertTrue(map.size() != 0);

		map.clear();
		assertTrue(map.size() == 0);
		assertTrue(map.get(1) == null);
	}

	@Test (timeout = 500)
	public void test10_sizeTester_5_pts() {
		TreapMap<Integer, String> list = new TreapMap<>();
		assertTrue(list.size() == 0);
		list.put(1, "1");
		assertTrue(list.size() == 1);
		list.put(2, "9");
		assertTrue(list.size() == 2);
	}

	@Test (timeout = 500)
	public void test11_toStringTester1_5_pts() {
		TreapMap<Integer, String> list = new TreapMap<>();
		assertTrue(list.toString().equals("{}"));
	}

	@Test (timeout = 500)
	public void test12_toStringTester2_10_pts() {
		TreapMap<Integer, String> map = new TreapMap<>();
		map.put(1, "1"); map.put(2, "2"); map.put(3, "3");
		assertTrue("{1=1, 2=2, 3=3}".equals(map.toString()));
	}
	
	@Test (timeout = 2000)
	public void test13_keySetTest_10pts() {
		final int n = 299;
		Map <Integer, Integer> map = new TreapMap<>();
		for (int i = 0; i < n; i++) map.put(i, i);

		Iterator<Integer> setIterator = map.keySet().iterator();
		int value = 0;
		while(setIterator.hasNext()) {
			assertTrue("Treap Iterator: wrong value", setIterator.next() == value++);
		}
	}

	@Test (timeout = 2000)
	public void test14_logPerformanceAdd_FAIL_MINUS_20_pts(){
		final int SIZE = 0x10000;
		final int CONST_FACTOR = 10;
		Map <Integer, Integer> map1 = new TreeMap<>();
		Map <Integer, Integer> map2 = new TreapMap<>();

		//TreeMap put at the end (benchmark)
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++){
				map1.put(map1.size(), i);
		}
		long endTime = System.currentTimeMillis();
		long elapsedTreeMap = endTime - startTime;

		//TreapMap put at the end
		startTime = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++){
			map2.put(map2.size(), i);
		}
		endTime = System.currentTimeMillis();
		long elapsedTreapMap = endTime - startTime;
		
		map1.clear();
		//TreeMap put at the beginning (replace, benchmark)
		startTime = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++){
				map1.put(0, i);
		}
		endTime = System.currentTimeMillis();
		long elapsedTreeMap0 = endTime - startTime;

		map2.clear();
		//TreapMap put at the beginning (replace)
		startTime = System.currentTimeMillis();
		for (int i = 0; i < SIZE; i++){
			map2.put(0, i);
		}
		endTime = System.currentTimeMillis();
		long elapsedTreapMap0 = endTime - startTime;
		
		System.out.println("TreeMap.put  (ms):  " + elapsedTreeMap + 
				"\nTreapMap.put (ms):  " + elapsedTreapMap + 
				"\nTreeMap.put0  (ms): " + elapsedTreeMap0 +
				"\nTreapMap.put0 (ms): " + elapsedTreapMap0);
		
		assertTrue("Non-logarithmic put() performance" , 
				elapsedTreapMap < elapsedTreeMap * CONST_FACTOR 
				&& elapsedTreapMap0 < elapsedTreeMap0 * CONST_FACTOR);
	}
	
	@Test (timeout = 20000)
	public void test15_logPerformanceGet_FAIL_MINUS_20_pts(){
		final int SIZE = 0x10000;
		final int CONST_FACTOR = 10;
		Map <Integer, Integer> map1 = new TreeMap<>();
		Map <Integer, Integer> map2 = new TreapMap<>();

		//fill with data
		for (int i = 0; i < SIZE; i++) map1.put(i, i);
		for (int i = 0; i < SIZE; i++) map2.put(i, i);

		//TreeMap get (benchmark)
		long startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
				map1.get(i);
		}
		long endTime = System.nanoTime();
		long elapsedTreeMapGet = endTime - startTime;

		//TreapMap get
		startTime = System.nanoTime();
		for (int i = 0; i < SIZE; i++){
			map2.get(i);
		}
		endTime = System.nanoTime();
		long elapsedTreapMapGet = endTime - startTime;
		
		System.out.println("TreeMap get time (ms): " + elapsedTreeMapGet/1e6 + "\nTreapMap get time (ms): " + elapsedTreapMapGet/1e6);
		
		assertTrue("get in TreapMap must be about as fast as in java.util.TreeMap" , 
				elapsedTreapMapGet <= elapsedTreeMapGet * CONST_FACTOR  ); 
	}


}