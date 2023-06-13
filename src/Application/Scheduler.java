package Application;

import java.util.ArrayList;
import java.util.HashMap;

import Exceptions.IllegalStructure;
import GraphFiles.AdjList;
import GraphFiles.Edge;

public class Scheduler
{
	ArrayList<Course> courseList;
	
	AdjList classStructure;
	
	AdjList prereqGraph;
	
	ArrayList<Integer> readyCourses;
	
	ArrayList<Integer> readySublist;
	
	private static final int PREREQUISITE_TO_THIS_VERTEX = 1;
	
	private static final int  PREREQUISITE_FOR_ANOTHER_VERTEX= 2;
	
	public Scheduler(Parser parsedFile) 
	{
		this.courseList = parsedFile.getCourseList();
		classStructure = new AdjList(courseList.size());
		prereqGraph = new AdjList(courseList.size());
		buildGraph(parsedFile.getIdToVertex(), parsedFile.getRawPrerequisites());
		checkIfCycle();
	}
	
	private void buildGraph(HashMap<String, Integer> idToVertex, ArrayList<ArrayList<String>> rawPrerequisites) 
	{
		for(int i = 0; i < rawPrerequisites.size(); i++) 
		{
			ArrayList<String> prerequisitesForVertex = rawPrerequisites.get(i);
			for(String s : prerequisitesForVertex) 
			{
				Integer prereqVertex = idToVertex.get(s);
				if(prereqVertex == null)
				{
					throw new RuntimeException();
				}
				classStructure.addEdge(i, prereqVertex, PREREQUISITE_TO_THIS_VERTEX);
				classStructure.addEdge(prereqVertex, i, PREREQUISITE_FOR_ANOTHER_VERTEX);
				prereqGraph.addEdge(i, prereqVertex, PREREQUISITE_TO_THIS_VERTEX);
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
				System.out.println("Cycle is:" + vertexesInCycle);
				throw new IllegalStructure();
			}
		}
	}
	
	private boolean containsCycleDFS(int vertex, int[] visited, ArrayList<Integer> vertexesInCycle) 
	{
		visited[vertex] = IN_STACK;
		Edge currEdge = classStructure.getNeighborList(vertex).next;
		while(currEdge != null) 
		{
			if(currEdge.weight == PREREQUISITE_FOR_ANOTHER_VERTEX) 
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
			}
			currEdge = currEdge.next;
		}
		visited[vertex] = VISITED;
		return false;
	}
}
