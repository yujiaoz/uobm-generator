package gen;

import java.io.File;
import java.util.*;

public class Generator {

	static boolean outputMerged = true;
	String ontology;

	private int univNum;
	private University[] universities;

	public static void main(String[] args) {
		int univNum = 1, startIndex = 0, seed = 0;
		String ontology = "http://semantics.crl.ibm.com/univ-bench-dl.owl";
		String outputPath = System.getProperty("user.dir") + System.getProperty("file.separator");
//		String outputPath = "/users/yzhou/scratch/Ontologies/generated/uobm1/";
//		String outputPath = "/users/yzhou/workspace/OWLim/preload_generated_uobm/";

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
				} else if (arg.equals("-merge")){
					arg = args[i++];
					if (arg.equals("true"))
						outputMerged = true;
					else if (arg.equals("false"))
						outputMerged = false;
					else 
						throw new Exception("-merge true or -merge false");
				} else if (arg.equals("-onto")) {
					if (i < args.length) {
						arg = args[i++];
						ontology = arg;
					} else
						throw new Exception();
				} else if (arg.equals("-path")) {
					if (i < args.length) {
						arg = args[i++];
						outputPath = arg;
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
					+ "\t[-univ <num of universities(1~" + Integer.MAX_VALUE + ")>]\n" 
					+ "\t[-path output path>]\n" 
					+ "\t[-merge true or false>]\n" 
					+ "\t[-index <start index(0~" + Integer.MAX_VALUE + ")>]\n" 
					+ "\t[-seed <seed(0~" + Integer.MAX_VALUE + ")>]\n" + "\t[-daml]\n"
					+ "\t-onto <univ-bench ontology url>");
			System.exit(0);
		}

		Class.setup(univNum);
		
		File file = new File(outputPath);
		if (!file.exists())	file.mkdir();
		
		new Generator().start(univNum, startIndex, seed, ontology, outputPath);
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
	public void start(int univNum, int startIndex, int seed, String ontology, String outputPath) {
		this.ontology = ontology;
		this.univNum = univNum;
		Lib.setSeed(seed);
		generate(outputPath);
	}


	/** Begins data generation according to the specification */
	private void generate(String outputPath) {
		System.out.println("Started...");

		universities = new University[univNum];
		for (int i = 0; i < univNum; ++i)
			universities[i] = new University(this, i, outputPath);

		for (int i = 0; i < univNum; ++i)
			universities[i].generateFaculty();
		
		for (int i = 0; i < univNum; ++i)
			universities[i].generateStudents();
		
		System.out.println("Completed!");
	}

	public LinkedList<String> getOtherPeopleList4SameHomeTown(String ID, int num) {
		LinkedList<String> list = new LinkedList<String>();
		HashSet<String> hash = new HashSet<String>();
		String people;

		hash.add(ID);
		for (int i = 0; i < num; ++i) {
			people = getRandomPeople();
			while (hash.contains(people))
				people = getRandomPeople();
			hash.add(people);
			list.add(people);
		}

		return list;
	}

	public LinkedList<String> getOtherPeopleList4FriendOf(Organization o, String ID, int num, int outsideOrg, int classType) {
		LinkedList<String> list = new LinkedList<String>();
		HashSet<String> hash = new HashSet<String>();
		String people;

		hash.add(ID);
		for (int i = 0; i < num; ++i) {
			people = getRandomPeople(o, outsideOrg, classType);
			while (people == null || hash.contains(people))
				people = getRandomPeople(o, outsideOrg, classType);
			hash.add(people);
			list.add(people);
		}

		return list;
	}
	
	private String getRandomPeople(Organization o, int outsideOrg, int type) {
		if (Lib.getRandomFromRange(0, outsideOrg) == 0) {
			o = getRandomUniv();
		}
		
		if (Lib.getRandomFromRange(0, 4) == 0) {
			return o.getRandomPeople();
		}
		
		switch (type) {
		case Class.INDEX_FACULTY: return o.getRandomFaculty();
		case Class.INDEX_STUDENT: return o.getRandomStudent();
		case Class.INDEX_SUPPORTINGSTAFF: return o.getRandomSupportingStaff();
		case Class.INDEX_OTHERSTAFF: case Class.INDEX_WOMANSTUDENT: return o.getRandomPeople();
		default:
			System.out.println("other getting random people!");
			return null;
		}
	}

