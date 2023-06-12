package Application;

import java.util.ArrayList;
import java.util.HashMap;

import GraphFiles.AdjList;

public class Scheduler
{
	ArrayList<Course> courseList;
	
	AdjList classStructure;
	
	AdjList prereqGraph;
	
	private static final int PREREQUISITE_TO_VERTEX = 1;
	
	private static final int  PREREQUISITE_FOR_ANOTHER_VERTEX= 2;
	
	public Scheduler(ArrayList<Course> courseList, HashMap<String, Integer> idToVertex, ArrayList<ArrayList<String>> rawPrerequisites) 
	{
		this.courseList = courseList;
		classStructure = new AdjList(courseList.size());
		prereqGraph = new AdjList(courseList.size());
		buildGraph(idToVertex, rawPrerequisites);
	}
	
	private void buildGraph(HashMap<String, Integer> idToVertex, ArrayList<ArrayList<String>> rawPrerequisites) 
	{
		for(int i = 0; i < rawPrerequisites.size(); i++) 
		{
			ArrayList<String> prerequisitesForVertex = rawPrerequisites.get(i);
			for(String s : prerequisitesForVertex) 
			{
				int prereqVertex = idToVertex.get(s);
				classStructure.addEdge(i, prereqVertex, PREREQUISITE_TO_VERTEX);
				classStructure.addEdge(prereqVertex, i, PREREQUISITE_FOR_ANOTHER_VERTEX);
				prereqGraph.addEdge(i, prereqVertex, PREREQUISITE_TO_VERTEX);
			}
		}
	}
}
