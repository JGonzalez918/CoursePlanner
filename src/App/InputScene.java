package App;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
	
	private ComboBox<String> courseCategoryBox;
	
	private ComboBox<String> coursePrereqBox;
	
	private ListView<HBox> enteredCourses;
	
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
		categoryButtonFunctionality();
		addCourseButtonFunctionality();
	}


	private void addCourseButtonFunctionality()
	{
		addCourse.setOnAction((notUsed) -> {
			
		});
	}

	private void categoryButtonFunctionality()
	{
		addCategoryButton.setOnAction((notUsed) -> {			
			String categoryName = categoryNameField.getText().strip();
			String categoryUnitRequirement = categoryUnitField.getText().strip();

			if(containsError(categoryName, categoryUnitRequirement)) 
			{
				return;
			}
			HBox items = makeHBox(categoryName, categoryUnitRequirement);
			//TODO: add category name to catgeory choice box
			enteredCategories.getItems().add(items);
			categoryNameField.clear();
			categoryUnitField.clear();
		});
	}


	private HBox makeHBox(String categoryName, String categoryUnitRequirement)
	{
		HBox items = new HBox();
		Button editButton = new Button("Edit");
		editButton.setOnAction((a) -> {
			categoryNameField.setText(categoryName);
			categoryUnitField.setText(categoryUnitRequirement);
			enteredCategories.getItems().remove(items);
		});
		
		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((a) -> {
			enteredCategories.getItems().remove(items);
		});
		items.getChildren().addAll(new Label(categoryName), new Label(categoryUnitRequirement),
				editButton, deleteButton);
		return items;
	}


	private boolean containsError(String categoryName, String categoryUnitRequirement)
	{
		String errorMessage = "";
		if(categoryName.isEmpty()) 
		{
			errorMessage = "Category field cannot be empty";
		}
		if(numberCantBeParsed(categoryUnitRequirement)) 
		{
			errorMessage += "\nUnit requirement field cannot be read as an integer";
		}
		if(errorMessage.isEmpty() == false) 
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.contentTextProperty().set(errorMessage);
			alert.headerTextProperty().set("Missing Information");
			alert.showAndWait();
			return true;
		}
		return false;
	}
	
	private boolean numberCantBeParsed(String categoryUnitRequirement) 
	{
		try 
		{
			Integer.parseInt(categoryUnitRequirement);
		}catch(NumberFormatException e) 
		{
			return true;
		}
		return false;
	}


	private void buildCourseInput()
	{
		layout.add(new Label("Courses:"), 0, COURSE_LABEL_ROW);
		layout.add(new Label("Course Name"), 0, COURSE_INPUT_ROW);
		layout.add(courseNameField, 1, COURSE_INPUT_ROW);
		layout.add(new Label("Course Units"), 2, COURSE_INPUT_ROW);
		layout.add(courseUnitsField, 3, COURSE_INPUT_ROW);
		layout.add(new Label("Course Category"), 4, COURSE_INPUT_ROW);
		layout.add(courseCategoryBox, 5, COURSE_INPUT_ROW);
		layout.add(new Label("Course Prerequisites"), 6, COURSE_INPUT_ROW);
		layout.add(coursePrereqBox, 7, COURSE_INPUT_ROW);
		layout.add(addCourse, 8, COURSE_INPUT_ROW);
		layout.add(enteredCourses,0,COURSE_LIST_ROW,9,1);
		setCourseInputProperties();
	}


	private void setCourseInputProperties()
	{
		courseNameField.setPromptText("Enter Name Here");
		courseUnitsField.setPromptText("Enter Units Here");
		courseCategoryBox.promptTextProperty().set("Choose Category");
		coursePrereqBox.promptTextProperty().set("Choose Prerequisites for Course");
		courseCategoryBox.setEditable(true);
		coursePrereqBox.setEditable(true);
	}


	private void setCategoryInputProperties()
	{
		categoryNameField.setPromptText("Enter Name Here");
		categoryUnitField.setPromptText("Enter Units Here");
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
		setCategoryInputProperties();
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
		courseCategoryBox = new ComboBox<>();
		coursePrereqBox = new ComboBox<>();
		enteredCourses = new ListView<>();
		addCourse = new Button("Add Course");
	}
}
