package droneSim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;




public class Runner {

	public Runner() {
		// current setup
		currentSetup = new CurrentSetup();
	}

	private CurrentSetup currentSetup;

	private enum Packager {
		FIFO, Knapsack
	};

	private double slowestTimeFIFO;
	private double fastestTimeFIFO;
	private double sumFIFO;

	private double slowestTimeKnapsack;
	private double fastestTimeKnapsack;
	private double sumKnapsack;
	
	
	private ArrayList<Integer> deliveryTimesFifo;
	private ArrayList<Integer> deliveryTimesKnapsack;
	
	
	private boolean simRunning = false;
	private StringBuilder csvTextSB;
	private StringBuilder displayTextSB;

	/**
	 * 
	 * @return (String builder for display text, String builder for saved text)
	 */
	public Tuple run() {
		simRunning = true;
		
		
		long startTime = new Date().getTime();
		csvTextSB = new StringBuilder(); // text to save to log file
		displayTextSB = new StringBuilder(); // text to display to log screen
		// Hash map of (time -> numOrders)
		HashMap<Time, Integer> map = new HashMap<>();
		
		
		/*
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
		 */
		// create csv log headers
		csvTextSB.append("Orders Over Time Chart\n");
		csvTextSB.append("# of Orders vs. Time for " +  currentSetup.getNumShifts() + " shifts\n");
		csvTextSB.append("Time\n");
		csvTextSB.append("# of Orders\n");
		csvTextSB.append("# of Orders\n");
		csvTextSB.append("########################\n");
		
		
		// generate all orders to XML
		XMLOrderGenerator gen = new XMLOrderGenerator(currentSetup);
		gen.generateAllOrders();

		XMLOrderParser parse = new XMLOrderParser("order.xml", currentSetup);


		Packager packagerType = Packager.Knapsack;
		int numOrders = 0;

		// Create Knapsack packer
		KnapsackPacker kp = new KnapsackPacker(currentSetup);
		sumKnapsack = 0;
		slowestTimeKnapsack = 0.0;
		fastestTimeKnapsack = Double.MAX_VALUE;
		deliveryTimesKnapsack = new ArrayList<>();

		// Create FIFO packer
		FIFOPacker fp = new FIFOPacker(currentSetup);
		sumFIFO = 0;
		slowestTimeFIFO = 0.0;
		fastestTimeFIFO = Double.MAX_VALUE;
		deliveryTimesFifo = new ArrayList<>();

		// This Section Mimics how we will be occasionally refreshing our order backlog
		// list at certain times

		// keep track of current time
		Time currentTime = new Time();
		int counter = 0;

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
						if (type == 0 && orderBacklog.size() != 0) { // only do it once, and only show times where there are orders
							Time timeObj = new Time(time);
							int oldVal = map.getOrDefault(timeObj, 0);
							if (oldVal != 0) {
								System.out.println(oldVal);
							}
							counter++;
							map.put(timeObj,  orderBacklog.size() + oldVal);
						}
						

						// list new orders grabbed
						for (int i = 0; i < newOrders.size(); i++) {
							Order order = newOrders.get(i);
						}
						

						for (int i = 0; i < orderBacklog.size(); i++) {
							Order order = orderBacklog.get(i);
						}

						if (packagerType == Packager.Knapsack) {
							packedOrders = kp.pack(orderBacklog);
						} else {
							packedOrders = fp.pack(orderBacklog);
						}

						// list new orders grabbed
						if (packedOrders != null) {
							for (int i = 0; i < packedOrders.size(); i++) {
								Order order = packedOrders.get(i);
							}
						}

						// list skipped orders
						if (packedOrders != null) {
							for (int i = 0; i < kp.skippedOrders.size(); i++) {
								Order order = kp.skippedOrders.get(i);
							}
						}

						/*
						 * Here we would take the packed orders and add them to drones list As well as
						 * instigate drone's tsp
						 * 
						 * Increment currentTime accordingly (how long the trip took + recharge of
						 * batteries)
						 */
						Tuple tripResult = currentSetup.sendDrone(packedOrders);
						int secondsTaken = (int) tripResult.getA(); // get the time the trip took
						//String pathTaken = (String) tripResult.getB(); //TODO save somewhere
						ArrayList<Integer> curDeliveryTimes = (ArrayList<Integer>) tripResult.getB(); // list of delivery times in seconds
						
						// update the slowest and fastest times + add delivery times
						if (packagerType == Packager.Knapsack) {
							sumKnapsack += secondsTaken;
							if (secondsTaken > slowestTimeKnapsack) {
								slowestTimeKnapsack = secondsTaken;
							}
							if (secondsTaken < fastestTimeKnapsack) {
								fastestTimeKnapsack = secondsTaken;
							}
							
							// add delivery times
							deliveryTimesKnapsack.addAll(curDeliveryTimes);
						} else if (packagerType == Packager.FIFO) {
							sumFIFO += secondsTaken;
							if (secondsTaken > slowestTimeFIFO) {
								slowestTimeFIFO = secondsTaken;
							}
							if (secondsTaken < fastestTimeFIFO) {
								fastestTimeFIFO = secondsTaken;
							}
							
							// add delivery times
							deliveryTimesFifo.addAll(curDeliveryTimes);
						}

						/*
						 * Here we would update time calcs for orders after TSP is done
						 */

						// if have gotten the last few orders from that shift
						if (hour == 3 && min >= 60 && newOrders.size() == 0)
							canStopLastHour = true;

						// increment timer
						min += Math.round(secondsTaken / 60);
						currentTime.incrementTimerMinute(Math.round(secondsTaken / 60));
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
		
		// print map to file
		// TreeMap to store sorted values of HashMap 
        TreeMap<Time, Integer> sorted = new TreeMap<>(map); 
		for (Time t : sorted.keySet()) {
			csvTextSB.append(t.toString() + ","+ sorted.get(t) +"\n");
		}
		
		// sort the delivery times largest to smallest
		//Collections.sort(deliveryTimesFifo, Collections.reverseOrder()); 
		//Collections.sort(deliveryTimesKnapsack, Collections.reverseOrder());
		
		
		// create xy chart
		XYGraph xyGraph = new XYGraph("Order #", "Delivery Time (Seconds)", "Delivery Times for FIFO & Knapsack", "Chart of Delivery Times");
		xyGraph.createDataSet(new String[] {"FIFO","Knapsack"}, deliveryTimesFifo, deliveryTimesKnapsack);
		xyGraph.showGraph();
		
		
		// add results to string builder for output to screen
		displayTextSB.append("Simulation Results:\n----");
		displayTextSB.append("\nTRIP STATS:");
		
		if (sumKnapsack > 1) {
			String fastestS = String.format("\nFastest Time Knapsack: %.3f seconds", fastestTimeKnapsack);
			String slowestS = String.format("\nSlowest Time Knapsack: %.3f seconds", slowestTimeKnapsack);
			String avgS = String.format("\nAverage Time Knapsack: %.3f seconds", (sumKnapsack / numOrders));
			
			//displayTextSB.append("\nSum Knapsack: " + sumKnapsack);
			displayTextSB.append(fastestS);
			displayTextSB.append(slowestS);
			displayTextSB.append(avgS);
		}

		if (sumFIFO > 1) {
			String fastestS = String.format("\n\nFastest Time FIFO: %.3f seconds", fastestTimeFIFO);
			String slowestS = String.format("\nSlowest Time FIFO: %.3f seconds", slowestTimeFIFO);
			String avgS = String.format("\nAverage Time FIFO: %.3f seconds", (sumFIFO / numOrders));
			
			//displayTextSB.append("\n\nSum FIFO: " + sumFIFO);
			displayTextSB.append(fastestS);
			displayTextSB.append(slowestS);
			displayTextSB.append(avgS);
		}

		long stopTime = new Date().getTime();
		double runTime = (stopTime - startTime) / 1000.0;
		displayTextSB.append("\n----\nNumber of Orders Simulated: " + numOrders);
		displayTextSB.append("\nSimulation Took: ");
		displayTextSB.append(runTime + " seconds");
		//sBuilder.append("\n<terminated>");

		simRunning = false;
		Tuple results = new Tuple(displayTextSB, csvTextSB);
		return results;
	}
	
	public boolean isRunning() {
		return simRunning;
	}

	public CurrentSetup getCurrentSetup() {
		return currentSetup;
	}

	public StringBuilder getLogStringBuilder() {
		return csvTextSB;
	}
	
	public StringBuilder getDisplayStringBuilder() {
		return displayTextSB;
	}
}
