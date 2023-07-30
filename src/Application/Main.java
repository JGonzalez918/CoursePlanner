package Application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
	static Scanner kb = new Scanner(System.in);
	
	static Scheduler scheduler;
	
	static Parser parsedFile;
	
	static Scanner inputFile;
	
	static ArrayList<Action> userActions = new ArrayList<>();

	public static void main(String[] args) throws FileNotFoundException, NoSuchFieldException, SecurityException
	{
		launch(args);
	}
	
    public void start(Stage primaryStage) throws Exception{
    	primaryStage.show();
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
		catch(InputMismatchException e) 
		{
			System.out.println(e.getMessage());
			System.out.println("Please fix the error and reinput the file.");
			System.exit(0);
		}
	}
	
	public static void promptUserForAction()
	{
		for(int i = 0; i < userActions.size(); i++) 
		{
			System.out.println((i+1) + ") " + userActions.get(i).description);
		}
		System.out.println("Current Semester: " + scheduler.convertSemesterToTerm(scheduler.getCurrentSemester())
						+  " Semester Number: " + scheduler.getCurrentSemester());
		System.out.print("Enter the number corresponding to the action you want to take: ");
		int actionIndex = getNumber(1,userActions.size());
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
		
		//TODO: Have add course action take a list of numbers rather than only one
		userActions.add(new Action("Add a courses to the current semester") 
		{
			@Override
			public void doAction()
			{
				ArrayList<Integer> availableCourses = scheduler.getAvailableCourses();
				System.out.println(scheduler.convertCourseListToStr("\nCourses available for " 
				+ scheduler.convertSemesterToTerm(scheduler.getCurrentSemester()) 
				+ " Semester #" + scheduler.getCurrentSemester(),availableCourses));
				System.out.print("Enter a list of indexes that correspond to the classes you want to add to this semester: ");
				ArrayList<Integer> listIndexes = getNumberList(kb.nextLine());
				
				Collections.sort(listIndexes,Collections.reverseOrder());
				for(int i : listIndexes)
				{
					if(i > 0 && i <= availableCourses.size())
					{
						scheduler.addClassToSemester(availableCourses.get(i - 1));
					}
				}
			}
		});
		
		userActions.add(new Action("Remove a course from the schedule") 
		{
			@Override
			public void doAction()
			{
				System.out.println(scheduler.getPlannedSchedule());
				System.out.println("Enter the id of the course you want to remove: ");
				String id = Parser.removeIllegalChars(kb.nextLine());
				Integer vertex = parsedFile.convertedIdToVertex(id);
				if(vertex == null) {
					System.out.println("Entered course id does not exist double check spelling.");
					return;
				}
				else if(scheduler.getCourseList().get(vertex).semesterClassCompleted == Course.COURSE_NOT_TAKEN) 
				{
					System.out.println("This course has not been taken yet");
					return;
				}
				ArrayList<Integer> removedCourses = scheduler.removeCourse(vertex);
				System.out.println(scheduler.convertCourseListToStr("The removed courses are listed below", removedCourses));
			}
		});
		
		userActions.add(new Action("Show the prerequisites required for a certain course") 
		{
			@Override
			public void doAction()
			{
				System.out.println(scheduler.convertCourseListToStr("All Courses are listed below in no particular order", scheduler.getSortedCourseList()));
				System.out.print("Enter 1 if you want to enter a list index or 2 if you want to enter a course id: ");
				int inputType = getNumber(1, 2);
				Integer vertex = -1;
				ArrayList<Integer> prereqs = null;
				if(inputType == 1) {
					System.out.print("Enter the list index: ");
					int index = getNumber(1, scheduler.getSortedCourseList().size());
					vertex = scheduler.getSortedCourseList().get(index - 1);
				}else {
					System.out.print("Enter the course id: ");
					vertex = parsedFile.convertedIdToVertex(Parser.removeIllegalChars(kb.nextLine()));
					if(vertex == null)
					{
						System.out.println("The entered course id does not exists");
						return;
					}
				}
				prereqs = scheduler.getPrereqList(vertex);
				if(prereqs.isEmpty()) 
				{
					System.out.println("This course has no prerequisites");
				}
				else 
				{
					System.out.println(scheduler.convertCourseListToStr("The prerequisites to " + scheduler.getCourseList().get(vertex).courseName + "  are: ", prereqs));
				}
			}
		});
		
		userActions.add(new Action("Change semester") 
		{
			@Override
			public void doAction()
			{
				System.out.print("Enter the semester to change into (current semester is: " + scheduler.getCurrentSemester() + "): ");
				scheduler.setCurrentSemester(getNumber(1, Integer.MAX_VALUE));
				
			}
		});
		
	}
	
	public static ArrayList<Integer> getNumberList(String numberList)
	{
		ArrayList<Integer> digits = new ArrayList<>();
		int i = 0;
		while(i < numberList.length()) 
		{
			while(i < numberList.length() && Character.isDigit(numberList.charAt(i)) == false) 
			{
				i++;
			}
			if(i == numberList.length()) {break;}
			int number = 0;
			while(i < numberList.length() && Character.isDigit(numberList.charAt(i))) {
				int digit = numberList.charAt(i) - '0';
				if(numberIsInvalid(number,digit)) {
					number = -1;
					break;
				}
				number = number * 10 + digit;
				i++;
			}
			digits.add(number);
		}
		return digits;
	}
	
	public static boolean numberIsInvalid(int number, int digit) 
	{
		return number > Integer.MAX_VALUE/10 || number == Integer.MAX_VALUE/10 && digit > 7;
	}
	
	public static int getNumber(int min, int max) 
	{
		int number = 0;
		boolean numberPicked = false;
		while(numberPicked == false) 
		{
			try
			{
				number = kb.nextInt();
				kb.nextLine();
				if(number >= min && number <= max) 
				{
					numberPicked = true;
				}
				else 
				{
					System.out.println("The number entered is out of the valid range of " + min + " minimum "
							+ "and " + max + " maximum");
					System.out.print("Please enter again: ");
				}
			} 
			catch (InputMismatchException e)
			{
				System.out.print("The string entered was not a number. Please enter again: ");
				kb.nextLine();
			}

		}
		return number;
	}
}
