package gen;

import java.util.HashSet;
import java.util.LinkedList;

public class Department implements Organization {

	int m_groupNum;
	int m_professorNum, m_assoProfNum, m_asstProfNum, m_lecturerNum, m_vistProfNum;
	int m_underStudNum, m_gradStudNum;
	int m_systemsStaffNum, m_clericalStaffNum, m_otherStaffNum;;
	
	int m_underCourseNum, m_gradCourseNum;
	int m_publicationNum;
	int m_TANum, m_RANum;
	
	int m_sameIndividualNum = 0;
	
	String m_ID;

	Writer m_writer;
	int m_chair;
	Generator m_gen;
	int m_deptIndex, m_univIndex, m_collegeIndex;
	String m_filename;

	int total_facultyNum, total_studentNum;

	public Department(University univ, int dept, String prefix) {
		m_groupNum = Lib.getRandomFromRange(Class.RESEARCHGROUP_MIN, Class.RESEARCHGROUP_MAX);
		m_collegeIndex = univ.getRandomCollege();

		/*
		 * the number of faculty
		 */
		m_professorNum = Lib.getRandomFromRange(Class.FULLPROF_MIN,	Class.FULLPROF_MAX);
		m_assoProfNum = Lib.getRandomFromRange(Class.ASSOPROF_MIN, Class.ASSOPROF_MAX);
		m_asstProfNum = Lib.getRandomFromRange(Class.ASSTPROF_MIN, Class.ASSTPROF_MAX);
		m_lecturerNum = Lib.getRandomFromRange(Class.LEC_MIN, Class.LEC_MAX);

		total_facultyNum = m_professorNum + m_assoProfNum + m_asstProfNum + m_lecturerNum;
		m_vistProfNum = Lib.getRandomFromRange(Class.VISTPROF_MIN, Class.VISTPROF_MAX);

		/*
		 * the number of students
		 */
		m_underStudNum = Lib.getRandomFromRange(total_facultyNum * Class.R_UNDERSTUD_FACULTY_MIN, total_facultyNum * Class.R_UNDERSTUD_FACULTY_MAX);
		m_gradStudNum = Lib.getRandomFromRange(total_facultyNum	* Class.R_GRADSTUD_FACULTY_MIN, total_facultyNum * Class.R_GRADSTUD_FACULTY_MAX);
		total_studentNum = m_underStudNum + m_gradStudNum;
		
		m_TANum = Lib.getRandomFromRange(m_gradCourseNum / Class.R_GRADSTUD_TA_MAX, m_gradStudNum / Class.R_GRADSTUD_TA_MIN);
		m_RANum = Lib.getRandomFromRange(m_gradCourseNum / Class.R_GRADSTUD_RA_MAX, m_gradStudNum / Class.R_GRADSTUD_RA_MIN);

		m_systemsStaffNum = Lib.getRandomFromRange(Class.SYSTEMS_STAFF_MIN, Class.SYSTEMS_STAFF_MAX);
		m_clericalStaffNum = Lib.getRandomFromRange(Class.CLERICAL_STAFF_MIN, Class.CLERICAL_STAFF_MAX);
		m_otherStaffNum = Lib.getRandomFromRange(Class.OTHER_STAFF_MIN, Class.OTHER_STAFF_MAX);
		
		/*
		 * initial the list of courses
		 */
		m_underCourseNum = 0;
		m_gradCourseNum = 0;
		m_publicationNum = 0;
		
		m_gen = univ.m_gen;
		m_writer = new OwlWriter(m_gen.ontology);
		m_chair = Lib.getRandomFromRange(0, m_professorNum - 1);
		m_univIndex = univ.m_index;
		m_deptIndex = dept;
		m_ID = Class.getDeptID(univ.m_index, dept);
		m_filename = prefix + Class.INDEX_DELIMITER + "dept" + m_deptIndex + ".owl";
	}

	@Override
//	public void generateFaculty() {
//		generateFaculty();
//		generateStudents();
//	}
	
	public void generateFaculty() {
		m_writer.start();
		m_writer.startFile(m_filename);
		
		m_writer.startSection(Class.INDEX_DEPT, m_ID);
		m_writer.addProperty(Property.INDEX_NAME, Class.getName(Class.INDEX_DEPT, m_deptIndex), true);
		m_writer.addProperty(Property.INDEX_SUBORG, Class.getCollegeID(m_univIndex, m_collegeIndex), true);
		m_writer.endSection(Class.INDEX_DEPT);
		
		generateProfs();
		generateAssoProfs();
		generateAsstProfs();
		generateVistProfs();
		generateLectures();
	}
	
