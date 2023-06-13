package Exceptions;

public class IllegalStructure extends RuntimeException
{
	public IllegalStructure() 
	{
		super("The input file that list the structure of the courses to be taken contains a cycle."
				+ "\nTwo or more classes list require each other as a prerequisite which means that those class cannot be taken.");
	}
}
