package droneSim;

import java.util.ArrayList;

public class Shift {
	private ArrayList<Trip> trips;
	private int numOrders;
	private double shiftLength;
	
	/**
	 * Creates a Shift instance
	 * @param trips
	 * @param numOrders
	 * @param shiftLength
	 */
	public Shift(ArrayList<Trip> trips, int numOrders, double shiftLength) {
		super();
		this.trips = trips;
		this.numOrders = numOrders;
		this.shiftLength = shiftLength;
	}
	
	public int getNumOrders() { return numOrders; }
	public double getShiftLength() { return shiftLength; }
	public ArrayList<Trip> getTrips() { return trips; }
}
