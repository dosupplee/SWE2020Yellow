package ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import droneSim.CurrentSetup;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class BarChartScreen extends Stage {
	private CurrentSetup currentSetup;
	private String screenTitle = "Bar Chart";
	private ArrayList<Integer> fifoTimes;
	private ArrayList<Integer> knapSackTimes;
	private BarChart<String, Number> barChart;
	private MenuBar mb;
	private Scene scene;
	private final int NUMBER_OF_BARS = 20; // how many bars to make
	private int height = 425;
	private int width = 700;


	public BarChartScreen(CurrentSetup currentSetup) {
		this.currentSetup = currentSetup;

	}

	public void init(ArrayList<Integer> fifoTimes, ArrayList<Integer> knapSackTimes) {
		this.fifoTimes = fifoTimes;
		this.knapSackTimes = knapSackTimes;

		setTitle(screenTitle);

		makeGraph();
		
		makeMenuBar();

		// Creating a Group object
		VBox root = new VBox(mb, barChart);
		VBox.setVgrow(barChart, Priority.ALWAYS);
		

		// Creating a scene object
		scene = new Scene(root, width, height);

		// Setting title to the Stage
		setTitle("Bar Chart");

		// Adding scene to the stage
		setScene(scene);

		// Displaying the contents of the stage
		show();
	}
	
	public void saveAsPng() {
        WritableImage image = scene.snapshot(null);
        
        FileChooser fileChooser = new FileChooser();

		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)",
				"*.png");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showSaveDialog(this);

		if (file != null) {
			try {
	            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
    }
	
	private void makeMenuBar() {
		Menu m = new Menu("File");
		
		String saveS = "Save as png";
		MenuItem m1 = new MenuItem(saveS);	

		// add event
		m1.setOnAction(onclick -> {
			saveAsPng();
		});
		
		// add menu items to menu
		m.getItems().add(m1);
		
		// create a menubar
		mb = new MenuBar();

		// add menu to menubar
		mb.getMenus().add(m);
		
		

	}

	public void makeGraph() {
		// get the longest delivery time
		int longestDeliveryTime = getLongestDeliveryTime();
		// get the shortest delivery time
		int shortestDeliveryTime = getShortestDeliveryTime();
		// get the step amount
		int step = (longestDeliveryTime - shortestDeliveryTime) / NUMBER_OF_BARS;

	
		// Defining the axes
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Delivery Times (seconds)");
		NumberAxis yAxis = new NumberAxis(); // min, max, space
		yAxis.setLabel("Number of Orders");
		yAxis.setForceZeroInRange(false);

		// Creating the Bar chart
		barChart = new BarChart<>(xAxis, yAxis);
		barChart.setTitle("Comparison between FIFO and Knapsack delivery times");

		// get the number of orders to simulate
		int totalNumberOfOrdersSimulated = currentSetup.getTotalOrdersSimulated();
		System.out.println("num orders: " + totalNumberOfOrdersSimulated);

		// sort the delivery times smallest to largest
		Collections.sort(knapSackTimes);
		Collections.sort(fifoTimes);

		// make the 2 data sets
		XYChart.Series<String, Number> fifoDataset = new XYChart.Series<>();
		XYChart.Series<String, Number> knapDataset = new XYChart.Series<>();
		fifoDataset.setName("FIFO");
		knapDataset.setName("KNAPSACK");

		//
		// add data to the data sets
		//

		// fifo
		
		int index = 0;
		int curTime = shortestDeliveryTime + step;
		int maxTime = longestDeliveryTime + 2*step;
		while (curTime <= maxTime) {
			int amountInBar = 0;
			boolean inTimeRange = true;
			while (index < fifoTimes.size() && inTimeRange) {

				int curValue = fifoTimes.get(index);
				if (curValue <= curTime) {
					amountInBar++;
					index++;
				} else {
					String seriesName = (curTime - step) + " - " + (curTime);
					fifoDataset.getData().add(new XYChart.Data<>(seriesName, amountInBar));
					inTimeRange = false; // break the loop
				}
			}
			curTime += step;
		}
		// uncomment for analysis
		//System.out.println(String.format("Time: %d, Step: %d, Index: %d, curVal: %d, Amount in Bar: %d", curTime, step, index, curValue, amountInBar));


		// knapsack
		index = 0;
		curTime = shortestDeliveryTime + step;
		while (curTime <= maxTime) {
			int amountInBar = 0;
			boolean inTimeRange = true;
			while (index < knapSackTimes.size() && inTimeRange) {

				int curValue = knapSackTimes.get(index);
				if (curValue <= curTime) {
					amountInBar++;
					index++;
				} else {
					String seriesName = (curTime - step) + " - " + (curTime);
					knapDataset.getData().add(new XYChart.Data<>(seriesName, amountInBar));
					inTimeRange = false; // break the loop
				}
			}
			curTime += step;
		}

		// add datasets to bar chart
		barChart.getData().add(fifoDataset);
		barChart.getData().add(knapDataset);
		barChart.setLegendSide(Side.RIGHT); // set the legend to right side
	}

	/**
	 * returns the shortest delivery time
	 */
	private int getShortestDeliveryTime() {
		int shortestFifoTime = Collections.min(fifoTimes);
		int shortestKnapTime = Collections.min(knapSackTimes);
		int shortestDeliveryTime = shortestKnapTime;
		if (shortestDeliveryTime > shortestFifoTime) {
			shortestDeliveryTime = shortestFifoTime;
		}

		return shortestDeliveryTime;
	}

	/**
	 * @param totalNumberOfOrdersSimulated
	 */
	private int getLongestDeliveryTime() {
		int longestFifoTime = Collections.max(fifoTimes);
		int longestKnapTime = Collections.max(knapSackTimes);
		int longestDeliveryTime = longestKnapTime;
		if (longestDeliveryTime < longestFifoTime) {
			longestDeliveryTime = longestFifoTime;
		}

		return longestDeliveryTime;
	}

}
