package droneSim;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;


/**
 * Credits to: http://zetcode.com/java/jfreechart/
 * - create data set
 * - show graph
 * - save graph
 * @author LEHMANIT17
 *
 */
public class Graph extends JFrame {
	

	private static final long serialVersionUID = 1L;
	private String xTitle, yTitle, chartTitle, frameTitle; // titles
	private JFreeChart chart;  // the chart instance
	private XYSeriesCollection dataset; // the data set to chart
	private ChartPanel chartPanel; // display panel



	/**
	 * Graph Constructor: takes in all titles
	 * @param xTitle
	 * @param yTitle
	 * @param chartTitle
	 * @param frameTitle
	 */
	public Graph(String xTitle, String yTitle, String chartTitle, String frameTitle) {
		this.xTitle = xTitle;
		this.yTitle = yTitle;
		this.chartTitle = chartTitle;
		this.frameTitle = frameTitle;
	}


	
	/**
	 * Creates an XY data set to be ploted
	 * 
	 * @return
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
	
	private void createChart() {

		/*
		 * The ChartFactory.createXYLineChart() creates a new line chart. The parameters
		 * of the method are: chart title, X axis label, Y axis label, data, plot
		 * orientation, and three flags indicating whether to show legend, tooltips, and
		 * URLs.
		 */
		chart = ChartFactory.createXYLineChart(chartTitle, // title
				xTitle, // x axis
				yTitle, // y axis
				dataset, 
				PlotOrientation.VERTICAL, 
				true, true, false);
		

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
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//		renderer.setSeriesPaint(0, Color.RED);
//		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		plot.setRenderer(renderer);
		
		// Sets the background colour of the plot area.
		plot.setBackgroundPaint(Color.white);

		// Show the grid lines and paint them in black colour.
		plot.setRangeGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);

		plot.setDomainGridlinesVisible(true);
		plot.setDomainGridlinePaint(Color.BLACK);

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
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
	
}