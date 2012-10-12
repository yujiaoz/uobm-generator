package gen;

public interface Organization {
	
	public int getUniversityIndex();
	
	public int getIndex();
	
	void generateFaculty();
	
	void generateStudents();
	
	public Organization getRandomSubOrgan();
	
	public String getRandomPeople();
	
	public String getRandomCourse();
	
	public String getRandomGradCourse();
	
	public String getRandomFaculty();
	
}
