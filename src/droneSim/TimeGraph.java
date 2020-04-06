package droneSim;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class TimeGraph extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private String tTitle, yTitle, chartTitle, frameTitle; // titles
	private JFreeChart chart;  // the chart instance
	private TimeSeriesCollection  dataset; // the data set to chart
	private ChartPanel chartPanel; // display panel



	/**
	 * Graph Constructor: takes in all titles
	 * @param xTitle
	 * @param yTitle
	 * @param chartTitle
	 * @param frameTitle
	 */
	public TimeGraph(String tTitle, String yTitle, String chartTitle, String frameTitle) {
		this.tTitle = tTitle;
		this.yTitle = yTitle;
		this.chartTitle = chartTitle;
		this.frameTitle = frameTitle;
	}
	
	/**
	 * Blank Constructor
	 */
	public TimeGraph() {
		this.tTitle = "Time";
		this.yTitle = "y";
		this.chartTitle = "Ty chart";
		this.frameTitle = "Time vs. y";
	}

	


	/**
	 * Creates an XY data set to be ploted
	 * Takes in a csvFile
	 * 
	 * CSV file setup:
	 * -----------------------------------
	 * <Frame Title>
	 * <Chart Title>
	 * <tAxis Title>
	 * <yAxis Title>
	 * <s1 name>,<s2 name>,...,<sn name>
	 * ###################################
	 * <h:m:s>,<y>,...,<y>
	 * <h:m:s>,<y>,...,<y>
	 * .
	 * .
	 * .
	 * <h:m:s>,<y>,...,<y>
	 * -----------------------------------
	 * 
	 * 
	 * @return
	 */
	public void createDataSet(File csvFile) {
		String[] seriesNames; // names of ty sets
		ArrayList<Second> seriesValsT = new ArrayList<>(); // t values
		ArrayList<ArrayList<Integer>> seriesValsY = new ArrayList<>(); // y values
		dataset = new TimeSeriesCollection (); // the data set of all the series
		
		try {
			Scanner fileScn = new Scanner(csvFile);
			
			// -------------------------
			// get title and headers
			// -------------------------
			frameTitle = fileScn.nextLine().trim(); // frame title
			chartTitle = fileScn.nextLine().trim(); // chart title
			tTitle     = fileScn.nextLine().trim(); // t axis title
			yTitle     = fileScn.nextLine().trim(); // y axis title
			seriesNames = fileScn.nextLine().trim().split(","); // series names
			fileScn.nextLine(); // skip the blank line

			// -------------------------
			// intialise the y series
			// -------------------------
			for (int i = 0; i < seriesNames.length; i++) {
				seriesValsY.add(new ArrayList<Integer>());
			}
			
			// -------------------------
			// extract the t,y series
			// -------------------------			
			while (fileScn.hasNextLine()) {
				// get line and seperate values
				String curLine = fileScn.nextLine();
				String[] line = curLine.split(",", -1); 
				
				// parse time
				String timeS = line[0].trim();
				Time t = new Time(timeS);
				// Create a Second object
				Second curSec = new Second(t.getStartSecond(), t.getStartMinute(), t.getStartHour(), 1, 1, 2020);
				seriesValsT.add(curSec);
				
				for (int i = 1; i < line.length; i++) { // start at first value after time
					if (!line[i].trim().equals("")) {  // if not empty
						// parse value
						Integer y = Integer.parseInt(line[i].trim());
						
						// add value to series
						seriesValsY.get(i - 1).add(y);
						//seriesValsY.get(i-1).addor
					}
				}
			}
			
			// close the file
			fileScn.close();
			
			// -------------------------
			// add the t,y series to dataset
			// -------------------------
			for (int s = 0; s < seriesNames.length; s++) {
				String curSeries = seriesNames[s].trim(); // get the name
				TimeSeries  series = new TimeSeries(curSeries); // create the series
				
				for (int val = 0; val < seriesValsT.size(); val++) {
					series.add(seriesValsT.get(val), seriesValsY.get(s).get(val)); // add (t,y)
				}
				dataset.addSeries(series); // add to dataset
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Creates an TY data set to be ploted
	 * @param seriesNames
	 * 	- String array
	 * @param seriesValsT
	 * 	- Time array
	 * @param seriesValsY
	 *  - int array
	 */
	public void createDataSet(String[] seriesNames, Time[] seriesValsT, int[][] seriesValsY) {
		// initialise the dataset
		dataset = new TimeSeriesCollection ();
		
		// unpack the inputed series
		for (int i = 0; i < seriesNames.length; i++) {
			String curSeries = seriesNames[i]; // get the name
			TimeSeries  series = new TimeSeries(curSeries); // create the current series
			
			for (int j = 0; j < seriesValsT.length; j++) {
				Second curSec = new Second(seriesValsT[j].getStartSecond(), seriesValsT[j].getStartMinute(), seriesValsT[j].getStartHour(), 1, 1, 2020);
				series.add(curSec, seriesValsY[i][j]);
			}
			
			// add series to dataset
			dataset.addSeries(series);
		}
	}
	
	private void createChart() {

		/*
		 * The ChartFactory.createXYLineChart() creates a new line chart. The parameters
		 * of the method are: chart title, X axis label, Y axis label, data, plot
		 * orientation, and three flags indicating whether to show legend, tooltips, and
		 * URLs.
		 */
		chart = ChartFactory.createScatterPlot(
				chartTitle, // title
				tTitle, // x axis
				yTitle, // y axis
				dataset);
		

		// Get a reference to the plot in order to customize it.
		XYPlot plot = chart.getXYPlot();

		/*
		 * Set a stroke and a colour for the line of the chart. 
		 * XYLineAndShapeRenderer is an object that connects data 
		 * points with lines and/or draws shapes at each data point. 
		 * The renderer is set with the setRenderer() method.
		 * 
		 * Can calulate on own though...
		 */
		//XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//		renderer.setSeriesPaint(0, Color.RED);
//		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		//plot.setRenderer(renderer);
		
		// Sets the background colour of the plot area.
		plot.setBackgroundPaint(Color.white);

		// Show the grid lines and paint them in black colour.
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);
		
		// format time axis
		DateAxis axis = new DateAxis();
        axis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss a"));
        axis.setVerticalTickLabels(true);
        plot.setDomainAxis(axis);
        
        
		// Remove the border around the legend.
		chart.getLegend().setFrame(BlockBorder.NONE);

		// Create a chart title with a new font.
		chart.setTitle(new TextTitle(chartTitle, new Font("Serif", java.awt.Font.BOLD, 18)));

	}
	
	
	/**
	 * Sets up all graph UI
	 */
	private void run() {
		//----------------------------
		// initialise
		// ----------------------------
		createChart();

		chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10)); // t,l,b,r
		chartPanel.setBackground(Color.white);
		add(chartPanel);

		pack();
		setTitle(frameTitle);
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits all graphs
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);  // closes only current graph
		
		//----------------------------
		// Display
		// ----------------------------
		setVisible(true);
	}
	
	/**
	 * Display the Graph
	 */
	public void showGraph() {
		
		// More info here: https://stackoverflow.com/questions/22534356/java-awt-eventqueue-invokelater-explained
		EventQueue.invokeLater(() -> run());
	}
	
	

	
	//----------------------------
	// Getters & Setters
	// ----------------------------
	/**
	 * @return the xTitle
	 */
	public String gettTitle() {
		return tTitle;
	}


	/**
	 * @param xTitle the xTitle to set
	 */
	public void settTitle(String xTitle) {
		this.tTitle = xTitle;
	}


	/**
	 * @return the yTitle
	 */
	public String getyTitle() {
		return yTitle;
	}


	/**
	 * @param yTitle the yTitle to set
	 */
	public void setyTitle(String yTitle) {
		this.yTitle = yTitle;
	}


	/**
	 * @return the chartTitle
	 */
	public String getChartTitle() {
		return chartTitle;
	}


	/**
	 * @param chartTitle the chartTitle to set
	 */
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}


	/**
	 * @return the frameTitle
	 */
	public String getFrameTitle() {
		return frameTitle;
	}


	/**
	 * @param frameTitle the frameTitle to set
	 */
	public void setFrameTitle(String frameTitle) {
		this.frameTitle = frameTitle;
	}
	
}