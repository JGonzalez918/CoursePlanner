package Application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import Exceptions.ContainsCycle;
import Exceptions.UndeclaredCourse;
import GraphFiles.AdjList;
import GraphFiles.Edge;

public class Scheduler
{
	private ArrayList<Course> courseList;
	
	private ArrayList<Integer> sortedCourseList;
	
	private ArrayList<Integer> availableCourses;
	
	private AdjList classStructure;
	
	private AdjList prereqGraph;
	
	private int coursesProcessed;
	
	private static final String[] TERM_NAMES = new String[2];
	
	private int startYear;
	
	private String startTerm;
	
	private int currentSemester;
	
	private Comparator<Integer> courseComparator = new Comparator<Integer>()
	{
		public int compare(Integer o1, Integer o2)
		{
			return courseList.get(o1).semesterClassCompleted - courseList.get(o2).semesterClassCompleted;
		}
	};
	
	private static final int NORMAL_PREREQUISITE = 1;
	
	private static final int CONCURRENT_PREREQUISITE = 2;
	
	private static final int PREREQUISITE_TO_THIS_VERTEX = 1;
	
	private static final int  PREREQUISITE_FOR_ANOTHER_VERTEX= 2;

	private static final int CONCURRENT_PREREQUISITE_TO_THIS_VERTEX = 0;
	
	public Scheduler(Parser parsedFile) 
	{
		this.courseList = parsedFile.getCourseList();
		this.startYear = parsedFile.getStartYear();
		this.startTerm = parsedFile.getStartTerm();
		this.availableCourses = new ArrayList<>();
		
		if(startTerm.equals("FALL")) {
			TERM_NAMES[0] = "Spring";
			TERM_NAMES[1] = "Fall";

		}else {
			TERM_NAMES[0] = "Fall";
			TERM_NAMES[1] = "Spring";
		}
		sortedCourseList = new ArrayList<>();
		for(int i = 0; i < courseList.size(); i++) 
		{
			sortedCourseList.add(i);
		}
		classStructure = new AdjList(courseList.size());
		prereqGraph = new AdjList(courseList.size());
		coursesProcessed = 0;
		
		buildGraph(parsedFile.getIdToVertex(), parsedFile.getRawPrerequisites());
		checkIfCycle();
		markInitialClasses();
		setCurrentSemester(1);
	}
	
	/**
	 * The course scheduler is essentially a list of lists of courses 
	 * There is a list for each semester that the student is in school for.
	 * The list contains the courses that the student chose to take that semester
	 * The purpose of this function is mark the semester the class was taken 
	 * and then remove the prerequisite of this class from other courses
	 * Assume that when this funciton is called it is a valid vertex that cna be taken
	 * @param vertex
	 */
	public void addClassToSemester(int vertex) 
	{
		Course takenCourse = courseList.get(vertex);
		takenCourse.semesterClassCompleted = currentSemester;
		Edge currEdge = classStructure.getNeighborList(vertex).next;
		while(currEdge != null) 
		{
			if(currEdge.weight == PREREQUISITE_FOR_ANOTHER_VERTEX) 
			{
				prereqGraph.removeEdge(currEdge.vertex, vertex);
				if(prereqGraph.getNeighborList(currEdge.vertex).next == null) 
				{
					courseList.get(currEdge.vertex).semesterPrereqCompleted = currentSemester;
				}
			}
			currEdge = currEdge.next;
		}
		coursesProcessed++;
	}
	
	public boolean addClassConcurrently(int vertex)
	{
		if(allPrereqsCompleted(vertex) == false) {
			return false;
		}
		return true;
	}
	
	/*
	 * Function checks to see if all prerequisites that can not be taken concurrently have been completed.
	 * This is check is necessary because a class can be taken concurrently with a subset of its prerequisites if all 
	 * non concurrent prerequisites are completed.
	 */
	private boolean allPrereqsCompleted(int vertex)
	{
		Edge currPrereq = prereqGraph.getNeighborList(vertex).next;
		while(currPrereq != null) 
		{
			if(currPrereq.weight != CONCURRENT_PREREQUISITE) 
			{
				return false;
			}
			currPrereq = currPrereq.next;
		}
		return true;
	}

