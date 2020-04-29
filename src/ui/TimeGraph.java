package ui;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import droneSim.Time;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class TimeGraph extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private String tTitle, yTitle, chartTitle, frameTitle; // titles
	private JFreeChart chart;  // the chart instance
	private TimeSeriesCollection  dataset, avgDataSet; // the data set to chart
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
	 * creates the series to be plottes
	 * @param values
	 * @param numShifts
	 */
	public void createDataSet(TreeMap<Time, Integer> values, int numShifts) {
		
		dataset = new TimeSeriesCollection (); // the data set of all the series
		avgDataSet = new TimeSeriesCollection();


		// -------------------------
		// set title and headers
		// -------------------------
		frameTitle = "Orders Over Time Chart"; // frame title
		chartTitle = "# of Orders vs. Time for " + numShifts + " shifts\n"; // chart title
		tTitle = "Time"; // t axis title
		yTitle = "# of Orders"; // y axis title

		

		// -------------------------
		// intialise and extract the t,y series
		// -------------------------
		String seriesName = "# of orders";
		TimeSeries series = new TimeSeries(seriesName);
		
		// add data to series
		for (Time t : values.keySet()) {
			Second curSec = new Second(t.getStartSecond(), t.getStartMinute(), t.getStartHour(), 1, 1, 2020);
			Integer numOrders = values.get(t);
			series.addOrUpdate(curSec, numOrders); // add (t,y)
		}
		
		// create moving average series
		TimeSeries movingAvg = MovingAverage.createMovingAverage(series, "Average", 1600, 0); // avg of cur series
		dataset.addSeries(series); // add to dataset
		avgDataSet.addSeries(movingAvg); // add to dataset
	}
	

	
	
	private void createChart() {
		
		NumberAxis rangeAxis = new NumberAxis();
		// format time axis
		DateAxis domainAxis = new DateAxis();
		domainAxis.setDateFormatOverride(new SimpleDateFormat("hh:mm:ss a"));
		domainAxis.setVerticalTickLabels(true);
				
		        
	   

		// Get a reference to the plot in order to customize it.
		XYPlot plot = new XYPlot();
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(rangeAxis);
		XYLineAndShapeRenderer rend0 = new XYLineAndShapeRenderer(true,false);
		XYLineAndShapeRenderer rend1 = new XYLineAndShapeRenderer(false,true);
		plot.setDataset(0, avgDataSet);
		plot.setRenderer(0,rend0);
		plot.setDataset(1,dataset);
		plot.setRenderer(1,rend1);
		plot.getDomainAxis().setLabel(tTitle);
		plot.getRangeAxis().setLabel(yTitle);

		
		// Sets the background colour of the plot area.
		plot.setBackgroundPaint(Color.white);

		chart = new JFreeChart(chartTitle, JFreeChart.DEFAULT_TITLE_FONT, plot, true);

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