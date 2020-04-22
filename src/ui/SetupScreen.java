package ui;

import java.io.File;

import droneSim.Food;
import droneSim.Meal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

public class SetupScreen {

	private UI_Setup ui_Setup;

	private ListView<Meal> listView;
	private CustomMealListView mealListView;
	
	private VBox vBox;

	public SetupScreen(UI_Setup ui_Setup) {
		this.ui_Setup = ui_Setup;
	}
	
	public void makeSetupScreen() {
		ui_Setup.curSetup.loadDefaultDroneSettings();

		// -----------------------------------------
		// SETUP PAGE
		// -----------------------------------------
		
		// create the color for the background
		BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
		// make the actual background to be used
		Background applicationBackground = new Background(applicationColor);

		//
		double screenW = ui_Setup.SCREEN_WIDTH;
		double screenH = ui_Setup.SCREEN_HEIGHT;
		int insets = 15; // PADDING FOR SCREEN
		int numColumns = 4; // NUMBER OF COLUMNS FOR GRIDPANE
		int numRows = 12; // NUMBER OF ROWS FOR GRIDPANE
		double colW = screenW / numColumns; // COL WIDTH
		double rowH = screenH / numRows; // ROW WIDTH

		GridPane screenLayoutSetup = new GridPane(); // main layout

		screenLayoutSetup.setPadding(new Insets(insets));
		screenLayoutSetup.setVgap(5);
		screenLayoutSetup.setHgap(5);

		// set column widths
		for (int i = 0; i < numColumns; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(25);
			screenLayoutSetup.getColumnConstraints().add(column);
		}

		// set column heights
		for (int i = 0; i < numRows; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100.0 / numRows);
			screenLayoutSetup.getRowConstraints().add(row);
		}

		// ------------------
		// Screen Objects
		// ------------------

		// ---------------------------------
		// BUTTON'S SETUP
		// ---------------------------------
		Button mainPageButton = new Button("BACK");
		Button createFoodButton = new Button("CREATE FOOD");
		Button addFoodButton = new Button("ADD FOOD");
		Button addMealButton = new Button("ADD MEAL");
		Button clearMealButton = new Button("CLEAR MEAL");
		Button removeSelectedMealButton = new Button("REMOVE SELECTED");
		Button editSelectedButton = new Button("EDIT SELECTED");

		mainPageButton.setPrefSize(150, 50);
		createFoodButton.setPrefSize(150, 50);
		addFoodButton.setPrefSize(150, 50);
		addMealButton.setPrefSize(150, 50);
		clearMealButton.setPrefSize(150, 50);
		removeSelectedMealButton.setPrefSize(150, 50);
		editSelectedButton.setPrefSize(150, 50);

		// ---------------------------------
		// TEXT FIELD'S SETUP
		// ---------------------------------
		TextField foodNameTextField = new TextField();
		TextField foodWeightTextField = new TextField();
		TextField mealProbabilityTextField = new TextField();
		TextField mealNameTextField = new TextField();

		foodNameTextField.setPromptText("Food Name");
		foodWeightTextField.setPromptText("Food Weight (oz)");
		mealProbabilityTextField.setPromptText("Meal Probability");
		mealNameTextField.setPromptText("Meal Name");

		foodNameTextField.setMaxWidth(1.5 * colW);
		foodWeightTextField.setMaxWidth(1.5 * colW);
		mealProbabilityTextField.setMaxWidth(150);
		mealNameTextField.setMaxWidth(150);

		// ---------------------------------
		// COMBO BOX SETUP (FOR MEAL CREATION)
		// ---------------------------------
		// FOOD SELECTION
		ObservableList<String> options = FXCollections.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
		ComboBox<String> foodOptionsComboBox = new ComboBox<>(options);
		foodOptionsComboBox.setPromptText("SELECT FOOD");
		foodOptionsComboBox.setMaxWidth(150);

