package Exceptions;

public class ContainsCycle extends RuntimeException
{
	public ContainsCycle(String s) 
	{
		super(s);
	}
}
