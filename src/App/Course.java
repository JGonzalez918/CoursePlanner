package App;

public class Course
{
	public static final int PREREQ_NOT_COMPLETED = Integer.MAX_VALUE;

	public static final int COURSE_NOT_TAKEN = -1;
	
	public String courseName;
	
	public boolean hasConcurrentPrereqs;
		
	public int unitWorth;
	
	public int semesterPrereqCompleted;
	
	public int semesterClassCompleted;

	public Course(String classId, String courseName, int unitWorth)
	{
		this.courseName = courseName;
		this.unitWorth = unitWorth;
		this.hasConcurrentPrereqs = false;
		this.semesterPrereqCompleted = PREREQ_NOT_COMPLETED;
		this.semesterClassCompleted = COURSE_NOT_TAKEN;
	}
	
	public String toString() 
	{
		return courseName + " " + unitWorth + " units ";
	}
	
	
}
