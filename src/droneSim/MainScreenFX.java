package droneSim;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;



/**
 * 
 * TODO - Only let user add foods/meals within drone carrying capacity?
 *
 */
public class MainScreenFX extends Application {

	public Stage window;
	public Scene mainScene, setupScene;
	private Runner runner;
	private CurrentSetup curSetup;
	
	private final int SCREEN_HEIGHT = 600;
	private final int SCREEN_WIDTH = 800;
	

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		runner = new Runner();
		curSetup = runner.getCurrentSetup();

		makeMainScreen();
		makeSetupScreen();

		window = stage;

		window.setScene(mainScene);
		window.setTitle("Dromedary Drones");
		window.show();
	}
	
	/**
	 *  get rid of all the old graph files
	 */
	@Override
	public void stop(){
	    try // get all the files in directory
	    {
	        String lscmd = "ls"; // bash cmd to print the files in current dir
	        Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", lscmd}); // execute the process
	        p.waitFor();
	        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
	        
	        // parse the output
	        String line=reader.readLine();
	        while(line!=null)
	        {
	            if (line.endsWith("_time graph.csv")) { // if it's a graph file
					File file = new File(line); // file to delete
					if (file.exists()) {
						file.delete(); // delete it 
					}
				}
	            line=reader.readLine();
	        }
	    }
	    catch(IOException e1) {
	        System.err.println("Pblm found1.");
	    }
	    catch(InterruptedException e2) {
	        System.err.println("Pblm found2.");
	    }

	}

	public void makeMainScreen() {

		// create the color for the background
		BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
		// make the actual background to be used
		Background applicationBackground = new Background(applicationColor);

		/*
		 * Main page screen setup
		 */
		double screenW = SCREEN_WIDTH;
		double screenH = SCREEN_HEIGHT;
		int insets = 15;
		int numColumns = 4;
		int numRows = 14;
		double colW = screenW / numColumns - 11;
		double rowH = screenH / numRows - insets / numRows;

		// layout for the main page
		GridPane screenLayoutMain = new GridPane();
		screenLayoutMain.setMaxWidth(colW * numColumns - 2 * insets);
		screenLayoutMain.setMaxHeight(rowH * numRows - 2 * insets);
		screenLayoutMain.setPadding(new Insets(insets * 2));
		screenLayoutMain.setHgap(8);
		screenLayoutMain.setVgap(8);
		screenLayoutMain.setBackground(applicationBackground);

		// set column widths
		for (int i = 0; i < numColumns; i++) {

			ColumnConstraints column = new ColumnConstraints(colW);
			screenLayoutMain.getColumnConstraints().add(column);

		}

		// set column heights
		for (int i = 0; i < numRows; i++) {
			RowConstraints row = new RowConstraints(rowH);
			screenLayoutMain.getRowConstraints().add(row);
		}

		// file chooser for selecting a saved setup
		FileChooser selectSetupFile = new FileChooser();
		// title for the file chooser
		selectSetupFile.setTitle("Select Saved Setup");

		// Main Page text
		Label fileName = new Label(curSetup.getCurrentMap().getMapName());
		Label avgTime = new Label("AVERAGE TIME:");
		Label slowestTime = new Label("SLOWEST TIME:");
		Label fastestTime = new Label("FASTEST TIME:");

		// Time textFields
		TextField fastFifoTextField = new TextField();
		TextField fastKnapTextField = new TextField();

		TextField slowFifoTextField = new TextField();
		TextField slowKnapTextField = new TextField();

		TextField avgFifoTextField = new TextField();
		TextField avgKnapTextField = new TextField();

		fastFifoTextField.setEditable(false);
		fastKnapTextField.setEditable(false);
		slowFifoTextField.setEditable(false);
		slowKnapTextField.setEditable(false);
		avgFifoTextField.setEditable(false);
		avgKnapTextField.setEditable(false);

		// change the size and font of the label
		fileName.setFont(new Font("Arial", 18));
		avgTime.setFont(new Font("Arial", 18));
		slowestTime.setFont(new Font("Arial", 18));
		fastestTime.setFont(new Font("Arial", 18));

		fileName.setTextFill(Color.WHITE);
		avgTime.setTextFill(Color.WHITE);
		slowestTime.setTextFill(Color.WHITE);
		fastestTime.setTextFill(Color.WHITE);

		// create new text Area for the running log
		TextArea outputLog = new TextArea();
		outputLog.setMaxWidth(1.8 * colW);
		outputLog.setMaxHeight(6 * rowH);
		outputLog.setEditable(false);

		// create new text Area for the results
		TextArea outputResults = new TextArea();
		outputResults.setMaxWidth(colW * .75);
		outputResults.setMaxHeight(6 * rowH);
		outputResults.setEditable(false);

		// Main page buttons
		Button setupPageButton = new Button("SETUP");
		Button runSimulationButton = new Button("RUN");
		Button saveLogButton = new Button("SAVE LOG");
		Button clearLogButton = new Button("CLEAR LOG");
		Button selectFileButton = new Button("SELECT FILE");
		
		
		// MAIN PAGE MENU BAR (for selecting map file and saving log ouput)
		
		// create a menu
		Menu mainPageMenu = new Menu("File");

		// create menu items
		String selectFileString = "Select Map File";
		String saveLogString = "Save Log Output";
		MenuItem mainScreenSelectFileMenuItem = new MenuItem(selectFileString);
		MenuItem mainScreenSaveLogMenuItem = new MenuItem(saveLogString);
		
		//add menu item to menu
		mainPageMenu.getItems().add(mainScreenSelectFileMenuItem); 
		mainPageMenu.getItems().add(mainScreenSaveLogMenuItem); 
		
		MenuBar mainPageMenuBar = new MenuBar();
		
		mainPageMenuBar.getMenus().add(mainPageMenu);
		
		// Setup event handlers for menu items
		mainScreenSelectFileMenuItem.setOnAction(e -> {
			File selectedSetupFile = selectSetupFile.showOpenDialog(window);
		});
		
		mainScreenSaveLogMenuItem.setOnAction(e -> {
			if (runner.getDisplayStringBuilder() != null && !runner.getDisplayStringBuilder().toString().equals("")) {
				
				// zip stats file with the graph file
				ArrayList<String> graphFiles = new ArrayList<>(); // list of graph file names to save
				try // get all the files in directory
			    {
			        String lscmd = "ls"; // bash cmd to get the names of the contents in the working directory
			        Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", lscmd}); // execute the command
			        p.waitFor();
			        BufferedReader reader=new BufferedReader(new InputStreamReader(p.getInputStream()));
			        
			        // parse through the output
			        String line=reader.readLine();
			        while(line!=null)
			        {
			            if (line.endsWith("_time graph.csv")) { // if it's a graph file
							graphFiles.add(line); // save the file name
						}
			            line=reader.readLine();
			        }
			    }
			    catch(IOException e1) {
			        System.err.println("Pblm found1.");
			    }
			    catch(InterruptedException e2) {
			        System.err.println("Pblm found2.");
			    }

				
				// create an array of the file names to save
				String[] fileNames = new String[1 + graphFiles.size()];
				fileNames[0] = "Simulation Results.txt"; // add the result file name
				for (int i = 0; i < graphFiles.size(); i++) { // add the graph file names
					fileNames[i + 1] = graphFiles.get(i);
				}
				
				
				

				FileChooser fileChooser = new FileChooser();

				// Set extension filter for popup file chooser dialog
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File zip = fileChooser.showSaveDialog(window);

				// create the result file to zip with the graph files, then zip them up
				if (zip != null) {
					try {
						
						// create results file (log display text)
						File statsFile = new File("Simulation Results.txt");
						PrintWriter pWriter = new PrintWriter(statsFile);
						String logString = outputLog.getText(); // text in display log
						pWriter.append(logString);
						pWriter.flush();
						pWriter.close();
						
						// zip them up
						zipFiles(fileNames, zip);
					} catch (FileNotFoundException e1) {
						System.err.println(e1.getMessage());
					}
				}
			}
		});
		
		
		//---------------------------------
		// RUN SIMULATION BUTTON EVENT
		//---------------------------------
		runSimulationButton.setOnAction(e -> {

			if (!runner.isRunning()) {
				// show an alert to let the user know the simulation is running
				Alert alert = new Alert(AlertType.NONE, "Running " + curSetup.getNumShifts() + " Simulations...", ButtonType.CLOSE);
				alert.setTitle("Simulation Info:");
				alert.show();

				//TODO display graphs
				Tuple results = runner.run(); // run the simulation and get strings
				StringBuilder displayString = (StringBuilder) results.getA(); // text to display
				StringBuilder logStringBuilder = (StringBuilder) results.getB(); // text to save
				//outputLog.clear();
				
				Date simTime = new Date();
				outputLog.appendText("\n\n\n");
				outputLog.appendText("-----------------------------------------------------------------\n");
				outputLog.appendText("-----------------------------------------------------------------\n\n\n");
				outputLog.appendText(simTime.toString());
				outputLog.appendText("\n");
				outputLog.appendText(displayString.toString()); // add to log screen
				
				
				// graph
				
				// Show save file dialog
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh;mm;ss");
				String fName = "Number of Orders vs Time_ " + format.format(simTime) + "_time graph.csv";
				File csvFile = new File(fName);

				if (csvFile != null) {
					try {
						PrintWriter pWriter = new PrintWriter(csvFile);
						pWriter.append(logStringBuilder.toString());
						pWriter.flush();
						pWriter.close();
						
						// graph result
						TimeGraph timeGraph = new TimeGraph();
						timeGraph.createDataSet(csvFile);
						timeGraph.showGraph();
			
					} catch (FileNotFoundException e1) {
						System.err.println(e1.getMessage());
					}
				}

				if (alert.isShowing()) { // close the alert if still showing
					alert.close();
				}
				
				// Auto Scroll to bottom
				outputLog.selectPositionCaret(outputLog.getLength()); 
				outputLog.deselect();
			}
			
		});
		
		// change to the setup page
		setupPageButton.setOnAction(e -> {
			window.setScene(setupScene);
		});

		// select the file button
		selectFileButton.setOnAction(e -> {
			File selectedSetupFile = selectSetupFile.showOpenDialog(window);
		});

		// change the size of buttons
		setupPageButton.setMaxSize(150, 50);
		runSimulationButton.setMaxSize(150, 50);
		saveLogButton.setMaxSize(150, 50);
		clearLogButton.setMaxSize(150, 50);
		selectFileButton.setMaxSize(150, 25);

		// add buttons to the main screen
		screenLayoutMain.add(setupPageButton, 1, 8, 1, 1);
		screenLayoutMain.add(runSimulationButton, 0, 8, 1, 1);
		//screenLayoutMain.add(saveLogButton, 0, 9, 1, 1);
		screenLayoutMain.add(clearLogButton, 1, 9, 1, 1);
		//screenLayoutMain.add(selectFileButton, 1, 7, 2, 1);
		screenLayoutMain.add(fileName, 0, 7, 1, 1);

		// add output blocks to the main screen
		screenLayoutMain.add(outputLog, 2, 0, 2, 5);
		screenLayoutMain.add(outputResults, 3, 6, 1, 5);

		// add results text to the screen
		screenLayoutMain.add(avgTime, 2, 6, 1, 1);
		screenLayoutMain.add(fastestTime, 2, 7, 1, 1);
		screenLayoutMain.add(slowestTime, 2, 8, 1, 1);

		// Create second level containers in page hierarchy
		VBox mainPageVBox = new VBox(mainPageMenuBar, screenLayoutMain);
		
		// making the new main scene
		mainScene = new Scene(mainPageVBox, 800, 600);

	}
	
	/**
	 * Zips up all the 'filePaths' files in in a given directory
	 * and puts them into 'zipFile' 
	 * @param filePaths
	 * @param zipFile
	 */
	private void zipFiles(String[] filePaths, File zipFile) {
		//System.out.println(Arrays.toString(filePaths));
        try { 
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
 
            for (String aFile : filePaths) {
                zos.putNextEntry(new ZipEntry(new File(aFile).getName()));
 
                byte[] bytes = Files.readAllBytes(Paths.get(aFile));
                zos.write(bytes, 0, bytes.length);
                zos.closeEntry();
            }
 
            zos.close();
 
        } catch (FileNotFoundException ex) {
            System.err.println("A file does not exist: " + ex);
        } catch (IOException ex) {
            System.err.println("I/O error: " + ex);
        }
    }

	public void makeSetupScreen() {
		curSetup.loadDefaultDroneSettings();

		// -----------------------------------------
		// SETUP PAGE
		// -----------------------------------------
		// 5 columns wide
		// first 2 are meal/prob
		// next 2 are meal items/possible foods

		// create the color for the background
		BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
		// make the actual background to be used
		Background applicationBackground = new Background(applicationColor);

		//
		double screenW = SCREEN_WIDTH;
		double screenH = SCREEN_HEIGHT;
		int insets = 15; // PADDING FOR SCREEN
		int numColumns = 4; // NUMBER OF COLUMNS FOR GRIDPANE
		int numRows = 15; // NUMBER OF ROWS FOR GRIDPANE
		double colW = screenW / numColumns - 11; // COL WIDTH
		double rowH = screenH / numRows - insets / numRows; // ROW WIDTH

		GridPane screenLayoutSetup = new GridPane(); // main layout
		screenLayoutSetup.setMaxWidth(colW * numColumns - 2 * insets);
		screenLayoutSetup.setMaxHeight(rowH * numRows - 2 * insets);
		screenLayoutSetup.setPadding(new Insets(insets));
		screenLayoutSetup.setVgap(5);
		screenLayoutSetup.setHgap(5);
		screenLayoutSetup.setBackground(applicationBackground);

		// set column widths
		for (int i = 0; i < numColumns; i++) {

			ColumnConstraints column = new ColumnConstraints(colW);
			screenLayoutSetup.getColumnConstraints().add(column);

		}

		// set column heights
		for (int i = 0; i < numRows; i++) {
			RowConstraints row = new RowConstraints(rowH);
			screenLayoutSetup.getRowConstraints().add(row);
		}

		// ------------------
		// Screen Objects
		// ------------------


		//---------------------------------
		// BUTTON'S SETUP
		//---------------------------------
		Button mainPageButton = new Button("BACK");
		Button createFoodButton = new Button("CREATE FOOD");
		Button addFoodButton = new Button("ADD FOOD");
		Button addMealButton = new Button("ADD MEAL");
		Button clearMealButton = new Button("CLEAR MEAL");

		mainPageButton.setMaxSize(150, 50);
		createFoodButton.setMaxSize(150, 50);
		addFoodButton.setMaxSize(150, 50);
		addMealButton.setMaxSize(150, 50);
		clearMealButton.setMaxSize(150, 50);


		//---------------------------------
		// TEXT FIELD'S SETUP
		//---------------------------------
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

		//---------------------------------
		// COMBO BOX SETUP (FOR MEAL CREATION)
		//---------------------------------
		// FOOD SELECTION
		ObservableList<String> options = FXCollections.observableArrayList(curSetup.getAllFoodNames());
		ComboBox<String> foodOptionsComboBox = new ComboBox<>(options);
		foodOptionsComboBox.setPromptText("SELECT FOOD");
		foodOptionsComboBox.setMaxWidth(150);

		// FOOD QUANTITY
		ObservableList<Integer> qoptions = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
				14, 15);
		ComboBox<Integer> foodQuantityComboBox = new ComboBox<>(qoptions);
		foodQuantityComboBox.setPromptText("QUANTITY");
		foodQuantityComboBox.setMaxWidth(150);

		//---------------------------------
		// TEXT AREA'S SETUP
		//---------------------------------
		// create new text Area
		TextArea mealProbTextArea = new TextArea();
		mealProbTextArea.setMaxWidth(2 * colW);
		mealProbTextArea.setMaxHeight(12 * rowH);
		mealProbTextArea.setEditable(false);
		// load meals
		{
			String out = "";
			for (Meal meal : curSetup.getAllMeals()) {
				out += String.format("%-63s%.2f -> %.2f%%", meal.getName(), meal.getRawProbability(),
						(100 * meal.getScaledProbability()));
				out += "\n";
				out += "\tContains: ";
				for (Food food : meal.getFoodItems()) {
					out += food.getName() + ", ";
				}
				out = out.substring(0, out.lastIndexOf(",")); // get rid of last ","
				out += "\n";
				out += "\tWeight: " + meal.getWeight() + " (oz)";
				out += "\n\n";
			}
			mealProbTextArea.setText(out);
		}

		TextArea mealCreaterTextArea = new TextArea();
		mealCreaterTextArea.setMaxWidth(colW);
		// mealCreaterTextArea.setMaxHeight(6 * rowH);
		mealCreaterTextArea.setEditable(false);
		mealCreaterTextArea.setPromptText("Your Custom Meal...");

		//---------------------------------
		// LABEL'S SETUP
		//---------------------------------
		Label mealLabel = new Label("MEAL");
		Label probabilityLabel = new Label("PROBABILITY");
		Label inMealLabel = new Label("CUSTOM MEAL");

		mealLabel.setFont(new Font("Arial", 18));
		probabilityLabel.setFont(new Font("Arial", 18));
		inMealLabel.setFont(new Font("Arial", 18));

		mealLabel.setTextFill(Color.WHITE);
		probabilityLabel.setTextFill(Color.WHITE);
		inMealLabel.setTextFill(Color.WHITE);

		// buttons action:
		//---------------------------------
		// MAIN PAGE BUTTON EVENT
		//---------------------------------
		mainPageButton.setOnAction(e -> window.setScene(mainScene)); // go back to main screen
		
		//---------------------------------
		// CREATE FOOD BUTTON EVENT
		//---------------------------------
		createFoodButton.setOnAction(e -> {
			try {
				// parse new food
				String name = foodNameTextField.getText();
				int weight = Integer.parseInt(foodWeightTextField.getText());
				Food food = new Food(name, weight);
				curSetup.addFood(food);

				// update combo box
				ObservableList<String> foodOptions = FXCollections.observableArrayList(curSetup.getAllFoodNames());
				foodOptionsComboBox.setItems(foodOptions);

				foodNameTextField.setText("");
				foodWeightTextField.setText("");
			} catch (Exception e2) {
				foodWeightTextField.setText(""); // if not an integer placed
			}
		});
		
		//---------------------------------
		// ADD FOOD BUTTON EVENT
		//---------------------------------
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
					ObservableList<String> foodOptions = FXCollections.observableArrayList(curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);

				} catch (Exception e2) {
					System.err.println(e2.getMessage());
				}

			}
		});
		clearMealButton.setOnAction(e -> {
			mealCreaterTextArea.setText("");
		});
		
		//---------------------------------
		// ADD MEAL BUTTON EVENT
		//---------------------------------
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
						for (Food food : curSetup.getAllFoods()) { // find the right food to add to meal
							if (name.equals(food.getName())) {
								for (int i = 0; i < quantity; i++) { // add the given quantity of food to meal
									newMeal.addFood(food);
									// System.out.println("food added");
								}
							}
						}

					}

					if (newMeal.getWeight() > curSetup.getDroneWeight()) { // if the meal weighs more than the drone can
																			// carry
						new createPopUp("ERROR:",
								"Meal weight is greater than the Drone weight capacity.\n" + "\t- Meal Weight: "
										+ newMeal.getWeight() + " (oz)\n" + "\t- Drone Capacity: "
										+ curSetup.getDroneWeight() + " (oz)");
					} else {
						// add meal
						curSetup.addMeal(newMeal);
						curSetup.adjustMealProbabilities();
						{ // print
							String out = "";
							for (Meal meal : curSetup.getAllMeals()) {
								out += String.format("%-63s%.2f -> %.2f%%", meal.getName(), meal.getRawProbability(),
										(100 * meal.getScaledProbability()));
								out += "\n";
								out += "\tContains: ";
								for (Food food : meal.getFoodItems()) {
									out += food.getName() + ", ";
								}
								out = out.substring(0, out.lastIndexOf(",")); // get rid of last ","
								out += "\n";
								out += "\tWeight: " + meal.getWeight() + " (oz)";
								out += "\n\n";
							}
							mealProbTextArea.setText(out);
						}
					}

				}

			} catch (Exception e3) {
				System.err.println(e3.getMessage());
			}
			//Clear entered area for last created meal
			mealCreaterTextArea.setText("");
		});

		// ------------------
		// left area
		// ------------------
		// add labels for meal/prob
		screenLayoutSetup.add(mealLabel, 0, 0);
		screenLayoutSetup.add(probabilityLabel, 1, 0);

		// set their allignment
		screenLayoutSetup.setHalignment(mealLabel, HPos.CENTER);
		screenLayoutSetup.setHalignment(probabilityLabel, HPos.CENTER);

		// add text area
		screenLayoutSetup.add(mealProbTextArea, 0, 1, 2, 10);

		// add back button
		screenLayoutSetup.add(mainPageButton, 0, 11);

		// ------------------
		// right side
		// ------------------
		screenLayoutSetup.add(new Label(), 2, 0);

		screenLayoutSetup.add(foodNameTextField, 2, 1, 2, 1);
		screenLayoutSetup.add(foodWeightTextField, 2, 2, 2, 1);
		screenLayoutSetup.setHalignment(foodNameTextField, HPos.CENTER);
		screenLayoutSetup.setHalignment(foodWeightTextField, HPos.CENTER);

		screenLayoutSetup.add(createFoodButton, 2, 3, 2, 1);
		screenLayoutSetup.setHalignment(createFoodButton, HPos.CENTER);

		// add some meal stuff
		mealCreaterTextArea.setMaxHeight(4 * rowH);
		screenLayoutSetup.add(inMealLabel, 3, 5);
		screenLayoutSetup.setHalignment(inMealLabel, HPos.CENTER);
		screenLayoutSetup.add(mealCreaterTextArea, 3, 6, 1, 4);

		screenLayoutSetup.add(foodOptionsComboBox, 2, 5);
		screenLayoutSetup.add(foodQuantityComboBox, 2, 6);
		screenLayoutSetup.add(addFoodButton, 2, 7);
		screenLayoutSetup.add(mealNameTextField, 2, 8);
		screenLayoutSetup.add(mealProbabilityTextField, 2, 9);
		screenLayoutSetup.setHalignment(foodOptionsComboBox, HPos.CENTER);
		screenLayoutSetup.setHalignment(foodQuantityComboBox, HPos.CENTER);
		screenLayoutSetup.setHalignment(addFoodButton, HPos.CENTER);
		screenLayoutSetup.setHalignment(mealProbabilityTextField, HPos.CENTER);
		screenLayoutSetup.setHalignment(mealNameTextField, HPos.CENTER);

		screenLayoutSetup.add(addMealButton, 2, 10);
		screenLayoutSetup.add(clearMealButton, 3, 10);
		screenLayoutSetup.setHalignment(addMealButton, HPos.CENTER);
		screenLayoutSetup.setHalignment(clearMealButton, HPos.CENTER);

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
					File file = fileChooser.showSaveDialog(window);

					if (file != null) {
						curSetup.saveFoodSettings(file);
					}

				} else if (selectedS.equals(loadS)) {

					FileChooser fileChooser = new FileChooser();
					FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter("CSV files (*.csv)",
							"*.csv");
					fileChooser.getExtensionFilters().add(fileExtensions);

					File selectedFile = fileChooser.showOpenDialog(window);

					if (selectedFile != null) { // if a file was selected
						curSetup.loadFoodSettings(selectedFile); // load the file

					}
					
					// update combo box
					ObservableList<String> foodOptions = FXCollections.observableArrayList(curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);
					//reset built meal
					mealCreaterTextArea.setText("");

				} else if (selectedS.equals(defaultS)) {
					curSetup.loadDefaultFoodSettings();
					// update combo box
					ObservableList<String> foodOptions = FXCollections.observableArrayList(curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);
					//reset built meal
					mealCreaterTextArea.setText("");
				} else if (selectedS.equals(clearS)) {
					curSetup.clearFoodsAndMeals();
					// update combo box
					ObservableList<String> foodOptions = FXCollections.observableArrayList(curSetup.getAllFoodNames());
					foodOptionsComboBox.setItems(foodOptions);
					//reset built meal
					mealCreaterTextArea.setText("");
				}

				// reload screen stuff
				mealProbabilityTextField.clear();
				String out = "";
				for (Meal meal : curSetup.getAllMeals()) {
					out += String.format("%-63s%.2f -> %.2f%%", meal.getName(), meal.getRawProbability(),
							(100 * meal.getScaledProbability()));
					out += "\n";
					out += "\tContains: ";
					for (Food food : meal.getFoodItems()) {
						out += food.getName() + ", ";
					}
					out = out.substring(0, out.lastIndexOf(",")); // get rid of last ","
					out += "\n";
					out += "\tWeight: " + meal.getWeight() + " (oz)";
					out += "\n\n";
				}
				mealProbTextArea.setText(out);

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

		VBox vBox = new VBox(mb, screenLayoutSetup);

		setupScene = new Scene(vBox, screenW, screenH);
	}

	/**
	 * Creates a custom pop up window with given text and title
	 * @author LEHMANIT17
	 *
	 */
	class createPopUp extends Stage {
		createPopUp(String title, String msg) {
			setTitle(title);
			VBox y = new VBox();

			y.getChildren().add(new Label(msg));
			y.setAlignment(Pos.CENTER);
			setScene(new Scene(y, 300, 100));
			show();

		}
	}

}
