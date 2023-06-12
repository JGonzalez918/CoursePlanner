package Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*
 * The rules for the input are as follows:
 * Each course input will be included in one line
 * They will follow the following syntax <CourseID>,<CourseName>,<Units>,<CourseIDList>
 * where <CourseIDList> can take the form <CourseID> | <CourseID>, <CourseIDList> | <EmptyList>
 */
public class Parser
{
	private Scanner inputFile;
	
	private ArrayList<Course> courseList;
	
	private HashMap<String,Integer> idToVertex;
	
	private ArrayList<ArrayList<String>> rawPrerequisites;
	
	private static final String COMMA_SEPERATOR = ",";
	
	private static final int ID_INDEX = 0;
	
	private static final int NAME_INDEX = 1;
	
	private static final int UNITS_INDEX = 2;
	
	private static final int PREREQ_LIST_INDEX = 3;
	
	public Parser(String filePath) throws FileNotFoundException
	{
		inputFile = new Scanner(new File(filePath));
		
		courseList = new ArrayList<>();
		
		idToVertex = new HashMap<>();
		
		rawPrerequisites = new ArrayList<>();
		
		parseCourses();
	}
	
	
	public void parseCourses() 
	{
		while(inputFile.hasNextLine())
		{
			String currLine = inputFile.nextLine();
			if(currLine.isBlank() == false) 
			{
				parseCourse(currLine);
			}
		}
	}
	
	private void parseCourse(String inputLine) 
	{	
		String[] courseInfo = inputLine.split(COMMA_SEPERATOR);
		String courseId = removeIllegalChars(courseInfo[ID_INDEX]);
		String courseName = courseInfo[NAME_INDEX].strip();
		int unitWorth = (int)Double.parseDouble(courseInfo[UNITS_INDEX].strip());
		
		ArrayList<String> prereqList = new ArrayList<>();
		for(int i = PREREQ_LIST_INDEX; i < courseInfo.length; i++) 
		{
			prereqList.add(removeIllegalChars(courseInfo[i]));
		}
		
		courseList.add(new Course(courseId, courseName, unitWorth));
		idToVertex.put(courseList.get(courseList.size() - 1).classId, courseList.size() - 1);
		rawPrerequisites.add(prereqList);
	}
	
	private String removeIllegalChars(String string) 
	{
		StringBuilder s = new StringBuilder();
		for(char c : string.toCharArray()) 
		{
			if(Character.isAlphabetic(c)) 
			{
				s.append(Character.toUpperCase(c));
			}
			else if(Character.isDigit(c)) 
			{
				s.append(c);
			}
		}
		return s.toString();
	}


	public ArrayList<Course> getCourseList()
	{
		return courseList;
	}


	public HashMap<String, Integer> getIdToVertex()
	{
		return idToVertex;
	}


	public ArrayList<ArrayList<String>> getRawPrerequisites()
	{
		return rawPrerequisites;
	}
	
	 
	
}
