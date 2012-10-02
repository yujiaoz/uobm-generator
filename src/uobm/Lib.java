package uobm;

import java.util.Random;

public class Lib {
	public static Random random_;
	
	public static void setSeed(long seed) {
		random_.setSeed(seed);
	}
	
	public static int getRandomFromRange(int min, int max) {
		return min + random_.nextInt(max - min + 1);
	}

}
