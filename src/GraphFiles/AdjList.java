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
		
	}
}
