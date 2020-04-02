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
	
		// get the drone carrying capacity per trip
		int droneCarryWeight = Main.getCurrentSetup().getDroneWeight();
		
		// trip weight to see if below drone carry capacity
		double tripWeight = 0.0;
				
		// arrayList containing the packed orders with their respective weight
		ArrayList<Order> packed = new ArrayList<Order>();

		while (orderBacklog.size() > 0 && tripWeight + orderBacklog.get(0).getOrderWeight() <= droneCarryWeight) {
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
