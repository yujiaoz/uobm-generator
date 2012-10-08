package uobm;

public class University {

	boolean m_hasWomanCollege = false;
	int m_collegeNum; // exclude woman college
	int m_departmentNum;
	Department[] m_depts;
	
	/** data file writer */
	private Writer m_writer;
	Generator m_gen;
	
	int m_index;
	String m_id;
	String m_filename;
	
	public University(Generator gen, int index) {
		m_collegeNum = Lib.getRandomFromRange(Class.COLL_MIN, Class.COLL_MAX);
		m_hasWomanCollege = Lib.getRandomFromRange(0, Class.R_WOMAN_COLLEGE) == 0;
		m_departmentNum = Lib.getRandomFromRange(Class.DEPT_MIN, Class.DEPT_MAX);
		m_filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "" + Class.INDEX_DELIMITER;
		m_depts = new Department[m_departmentNum];
		for (int i = 0; i < m_departmentNum; ++i) {
			m_depts[i] = new Department(gen, index, i, m_filename);
		}
		
		m_gen = gen;
		m_filename +=  ".owl";
		m_writer = new OwlWriter(gen.ontology);
		m_index = index;
		m_id = Class.getUnivID(m_index);
	}
	
	public void generate() {
		m_writer.start();
		m_writer.startFile(m_filename);
		
		
		m_writer.startSection(Class.INDEX_UNIV, m_id);
		m_writer.addProperty(Property.INDEX_NAME, Class.getRelativeName(Class.INDEX_UNIV, m_index), false);
		m_writer.endSection(Class.INDEX_UNIV);
		
		generateColleges();
		
		m_writer.endFile();
		m_writer.end();
	}

	private void generateColleges() {
		for (int i = 0; i < m_collegeNum; ++i) {
			m_writer.startSection(Class.INDEX_COLLEGE, Class.getCollegeID(m_index, i));
			m_writer.addProperty(Property.INDEX_SUBORG, m_id, true);
			m_writer.endSection(Class.INDEX_COLLEGE);
		}
		
		if (m_hasWomanCollege) {
			String collegeID = Class.getCollegeID(m_index, m_collegeNum); 
			m_writer.startSection(Class.INDEX_COLLEGE, collegeID);
			m_writer.addProperty(Property.INDEX_SUBORG, m_id, true);
			m_writer.endSection(Class.INDEX_COLLEGE);
			
			int womanNum = Lib.getRandomFromRange(230, 270);
			for (int i = 0; i < womanNum; ++i) {
				m_writer.startSection(Class.INDEX_PERSON, Class.getOtherID(collegeID, Class.INDEX_STUDENT, i));
				m_gen.generateAStudent();
				m_writer.endSection(Class.INDEX_PERSON);
			}
		}
	}
	
}
