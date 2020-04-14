package droneSim;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;



/**
 * Credits to: http://zetcode.com/java/jfreechart/
 * - create data set
 * - show graph
 * - save graph
 * @author LEHMANIT17
 *
 */
public class XYGraph extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private String xTitle, yTitle, chartTitle, frameTitle; // titles
	private JFreeChart chart;  // the chart instance
	private XYSeriesCollection dataset, avgDataset; // the data set to chart
	private ChartPanel chartPanel; // display panel



	/**
	 * Graph Constructor: takes in all titles
	 * @param xTitle
	 * @param yTitle
	 * @param chartTitle
	 * @param frameTitle
	 */
	public XYGraph(String xTitle, String yTitle, String chartTitle, String frameTitle) {
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.chartTitle = chartTitle;
		this.frameTitle = frameTitle;
	}
	
	/**
	 * Blank Constructor
	 */
	public XYGraph() {
		this.xTitle = "x";
		this.yTitle = "y";
		this.chartTitle = "xy chart";
		this.frameTitle = "x vs. y";
	}

	


	/**
	 * Creates an XY data set to be ploted
	 * Takes in a csvFile
	 * 
	 * CSV file setup:
	 * -----------------------------------
	 * <Frame Title>
	 * <Chart Title>
	 * <xAxis Title>
	 * <yAxis Title>
	 * <s1 name>,<s2 name>,...,<sn name>
	 * ###################################
	 * <x,y>,<x,y>,...,<x,y>
	 * <x,y>,<x,y>,...,<x,y>
	 * .
	 * .
	 * .
	 * <x,y>,<x,y>,...,<x,y>
	 * -----------------------------------
	 * 
	 * 
	 * @return
	 */
	public void createDataSet(File csvFile) {
		String[] seriesNames; // names of xy sets
		ArrayList<ArrayList<Integer>> seriesValsX = new ArrayList<>(); // x values
		ArrayList<ArrayList<Integer>> seriesValsY = new ArrayList<>(); // y values
		dataset = new XYSeriesCollection(); // the data set of all the series
		
		try {
			Scanner fileScn = new Scanner(csvFile);
			
			// -------------------------
			// get title and headers
			// -------------------------
			frameTitle = fileScn.nextLine().trim(); // frame title
			chartTitle = fileScn.nextLine().trim(); // chart title
			xTitle     = fileScn.nextLine().trim(); // x axis title
			yTitle     = fileScn.nextLine().trim(); // y axis title
			seriesNames = fileScn.nextLine().trim().split(","); // series names
			fileScn.nextLine(); // skip the blank line

			// -------------------------
			// intialise the x,y series
			// -------------------------
			for (int i = 0; i < seriesNames.length; i++) {
				seriesValsX.add(new ArrayList<Integer>());
				seriesValsY.add(new ArrayList<Integer>());
			}
			
			// -------------------------
			// extract the x,y series
			// -------------------------			
			while (fileScn.hasNextLine()) {
				// get line and seperate values
				String curLine = fileScn.nextLine();
				String[] line = curLine.split(",", -1); 
				
				for (int i = 0; i < line.length - 1; i+=2) {
					if (!line[i].trim().equals("") && !line[i+1].trim().equals("")) {  // if not empty
						// parse values
						Integer x1 = Integer.parseInt(line[i].trim());
						Integer y1 = Integer.parseInt(line[i+1].trim());
						
						// add to series
						seriesValsX.get(i/2).add(x1);	
						seriesValsY.get(i/2).add(y1);
					}
				}
			}
			
			fileScn.close();
			
			// -------------------------
			// add the x,y series to dataset
			// -------------------------
			for (int s = 0; s < seriesNames.length; s++) {
				String curSeries = seriesNames[s]; // get the name
				XYSeries series = new XYSeries(curSeries); // create the series
				
				for (int val = 0; val < seriesValsX.get(s).size(); val++) {
					series.add(seriesValsX.get(s).get(val), seriesValsY.get(s).get(val));
				}
				dataset.addSeries(series); // add to dataset
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/**
	 * Creates an XY data set to be ploted
	 * @param seriesNames
	 * @param seriesValsX
	 * @param seriesValsY
	 */
	public void createDataSet(String[] seriesNames, int[][] seriesValsX, int[][] seriesValsY) {
		// initialise the dataset
		dataset = new XYSeriesCollection();
		
		
		// unpack the inputed series
		for (int i = 0; i < seriesNames.length; i++) {
			String curSeries = seriesNames[i]; // get the name
			XYSeries series = new XYSeries(curSeries);
			
			for (int j = 0; j < seriesValsX[i].length; j++) {
				series.add(seriesValsX[i][j], seriesValsY[i][j]);
			}
			
			// add series to dataset
			dataset.addSeries(series);
		}
	}
	
	/**
	 * Creates an XY data set to be ploted
	 * @param seriesNames
	 * @param seriesValsX
	 * @param seriesValsY
	 */
	public void createDataSet(String[] seriesNames, ArrayList<Integer> fifoVals, ArrayList<Integer> kanpsackVals) {
		// initialise the dataset
		dataset = new XYSeriesCollection();
		avgDataset = new XYSeriesCollection();
		
		// do fifo
		
		// unpack the inputed series
		String curSeries = seriesNames[0]; // get the name
		XYSeries seriesFIFO = new XYSeries(curSeries);

		for (int j = 0; j < fifoVals.size(); j++) {
			seriesFIFO.add(j + 1, fifoVals.get(j));
		}

		// add series to dataset
		dataset.addSeries(seriesFIFO);

		
		// do knapsack
		
		curSeries = seriesNames[1]; // get the name
		XYSeries seriesKnapsack = new XYSeries(curSeries);

		for (int j = 0; j < kanpsackVals.size(); j++) {
			seriesKnapsack.add(j + 1, kanpsackVals.get(j));
		}

		// add series to dataset
		dataset.addSeries(seriesKnapsack);
		
		//createMovingAverage(XYDataset source, int series, String name, double period, double skip)
		XYSeriesCollection fifoAvg = new XYSeriesCollection();
		fifoAvg.addSeries(seriesFIFO);
		XYSeries movingAvgF = MovingAverage.createMovingAverage(fifoAvg, 0, "Fifo Average", 250.0, 0.0); // avg of cur series 
		avgDataset.addSeries(movingAvgF); // add to dataset
		
		//createMovingAverage(XYDataset source, int series, String name, double period, double skip)
		XYSeriesCollection knapAvg = new XYSeriesCollection();
		knapAvg.addSeries(seriesKnapsack);
		XYSeries movingAvgK = MovingAverage.createMovingAverage(knapAvg, 0, "Knapsack Average", 250.0, 0.0); // avg of cur series 
		avgDataset.addSeries(movingAvgK); // add to dataset
		
	}
	
	private void createChart() {
		


		NumberAxis rangeAxis = new NumberAxis();
		rangeAxis.setAutoRangeIncludesZero(false); // so graph doesn't need tp include 0
		NumberAxis domainAxis = new NumberAxis();

		// Get a reference to the plot in order to customize it.
		XYPlot plot = new XYPlot();
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(rangeAxis);
		XYLineAndShapeRenderer dotRenderer = new XYLineAndShapeRenderer(false, true);
		XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
		
		
		// set line/dot colors
		dotRenderer.setSeriesPaint(0, Color.GRAY);
		dotRenderer.setSeriesPaint(1, Color.GREEN);
		lineRenderer.setSeriesPaint(0, Color.RED);
		lineRenderer.setSeriesPaint(1, Color.BLUE);
		
		plot.setDataset(0, avgDataset);
		plot.setRenderer(0, lineRenderer);
		plot.setDataset(1, dataset);
		plot.setRenderer(1, dotRenderer);
		
		// Sets the background colour of the plot area.
		plot.setBackgroundPaint(Color.white);
		plot.getDomainAxis().setLabel(xTitle);
		plot.getRangeAxis().setLabel(yTitle);
		
		// create the chart
		chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);


//		/*
//		 * The ChartFactory.createXYLineChart() creates a new line chart. The parameters
//		 * of the method are: chart title, X axis label, Y axis label, data, plot
//		 * orientation, and three flags indicating whether to show legend, tooltips, and
//		 * URLs.
//		 */
//		chart = ChartFactory.createXYLineChart(chartTitle, // title
//				xTitle, // x axis
//				yTitle, // y axis
//				dataset, 
//				PlotOrientation.VERTICAL, 
//				true, true, false);
//		
//
//		// Get a reference to the plot in order to customize it.
//		XYPlot plot = chart.getXYPlot();
//
//		/*
//		 * Set a stroke and a colour for the line of the chart. 
//		 * XYLineAndShapeRenderer is an object that connects data 
//		 * points with lines and/or draws shapes at each data point. 
//		 * The renderer is set with the setRenderer() method.
//		 * 
//		 * Can calulate on own though...
//		 */
//		XYLineAndShapeRenderer dotRenderer = new XYLineAndShapeRenderer(false, true);
//		XYLineAndShapeRenderer lineRenderer = new XYLineAndShapeRenderer(true, false);
////		renderer.setSeriesPaint(0, Color.RED);
////		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
//		//plot.setRenderer(dotRenderer);
//		plot.setRenderer(0, dotRenderer);
//		plot.setRenderer(1, dotRenderer);
//		plot.setRenderer(2, lineRenderer);
//		plot.setRenderer(3, lineRenderer);
//		
//		// Sets the background colour of the plot area.
//		plot.setBackgroundPaint(Color.white);
//
//		// Show the grid lines and paint them in black colour.
//		plot.setRangeGridlinesVisible(true);
//		plot.setRangeGridlinePaint(Color.BLACK);
//
//		plot.setDomainGridlinesVisible(true);
//		plot.setDomainGridlinePaint(Color.BLACK);
//
//		// Remove the border around the legend.
//		chart.getLegend().setFrame(BlockBorder.NONE);
//
//		// Create a chart title with a new font.
//		chart.setTitle(new TextTitle(chartTitle, new Font("Serif", java.awt.Font.BOLD, 18)));

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
	public String getxTitle() {
		return xTitle;
	}


	/**
	 * @param xTitle the xTitle to set
	 */
	public void setxTitle(String xTitle) {
		this.xTitle = xTitle;
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