package Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main
{
	static Scanner kb = new Scanner(System.in);
	
	static Scheduler scheduler;
	
	static Parser parsedFile;
	
	static Scanner inputFile;
	
	static ArrayList<Action> userActions = new ArrayList<>();

	public static void main(String[] args) throws FileNotFoundException, NoSuchFieldException, SecurityException
	{
		addUserActions();
		getInputFile();
		processFile();
		while(scheduler.donePlanning() == false) 
		{
			promptUserForAction();
		}
		
	}
	

	public static void getInputFile() 
	{
		String fileName = "";
		System.out.print("Enter the the input file: ");
		do 
		{
			fileName = kb.nextLine();
			try
			{
				inputFile = new Scanner(new File(fileName));
			} catch (FileNotFoundException e)
			{
				System.out.print("The entered file cannot be found. Please enter again:");
			}
		}while(inputFile == null);
	}
	
	private static void processFile()
	{
		try 
		{
			parsedFile = new Parser(inputFile);
			scheduler = new Scheduler(parsedFile);
		}
		catch(Exception e) 
		{
			System.out.println(e.getMessage());
			System.out.println("Please fix the error and reinput the file.");
			System.exit(0);
		}
	}
	
	public static void promptUserForAction() throws NoSuchFieldException, SecurityException 
	{
		for(int i = 0; i < userActions.size(); i++) 
		{
			System.out.println((i+1) + ") " + userActions.get(i).description);
		}
		System.out.print("Enter the number corresponding to the action you want to take:");
		int actionIndex = -1;
		do 
		{
			actionIndex = getNumber();
		}while(actionIndex < 1 || actionIndex > userActions.size());
		userActions.get(actionIndex - 1).doAction();
	}
	
	public static void addUserActions() 
	{
		userActions.add(new Action("Print the currently planned course schedule")
		{
			public void doAction()
			{
				System.out.println(scheduler.getPlannedSchedule());
			}
		});
	}
	
	public static int getNumber() 
	{
		int number = 0;
		boolean numberPicked = false;
		while(numberPicked == false) 
		{
			try
			{
				number = kb.nextInt();
				numberPicked = true;
			} 
			catch (InputMismatchException e)
			{
				System.out.print("The string entered was not a number. Please enter again: ");
			}

		}
		return number;
	}
}
