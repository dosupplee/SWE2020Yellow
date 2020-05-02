package ui;

import droneSim.CurrentSetup;
import droneSim.Runner;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UI_Setup {
	public Stage window;
	public Scene mainScene, setupScene, splashScene;
	public Runner runner;
	public CurrentSetup curSetup;
	public FileTools fileTools;

	public final int SCREEN_HEIGHT = 600;
	public final int SCREEN_WIDTH = 900;
	
	public final String SPLASH_SCREEN_TITLE = "Dromitary Drone Delivery Simulation";
	
	public UI_Setup() {
		
		
		runner = new Runner();
		curSetup = runner.getCurrentSetup();
		fileTools = new FileTools();
	}

	
}
