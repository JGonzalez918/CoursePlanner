package Application;

public class Course
{
	public static final int PREREQ_NOT_COMPLETED = Integer.MAX_VALUE;

	public static final int COURSE_NOT_TAKEN = -1;

	public String classId;
	
	public String courseName;
	
	public boolean hasConcurrentPrereqs;
	
	public boolean addConcurrently;
	
	public int unitWorth;
	
	public int semesterPrereqCompleted;
	
	public int semesterClassCompleted;

	public Course(String classId, String courseName, int unitWorth)
	{
		this.classId = classId;
		this.courseName = courseName;
		this.unitWorth = unitWorth;
		this.hasConcurrentPrereqs = false;
		this.addConcurrently = false;
		this.semesterPrereqCompleted = PREREQ_NOT_COMPLETED;
		this.semesterClassCompleted = COURSE_NOT_TAKEN;
	}
	
	public String toString() 
	{
		return classId + " " + courseName + " " + unitWorth + " units ";
	}
	
	
}
