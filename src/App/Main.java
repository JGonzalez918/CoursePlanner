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
		primaryStage.setResizable(false);
    	inputScene = new InputScene(primaryStage);
    	primaryStage.setScene(inputScene.inputScene);
		primaryStage.show();
    }
}
