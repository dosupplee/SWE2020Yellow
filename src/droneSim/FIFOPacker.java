package droneSim;

import java.util.ArrayList;

public class FIFOPacker {
	
	private CurrentSetup currentSetup;
	private double longestDistance;
	
	public FIFOPacker(CurrentSetup currentSetup) {
		this.currentSetup = currentSetup;
		//longest distance between any two points
		longestDistance = currentSetup.getCurrentMap().getLongestFlightDistance();
	}
	
	/**
	 * Uses First-In-First-Out queuing system load orders from backlog into ArrayList<Order> package
	 * @param orderBacklog 
	 * @return smaller package of orders that will fit in drone
	 */
	public ArrayList<Order> pack(ArrayList<Order> orderBacklog) {
	
		// get the drone carrying capacity per trip
		int droneCarryWeight = currentSetup.getDroneWeight();
		
		// trip weight to see if below drone carry capacity
		double tripWeight = 0.0;
				
		// arrayList containing the packed orders with their respective weight
		ArrayList<Order> packed = new ArrayList<Order>();
		
		//the time to fly the max distance and then drop off food
		double timePerDestination = (longestDistance * 10) / currentSetup.getDrone().getSpeedMPS() + currentSetup.getDrone().getDropOffTime();
		
		int numMaxDestinations = (int) ((currentSetup.getDrone().getMaxFlightTime() * 0.95) / timePerDestination);
		
		while (orderBacklog.size() > 0 && tripWeight + orderBacklog.get(0).getOrderWeight() <= droneCarryWeight && packed.size() < numMaxDestinations) {
			// get the first order to pack
			Order orderToPack = orderBacklog.remove(0);
			
			// add that order's weight to the test weight
			tripWeight += orderToPack.getOrderWeight();
				
			// add the item to the packed list
			packed.add(orderToPack);
		}
		
		// return the arrayList of the packed items
		return packed;
	}
}
