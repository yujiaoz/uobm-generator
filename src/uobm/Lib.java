package uobm;

import java.util.Random;

public class Lib {
	public static final Random random_ = new Random(20121001); 
	
	public static int getRandomFromRange(int min, int max) {
		return min + random_.nextInt(max - min + 1);
	}

}
