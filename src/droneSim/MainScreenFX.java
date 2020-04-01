package droneSim;

import java.io.File;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.collections.*;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;


public class MainScreenFX extends Application {
	
	public static Stage window;
	public static Scene mainScene, setupScene;

	public static void main(String[] args) {
        launch(args);
    }
	
	@Override
	public void start(Stage stage) {
	 	
	 	makeMainScreen();
	 	makeSetupScreen();
	 
    	window = stage;
    
    	
    	window.setScene(mainScene);
    	window.setTitle("Dromedary Drones");
    	window.show();
    }
	
 
 	public void makeMainScreen() {
 	
	
		// create the color for the background
	 	BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
	 	// make the actual background to be used
	 	Background applicationBackground = new Background(applicationColor);
	 	
	 	
	 	/*
	 	 * Main page screen setup
	 	 * */
		double screenW = 800;
		double screenH = 600;
		int insets = 15;
		int numColumns = 4;
		int numRows = 14;
		double colW = screenW / numColumns - 11;
		double rowH = screenH / numRows - insets / numRows;
		
		// layout for the main page
		GridPane screenLayoutMain = new GridPane();
		screenLayoutMain.setMaxWidth(colW * numColumns - 2 * insets);
		screenLayoutMain.setMaxHeight(rowH * numRows - 2 * insets);
		screenLayoutMain.setPadding(new Insets(insets * 2));
		screenLayoutMain.setHgap(8);
		screenLayoutMain.setVgap(8);
		screenLayoutMain.setBackground(applicationBackground);
	
		// set column widths
		for (int i = 0; i < numColumns; i++) {
	
			ColumnConstraints column = new ColumnConstraints(colW);
			screenLayoutMain.getColumnConstraints().add(column);
	
		}
	
		// set column heights
		for (int i = 0; i < numRows; i++) {
			RowConstraints row = new RowConstraints(rowH);
			screenLayoutMain.getRowConstraints().add(row);
		}
	 	
	 	// file chooser for selecting a saved setup
		FileChooser selectSetupFile = new FileChooser();
		// title for the file chooser
		selectSetupFile.setTitle("Select Saved Setup");
		
		// Main Page text
		Label fileName = new Label("<FileName>");
		Label avgTime = new Label("AVERAGE TIME:");
		Label slowestTime = new Label("SLOWEST TIME:");
		Label fastestTime = new Label("FASTEST TIME:");
		
		// change the size and font of the label
		fileName.setFont(new Font("Arial", 18));
		avgTime.setFont(new Font("Arial", 18));
		slowestTime.setFont(new Font("Arial", 18));
		fastestTime.setFont(new Font("Arial", 18));
		
		fileName.setTextFill(Color.WHITE);
		avgTime.setTextFill(Color.WHITE);
		slowestTime.setTextFill(Color.WHITE);
		fastestTime.setTextFill(Color.WHITE);
	
		
		// create new text Area for the running log
		TextArea outputLog = new TextArea();
		outputLog.setMaxWidth(1.8 * colW);
		outputLog.setMaxHeight(6 * rowH);
		
		// create new text Area for the results
		TextArea outputResults = new TextArea();
		outputResults.setMaxWidth(colW * .75);
		outputResults.setMaxHeight(6 * rowH);
		
		// Main page buttons
		Button setupPageButton = new Button("SETUP");
		Button runSimulationButton = new Button("RUN");
		Button saveLogButton = new Button("SAVE LOG");
		Button clearLogButton = new Button("CLEAR LOG");
		Button selectFileButton = new Button("SELECT FILE");
		
		// change the size of buttons
		setupPageButton.setMaxSize(150, 50);
		runSimulationButton.setMaxSize(150, 50);
		saveLogButton.setMaxSize(150, 50);
		clearLogButton.setMaxSize(150, 50);
		selectFileButton.setMaxSize(150, 25);
		
		
		// add buttons to the main screen
		screenLayoutMain.add(setupPageButton, 1, 8, 1, 1);
		screenLayoutMain.add(runSimulationButton, 0, 8, 1, 1);
		screenLayoutMain.add(saveLogButton, 0, 9, 1, 1);
		screenLayoutMain.add(clearLogButton, 1, 9, 1, 1);
		screenLayoutMain.add(selectFileButton, 1, 7, 2, 1);
		screenLayoutMain.add(fileName, 0, 7, 1, 1);
		
		// add output blocks to the main screen
		screenLayoutMain.add(outputLog, 2, 0, 2, 5);
		screenLayoutMain.add(outputResults, 3, 6, 1, 5);
		
		// add results text to the screen
		screenLayoutMain.add(avgTime, 2, 6, 1, 1);
		screenLayoutMain.add(fastestTime, 2, 7, 1, 1);
		screenLayoutMain.add(slowestTime, 2, 8, 1, 1);
		
		// making the new main scene
		mainScene = new Scene(screenLayoutMain, 800, 600);
		
		// change to the setup page
		setupPageButton.setOnAction(e -> {
			window.setScene(setupScene); 
		});
		
		// select the file button
	    	selectFileButton.setOnAction(e -> {
	    		File selectedSetupFile = selectSetupFile.showOpenDialog(window);
	    	});
 	}
 
