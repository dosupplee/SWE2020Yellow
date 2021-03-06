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
	private Button add, remove, applyButton, cancelButton;
	private Label weightLabel, scaledProbabilityLabel, arrowLabel;
	private HBox probabilityHBox, bottomActionsHBox;
	private CustomMealListView mealListView;
	private PopUp customPopup;
	
	private Scene editMealScene;

	public EditMealScreen(UI_Setup ui_Setup, CustomMealListView mealListView) {
		this.ui_Setup = ui_Setup;
		this.mealListView = mealListView;
	}

	public void makeScreen(Meal selectedMeal) {
		this.selectedMeal = selectedMeal;
		setTitle(editMealTitle);

		/*
		 * Vbox { name raw prob -> scaled prob weight
		 * 
		 * Hbox {
		 * 
		 * meal contents, Selection's, Possible Foods
		 * 
		 * } }
		 */

		makeNameTextField(selectedMeal);

		makeProbabilityHBox(selectedMeal);

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

		// apply and cancel buttons
		makeApplyCancelButtons();
		makeBottomActinoHBox();

		// put it all together
		VBox screenBox = makeScreenVBox(selectionHBox);

		screenBox.setAlignment(Pos.CENTER);
		editMealScene = new Scene(screenBox, 600, 400);
		editMealScene.getStylesheets().add("EditMeal.css");
		setScene(editMealScene);
		show();
	}

	/**
	 * 
	 */
	private void makeBottomActinoHBox() {
		bottomActionsHBox = new HBox(applyButton, cancelButton);
		bottomActionsHBox.setAlignment(Pos.BOTTOM_RIGHT);
		bottomActionsHBox.setSpacing(5);
	}

	/**
	 * TODO empty meal
	 */
	private void makeApplyCancelButtons() {
		double sizeH = 10; // height of button
		double sizeW = 75; // width of button

		// apply button
		applyButton = new Button("APPLY");
		applyButton.setPrefSize(sizeW, sizeH);
		applyButton.setOnAction(clicked -> {

			Double probability = getProbability();
			if (isMealNameEntered()) {
				makePopup("Empty Name Error", "Please enter a name for your meal.");
			} else if (isProbabilityEnetered() || isValidProbabiltiy(probability)) {
				makePopup("Invalid Probability Error", "Please enter a valid number for your raw probibility. (# > 0)");
			} else {

				if (isMealEmpty()) {
					makePopup("Empty Meal Error", "Please add at least one food to your meal.");
				} else {

					//
					// makes sure new meal weight does not exceed the drones weight capacity
					//
					int weight = 0;
					for (Food food : selectedFoodsTable.getItems()) {
						weight += food.getWeight(); // add all the new foods into meal
					}
					if (exceedsDroneWeightCapacity(weight)) { // if the meal weighs more than the drone can carry
						makePopup("ERROR:",
								"Meal weight is greater than the Drone weight capacity.\n" + "\t- Meal Weight: "
										+ weight + " (oz)\n" + "\t- Drone Capacity: "
										+ ui_Setup.curSetup.getDroneWeight() + " (oz)");
					}

					//
					// - update the actual meal item with name, probability, and foods
					// - adjust the probabilities
					// - refresh the listview
					// - close the popup
					//
					else {
						selectedMeal.setName(editNameTF.getText());
						selectedMeal.setRawProbability(probability);

						// add the foods to meal
						selectedMeal.clearFoods(); // remove all foods
						for (Food food : selectedFoodsTable.getItems()) {
							selectedMeal.addFood(food);
						}

						ui_Setup.curSetup.adjustMealProbabilities(); // adjust for the new probability
						refreshMealListView(); // update the list view
						close(); // close the window
					}
				}
			}
		});

		// cancel button
		cancelButton = new Button("CANCEL");
		cancelButton.setPrefSize(sizeW, sizeH);
		cancelButton.setOnAction(clicked -> {
			close(); // close the window. Do no changes
		});
	}

	private void makePopup(String title, String msg) {
		if (customPopup != null && customPopup.isShowing()) {
			customPopup.close();
		}
		customPopup = new PopUp(title, msg);
	}

	/**
	 * @param weight
	 * @return
	 */
	private boolean exceedsDroneWeightCapacity(int weight) {
		return weight > ui_Setup.curSetup.getDroneWeight();
	}

	/**
	 * @param probability
	 * @return
	 */
	private boolean isValidProbabiltiy(Double probability) {
		return probability < 0.0;
	}

	/**
	 * @return
	 */
	private boolean isMealEmpty() {
		return selectedFoodsTable.getItems().size() <= 0;
	}

	/**
	 * returns -1 if malformed, else the given value
	 * 
	 * @return
	 */
	private Double getProbability() {
		Double probability = -1.0;

		try {
			probability = Double.parseDouble(editProbabilityTF.getText());
		} catch (Exception e) {
			// if number was malformed
		}
		return probability;
	}

	/**
	 * @return
	 */
	private boolean isProbabilityEnetered() {
		return editProbabilityTF.getText() == null || editProbabilityTF.getText().equals("");
	}

	/**
	 * @return
	 */
	private boolean isMealNameEntered() {
		return editNameTF.getText() == null || editNameTF.getText().equals("");
	}

	/**
	 * refresh the meal list view with new content
	 */
	public void refreshMealListView() {
		mealListView.clearListView();
		mealListView.addMeal(ui_Setup.curSetup.getAllMeals());
	}

	/**
	 * @param selectedMeal
	 */
	private void makeProbabilityHBox(Meal selectedMeal) {
		makeEditProbabilityTextField(selectedMeal);
		makeArrowLabel();
		maleScaledProbabilityLabel(selectedMeal);
		probabilityHBox = new HBox(editProbabilityTF, arrowLabel, scaledProbabilityLabel);
		probabilityHBox.setAlignment(Pos.CENTER);
	}

	/**
	 * @param selectedMeal
	 */
	private void maleScaledProbabilityLabel(Meal selectedMeal) {
		scaledProbabilityLabel = new Label(selectedMeal.getScaledProbability() * 100.0 + "%");
		scaledProbabilityLabel.setMaxWidth(140);
		scaledProbabilityLabel.setPrefWidth(140);
	}

	/**
	 * 
	 */
	private void makeArrowLabel() {
		arrowLabel = new Label(" → ");
		arrowLabel.setMaxWidth(20);
		arrowLabel.setPrefWidth(20);
	}

	/**
	 * @param selectionHBox
	 * @return
	 */
	private VBox makeScreenVBox(HBox selectionHBox) {
		VBox screenBox = new VBox(editNameTF, probabilityHBox, weightLabel, selectionHBox, bottomActionsHBox);
		screenBox.setSpacing(5);
		screenBox.setPadding(new Insets(5));
		updateWeightLabel();
		return screenBox;
	}

	/**
	 * create and update the weight label
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
	 * 
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
		VBox selectionButtons = new VBox(new Label(""), add, remove);
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

	String oldval; // for the edit probability to check if it actually changes

	/**
	 * @param selectedMeal
	 */
	private void makeEditProbabilityTextField(Meal selectedMeal) {
		editProbabilityTF = new TextField();
		editProbabilityTF.setText(selectedMeal.getRawProbability() + "");
		editProbabilityTF.setMaxWidth(140);
		editProbabilityTF.setPrefWidth(140);
		oldval = editProbabilityTF.getText();

		editProbabilityTF.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != newValue && !oldval.equals(editProbabilityTF.getText())) { // if the current text is not
																						// what was the last time
																						// selected
				Double probability = -1.0;
				try {
					probability = Double.parseDouble(editProbabilityTF.getText());
				} catch (Exception e) {
					// if number was malformed
				}

				if (isValidProbabiltiy(probability)) { // make sure propability is greater than 0
					makePopup("Invalid Probability Error",
							"Please enter a valid number for your raw probibility. (# > 0)");
				} else {
					double scaledProbability = ui_Setup.curSetup.adjustMealProbabilities(probability);
					scaledProbability *= 100;
					scaledProbabilityLabel.setText(String.format("%.2f%%", scaledProbability));
				}
				oldval = editProbabilityTF.getText();
			}

		});

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
	 * 
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
