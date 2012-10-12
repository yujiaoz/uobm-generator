package gen;

import java.util.*;

public class Generator {

	String ontology;

	private int univNum;
	private University[] universities;

	public static void main(String[] args) {
		int univNum = 1, startIndex = 0, seed = 0;
		String ontology = "http://semantics.crl.ibm.com/univ-bench-dl.owl";

		try {
			String arg;
			int i = 0;
			while (i < args.length) {
				arg = args[i++];
				if (arg.equals("-univ")) {
					if (i < args.length) {
						arg = args[i++];
						univNum = Integer.parseInt(arg);
						if (univNum < 1)
							throw new NumberFormatException();
					} else
						throw new NumberFormatException();
				} else if (arg.equals("-index")) {
					if (i < args.length) {
						arg = args[i++];
						startIndex = Integer.parseInt(arg);
						if (startIndex < 0)
							throw new NumberFormatException();
					} else
						throw new NumberFormatException();
				} else if (arg.equals("-seed")) {
					if (i < args.length) {
						arg = args[i++];
						seed = Integer.parseInt(arg);
						if (seed < 0)
							throw new NumberFormatException();
					} else
						throw new NumberFormatException();
				} else if (arg.equals("-onto")) {
					if (i < args.length) {
						arg = args[i++];
						ontology = arg;
					} else
						throw new Exception();
				} else
					throw new Exception();
			}
			if (((long) startIndex + univNum - 1) > Integer.MAX_VALUE) {
				System.err.println("Index overflow!");
				throw new Exception();
			}
			if (null == ontology) {
				System.err.println("ontology url is requested!");
				throw new Exception();
			}
		} catch (Exception e) {
			System.err.println("Usage: Generator\n"
					+ "\t[-univ <num of universities(1~" + Integer.MAX_VALUE
					+ ")>]\n" + "\t[-index <start index(0~" + Integer.MAX_VALUE
					+ ")>]\n" + "\t[-seed <seed(0~" + Integer.MAX_VALUE
					+ ")>]\n" + "\t[-daml]\n"
					+ "\t-onto <univ-bench ontology url>");
			System.exit(0);
		}

		new Generator().start(univNum, startIndex, seed, ontology);
	}

	/**
	 * Begins the data generation.
	 * 
	 * @param univNum
	 *            Number of universities to generate.
	 * @param startIndex
	 *            Starting index of the universities.
	 * @param seed
	 *            Seed for data generation.
	 * @param daml
	 *            Generates DAML+OIL data if true, OWL data otherwise.
	 * @param ontology
	 *            Ontology url.
	 */
	public void start(int univNum, int startIndex, int seed, String ontology) {
		this.ontology = ontology;
		this.univNum = univNum;
		Lib.setSeed(seed);
		generate();
	}


	/** Begins data generation according to the specification */
	private void generate() {
		System.out.println("Started...");

		universities = new University[univNum];
		for (int i = 0; i < univNum; ++i)
			universities[i] = new University(this, i);

		for (int i = 0; i < univNum; ++i)
			universities[i].generate();
		System.out.println("Completed!");
	}

	public LinkedList<String> getOtherPeopleList(Organization o, String ID, int num, int ratio) {
		LinkedList<String> list = new LinkedList<String>();
		HashSet<String> hash = new HashSet<String>();
		String people;

		hash.add(ID);
		for (int i = 0; i < num; ++i) {
			people = getRandomPeople(o, ratio);
			while (hash.contains(people))
				people = getRandomPeople(o, ratio);
			hash.add(people);
			list.add(people);
		}

		return list;
	}

	private String getRandomPeople(Organization o, int ratio) {
		if (Lib.getRandomFromRange(0, ratio) == 0) 
			return getRandomPeople();
		else return o.getRandomPeople(); 
	}

	private String getRandomPeople() {
		University univ = getRandomUniv();
		return univ.getRandomPeople(); 
	}
	
	private University getRandomUniv() {
		return universities[Lib.getRandomFromRange(0, univNum - 1)]; 
	}
	
