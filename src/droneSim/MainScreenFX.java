package droneSim;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainScreenFX extends Application {
	
	public static Stage window;
	public static Scene mainScene, setupScene;

	public static void main(String[] args) {
        launch(args);
    }
	
	 @Override
	    public void start(Stage stage) {
	  
	    	window = stage;
	    	
	    	// create new buttons
	    	Button setupPageButton = new Button("SETUP");
	    	Button runSimulation = new Button("RUN");
	    	Button mainPageButton = new Button("BACK");
	    	
	    	// layout for the main page
	    	HBox screenLayoutMain = new HBox();
	    	screenLayoutMain.setPadding(new Insets(50));
	    	
	    	screenLayoutMain.getChildren().addAll(setupPageButton, runSimulation);
	    	
	    	// layout for the setup page
	    	HBox screenLayoutSetup = new HBox();
	    	screenLayoutSetup.setPadding(new Insets(50));
	    	
	    	screenLayoutSetup.getChildren().addAll(mainPageButton);
	 
	    	// making the new scenes
	    	mainScene = new Scene(screenLayoutMain, 800, 600);
	    	setupScene = new Scene(screenLayoutSetup, 800, 600);
	    	
	    	// adding events for the button click
	    	setupPageButton.setOnAction((ActionEvent event) -> {
	    		window.setScene(setupScene); 
	    	});
	    	mainPageButton.setOnAction((ActionEvent event) ->  {
	    		window.setScene(mainScene);
	    	});
	    	
	    	
	    	window.setScene(mainScene);
	    	window.setTitle("Dromedary Drones");
	    	window.show();
	    }
}
