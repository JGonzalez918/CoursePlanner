package Exceptions;

public class IllegalStructure extends RuntimeException
{
	public IllegalStructure(String cycleError) 
	{
		super(cycleError);
	}
}
