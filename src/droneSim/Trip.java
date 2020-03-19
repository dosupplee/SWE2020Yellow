package droneSim;

import java.util.ArrayList;

public class Trip {
	private ArrayList<Order> orders;
	private String algorithm;
	private double tripTime;
	
	/**
	 * Creates a Trip instance
	 * @param orders
	 * @param algorithm
	 * @param tripTime
	 */
	public Trip(ArrayList<Order> orders, String algorithm, double tripTime) {
		this.orders = orders;
		this.algorithm = algorithm;
		this.tripTime = tripTime;
	}
	
	public String getAlgorithm() { return algorithm; }
	public double getTripTime() { return tripTime; }
	public double getLongestWait() { return 0.0; } // return longest (delivery time - order time)
}