	public ArrayList<Integer> removeCourse(int vertex)
	{
		ArrayList<Integer> removedCourses = new ArrayList<>();
		Queue<Integer> q = new LinkedList<>();
		boolean[] visited = new boolean[courseList.size()];
		visited[vertex] = true;
		q.add(vertex);
		while(q.isEmpty() == false) 
		{
			int currVertex = q.poll();
			courseList.get(currVertex).semesterClassCompleted = Course.COURSE_NOT_TAKEN;
			removedCourses.add(currVertex);
			coursesProcessed--;
			
			Edge currEdge = classStructure.getNeighborList(currVertex).next;
			while(currEdge != null)
			{
				if(currEdge.weight == PREREQUISITE_FOR_ANOTHER_VERTEX) {
					if(visited[currEdge.vertex] == false && 
						courseList.get(currEdge.vertex).semesterClassCompleted != Course.COURSE_NOT_TAKEN) {
						visited[currEdge.vertex] = true;
						q.add(currEdge.vertex);
					}
					//TODO: have it add back the correct edge weight
					prereqGraph.addEdge(currEdge.vertex, currVertex, PREREQUISITE_TO_THIS_VERTEX);
					courseList.get(currEdge.vertex).semesterPrereqCompleted = Course.PREREQ_NOT_COMPLETED;
				}
				currEdge = currEdge.next;
			}
		}
		setAvailableClasses();
		return removedCourses;
	}
	
	/*
	 * Return whether or not a class can be taken during a certain semester.
	 * It checks if the class prerequisites have been completed, the class has not been taken,
	 * and that the prerequisite have been completed before the current semster
	 */
	private boolean canBeTaken(int vertex) 
	{
		Course course = courseList.get(vertex);
		return currentSemester > course.semesterPrereqCompleted &&
				course.semesterClassCompleted == Course.COURSE_NOT_TAKEN;
	}
	
	private void markInitialClasses() 
	{
		for(int i = 0; i < prereqGraph.nodeCount; i++) 
		{
			if(prereqGraph.getNeighborList(i).next == null) 
			{
				courseList.get(i).semesterPrereqCompleted = 0;
			}
		}
	}
	
	private void buildGraph(HashMap<String, Integer> idToVertex, ArrayList<ArrayList<String>> rawPrerequisites) 
	{
		for(int i = 0; i < rawPrerequisites.size(); i++) 
		{
			ArrayList<String> prerequisitesForVertex = rawPrerequisites.get(i);
			for(String s : prerequisitesForVertex) 
			{
				int prereqWeight = NORMAL_PREREQUISITE;
				int structureWegith = PREREQUISITE_TO_THIS_VERTEX;
				if(s.endsWith(Parser.CONCURRENT_FLAG)) 
				{
					s = s.substring(0,s.length() -  Parser.CONCURRENT_FLAG.length());
					prereqWeight = CONCURRENT_PREREQUISITE;
					structureWegith = CONCURRENT_PREREQUISITE_TO_THIS_VERTEX;
				}
				Integer prereqVertex = idToVertex.get(s);
				if(prereqVertex == null)
				{
					throw new UndeclaredCourse("The given prerequisite " + s +" for class " + courseList.get(i).courseName + " was not declared in the input file");
				}
				classStructure.addEdge(i, prereqVertex, structureWegith);
				classStructure.addEdge(prereqVertex, i, PREREQUISITE_FOR_ANOTHER_VERTEX);
				prereqGraph.addEdge(i, prereqVertex, prereqWeight);
			}
		}
	}
	
	private static int UNVISITED = 0;
	private static int IN_STACK = 1;
	private static int VISITED = 2;
	
	private void checkIfCycle() 
	{
		int[] visited = new int[courseList.size()];
		ArrayList<Integer> vertexesInCycle = new ArrayList<>();
		for(int i = 0; i < courseList.size(); i++) 
		{
			if(visited[i] == UNVISITED && containsCycleDFS(i,visited,vertexesInCycle)) 
			{
				throw new ContainsCycle(buildErrorMessage(vertexesInCycle));
			}
		}
	}
	

	private boolean containsCycleDFS(int vertex, int[] visited, ArrayList<Integer> vertexesInCycle) 
	{
		visited[vertex] = IN_STACK;
		Edge currEdge = prereqGraph.getNeighborList(vertex).next;
		while(currEdge != null) 
		{
			if(visited[currEdge.vertex] == UNVISITED) 
			{
				if(containsCycleDFS(currEdge.vertex,visited,vertexesInCycle)) 
				{
					if(vertexesInCycle.get(0) != vertexesInCycle.get(vertexesInCycle.size() - 1))
					{
						vertexesInCycle.add(vertex);
					}
					return true;
				}
			}
			else if(visited[currEdge.vertex] == IN_STACK) 
			{
				vertexesInCycle.add(currEdge.vertex);
				vertexesInCycle.add(vertex);
				return true;
			}
			currEdge = currEdge.next;
		}
		visited[vertex] = VISITED;
		return false;
	}	
	
