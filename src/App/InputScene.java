package App;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import DataStructures.Trie;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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
	
	private Alert missingInfoError;

	private TextField categoryAbbreviation;
	
	private TextField categoryNameField;
	
	private TextField categoryUnitField;
	
	private Button addCategoryButton;
	
	private ListView<HBox> enteredCategories;
	
	private final static int CATEGORY_LABEL_ROW = 0;
		
	private final static int CATEGORY_INPUT_ROW = 1;
	
	private final static int CATEGORY_LIST_ROW = 2;
	
	private TextField courseAbbreviation;
	
	private TextField courseNameField;
	
	private TextField courseUnitsField;
	
	private ComboBox<String> courseCategoryBox;
	
	private ComboBox<String> coursePrereqBox;
	
	private ListView<HBox> enteredCourses;
	
	private Button addCourse;
	
	private Button submitButton;
	
	private final static int COURSE_LABEL_ROW = 3;
	
	private final static int COURSE_INPUT_ROW = 4;
	
	private final static int COURSE_LIST_ROW = 5;
	
	private final static int MAX_COLUMS = 11;

	private final static double PADDING = 1;

	private static final double SPACING = 25;

	private static final double SUBMITBUTTON_VERTICAL_PADDING = 5;

	private static final double SUBMITBUTTON_HORIZANTAL_PADDING = 75;

	private static final int SUBMITBUTTON_ROW = 6;

	private static final int SUBMITBUTTON_COL = 5;
	
	private ArrayList<Category> categoryList;
	
	private Trie categoryIdTree;
	
	public InputScene(Stage primaryStage) 
	{
		this.primaryStage = primaryStage;
		primaryStage.setResizable(false);
		
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
		addSubmitButton();
	}


	private void addSubmitButton()
	{
		submitButton = new Button("Submit");
		submitButton.setPadding(new Insets(SUBMITBUTTON_VERTICAL_PADDING,SUBMITBUTTON_HORIZANTAL_PADDING,SUBMITBUTTON_VERTICAL_PADDING,SUBMITBUTTON_HORIZANTAL_PADDING));
		
		layout.add(submitButton, SUBMITBUTTON_COL, SUBMITBUTTON_ROW);
	}


	private void addCourseButtonFunctionality()
	{
		addCourse.setOnAction((click) -> {

		});
	}
	
	private void courseCategoryBox() 
	{
		courseCategoryBox.getEditor().setOnMouseClicked(event -> {
			courseCategoryBox.getItems().clear();
			ArrayList<Integer> matches = categoryIdTree.getNMatches("");
			for(int i = 0; i < matches.size(); i++) 
			{
				courseCategoryBox.getItems().add(categoryList.get(matches.get(i)).toString());
			}
			courseCategoryBox.show();
			
		});
		
		courseCategoryBox.getEditor().setOnKeyTyped(event -> {
			courseCategoryBox.getItems().clear();
			String enteredString = courseCategoryBox.getEditor().getText();
			
			ArrayList<Integer> matches = categoryIdTree.getNMatches(enteredString);
			for(int i = 0; i < matches.size(); i++) 
			{
				courseCategoryBox.getItems().add(categoryList.get(matches.get(i)).toString());
			}
			courseCategoryBox.show();
		});
	}

	private void categoryButtonFunctionality()
	{
		addCategoryButton.setOnAction((notUsed) -> {	
			String categoryAbbr = categoryAbbreviation.getText();//all caps no space function
			String categoryName = categoryNameField.getText().strip();
			String categoryUnitRequirement = categoryUnitField.getText().strip();

			if(containsError(categoryAbbr, categoryName, categoryUnitRequirement)) 
			{
				return;
			}
			HBox categoryBox = makeHBox(categoryAbbr,categoryName, categoryUnitRequirement);
			categoryList.add(new Category(categoryAbbr, categoryName, Integer.parseInt(categoryUnitRequirement)));
			categoryIdTree.addString(categoryAbbr, categoryList.size() - 1);
			
			enteredCategories.getItems().add(categoryBox);
			categoryAbbreviation.clear();
			categoryNameField.clear();
			categoryUnitField.clear();
		});
	}


	private HBox makeHBox(String categoryAbbr, String categoryName, String categoryUnitRequirement)
	{
		HBox categoryBox = new HBox();
		categoryBox.setSpacing(SPACING);
		categoryBox.setPadding(new Insets(PADDING));
		
		Button editButton = new Button("Edit");
		editButton.setOnAction((a) -> {
			categoryAbbreviation.setText(categoryAbbr);
			categoryNameField.setText(categoryName);
			categoryUnitField.setText(categoryUnitRequirement);
			enteredCategories.getItems().remove(categoryBox);
		});
		
		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((a) -> {
			enteredCategories.getItems().remove(categoryBox);
		});
		
		categoryBox.getChildren().addAll(new Label(categoryAbbr), new Label(categoryName), new Label(categoryUnitRequirement + " Units"),
				editButton, deleteButton);
		
		return categoryBox;
	}


	private boolean containsError(String categoryAbrr, String categoryName, String categoryUnitRequirement)
	{
		String errorMessage = "";
		if(categoryAbrr.isEmpty()) 
		{
			errorMessage = "Category Abbreviation field cannot be empty\n";
		}
		if(categoryName.isEmpty()) 
		{
			errorMessage += "Category field cannot be empty\n";
		}
		if(numberCantBeParsed(categoryUnitRequirement)) 
		{
			errorMessage += "Unit requirement field cannot be read as an integer";
		}
		if(errorMessage.isEmpty() == false) 
		{
			missingInfoError.contentTextProperty().set(errorMessage);
			missingInfoError.headerTextProperty().set("Missing Information");
			missingInfoError.showAndWait();
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
		layout.add(new Label("Course abbreviation"), 0, COURSE_INPUT_ROW);
		layout.add(courseAbbreviation, 1, COURSE_INPUT_ROW);
		layout.add(new Label("Course Name"), 2, COURSE_INPUT_ROW);
		layout.add(courseNameField, 3, COURSE_INPUT_ROW);
		layout.add(new Label("Course Units"), 4, COURSE_INPUT_ROW);
		layout.add(courseUnitsField, 5, COURSE_INPUT_ROW);
		layout.add(new Label("Course Category"), 6, COURSE_INPUT_ROW);
		layout.add(courseCategoryBox, 7, COURSE_INPUT_ROW);
		layout.add(new Label("Course Prerequisites"), 8, COURSE_INPUT_ROW);
		layout.add(coursePrereqBox, 9, COURSE_INPUT_ROW);
		layout.add(addCourse, 10, COURSE_INPUT_ROW);
		layout.add(enteredCourses,0,COURSE_LIST_ROW,MAX_COLUMS,1);
		setCourseInputProperties();
		courseCategoryBox();
	}


	private void setCourseInputProperties()
	{
		courseAbbreviation.setPromptText("Enter abbreviation to course");
		courseNameField.setPromptText("Enter Name");
		courseUnitsField.setPromptText("Enter Units");
		courseCategoryBox.promptTextProperty().set("Choose Category");
		coursePrereqBox.promptTextProperty().set("Choose Prerequisites for Course");
		courseCategoryBox.setEditable(true);
		coursePrereqBox.setEditable(true);
	}


	private void setCategoryInputProperties()
	{
		categoryAbbreviation.setPromptText("Enter abbreviation");
		categoryNameField.setPromptText("Enter Name");
		categoryUnitField.setPromptText("Enter Units");
	}


	private void buildCategoryInput()
	{
		layout.add(new Label("Categories"), 0, CATEGORY_LABEL_ROW);
		layout.add(new Label("Category Abbreviation"), 0, CATEGORY_INPUT_ROW);
		layout.add(categoryAbbreviation, 1, CATEGORY_INPUT_ROW);
		layout.add(new Label("Category Name:"), 2, CATEGORY_INPUT_ROW);
		layout.add(categoryNameField, 3, CATEGORY_INPUT_ROW);
		layout.add(new Label("Unit Requirement:"), 4, CATEGORY_INPUT_ROW);
		layout.add(categoryUnitField, 5, CATEGORY_INPUT_ROW);
		layout.add(addCategoryButton, 6, CATEGORY_INPUT_ROW);
		layout.add(enteredCategories, 0, CATEGORY_LIST_ROW,MAX_COLUMS,1);
		setCategoryInputProperties();
	}


	private void initialize()
	{
		layout = new GridPane();
		inputScene = new Scene(layout);
		missingInfoError = new Alert(AlertType.ERROR);
		categoryAbbreviation = new TextField();
		categoryNameField = new TextField();
		categoryUnitField = new TextField();
		enteredCategories = new ListView<>();
		addCategoryButton = new Button("Add category");
		courseAbbreviation = new TextField();
		courseNameField = new TextField();
		courseUnitsField = new TextField();
		courseCategoryBox = new ComboBox<>();
		coursePrereqBox = new ComboBox<>();
		enteredCourses = new ListView<>();
		addCourse = new Button("Add Course");
		categoryIdTree = new Trie();
		categoryList = new ArrayList<>();
		layout.setGridLinesVisible(true);
	}
}