	private String getRandomPeople() {
		University univ = getRandomUniv();
		return univ.getRandomPeople(); 
	}
	
	private University getRandomUniv() {
		return universities[Lib.getRandomFromRange(0, univNum - 1)]; 
	}
	
	public String getUnivInst() {
		return Class.getUnivID(Lib.getRandomFromRange(0, Class.UNIV_NUM - 1));
	}

	public LinkedList<String> getInterestList(int num) {
		return Interest.getList(num);
	}

	public LinkedList<String> getLoverList(int num) {
		return Interest.getLoverList(num);
	}
	
	public static final int BASE4Factulty = 4;
	public static final int BASE4Others = 10;

	public void addSameHomeTownAttributes(Writer m_writer, String ID) {
		int num = getDistributedRandomFromRange(10, Property.SAMEHOMETOWN_MIN, Property.SAMEHOMETOWN_MAX);
		LinkedList<String> list = getOtherPeopleList4SameHomeTown(ID, num);
		for (int i = 0; i < num; ++i)
			m_writer.addProperty(Property.INDEX_SAMEHOMETOWN, list.remove(), true);

	}

//	public void addSameHomeTownAttributes(Organization o, Writer m_writer, String ID) {
//		int num = getDistributedRandomFromRange(10, Property.SAMEHOMETOWN_MIN, Property.SAMEHOMETOWN_MAX);
//		LinkedList<String> list = getOtherPeopleList(o, ID, num, Property.R_TOWN_OUTSIDE_DEPT);
//		for (int i = 0; i < num; ++i)
//			m_writer.addProperty(Property.INDEX_SAMEHOMETOWN, list.remove(), true);
//
//	}

	public void addIsFriendOfAttributes(Organization o, Writer m_writer, String ID, int classType) {
		int num = Lib.getRandomFromRange(Property.FRIENDOF_MIN, Property.FRIENDOF_MAX);
		LinkedList<String> list = getOtherPeopleList4FriendOf(o, ID, num, Property.R_FRIEND_OUTSIDE_DEPT, classType);
		for (int i = 0; i < num; ++i)
			m_writer.addProperty(Property.INDEX_FRIEND, list.remove(), true);
	}
	
	public void addLikeAttributes(Writer m_writer) {
		int num = getDistributedRandomFromRange(2, Property.LIKE_MIN, Property.LIKE_MAX);
		LinkedList<String> list = getInterestList(num);
		for (int i = 0, flag; i < num; ++i)
			if ((flag = Lib.getRandomFromRange(0, 3)) == 0)
				m_writer.addProperty(Property.INDEX_LOVE, list.remove(), true);
			else if (flag == 3)
				m_writer.addProperty(Property.INDEX_CRAZY, list.remove(), true);
			else 
				m_writer.addProperty(Property.INDEX_LIKE, list.remove(), true);
	}

	public void addFanAttributes(Writer m_writer) {
		int num = getDistributedRandomFromRange(4, Class.FAN_MIN, Class.FAN_MAX);
		LinkedList<String> list = getLoverList(num);
		for (int i = 0; i < num; ++i)
			m_writer.addTypeProperty(list.remove());
	}
	
	private static final int[][] bench = {{0, 0, 0, 0, 0, 0}, {1, 1, 1, 1, 1, 1}, {1, 2, 4, 8, 16, 32}, 
											{1, 3, 9, 27, 81, 243}, {1, 4, 16, 64, 256, 1024}, {1, 5, 25, 125, 625, 3125}, 
											{0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, 
											{1, 10, 100, 1000, 10000, 100000}};

	private static int getDistributedRandomFromRange(int base, int min, int max) {
		int num = Lib.getRandomFromRange(1, bench[base][max - min]);
		for (int i = 0; i <= max - min; i++)
			if (num <= bench[base][i])
				return max - i;
		System.out.println("error in distributed random");
		return 0;
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
		return RdfWriter.EntityPrefix + AcademicSubject.TOKEN[Lib.getRandomFromRange(0, AcademicSubject.LENGTH - 1)];
	}

	public String getRandomPublicationGenre() {
		return Publication.TOKEN[Lib.getRandomFromRange(0, Publication.LENGTH - 1)];
	}
}

