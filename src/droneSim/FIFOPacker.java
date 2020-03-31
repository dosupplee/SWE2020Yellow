package droneSim;

import java.util.ArrayList;

public class FIFOPacker {

	public FIFOPacker() {
		
	}
	
	/**
	 * Uses First-In-First-Out queuing system load orders from backlog into ArrayList<Order> package
	 * @param orderBacklog 
	 * @return smaller package of orders that will fit in drone
	 */
	public ArrayList<Order> pack(ArrayList<Order> orderBacklog) {
		// get the number of orders that need to be packed
		boolean keepPacking = true;
		
		// get the drone carrying capacity per trip
		int droneCarryWeight = Main.getCurrentSetup().getDroneWeight();
		
		// trip weight to see if below drone carry capacity
		double testTripWeight = 0.0;
				
		// arrayList containing the packed drones with their respective weight
		ArrayList<Order> packed = new ArrayList<Order>();
		
		// just used to get the first order in the backlog
		int currentOrder = 0;
		
		while (keepPacking) {
			
			// get the first order to pack
			Order orderToPack = orderBacklog.get(currentOrder);
			
			// add that order's weight to the test weight
			testTripWeight += orderToPack.getOrderWeight();
			// if the test weight is still below or equal to the drone carry compacity
			if (testTripWeight <= droneCarryWeight) {
				
				// add the item to the packed list
				packed.add(orderToPack);
	
				// remove the item from the backlog
				orderBacklog.remove(currentOrder);
			}
			// if the trip weight exceeds the drone carrying capacity
			else {
				// exit the while loop
				keepPacking = false;
			}
		}
		// return the arrayList of the packed items
		return packed;
	}
}
