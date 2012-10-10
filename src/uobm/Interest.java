package uobm;

import java.util.HashSet;
import java.util.LinkedList;

public class Interest {

	public static final String[] TOKEN = { "Classic_Music", "Pop_Music",
			"Rock_and_Roll", "AmericanFootball", "Kickboxing", "Soccer",
			"Baseball", "BasketBall", "Swimming", "Tennis" };

	public static final int LENGTH = TOKEN.length;

	public static final String[] LOVER_TOKEN = { "SportsLover",
			"BasketBallLover", "SwimmingLover", "BaseballLover", "SportsFan",
			"BasketBallFan", "TennisFan", "SwimmingFan", "BaseballFan" };

	public static final int LOVER_LENGTH = LOVER_TOKEN.length;

	public static LinkedList<String> getList(int num) {
		LinkedList<String> list = new LinkedList<String>();
		HashSet<Integer> hash = new HashSet<Integer>();
		int index;
		for (int i = 0; i < num; ++i) {
			index = Lib.getRandomFromRange(0, LENGTH);
			while (hash.contains(index))
				index = Lib.getRandomFromRange(0, LENGTH);
			hash.add(index);
			list.add(TOKEN[index]);
		}
		return list;
	}

	public static LinkedList<String> getLoverList(int num) {
		LinkedList<String> list = new LinkedList<String>();
		HashSet<Integer> hash = new HashSet<Integer>();
		int index;
		for (int i = 0; i < num; ++i) {
			index = Lib.getRandomFromRange(0, LENGTH);
			while (hash.contains(index))
				index = Lib.getRandomFromRange(0, LENGTH);
			hash.add(index);
			list.add(LOVER_TOKEN[index]);
		}
		return list;
	}

}
