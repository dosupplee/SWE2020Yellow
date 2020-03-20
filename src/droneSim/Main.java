/**
 * 
 */
package droneSim;

import java.util.ArrayList;

public class Main {
	private static CurrentSetup currentSetup;

	private static double avgTimeFIFO;
	private static double slowestTimeFIFO;
	private static double fastestTimeFIFO;
	private static double sumFIFO;

	private static double avgTimeKnapsack;
	private static double slowestTimeKnapsack;
	private static double fastestTimeKnapsack;
	private static double sumKnapsack;


	public static void main(String[] args) {
		// current setup
		currentSetup = new CurrentSetup();

		XMLOrderGenerator gen = new XMLOrderGenerator(currentSetup);

		gen.generateAllOrders();

	}

	public static void createAllOrders() {
		// TODO generate the orders for the simulation
	}

	public static void runSimulation() {
		// TODO run the simulation
	}

	public static void runTSP() {

	}

	public static void packFIFO() {
		// TODO pack drones for FIFO simulation
	}

	public static void packKnapsack() {
		// TODO pack drones for knapsack simulation
	}

}
