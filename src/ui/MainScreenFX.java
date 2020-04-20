package ui;

import java.io.FileNotFoundException;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * 
 * TODO - Only let user add foods/meals within drone carrying capacity?
 *
 */
public class MainScreenFX extends Application {


	private UI_Setup ui_Setup;
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		ui_Setup = new UI_Setup();
		
		SplashScreen splashScreen = new SplashScreen(ui_Setup);		
		try {
			splashScreen.makeSpashScreen();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		MainScreen mainScreen = new MainScreen(ui_Setup);
		mainScreen.makeMainScreen();

		SetupScreen setupScreen = new SetupScreen(ui_Setup);
		setupScreen.makeSetupScreen();

		ui_Setup.window = stage;

		ui_Setup.window.setScene(ui_Setup.splashScene);		
		ui_Setup.window.setTitle("Dromedary Drones");
		ui_Setup.window.show();
		
		
		PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
		pause.setOnFinished(event -> {
		        ui_Setup.window.setScene(ui_Setup.mainScene);
		});
		pause.play();
		
		
	}

	/**
	 * get rid of all the old graph files
	 */
	@Override
	public void stop() {
		ui_Setup.graphingTools.deleteGraphFiles();
	}

}
