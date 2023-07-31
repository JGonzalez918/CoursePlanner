package App;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class InputScene
{
	private Stage primaryStage;
	
	private Scene inputScene;
		
	private GridPane layout;

	private TextField categoryNameField;
	
	private TextField categoryUnitField;
	
	private Button addCategoryButton;
	
	private ListView<HBox> enteredCategories;
	
	private final static int CATEGORY_LABEL_ROW = 0;
		
	private final static int CATEGORY_INPUT_ROW = 1;
	
	private final static int CATEGORY_LIST_ROW = 2;
	
	private TextField courseNameField;
	
	private TextField courseUnitsField;
	
	private ComboBox<String> courseCategoryField;
	
	private ComboBox<String> coursePrereqBox;
	
	private ListView<String> enteredCourses;
	
	private Button addCourse;
	
	private final static int COURSE_LABEL_ROW = 3;
	
	private final static int COURSE_INPUT_ROW = 4;
	
	private final static int COURSE_LIST_ROW = 5;
	
	
	public InputScene(Stage primaryStage) 
	{
		this.primaryStage = primaryStage;
		initialize();
		buildScene();
		primaryStage.setScene(inputScene);
		primaryStage.show();
	}


	private void buildScene()
	{
		buildCategoryInput();
		buildCourseInput();
	}


	private void buildCourseInput()
	{
		layout.add(new Label("Courses:"), 0, COURSE_LABEL_ROW);
		layout.add(new Label("Course Name"), 0, COURSE_INPUT_ROW);
		layout.add(courseNameField, 1, COURSE_INPUT_ROW);
		layout.add(new Label("Course Units"), 2, COURSE_INPUT_ROW);
		layout.add(courseUnitsField, 3, COURSE_INPUT_ROW);
		layout.add(new Label("Course Category"), 4, COURSE_INPUT_ROW);
		layout.add(courseCategoryField, 5, COURSE_INPUT_ROW);
		layout.add(new Label("Course Prerequisites"), 6, COURSE_INPUT_ROW);
		layout.add(coursePrereqBox, 7, COURSE_INPUT_ROW);
		layout.add(addCourse, 8, COURSE_INPUT_ROW);
		layout.add(enteredCourses,0,COURSE_LIST_ROW,9,1);
	}


	private void buildCategoryInput()
	{
		layout.add(new Label("Categories"), 0, CATEGORY_LABEL_ROW);
		layout.add(new Label("Category Name:"), 0, CATEGORY_INPUT_ROW);
		layout.add(categoryNameField, 1, CATEGORY_INPUT_ROW);
		layout.add(new Label("Unit Requirement:"), 2, CATEGORY_INPUT_ROW);
		layout.add(categoryUnitField, 3, CATEGORY_INPUT_ROW);
		layout.add(addCategoryButton, 4, CATEGORY_INPUT_ROW);
		layout.add(enteredCategories, 0, CATEGORY_LIST_ROW,9,1);
	}


	private void initialize()
	{
		layout = new GridPane();
		inputScene = new Scene(layout);
		categoryNameField = new TextField();
		categoryUnitField = new TextField();
		enteredCategories = new ListView<>();
		addCategoryButton = new Button("Add category");
		courseNameField = new TextField();
		courseUnitsField = new TextField();
		courseCategoryField = new ComboBox<>();
		courseCategoryField.setEditable(true);
		coursePrereqBox = new ComboBox<>();
		coursePrereqBox.setEditable(true);
		enteredCourses = new ListView<String>();
		addCourse = new Button("Add Course");
	}
}