		// FOOD QUANTITY
		ObservableList<Integer> qoptions = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
				14, 15);
		ComboBox<Integer> foodQuantityComboBox = new ComboBox<>(qoptions);
		foodQuantityComboBox.setPromptText("QUANTITY");
		foodQuantityComboBox.setMaxWidth(150);

		// ---------------------------------
		// TEXT AREA'S SETUP
		// ---------------------------------
		// for meal creator
		TextArea mealCreaterTextArea = new TextArea();
		mealCreaterTextArea.setMaxWidth(colW);
		mealCreaterTextArea.setEditable(false);
		mealCreaterTextArea.setPromptText("Your Custom Meal...");
		mealCreaterTextArea.setMaxHeight(4 * rowH);

		// ---------------------------------
		// List View SETUP
		// ---------------------------------
		mealListView = new CustomMealListView();
		mealListView.addMeal(ui_Setup.curSetup.getAllMeals());
		listView = mealListView.getListView();

		//listView.setMaxWidth(2 * colW);
		//listView.setMaxHeight(12 * rowH);

		// ---------------------------------
		// LABEL'S SETUP
		// ---------------------------------
		Label mealLabel = new Label("CURRENT MEALS");
		Label inMealLabel = new Label("CUSTOM MEAL");

		mealLabel.setFont(new Font("Arial", 18));
		inMealLabel.setFont(new Font("Arial", 18));

		mealLabel.setTextFill(Color.WHITE);
		inMealLabel.setTextFill(Color.WHITE);

		// buttons action:
		// ---------------------------------
		// MAIN PAGE BUTTON EVENT
		// ---------------------------------
		mainPageButton.setOnAction(e -> ui_Setup.window.setScene(ui_Setup.mainScene)); // go back to main screen

		// ---------------------------------
		// CREATE FOOD BUTTON EVENT
		// ---------------------------------
		createFoodButton.setOnAction(e -> {
			try {
				// parse new food
				String name = foodNameTextField.getText();
				int weight = Integer.parseInt(foodWeightTextField.getText());
				Food food = new Food(name, weight);
				ui_Setup.curSetup.addFood(food);

				// update combo box
				ObservableList<String> foodOptions = FXCollections
						.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
				foodOptionsComboBox.setItems(foodOptions);

				foodNameTextField.setText("");
				foodWeightTextField.setText("");
			} catch (Exception e2) {
				foodWeightTextField.setText(""); // if not an integer placed
			}
		});

		// ---------------------------------
		// ADD FOOD BUTTON EVENT
		// ---------------------------------
		addFoodButton.setOnAction(e -> {
			if (foodOptionsComboBox.getValue() != null && foodQuantityComboBox.getValue() != null) {
				try {
					String mealName = foodOptionsComboBox.getValue();
					mealName = mealName.substring(0, mealName.lastIndexOf("-")); // get rid of weight
					int mealQuantity = foodQuantityComboBox.getValue();
					mealCreaterTextArea.appendText(mealQuantity + "x\t- " + mealName);
					mealCreaterTextArea.appendText("\n\n");

					// TODO reset combobox
					foodQuantityComboBox.setValue(1);

					// update combo box
					ObservableList<String> foodOptions = FXCollections
							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);

				} catch (Exception e2) {
					System.err.println(e2.getMessage());
				}

			}
		});
		clearMealButton.setOnAction(e -> {
			mealCreaterTextArea.setText("");
		});

		// ---------------------------------
		// ADD MEAL BUTTON EVENT
		// ---------------------------------
		addMealButton.setOnAction(e -> {
			String inTXT = mealCreaterTextArea.getText();
			try {

				if (inTXT.length() > 0 && mealProbabilityTextField.getText() != null
						&& mealNameTextField.getText() != null) {
					// parse out meals
					String[] lines = inTXT.split("\n\n");

					// get name and then clear input box
					String mealName = mealNameTextField.getText();
					mealNameTextField.clear();
					// get probability and then clear input box
					double rawProb = Double.parseDouble(mealProbabilityTextField.getText());
					mealProbabilityTextField.clear();
					Meal newMeal = new Meal(mealName, rawProb);

					for (String curS : lines) {
						int quantity = Integer.parseInt(curS.substring(0, curS.indexOf("x")));
						String name = curS.substring(curS.indexOf("-") + 2, curS.length()).trim();

						// System.out.println(curSetup.getAllFoods());
						for (Food food : ui_Setup.curSetup.getAllFoods()) { // find the right food to add to meal
							if (name.equals(food.getName())) {
								for (int i = 0; i < quantity; i++) { // add the given quantity of food to meal
									newMeal.addFood(food);
									// System.out.println("food added");
								}
							}
						}

					}

					if (newMeal.getWeight() > ui_Setup.curSetup.getDroneWeight()) { // if the meal weighs more than the
																					// drone can
						// carry
						new PopUp("ERROR:",
								"Meal weight is greater than the Drone weight capacity.\n" + "\t- Meal Weight: "
										+ newMeal.getWeight() + " (oz)\n" + "\t- Drone Capacity: "
										+ ui_Setup.curSetup.getDroneWeight() + " (oz)");
					} else {
						// add meal
						ui_Setup.curSetup.addMeal(newMeal);
						ui_Setup.curSetup.adjustMealProbabilities();

						// update output
						mealListView.addMeal(newMeal);
					}

				}

			} catch (Exception e3) {
				System.err.println(e3.getMessage());
			}
			// Clear entered area for last created meal
			mealCreaterTextArea.setText("");
		});
		
		// ---------------------------------
		// EDIT/REMOVE ContextMenu EVENT
		// ---------------------------------		
        // Create ContextMenu
		EditMealScreen editMealScreen = new EditMealScreen(ui_Setup);
        ContextMenu contextMenu = new ContextMenu();
 
        MenuItem item1 = new MenuItem("Edit");
        item1.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	Meal selectedMeal = mealListView.getSelected();
        		if (selectedMeal != null) { // if a meal was selected
        			editMealScreen.makeScreen(selectedMeal);
    				refreshMealListView();
    			}
            }
        });
        MenuItem item2 = new MenuItem("Delete");
        item2.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
            	Meal removedMeal = mealListView.removeSelected();
        		if (removedMeal != null) { // if a meal was selected
    				ui_Setup.curSetup.deleteMeal(removedMeal);
    				ui_Setup.curSetup.adjustMealProbabilities();
    				refreshMealListView();
    			}
            }
        });
 
        // Add MenuItem to ContextMenu
        contextMenu.getItems().addAll(item1, item2);
 
        // When user right-click on List View of meals
        listView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
 
            @Override
            public void handle(ContextMenuEvent event) {
 
                contextMenu.show(ui_Setup.window, event.getScreenX(), event.getScreenY());
            }
        });
        

		// ------------------
		// left area
		// ------------------
		// add labels for meal/prob
		screenLayoutSetup.add(mealLabel, 0, 0, 2, 1);

		// add list view
		screenLayoutSetup.add(listView, 0, 1, 2, 10);
		
		// add hBox for (back, edit, remove) buttons
		
		HBox buttonHBox = new HBox(mainPageButton, removeSelectedMealButton, editSelectedButton);
		buttonHBox.setAlignment(Pos.CENTER);
		buttonHBox.setSpacing(5);

		HBox.setHgrow(mainPageButton, Priority.ALWAYS);
		HBox.setHgrow(removeSelectedMealButton, Priority.ALWAYS);
		HBox.setHgrow(editSelectedButton, Priority.ALWAYS);
		
		
		
		// add the hBox
		screenLayoutSetup.add(buttonHBox, 0, 11, 2, 1);

