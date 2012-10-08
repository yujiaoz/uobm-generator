package uobm;

public class Property {
	
	  static final int INDEX_NAME = 0;
	  static final int INDEX_TAKES = 1;
	  static final int INDEX_TEACHES = 2;
	  static final int INDEX_UNDERDEGREE = 3;
	  static final int INDEX_MASTERDEGREE = 4;
	  static final int INDEX_DOCDEGREE = 5;
	  static final int INDEX_ADVISED = 6;
	  static final int INDEX_MEMBER = 7;
	  static final int INDEX_AUTHOR = 8;
	  static final int INDEX_HEAD = 9;
	  static final int INDEX_TA = 10;
	  static final int INDEX_INTEREST = 11;
	  static final int INDEX_EMAIL = 12;
	  static final int INDEX_TELE = 13;
	  static final int INDEX_SUBORG = 14;
	  static final int INDEX_WORKS = 15;
	  static final int INDEX_MAJOR = 16;
	  static final int INDEX_SAMEHOMETOWN = 17;
	  static final int INDEX_FRIEND = 18;
	  static final int INDEX_LIKE = 19;
	  static final int INDEX_LOVE = 20;
	  static final int INDEX_CRAZY = 21;
	  static final int INDEX_STUDENT = 22;
	  static final int INDEX_ENROLL = 23;
	  static final int INDEX_FIRSTNAME = 24;
	  static final int INDEX_LASTNAME = 25;
	
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
	      "enrollIn", 
	      "firstName", 
	      "lastName"
	  };
	  /** number of properties */
	  static final int LENGTH = TOKEN.length;
	  
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
