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

	public static void main(String[] args) {
        launch(args);
    }
	
    @Override
    public void start(Stage stage) {
    	new MainPage();
    }
}

class MainPage extends Stage {
	// create new buttons
	Button setupPageButton = new Button("SETUP");
	Button runSimulation = new Button("RUN");
	
	// make a layout box for the screen
	HBox screenLayout = new HBox();
	
	MainPage() {
		
		screenLayout.setPadding(new Insets(50));
		
		// add the buttons to the screen
		screenLayout.getChildren().add(setupPageButton);
		screenLayout.getChildren().add(runSimulation);
		
		// make this the current scene
		this.setScene(new Scene(screenLayout, 800, 600));
		this.show();
		
		setupPageButton.setOnAction((ActionEvent event) -> {
			new SetupPage();
		});
	}
	
}

class SetupPage extends Stage {
	// create new buttons
		Button mainPageButton = new Button("BACK");
		
		// make a layout box for the screen
		HBox screenLayout = new HBox();
		
		SetupPage() {
			
			screenLayout.setPadding(new Insets(50));
			
			// add the buttons to the screen
			screenLayout.getChildren().add(mainPageButton);
			
			// make this the current scene
			this.setScene(new Scene(screenLayout, 800, 600));
			this.show();
			
			mainPageButton.setOnAction((ActionEvent event) -> {
				new MainPage();
			});
		}
}

class MapSetupPage extends Stage {
	
}
