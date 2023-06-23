package Application;

/*
 * The purpose of the action class is to add the actions that users can do in the main function.
 * How this will work is there will be an ArrayList of anonymous Action classes. In the main class I'll
 * implement them with a string that describes the action and one function that will actually do that specific action.
 * The class fields are all static so they will be visible to the anonymous action class. In the main application
 * loop a for loop will go through each individual action and print the description and then do the action that 
 * the user wants. 
 * 
 * I chose to do it this way because any other way would involve using switch statements or a bunch of if else statements
 * and if I chose to change the order of actions later on I would have to alter the structure of the switch cases.
 */
public abstract class Action
{
	String description;
	
	public Action(String description) 
	{
		this.description = description;
	}
	
	public abstract void doAction();
}
