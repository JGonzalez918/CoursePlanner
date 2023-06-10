package GraphFiles;

public class AdjList
{
	public Edge[] graph;
	
	public int nodeCount;
	
	public AdjList(int size) 
	{
		nodeCount = size;
		graph = new Edge[size];
		
		for(int i = 0; i < graph.length; i++) 
		{
			//head of each linked list
			graph[i] = new Edge(-1,-1,null);
		}
	}
	
	/*
	 * Add edge from vertex u to vertex v with weight "weight"
	 * I am inserting the edges in sorted order of v in order to avoid 
	 * duplicate edges that may be added by mistake
	 */
	public void addEdge(int u, int v, int weight) 
	{
		if(invalidVertex(u) || invalidVertex(v)) 
		{
			throw new IllegalArgumentException("Vertex passed into function is out of valid range");
		}
		Edge currEdge = graph[u];
		while(currEdge.next != null && currEdge.next.vertex < v) 
		{
			currEdge = currEdge.next;
		}
		if(currEdge.next != null && currEdge.next.vertex == v) 
		{
			currEdge.next.weight = weight;
		}
		else 
		{
			currEdge.next = new Edge(v,weight,currEdge.next);
		}
	}
	
	public void removeEdge(int u, int v) 
	{
		if(invalidVertex(u) || invalidVertex(v)) 
		{
			throw new IllegalArgumentException("Vertex passed into function is out of valid range");
		}
		Edge currEdge = graph[u];
		while(currEdge.next != null && currEdge.next.vertex < v) 
		{
			currEdge = currEdge.next;
		}
	}
	
	/*
	 * Vertexes are labeled from 0 to (n - 1) where n is the number of nodes in the graph
	 */
	private boolean invalidVertex(int vertex) 
	{
		if(vertex < 0 || vertex >= graph.length) 
		{
			return true;
		}
		return false;
	}
}
