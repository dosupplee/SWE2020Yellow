package ui;

import droneSim.Food;
import droneSim.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EditMealScreen extends Stage {
	
	private UI_Setup ui_Setup;
	private Meal selectedMeal;
	private final String editMealTitle = "Edit Selected Meal";
	private TextField editNameTF, editProbabilityTF;
	private ListView<Food> mealContentsListView;

	
	public EditMealScreen(UI_Setup ui_Setup) {
		this.ui_Setup = ui_Setup;
	}

	
	public void makeScreen(Meal selectedMeal) {
		this.selectedMeal = selectedMeal;
		setTitle(editMealTitle);
		
		editNameTF = new TextField();
		editNameTF.setText(selectedMeal.getName());
		
		editProbabilityTF = new TextField();
		editProbabilityTF.setText(selectedMeal.getRawProbability() + "");
		
		ObservableList<Food> foodsInMeal = FXCollections.observableArrayList(selectedMeal.getFoodItems());
		mealContentsListView = new ListView<Food>(foodsInMeal);
		
		
		VBox y = new VBox(editNameTF, editProbabilityTF, mealContentsListView);
		
		

		//y.getChildren().add(new Label(msg));
		y.setAlignment(Pos.CENTER);
		setScene(new Scene(y, 600, 400));
		show();
	}
}
