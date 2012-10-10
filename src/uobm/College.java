package uobm;

import java.util.LinkedList;

public class College implements Organization{

	Writer m_writer;
	int m_univIndex, m_collegeIndex;
	Generator m_gen;
	boolean m_isWomanCollege;
	int m_womanNum;
	String m_ID;
	
	LinkedList<Department> m_depts;

	public College(Writer writer, University univ, int collegeIndex, boolean isWomanCollege) {
		m_writer = writer;
		m_univIndex = univ.m_index;
		m_collegeIndex = collegeIndex;
		m_gen = univ.m_gen;
		m_isWomanCollege = isWomanCollege;
		m_depts = new LinkedList<Department>();
		m_ID = Class.getCollegeID(m_univIndex, m_collegeIndex);
	}

	@Override
	public void generate() {
		if (!m_isWomanCollege) {
			m_writer.startSection(Class.INDEX_COLLEGE,
					Class.getCollegeID(m_univIndex, m_collegeIndex));
			m_writer.addProperty(Property.INDEX_SUBORG, Class.getUnivID(m_univIndex), true);
			m_writer.endSection(Class.INDEX_COLLEGE);
			return;
		}

		
		m_writer.startSection(Class.INDEX_COLLEGE, m_ID);
		m_writer.addProperty(Property.INDEX_SUBORG, Class.getUnivID(m_univIndex), true);
		m_writer.endSection(Class.INDEX_COLLEGE);

		m_womanNum = Lib.getRandomFromRange(230, 270);
		String womanID;
		for (int i = 0; i < m_womanNum; ++i) {
			m_writer.startSection(Class.INDEX_PERSON, womanID = Class.getOtherID(m_ID, "woman_student", i));
			generateWomanStudent(m_ID, womanID, Class.getName("woman_student", i));
			m_writer.endSection(Class.INDEX_PERSON);
		}
	}

	private void generateWomanStudent(String collegeID, String womanID,
			String name) {
		m_writer.addProperty(Property.INDEX_STUDENT, collegeID, true);
		m_writer.addProperty(Property.INDEX_EMAIL,
				collegeID.replaceAll("http://www.", name + "@"), false);
		m_writer.addProperty(Property.INDEX_FIRSTNAME, name + ".first", false);
		m_writer.addProperty(Property.INDEX_LASTNAME, name + ".last", false);
		m_writer.addProperty(Property.INDEX_TELE, "xxx-xxx-xxxx", false);

		m_gen.addSameHomeTownAttributes(this, m_writer, womanID);
		m_gen.addIsFriendOfAttributes(this, m_writer, womanID);
		m_gen.addLikeAttributes(m_writer);
		m_gen.addFanAttributes(m_writer);

	}

	@Override
	public Organization getRandomSubOrgan() {
		return m_depts.get(Lib.getRandomFromRange(0, m_depts.size() - 1));
	}

	@Override
	public String getRandomPeople() {
		if (m_isWomanCollege)
			return Class.getOtherID(m_ID, "woman_student", Lib.getRandomFromRange(0, m_womanNum - 1));
		else return null;
	}

	@Override
	public int getUniversityIndex() {
		return m_univIndex;
	}

	@Override
	public int getIndex() {
		return m_collegeIndex;
	}

	@Override
	public String getRandomCourse() {
		return null;
	}

	@Override
	public String getRandomGradCourse() {
		return null;
	}

}
