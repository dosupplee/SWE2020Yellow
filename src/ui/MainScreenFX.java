package ui;

import javafx.application.Application;
import javafx.stage.Stage;

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
		

		MainScreen mainScreen = new MainScreen(ui_Setup);
		mainScreen.makeMainScreen();

		SetupScreen setupScreen = new SetupScreen(ui_Setup);
		setupScreen.makeSetupScreen();

		ui_Setup.window = stage;

		ui_Setup.window.setScene(ui_Setup.mainScene);
		ui_Setup.window.setTitle("Dromedary Drones");
		ui_Setup.window.show();
	}

	/**
	 * get rid of all the old graph files
	 */
	@Override
	public void stop() {
		ui_Setup.graphingTools.deleteGraphFiles();
	}

}
