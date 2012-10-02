
package uobm;

import java.util.*;
import java.io.*;

public class Generator {

  /** delimiter between different parts in an id string*/
  private static final char ID_DELIMITER = '/';
  /** delimiter between name and index in a name string of an instance */
  private static final char INDEX_DELIMITER = '_';
  /** name of the log file */
  private static final String LOG_FILE = "log.txt";

  /** instance count of a class */
  private class InstanceCount {
    /** instance number within one department */
    public int num = 0;
    /** total instance num including sub-classes within one department */
    public int total = 0;
    /** index of the current instance within the current department */
    public int count = 0;
    /** total number so far within the current department */
    public int logNum = 0;
    /** total number so far */
    public long logTotal = 0l;
  }

  /** instance count of a property */
  private class PropertyCount {
    /** total number so far within the current department */
    public int logNum = 0;
    /** total number so far */
    public long logTotal = 0l;
  }

  /** information a course instance */
  private class CourseInfo {
    /** index of the faculty who teaches this course */
    public int indexInFaculty = 0;
    /** index of this course */
    public int globalIndex = 0;
  }

  /** information of an RA instance */
  private class RaInfo {
    /** index of this RA in the graduate students */
    public int indexInGradStud = 0;
  }

  /** information of a TA instance */
  private class TaInfo {
    /** index of this TA in the graduate students */
    public int indexInGradStud = 0;
    /** index of the course which this TA assists */
    public int indexInCourse = 0; //local index in courses
  }

  /** informaiton of a publication instance */
  private class PublicationInfo {
    /** id */
    public String id;
    /** name */
    public String name;
    /** list of authors */
    public ArrayList<String> authors;
  }

  /** univ-bench-dl ontology url */
  String ontology;

  /** seed of the random number genertor for the current university */
  private long seed_ = 0l;
  /** user specified seed for the data generation */
  private long baseSeed = 0l;
  /** log writer */
  private PrintStream log_ = null;
  
  private int univNum;
  private int startIndex;
  private University[] universities;

  /**
   * main method
   */
  public static void main(String[] args) {
    //default values
    int univNum = 1, startIndex = 0, seed = 0;
    boolean daml = false;
    String ontology = null;

    try {
      String arg;
      int i = 0;
      while (i < args.length) {
        arg = args[i++];
        if (arg.equals("-univ")) {
          if (i < args.length) {
            arg = args[i++];
            univNum = Integer.parseInt(arg);
            if (univNum < 1)
              throw new NumberFormatException();
          }
          else
            throw new NumberFormatException();
        }
        else if (arg.equals("-index")) {
          if (i < args.length) {
            arg = args[i++];
            startIndex = Integer.parseInt(arg);
            if (startIndex < 0)
              throw new NumberFormatException();
          }
          else
            throw new NumberFormatException();
        }
        else if (arg.equals("-seed")) {
          if (i < args.length) {
            arg = args[i++];
            seed = Integer.parseInt(arg);
            if (seed < 0)
              throw new NumberFormatException();
          }
          else
            throw new NumberFormatException();
        }
        else if (arg.equals("-onto")) {
          if (i < args.length) {
            arg = args[i++];
            ontology = arg;
          }
          else
            throw new Exception();
        }
        else
          throw new Exception();
      }
      if ( ( (long) startIndex + univNum - 1) > Integer.MAX_VALUE) {
        System.err.println("Index overflow!");
        throw new Exception();
      }
      if (null == ontology) {
        System.err.println("ontology url is requested!");
        throw new Exception();
      }
    }
    catch (Exception e) {
      System.err.println("Usage: Generator\n" +
                         "\t[-univ <num of universities(1~" + Integer.MAX_VALUE +
                         ")>]\n" +
                         "\t[-index <start index(0~" + Integer.MAX_VALUE +
                         ")>]\n" +
                         "\t[-seed <seed(0~" + Integer.MAX_VALUE + ")>]\n" +
                         "\t[-daml]\n" +
                         "\t-onto <univ-bench ontology url>");
      System.exit(0);
    }

    new Generator().start(univNum, startIndex, seed, daml, ontology);
  }

  /**
   * Begins the data generation.
   * @param univNum Number of universities to generate.
   * @param startIndex Starting index of the universities.
   * @param seed Seed for data generation.
   * @param daml Generates DAML+OIL data if true, OWL data otherwise.
   * @param ontology Ontology url.
   */
  public void start(int univNum, int startIndex, int seed, boolean daml, String ontology) {
    this.ontology = ontology;
    this.startIndex = startIndex;
    this.baseSeed = seed;
    this.univNum = univNum;
    _generate();
    System.out.println("See log.txt for more details.");
  }

  ///////////////////////////////////////////////////////////////////////////
  //writer callbacks

  /**
   * Callback by the writer when it starts an instance section.
   * @param classType Type of the instance.
   */
  void startSectionCB(int classType) {
    instances_[classType].logNum++;
    instances_[classType].logTotal++;
  }

