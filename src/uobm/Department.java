package uobm;

import java.util.LinkedList;

public class Department {

	int facultyNum, groupNum;
	int professorNum, assoProfNum, asstProfNum, lecturerNum, vistProfNum;
	int underStudNum, gradStudNum; 
	int publicationNum;
	LinkedList<Integer> underCourses, gradCourses, restUnderCourses, restGradCourses;
	
	Writer m_writer;
	int m_chair;
		
	public Department(Generator g) {
		groupNum = Lib.getRandomFromRange(Class.RESEARCHGROUP_MIN, Class.RESEARCHGROUP_MAX);

		/*
		 * the number of faculty 
		 */
		professorNum = Lib.getRandomFromRange(Class.FULLPROF_MIN, Class.FULLPROF_MAX);
		assoProfNum = Lib.getRandomFromRange(Class.ASSOPROF_MIN, Class.ASSOPROF_MAX);
		asstProfNum = Lib.getRandomFromRange(Class.ASSTPROF_MIN, Class.ASSTPROF_MAX);
		lecturerNum = Lib.getRandomFromRange(Class.LEC_MIN, Class.LEC_MAX);
		
		facultyNum = professorNum + assoProfNum + asstProfNum + lecturerNum;
		vistProfNum = Lib.getRandomFromRange(Class.VISTPROF_MIN, Class.VISTPROF_MAX);
		
		
		/*
		 * the number of students
		 */
		underStudNum = Lib.getRandomFromRange(facultyNum * Class.R_UNDERSTUD_FACULTY_MIN, facultyNum * Class.R_UNDERSTUD_FACULTY_MAX);
		gradStudNum = Lib.getRandomFromRange(facultyNum * Class.R_GRADSTUD_FACULTY_MIN, facultyNum * Class.R_GRADSTUD_FACULTY_MAX);
		
		/*
		 * initial the list of courses
		 */
		underCourses = new LinkedList<Integer>();
		gradCourses = new LinkedList<Integer>();
		restUnderCourses = new LinkedList<Integer>();
		restGradCourses = new LinkedList<Integer>();
		for (int i = 0; i < Class.UNDER_COURSE_NUM; ++i)
			restUnderCourses.add(i);
		for (int i = 0; i < Class.GRAD_COURSE_NUM; ++i)
			restGradCourses.add(i);
		
		m_writer = new OwlWriter(g);
		m_chair = Lib.getRandomFromRange(0, professorNum);
		
		//TODO: publications
	}
	
}