	public void generateStudents() {
		generateUnderGradStud();
		generateGradStud();
		
		generateCourses();
		generatePublications();
		generateTARAs();
		generateResearchGroups();
		generateStaffs();
		m_writer.endFile();
		m_writer.end();
	}
	
	private void generateStaffs() {
		String ID;
		for (int i = 0; i < m_clericalStaffNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_CLERICALSTAFF, i);
			m_writer.startSection(Class.INDEX_CLERICALSTAFF, ID);
			if (Lib.getRandomFromRange(0, 2) != 0) m_writer.addProperty(Property.INDEX_WORKS, m_ID, true);
			else m_writer.addProperty(Property.INDEX_MEMBER, m_ID, true);
			generatePerson(ID, Class.getName(Class.INDEX_CLERICALSTAFF, i));
			m_writer.endSection(Class.INDEX_CLERICALSTAFF);
		}
			
		for (int i = 0; i < m_systemsStaffNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_SYSTEMSSTAFF, i);
			m_writer.startSection(Class.INDEX_SYSTEMSSTAFF, ID);
			m_writer.addProperty(Property.INDEX_WORKS, m_ID, true);
			generatePerson(ID, Class.getName(Class.INDEX_SYSTEMSSTAFF, i));
			m_writer.endSection(Class.INDEX_SYSTEMSSTAFF);
		}
		
		for (int i = 0; i < m_otherStaffNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_OTHERSTAFF, i);
			m_writer.startSection(Class.INDEX_PERSON, ID);
			m_writer.addProperty(Property.INDEX_WORKS, m_ID, true);
			generatePerson(ID, Class.getName(Class.INDEX_OTHERSTAFF, i));
			m_writer.endSection(Class.INDEX_PERSON);
		}
	}

	private void generateResearchGroups() {
		String groupID;
		for (int i = 0; i < m_groupNum; ++i) {
			groupID = Class.getOtherID(m_ID, Class.INDEX_RESEARCHGROUP, i);
			m_writer.startSection(Class.INDEX_RESEARCHGROUP, groupID);
			m_writer.addProperty(Property.INDEX_SUBORG, m_ID, true);
			m_writer.endSection(Class.INDEX_RESEARCHGROUP);
		}
	}

	private void generateTARAs() {
		LinkedList<Integer> list = Lib.getRandomList(m_TANum, m_gradStudNum);
		LinkedList<Integer> courseList = Lib.getRandomList(m_TANum, m_underCourseNum);
		for (int i: list) {
			m_writer.startSection(Class.INDEX_TA, Class.getOtherID(m_ID, Class.INDEX_GRADSTUD, i));
			m_writer.addProperty(Property.INDEX_TA, Class.getOtherID(m_ID, Class.INDEX_COURSE, courseList.remove()), true);
			m_writer.endSection(Class.INDEX_TA);
		}
		
		list = Lib.getRandomList(m_RANum, m_gradStudNum);
		for (int i: list) {
			m_writer.startSection(Class.INDEX_RA, Class.getOtherID(m_ID, Class.INDEX_GRADSTUD, i));
			m_writer.endSection(Class.INDEX_RA);
		}
	}

	private void generatePublications() {
		String ID;
		int publication;
		for (int i = 0; i < m_professorNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_FULLPROF, i);
			for (int j = 0; j < Lib.getRandomFromRange(Property.FULLPROF_PUB_MIN, Property.FULLPROF_PUB_MAX); ++j) {
				publication = m_publicationNum++;
				assignAuthors(ID, publication);
			}
				
		}
			
		for (int i = 0; i < m_assoProfNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_ASSOPROF, i);
			for (int j = 0; j < Lib.getRandomFromRange(Property.ASSOPROF_PUB_MIN, Property.ASSOPROF_PUB_MAX); ++j) {
				publication = m_publicationNum++;
				assignAuthors(ID, publication);
			}
				
		}
			
		for (int i = 0; i < m_asstProfNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_ASSTPROF, i);
			for (int j = 0; j < Lib.getRandomFromRange(Property.ASSTPROF_PUB_MIN, Property.ASSTPROF_PUB_MAX); ++j) {
				publication = m_publicationNum++;
				assignAuthors(ID, publication);
			}
				
		}
		
		for (int i = 0; i < m_lecturerNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_LECTURE, i);
			for (int j = 0; j < Lib.getRandomFromRange(Property.LEC_PUB_MIN, Property.LEC_PUB_MAX); ++j) {
				publication = m_publicationNum++;
				assignAuthors(ID, publication);
			}
				
		}

		for (int i = 0; i < m_gradStudNum; ++i) {
			ID = Class.getOtherID(m_ID, Class.INDEX_GRADSTUD, i);
			for (int j = 0; j < Lib.getRandomFromRange(Property.GRADSTUD_PUB_MIN, Property.GRADSTUD_PUB_MAX); ++j) {
				publication = m_publicationNum++;
				assignAuthors(ID, publication);
			}
				
		}

	}
	
	private void assignAuthors(String ID, int publication) {
		String pubID = Class.getOtherID(m_ID, Class.INDEX_PUBLICATION, publication);
		HashSet<String> hash = new HashSet<String>();
		int num = Lib.getRandomFromRange(Property.PUB_AUTHOR_MIN, Property.PUB_AUTHOR_MAX);
		hash.add(ID);
		
		String genre;
		m_writer.startSection(genre = m_gen.getRandomPublicationGenre(), pubID);
		m_writer.addProperty(Property.INDEX_AUTHOR, ID, true);
		String people;
		for (int i = 0; i < num; ++i) {
			people = getRandomPerson(1, total_facultyNum + m_gradStudNum);
			while (hash.contains(people))
				people = getRandomPerson(1, total_facultyNum + m_gradStudNum);
			hash.add(people);
			m_writer.addProperty(Property.INDEX_AUTHOR, people, true);
		}
		m_writer.endSection(genre);
	}
	
	String assignSameIndividual() {
		String ID = null;
		if (Lib.getRandomFromRange(0, Property.R_SAME_AUTHOR) == 0)
			m_writer.addProperty(Property.INDEX_TAUGHTBY, ID = Class.getOtherID(m_ID, Class.INDEX_SAMEINDIVIDUAL, m_sameIndividualNum++), true);
		return ID;
	}

	private void generateCourses() {
		String sameIndividual;
		for (int i = 0; i < m_underCourseNum; ++i) {
			m_writer.startSection(Class.INDEX_COURSE, Class.getOtherID(m_ID, Class.INDEX_COURSE, i));
			m_writer.addProperty(Property.INDEX_NAME, Class.getName(Class.INDEX_COURSE, i), true);
			sameIndividual = assignSameIndividual();
			m_writer.endSection(Class.INDEX_COURSE);
			if (sameIndividual != null) {
				m_writer.startSection(Class.INDEX_PERSON, sameIndividual);
				m_gen.addIsFriendOfAttributes(this, m_writer, sameIndividual);
				m_writer.endSection(Class.INDEX_PERSON);
			}
		}
		
		for (int i = 0; i < m_gradCourseNum; ++i) {
			m_writer.startSection(Class.INDEX_GRADCOURSE, Class.getOtherID(m_ID, Class.INDEX_GRADCOURSE, i));
			m_writer.addProperty(Property.INDEX_NAME, Class.getName(Class.INDEX_GRADCOURSE, i), true);
			sameIndividual = assignSameIndividual();
			m_writer.endSection(Class.INDEX_GRADCOURSE);
			if (sameIndividual != null) {
				m_writer.startSection(Class.INDEX_PERSON, sameIndividual);
				m_gen.addIsFriendOfAttributes(this, m_writer, sameIndividual);
				m_writer.endSection(Class.INDEX_PERSON);
			}
		}
	}

	private void generatePerson(String ID, String name) {
		m_writer.addProperty(Property.INDEX_EMAIL, ID.replaceAll("http://www.", name + "@"), false);
		m_writer.addProperty(Property.INDEX_FIRSTNAME, name + ".first", false);
		m_writer.addProperty(Property.INDEX_FIRSTNAME, name + ".second", false);
		m_writer.addProperty(Property.INDEX_TELE, "xxx-xxx-xxxx", false);

		if (Lib.getRandomFromRange(0, 1) == 0)
			m_writer.addTypeProperty(RdfWriter.EntityPrefix + "Man");
		else
			m_writer.addTypeProperty(RdfWriter.EntityPrefix + "Woman");

		m_gen.addSameHomeTownAttributes(this, m_writer, ID);
		m_gen.addIsFriendOfAttributes(this, m_writer, ID);
		m_gen.addLikeAttributes(m_writer);
		m_gen.addFanAttributes(m_writer);
	}
	
	private void generateFaculty(String ID, String name) {
		generatePerson(ID, name);
		
		if (Lib.getRandomFromRange(0, 2) == 0) m_writer.addProperty(Property.INDEX_WORKS, m_ID, true);
		else m_writer.addProperty(Property.INDEX_MEMBER, m_ID, true);
		m_writer.addProperty(Property.INDEX_UNDERDEGREE, m_gen.getUnivInst(), true);
		m_writer.addProperty(Property.INDEX_MASTERDEGREE, m_gen.getUnivInst(), true);
		m_writer.addProperty(Property.INDEX_DOCDEGREE, m_gen.getUnivInst(), true);

		for (int i = 0; i < Lib.getRandomFromRange(Property.FACULTY_COURSE_MIN, Property.FACULTY_COURSE_MAX); ++i) {
			m_writer.addProperty(Property.INDEX_TEACHES, assignCourse(), true);
		}
		
		for (int i = 0; i < Lib.getRandomFromRange(Property.FACULTY_GRADCOURSE_MIN, Property.FACULTY_GRADCOURSE_MAX); ++i) {
			m_writer.addProperty(Property.INDEX_TEACHES, assignGradCourse(), true);
		}
	}
	
	private String assignCourse() {
		int course = m_underCourseNum++;
		return Class.getOtherID(m_ID, Class.INDEX_COURSE, course);
	}
	
	private String assignGradCourse() {
		int course = m_gradCourseNum++;
		return Class.getOtherID(m_ID, Class.INDEX_GRADCOURSE, course);
	}
	
	void assignResearch() {
		m_writer.addProperty(Property.INDEX_INTEREST, Class.getOtherID(m_ID, Class.INDEX_RESEARCH, Class.RESEARCH_NUM - 1), true);
	}
	
	private void generateProfs() {
		String profID;
		for (int i = 0; i < m_professorNum; ++i) {
			profID = Class.getOtherID(m_ID, Class.INDEX_FULLPROF, i);
			m_writer.startSection(Class.INDEX_FULLPROF, profID);
			if (i == m_chair)
				m_writer.addProperty(Property.INDEX_HEAD, m_ID, true);
			generateFaculty(profID, Class.getName(Class.INDEX_FULLPROF, i));
			
			assignResearch();
			m_writer.endSection(Class.INDEX_FULLPROF);
		}

	}
	
	private void generateAssoProfs() {
		String profID;
		for (int i = 0; i < m_assoProfNum; ++i) {
			profID = Class.getOtherID(m_ID, Class.INDEX_ASSOPROF, i);
			m_writer.startSection(Class.INDEX_ASSOPROF, profID);
			generateFaculty(profID, Class.getName(Class.INDEX_ASSOPROF, i));
			assignResearch();
			m_writer.endSection(Class.INDEX_ASSOPROF);
		}
	}

	private void generateAsstProfs() {
		String profID;
		for (int i = 0; i < m_asstProfNum; ++i) {
			profID = Class.getOtherID(m_ID, Class.INDEX_ASSTPROF, i);
			m_writer.startSection(Class.INDEX_ASSTPROF, profID);
			generateFaculty(profID, Class.getName(Class.INDEX_ASSTPROF, i));
			assignResearch();
			m_writer.endSection(Class.INDEX_ASSTPROF);
		}
	}

	private void generateVistProfs() {
		// TODO generateVistProfs Auto-generated method stub

	}

	private void generateLectures() {
		String lectureID;
		for (int i = 0; i < m_lecturerNum; ++i) {
			lectureID = Class.getOtherID(m_ID, Class.INDEX_LECTURE, i);
			m_writer.startSection(Class.INDEX_LECTURE, lectureID);
			generateFaculty(lectureID, Class.getName(Class.INDEX_LECTURE, i));
			m_writer.endSection(Class.INDEX_LECTURE);
		}
	}

	private void generateUnderGradStud() {
		int num;
		LinkedList<String> list;
		String ID;
		for (int i = 0; i < m_underStudNum; ++i) {
			m_writer.startSection(Class.INDEX_UNDERSTUD, ID = Class.getOtherID(m_ID, Class.INDEX_UNDERSTUD, i));
			generatePerson(ID, Class.getName(Class.INDEX_UNDERSTUD, i));
			m_writer.addProperty(Property.INDEX_ENROLL, m_ID, true);
			m_writer.addProperty(Property.INDEX_MAJOR, m_gen.getRandomMajor(), true);
			num = Lib.getRandomFromRange(Property.UNDERSTUD_COURSE_MIN, Property.UNDERSTUD_COURSE_MAX);
			list = m_gen.getCourseList(this, num);
			while (!list.isEmpty())
				m_writer.addProperty(Property.INDEX_TAKES, list.remove(), true);
			if (Lib.getRandomFromRange(0, Class.R_UNDERSTUD_ADVISOR) == 0)
				m_writer.addProperty(Property.INDEX_ADVISED, getRandomPerson(1, total_facultyNum), true);
			m_writer.endSection(Class.INDEX_UNDERSTUD);
		}
	}
	
	private void generateGradStud() {
		int num;
		LinkedList<String> list;
		String ID;
		for (int i = 0; i < m_gradStudNum; ++i) {
			m_writer.startSection(Class.INDEX_GRADSTUD, ID = Class.getOtherID(m_ID, Class.INDEX_GRADSTUD, i));
			generatePerson(ID, Class.getName(Class.INDEX_GRADSTUD, i));
			m_writer.addProperty(Property.INDEX_ENROLL, m_ID, true);
			m_writer.addProperty(Property.INDEX_MAJOR, m_gen.getRandomMajor(), true);
			m_writer.addProperty(Property.INDEX_UNDERDEGREE, m_gen.getUnivInst(), true);
			num = Lib.getRandomFromRange(Property.GRADSTUD_COURSE_MIN, Property.GRADSTUD_COURSE_MAX);
			list = m_gen.getGradCourseList(this, num);
			while (!list.isEmpty())
				m_writer.addProperty(Property.INDEX_TAKES, list.remove(), true);
			if (Lib.getRandomFromRange(0, Class.R_GRADSTUD_ADVISOR) == 0)
				m_writer.addProperty(Property.INDEX_ADVISED, getRandomPerson(1, total_facultyNum), true);
			m_writer.endSection(Class.INDEX_GRADSTUD);
		}
	}
	
	@Override
	public String getRandomPeople() {
		return getRandomPerson(1, total_facultyNum + total_studentNum);
	}
	
	public String getRandomPerson(int min, int max) {
		int index = Lib.getRandomFromRange(min, max);
		
		if (index <= total_facultyNum) {
			if (index <= m_professorNum + m_assoProfNum) {
				if (index <= m_professorNum)
					return Class.getOtherID(m_ID, Class.INDEX_FULLPROF, index - 1);
				index -= m_professorNum;
				return Class.getOtherID(m_ID, Class.INDEX_ASSOPROF, index - 1);
			}
			
			index -= m_professorNum + m_assoProfNum;
			if (index <= m_asstProfNum)
				return Class.getOtherID(m_ID, Class.INDEX_ASSTPROF, index - 1);
			index -= m_asstProfNum;
			return Class.getOtherID(m_ID, Class.INDEX_LECTURE, index - 1);
		}
		else {
			index -= total_facultyNum;
			if (index <= total_studentNum) { 
				if (index <= m_gradStudNum)
					return Class.getOtherID(m_ID, Class.INDEX_GRADSTUD, index - 1);
				index -= m_gradStudNum;
				return Class.getOtherID(m_ID, Class.INDEX_UNDERSTUD, index - 1);
			}
				
			index -= total_studentNum;
			if (index <= m_systemsStaffNum)
				return Class.getOtherID(m_ID, Class.INDEX_SYSTEMSSTAFF, index - 1);
			index -= m_systemsStaffNum;
			
			if (index <= m_clericalStaffNum) 
				return Class.getOtherID(m_ID, Class.INDEX_CLERICALSTAFF, index - 1);
			index -= m_clericalStaffNum;
			
			if (index > m_otherStaffNum) {
				System.err.println("error");
				throw new Error();
			}
			return Class.getOtherID(m_ID, Class.INDEX_OTHERSTAFF, index - 1);
		}
	}

	@Override
	public Organization getRandomSubOrgan() {
		return null;
	}

	@Override
	public int getUniversityIndex() {
		return m_univIndex;
	}

	@Override
	public int getIndex() {
		return m_deptIndex;
	}

	@Override
	public String getRandomCourse() {
		if (m_underCourseNum < 1)
			System.out.println("here");
		return Class.getOtherID(m_ID, Class.INDEX_COURSE, Lib.getRandomFromRange(0, m_underCourseNum - 1));
	}

	@Override
	public String getRandomGradCourse() {
		return Class.getOtherID(m_ID, Class.INDEX_GRADCOURSE, Lib.getRandomFromRange(0, m_gradCourseNum - 1));
	}

	@Override
	public String getRandomFaculty() {
		return getRandomPerson(1, total_facultyNum);
	}

}
