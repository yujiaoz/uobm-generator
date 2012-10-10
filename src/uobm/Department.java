package uobm;

import java.util.LinkedList;

public class Department {

	int m_groupNum;
	int m_professorNum, m_assoProfNum, m_asstProfNum, m_lecturerNum,
			m_vistProfNum;
	int m_underStudNum, m_gradStudNum;
	LinkedList<Integer> m_underCourses, m_gradCourses, m_restUnderCourses,
			m_restGradCourses;

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
		m_underCourses = new LinkedList<Integer>();
		m_gradCourses = new LinkedList<Integer>();
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
		m_filename = prefix + Class.INDEX_DELIMITER + "dept" + m_deptIndex
				+ ".owl";

		// TODO: publications
	}

	public void generatePeople() {
		generateProfs();
		generateAssoProfs();
		generateAsstProfs();
		generateVistProfs();
		generateLectures();

		generateUnderGradStud();
		generateGradStud();
	}

	private void generateProfs() {
		String profID;
		for (int i = 0; i < m_professorNum; ++i) {
			profID = Class.getOtherID(
					Class.getDeptID(m_univIndex, m_deptIndex),
					Class.INDEX_FULLPROF, i);
			m_writer.startSection(Class.INDEX_FULLPROF, profID);
			if (i == m_chair)
				m_writer.addProperty(Property.INDEX_HEAD,
						Class.getUnivID(m_univIndex), true);
			generateFaculty(profID, Class.getName(Class.INDEX_FULLPROF, i));
			m_writer.endSection(Class.INDEX_FULLPROF);
		}

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

		m_writer.addProperty(Property.INDEX_MEMBER,
				Class.getDeptID(m_univIndex, m_deptIndex), true);

		m_writer.addProperty(Property.INDEX_UNDERDEGREE, m_gen.getUnivInst(),
				true);
		m_writer.addProperty(Property.INDEX_MASTERDEGREE, m_gen.getUnivInst(),
				true);
		m_writer.addProperty(Property.INDEX_DOCDEGREE, m_gen.getUnivInst(),
				true);

		m_gen.addSameHomeTownAttributes(m_writer, ID);
		m_gen.addIsFriendOfAttributes(m_writer, ID);
		m_gen.addLikeAttributes(m_writer);
		m_gen.addFanAttributes(m_writer);

	}

	private void generateAssoProfs() {
		// TODO Auto-generated method stub

	}

	private void generateAsstProfs() {
		// TODO Auto-generated method stub

	}

	private void generateVistProfs() {
		// TODO Auto-generated method stub

	}

	private void generateLectures() {
		// TODO Auto-generated method stub

	}

	private void generateUnderGradStud() {
		// TODO
	}

	private void generateGradStud() {
		// TODO
	}

	public void generateCourse() {

	}

	public String getRandomPeople() {
		// TODO Auto-generated method stub
		return null;
	}

}
