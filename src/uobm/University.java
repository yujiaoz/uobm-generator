package uobm;

public class University {

	boolean hasWomanCollege = false;
	int collegeNum, departmentNum;
	Department[] depts;
	
	public University() {
		collegeNum = Lib.getRandomFromRange(Class.COLL_MIN, Class.COLL_MAX);
		hasWomanCollege = Lib.getRandomFromRange(0, Class.R_WOMAN_COLLEGE) == 0;
		departmentNum = Lib.getRandomFromRange(Class.DEPT_MIN, Class.DEPT_MAX);
		
		depts = new Department[departmentNum];
		for (int i = 0; i < departmentNum; ++i)
			depts[i] = new Department();
	}
	
}
