package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Creates a custom pop up window with given text and title
 * 
 * @author LEHMANIT17
 *
 */
public class PopUp extends Stage {
	public PopUp(String title, String msg) {
		setTitle(title);
		VBox y = new VBox();

		y.getChildren().add(new Label(msg));
		y.setAlignment(Pos.CENTER);
		setScene(new Scene(y, 300, 100));
		show();

	}
}
