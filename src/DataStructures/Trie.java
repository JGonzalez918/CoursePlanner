package DataStructures;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

public class Trie
{
	private Node root;
	
	public Logger trie;
	
	private static final Level LogLevel = Level.INFO;
	
	public Trie() 
	{
		root = new Node();
		makeLogger();
		trie.log(Level.INFO, "Trie Created");
	}

	private void makeLogger() 
	{
		trie = Logger.getLogger(Trie.class.getName());
		try 
		{
			Handler handler = new FileHandler("AppLogger.txt", false);
			handler.setFormatter(new SimpleFormatter());
			trie.addHandler(handler);
			trie.setLevel(LogLevel);
		}
		catch(IOException e) 
		{
			trie.log(Level.SEVERE,"Logger could not be made");
		}
		trie.setUseParentHandlers(false);
		trie.log(Level.INFO,"Logger made");
	}
	
	//Assume that word only contains letters and digits
	public boolean addString(String word, int vertex) 
	{
		trie.log(Level.INFO, "Adding " + word  + " to trie. Note that it is assumed that word consists of only letters and digits.");
		Node currNode = root;
		for(int i = 0; i < word.length(); i++) 
		{
			int index = charToIndex(word.charAt(i));
			if(index == -1) continue;
			if(currNode.children[index] == null) {
				currNode.children[index] = new Node();
			}
			currNode = currNode.children[index];
		}
		if(currNode.isWord()) 
		{
			trie.log(Level.INFO, word +  " was already in the trie");
			return false;
		}
		trie.log(Level.INFO, word + " was not in the trie already");
		currNode.vertex = vertex;
		return true;
	}
	
	public int find(String str) 
	{
		int i = 0;
		Node curr = root;
		while(i < str.length() && curr != null)
		{
			curr = nextNode(curr, str, i);
			i++;
		}
		
		if(curr == null) 
		{
			return -1;
		}
		else 
		{
			return curr.vertex;
		}
	}
	
	public int charToIndex(char c) 
	{
		int index = -1;
		if(Character.isDigit(c)) 
		{
			index = c - '0' + Node.DIGIT_START;
		}
		else if(Character.isUpperCase(c)) 
		{
			index = c - 'A';
		}
		else if(Character.isLowerCase(c)) 
		{
			index = c - 'a';
		}
		if(index == -1 && Character.isWhitespace(c) == false) 
		{
			trie.log(Level.SEVERE, "Character not in alphabet passed to trie");
		}
		return index;
	}
	
	private static final int MAX_MISTAKE_NODES = 200;
	private static final int MAX_MATCHING = 10;
	public ArrayList<Integer> getNMatches(String prefix)
	{
		trie.log(Level.INFO, "Getting n matches for prefix " + prefix);
		return getNMatchesRec(prefix,root,new int[] {MAX_MISTAKE_NODES},0, new ArrayList<>());
	}
	
	public ArrayList<Integer> getNMatchesRec(String prefix, Node root, int[] mistakeNodesLeft,int index, ArrayList<Integer> matchList) 
	{
		while(index < prefix.length() && nextNode(root, prefix, index) != null) 
		{
			root = nextNode(root, prefix, index);
			index++;
		}
		if(index == prefix.length()) 
		{
			fillMatchList(root,matchList);
		}
		else if(mistakeNodesLeft[0] > 0)
		{
			for(int i = 0; i < root.children.length && matchList.size() < MAX_MATCHING; i++) 
			{
				if(root.children[i] != null) 
				{
					mistakeNodesLeft[0]--;
					getNMatchesRec(prefix, root, mistakeNodesLeft, index, matchList);
				}
			}
		}
		return matchList;
	}
	
	private void fillMatchList(Node root, ArrayList<Integer> matchList) 
	{
		if(root.isWord()) 
		{
			matchList.add(root.vertex);
		}
		for(int i = 0; i < root.children.length && matchList.size() < MAX_MATCHING; i++) 
		{
			if(root.children[i] != null) 
			{
				fillMatchList(root.children[i], matchList);
			}
		}
	}
	
	public Node nextNode(Node currNode, String prefix, int currIndex) 
	{
		int trieIndex = charToIndex(prefix.charAt(currIndex));
		if(trieIndex == -1) 
		{
			return currNode;
		}
		else //the node in the array could be null or an actual node
		{
			return currNode.children[trieIndex];
		}
	}
	
	public int removeWord(String word) 
	{
		Node curr = root;
		for(int i = 0; i < word.length(); i++) 
		{
			curr = nextNode(curr, word, i);
		}
		int save = curr.vertex;
		curr.vertex = Node.NOT_A_WORD;
		return save; 
	}
	
	
}
