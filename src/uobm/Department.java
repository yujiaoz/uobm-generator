package uobm;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class Department implements Organization {

	int m_groupNum;
	int m_professorNum, m_assoProfNum, m_asstProfNum, m_lecturerNum,
			m_vistProfNum;
	int m_underStudNum, m_gradStudNum;
	ArrayList<Integer> m_underCourses, m_gradCourses;
	LinkedList<Integer> m_restUnderCourses, m_restGradCourses;
	String m_ID;

	Writer m_writer;
	int m_chair;
	Generator m_gen;
	int m_deptIndex, m_univIndex;
	String m_filename;

	int total_facultyNum, total_studentNum;

	public Department(Generator gen, int univ, int dept, String prefix) {
		m_groupNum = Lib.getRandomFromRange(Class.RESEARCHGROUP_MIN,
				Class.RESEARCHGROUP_MAX);

		/*
		 * the number of faculty
		 */
		m_professorNum = Lib.getRandomFromRange(Class.FULLPROF_MIN,
				Class.FULLPROF_MAX);
		m_assoProfNum = Lib.getRandomFromRange(Class.ASSOPROF_MIN,
				Class.ASSOPROF_MAX);
		m_asstProfNum = Lib.getRandomFromRange(Class.ASSTPROF_MIN,
				Class.ASSTPROF_MAX);
		m_lecturerNum = Lib.getRandomFromRange(Class.LEC_MIN, Class.LEC_MAX);

		total_facultyNum = m_professorNum + m_assoProfNum + m_asstProfNum
				+ m_lecturerNum;
		m_vistProfNum = Lib.getRandomFromRange(Class.VISTPROF_MIN,
				Class.VISTPROF_MAX);

		/*
		 * the number of students
		 */
		m_underStudNum = Lib.getRandomFromRange(total_facultyNum
				* Class.R_UNDERSTUD_FACULTY_MIN, total_facultyNum
				* Class.R_UNDERSTUD_FACULTY_MAX);
		m_gradStudNum = Lib.getRandomFromRange(total_facultyNum
				* Class.R_GRADSTUD_FACULTY_MIN, total_facultyNum
				* Class.R_GRADSTUD_FACULTY_MAX);
		total_studentNum = m_underStudNum + m_gradStudNum;

		/*
		 * initial the list of courses
		 */
		m_underCourses = new ArrayList<Integer>();
		m_gradCourses = new ArrayList<Integer>();
		m_restUnderCourses = new LinkedList<Integer>();
		m_restGradCourses = new LinkedList<Integer>();
		
		for (int i = 0; i < Class.UNDER_COURSE_NUM; ++i) 
			m_restUnderCourses.add(i);
		for (int i = 0; i < Class.GRAD_COURSE_NUM; ++i)
			m_restGradCourses.add(i);

		m_gen = gen;
		m_writer = new OwlWriter(gen.ontology);
		m_chair = Lib.getRandomFromRange(0, m_professorNum);
		m_univIndex = univ;
		m_deptIndex = dept;
		m_ID = Class.getDeptID(univ, dept);
		m_filename = prefix + Class.INDEX_DELIMITER + "dept" + m_deptIndex
				+ ".owl";

		// TODO: publications
	}

	@Override
	public void generate() {
		generateProfs();
		generateAssoProfs();
		generateAsstProfs();
		generateVistProfs();
		generateLectures();

		generateUnderGradStud();
		generateGradStud();
		
		generateCourses();
		generatePublications();
	}
	
	private void generateFaculty(String ID, String name) {
		m_writer.addProperty(Property.INDEX_EMAIL,
				ID.replaceAll("http://www.", name + "@"), false);
		m_writer.addProperty(Property.INDEX_FIRSTNAME, name + ".first", false);
		m_writer.addProperty(Property.INDEX_FIRSTNAME, name + ".second", false);
		m_writer.addProperty(Property.INDEX_TELE, "xxx-xxx-xxxx", false);

		if (Lib.getRandomFromRange(0, 1) == 0)
			m_writer.addProperty(RdfWriter.T_RDF_TYPE, "Man", true);
		else
			m_writer.addProperty(RdfWriter.T_RDF_TYPE, "Woman", true);

		m_writer.addProperty(Property.INDEX_MEMBER, m_ID, true);

		m_writer.addProperty(Property.INDEX_UNDERDEGREE, m_gen.getUnivInst(),
				true);
		m_writer.addProperty(Property.INDEX_MASTERDEGREE, m_gen.getUnivInst(),
				true);
		m_writer.addProperty(Property.INDEX_DOCDEGREE, m_gen.getUnivInst(),
				true);

		m_gen.addSameHomeTownAttributes(m_univIndex, m_deptIndex, m_writer, ID);
		m_gen.addIsFriendOfAttributes(m_univIndex, m_deptIndex, m_writer, ID);
		m_gen.addLikeAttributes(m_writer);
		m_gen.addFanAttributes(m_writer);
		
		for (int i = 0; i < Lib.getRandomFromRange(Property.FACULTY_COURSE_MIN, Property.FACULTY_COURSE_MAX); ++i) {
			m_writer.addProperty(Property.INDEX_TEACHES, Class.getOtherID(m_ID, Class.INDEX_COURSE, assignCourse()), true);
		}
		
		for (int i = 0; i < Lib.getRandomFromRange(Property.FACULTY_GRADCOURSE_MIN, Property.FACULTY_GRADCOURSE_MAX); ++i) {
			m_writer.addProperty(Property.INDEX_TEACHES, Class.getOtherID(m_ID, Class.INDEX_GRADCOURSE, assignGradCourse()), true);
		}
		
	}
	
	int assignCourse() {
		int course = m_restUnderCourses.remove();
		m_underCourses.add(course);
		return course;
	}
	
	int assignGradCourse() {
		int course = m_restGradCourses.remove();
		m_gradCourses.add(course);
		return course;
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
				m_writer.addProperty(Property.INDEX_HEAD, Class.getUnivID(m_univIndex), true);
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
		// TODO Auto-generated method stub

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
		// TODO
	}

	private void generateGradStud() {
		// TODO
	}

	@Override
	public String getRandomPeople() {
		int index = Lib.getRandomFromRange(1, total_facultyNum + total_studentNum);
		if (index > total_facultyNum) {
			index -= total_facultyNum;
			
			if (index > m_underStudNum) index -= m_underStudNum;
			else return Class.getOtherID(m_ID, Class.INDEX_UNDERSTUD, index - 1);
			
			return Class.getOtherID(m_ID, Class.INDEX_GRADSTUD, index - 1);
		}
		else {
			if (index > m_professorNum) index -= m_professorNum;
			else return Class.getOtherID(m_ID, Class.INDEX_FULLPROF, index - 1);
			
			if (index > m_assoProfNum) index -= m_assoProfNum;
			else return Class.getOtherID(m_ID, Class.INDEX_ASSOPROF, index - 1);
			
			if (index > m_asstProfNum) index -= m_asstProfNum;
			else return Class.getOtherID(m_ID, Class.INDEX_ASSTPROF, index - 1);
			
			if (index > m_vistProfNum) index -= m_vistProfNum;
			else return Class.getOtherID(m_ID, Class.INDEX_VISTPROF, index - 1);
			
			return Class.getOtherID(m_ID, Class.INDEX_LECTURE, index - 1);
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

}
