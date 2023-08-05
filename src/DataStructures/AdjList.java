package DataStructures;

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
	
	public Edge getNeighborList(int vertex) 
	{
		if(invalidVertex(vertex)) 
		{
			throw new IllegalArgumentException("Vertex passed into function is out of valid range");
		}
		return graph[vertex];
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
	
	/*
	 * Remove some directed edge from vertex u to vertex v
	 * Will throw an exception is the vertex is invalid or there was no edge there to begin with
	 */
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
		if(currEdge.next == null || currEdge.next.vertex != v) 
		{
			throw new IllegalArgumentException("Edge form vertex " + u + " (u) to " + v + " (v) does not exist");
		}	
		else 
		{
			currEdge.next = currEdge.next.next;
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
	
	public String toString() 
	{
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < graph.length; i++) 
		{
			s.append(i + ": ");
			Edge currEdge = graph[i].next;
			while(currEdge != null && currEdge.next != null) 
			{
				s.append(currEdge.toString());
				s.append("->");
				currEdge = currEdge.next;
			}
			if(currEdge != null) {
				s.append(currEdge.toString());
				s.append("->");
			}
			s.append("X");
			s.append("\n");
		}
		return s.toString();
	}
}
