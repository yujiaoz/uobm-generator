package uobm;

public class Class {

	
	/** delimiter between different parts in an id string*/
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
	
	  /** class name strings */
	  public static final String[] TOKEN = {
	      "University", //CS_C_UNIV
	      "College", //CS_C_COLLEGE
	      "Department", //CS_C_DEPT
	      "Person", //CS_C_PERSON
	      "Man", //CS_C_MAN
	      "Woman", //CS_C_WOMAN
	      "Faculty", //CS_C_FACULTY
	      "Professor", //CS_C_PROF
	      "FullProfessor", //CS_C_FULLPROF
	      "AssociateProfessor", //CS_C_ASSOPROF
	      "AssistantProfessor", //CS_C_ASSTPROF
	      "VistingProfessor", //CS_C_VISTPROF
	      "Lecturer", //CS_C_LECTURER
	      "Student", //CS_C_STUDENT
	      "UndergraduateStudent", //CS_C_UNDERSTUD
	      "GraduateStudent", //CS_C_GRADSTUD
	      "TeachingAssistant", //CS_C_TA
	      "ResearchAssistant", //CS_C_RA
	      "Course", //CS_C_COURSE
	      "GraduateCourse", //CS_C_GRADCOURSE
	      "Publication", //CS_C_PUBLICATION
	      "Chair", //CS_C_CHAIR
	      "Dean", //CS_C_DEAN
	      "Research", //CS_C_RESEARCH
	      "ResearchGroup" //CS_C_RESEARCHGROUP
	  };
	  /** number of classes */
	  static final int CLASS_NUM = TOKEN.length;
	  
	  /** size of the pool of the undergraduate courses for one department */
	  public static final int UNDER_COURSE_NUM = 100; //must >= max faculty # * FACULTY_COURSE_MAX
	  /** size of the pool of the graduate courses for one department */
	  public static final int GRAD_COURSE_NUM = 100; //must >= max faculty # * FACULTY_GRADCOURSE_MAX
	  /** size of the pool of universities */
	  public static final int UNIV_NUM = 1000;
	  /** size of the pool of reasearch areas */
	  public static final int RESEARCH_NUM = 30;
	  /** minimum number of departments in a university */
	  public static final int DEPT_MIN = 15;
	  /** maximum number of departments in a university */
	  public static final int DEPT_MAX = 25; //must: DEPT_MAX - DEPT_MIN + 1 <> 2 ^ n
	  /** minimum number of colleges in a university */
	  public static final int COLL_MIN = 1;
	  /** maximum number of colleges in a university */
	  public static final int COLL_MAX = 5; 
	  
	  public static final int R_WOMAN_COLLEGE = 4;

	  /** minimum number of research groups in a department */
	  public static final int RESEARCHGROUP_MIN = 10;
	  /** maximum number of research groups in a department */
	  public static final int RESEARCHGROUP_MAX = 20;
	  
	  //faculty number: 30-42
	  /** minimum number of full professors in a department*/
	  public static final int FULLPROF_MIN = 7;
	  /** maximum number of full professors in a department*/
	  public static final int FULLPROF_MAX = 10;
	  /** minimum number of associate professors in a department*/
	  public static final int ASSOPROF_MIN = 10;
	  /** maximum number of associate professors in a department*/
	  public static final int ASSOPROF_MAX = 14;
	  /** minimum number of assistant professors in a department*/
	  public static final int ASSTPROF_MIN = 8;
	  /** maximum number of assistant professors in a department*/
	  public static final int ASSTPROF_MAX = 11;
	  /** minimum number of lecturers in a department*/
	  public static final int LEC_MIN = 5;
	  /** maximum number of lecturers in a department*/
	  public static final int LEC_MAX = 7;
	  /** minimum number of visting professors in a department*/
	  public static final int VISTPROF_MIN = 0;
	  /** maximum number of visting professors in a department*/
	  public static final int VISTPROF_MAX = 2;
	  
	  /** minimum ratio of undergraduate students to faculties in a department*/
	  public static final int R_UNDERSTUD_FACULTY_MIN = 8;
	  /** maximum ratio of undergraduate students to faculties in a department*/
	  public static final int R_UNDERSTUD_FACULTY_MAX = 14;
	  /** minimum ratio of graduate students to faculties in a department*/
	  public static final int R_GRADSTUD_FACULTY_MIN = 3;
	  /** maximum ratio of graduate students to faculties in a department*/
	  public static final int R_GRADSTUD_FACULTY_MAX = 4;
	  //MUST: FACULTY_COURSE_MIN >= R_GRADSTUD_FACULTY_MAX / R_GRADSTUD_TA_MIN;
	  
	  
	  /** minimum ratio of graduate students to TA in a department */
	  public static final int R_GRADSTUD_TA_MIN = 4;
	  /** maximum ratio of graduate students to TA in a department */
	  public static final int R_GRADSTUD_TA_MAX = 5;
	  /** minimum ratio of graduate students to RA in a department */
	  public static final int R_GRADSTUD_RA_MIN = 3;
	  /** maximum ratio of graduate students to RA in a department */
	  public static final int R_GRADSTUD_RA_MAX = 4;
	  /** average ratio of undergraduate students to undergraduate student advising professors */
	  public static final int R_UNDERSTUD_ADVISOR = 5;
	  /** average ratio of graduate students to graduate student advising professors */
	  public static final int R_GRADSTUD_ADVISOR = 1;
	  
	  public static String getUnivID(int index) {
		  return "http://www.university" + index + ".edu";
	  }

	  public static String getDeptID(int univIndex, int deptIndex) {
		  return "http://www.department" + deptIndex + ".university" + univIndex + ".edu"; 
	  }
	  
	  public static String getOtherID(String deptID, int classType, int index) {
		  return deptID + ID_DELIMITER + getRelativeName(classType, index);
	  }
	  
	  public static String getRelativeName(int classType, int index) {
		  return TOKEN[classType] + "index";
	  }
	  
}
