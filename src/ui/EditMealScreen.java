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
	private Label weightLabel, scaledProbabilityLabel, arrowLabel;
	private HBox probabilityHBox;

	public EditMealScreen(UI_Setup ui_Setup) {
		this.ui_Setup = ui_Setup;
	}

	public void makeScreen(Meal selectedMeal) {
		this.selectedMeal = selectedMeal;
		setTitle(editMealTitle);

		/*
		 * Vbox
		 * { 
		 * 	name 
		 * 	raw prob -> scaled prob 
		 * 	weight
		 * 
		 * 	Hbox 
		 * 	{
		 * 
		 * 		meal contents, Selection's, Possible Foods
		 * 
		 * 	} 
		 * }
		 */
		
		makeNameTextField(selectedMeal);

		makeEditProbabilityTextField(selectedMeal);
		arrowLabel = new Label(" → ");
		arrowLabel.setMaxWidth(20);
		arrowLabel.setPrefWidth(20);
		scaledProbabilityLabel = new Label(selectedMeal.getScaledProbability()*100.0 + "%");
		scaledProbabilityLabel.setMaxWidth(140);
		scaledProbabilityLabel.setPrefWidth(140);
		probabilityHBox = new HBox(editProbabilityTF, arrowLabel, scaledProbabilityLabel);
		probabilityHBox.setAlignment(Pos.CENTER);
		
		makeWeightLabel();
		

		// possible foods
		VBox possibleVBox = makePossibleVBoxAndTable();

		// selected foods
		VBox selectedVBox = makeSelectedVBoxAndTable(selectedMeal);

		// add/remove buttons
		makeAddRemoveButtons();
		VBox selectionButtons = makeAddRemoveVBox();
		
		// put the bottom parts together into a HBox
		HBox selectionHBox = makeSelectionHBox(possibleVBox, selectedVBox, selectionButtons);

		// put it all together
		VBox screenBox = makeScreenVBox(selectionHBox);

		screenBox.setAlignment(Pos.CENTER);
		setScene(new Scene(screenBox, 600, 400));
		show();
	}

	/**
	 * @param selectionHBox
	 * @return
	 */
	private VBox makeScreenVBox(HBox selectionHBox) {
		VBox screenBox = new VBox(editNameTF, probabilityHBox, weightLabel, selectionHBox);
		screenBox.setSpacing(5);
		screenBox.setPadding(new Insets(5));
		updateWeightLabel();
		return screenBox;
	}

	/**
	 *  create and update the weight label
	 */
	private void makeWeightLabel() {
		weightLabel = new Label();
		weightLabel.setMaxWidth(300);
	}

	public void updateWeightLabel() {
		ObservableList<Food> foods = selectedFoodsTable.getItems();
		int weight = 0;
		if (foods != null) {
			for (Food food : foods) {
				weight += food.getWeight();
			}
		}
		
		weightLabel.setText("Weight: " + weight + " (oz)");
	}
	
	/**
	 * put the bottom parts together into a HBox
	 * @param possibleVBox
	 * @param selectedVBox
	 * @param selectionButtons
	 * @return
	 */
	private HBox makeSelectionHBox(VBox possibleVBox, VBox selectedVBox, VBox selectionButtons) {
		HBox selectionHBox = new HBox(selectedVBox, selectionButtons, possibleVBox);
		selectionHBox.setSpacing(5);
		selectionHBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(selectedVBox, Priority.ALWAYS);
		HBox.setHgrow(possibleVBox, Priority.ALWAYS);
		return selectionHBox;
	}

	/**
	 * @return
	 */
	private VBox makeAddRemoveVBox() {
		VBox selectionButtons = new VBox(new Label(""),add, remove);
		selectionButtons.setAlignment(Pos.CENTER);
		selectionButtons.setSpacing(5);
		return selectionButtons;
	}

	/**
	 * @param selectedMeal
	 * @return
	 */
	private VBox makeSelectedVBoxAndTable(Meal selectedMeal) {
		selectedFoodsTable = makeTable(selectedMeal.getFoodItems());
		VBox selectedVBox = new VBox(new Label("Selected Foods"), selectedFoodsTable);
		selectedVBox.setAlignment(Pos.CENTER);
		return selectedVBox;
	}

	/**
	 * @return
	 */
	private VBox makePossibleVBoxAndTable() {
		possibleFoodsTable = makeTable(ui_Setup.curSetup.getAllFoods());
		VBox possibleVBox = new VBox(new Label("Possible Foods"), possibleFoodsTable);
		possibleVBox.setAlignment(Pos.CENTER);
		return possibleVBox;
	}

	/**
	 * create the add and remove buttons
	 */
	private void makeAddRemoveButtons() {
		double sizeH = 10;
		double sizeW = 25;
		add = new Button("<");
		remove = new Button(">");
		add.setPrefSize(sizeW, sizeH);
		remove.setPrefSize(sizeW, sizeH);
		
		// what happens when add is clicked
		add.setOnAction(clickedItem -> {
			Food foodToAdd = possibleFoodsTable.getSelectionModel().getSelectedItem();
			if (possibleFoodsTable.getItems().size() > 0 && foodToAdd != null) {
				selectedFoodsTable.getItems().add(foodToAdd);
				updateWeightLabel();
			}
		});
		
		// what happens when remove is clicked
		remove.setOnAction(clickedItem -> {
			int selectedIndex = selectedFoodsTable.getSelectionModel().getSelectedIndex();
			if (selectedFoodsTable.getItems().size() > 0 && selectedIndex >= 0) {
				selectedFoodsTable.getItems().remove(selectedIndex);
				updateWeightLabel();
			}
		});
	}

	/**
	 * @param selectedMeal
	 */
	private void makeEditProbabilityTextField(Meal selectedMeal) {
		editProbabilityTF = new TextField();
		editProbabilityTF.setText(selectedMeal.getRawProbability() + "");
		editProbabilityTF.setMaxWidth(140);
		editProbabilityTF.setPrefWidth(140);
	}

	/**
	 * @param selectedMeal
	 */
	private void makeNameTextField(Meal selectedMeal) {
		editNameTF = new TextField();
		editNameTF.setText(selectedMeal.getName());
		editNameTF.setMaxWidth(300);
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