//		// add back button
//		screenLayoutSetup.add(mainPageButton, 0, 11);
//
//		// add remove meal button
//		screenLayoutSetup.add(removeSelectedMealButton, 1, 11);

		// set their allignment
		GridPane.setHalignment(mealLabel, HPos.CENTER);
		GridPane.setHalignment(mainPageButton, HPos.LEFT);
		GridPane.setHalignment(removeSelectedMealButton, HPos.RIGHT);

		// ------------------
		// right side
		// ------------------
		screenLayoutSetup.add(new Label(), 2, 0);

		screenLayoutSetup.add(foodNameTextField, 2, 1, 2, 1);
		screenLayoutSetup.add(foodWeightTextField, 2, 2, 2, 1);
		GridPane.setHalignment(foodNameTextField, HPos.CENTER);
		GridPane.setHalignment(foodWeightTextField, HPos.CENTER);

		screenLayoutSetup.add(createFoodButton, 2, 3, 2, 1);
		GridPane.setHalignment(createFoodButton, HPos.CENTER);

		// add some meal stuff
		screenLayoutSetup.add(inMealLabel, 3, 5);
		GridPane.setHalignment(inMealLabel, HPos.CENTER);
		screenLayoutSetup.add(mealCreaterTextArea, 3, 6, 1, 4);

		screenLayoutSetup.add(foodOptionsComboBox, 2, 5);
		screenLayoutSetup.add(foodQuantityComboBox, 2, 6);
		screenLayoutSetup.add(addFoodButton, 2, 7);
		screenLayoutSetup.add(mealNameTextField, 2, 8);
		screenLayoutSetup.add(mealProbabilityTextField, 2, 9);
		GridPane.setHalignment(foodOptionsComboBox, HPos.CENTER);
		GridPane.setHalignment(foodQuantityComboBox, HPos.CENTER);
		GridPane.setHalignment(addFoodButton, HPos.CENTER);
		GridPane.setHalignment(mealProbabilityTextField, HPos.CENTER);
		GridPane.setHalignment(mealNameTextField, HPos.CENTER);

		screenLayoutSetup.add(addMealButton, 2, 10);
		screenLayoutSetup.add(clearMealButton, 3, 10);
		GridPane.setHalignment(addMealButton, HPos.CENTER);
		GridPane.setHalignment(clearMealButton, HPos.CENTER);

		// ------------------
		// spacer
		// ------------------
		//

		// menu
		// create a menu
		Menu m = new Menu("File");

		// create menuitems
		String saveS = "Save Settings";
		String loadS = "Load Settings";
		String defaultS = "Default Settings";
		String clearS = "Clear Meals";
		MenuItem m1 = new MenuItem(saveS);
		MenuItem m2 = new MenuItem(loadS);
		MenuItem m3 = new MenuItem(defaultS);
		MenuItem m4 = new MenuItem(clearS);

		// create events for menu items
		// action event
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				String selectedS = ((MenuItem) e.getSource()).getText();
				if (selectedS.equals(saveS)) {

					FileChooser fileChooser = new FileChooser();

					// Set extension filter
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)",
							"*.csv");
					fileChooser.getExtensionFilters().add(extFilter);

					// Show save file dialog
					File file = fileChooser.showSaveDialog(ui_Setup.window);

					if (file != null) {
						ui_Setup.curSetup.saveFoodSettings(file);
					}

				} else if (selectedS.equals(loadS)) {

					FileChooser fileChooser = new FileChooser();
					FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("CSV files (*.csv)",
							"*.csv");
					fileChooser.getExtensionFilters().add(fileExtensions);

					File selectedFile = fileChooser.showOpenDialog(ui_Setup.window);

					if (selectedFile != null) { // if a file was selected
						ui_Setup.curSetup.loadFoodSettings(selectedFile); // load the file

					}

					// update combo box
					ObservableList<String> foodOptions = FXCollections
							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);
					// reset built meal
					mealCreaterTextArea.setText("");

				} else if (selectedS.equals(defaultS)) {
					ui_Setup.curSetup.loadDefaultFoodSettings();
					// update combo box
					ObservableList<String> foodOptions = FXCollections
							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);
					// reset built meal
					mealCreaterTextArea.setText("");
				} else if (selectedS.equals(clearS)) {
					ui_Setup.curSetup.clearFoodsAndMeals();
					// update combo box
					ObservableList<String> foodOptions = FXCollections
							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);
					// reset built meal
					mealCreaterTextArea.setText("");
				}

				// reload screen stuff
				mealProbabilityTextField.clear();
				mealListView.clearListView();
				mealListView.addMeal(ui_Setup.curSetup.getAllMeals());

			}
		};

		// add event
		m1.setOnAction(event);
		m2.setOnAction(event);
		m3.setOnAction(event);
		m4.setOnAction(event);

		// add menu items to menu
		m.getItems().add(m1);
		m.getItems().add(m2);
		m.getItems().add(m3);
		m.getItems().add(m4);
		// create a menubar
		MenuBar mb = new MenuBar();

		// add menu to menubar
		mb.getMenus().add(m);

		vBox = new VBox(mb, screenLayoutSetup);
		vBox.setBackground(applicationBackground);
		VBox.setVgrow(screenLayoutSetup, Priority.ALWAYS);

		ui_Setup.setupScene = new Scene(vBox, screenW, screenH);
		
		ui_Setup.setupScene.getStylesheets().add("SetupPage.css");

	}
	
	public void refreshMealListView() {
		mealListView.clearListView();
		mealListView.addMeal(ui_Setup.curSetup.getAllMeals());
	}
}