 	public void makeSetupScreen() {
			// -----------------------------------------
		// SETUP PAGE
		// -----------------------------------------
		// 5 columns wide
		// first 2 are meal/prob
		// next 2 are meal items/possible foods
	
		// create the color for the background
	 	BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
	 	// make the actual background to be used
	 	Background applicationBackground = new Background(applicationColor);
		
		
		//
		double screenW = 800;
		double screenH = 600;
		int insets = 15;
		int numColumns = 4;
		int numRows = 14;
		double colW = screenW / numColumns - 11;
		double rowH = screenH / numRows - insets / numRows;
	
		GridPane screenLayoutSetup = new GridPane(); // main layout
		screenLayoutSetup.setMaxWidth(colW * numColumns - 2 * insets);
		screenLayoutSetup.setMaxHeight(rowH * numRows - 2 * insets);
		screenLayoutSetup.setPadding(new Insets(insets));
		screenLayoutSetup.setVgap(5);
		screenLayoutSetup.setHgap(5);
		screenLayoutSetup.setBackground(applicationBackground);
	
		// set column widths
		for (int i = 0; i < numColumns; i++) {
	
			ColumnConstraints column = new ColumnConstraints(colW);
			screenLayoutSetup.getColumnConstraints().add(column);
	
		}
	
		// set column heights
		for (int i = 0; i < numRows; i++) {
			RowConstraints row = new RowConstraints(rowH);
			screenLayoutSetup.getRowConstraints().add(row);
		}
	
		// ------------------
		// Screen Objects
		// ------------------
	
		// create some new buttons
		Button mainPageButton = new Button("BACK");
		Button createFoodButton = new Button("CREATE FOOD");
		Button addFoodButton = new Button("ADD FOOD");
		Button addMealButton = new Button("ADD MEAL");
		Button clearMealButton = new Button("CLEAR MEAL");
		mainPageButton.setOnAction(e -> window.setScene(mainScene)); // go back to main screen
		
		mainPageButton.setMaxSize(150, 50);
		createFoodButton.setMaxSize(150, 50);
		addFoodButton.setMaxSize(150, 50);
		addMealButton.setMaxSize(150, 50);
		clearMealButton.setMaxSize(150, 50);
	
		// create new text fields
		TextField foodNameTextField = new TextField();
		TextField foodWeightTextField = new TextField();
		foodNameTextField.setPromptText("Food name");
		foodWeightTextField.setPromptText("Food weight (oz)");
		foodNameTextField.setMaxWidth(1.5*colW);
		foodWeightTextField.setMaxWidth(1.5*colW);
		
		// create new combo box
		// TODO
		ObservableList<String> options = FXCollections.observableArrayList( "Pizza", "French Fries", "Burger", "Drink");
		ComboBox<String> foodOptionsComboBox = new ComboBox<>(options);
		foodOptionsComboBox.setPromptText("SELECT FOOD");
		foodOptionsComboBox.setMaxWidth(150);
		
		ObservableList<Integer> qoptions = FXCollections.observableArrayList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		ComboBox<Integer> foodQuantityComboBox = new ComboBox<>(qoptions);
		foodQuantityComboBox.setPromptText("QUANTITY");
		foodQuantityComboBox.setMaxWidth(150);
	
		// create new text Area
		TextArea mealProbTextArea = new TextArea();
		mealProbTextArea.setMaxWidth(2 * colW);
		mealProbTextArea.setMaxHeight(12 * rowH);
		
		TextArea mealCreaterTextArea = new TextArea();
		mealCreaterTextArea.setMaxWidth(colW);
		mealCreaterTextArea.setMaxHeight(6 * rowH);
	
		// create new labels
		Label mealLabel = new Label("MEAL");
		Label probabilityLabel = new Label("PROBABILITY");
		Label inMealLabel = new Label("FOODS IN MEAL");
		
		mealLabel.setFont(new Font("Arial", 18));
		probabilityLabel.setFont(new Font("Arial", 18));
		inMealLabel.setFont(new Font("Arial", 18));
		
//		mealLabel.setUnderline(true);
//		probabilityLabel.setUnderline(true);
//		inMealLabel.setUnderline(true);
//		
		mealLabel.setTextFill(Color.WHITE);
		probabilityLabel.setTextFill(Color.WHITE);
		inMealLabel.setTextFill(Color.WHITE);
	
		// ------------------
		// left area
		// ------------------
		// add labels for meal/prob
		screenLayoutSetup.add(mealLabel, 0, 0);
		screenLayoutSetup.add(probabilityLabel, 1, 0);
	
		// set their allignment
		screenLayoutSetup.setHalignment(mealLabel, HPos.CENTER);
		screenLayoutSetup.setHalignment(probabilityLabel, HPos.CENTER);
	
		// add text area
		screenLayoutSetup.add(mealProbTextArea, 0, 1, 2, 10);
	
		// add back button
		screenLayoutSetup.add(mainPageButton, 0, 11);
	
		// ------------------
		// right side
		// ------------------
		screenLayoutSetup.add(new Label(), 2, 0);
	
	
		screenLayoutSetup.add(foodNameTextField, 2, 1,2,1);
		screenLayoutSetup.add(foodWeightTextField, 2, 2,2,1);
		screenLayoutSetup.setHalignment(foodNameTextField, HPos.CENTER);
		screenLayoutSetup.setHalignment(foodWeightTextField, HPos.CENTER);
	
		screenLayoutSetup.add(createFoodButton, 2, 3,2,1);
		screenLayoutSetup.setHalignment(createFoodButton, HPos.CENTER);
		
		
		
		// add some meal stuff
		screenLayoutSetup.add(inMealLabel, 3, 4);
		screenLayoutSetup.setHalignment(inMealLabel, HPos.CENTER);
		screenLayoutSetup.add(mealCreaterTextArea, 3, 5, 1, 5);
		
		screenLayoutSetup.add(foodOptionsComboBox, 2, 6);
		screenLayoutSetup.add(foodQuantityComboBox, 2, 7);
		screenLayoutSetup.add(addFoodButton, 2, 8);
		screenLayoutSetup.setHalignment(foodOptionsComboBox, HPos.CENTER);
		screenLayoutSetup.setHalignment(foodQuantityComboBox, HPos.CENTER);
		screenLayoutSetup.setHalignment(addFoodButton, HPos.CENTER);
		
		screenLayoutSetup.add(addMealButton, 2, 10);
		screenLayoutSetup.add(clearMealButton, 3, 10);
		screenLayoutSetup.setHalignment(addMealButton, HPos.CENTER);
		screenLayoutSetup.setHalignment(clearMealButton, HPos.CENTER);
		
		
		
		// ------------------
		// spacer
		// ------------------
		//
	
		setupScene = new Scene(screenLayoutSetup, screenW, screenH);
	}
}