  /**
   * Callback by the writer when it starts an instance section identified by an rdf:about attribute.
   * @param classType Type of the instance.
   */
  void startAboutSectionCB(int classType) {
    startSectionCB(classType);
  }

  /**
   * Callback by the writer when it adds a property statement.
   * @param property Type of the property.
   */
  void addPropertyCB(int property) {
    properties_[property].logNum++;
    properties_[property].logTotal++;
  }

  /**
   * Callback by the writer when it adds a property statement whose value is an individual.
   * @param classType Type of the individual.
   */
  void addValueClassCB(int classType) {
    instances_[classType].logNum++;
    instances_[classType].logTotal++;
  }

  ///////////////////////////////////////////////////////////////////////////

  /**
   * Sets instance specification.
   */
  private void _setInstanceInfo(Specification spec) {
    int subClass, superClass;

    for (int i = 0; i < CLASS_NUM; i++) {
      switch (i) {
        case CS_C_UNIV: case CS_C_COLL: case CS_C_DEPT: //case CS_C_DEAN:
          break;
        case CS_C_FULLPROF: case CS_C_ASSOPROF: case CS_C_ASSTPROF: case CS_C_LECTURER: case CS_C_UNDERSTUD: case CS_C_GRADSTUD:
        case CS_C_COURSE: case CS_C_GRADCOURSE:
        case CS_C_RESEARCHGROUP:        
        	instances_[i].num = spec.num[i];
        	
        case CS_C_TA:
            instances_[i].num = _getRandomFromRange(instances_[CS_C_GRADSTUD].total /
            		R_GRADSTUD_TA_MAX,
            		instances_[CS_C_GRADSTUD].total /
            		R_GRADSTUD_TA_MIN);
            break;
        case CS_C_RA:
        	instances_[i].num = _getRandomFromRange(instances_[CS_C_GRADSTUD].total /
        			R_GRADSTUD_RA_MAX,
        			instances_[CS_C_GRADSTUD].total /
        			R_GRADSTUD_RA_MIN);
            break;
        default:
          instances_[i].num = CLASS_INFO[i][INDEX_NUM];
          break;
      }
      instances_[i].total = instances_[i].num;
      subClass = i;
      while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
        instances_[superClass].total += instances_[i].num;
        subClass = superClass;
      }
    }
  }

  private Specification total; 
  
  /** Begins data generation according to the specification */
  private void _generate() {
    System.out.println("Started...");
    
    universities = new University[univNum];
    for (int i = 0; i < univNum; ++i)
    	universities[i] = new University(this);
    
    try {
      log_ = new PrintStream(new FileOutputStream(System.getProperty("user.dir") +
                                                 System.getProperty("file.separator") + LOG_FILE));
      writer_.start();
      for (int i = 0, j = 0; i < instances_[CS_C_UNIV].num; i++) {
    	  _generateUniv(i + startIndex, numDept[i], deptSpec, j);
    	  j += numDept[i];
      }
      writer_.end();
      log_.close();
    }
    catch (IOException e) {
      System.out.println("Failed to create log file!");
    }
    System.out.println("Completed!");
  }

  /**
   * Creates a university.
   * @param index Index of the university.
   */
  private void _generateUniv(int index, int deptNum, Specification[] specs, int startIndex) {
    //this transformation guarantees no different pairs of (index, baseSeed) generate the same data
    seed_ = baseSeed * (Integer.MAX_VALUE + 1) + index;
    Lib.setSeed(seed_);

    //determine department number
    instances_[CS_C_DEPT].num = deptNum;
    instances_[CS_C_DEPT].count = 0;
    //generate departments
    for (int i = 0; i < instances_[CS_C_DEPT].num; i++) {
      _generateDept(index, i, specs[startIndex + i]);
    }
  }

  /**
   * Creates a department.
   * @param univIndex Index of the current university.
   * @param index Index of the department.
   * NOTE: Use univIndex instead of instances[CS_C_UNIV].count till generateASection(CS_C_UNIV, ) is invoked.
   */
  private void _generateDept(int univIndex, int index, Specification spec) {
    String fileName = System.getProperty("user.dir") + System.getProperty("file.separator") +
        _getName(CS_C_UNIV, univIndex) + INDEX_DELIMITER + "dept" + index + _getFileSuffix();
    writer_.startFile(fileName);

    //reset
    _setInstanceInfo(spec);
    underCourses_.clear();
    gradCourses_.clear();
    remainingUnderCourses_.clear();
    remainingGradCourses_.clear();
    for (int i = 0; i < UNDER_COURSE_NUM; i++) {
      remainingUnderCourses_.add(new Integer(i));
    }
    for (int i = 0; i < GRAD_COURSE_NUM; i++) {
      remainingGradCourses_.add(new Integer(i));
    }
    publications_.clear();
    for (int i = 0; i < CLASS_NUM; i++) {
      instances_[i].logNum = 0;
    }
    for (int i = 0; i < PROP_NUM; i++) {
      properties_[i].logNum = 0;
    }

    //decide the chair
    chair_ = random_.nextInt(instances_[CS_C_FULLPROF].total);

    if (index == 0) {
      _generateASection(CS_C_UNIV, univIndex);
    }
    _generateASection(CS_C_DEPT, index);
    for (int i = CS_C_DEPT + 1; i < CLASS_NUM; i++) {
      instances_[i].count = 0;
      for (int j = 0; j < instances_[i].num; j++) {
        _generateASection(i, j);
      }
    }

    _generatePublications();
    _generateCourses();
    _generateRaTa();

    System.out.println(fileName + " generated");
    String bar = "";
    for (int i = 0; i < fileName.length(); i++)
      bar += '-';
    log_.println(bar);
    log_.println(fileName);
    log_.println(bar);
    _generateComments();
    writer_.endFile();
  }

  ///////////////////////////////////////////////////////////////////////////
  //instance generation

  /**
   * Generates an instance of the specified class
   * @param classType Type of the instance.
   * @param index Index of the instance.
   */
  private void _generateASection(int classType, int index) {
    _updateCount(classType);

    switch (classType) {
      case CS_C_UNIV:
        _generateAUniv(index);
        break;
      case CS_C_DEPT:
        _generateADept(index);
        break;
      case CS_C_FACULTY:
        _generateAFaculty(index);
        break;
      case CS_C_PROF:
        _generateAProf(index);
        break;
      case CS_C_FULLPROF:
        _generateAFullProf(index);
        break;
      case CS_C_ASSOPROF:
        _generateAnAssociateProfessor(index);
        break;
      case CS_C_ASSTPROF:
        _generateAnAssistantProfessor(index);
        break;
      case CS_C_LECTURER:
        _generateALecturer(index);
        break;
      case CS_C_UNDERSTUD:
        _generateAnUndergraduateStudent(index);
        break;
      case CS_C_GRADSTUD:
        _generateAGradudateStudent(index);
        break;
      case CS_C_COURSE:
        _generateACourse(index);
        break;
      case CS_C_GRADCOURSE:
        _generateAGraduateCourse(index);
        break;
      case CS_C_RESEARCHGROUP:
        _generateAResearchGroup(index);
        break;
      default:
        break;
    }
  }

  /**
   * Generates a university instance.
   * @param index Index of the instance.
   */
  private void _generateAUniv(int index) {
    writer_.startSection(CS_C_UNIV, _getId(CS_C_UNIV, index));
    writer_.addProperty(CS_P_NAME, _getRelativeName(CS_C_UNIV, index), false);
    writer_.endSection(CS_C_UNIV);
  }

  /**
   * Generates a department instance.
   * @param index Index of the department.
   */
  private void _generateADept(int index) {
    writer_.startSection(CS_C_DEPT, _getId(CS_C_DEPT, index));
    writer_.addProperty(CS_P_NAME, _getRelativeName(CS_C_DEPT, index), false);
    writer_.addProperty(CS_P_SUBORGANIZATIONOF, CS_C_UNIV,
                       _getId(CS_C_UNIV, instances_[CS_C_UNIV].count - 1));
    writer_.endSection(CS_C_DEPT);
  }

  /**
   * Generates a faculty instance.
   * @param index Index of the faculty.
   */
  private void _generateAFaculty(int index) {
    writer_.startSection(CS_C_FACULTY, _getId(CS_C_FACULTY, index));
    _generateAFaculty_a(CS_C_FACULTY, index);
    writer_.endSection(CS_C_FACULTY);
  }

  /**
   * Generates properties for the specified faculty instance.
   * @param type Type of the faculty.
   * @param index Index of the instance within its type.
   */
  private void _generateAFaculty_a(int type, int index) {
    int indexInFaculty;
    int courseNum;
    int courseIndex;
    boolean dup;
    CourseInfo course;

    indexInFaculty = instances_[CS_C_FACULTY].count - 1;

    writer_.addProperty(CS_P_NAME, _getRelativeName(type, index), false);

    //undergradutate courses
    courseNum = _getRandomFromRange(FACULTY_COURSE_MIN, FACULTY_COURSE_MAX);
    for (int i = 0; i < courseNum; i++) {
      courseIndex = _AssignCourse(indexInFaculty);
      writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_COURSE, courseIndex), true);
    }
    //gradutate courses
    courseNum = _getRandomFromRange(FACULTY_GRADCOURSE_MIN, FACULTY_GRADCOURSE_MAX);
    for (int i = 0; i < courseNum; i++) {
      courseIndex = _AssignGraduateCourse(indexInFaculty);
      writer_.addProperty(CS_P_TEACHEROF, _getId(CS_C_GRADCOURSE, courseIndex), true);
    }
    //person properties
    writer_.addProperty(CS_P_UNDERGRADFROM, CS_C_UNIV,
                       _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
    writer_.addProperty(CS_P_GRADFROM, CS_C_UNIV,
                       _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
    writer_.addProperty(CS_P_DOCFROM, CS_C_UNIV,
                       _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
    writer_.addProperty(CS_P_WORKSFOR,
                       _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
    writer_.addProperty(CS_P_EMAIL, _getEmail(type, index), false);
    writer_.addProperty(CS_P_TELEPHONE, "xxx-xxx-xxxx", false);
  }

  /**
   * Assigns an undergraduate course to the specified faculty.
   * @param indexInFaculty Index of the faculty.
   * @return Index of the selected course in the pool.
   */
  private int _AssignCourse(int indexInFaculty) {
    //NOTE: this line, although overriden by the next one, is deliberately kept
    // to guarantee identical random number generation to the previous version.
    int pos = _getRandomFromRange(0, remainingUnderCourses_.size() - 1);
    pos = 0; //fetch courses in sequence

    CourseInfo course = new CourseInfo();
    course.indexInFaculty = indexInFaculty;
    course.globalIndex = ( (Integer) remainingUnderCourses_.get(pos)).intValue();
    underCourses_.add(course);

    remainingUnderCourses_.remove(pos);

    return course.globalIndex;
  }

  /**
   * Assigns a graduate course to the specified faculty.
   * @param indexInFaculty Index of the faculty.
   * @return Index of the selected course in the pool.
   */
  private int _AssignGraduateCourse(int indexInFaculty) {
    //NOTE: this line, although overriden by the next one, is deliberately kept
    // to guarantee identical random number generation to the previous version.
    int pos = _getRandomFromRange(0, remainingGradCourses_.size() - 1);
    pos = 0; //fetch courses in sequence

    CourseInfo course = new CourseInfo();
    course.indexInFaculty = indexInFaculty;
    course.globalIndex = ( (Integer) remainingGradCourses_.get(pos)).intValue();
    gradCourses_.add(course);

    remainingGradCourses_.remove(pos);

    return course.globalIndex;
  }

  /**
   * Generates a professor instance.
   * @param index Index of the professor.
   */
  private void _generateAProf(int index) {
    writer_.startSection(CS_C_PROF, _getId(CS_C_PROF, index));
    _generateAProf_a(CS_C_PROF, index);
    writer_.endSection(CS_C_PROF);
  }

  /**
   * Generates properties for a professor instance.
   * @param type Type of the professor.
   * @param index Index of the intance within its type.
   */
  private void _generateAProf_a(int type, int index) {
    _generateAFaculty_a(type, index);
    writer_.addProperty(CS_P_RESEARCHINTEREST,
                       _getRelativeName(CS_C_RESEARCH,
                                       random_.nextInt(RESEARCH_NUM)), false);
  }

  /**
   * Generates a full professor instances.
   * @param index Index of the full professor.
   */
  private void _generateAFullProf(int index) {
    String id;

    id = _getId(CS_C_FULLPROF, index);
    writer_.startSection(CS_C_FULLPROF, id);
    _generateAProf_a(CS_C_FULLPROF, index);
    if (index == chair_) {
      writer_.addProperty(CS_P_HEADOF,
                         _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
    }
    writer_.endSection(CS_C_FULLPROF);
    _assignFacultyPublications(id, FULLPROF_PUB_MIN, FULLPROF_PUB_MAX);
  }

  /**
   * Generates an associate professor instance.
   * @param index Index of the associate professor.
   */
  private void _generateAnAssociateProfessor(int index) {
    String id = _getId(CS_C_ASSOPROF, index);
    writer_.startSection(CS_C_ASSOPROF, id);
    _generateAProf_a(CS_C_ASSOPROF, index);
    writer_.endSection(CS_C_ASSOPROF);
    _assignFacultyPublications(id, ASSOPROF_PUB_MIN, ASSOPROF_PUB_MAX);
  }

  /**
   * Generates an assistant professor instance.
   * @param index Index of the assistant professor.
   */
  private void _generateAnAssistantProfessor(int index) {
    String id = _getId(CS_C_ASSTPROF, index);
    writer_.startSection(CS_C_ASSTPROF, id);
    _generateAProf_a(CS_C_ASSTPROF, index);
    writer_.endSection(CS_C_ASSTPROF);
    _assignFacultyPublications(id, ASSTPROF_PUB_MIN, ASSTPROF_PUB_MAX);
  }

  /**
   * Generates a lecturer instance.
   * @param index Index of the lecturer.
   */
  private void _generateALecturer(int index) {
    String id = _getId(CS_C_LECTURER, index);
    writer_.startSection(CS_C_LECTURER, id);
    _generateAFaculty_a(CS_C_LECTURER, index);
    writer_.endSection(CS_C_LECTURER);
    _assignFacultyPublications(id, LEC_PUB_MIN, LEC_PUB_MAX);
  }

  /**
   * Assigns publications to the specified faculty.
   * @param author Id of the faculty
   * @param min Minimum number of publications
   * @param max Maximum number of publications
   */
  private void _assignFacultyPublications(String author, int min, int max) {
    int num;
    PublicationInfo publication;

    num = _getRandomFromRange(min, max);
    for (int i = 0; i < num; i++) {
      publication = new PublicationInfo();
      publication.id = _getId(CS_C_PUBLICATION, i, author);
      publication.name = _getRelativeName(CS_C_PUBLICATION, i);
      publication.authors = new ArrayList<String>();
      publication.authors.add(author);
      publications_.add(publication);
    }
  }

  /**
   * Assigns publications to the specified graduate student. The publications are
   * chosen from some faculties'.
   * @param author Id of the graduate student.
   * @param min Minimum number of publications.
   * @param max Maximum number of publications.
   */
  private void _assignGraduateStudentPublications(String author, int min, int max) {
    int num;
    PublicationInfo publication;

    num = _getRandomFromRange(min, max);
    ArrayList list = _getRandomList(num, 0, publications_.size() - 1);
    for (int i = 0; i < list.size(); i++) {
      publication = (PublicationInfo) publications_.get( ( (Integer) list.get(i)).
                                               intValue());
      publication.authors.add(author);
    }
  }

  /**
   * Generates publication instances. These publications are assigned to some faculties
   * and graduate students before.
   */
  private void _generatePublications() {
    for (int i = 0; i < publications_.size(); i++) {
      _generateAPublication( (PublicationInfo) publications_.get(i));
    }
  }

  /**
   * Generates a publication instance.
   * @param publication Information of the publication.
   */
  private void _generateAPublication(PublicationInfo publication) {
    writer_.startSection(CS_C_PUBLICATION, publication.id);
    writer_.addProperty(CS_P_NAME, publication.name, false);
    for (int i = 0; i < publication.authors.size(); i++) {
      writer_.addProperty(CS_P_PUBLICATIONAUTHOR,
                         (String) publication.authors.get(i), true);
    }
    writer_.endSection(CS_C_PUBLICATION);
  }

  /**
   * Generates properties for the specified student instance.
   * @param type Type of the student.
   * @param index Index of the instance within its type.
   */
  private void _generateAStudent_a(int type, int index) {
    writer_.addProperty(CS_P_NAME, _getRelativeName(type, index), false);
    writer_.addProperty(CS_P_ENROLLIN,
                       _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
    writer_.addProperty(CS_P_EMAIL, _getEmail(type, index), false);
    writer_.addProperty(CS_P_TELEPHONE, "xxx-xxx-xxxx", false);
  }
  
  private String _getADiffPerson(int type, int index) {
	  int type1 = type, index1 = index;
	  while (type1 == type && index1 == index) {
		  index1 = _getRandomFromRange(0, total.num[CS_C_PERSON] - 1);
		  if (index1 < total.num[CS_C_FULLPROF]) {
			  type = CS_C_FULLPROF;
			  
		  }
			  
	  }
		 
  }
  
  private void _generateAPerson_a(int type, int index) {
	  writer_.addProperty(CS_P_SAMEHOMETOWN, _getADiffPerson(type, index), true);
	  writer_.addProperty(CS_P_FRIENDOF, _getADiffPerson(type, index), true);
  }

  /**
   * Generates an undergraduate student instance.
   * @param index Index of the undergraduate student.
   */
  private void _generateAnUndergraduateStudent(int index) {
    int n;
    ArrayList list;

    writer_.startSection(CS_C_UNDERSTUD, _getId(CS_C_UNDERSTUD, index));
    _generateAStudent_a(CS_C_UNDERSTUD, index);
    n = _getRandomFromRange(UNDERSTUD_COURSE_MIN, UNDERSTUD_COURSE_MAX);
    list = _getRandomList(n, 0, underCourses_.size() - 1);
    for (int i = 0; i < list.size(); i++) {
      CourseInfo info = (CourseInfo) underCourses_.get( ( (Integer) list.get(i)).
          intValue());
      writer_.addProperty(CS_P_TAKECOURSE, _getId(CS_C_COURSE, info.globalIndex), true);
    }
    if (0 == random_.nextInt(R_UNDERSTUD_ADVISOR)) {
      writer_.addProperty(CS_P_ADVISOR, _selectAdvisor(), true);
    }
    writer_.endSection(CS_C_UNDERSTUD);
  }

  /**
   * Generates a graduate student instance.
   * @param index Index of the graduate student.
   */
  private void _generateAGradudateStudent(int index) {
    int n;
    ArrayList list;
    String id;

    id = _getId(CS_C_GRADSTUD, index);
    writer_.startSection(CS_C_GRADSTUD, id);
    _generateAStudent_a(CS_C_GRADSTUD, index);
    n = _getRandomFromRange(GRADSTUD_COURSE_MIN, GRADSTUD_COURSE_MAX);
    list = _getRandomList(n, 0, gradCourses_.size() - 1);
    for (int i = 0; i < list.size(); i++) {
      CourseInfo info = (CourseInfo) gradCourses_.get( ( (Integer) list.get(i)).
          intValue());
      writer_.addProperty(CS_P_TAKECOURSE,
                         _getId(CS_C_GRADCOURSE, info.globalIndex), true);
    }
    writer_.addProperty(CS_P_UNDERGRADFROM, CS_C_UNIV,
                       _getId(CS_C_UNIV, random_.nextInt(UNIV_NUM)));
    if (0 == random_.nextInt(R_GRADSTUD_ADVISOR)) {
      writer_.addProperty(CS_P_ADVISOR, _selectAdvisor(), true);
    }
    _assignGraduateStudentPublications(id, GRADSTUD_PUB_MIN, GRADSTUD_PUB_MAX);
    writer_.endSection(CS_C_GRADSTUD);
  }

  /**
   * Select an advisor from the professors.
   * @return Id of the selected professor.
   */
  private String _selectAdvisor() {
    int profType;
    int index;

    profType = _getRandomFromRange(CS_C_FULLPROF, CS_C_ASSTPROF);
    index = random_.nextInt(instances_[profType].total);
    return _getId(profType, index);
  }

  /**
   * Generates a TA instance according to the specified information.
   * @param ta Information of the TA.
   */
  private void _generateATa(TaInfo ta) {
    writer_.startAboutSection(CS_C_TA, _getId(CS_C_GRADSTUD, ta.indexInGradStud));
    writer_.addProperty(CS_P_TAOF, _getId(CS_C_COURSE, ta.indexInCourse), true);
    writer_.endSection(CS_C_TA);
  }

  /**
   * Generates an RA instance according to the specified information.
   * @param ra Information of the RA.
   */
  private void _generateAnRa(RaInfo ra) {
    writer_.startAboutSection(CS_C_RA, _getId(CS_C_GRADSTUD, ra.indexInGradStud));
    writer_.endSection(CS_C_RA);
  }

  /**
   * Generates a course instance.
   * @param index Index of the course.
   */
  private void _generateACourse(int index) {
    writer_.startSection(CS_C_COURSE, _getId(CS_C_COURSE, index));
    writer_.addProperty(CS_P_NAME,
                       _getRelativeName(CS_C_COURSE, index), false);
    writer_.endSection(CS_C_COURSE);
  }

  /**
   * Generates a graduate course instance.
   * @param index Index of the graduate course.
   */
  private void _generateAGraduateCourse(int index) {
    writer_.startSection(CS_C_GRADCOURSE, _getId(CS_C_GRADCOURSE, index));
    writer_.addProperty(CS_P_NAME,
                       _getRelativeName(CS_C_GRADCOURSE, index), false);
    writer_.endSection(CS_C_GRADCOURSE);
  }

  /**
   * Generates course/graduate course instances. These course are assigned to some
   * faculties before.
   */
  private void _generateCourses() {
    for (int i = 0; i < underCourses_.size(); i++) {
      _generateACourse( ( (CourseInfo) underCourses_.get(i)).globalIndex);
    }
    for (int i = 0; i < gradCourses_.size(); i++) {
      _generateAGraduateCourse( ( (CourseInfo) gradCourses_.get(i)).globalIndex);
    }
  }

  /**
   * Chooses RAs and TAs from graduate student and generates their instances accordingly.
   */
  private void _generateRaTa() {
    ArrayList list, courseList;
    TaInfo ta;
    RaInfo ra;
    ArrayList tas, ras;
    int i;

    tas = new ArrayList();
    ras = new ArrayList();
    list = _getRandomList(instances_[CS_C_TA].total + instances_[CS_C_RA].total,
                      0, instances_[CS_C_GRADSTUD].total - 1);
    courseList = _getRandomList(instances_[CS_C_TA].total, 0,
                            underCourses_.size() - 1);

    for (i = 0; i < instances_[CS_C_TA].total; i++) {
      ta = new TaInfo();
      ta.indexInGradStud = ( (Integer) list.get(i)).intValue();
      ta.indexInCourse = ( (CourseInfo) underCourses_.get( ( (Integer)
          courseList.get(i)).intValue())).globalIndex;
      _generateATa(ta);
    }
    while (i < list.size()) {
      ra = new RaInfo();
      ra.indexInGradStud = ( (Integer) list.get(i)).intValue();
      _generateAnRa(ra);
      i++;
    }
  }

  /**
   * Generates a research group instance.
   * @param index Index of the research group.
   */
  private void _generateAResearchGroup(int index) {
    String id;
    id = _getId(CS_C_RESEARCHGROUP, index);
    writer_.startSection(CS_C_RESEARCHGROUP, id);
    writer_.addProperty(CS_P_SUBORGANIZATIONOF,
                       _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1), true);
    writer_.endSection(CS_C_RESEARCHGROUP);
  }

  ///////////////////////////////////////////////////////////////////////////

  /**
   * @return Suffix of the data file.
   */
  private String _getFileSuffix() {
    return isDaml_ ? ".daml" : ".owl";
  }

  /**
   * Gets the id of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return Id of the instance.
   */
  private String _getId(int classType, int index) {
    String id;

    switch (classType) {
      case CS_C_UNIV:
        id = "http://www." + _getRelativeName(classType, index) + ".edu";
        break;
      case CS_C_DEPT:
        id = "http://www." + _getRelativeName(classType, index) + "." +
            _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) +
            ".edu";
        break;
      default:
        id = _getId(CS_C_DEPT, instances_[CS_C_DEPT].count - 1) + ID_DELIMITER +
            _getRelativeName(classType, index);
        break;
    }

    return id;
  }

  /**
   * Gets the id of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @param param Auxiliary parameter.
   * @return Id of the instance.
   */
  private String _getId(int classType, int index, String param) {
    String id;

    switch (classType) {
      case CS_C_PUBLICATION:
        //NOTE: param is author id
        id = param + ID_DELIMITER + CLASS_TOKEN[classType] + index;
        break;
      default:
        id = _getId(classType, index);
        break;
    }

    return id;
  }

  /**
   * Gets the globally unique name of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return Name of the instance.
   */
  private String _getName(int classType, int index) {
    String name;

    switch (classType) {
      case CS_C_UNIV:
        name = _getRelativeName(classType, index);
        break;
      case CS_C_DEPT:
        name = _getRelativeName(classType, index) + INDEX_DELIMITER +
            (instances_[CS_C_UNIV].count - 1);
        break;
      //NOTE: Assume departments with the same index share the same pool of courses and researches
      case CS_C_COURSE:
      case CS_C_GRADCOURSE:
      case CS_C_RESEARCH:
        name = _getRelativeName(classType, index) + INDEX_DELIMITER +
            (instances_[CS_C_DEPT].count - 1);
        break;
      default:
        name = _getRelativeName(classType, index) + INDEX_DELIMITER +
            (instances_[CS_C_DEPT].count - 1) + INDEX_DELIMITER +
            (instances_[CS_C_UNIV].count - 1);
        break;
    }

    return name;
  }

  /**
   * Gets the name of the specified instance that is unique within a department.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return Name of the instance.
   */
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

  /**
   * Gets the email address of the specified instance.
   * @param classType Type of the instance.
   * @param index Index of the instance within its type.
   * @return The email address of the instance.
   */
  private String _getEmail(int classType, int index) {
    String email = "";

    switch (classType) {
      case CS_C_UNIV:
        email += _getRelativeName(classType, index) + "@" +
            _getRelativeName(classType, index) + ".edu";
        break;
      case CS_C_DEPT:
        email += _getRelativeName(classType, index) + "@" +
            _getRelativeName(classType, index) + "." +
            _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) + ".edu";
        break;
      default:
        email += _getRelativeName(classType, index) + "@" +
            _getRelativeName(CS_C_DEPT, instances_[CS_C_DEPT].count - 1) +
            "." + _getRelativeName(CS_C_UNIV, instances_[CS_C_UNIV].count - 1) +
            ".edu";
        break;
    }

    return email;
  }

  /**
   * Increases by 1 the instance count of the specified class. This also includes
   * the increase of the instacne count of all its super class.
   * @param classType Type of the instance.
   */
  private void _updateCount(int classType) {
    int subClass, superClass;

    instances_[classType].count++;
    subClass = classType;
    while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
      instances_[superClass].count++;
      subClass = superClass;
    }
  }

  /**
   * Creates a list of the specified number of integers without duplication which
   * are randomly selected from the specified range.
   * @param num Number of the integers.
   * @param min Minimum value of selectable integer.
   * @param max Maximum value of selectable integer.
   * @return So generated list of integers.
   */
  private ArrayList _getRandomList(int num, int min, int max) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    ArrayList<Integer> tmp = new ArrayList<Integer>();
    for (int i = min; i <= max; i++) {
      tmp.add(new Integer(i));
    }

    for (int i = 0; i < num; i++) {
      int pos = _getRandomFromRange(0, tmp.size() - 1);
      list.add( (Integer) tmp.get(pos));
      tmp.remove(pos);
    }

    return list;
  }

  /**
   * Randomly selects a integer from the specified range.
   * @param min Minimum value of the selectable integer.
   * @param max Maximum value of the selectable integer.
   * @return The selected integer.
   */
  public int _getRandomFromRange(int min, int max) {
    return min + random_.nextInt(max - min + 1);
  }

  /**
   * Outputs log information to both the log file and the screen after a department
   * is generated.
   */
  private void _generateComments() {
    int classInstNum = 0; //total class instance num in this department
    long totalClassInstNum = 0l; //total class instance num so far
    int propInstNum = 0; //total property instance num in this department
    long totalPropInstNum = 0l; //total property instance num so far
    String comment;

    comment = "External Seed=" + baseSeed + " Interal Seed=" + seed_;
    log_.println(comment);
    log_.println();

    comment = "CLASS INSTANCE# TOTAL-SO-FAR";
    log_.println(comment);
    comment = "----------------------------";
    log_.println(comment);
    for (int i = 0; i < CLASS_NUM; i++) {
      comment = CLASS_TOKEN[i] + " " + instances_[i].logNum + " " +
          instances_[i].logTotal;
      log_.println(comment);
      classInstNum += instances_[i].logNum;
      totalClassInstNum += instances_[i].logTotal;
    }
    log_.println();
    comment = "TOTAL: " + classInstNum;
    log_.println(comment);
    comment = "TOTAL SO FAR: " + totalClassInstNum;
    log_.println(comment);

    comment = "PROPERTY---INSTANCE NUM";
    log_.println();
    comment = "PROPERTY INSTANCE# TOTAL-SO-FAR";
    log_.println(comment);
    comment = "-------------------------------";
    log_.println(comment);
    for (int i = 0; i < PROP_NUM; i++) {
      comment = PROP_TOKEN[i] + " " + properties_[i].logNum;
      comment = comment + " " + properties_[i].logTotal;
      log_.println(comment);
      propInstNum += properties_[i].logNum;
      totalPropInstNum += properties_[i].logTotal;
    }
    log_.println();
    comment = "TOTAL: " + propInstNum;
    log_.println(comment);
    comment = "TOTAL SO FAR: " + totalPropInstNum;
    log_.println(comment);

    System.out.println("CLASS INSTANCE #: " + classInstNum + ", TOTAL SO FAR: " +
                       totalClassInstNum);
    System.out.println("PROPERTY INSTANCE #: " + propInstNum +
                       ", TOTAL SO FAR: " + totalPropInstNum);
    System.out.println();

    log_.println();
  }

  private class Specification {

	int[] num; 
	
	public Specification() {
		for (int i = 0; i < CLASS_NUM; ++i)
			num[i] = 0;
	}
	
	public void add(Specification spec, int index) {
		for (int i = index; i < CLASS_NUM; ++i)
			num[i] += spec.num[i];
	}

	public void randomGenerate() {
		int subClass, superClass;
		num = new int[CLASS_NUM];
		for (int i = 0; i < CLASS_NUM; ++i) { 
			
			switch (i) {
				case CS_C_UNIV:
					break;
		        case CS_C_DEPT:
		        	break;
		        case CS_C_FULLPROF:
		        	num[i] = _getRandomFromRange(FULLPROF_MIN, FULLPROF_MAX);
		        	break;
		        case CS_C_ASSOPROF:
		        	num[i] = _getRandomFromRange(ASSOPROF_MIN, ASSOPROF_MAX);
		        	break;
		        case CS_C_ASSTPROF:
		        	num[i] = _getRandomFromRange(ASSTPROF_MIN, ASSTPROF_MAX);
		        	break;
		        case CS_C_LECTURER:
		        	num[i] = _getRandomFromRange(LEC_MIN, LEC_MAX);
		        	break;
		        case CS_C_UNDERSTUD:
		        	num[i] = _getRandomFromRange(R_UNDERSTUD_FACULTY_MIN *
		        			num[CS_C_FACULTY], R_UNDERSTUD_FACULTY_MAX * num[CS_C_FACULTY]);
		        	break;
		        case CS_C_GRADSTUD:
		        	num[i] = _getRandomFromRange(R_GRADSTUD_FACULTY_MIN *
		        			num[CS_C_FACULTY], R_GRADSTUD_FACULTY_MAX * num[CS_C_FACULTY]);
		        	break;
		        case CS_C_COURSE:
		        	num[i] = _getRandomFromRange(FACULTY_COURSE_MIN, FACULTY_COURSE_MAX);
		        	break;
		        case CS_C_GRADCOURSE:
		        	num[i] = _getRandomFromRange(FACULTY_GRADCOURSE_MIN, FACULTY_GRADCOURSE_MAX);
		        	break;
		        case CS_C_RESEARCHGROUP:
		        	num[i] = _getRandomFromRange(RESEARCHGROUP_MIN, RESEARCHGROUP_MAX);
		        	break;
		        default:
		        	num[i] = CLASS_INFO[i][INDEX_NUM];
		      }
		      subClass = i;
		      while ( (superClass = CLASS_INFO[subClass][INDEX_SUPER]) != CS_C_NULL) {
		        num[superClass] += num[i];
		        subClass = superClass;
		      }
		}
	}
	
  }

}