//	public void makeSetupScreen() {
//		ui_Setup.curSetup.loadDefaultDroneSettings();
//
//		// -----------------------------------------
//		// SETUP PAGE
//		// -----------------------------------------
//		// 5 columns wide
//		// first 2 are meal/prob
//		// next 2 are meal items/possible foods
//
//		// create the color for the background
//		BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
//		// make the actual background to be used
//		Background applicationBackground = new Background(applicationColor);
//
//		//
//		double screenW = ui_Setup.SCREEN_WIDTH;
//		double screenH = ui_Setup.SCREEN_HEIGHT;
//		int insets = 15; // PADDING FOR SCREEN
//		int numColumns = 4; // NUMBER OF COLUMNS FOR GRIDPANE
//		int numRows = 15; // NUMBER OF ROWS FOR GRIDPANE
//		double colW = screenW / numColumns - 11; // COL WIDTH
//		double rowH = screenH / numRows - insets / numRows; // ROW WIDTH
//
//		GridPane screenLayoutSetup = new GridPane(); // main layout
//		screenLayoutSetup.setMaxWidth(colW * numColumns - 2 * insets);
//		screenLayoutSetup.setMaxHeight(rowH * numRows - 2 * insets);
//		screenLayoutSetup.setPadding(new Insets(insets));
//		screenLayoutSetup.setVgap(5);
//		screenLayoutSetup.setHgap(5);
//		screenLayoutSetup.setBackground(applicationBackground);
//
//		// set column widths
//		for (int i = 0; i < numColumns; i++) {
//
//			ColumnConstraints column = new ColumnConstraints(colW);
//			screenLayoutSetup.getColumnConstraints().add(column);
//
//		}
//
//		// set column heights
//		for (int i = 0; i < numRows; i++) {
//			RowConstraints row = new RowConstraints(rowH);
//			screenLayoutSetup.getRowConstraints().add(row);
//		}
//
//		// ------------------
//		// Screen Objects
//		// ------------------
//
//		// ---------------------------------
//		// BUTTON'S SETUP
//		// ---------------------------------
//		Button mainPageButton = new Button("BACK");
//		Button createFoodButton = new Button("CREATE FOOD");
//		Button addFoodButton = new Button("ADD FOOD");
//		Button addMealButton = new Button("ADD MEAL");
//		Button clearMealButton = new Button("CLEAR MEAL");
//		Button removeSelectedMealButton = new Button("REMOVE SELECTED");
//
//		mainPageButton.setMaxSize(150, 50);
//		createFoodButton.setMaxSize(150, 50);
//		addFoodButton.setMaxSize(150, 50);
//		addMealButton.setMaxSize(150, 50);
//		clearMealButton.setMaxSize(150, 50);
//		removeSelectedMealButton.setMaxSize(150, 50);
//
//		// ---------------------------------
//		// TEXT FIELD'S SETUP
//		// ---------------------------------
//		TextField foodNameTextField = new TextField();
//		TextField foodWeightTextField = new TextField();
//		TextField mealProbabilityTextField = new TextField();
//		TextField mealNameTextField = new TextField();
//
//		foodNameTextField.setPromptText("Food Name");
//		foodWeightTextField.setPromptText("Food Weight (oz)");
//		mealProbabilityTextField.setPromptText("Meal Probability");
//		mealNameTextField.setPromptText("Meal Name");
//
//		foodNameTextField.setMaxWidth(1.5 * colW);
//		foodWeightTextField.setMaxWidth(1.5 * colW);
//		mealProbabilityTextField.setMaxWidth(150);
//		mealNameTextField.setMaxWidth(150);
//
//		// ---------------------------------
//		// COMBO BOX SETUP (FOR MEAL CREATION)
//		// ---------------------------------
//		// FOOD SELECTION
//		ObservableList<String> options = FXCollections.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
//		ComboBox<String> foodOptionsComboBox = new ComboBox<>(options);
//		foodOptionsComboBox.setPromptText("SELECT FOOD");
//		foodOptionsComboBox.setMaxWidth(150);
//
//		// FOOD QUANTITY
//		ObservableList<Integer> qoptions = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
//				14, 15);
//		ComboBox<Integer> foodQuantityComboBox = new ComboBox<>(qoptions);
//		foodQuantityComboBox.setPromptText("QUANTITY");
//		foodQuantityComboBox.setMaxWidth(150);
//
//		// ---------------------------------
//		// TEXT AREA'S SETUP
//		// ---------------------------------
//		// for meal creator
//		TextArea mealCreaterTextArea = new TextArea();
//		mealCreaterTextArea.setMaxWidth(colW);
//		mealCreaterTextArea.setEditable(false);
//		mealCreaterTextArea.setPromptText("Your Custom Meal...");
//		mealCreaterTextArea.setMaxHeight(4 * rowH);
//
//		// ---------------------------------
//		// List View SETUP
//		// ---------------------------------
//		CustomMealListView mealListView = new CustomMealListView();
//		mealListView.addMeal(ui_Setup.curSetup.getAllMeals());
//		listView = mealListView.getListView();
//
//		listView.setMaxWidth(2 * colW);
//		listView.setMaxHeight(12 * rowH);
//
//		// ---------------------------------
//		// LABEL'S SETUP
//		// ---------------------------------
//		Label mealLabel = new Label("CURRENT MEALS");
//		Label inMealLabel = new Label("CUSTOM MEAL");
//
//		mealLabel.setFont(new Font("Arial", 18));
//		inMealLabel.setFont(new Font("Arial", 18));
//
//		mealLabel.setTextFill(Color.WHITE);
//		inMealLabel.setTextFill(Color.WHITE);
//
//		// buttons action:
//		// ---------------------------------
//		// MAIN PAGE BUTTON EVENT
//		// ---------------------------------
//		mainPageButton.setOnAction(e -> ui_Setup.window.setScene(ui_Setup.mainScene)); // go back to main screen
//
//		// ---------------------------------
//		// CREATE FOOD BUTTON EVENT
//		// ---------------------------------
//		createFoodButton.setOnAction(e -> {
//			try {
//				// parse new food
//				String name = foodNameTextField.getText();
//				int weight = Integer.parseInt(foodWeightTextField.getText());
//				Food food = new Food(name, weight);
//				ui_Setup.curSetup.addFood(food);
//
//				// update combo box
//				ObservableList<String> foodOptions = FXCollections
//						.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
//				foodOptionsComboBox.setItems(foodOptions);
//
//				foodNameTextField.setText("");
//				foodWeightTextField.setText("");
//			} catch (Exception e2) {
//				foodWeightTextField.setText(""); // if not an integer placed
//			}
//		});
//
//		// ---------------------------------
//		// ADD FOOD BUTTON EVENT
//		// ---------------------------------
//		addFoodButton.setOnAction(e -> {
//			if (foodOptionsComboBox.getValue() != null && foodQuantityComboBox.getValue() != null) {
//				try {
//					String mealName = foodOptionsComboBox.getValue();
//					mealName = mealName.substring(0, mealName.lastIndexOf("-")); // get rid of weight
//					int mealQuantity = foodQuantityComboBox.getValue();
//					mealCreaterTextArea.appendText(mealQuantity + "x\t- " + mealName);
//					mealCreaterTextArea.appendText("\n\n");
//
//					// TODO reset combobox
//					foodQuantityComboBox.setValue(1);
//
//					// update combo box
//					ObservableList<String> foodOptions = FXCollections
//							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
//					foodOptionsComboBox.setItems(foodOptions);
//
//				} catch (Exception e2) {
//					System.err.println(e2.getMessage());
//				}
//
//			}
//		});
//		clearMealButton.setOnAction(e -> {
//			mealCreaterTextArea.setText("");
//		});
//
//		// ---------------------------------
//		// ADD MEAL BUTTON EVENT
//		// ---------------------------------
//		addMealButton.setOnAction(e -> {
//			String inTXT = mealCreaterTextArea.getText();
//			try {
//
//				if (inTXT.length() > 0 && mealProbabilityTextField.getText() != null
//						&& mealNameTextField.getText() != null) {
//					// parse out meals
//					String[] lines = inTXT.split("\n\n");
//
//					// get name and then clear input box
//					String mealName = mealNameTextField.getText();
//					mealNameTextField.clear();
//					// get probability and then clear input box
//					double rawProb = Double.parseDouble(mealProbabilityTextField.getText());
//					mealProbabilityTextField.clear();
//					Meal newMeal = new Meal(mealName, rawProb);
//
//					for (String curS : lines) {
//						int quantity = Integer.parseInt(curS.substring(0, curS.indexOf("x")));
//						String name = curS.substring(curS.indexOf("-") + 2, curS.length()).trim();
//
//						// System.out.println(curSetup.getAllFoods());
//						for (Food food : ui_Setup.curSetup.getAllFoods()) { // find the right food to add to meal
//							if (name.equals(food.getName())) {
//								for (int i = 0; i < quantity; i++) { // add the given quantity of food to meal
//									newMeal.addFood(food);
//									// System.out.println("food added");
//								}
//							}
//						}
//
//					}
//
//					if (newMeal.getWeight() > ui_Setup.curSetup.getDroneWeight()) { // if the meal weighs more than the
//																					// drone can
//						// carry
//						new PopUp("ERROR:",
//								"Meal weight is greater than the Drone weight capacity.\n" + "\t- Meal Weight: "
//										+ newMeal.getWeight() + " (oz)\n" + "\t- Drone Capacity: "
//										+ ui_Setup.curSetup.getDroneWeight() + " (oz)");
//					} else {
//						// add meal
//						ui_Setup.curSetup.addMeal(newMeal);
//						ui_Setup.curSetup.adjustMealProbabilities();
//
//						// update output
//						mealListView.addMeal(newMeal);
//					}
//
//				}
//
//			} catch (Exception e3) {
//				System.err.println(e3.getMessage());
//			}
//			// Clear entered area for last created meal
//			mealCreaterTextArea.setText("");
//		});
//
//		// ------------------
//		// left area
//		// ------------------
//		// add labels for meal/prob
//		screenLayoutSetup.add(mealLabel, 0, 0, 2, 1);
//
//		// add text area
//		screenLayoutSetup.add(listView, 0, 1, 2, 10);
//
//		// add back button
//		screenLayoutSetup.add(mainPageButton, 0, 11);
//
//		// add remove meal button
//		screenLayoutSetup.add(removeSelectedMealButton, 1, 11);
//
//		// set their allignment
//		GridPane.setHalignment(mealLabel, HPos.CENTER);
//		GridPane.setHalignment(mainPageButton, HPos.LEFT);
//		GridPane.setHalignment(removeSelectedMealButton, HPos.RIGHT);
//
//		// ------------------
//		// right side
//		// ------------------
//		screenLayoutSetup.add(new Label(), 2, 0);
//
//		screenLayoutSetup.add(foodNameTextField, 2, 1, 2, 1);
//		screenLayoutSetup.add(foodWeightTextField, 2, 2, 2, 1);
//		GridPane.setHalignment(foodNameTextField, HPos.CENTER);
//		GridPane.setHalignment(foodWeightTextField, HPos.CENTER);
//
//		screenLayoutSetup.add(createFoodButton, 2, 3, 2, 1);
//		GridPane.setHalignment(createFoodButton, HPos.CENTER);
//
//		// add some meal stuff
//		screenLayoutSetup.add(inMealLabel, 3, 5);
//		GridPane.setHalignment(inMealLabel, HPos.CENTER);
//		screenLayoutSetup.add(mealCreaterTextArea, 3, 6, 1, 4);
//
//		screenLayoutSetup.add(foodOptionsComboBox, 2, 5);
//		screenLayoutSetup.add(foodQuantityComboBox, 2, 6);
//		screenLayoutSetup.add(addFoodButton, 2, 7);
//		screenLayoutSetup.add(mealNameTextField, 2, 8);
//		screenLayoutSetup.add(mealProbabilityTextField, 2, 9);
//		GridPane.setHalignment(foodOptionsComboBox, HPos.CENTER);
//		GridPane.setHalignment(foodQuantityComboBox, HPos.CENTER);
//		GridPane.setHalignment(addFoodButton, HPos.CENTER);
//		GridPane.setHalignment(mealProbabilityTextField, HPos.CENTER);
//		GridPane.setHalignment(mealNameTextField, HPos.CENTER);
//
//		screenLayoutSetup.add(addMealButton, 2, 10);
//		screenLayoutSetup.add(clearMealButton, 3, 10);
//		GridPane.setHalignment(addMealButton, HPos.CENTER);
//		GridPane.setHalignment(clearMealButton, HPos.CENTER);
//
//		// ------------------
//		// spacer
//		// ------------------
//		//
//
//		// menu
//		// create a menu
//		Menu m = new Menu("File");
//
//		// create menuitems
//		String saveS = "Save Settings";
//		String loadS = "Load Settings";
//		String defaultS = "Default Settings";
//		String clearS = "Clear Meals";
//		MenuItem m1 = new MenuItem(saveS);
//		MenuItem m2 = new MenuItem(loadS);
//		MenuItem m3 = new MenuItem(defaultS);
//		MenuItem m4 = new MenuItem(clearS);
//
//		// create events for menu items
//		// action event
//		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
//			public void handle(ActionEvent e) {
//				String selectedS = ((MenuItem) e.getSource()).getText();
//				if (selectedS.equals(saveS)) {
//
//					FileChooser fileChooser = new FileChooser();
//
//					// Set extension filter
//					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)",
//							"*.csv");
//					fileChooser.getExtensionFilters().add(extFilter);
//
//					// Show save file dialog
//					File file = fileChooser.showSaveDialog(ui_Setup.window);
//
//					if (file != null) {
//						ui_Setup.curSetup.saveFoodSettings(file);
//					}
//
//				} else if (selectedS.equals(loadS)) {
//
//					FileChooser fileChooser = new FileChooser();
//					FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("CSV files (*.csv)",
//							"*.csv");
//					fileChooser.getExtensionFilters().add(fileExtensions);
//
//					File selectedFile = fileChooser.showOpenDialog(ui_Setup.window);
//
//					if (selectedFile != null) { // if a file was selected
//						ui_Setup.curSetup.loadFoodSettings(selectedFile); // load the file
//
//					}
//
//					// update combo box
//					ObservableList<String> foodOptions = FXCollections
//							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
//					foodOptionsComboBox.setItems(foodOptions);
//					// reset built meal
//					mealCreaterTextArea.setText("");
//
//				} else if (selectedS.equals(defaultS)) {
//					ui_Setup.curSetup.loadDefaultFoodSettings();
//					// update combo box
//					ObservableList<String> foodOptions = FXCollections
//							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
//					foodOptionsComboBox.setItems(foodOptions);
//					// reset built meal
//					mealCreaterTextArea.setText("");
//				} else if (selectedS.equals(clearS)) {
//					ui_Setup.curSetup.clearFoodsAndMeals();
//					// update combo box
//					ObservableList<String> foodOptions = FXCollections
//							.observableArrayList(ui_Setup.curSetup.getAllFoodNames());
//					foodOptionsComboBox.setItems(foodOptions);
//					// reset built meal
//					mealCreaterTextArea.setText("");
//				}
//
//				// reload screen stuff
//				mealProbabilityTextField.clear();
//				mealListView.clearListView();
//				mealListView.addMeal(ui_Setup.curSetup.getAllMeals());
//
//			}
//		};
//
//		// add event
//		m1.setOnAction(event);
//		m2.setOnAction(event);
//		m3.setOnAction(event);
//		m4.setOnAction(event);
//
//		// add menu items to menu
//		m.getItems().add(m1);
//		m.getItems().add(m2);
//		m.getItems().add(m3);
//		m.getItems().add(m4);
//		// create a menubar
//		MenuBar mb = new MenuBar();
//
//		// add menu to menubar
//		mb.getMenus().add(m);
//
//		VBox vBox = new VBox(mb, screenLayoutSetup);
//
//		ui_Setup.setupScene = new Scene(vBox, screenW, screenH);
//	}
//}
