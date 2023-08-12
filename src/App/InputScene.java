package App;


import java.util.ArrayList;

import DataStructures.Trie;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class InputScene
{
	private Stage primaryStage;
	
	public Scene inputScene;
		
	private GridPane layout;
	
	private Alert missingInfoError;
	
	private TextField categoryNameField;
	
	private TextField categoryUnitField;
	
	private Button addCategoryButton;
	
	private ListView<HBox> enteredCategories;
	
	private final static int CATEGORY_LABEL_ROW = 0;
		
	private final static int CATEGORY_INPUT_ROW = 1;
	
	private final static int CATEGORY_LIST_ROW = 2;
		
	private TextField courseNameField;
	
	private TextField courseUnitsField;
	
	//this is used to resolve a bug in the ComboBox class
	//which happens whenever a space is entered the input is ereased
	private ComboBoxListViewSkin<String> skin;
	
	private ComboBox<String> courseCategoryBox;
	
	private ComboBox<String> coursePrereqBox;
	
	private ListView<HBox> enteredCourses;
	
	private Button addCourse;
	
	private Button submitButton;
	
	private final static int COURSE_LABEL_ROW = 3;
	
	private final static int COURSE_INPUT_ROW = 4;
	
	private final static int COURSE_LIST_ROW = 5;
	
	private final static int MAX_COLUMS = 9;

	private final static double PADDING = 1;

	private static final double SPACING = 25;

	private static final double SUBMITBUTTON_VERTICAL_PADDING = 5;

	private static final double SUBMITBUTTON_HORIZANTAL_PADDING = 45;

	private static final int SUBMITBUTTON_ROW = 6;

	private static final int SUBMITBUTTON_COL = 4;
	
	private ArrayList<Category> categoryList;
	
	private Trie categoryIdTree;
	
	public InputScene(Stage primaryStage) 
	{
		this.primaryStage = primaryStage;
		initialize();
		buildScene();
		addFunctionality();

	}

	private void initialize()
	{
		layout = new GridPane();
		inputScene = new Scene(layout);
		missingInfoError = new Alert(AlertType.ERROR);
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
		categoryIdTree = new Trie();
		categoryList = new ArrayList<>();
		layout.setGridLinesVisible(true);
	}
	
	
	/**
	 * ======================================================================================
	 * ===============================SCENE BUILDING FUNCTIONS===============================
	 * ======================================================================================
	 * =============The scene building functions create the look for the input scene=========
	 * =============They add the ndoes and set prompt text etc.==============================
	 * ======================================================================================
	 */
	private void buildScene()
	{
		buildCategoryInput();
		buildCourseInput();
		addSubmitButton();

	}
	
	private void buildCategoryInput()
	{
		layout.add(new Label("Categories"), 0, CATEGORY_LABEL_ROW);
		layout.add(new Label("Category Name:"), 0, CATEGORY_INPUT_ROW);
		layout.add(categoryNameField, 1, CATEGORY_INPUT_ROW);
		layout.add(new Label("Unit Requirement:"), 2, CATEGORY_INPUT_ROW);
		layout.add(categoryUnitField, 3, CATEGORY_INPUT_ROW);
		layout.add(addCategoryButton, 4, CATEGORY_INPUT_ROW);
		layout.add(enteredCategories, 0, CATEGORY_LIST_ROW,MAX_COLUMS,1);
		setCategoryInputProperties();
	}
	
	private void setCategoryInputProperties()
	{
		categoryNameField.setPromptText("Enter Name");
		categoryUnitField.setPromptText("Enter Units");
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
		layout.add(enteredCourses,0,COURSE_LIST_ROW,MAX_COLUMS,1);
		setCourseInputProperties();
	}

	private void setCourseInputProperties()
	{
		courseNameField.setPromptText("Enter Name");
		courseUnitsField.setPromptText("Enter Units");
		courseCategoryBox.promptTextProperty().set("Choose Category");
		coursePrereqBox.promptTextProperty().set("Choose Prerequisites for Course");
		courseCategoryBox.setEditable(true);
		coursePrereqBox.setEditable(true);
	}
	

	private void addSubmitButton()
	{
		submitButton = new Button("Submit");
		submitButton.setPadding(new Insets(SUBMITBUTTON_VERTICAL_PADDING,SUBMITBUTTON_HORIZANTAL_PADDING,SUBMITBUTTON_VERTICAL_PADDING,SUBMITBUTTON_HORIZANTAL_PADDING));
		layout.add(submitButton, SUBMITBUTTON_COL, SUBMITBUTTON_ROW);
	}
	
	/**
	 * ===================================================================================
	 * ===================================================================================
	 * =============================END SCENE BUILDING FUNCTIONS==========================
	 * ===================================================================================
	 * ===================================================================================
	 */
	
	
	
	
	/**
	 * ===================================================================================
	 * ============================ADD FUNCTIONALITY FUNCTIONS============================
	 * ===================================================================================
	 * The functions below add functionallity to buttons and text boxes. The logic in these 
	 * functions is distinctly different from the scene building ones so I seperated them
	 * ===================================================================================
	 * ===================================================================================
	 */
	
	
	private void addFunctionality() 
	{
		addCategoryButtonFunctionality();
		addCourseButtonFunctionality();
		addcourseCategoryComboBoxFunctionality();

	}
	private void addCategoryButtonFunctionality()
	{
		addCategoryButton.setOnMouseClicked((notUsed) -> {	
			String categoryName = categoryNameField.getText();
			String categoryUnitRequirement = categoryUnitField.getText();

			if(categoryContainsError(categoryName, categoryUnitRequirement)) 
			{
				return;
			}
			
			HBox categoryBox = makeCategoryHBox(categoryName, categoryUnitRequirement);
			categoryList.add(new Category(categoryName, Integer.parseInt(categoryUnitRequirement)));
			categoryIdTree.addString(categoryName, categoryList.size() - 1);
			enteredCategories.getItems().add(categoryBox);
			categoryNameField.clear();
			categoryUnitField.clear();
		});
	}
	
	/**
	 * Objectives for add course button functionality
	 * check for errors in input information
	 * 	course name only letters and digits
	 * 	course units must be parsable
	 * 	category name if present must be valid (if filled)
	 * 	course prerequisites must be valid if filled 
	 * verify that if category list is not empty then all courses must have a category
	 * 	
	 * if empty then category list must be empty 
	 * if course prerqlist is empty leave it 
	 * if not verify all valid 
	 * 
	 */
	private void addCourseButtonFunctionality()
	{
		addCourse.setOnAction((click) -> {
			
		});
	}
	
	private void addcourseCategoryComboBoxFunctionality() 
	{
//		Uncomment this sections when project is done. For some reason 
//		a null pointer exception is thrown whenever something is typed in the combobox
//		skin = new ComboBoxListViewSkin<>(courseCategoryBox);
//		skin.getPopupContent().addEventFilter(KeyEvent.ANY, event -> {
//			if(event.getCode() == KeyCode.SPACE) {
//				event.consume();
//			}
//		});
//		courseCategoryBox.setSkin(skin);
//		
		
		courseCategoryBox.getEditor().setOnKeyTyped(event -> {
			courseCategoryBox.getItems().clear();
			String enteredString = courseCategoryBox.getEditor().getText();			
			ArrayList<Integer> matches = categoryIdTree.getNMatches(enteredString);
			for(int i = 0; i < matches.size(); i++) 
			{
				courseCategoryBox.getItems().add(categoryList.get(matches.get(i)).categoryName);
			}
			courseCategoryBox.show();
		});
	}


	private HBox makeCategoryHBox(String categoryName, String categoryUnitRequirement)
	{
		HBox categoryBox = new HBox();
		categoryBox.setSpacing(SPACING);
		categoryBox.setPadding(new Insets(PADDING));
		
		Button editButton = new Button("Edit");
		editButton.setOnAction((a) -> {
			categoryNameField.setText(categoryName);
			categoryUnitField.setText(categoryUnitRequirement);
			enteredCategories.getItems().remove(categoryBox);
		});
		
		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction((a) -> {
			enteredCategories.getItems().remove(categoryBox);
			categoryIdTree.removeWord(categoryName);
		});
		
		categoryBox.getChildren().addAll(new Label(categoryName), new Label(categoryUnitRequirement + " Units required to be completed"),
				editButton, deleteButton);
		
		return categoryBox;
	}


	private boolean categoryContainsError(String categoryName, String categoryUnitRequirement)
	{
		String errorMessage = "";
		if(categoryName.isBlank()) 
		{
			errorMessage += "Category field cannot be empty\n";
		}
		else if(HelperFunctions.containsNonSpaceCharDigit(categoryName)) 
		{
			errorMessage += "Category field must only consists of letters and digits\n";
		}
		if(HelperFunctions.numberCantBeParsed(categoryUnitRequirement)) 
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
	
	/**
	 * ===================================================================================
	 * ===================================================================================
	 * ============================END OF ADD FUNCTIONALLITY==============================
	 * ===================================================================================
	 * ===================================================================================
	 */















}
