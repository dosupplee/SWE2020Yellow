package ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creates a splash screen with an image
 * 
 * @author LEHMANIT17
 *
 */
public class SplashScreen extends Stage {

	private UI_Setup ui_Setup;

	public SplashScreen(UI_Setup ui_Setup) {
		this.ui_Setup = ui_Setup;
	}

	public void makeSpashScreen() throws FileNotFoundException {
		// Creating an image
		Image image = new Image(new FileInputStream("Loading Screen.jpg"));

		// Setting the image view
		ImageView imageView = new ImageView(image);


		// setting the fit height and width of the image view
		imageView.setFitHeight(ui_Setup.SCREEN_HEIGHT);
		imageView.setFitWidth(ui_Setup.SCREEN_WIDTH);

		// Setting the preserve ratio of the image view
		//imageView.setPreserveRatio(true);
		
		// add image to layout
		VBox picture = new VBox(imageView);
		VBox.setVgrow(imageView, Priority.ALWAYS);

		ui_Setup.splashScene = new Scene(picture, ui_Setup.SCREEN_WIDTH, ui_Setup.SCREEN_HEIGHT);
		
		ui_Setup.splashScene.getStylesheets().add("SplashPage.css");
	}
}
