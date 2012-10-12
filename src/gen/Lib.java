package gen;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class Lib {
	public static Random random_ = new Random();
	
	public static void setSeed(long seed) {
		random_.setSeed(seed);
	}
	
	public static int getRandomFromRange(int min, int max) {
		return min + random_.nextInt(max - min + 1);
	}
	
	public static LinkedList<Integer> getRandomList(int num, int n) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		HashSet<Integer> hash = new HashSet<Integer>();
		int index;
		for (int i = 0; i < num; ++i) {
			index = Lib.getRandomFromRange(0, n - 1);
			while (hash.contains(index))
				index = Lib.getRandomFromRange(0, n - 1);
			hash.add(index);
			list.add(index);
		}
		return list;

	}

}
