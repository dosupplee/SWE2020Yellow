package ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import droneSim.Tuple;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class MainScreen implements MapComponentInitializedListener {

	private UI_Setup ui_Setup;
	private GoogleMapView mapView;
	private GoogleMap map;

	public MainScreen(UI_Setup ui_Setup) {
		this.ui_Setup = ui_Setup;
	}

	public void makeMainScreen() {

		// create the color for the background
		BackgroundFill applicationColor = new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY);
		// make the actual background to be used
		Background applicationBackground = new Background(applicationColor);

		/*
		 * Main page screen setup
		 */
		int insets = 15;
		int numColumns = 4;
		int numRows = 13;


		// layout for the main page
		GridPane screenLayoutMain = new GridPane();

		screenLayoutMain.setPadding(new Insets(insets));
		screenLayoutMain.setHgap(5);
		screenLayoutMain.setVgap(5);

		// set column widths
		for (int i = 0; i < numColumns; i++) {
			ColumnConstraints column = new ColumnConstraints();
			column.setPercentWidth(25);
			screenLayoutMain.getColumnConstraints().add(column);
		}

		// set column heights
		for (int i = 0; i < numRows; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100.0 / numRows);
			screenLayoutMain.getRowConstraints().add(row);
		}

		// Main Page text
		Label fileName = new Label("<FileName>");

		// change the size and font of the label
		fileName.setFont(new Font("Arial", 18));

		fileName.setTextFill(Color.WHITE);

		// create new text Area for the running log
		TextArea outputLog = new TextArea();
		outputLog.setEditable(false);

		// Main page buttons
		Button setupPageButton = new Button("SETUP");
		Button runSimulationButton = new Button("RUN");

		Button clearLogButton = new Button("CLEAR LOG");

		// ---------------------------------------------------------------
		// MAIN PAGE MENU BAR (for selecting map file and saving log ouput)
		// ---------------------------------------------------------------

		// create a menu
		Menu mainPageMenu = new Menu("File");

		// create menu items
		String selectFileString = "Select Map File";
		String saveLogString = "Save Log Output";

		MenuItem mainScreenSelectFileMenuItem = new MenuItem(selectFileString);
		MenuItem mainScreenSaveLogMenuItem = new MenuItem(saveLogString);

		// add menu item to menu
		mainPageMenu.getItems().add(mainScreenSelectFileMenuItem);
		mainPageMenu.getItems().add(mainScreenSaveLogMenuItem);

		MenuBar mainPageMenuBar = new MenuBar();

		mainPageMenuBar.getMenus().add(mainPageMenu);

		// Setup event handlers for menu items
		mainScreenSelectFileMenuItem.setOnAction(e -> {
			// file chooser for selecting a saved setup
			FileChooser selectSetupFile = new FileChooser();
			// title for the file chooser
			selectSetupFile.setTitle("Select Saved Map");
			File selectedSetupFile = selectSetupFile.showOpenDialog(ui_Setup.window);
			if (selectedSetupFile != null) {
				fileName.setText(selectedSetupFile.getName());
			}
		});


		mainScreenSaveLogMenuItem.setOnAction(e -> {
			if (ui_Setup.runner.getDisplayStringBuilder() != null
					&& !ui_Setup.runner.getDisplayStringBuilder().toString().equals("")) {

				// zip stats file with the graph file

				// ------------------------------
				// get the graph file names to save
				// ------------------------------
				String[] fileNames = ui_Setup.graphingTools.getGraphFileNames();

				// ------------------------------
				// Open File dialog for saving
				// -----------------------------
				FileChooser fileChooser = new FileChooser();

				// Set extension filter for popup file chooser dialog
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
				fileChooser.getExtensionFilters().add(extFilter);

				// Show save file dialog
				File zip = fileChooser.showSaveDialog(ui_Setup.window);

				// ------------------------------
				// create the results file and zip it with the graph files
				// ------------------------------
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
						ui_Setup.graphingTools.zipFiles(fileNames, zip);
					} catch (FileNotFoundException e1) {
						System.err.println(e1.getMessage());
					}
				}
			}
		});

		// ---------------------------------
		// RUN SIMULATION BUTTON EVENT
		// ---------------------------------
		runSimulationButton.setOnAction(e -> {

			if (!ui_Setup.runner.isRunning()) {
				// show an alert to let the user know the simulation is running
				Alert alert = new Alert(AlertType.NONE,
						"Running " + ui_Setup.curSetup.getNumShifts() + " Simulations...", ButtonType.CLOSE);
				alert.setTitle("Simulation Info:");
				alert.show();

				// TODO display graphs
				Tuple results = ui_Setup.runner.run(); // run the simulation and get strings
				StringBuilder displayString = (StringBuilder) results.getA(); // text to display
				StringBuilder logStringBuilder = (StringBuilder) results.getB(); // text to save
				// outputLog.clear();

				Date simTime = new Date();
				
				outputLog.appendText(simTime.toString());
				outputLog.appendText("\n");
				outputLog.appendText(displayString.toString()); // add to log screen
				outputLog.appendText("\n\n\n");
				outputLog.appendText("---------------------------------------------------------------------------------------\n");
				outputLog.appendText("---------------------------------------------------------------------------------------\n\n\n");


				// Show save file dialog
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_hh;mm;ss");
				String fName = "Simulation Results_ " + format.format(simTime) + "_RAW.csv";
				File csvFile = new File(fName);

				if (csvFile != null) {
					try {
						PrintWriter pWriter = new PrintWriter(csvFile);
						pWriter.append(logStringBuilder.toString());
						pWriter.flush();
						pWriter.close();

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

		// ---------------------------------
		// SETUP PAGE BUTTON EVENT
		// ---------------------------------
		// change to the setup page
		setupPageButton.setOnAction(e -> {
			ui_Setup.window.setScene(ui_Setup.setupScene);
		});

		// ---------------------------------
		// CLEAR LOG BUTTON EVENT
		// ---------------------------------
		clearLogButton.setOnAction(e -> {
			ui_Setup.graphingTools.deleteGraphFiles();
			outputLog.clear();
		});
		
		// ---------------------------------
		// GOOGLE MAPS VIEW
		// ---------------------------------
		mapView = new GoogleMapView();
	    mapView.addMapInializedListener(this);

		// change the size of buttons
		setupPageButton.setPrefSize(150, 50);
		runSimulationButton.setPrefSize(150, 50);
		clearLogButton.setPrefSize(150, 50);
		
		GridPane.setHalignment(setupPageButton, HPos.CENTER);
		GridPane.setHalignment(runSimulationButton, HPos.CENTER);
		GridPane.setHalignment(clearLogButton, HPos.CENTER);
		GridPane.setHalignment(fileName, HPos.CENTER);

		// add buttons to screen
		screenLayoutMain.add(setupPageButton, 1, 11, 1, 1);
		screenLayoutMain.add(runSimulationButton, 0, 10, 1, 1);
		screenLayoutMain.add(clearLogButton, 0, 11, 1, 1);
		screenLayoutMain.add(fileName, 1, 10, 1, 1);
		screenLayoutMain.add(outputLog, 2, 0, 2, 13);
		screenLayoutMain.add(mapView, 0, 0, 2, 9);

		// Create second level containers in page hierarchy
		VBox mainPageVBox = new VBox(mainPageMenuBar, screenLayoutMain);
		mainPageVBox.setBackground(applicationBackground);
		VBox.setVgrow(screenLayoutMain, Priority.ALWAYS);

		// making the new main scene
		ui_Setup.mainScene = new Scene(mainPageVBox, ui_Setup.SCREEN_WIDTH, ui_Setup.SCREEN_HEIGHT);
		
		ui_Setup.mainScene.getStylesheets().add("MainPage.css");
		
	}

	@Override
	public void mapInitialized() {
	    //Set the initial properties of the map.
	    MapOptions mapOptions = new MapOptions();
	
	    mapOptions.center(new LatLong(41.1558, -80.0785))
        .mapType(MapTypeIdEnum.SATELLITE)
        .overviewMapControl(false)
        .mapTypeControl(false)
        .panControl(false)
        .rotateControl(false)
        .scaleControl(false)
        .streetViewControl(false)
        .zoomControl(false)
        .zoom(17);

		map = mapView.createMap(mapOptions);
		
		//Add a marker to the map
		MarkerOptions sacMarkerOption = new MarkerOptions();
		MarkerOptions halMarkerOption = new MarkerOptions();
		MarkerOptions stemMarkerOption = new MarkerOptions();
		
		sacMarkerOption.position( new LatLong(41.1548, -80.0778) )
		        .visible(Boolean.TRUE)
		        .title("SAC");
		halMarkerOption.position( new LatLong(41.1546, -80.0773) )
				.visible(Boolean.TRUE)
				.title("HAL");
		stemMarkerOption.position( new LatLong(41.1553, -80.0789) )
				.visible(Boolean.TRUE)
				.title("STEM");
		
		Marker sacMarker = new Marker( sacMarkerOption );
		Marker halMarker = new Marker( halMarkerOption );
		Marker stemMarker = new Marker( stemMarkerOption );
		
		map.addMarker(sacMarker);
		map.addMarker(halMarker);
		map.addMarker(stemMarker);
	}

}
