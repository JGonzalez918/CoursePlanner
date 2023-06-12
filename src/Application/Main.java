package Application;

import java.io.FileNotFoundException;

import GraphFiles.AdjList;

public class Main
{
	public static void main(String[] args)throws FileNotFoundException
	{
		Parser p = new Parser("TestCases//CSUDHTC");
		System.out.println(p.getCourseList());
		System.out.println(p.getRawPrerequisites());
	}
}
