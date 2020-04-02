package droneSim;

import java.util.ArrayList;

/**
 * Note: Time is stored in seconds
 * 
 * @author LEHMANIT17
 *
 */
public class Drone {
	private String name; // the name of the drone
	private int weightCapacity; // the weight capacity of the drone (OZ)
	private int speed; // the speed of the drone (MPS)
	private int maxFlightTime; // the maximum flight time (seconds)
	private int turnAroundTime; // time between flights (seconds)
	private int dropOffTime; // time to unload the drone at a delivery point (seconds)
	
	private int bestLengthSoFar;	//Tracks the distance of the quickest route so far
	private ArrayList<DeliveryPoint> bestPath;	//Holds the quickest route so far
	private ArrayList<DeliveryPoint> orderLocations;	//Holds a list of the points in the route to visit

	/**
	 * Creates a custom Drone class
	 * 
	 * @param name
	 * @param weightCapacity
	 * @param speed (MPH, but stored in MPS)
	 * @param maxFlightTime
	 * @param turnAroundTime
	 * @param dropOffTime
	 */
	public Drone(String name, int weightCapacityLB, int speedMPH, int maxFlightTimeSec, int turnAroundTimeSec,
			int dropOffTimeSec) {
		this.name = name;
		this.weightCapacity = weightCapacity * 16; //Convert from lbs to oz
		this.speed = (int) (speed * 1.5);
		this.maxFlightTime = maxFlightTimeSec;
		this.turnAroundTime = turnAroundTimeSec;
		this.dropOffTime = dropOffTimeSec;
		this.orderLocations = new ArrayList<DeliveryPoint>();
	}

	/**
	 * Default constructor with default values
	 */
	public Drone() {
		this.name = "DefaultDrone";
		this.weightCapacity = 12 * 16; // in oz
		this.speed = (int) (20 * 1.5); // 20 mph to 30 ft/sec
		this.maxFlightTime = 20 * 60; // 20 minutes
		this.turnAroundTime = 3 * 60; // 3 minutes
		this.dropOffTime = 30; // 30 seconds
		this.orderLocations = new ArrayList<DeliveryPoint>();
	}

	
	
	/**
	 * Main function to run the algorithm and print the output
	 * 
	 * @return ArrayList<DeliveryPoint> containing the fastest path to take
	 */
	public int runTSP(ArrayList<Order> orders) {
		
		orderLocations.clear();
		bestLengthSoFar = Integer.MAX_VALUE;
		
		for (int i = 0; i < orders.size(); i++) {
			orderLocations.add(orders.get(i).getDeliveryPoint());
		}
		
		//Ensure that the print and recursion only run if there are valid points to visit
		if (orderLocations.size() > 0) {
			//Start the recursive algorithm using an empty ArrayList and the points to visit
			recursiveFindPath(new ArrayList<DeliveryPoint>(), orderLocations);
			
			//Print the best path to take as text
			System.out.print("\n\nFound Best path of distance: \nHome -> ");
			
			for (int i = 0; i < bestPath.size(); i++) {
				System.out.print(bestPath.get(i).getName() + " -> ");
			}
			
			System.out.print("Home \n\n");
		}
		
		int secondsTaken = ((bestLengthSoFar * 10) / this.speed) + (this.dropOffTime * orderLocations.size()) +
				this.turnAroundTime;
		
		return secondsTaken;
	}
	
	
	/**
	 * Recursive brute force path finder with pruning capabilities to shorten the runtime
	 * 
	 * @param travelledPath: the path travelled so far in this recursion branch
	 * @param remaining: holds the points not visited yet in this recursion branch
	 */
	private void recursiveFindPath(ArrayList<DeliveryPoint> travelledPath, ArrayList<DeliveryPoint> remaining) {
		//Only continue with recursion if the current branch has not reached the end
		if (!remaining.isEmpty()) {
			//Continue on with branches for each child node (unvisited delivery point)
			for (int i = 0; i < remaining.size(); i++) {
				//TODO comment
				DeliveryPoint current = new DeliveryPoint(remaining.remove(0));
				ArrayList<DeliveryPoint> newPath = (ArrayList<DeliveryPoint>) travelledPath.clone();
				newPath.add(current);
				
				if (findTotalDistance(newPath) < bestLengthSoFar) {
					recursiveFindPath(newPath, remaining);
				}
				remaining.add(current);
			}
		//Check if this completed branch is the quickest so far
		} else {
			//TODO comment
			int length = findTotalDistance(travelledPath);
			if (length < bestLengthSoFar) {
				bestPath = travelledPath;
				bestLengthSoFar = length;
			}
		}
	}
	