	private String buildErrorMessage(ArrayList<Integer> vertexesInCycle)
	{
		StringBuilder s = new StringBuilder();
		String course1 = courseList.get(0).courseName;
		String course2 = courseList.get(1).courseName;
		s.append("Error: There is a cycle in the provided course structure.\n");
		s.append("The course " + course2 + " requires " + course1 + " to be taken before it can be taken."
				+ "\nHowever, " + course1 + " also requires " + course2 + " to be taken before it at some point. "
				+ "\nThus this provided ordering makes taking the two courses to be taken impossible.");
		s.append("\nHere is the cycle:\n");
		for(int i = 1; i < vertexesInCycle.size(); i++) 
		{
			course2 = courseList.get(vertexesInCycle.get(i)).courseName;
			s.append(course2 + " requires " + course1 + "\n");
			course1 = course2;
		}
		return s.toString();
	}

	public ArrayList<Course> getCourseList()
	{
		return courseList;
	}

	public int getCurrentSemester()
	{
		return currentSemester;
	}
	
	//TODO: consider changing data type of semester to double to use the value Double.POSITIVE_INFINITY
	//instead of Integer.MAX_VALUE
	public boolean setCurrentSemester(int currentSemester) 
	{
		if(currentSemester < 0 || currentSemester == Course.PREREQ_NOT_COMPLETED) 
		{
			return false;
		}
		this.currentSemester = currentSemester;
		setAvailableClasses();
		return true;
	}
	
	private void setAvailableClasses() 
	{
		availableCourses.clear();
		for(int i = 0; i < courseList.size(); i++) 
		{
			if(canBeTaken(i)) 
			{
				availableCourses.add(i);
			}
		}
	}
	
	public ArrayList<Integer> getAvailableCourses()
	{
		return availableCourses;
	}
	
	public ArrayList<Integer> getSortedCourseList()
	{
		return sortedCourseList;
	}
	
	//Index is in the range of 1 - n where n is the number of courses in the list
	public int convertIndexToVertex(int index) 
	{
		if(index <= 0 || index >= courseList.size()) {
			return -1;
		}
		int i = 0;
		
		while(i < courseList.size() && index != 0) 
		{
			if(canBeTaken(i)) 
			{
				index--;
			}
			if(index != 0) 
			{
				i++;
			}
		}
		return i == courseList.size() ? -1 : i;
	}
	
	public String convertSemesterToTerm(int semester) 
	{
		return TERM_NAMES[semester % 2] + " " + 
				(semester/2 + startYear);
	}
	
	public String convertCourseListToStr(String header, ArrayList<Integer> subList) 
	{
		StringBuilder s = new StringBuilder();
		s.append(header + "\n");
		for(int i = 0; i < subList.size(); i++)
		{
			Course course = courseList.get(subList.get(i));
			s.append((i + 1) + ") " + course.toString() + "\n");
		} 
		return s.toString();
	}
	
	public String getPlannedSchedule() 
	{
		Collections.sort(sortedCourseList, courseComparator);
		
		int i = 0; 
		while(i < sortedCourseList.size() && courseList.get(sortedCourseList.get(i)).semesterClassCompleted == -1) 
		{
			i++;
		}
		StringBuilder s = new StringBuilder();
		s.append("\nPlanned schedule is listed below:\n");
		while(i < sortedCourseList.size()) 
		{
			int semesterBlock = courseList.get(sortedCourseList.get(i)).semesterClassCompleted;
			s.append("Semester #" + semesterBlock + " " + convertSemesterToTerm(semesterBlock) + "\n");
			int totalUnits = 0;
			int label = 1;
			while(i < sortedCourseList.size() && courseList.get(sortedCourseList.get(i)).semesterClassCompleted == semesterBlock) 
			{
				Course course = courseList.get(sortedCourseList.get(i));
				s.append(label + ") " + course.toString() + "\n");
				totalUnits += course.unitWorth;
				i++;
				label++;
			}
			s.append("Total Units This Semester: " + totalUnits + "\n");
		}
		return s.toString();
	}
	
	public boolean donePlanning() 
	{
		return coursesProcessed == courseList.size();
	}

	public ArrayList<Integer> getPrereqList(int vertex)
	{
		ArrayList<Integer> prereqs = new ArrayList<>();
		Queue<Integer> q = new LinkedList<>();
		boolean[] visited = new boolean[courseList.size()];
		q.add(vertex);
		while(q.isEmpty() == false) 
		{
			int currVertex = q.poll();
			prereqs.add(currVertex);
			Edge curr = classStructure.getNeighborList(currVertex).next;
			while(curr != null) 
			{
				if(curr.weight == PREREQUISITE_TO_THIS_VERTEX && visited[curr.vertex] == false) 
				{
					q.add(curr.vertex);
					visited[curr.vertex] = true;
				}
				curr = curr.next;
			}
			
		}
		prereqs.remove(0);
		return prereqs;
	}
}
