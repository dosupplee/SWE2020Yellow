package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Creates a custom pop up window with given text and title
 * 
 * @author LEHMANIT17
 *
 */
public class PopUp extends Stage {
	TextField inputBox;
	
	public PopUp(String title, String msg) {
		setTitle(title);
		VBox y = new VBox();

		y.getChildren().add(new Label(msg));
		y.setAlignment(Pos.CENTER);
		setScene(new Scene(y, 300, 100));
		show();
	}
	
	/*
	 * Creates a popup with an input box
	 */
	public PopUp(String title) {
		setTitle(title);
		VBox y = new VBox();
		Label homeWarningLabel = new Label("Make sure to enter home point first in map!");
		homeWarningLabel.setTextFill(Color.RED);
		inputBox = new TextField("Enter name of delivery point...");
		
		y.getChildren().addAll(homeWarningLabel, inputBox);
		y.setAlignment(Pos.CENTER);
		setScene(new Scene(y, 300, 100));
		show();
	}
	
	public TextField getInputBox() {
		return inputBox;
	}

}
