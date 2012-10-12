package gen;

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
		for (int i: Lib.getRandomList(num, LENGTH))
			list.add(TOKEN[i]);
		return list;
	}

	public static LinkedList<String> getLoverList(int num) {
		LinkedList<String> list = new LinkedList<String>();
		for (int i: Lib.getRandomList(num, LOVER_LENGTH))
			list.add(LOVER_TOKEN[i]);
		return list;
	}

}