	/**
	 * Finds the distance between two DeliveryPoints
	 * @param a first delivery point
	 * @param b second delivery point
	 * @return int of calculated distance
	 */
	private int findP2PDistance(DeliveryPoint a, DeliveryPoint b) {
		//Use basic 2D distance formula and cast into integer
		return (int) Math.sqrt( Math.pow(a.getY() - b.getY(), 2) 
				+ Math.pow(a.getX() - b.getX(), 2) );
	}
	
	/**
	 * Finds the total distance of a given path based on the order of the passed ArrayList
	 * @param path ArrayList<DeliveryPoint> to find total distance
	 * @return int of sum calculated distance
	 */
	private int findTotalDistance(ArrayList<DeliveryPoint> path) {
		int length = 0; //Hold sum of distances calculated
		//Find distance between Home and first point
		length += findP2PDistance(new DeliveryPoint(0,0,"Home"), path.get(0));
		//Find distance between the first and second, second and third, etc.
		for (int i = 0; i < path.size() - 2; i++) {
			length += findP2PDistance(path.get(i), path.get(i + 1));
		}
		//Find distance between the last point and Home
		length += findP2PDistance(new DeliveryPoint(0,0,"Home"), path.get(path.size() - 1));
		
		return length;
	}
	
	
	/**
	 * get time between flights
	 * 
	 * @return the turnAroundTime
	 */
	public int getTurnAroundTime() {
		return turnAroundTime;
	}

	/**
	 * set time between flights (seconds)
	 * 
	 * @param turnAroundTime
	 */
	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	/**
	 * get time to unload the drone at a delivery point (seconds)
	 * 
	 * @return the dropOffTime
	 */
	public int getDropOffTime() {
		return dropOffTime;
	}

	/**
	 * set time to unload the drone at a delivery point (seconds)
	 * 
	 * @param dropOffTime
	 */
	public void setDropOffTime(int dropOffTime) {
		this.dropOffTime = dropOffTime;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the weightCapacity
	 */
	public int getWeightCapacity() {
		return weightCapacity;
	}

	/**
	 * @param weightCapacity
	 *            the weightCapacity to set
	 */
	public void setWeightCapacity(int weightCapacity) {
		this.weightCapacity = weightCapacity;
	}

	/**
	 * @return the speed in MPH
	 */
	public int getSpeedMPH() {
		return (speed / 3600);
	}

	/**
	 * @return the speed in MPS
	 */
	public int getSpeedMPS() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set (MPH
	 */
	public void setSpeedMPH(int speed) {
		this.speed = speed * 3600; // convert to MPS
	}

	/**
	 * @return the maxFlightTime
	 */
	public int getMaxFlightTime() {
		return maxFlightTime;
	}

	/**
	 * @param maxFlightTime
	 *            the maxFlightTime to set
	 */
	public void setMaxFlightTime(int maxFlightTime) {
		this.maxFlightTime = maxFlightTime;
	}

	/**
	 * Converts Seconds to Minutes
	 * 
	 * @param seconds
	 * @return
	 */
	public int convertStoM(int seconds) {
		return seconds / 60;
	}
	
	/**
	 * Converts Minutes to Seconds
	 * 
	 * @param minutes
	 * @return
	 */
	public int convertMtoS(int minutes) {
		return minutes * 60;
	}

	/**
	 * overide equals method
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) { // if same object
			return true;
		}
		if (!(o instanceof Drone)) { // if other is not an instance of Drone
			return false;
		}

		Drone other = (Drone) o;
		return (this.name.equals(other.name) // check if same contents
				&& this.weightCapacity == other.getWeightCapacity() 
				&& this.speed == other.getSpeedMPH()
				&& this.maxFlightTime == other.getMaxFlightTime());
	}
}
