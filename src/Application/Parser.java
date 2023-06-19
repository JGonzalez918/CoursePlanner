package Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import Exceptions.DuplicateCourse;
import Exceptions.MissingInformation;

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
	
	private String startTerm;
	
	private int startYear;
	
	private static final String COMMA_SEPERATOR = ",";
	
	private static final int ID_INDEX = 0;
	
	private static final int NAME_INDEX = 1;
	
	private static final int UNITS_INDEX = 2;
	
	private static final int PREREQ_LIST_INDEX = 3;
	
	public Parser(Scanner openedFile)
	{
		inputFile = openedFile;
		
		courseList = new ArrayList<>();
		
		idToVertex = new HashMap<>();
		
		rawPrerequisites = new ArrayList<>();
		
		parseFile();
	}
	
	
	public void parseFile() 
	{
		startTerm = removeIllegalChars(inputFile.next());
		startYear = Integer.parseInt(removeIllegalChars(inputFile.nextLine()));
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
		if(courseInfo.length < 3) 
		{
			throw new MissingInformation(inputLine + 
					"\nThe given information on the above line does not meet the minimum length for a course"
				  + "\nAn input line must consist of the folloing information <Course_ID>,<Course_Name>,<Unit_Worth>[,<Course_ID_List>]"
				  + "\nWhere the <Course_ID_List> can be empty or a comma seperated list of course ids");
		}
		String courseId = removeIllegalChars(courseInfo[ID_INDEX]);
		String courseName = courseInfo[NAME_INDEX].strip();
		int unitWorth = (int)Double.parseDouble(courseInfo[UNITS_INDEX].strip());
		
		ArrayList<String> prereqList = new ArrayList<>();
		for(int i = PREREQ_LIST_INDEX; i < courseInfo.length; i++) 
		{
			prereqList.add(removeIllegalChars(courseInfo[i]));
		}
		
		courseList.add(new Course(courseId, courseName, unitWorth));
		if(idToVertex.put(courseId, courseList.size() - 1) != null) 
		{
			throw new DuplicateCourse("The course id " + courseId + " has been reused. Please redefine the course id.");
		}
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


	public String getStartTerm()
	{
		return startTerm;
	}


	public int getStartYear()
	{
		return startYear;
	}
	
	
	 
	
}
