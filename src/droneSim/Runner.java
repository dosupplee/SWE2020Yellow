package droneSim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import ui.BarChartScreen;
import ui.TimeGraph;

public class Runner {

	public Runner() {
		// current setup
		currentSetup = new CurrentSetup();
	}

	private CurrentSetup currentSetup;

	private enum Packager {
		FIFO, Knapsack
	};

	private int longestf;
	private int shortestf;
	private double avgf;

	private int longestk;
	private int shortestk;
	private double avgk;

	private ArrayList<Integer> deliveryTimesFifo;
	private ArrayList<Integer> deliveryTimesKnapsack;

	private boolean simRunning = false;
	private StringBuilder rawDataTextSB;
	private StringBuilder displayTextSB;

	/**
	 * 
	 * @return (String builder for display text, String builder for saved text)
	 */
	public Tuple run() {
		simRunning = true;

		long startTime = new Date().getTime();
		rawDataTextSB = new StringBuilder(); // text to save to csv file
		displayTextSB = new StringBuilder(); // text to display to log screen
		
		// create the csv header
		createRawDataCSVheader();

		// Hash map of (time -> numOrders)
		HashMap<Time, Integer> map = new HashMap<>();

		// generate all orders to XML
		XMLOrderGenerator gen = new XMLOrderGenerator(currentSetup);
		gen.generateAllOrders();

		XMLOrderParser parse = new XMLOrderParser("order.xml", currentSetup);

		Packager packagerType = Packager.Knapsack;
		int numOrders = 0;

		// Create Knapsack packer
		KnapsackPacker kp = new KnapsackPacker(currentSetup);
		deliveryTimesKnapsack = new ArrayList<>();

		// Create FIFO packer
		FIFOPacker fp = new FIFOPacker(currentSetup);
		deliveryTimesFifo = new ArrayList<>();

		// This Section Mimics how we will be occasionally refreshing our order backlog
		// list at certain times

		// keep track of current time
		Time currentTime = new Time();

		// Repeat whole simulation for each packager type (for now???)
		for (int type = 0; type < 2; type++) {

			ArrayList<Order> orderBacklog = new ArrayList<Order>();
			ArrayList<Order> packedOrders = new ArrayList<Order>();
			numOrders = 0;
			parse.reset();

			for (int shift = 0; shift < currentSetup.getNumShifts(); shift++) // for each shift
			{
				
				boolean canStopLastHour = false; // if we can stop our final hour overRun
				for (int hour = 0; hour < 4; hour++) // for each hour
				{
					int min = 0;
					// while either is one of the first three hours or is the final hour and have
					// finished all orders
					while ((min < 60 && hour <= 2) || ((min < 60 || canStopLastHour == false) && hour == 3)) {
						
						
						String time = Integer.toString(hour) + ":" + Integer.toString(min) + ":00";

						// grab new orders from parser
						ArrayList<Order> newOrders = parse.getNewOrders(shift, time);

						// Maintain count of orders processed (for statistical analysis)
						numOrders += newOrders.size();

						// Append now-valid orders to the delivery backlog
						orderBacklog.addAll(newOrders);

						// add to csv graph
						if (type == 0 && newOrders.size() != 0) { // only do it once, and only show times where there
																	// are orders
							
							for (Order order : newOrders) {
								Time timeObj = new Time(order.getOrderTime());
								int oldVal = map.getOrDefault(timeObj, 0);

								map.put(timeObj, newOrders.size() + oldVal);
							}

						}


						// determine the orders to pack
						if (packagerType == Packager.Knapsack) {
							packedOrders = kp.pack(orderBacklog);
						} else {
							packedOrders = fp.pack(orderBacklog);
						}



						/*
						 * Here we would take the packed orders and add them to drones list As well as
						 * instigate drone's tsp
						 * 
						 * Increment currentTime accordingly (how long the trip took + recharge of
						 * batteries)
						 */

						// send the drone out
						Tuple tripResult = currentSetup.sendDrone(packedOrders, currentTime);
						int secondsTaken = (int) tripResult.getA(); // get the time the trip took
						ArrayList<Integer> curDeliveryTimes = (ArrayList<Integer>) tripResult.getB(); // list of
																										// delivery
																										// times in
																										// seconds
						
						
						// if no delivery made, dont update stats
						if (secondsTaken != 0) {
							String packingMethod = "null";
							
							// add the delivery times
							if (packagerType == Packager.Knapsack) {
								deliveryTimesKnapsack.addAll(curDeliveryTimes);
								packingMethod = "KNAPSACK";
							} else if (packagerType == Packager.FIFO) {
								deliveryTimesFifo.addAll(curDeliveryTimes);
								packingMethod = "FIFO";
							}
							
							// append to csv string builder
							for (Order order : packedOrders) {
								rawDataTextSB.append(packingMethod +"," + shift + "," + order.getOrderTime() + "," +  order.getDroneSentTime() + "," + order.getDeliveryTime() + "," + order.getWaitTime() + "," + order.getDeliveryPoint().getName() + "\n");
							}
						}

						/*
						 * Here we would update time calcs for orders after TSP is done
						 */

						// if have gotten the last few orders from that shift
						if (hour == 3 && min >= 60 && orderBacklog.size() == 0) {
							canStopLastHour = true;
						}

						int incrementMin = 1;

						
						if(secondsTaken!=0)
						{
							//if we had a flight, increment timers by flightTime + drone turn around time
							incrementMin = Math.round(secondsTaken / 60) + (currentSetup.getDrone().getTurnAroundTime()/60);
						}
						
						
						// increment timer
						min += incrementMin;
						currentTime.incrementTimerMinute(incrementMin);
						
					}
				}


				// Increment the shift and reset the timer
				currentTime.incrementShift();
				currentTime.resetTimer();
			}

			// Switch between the two packager types
			if (packagerType == Packager.Knapsack) {
				packagerType = Packager.FIFO;
			} else {
				packagerType = Packager.Knapsack;
			}
		}


		// -------------------------------------------------
		// get Stats
		// -------------------------------------------------
		getTimeStatistics(); // avg, long, short


		// create bar chart
		createBarChart();

		// set display text
		appentResultsToCSV(startTime, numOrders);
		
		// create order chart
		createOrderChart(map);

		simRunning = false;
		Tuple results = new Tuple(displayTextSB, rawDataTextSB);
		return results;
	}

