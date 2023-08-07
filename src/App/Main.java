package App;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
	static InputScene inputScene;

	public static void main(String[] args) 
	{
		launch(args);
	}
	
    public void start(Stage primaryStage) throws Exception{
    	inputScene = new InputScene(primaryStage);
    }
}
