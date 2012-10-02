package uobm;

public class Class {

	
	public static final int UNIV = 0;
	public static final int COLLEGE = 1;
	public static final int DEPT = 2;	
	public static final int PERSON = 3;	
	public static final int MAN = 4;	
	public static final int WOMAN = 5;	
	public static final int FACULTY = 6;	
	public static final int PROF = 7;	
	public static final int FULLPROF = 8;	
	public static final int ASSOPROF = 9;	
	public static final int ASSTPROF = 10;	
	public static final int VISTPROF = 11;	
	public static final int LECTURE = 12;	
	public static final int STUDENT = 13;	
	public static final int UNDERSTUD = 14;	
	public static final int GRADSTUD = 15;	
	public static final int TA = 16;	
	public static final int RA = 17;
	public static final int COURSE = 18;	
	public static final int GRADCOURSE = 19;	
	public static final int PUBLICATION = 20;	
	public static final int CHAIR = 21;	
	public static final int DEAN = 22;	
	public static final int RESEARCH = 23;	
	public static final int RESEARCHGROUP = 24;	
	
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
	  
	  public static String getID(int index) {
		  String id;
		  switch (classType) {
		     case CS_C_UNIV:
		       id = "http://www." + getRelativeName(classType, index) + ".edu";
		       break;
		     case CS_C_DEPT:
		       id = "http://www." + getRelativeName(classType, index) + "." +
		           getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) +
		           ".edu";
		       break;
		     default:
		       id = getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1) + ID_DELIMITER +
		           getRelativeName(classType, index);
		       break;
		  }
		  return id;
	  }
	  
	  
	private String _getRelativeName(int classType, int index) {
		    String name;

		    switch (classType) {
		      case CS_C_UNIV:
		        //should be unique too!
		        name = CLASS_TOKEN[classType] + index;
		        break;
		      case CS_C_DEPT:
		        name = CLASS_TOKEN[classType] + index;
		        break;
		      default:
		    	if (classType == 23)
		    		System.out.println("here");
		        name = CLASS_TOKEN[classType] + index;
		        break;
		    }

		    return name;
		  }
}
