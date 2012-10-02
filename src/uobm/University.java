package uobm;

public class University {

	boolean m_hasWomanCollege = false;
	int m_collegeNum, m_departmentNum;
	Department[] m_depts;
	
	/** data file writer */
	private Writer m_writer;	/** generate DAML+OIL data (instead of OWL) */
	
	int m_index;
	String m_filename;

	
	public University(Generator gen, int index) {
		m_collegeNum = Lib.getRandomFromRange(Class.COLL_MIN, Class.COLL_MAX);
		m_hasWomanCollege = Lib.getRandomFromRange(0, Class.R_WOMAN_COLLEGE) == 0;
		m_departmentNum = Lib.getRandomFromRange(Class.DEPT_MIN, Class.DEPT_MAX);
		m_filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "" + Generator.INDEX_DELIMITER;
		m_depts = new Department[m_departmentNum];
		for (int i = 0; i < m_departmentNum; ++i)
			m_depts[i] = new Department(gen, index, i, m_filename);
		
		m_filename +=  ".owl";
		m_writer = new OwlWriter(gen.ontology);
		m_index = index;
	}
	
	public void output() {
		m_writer.start();
		m_writer.startFile(m_filename);
		m_writer.startSection(Class.UNIV, m_index);
		
		m_writer.endFile();
		m_writer.end();
	}
	
}
