package gen;

public class Class {

	/** delimiter between different parts in an id string */
	static final char ID_DELIMITER = '/';
	/** delimiter between name and index in a name string of an instance */
	static final char INDEX_DELIMITER = '_';

	static final int INDEX_UNIV = 0;
	static final int INDEX_COLLEGE = 1;
	static final int INDEX_DEPT = 2;
	static final int INDEX_PERSON = 3;
	static final int INDEX_MAN = 4;
	static final int INDEX_WOMAN = 5;
	static final int INDEX_FACULTY = 6;
	static final int INDEX_PROF = 7;
	static final int INDEX_FULLPROF = 8;
	static final int INDEX_ASSOPROF = 9;
	static final int INDEX_ASSTPROF = 10;
	static final int INDEX_VISTPROF = 11;
	static final int INDEX_LECTURE = 12;
	static final int INDEX_STUDENT = 13;
	static final int INDEX_UNDERSTUD = 14;
	static final int INDEX_GRADSTUD = 15;
	static final int INDEX_TA = 16;
	static final int INDEX_RA = 17;
	static final int INDEX_COURSE = 18;
	static final int INDEX_GRADCOURSE = 19;
	static final int INDEX_PUBLICATION = 20;
	static final int INDEX_CHAIR = 21;
	static final int INDEX_DEAN = 22;
	static final int INDEX_RESEARCH = 23;
	static final int INDEX_RESEARCHGROUP = 24;
	static final int INDEX_SYSTEMSSTAFF = 25;
	static final int INDEX_CLERICALSTAFF = 26;
	static final int INDEX_SAMEINDIVIDUAL = 27;
	static final int INDEX_OTHERSTAFF = 28;
	static final int INDEX_WOMANCOLLEGE = 29;
	static final int INDEX_WOMANSTUDENT = 30;
	static final int INDEX_SUPPORTINGSTAFF = 31;

	/** class name strings */
	public static final String[] TOKEN = { "University", // CS_C_UNIV
			"College", // CS_C_COLLEGE
			"Department", // CS_C_DEPT
			"Person", // CS_C_PERSON
			"Man", // CS_C_MAN
			"Woman", // CS_C_WOMAN
			"Faculty", // CS_C_FACULTYCHAIR
			"Professor", // CS_C_PROF
			"FullProfessor", // CS_C_FULLPROF
			"AssociateProfessor", // CS_C_ASSOPROF
			"AssistantProfessor", // CS_C_ASSTPROF
			"VisitingProfessor", // CS_C_VISTPROF
			"Lecturer", // CS_C_LECTURER
			"Student", // CS_C_STUDENT
			"UndergraduateStudent", // CS_C_UNDERSTUD
			"GraduateStudent", // CS_C_GRADSTUD
			"TeachingAssistant", // CS_C_TA
			"ResearchAssistant", // CS_C_RA
			"Course", // CS_C_COURSE
			"GraduateCourse", // CS_C_GRADCOURSE
			"Publication", // CS_C_PUBLICATION
			"Chair", // CS_C_CHAIR
			"Dean", // CS_C_DEAN
			"Research", // CS_C_RESEARCH
			"ResearchGroup", // CS_C_RESEARCHGROUP
			"SystemsStaff", 
			"ClericalStaff", 
			"SameIndividual", 
			"other_staff", 
			"WomanCollege", 
			"woman_student",
			"SupportingStaff"
	};
	/** number of classes */
	static final int LENGTH = TOKEN.length;

	/** size of the pool of the undergraduate courses for one department */
	public static int UNDER_COURSE_NUM = 100; // must >= max faculty # *
													// FACULTY_COURSE_MAX
	/** size of the pool of the graduate courses for one department */
	public static int GRAD_COURSE_NUM = 100; // must >= max faculty # *
													// FACULTY_GRADCOURSE_MAX
	/** size of the pool of universities */
	public static int UNIV_NUM = 1000;

	public static void setup(int num) {
		UNIV_NUM = num * 5;
		UNDER_COURSE_NUM = 50 * Property.FACULTY_COURSE_MAX;
		GRAD_COURSE_NUM = 50 * Property.FACULTY_GRADCOURSE_MAX;
	}
	
