package uobm;

public class Property {
	
	  /** property name strings */
	  static final String[] TOKEN = {
	      "name",
	      "takesCourse",
	      "teacherOf",
	      "undergraduateDegreeFrom",
	      "mastersDegreeFrom",
	      "doctoralDegreeFrom",
	      "isAdvisedBy",
	      "isMemberOf",
	      "publicationAuthor",
	      "isHeadOf",
	      "teachingAssistantOf",
	      "researchInterest",
	      "emailAddress",
	      "telephone",
	      "subOrganizationOf",
	      "worksFor", 
	      "hasMajor", 
	      "hasSameHomeTownWith", 
	      "isFriendOf", 
	      "like", 
	      "love",
	      "isCrazyAbout", 
	      "isStudentOf", 
	      "enrollIn"
	  };
	  /** number of properties */
	  static final int PROP_NUM = TOKEN.length;
	  
	  /*
	   * about publication rate
	   */
	  public static final int FULLPROF_PUB_MIN = 15;
	  public static final int FULLPROF_PUB_MAX = 20;
	  public static final int ASSOPROF_PUB_MIN = 10;
	  public static final int ASSOPROF_PUB_MAX = 18;
	  public static final int ASSTPROF_PUB_MIN = 5;
	  public static final int ASSTPROF_PUB_MAX = 10;
	  public static final int LEC_PUB_MIN = 0;
	  public static final int LEC_PUB_MAX = 5;
	  public static final int GRADSTUD_PUB_MIN = 0;
	  public static final int GRADSTUD_PUB_MAX = 5;
	  
	  /*
	   * about courses
	   */
	  public static final int FACULTY_COURSE_MIN = 1;
	  public static final int FACULTY_COURSE_MAX = 2;
	  public static final int FACULTY_GRADCOURSE_MIN = 1;
	  public static final int FACULTY_GRADCOURSE_MAX = 2;
	  public static final int UNDERSTUD_COURSE_MIN = 2;
	  public static final int UNDERSTUD_COURSE_MAX = 4;
	  public static final int GRADSTUD_COURSE_MIN = 1;
	  public static final int GRADSTUD_COURSE_MAX = 3;
	  
	  
	  
}
