package droneSim;

import java.util.ArrayList;
import java.util.Date;


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
	
	private boolean simRunning = false;
	private StringBuilder allTextSB;
	private StringBuilder displayTextSB;

	/**
	 * 
	 * @return (String builder for display text, String builder for saved text)
	 */
	public Tuple run() {
		simRunning = true;
		long startTime = new Date().getTime();
		allTextSB = new StringBuilder(); // text to save to log file
		displayTextSB = new StringBuilder(); // text to display to log screen
		
		// generate all orders to XML
		XMLOrderGenerator gen = new XMLOrderGenerator(currentSetup);
		gen.generateAllOrders();

		XMLOrderParser parse = new XMLOrderParser("order.xml", currentSetup);

		allTextSB.append("\nOrders Generated");

		Packager packagerType = Packager.Knapsack;
		int numOrders = 0;

		// Create Knapsack packer
		KnapsackPacker kp = new KnapsackPacker(currentSetup);
		sumKnapsack = 0;
		slowestTimeKnapsack = Double.MAX_VALUE;
		fastestTimeKnapsack = 0.0;

		// Create FIFO packer
		FIFOPacker fp = new FIFOPacker(currentSetup);
		sumFIFO = 0;
		slowestTimeFIFO = Double.MAX_VALUE;
		fastestTimeFIFO = 0.0;

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
						allTextSB.append("\n\nShift (" + Integer.toString(shift) + ") - Current Time: "
								+ Integer.toString(hour) + ":" + Integer.toString(min) + ":00\n");
						String time = Integer.toString(hour) + ":" + Integer.toString(min) + ":00";

						// grab new orders from parser
						ArrayList<Order> newOrders = parse.getNewOrders(shift, time);

						// Maintain count of orders processed (for statistical analysis)
						numOrders += newOrders.size();

						// Append now-valid orders to the delivery backlog
						orderBacklog.addAll(newOrders);

						// list new orders grabbed
						allTextSB.append("\n\tNew Orders Received");
						for (int i = 0; i < newOrders.size(); i++) {
							Order order = newOrders.get(i);
							allTextSB.append("\n\t\t" + order.getOrderTime().toString() + " - "
									+ order.getMeal().getName() + " - " + order.getDeliveryPoint().getName());
						}

						allTextSB.append("\n\tAll Current Orders:");
						for (int i = 0; i < orderBacklog.size(); i++) {
							Order order = orderBacklog.get(i);
							allTextSB.append("\n\t\t" + order.getOrderTime().toString() + " - "
									+ order.getMeal().getName() + " - " + order.getDeliveryPoint().getName());
						}

						if (packagerType == Packager.Knapsack) {
							packedOrders = kp.pack(orderBacklog);
						} else {
							packedOrders = fp.pack(orderBacklog);
						}

						// list new orders grabbed
						allTextSB.append("\n\tPacked Orders:");
						if (packedOrders != null) {
							for (int i = 0; i < packedOrders.size(); i++) {
								Order order = packedOrders.get(i);
								allTextSB.append("\n\t\t" + order.getOrderTime().toString() + " - "
										+ order.getMeal().getName() + " - " + order.getDeliveryPoint().getName());
							}
						}

						// list skipped orders
						allTextSB.append("\n\tSkipped Orders:");
						if (packedOrders != null) {
							for (int i = 0; i < kp.skippedOrders.size(); i++) {
								Order order = kp.skippedOrders.get(i);
								allTextSB.append("\n\t\t" + order.getOrderTime().toString() + " - "
										+ order.getMeal().getName() + " - " + order.getDeliveryPoint().getName());
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
						int secondsTaken = (int) tripResult.getA();
						String pathTaken = (String) tripResult.getB();
						allTextSB.append(pathTaken);
						if (packagerType == Packager.Knapsack) {
							sumKnapsack += secondsTaken;
							if (secondsTaken < slowestTimeKnapsack) {
								slowestTimeKnapsack = secondsTaken;
							}
							if (secondsTaken > fastestTimeKnapsack) {
								fastestTimeKnapsack = secondsTaken;
							}
						} else if (packagerType == Packager.FIFO) {
							sumFIFO += secondsTaken;
							if (secondsTaken < slowestTimeFIFO) {
								slowestTimeFIFO = secondsTaken;
							}
							if (secondsTaken > fastestTimeFIFO) {
								fastestTimeFIFO = secondsTaken;
							}
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

		if (sumKnapsack > 1) {
			allTextSB.append("\n\nSumKnapsack: " + sumKnapsack);
			allTextSB.append("\nFastestTimeKnapsack: " + fastestTimeKnapsack);
			allTextSB.append("\nSlowestTimeKnapsack: " + slowestTimeKnapsack);
			allTextSB.append("\nAvgTimeKnapsack: " + (sumKnapsack / numOrders));
		}

		if (sumFIFO > 1) {
			allTextSB.append("\n\nSumFIFO: " + sumFIFO);
			allTextSB.append("\nFastestTimeFIFO: " + fastestTimeFIFO);
			allTextSB.append("\nSlowestTimeFIFO: " + slowestTimeFIFO);
			allTextSB.append("\nAvgTimeFIFO: " + (sumFIFO / numOrders));
		}

		long stopTime = new Date().getTime();
		double runTime = (stopTime - startTime) / 1000.0;
		allTextSB.append("\n\nSimulation Took: ");
		allTextSB.append(runTime + " seconds");
		//sBuilder.append("\n<terminated>");

		simRunning = false;
		
		Tuple results = new Tuple(displayTextSB, allTextSB);
		return results;
	}
	
	public boolean isRunning() {
		return simRunning;
	}

	public CurrentSetup getCurrentSetup() {
		return currentSetup;
	}

}
