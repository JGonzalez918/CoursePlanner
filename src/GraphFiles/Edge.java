package GraphFiles;

public class Edge
{
	public int vertex;
	
	public int weight;
	
	public Edge next;
	
	public Edge(int vertex, int weight, Edge next) 
	{
		this.vertex = vertex;
		this.weight = weight;
		this.next = next;
	}
	
	public String toString() 
	{
		return "[" + vertex + "|" + weight + "]";
	}
}
