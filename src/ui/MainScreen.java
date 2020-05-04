package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.GMapMouseEvent;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import droneSim.Tuple;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
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
	LatLong rightClickLatLong;
	ArrayList<String> pointNames = new ArrayList<String>();

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
		GridPane mapLayout = new GridPane();

		screenLayoutMain.setPadding(new Insets(insets));
		screenLayoutMain.setHgap(5);
		screenLayoutMain.setVgap(5);

		// set column widths (and rows for mapLayout)
		for (int i = 0; i < numColumns; i++) {
			ColumnConstraints column = new ColumnConstraints();
			RowConstraints row = new RowConstraints();
			column.setPercentWidth(25);
			row.setPercentHeight(25);
			screenLayoutMain.getColumnConstraints().add(column);
			mapLayout.getColumnConstraints().add(column);
			mapLayout.getRowConstraints().add(row);
		}

		// set column heights
		for (int i = 0; i < numRows; i++) {
			RowConstraints row = new RowConstraints();
			row.setPercentHeight(100.0 / numRows);
			screenLayoutMain.getRowConstraints().add(row);
		}

		// Main Page text
		Label fileName = new Label("Grove City College");

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
		String saveMapString = "Save Map as .csv";

		MenuItem mainScreenLoadMapMenuItem = new MenuItem(selectFileString);
		MenuItem mainScreenSaveLogMenuItem = new MenuItem(saveLogString);
		MenuItem mainScreenSaveMapMenuItem = new MenuItem(saveMapString);

		// add menu item to menu
		mainPageMenu.getItems().add(mainScreenLoadMapMenuItem);
		mainPageMenu.getItems().add(mainScreenSaveLogMenuItem);
		mainPageMenu.getItems().add(mainScreenSaveMapMenuItem);

		MenuBar mainPageMenuBar = new MenuBar();

		mainPageMenuBar.getMenus().add(mainPageMenu);

		// Setup event handlers for menu items
		mainScreenLoadMapMenuItem.setOnAction(e -> {
			// file chooser for selecting a saved setup
			FileChooser selectSetupFile = new FileChooser();
			
			// Set initial display settings for File Explorer window
			selectSetupFile.setTitle("Select Saved Map");
			selectSetupFile.setInitialDirectory(new File(
					System.getProperty("user.home") + "\\Documents"));
			selectSetupFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
			
			File selectedSetupFile = selectSetupFile.showOpenDialog(ui_Setup.window);
			if (selectedSetupFile != null) {
				// Load the new map into the Map instance and refresh the GoogleMap instance 
				//  with the new delivery points
				fileName.setText(selectedSetupFile.getName());
				ui_Setup.curSetup.getCurrentMap().loadMap(selectedSetupFile.getName(), 
						selectedSetupFile.getAbsolutePath());
				mapInitialized();
			}
		});
		
		
		mainScreenSaveMapMenuItem.setOnAction(e -> {
			// file chooser for selecting a save location
			FileChooser selectSetupFile = new FileChooser();
			
			// Set initial display settings for File Explorer window
			selectSetupFile.setTitle("Select Save Location");
			selectSetupFile.setInitialDirectory(new File(
					System.getProperty("user.home") + "\\Documents"));
			selectSetupFile.setInitialFileName(ui_Setup.curSetup.getCurrentMap().getMapName());
			selectSetupFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
			
			File selectedSetupFile = selectSetupFile.showOpenDialog(ui_Setup.window);
			if (selectedSetupFile != null) {
				ui_Setup.curSetup.getCurrentMap().saveMap(selectedSetupFile.getName());
			}
		});


		mainScreenSaveLogMenuItem.setOnAction(e -> {
			if (ui_Setup.runner.getDisplayStringBuilder() != null
					&& !ui_Setup.runner.getDisplayStringBuilder().toString().equals("")) {

				// zip stats file with the graph file and food settings file

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
						
						// create a foods setting file
						File foodSettingFile = new File("Food's in Simulation.csv");
						ui_Setup.curSetup.saveFoodSettings(foodSettingFile);
						
						// ------------------------------
						// get the graph file names to save
						// ------------------------------
						String[] fileNames = ui_Setup.fileTools.getGraphFileNames();

						// zip them up
						ui_Setup.fileTools.zipFiles(fileNames, zip);
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
				
				if(ui_Setup.curSetup.getAllMeals()==null || ui_Setup.curSetup.getAllMeals().size()==0)
					ui_Setup.curSetup.loadDefaultFoodSettings();
				
				// show an alert to let the user know the simulation is running
				Alert alert = new Alert(AlertType.NONE,
						"Running " + ui_Setup.curSetup.getNumShifts() + " Simulations...", ButtonType.CLOSE);
				alert.setTitle("Simulation Info:");
				alert.show();

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
			ui_Setup.fileTools.deleteGraphFiles();
			outputLog.clear();
		});
		
		// ---------------------------------
		// GOOGLE MAPS VIEW
		// ---------------------------------
		//Create the JavaFX component and set this as a listener so we know when 
	    //the map has been initialized, at which point we can then begin manipulating it.
	    mapView = new GoogleMapView();
	    mapView.addMapInializedListener(this);
	    
        ContextMenu mapContextMenu = new ContextMenu();
 
        MenuItem addMarkerMenuItem = new MenuItem("Add Marker");
        MenuItem removeMarkerMenuItem = new MenuItem("Remove Nearby Marker");
        
        // Add marker (delivery point) to GoogleMap instance on button click
        addMarkerMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        	    // Get user input for name of new DeliveryPoint
        	    PopUp inputPopUp = new PopUp("Delivery Point Name");
        	    
        	    // Activated when the user hits enter in the PopUp instance
        	    inputPopUp.getInputBox().setOnAction(new EventHandler<ActionEvent>() { 
                    public void handle(ActionEvent e) { 
                    	String newPointName = inputPopUp.getInputBox().getText();
                    	
                    	ui_Setup.curSetup.getCurrentMap().addPoint(
                    			newPointName, rightClickLatLong.getLatitude(), rightClickLatLong.getLongitude());
                    	pointNames.add(newPointName);
                    	
                    	//TODO only create point if within certain distance of HOME
                    	//if (ui_Setup.curSetup.getCurrentMap().getLongestFlightDistance() > 
                    			
                    	MarkerOptions markerOptions = new MarkerOptions().position( rightClickLatLong )
            	                .visible(Boolean.TRUE)
            	                .title(newPointName);
            	
                    	Marker marker = new Marker( markerOptions );
                	    map.addMarker(marker);
                    	inputPopUp.hide();
                    } 
                });
        	    
        	    // Resets the view of the GoogleMap instance for if a new Marker object does not load immediately
        	    int currentZoom = map.getZoom();
                map.setZoom( currentZoom - 1 );
                map.setZoom( currentZoom );
            }
        });
        
        // Remove nearest marker (delivery point) to GoogleMap instance on button click
        removeMarkerMenuItem.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		boolean alreadyDelete = false;
        		double searchRadius = 0.2 * Math.pow(20 - map.getZoom(), 2);
        		int nearestPointIndex =-1; //nearest point to click
        		double nearestPointDistance=-1; //nearest point's to click distance

        		// Loop through all points to find the nearest point 
        		//   (within a given distance of the mouse right-click)
        		
        		
        		for (int index = 0; index < pointNames.size(); index++) 
        		{
        			double distanceOfPointToClick = 0.0;
        			//Retrieve each point from the Map instance
        			Tuple pointTuple = ui_Setup.curSetup.getCurrentMap().getLatLongPoint(index);
        			LatLong currentPoint = new LatLong(pointTuple.getLatitude(), pointTuple.getLongitude());
        			
        			// Basic distance formula
          			distanceOfPointToClick += Math.pow(rightClickLatLong.getLatitude() -
          					currentPoint.getLatitude(), 2);
          			distanceOfPointToClick += Math.pow(rightClickLatLong.getLongitude() -
          					currentPoint.getLongitude(), 2);
          			
          			distanceOfPointToClick = Math.sqrt(distanceOfPointToClick) * 1000;
          			
          			//if close enough to be able to delete
        			if (Math.abs(distanceOfPointToClick) < searchRadius) 
        			{
        				//if there is no current closest
        				if(nearestPointIndex==-1)
        				{
        					nearestPointIndex = index;
        					nearestPointDistance = Math.abs(distanceOfPointToClick);
        				}
        				//if nearer than previous nearest point
        				else if(Math.abs(distanceOfPointToClick) < nearestPointDistance)
        				{
        					nearestPointIndex = index;
        					nearestPointDistance = Math.abs(distanceOfPointToClick);
        				}
        			}
        		}
        		
        		//if there is a valid nearest point
        		if(nearestPointIndex !=-1)
        		{
        			ui_Setup.curSetup.getCurrentMap().deletePoint(nearestPointIndex);
        			pointNames.remove(nearestPointIndex);
        		}
        		
        		
        		// Since only Marker objects can be removed individually from the GoogleMap instance and the
        		//  Marker objects are not saved, all markers must be cleared and the remaining DeliveryPoints
        		//  used to repopulate the GoogleMap instance
        		map.clearMarkers();
        		
        		// Repopulate the GoogleMap instance with all remaining DeliveryPoints in the Map instance
        		for (int updatedIndex = 0; updatedIndex < pointNames.size(); updatedIndex++) {
        			Tuple pointDoubles = ui_Setup.curSetup.getCurrentMap().getLatLongPoint(updatedIndex);
        			
        			LatLong currentPoint = new LatLong(pointDoubles.getLatitude(), pointDoubles.getLongitude());
        			MarkerOptions markerOptions = new MarkerOptions()
        					.position(currentPoint)
        	                .visible(Boolean.TRUE)
        	                .title(pointNames.get(updatedIndex));
        	
	        	    Marker marker = new Marker( markerOptions );

	        	    map.addMarker(marker);
        		}
        	}
        });
        
        // Add MenuItem to ContextMenu
        mapContextMenu.getItems().addAll(addMarkerMenuItem, removeMarkerMenuItem);
 
        // When user right-click on List View of meals
        mapView.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                mapContextMenu.show(ui_Setup.window, event.getScreenX(), event.getScreenY());
            }
        });
        
        Button refreshMap = new Button("Load Map");
        GridPane.setHalignment(setupPageButton, HPos.CENTER);
        refreshMap.setOnAction(e -> {
        	mapView.getWebview().getEngine().reload();
        });
        mapLayout.add(refreshMap, 2, 3);
        mapView.getChildren().add(mapLayout);

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
		ArrayList<Tuple> existingPoints = ui_Setup.curSetup.getCurrentMap().getLatLongPoints();
		MapOptions mapOptions = new MapOptions();
	    
		LatLong homePoint = new LatLong(
    			existingPoints.get(0).getLatitude(), existingPoints.get(0).getLongitude());
		//Set default value for rightClickLatLong to avoid errors when clicking on markers upon startup
		rightClickLatLong = new LatLong(0,0);
		
	    mapOptions.center(homePoint)
	            .mapType(MapTypeIdEnum.ROADMAP)
	            .overviewMapControl(false)
	            .panControl(false)
	            .rotateControl(false)
	            .scaleControl(false)
	            .streetViewControl(false)
	            .zoomControl(false)
	            .zoom(10);
	
	    if (mapView.getChildren().size() > 1) {
	    	mapView.getChildren().remove(1);
	    }
	    map = mapView.createMap(mapOptions);
	    
	    // Save latitude and longitude of mouse right-click for use in add/delete point functionality (look above)
	    map.addMouseEventHandler(UIEventType.mouseup, (GMapMouseEvent event) -> {
     	   rightClickLatLong = event.getLatLong();
     	});
	    
	    // When the map is initially loaded, populate with all the pre-existing points in the Map instance
	    for (int index = 0; index < existingPoints.size(); index++) {
	    	pointNames.add(ui_Setup.curSetup.getCurrentMap().getPointName(index));
	    	
	    	LatLong pointToAdd = new LatLong(
	    			existingPoints.get(index).getLatitude(), existingPoints.get(index).getLongitude());
	    	MarkerOptions markerOptions = new MarkerOptions().position(pointToAdd)
	                .visible(Boolean.TRUE)
	                .title(pointNames.get(index));
	
        	Marker marker = new Marker( markerOptions );
        	
        	map.addMarker(marker);
	    }
	    
	    map.setZoom( 17 );
	
	}

}