	public String getUnivInst() {
		return Class.getName(Class.INDEX_UNIV, Lib.getRandomFromRange(0, Class.UNIV_NUM - 1));
	}

	public LinkedList<String> getInterestList(int num) {
		return Interest.getList(num);
	}

	public LinkedList<String> getLoverList(int num) {
		return Interest.getLoverList(num);
	}

	public void addSameHomeTownAttributes(Organization o, Writer m_writer, String ID) {
		int num = Lib.getRandomFromRange(Property.SAMEHOMETOWN_MIN, Property.SAMEHOMETOWN_MAX);
		LinkedList<String> list = getOtherPeopleList(o, ID, num, Property.R_TOWN_OUTSIDE_DEPT);
		for (int i = 0; i < num; ++i)
			m_writer.addProperty(Property.INDEX_SAMEHOMETOWN, list.remove(), true);

	}

	public void addIsFriendOfAttributes(Organization o, Writer m_writer, String ID) {
		int num = Lib.getRandomFromRange(Property.FRIENDOF_MIN, Property.FRIENDOF_MAX);
		LinkedList<String> list = getOtherPeopleList(o, ID, num, Property.R_FRIEND_OUTSIDE_DEPT);
		for (int i = 0; i < num; ++i)
			m_writer.addProperty(Property.INDEX_FRIEND, list.remove(), true);
	}
	
	public void addLikeAttributes(Writer m_writer) {
		int num = Lib.getRandomFromRange(Property.LIKE_MIN, Property.LIKE_MAX);
		LinkedList<String> list = getInterestList(num);
		for (int i = 0; i < num; ++i)
			if (Lib.getRandomFromRange(0, 1) == 0)
				m_writer.addProperty(Property.INDEX_LIKE, list.remove(), true);
			else
				m_writer.addProperty(Property.INDEX_LOVE, list.remove(), true);
	}

	public void addFanAttributes(Writer m_writer) {
		int num = Lib.getRandomFromRange(Class.FAN_MIN, Class.FAN_MAX);
		LinkedList<String> list = getLoverList(num);
		for (int i = 0; i < num; ++i)
			m_writer.addTypeProperty(list.remove());
	}

	public LinkedList<String> getCourseList(Organization o, int num) {
		LinkedList<String> list = new LinkedList<String>();
		HashSet<String> hash = new HashSet<String>();
		String c;
		
		for (int i = 0; i < num; ++i) {
			if (Lib.getRandomFromRange(0, Property.R_TAKES_GRAD_UNDER) == 0) {
				c = getGradCourse(o);
				while (hash.contains(c))
					c = getGradCourse(o);
			}
			else {
				c = getCourse(o);
				while (hash.contains(c))
					c = getCourse(o);
			}
			hash.add(c);
			list.add(c);
		}
		
		return list;
	}

	public LinkedList<String> getGradCourseList(Organization o, int num) {
		LinkedList<String> list = new LinkedList<String>();
		HashSet<String> hash = new HashSet<String>();
		String c;
		for (int i = 0; i < num; ++i) {
			c = getGradCourse(o);
			while (hash.contains(c))
				c = getGradCourse(o);
			hash.add(c);
			list.add(c);
		}
		return list;
	}
	
	private String getCourse(Organization o) {
		if (Lib.getRandomFromRange(0, Property.R_TAKES_COURSE_OTHER) == 0) 
			return getRandomCourse();
		else return o.getRandomCourse();
	}
	
	private String getGradCourse(Organization o) {
		if (Lib.getRandomFromRange(0, Property.R_TAKES_COURSE_OTHER) == 0) 
			return getRandomGradCourse();
		else return o.getRandomGradCourse();
	}

	private String getRandomCourse() {
		Organization univ = getRandomUniv();
		return univ.getRandomCourse();
	}
	
	private String getRandomGradCourse() {
		Organization univ = getRandomUniv();
		return univ.getRandomGradCourse();
	}

	public String getRandomMajor() {
		return AcademicSubject.TOKEN[Lib.getRandomFromRange(0, AcademicSubject.LENGTH - 1)];
	}
}