	/**
	 * @param map
	 */
	private void createOrderChart(HashMap<Time, Integer> map) {
		// TreeMap to store sorted values of HashMap by time
		TreeMap<Time, Integer> sortedOrders = new TreeMap<>(map);
		TimeGraph timeGraph = new TimeGraph();
		timeGraph.createDataSet(sortedOrders, currentSetup.getNumShifts());
		timeGraph.showGraph();
	}

	/**
	 * @param startTime
	 * @param numOrders
	 */
	private void appentResultsToCSV(long startTime, int numOrders) {
		// add results to string builder for output to screen
		displayTextSB.append("SIMULATION DELIVERY TIME RESULTS:\n----");

		displayTextSB.append("\nKNAPSACK:");
		String fastestS = String.format("\nFastest:\t%d seconds → %.2f minutes", shortestf, (shortestf / 60.0));
		String slowestS = String.format("\nSlowest:\t%d seconds → %.2f minutes", longestf, (longestf / 60.0));
		String avgS = String.format("\nAverage:\t%.2f seconds → %.2f minutes", avgf, (avgf / 60.0));

		// displayTextSB.append("\nSum Knapsack: " + sumKnapsack);
		displayTextSB.append(fastestS);
		displayTextSB.append(slowestS);
		displayTextSB.append(avgS);

		displayTextSB.append("\n\nFIFO:");
		String fastestK = String.format("\nFastest:\t%d seconds → %.2f minutes", shortestk, (shortestk / 60.0));
		String slowestK = String.format("\nSlowest:\t%d seconds → %.2f minutes", longestk, (longestk / 60.0));
		String avgK = String.format("\nAverage:\t%.2f seconds → %.2f minutes", avgk, (avgk / 60.0));

		// displayTextSB.append("\n\nSum FIFO: " + sumFIFO);
		displayTextSB.append(fastestK);
		displayTextSB.append(slowestK);
		displayTextSB.append(avgK);


		long stopTime = new Date().getTime();
		double runTime = (stopTime - startTime) / 1000.0;
		displayTextSB.append("\n----\nNumber of Orders Simulated: " + numOrders);
		displayTextSB.append("\nSimulation Took: ");
		displayTextSB.append(runTime + " seconds");
	}

	/**
	 * 
	 */
	private void createBarChart() {
		BarChartScreen barChartScreen = new BarChartScreen(currentSetup);
		barChartScreen.init(deliveryTimesFifo, deliveryTimesKnapsack);
	}


	/**
	 * calculates the average, shortest, and longest delivery times
	 */
	private void getTimeStatistics() {
		avgf = 0;
		for (int timeVal : deliveryTimesFifo) {
			avgf += timeVal;
		}
		avgf = (avgf / deliveryTimesFifo.size());
		longestf = Collections.max(deliveryTimesFifo);
		shortestf = Collections.min(deliveryTimesFifo);


		avgk = 0;
		for (int timeVal : deliveryTimesKnapsack) {
			avgk += timeVal;
		}
		avgk = (avgk / deliveryTimesKnapsack.size());
		longestk = Collections.max(deliveryTimesKnapsack);
		shortestk = Collections.min(deliveryTimesKnapsack);
	
	}

	/**
	 * 
	 */
	private void createRawDataCSVheader() {
		rawDataTextSB.append("Packing Method,Shift,Order Time,Drone Send Time,Delivery Time,Wait Time (seconds),Delivery Location\n");
	}

	public boolean isRunning() {
		return simRunning;
	}

	public CurrentSetup getCurrentSetup() {
		return currentSetup;
	}

	public StringBuilder getLogStringBuilder() {
		return rawDataTextSB;
	}

	public StringBuilder getDisplayStringBuilder() {
		return displayTextSB;
	}
}