package uobm;

import java.util.LinkedList;

public class Department {

	int m_groupNum;
	int m_professorNum, m_assoProfNum, m_asstProfNum, m_lecturerNum, m_vistProfNum;
	int m_underStudNum, m_gradStudNum; 
	LinkedList<Integer> m_underCourses, m_gradCourses, m_restUnderCourses, m_restGradCourses;
	
	Writer m_writer;
	int m_chair;
	Generator m_gen;
	int m_deptIndex, m_univIndex;
	String m_filename;
	
	int total_facultyNum, total_studentNum;
		
	public Department(Generator gen, int univ, int dept, String prefix) {
		m_groupNum = Lib.getRandomFromRange(Class.RESEARCHGROUP_MIN, Class.RESEARCHGROUP_MAX);

		/*
		 * the number of faculty 
		 */
		m_professorNum = Lib.getRandomFromRange(Class.FULLPROF_MIN, Class.FULLPROF_MAX);
		m_assoProfNum = Lib.getRandomFromRange(Class.ASSOPROF_MIN, Class.ASSOPROF_MAX);
		m_asstProfNum = Lib.getRandomFromRange(Class.ASSTPROF_MIN, Class.ASSTPROF_MAX);
		m_lecturerNum = Lib.getRandomFromRange(Class.LEC_MIN, Class.LEC_MAX);
		
		total_facultyNum = m_professorNum + m_assoProfNum + m_asstProfNum + m_lecturerNum;
		m_vistProfNum = Lib.getRandomFromRange(Class.VISTPROF_MIN, Class.VISTPROF_MAX);
		
		
		/*
		 * the number of students
		 */
		m_underStudNum = Lib.getRandomFromRange(total_facultyNum * Class.R_UNDERSTUD_FACULTY_MIN, total_facultyNum * Class.R_UNDERSTUD_FACULTY_MAX);
		m_gradStudNum = Lib.getRandomFromRange(total_facultyNum * Class.R_GRADSTUD_FACULTY_MIN, total_facultyNum * Class.R_GRADSTUD_FACULTY_MAX);
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
		
		m_writer = new OwlWriter(gen.ontology);
		m_chair = Lib.getRandomFromRange(0, m_professorNum);
		m_univIndex = univ;
		m_deptIndex = dept;
		m_filename = prefix + Class.INDEX_DELIMITER + "dept" + m_deptIndex + ".owl";
		
		//TODO: publications
	}
	
	public void output() {
	
	}
	
	
}
