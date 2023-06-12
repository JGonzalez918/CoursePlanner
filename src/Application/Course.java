package Application;

public class Course
{
	public String classId;
	
	public String courseName;
	
	public int unitWorth;
	
	public int semesterPrereqCompleted;
	
	public int semesterClassCompleted;

	public Course(String classId, String courseName, int unitWorth)
	{
		this.classId = classId;
		this.courseName = courseName;
		this.unitWorth = unitWorth;
		this.semesterPrereqCompleted = -1;
		this.semesterClassCompleted = -1;
	}
	
	public String toString() 
	{
		return classId + " " + courseName + " " + unitWorth + " units ";
	}
	
	
}