	/** size of the pool of reasearch areas */
	public static final int RESEARCH_NUM = 30;
	/** minimum number of departments in a university */
	public static final int DEPT_MIN = 15;
	/** maximum number of departments in a university */
	public static final int DEPT_MAX = 23; // must: DEPT_MAX - DEPT_MIN + 1 <> 2
											// ^ n
	/** minimum number of colleges in a university */
	public static final int COLL_MIN = 1;
	/** maximum number of colleges in a university */
	public static final int COLL_MAX = 5;

	public static final int R_WOMAN_COLLEGE = 4;

	/** minimum number of research groups in a department */
	public static final int RESEARCHGROUP_MIN = 8;
	/** maximum number of research groups in a department */
	public static final int RESEARCHGROUP_MAX = 15;

	// faculty number: 30-42
	/** minimum number of full professors in a department */
	public static final int FULLPROF_MIN = 7;
	/** maximum number of full professors in a department */
	public static final int FULLPROF_MAX = 10;
	/** minimum number of associate professors in a department */
	public static final int ASSOPROF_MIN = 10;
	/** maximum number of associate professors in a department */
	public static final int ASSOPROF_MAX = 14;
	/** minimum number of assistant professors in a department */
	public static final int ASSTPROF_MIN = 7;
	/** maximum number of assistant professors in a department */
	public static final int ASSTPROF_MAX = 10;
	/** minimum number of lecturers in a department */
	public static final int LEC_MIN = 5;
	/** maximum number of lecturers in a department */
	public static final int LEC_MAX = 7;
	/** minimum number of visting professors in a department */
	public static final int VISTPROF_MIN = 0;
	/** maximum number of visting professors in a department */
	public static final int VISTPROF_MAX = 6;

	/** minimum ratio of undergraduate students to faculties in a department */
	public static final int R_UNDERSTUD_FACULTY_MIN = 12;
	/** maximum ratio of undergraduate students to faculties in a department */
	public static final int R_UNDERSTUD_FACULTY_MAX = 16;
	/** minimum ratio of graduate students to faculties in a department */
	public static final int R_GRADSTUD_FACULTY_MIN = 4;
	/** maximum ratio of graduate students to faculties in a department */
	public static final int R_GRADSTUD_FACULTY_MAX = 5;
	// MUST: FACULTY_COURSE_MIN >= R_GRADSTUD_FACULTY_MAX / R_GRADSTUD_TA_MIN;

	/** minimum ratio of graduate students to TA in a department */
	public static final int R_GRADSTUD_TA_MIN = 2;
	/** maximum ratio of graduate students to TA in a department */
	public static final int R_GRADSTUD_TA_MAX = 4;
	/** minimum ratio of graduate students to RA in a department */
	public static final int R_GRADSTUD_RA_MIN = 2;
	/** maximum ratio of graduate students to RA in a department */
	public static final int R_GRADSTUD_RA_MAX = 3;

	public static final int FAN_MIN = 0;
	public static final int FAN_MAX = 2;
	
	public static final int OTHER_STAFF_MIN = 13;
	public static final int OTHER_STAFF_MAX = 17;
	
	public static final int SYSTEMS_STAFF_MIN = 15;
	public static final int SYSTEMS_STAFF_MAX = 19;
	
	public static final int CLERICAL_STAFF_MIN = 1;
	public static final int CLERICAL_STAFF_MAX = 2;
	

	public static String getUnivID(int index) {
		return "http://www.University" + index + ".edu";
	}

	public static String getDeptID(int univIndex, int deptIndex) {
		return "http://www.Department" + deptIndex + ".University" + univIndex
				+ ".edu";
	}

	public static String getCollegeID(int univIndex, int collegeIndex) {
		return "http://www.College" + collegeIndex + ".University" + univIndex
				+ ".edu";
	}

	public static String getOtherID(String fatherID, int classType, int index) {
		return fatherID + ID_DELIMITER + TOKEN[classType] + index;
	}

	public static String getOtherID(String fatherID, String token, int index) {
		return fatherID + ID_DELIMITER + token + index;
	}

	public static String getName(int classType, int index) {
		return TOKEN[classType] + index;
	}

	public static String getName(String token, int index) {
		return token + index;
	}

}
