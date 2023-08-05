package DataStructures;

public class Node
{
	public Node[] children;
	public int vertex;
	public final static int NOT_A_WORD = -1;
	public final static int DIGIT_START = 26;
	
	//26 for alphabet 10 for digits
	private final static int ALPHABET_SIZE = 26 + 10;
	
	public Node() 
	{
		children = new Node[ALPHABET_SIZE];
		vertex = NOT_A_WORD;
	}
	
	public boolean isWord() 
	{
		return vertex != NOT_A_WORD;
	}
}
