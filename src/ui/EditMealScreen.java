package ui;

import java.util.ArrayList;

import droneSim.Food;
import droneSim.Meal;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class EditMealScreen extends Stage {

	private UI_Setup ui_Setup;
	private Meal selectedMeal;
	private final String editMealTitle = "Edit Selected Meal";

	private TextField editNameTF, editProbabilityTF;
	private TableView<Food> possibleFoodsTable, selectedFoodsTable;
	private Button add, remove;

	public EditMealScreen(UI_Setup ui_Setup) {
		this.ui_Setup = ui_Setup;
	}

	public void makeScreen(Meal selectedMeal) {
		this.selectedMeal = selectedMeal;
		setTitle(editMealTitle);

		editNameTF = new TextField();
		editNameTF.setText(selectedMeal.getName());
		editNameTF.setMaxWidth(300);

		editProbabilityTF = new TextField();
		editProbabilityTF.setText(selectedMeal.getRawProbability() + "");
		editProbabilityTF.setMaxWidth(300);

		/*
		 * Vbox{ name raw prob -> scaled prob weight
		 * 
		 * Hbox {
		 * 
		 * meal contents, Selection's, Possible Foods
		 * 
		 * } 
		 * }
		 */

		// possible foods
		possibleFoodsTable = makeTable(ui_Setup.curSetup.getAllFoods());
		VBox possibleVBox = new VBox(new Label("Possible Foods"), possibleFoodsTable);
		possibleVBox.setAlignment(Pos.CENTER);

		// selected foods
		selectedFoodsTable = makeTable(selectedMeal.getFoodItems());
		VBox selectedVBox = new VBox(new Label("Selected Foods"), selectedFoodsTable);
		selectedVBox.setAlignment(Pos.CENTER);

		// add/remove buttons
		double sizeH = 10;
		double sizeW = 25;
		add = new Button("<");
		remove = new Button(">");
		add.setPrefSize(sizeW, sizeH);
		remove.setPrefSize(sizeW, sizeH);
		VBox selectionButtons = new VBox(new Label(""),add, remove);
		selectionButtons.setAlignment(Pos.CENTER);
		selectionButtons.setSpacing(5);
		
		// put the bottom parts together
		HBox selectionHBox = new HBox(selectedVBox, selectionButtons, possibleVBox);
		selectionHBox.setSpacing(5);
		selectionHBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(selectedVBox, Priority.ALWAYS);
		HBox.setHgrow(possibleVBox, Priority.ALWAYS);
		
		

		// put it all together
		VBox y = new VBox(editNameTF, editProbabilityTF, selectionHBox);
		y.setSpacing(5);
		y.setPadding(new Insets(5));

		y.setAlignment(Pos.CENTER);
		setScene(new Scene(y, 600, 400));
		show();
	}
	
	/**
	 * returns a table with two columns (name, weight)
	 * @param foods
	 * @return
	 */
	public TableView<Food> makeTable(ArrayList<Food> foods) {
		
		// create the table with it's foods
		ObservableList<Food> foodsList = FXCollections.observableArrayList(foods);
		TableView<Food> foodsTable = new TableView<Food>(foodsList);

		// make the columns
		TableColumn<Food, String> NameCol = new TableColumn<Food, String>("Food Name");
		TableColumn<Food, Integer> WeightCol = new TableColumn<Food, Integer>("Weight");

		// set the column cell factories
		NameCol.setCellValueFactory(e -> {
			Food food = e.getValue();
			String name = food.getName();

			return new ReadOnlyObjectWrapper<String>(name);
		});

		WeightCol.setCellValueFactory(e -> {
			Food food = e.getValue();
			Integer wt = food.getWeight();

			return new ReadOnlyObjectWrapper<Integer>(wt);
		});

		// add columns to table
		foodsTable.getColumns().add(NameCol);
		foodsTable.getColumns().add(WeightCol);
		
		// set table constraints
		foodsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		
		return foodsTable;
	}
}
