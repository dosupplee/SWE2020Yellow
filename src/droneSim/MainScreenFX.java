package droneSim;

import java.io.File;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainScreenFX extends Application {
	
	public static Stage window;
	public static Scene mainScene, setupScene;

	public static void main(String[] args) {
        launch(args);
    }
	
	 @Override
	    public void start(Stage stage) {
		 	
		 	// create the color for the background
		 	BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
		 	// make the actual background to be used
		 	Background applicationBackground = new Background(applicationColor);
		 	
	    	window = stage;
	    	
	    	/*
	    	 * MAIN PAGE
	    	 * */
	    	
	    	// file chooser for selecting a saved setup
	    	FileChooser selectSetupFile = new FileChooser();
	    	// title for the file chooser
	    	selectSetupFile.setTitle("Select Saved Setup");
	    	
	    	// Main Page text
	    	Label fileName = new Label("<FileName>");
	    	// change the size and font of the label
	    	fileName.setFont(new Font("Arial", 18));
	    	
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
	    	selectFileButton.setMaxSize(120, 25);
	    	
	    	// layout for the main page
	    	GridPane screenLayoutMain = new GridPane();
	    	screenLayoutMain.setHgap(8);
	    	screenLayoutMain.setVgap(8);
	    	screenLayoutMain.setPadding(new Insets(50));
	    	screenLayoutMain.setBackground(applicationBackground);
	    	
	    	// add all the items to the screen
	    	screenLayoutMain.add(setupPageButton, 22, 42, 16, 8);
	    	screenLayoutMain.add(runSimulationButton, 2, 42, 16, 8);
	    	screenLayoutMain.add(saveLogButton, 2, 52, 16, 8);
	    	screenLayoutMain.add(clearLogButton, 22, 52, 16, 8);
	    	screenLayoutMain.add(selectFileButton, 22, 36, 16, 4);
	    	screenLayoutMain.add(fileName, 4, 36, 16, 4);
	    	
	    	// making the new main scene
	    	mainScene = new Scene(screenLayoutMain, 800, 600);
	    	
	    	// change to the setup page
	    	setupPageButton.setOnAction(e -> {
	    		window.setScene(setupScene); 
	    	});
	    	
	    	// select the file button
	    	selectFileButton.setOnAction(e -> {
	    		File selectedSetupFile = selectSetupFile.showOpenDialog(stage);
	    	});
	    	
	    	
	    	
	    	/*
	    	 * SETUP PAGE
	    	 * */
	    	
	    	// setup page buttons
	    	Button backButton = new Button("BACK");
	    	Button addFoodButton = new Button("ADD FOOD");
	    	Button addMealButton = new Button("ADD MEAL");
	    	
	    	// change the size of the page buttons
	    	backButton.setMaxSize(100, 50);
	    	addFoodButton.setMaxSize(150, 50);
	    	addMealButton.setMaxSize(150, 50);
	    	
	    	// layout for the setup page
	    	GridPane screenLayoutSetup = new GridPane();
	    	screenLayoutSetup.setHgap(8);
	    	screenLayoutSetup.setVgap(8);
	    	screenLayoutSetup.setPadding(new Insets(50));
	    	screenLayoutSetup.setBackground(applicationBackground);
	    	
	    	screenLayoutSetup.add(backButton, 2, 52, 16, 8);
	    	screenLayoutSetup.add(addFoodButton, 52, 10, 16, 8);
	    	screenLayoutSetup.add(addMealButton, 52, 50, 16, 8);
	 
	    	// making the new setup scene
	    	setupScene = new Scene(screenLayoutSetup, 800, 600);
	    	
	    	// change to the main page
	    	backButton.setOnAction(e ->  {
	    		window.setScene(mainScene);
	    	});
	    
	    	
	    	window.setScene(mainScene);
	    	window.setTitle("Dromedary Drones");
	    	window.show();
	    }
}
