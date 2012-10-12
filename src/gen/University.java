package gen;

public class University implements Organization{

	boolean m_hasWomanCollege = false;
	
	int m_collegeNum; // exclude woman college
	int m_deptNum;
	Department[] m_depts;
	College[] m_colleges;
	
	/** data file writer */
	private Writer m_writer;
	Generator m_gen;
	
	int m_index;
	String m_filename;
	
	public University(Generator gen, int index) {
		m_gen = gen;
		m_writer = new OwlWriter(gen.ontology);
		m_index = index;
//		m_filename = System.getProperty("user.dir") + System.getProperty("file.separator") + "univ" + index;
		m_filename = "/users/yzhou/workspace/OWLim/preload_generated_uobm/univ" + index;
		
		m_hasWomanCollege = Lib.getRandomFromRange(0, Class.R_WOMAN_COLLEGE) == 0;
		m_collegeNum = Lib.getRandomFromRange(Class.COLL_MIN, Class.COLL_MAX);
		if (m_hasWomanCollege) 
			++m_collegeNum;
		
		m_deptNum = Lib.getRandomFromRange(Class.DEPT_MIN, Class.DEPT_MAX);
		m_depts = new Department[m_deptNum];
		m_colleges = new College[m_collegeNum];
		
		
		for (int i = 0; i < m_deptNum; ++i) {
			m_depts[i] = new Department(this, i, m_filename);
		}
		
		for (int i = 0; i < m_collegeNum; ++i)
			if (m_hasWomanCollege && i == 0)
				m_colleges[i] = new College(m_writer, this, i, true);
			else 
				m_colleges[i] = new College(m_writer, this, i, false);
		
		m_filename +=  ".owl";
	}
	
	@Override
	public void generateFaculty() {
		m_writer.start();
		m_writer.startFile(m_filename);
		
		m_writer.startSection(Class.INDEX_UNIV, Class.getUnivID(m_index));
		m_writer.addProperty(Property.INDEX_NAME, Class.getName(Class.INDEX_UNIV, m_index), false);
		m_writer.endSection(Class.INDEX_UNIV);
		
		for (int i = 0; i < m_collegeNum; ++i)
			m_colleges[i].generateFaculty();
		
		for (int i = 0; i < m_deptNum; ++i)
			m_depts[i].generateFaculty();
		
	}
	
	public void generateStudents() {
		for (int i = 0; i < m_collegeNum; ++i)
			m_colleges[i].generateStudents();
		
		for (int i = 0; i < m_deptNum; ++i)
			m_depts[i].generateStudents();
		
		m_writer.endFile();
		m_writer.end();
	}

	@Override
	public Organization getRandomSubOrgan() {
		int index = Lib.getRandomFromRange(-1, m_deptNum - 1);
		if (m_hasWomanCollege && index < 0) return m_colleges[0];
		
		if (index >= 0)
			return m_depts[index];
		else
			return m_depts[Lib.getRandomFromRange(0, m_deptNum - 1)];
	}

	@Override
	public String getRandomPeople() {
		return getRandomSubOrgan().getRandomPeople();
	}

	@Override
	public int getUniversityIndex() {
		return m_index;
	}

	@Override
	public int getIndex() {
		return m_index;
	}

	@Override
	public String getRandomCourse() {
		Department dept = m_depts[Lib.getRandomFromRange(0, m_deptNum - 1)];
		return dept.getRandomCourse();
	}

	@Override
	public String getRandomGradCourse() {
		Department dept = m_depts[Lib.getRandomFromRange(0, m_deptNum - 1)];
		return dept.getRandomGradCourse();
	}

	public int getRandomCollege() {
		if (m_hasWomanCollege)
			return Lib.getRandomFromRange(1, m_collegeNum - 1);
		else return Lib.getRandomFromRange(0, m_collegeNum - 1); 
	}

	@Override
	public String getRandomFaculty() {
		return m_depts[Lib.getRandomFromRange(0, m_deptNum - 1)].getRandomFaculty();
	}

}
