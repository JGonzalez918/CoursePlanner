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

	public static void main(String[] args) throws FileNotFoundException, NoSuchFieldException, SecurityException
	{
		launch(args);
	}
	
    public void start(Stage primaryStage) throws Exception{
    	primaryStage.show();
    }
}
