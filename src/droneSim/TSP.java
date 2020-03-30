/*
 * TSP code modified from original by MvG at link: 
 *   https://stackoverflow.com/questions/11703827/brute-force-algorithm-for-the-traveling-salesman-problem-in-java
 * 
 */


package droneSim;
import java.util.*;

public class TSP {
	private int bestLengthSoFar;	//Tracks the distance of the quickest route so far
	private ArrayList<DeliveryPoint> bestPath;	//Holds the quickest route so far
	private ArrayList<DeliveryPoint> orderLocations;	//Holds a list of the points in the route to visit
	
	/**
	 * Constructor for the TSP object that instantiates needed variables
	 * 
	 * @param orderLocations: delivery points to visit
	 */
	public TSP(ArrayList<DeliveryPoint> orderLocations) {
		bestLengthSoFar = Integer.MAX_VALUE;
		this.orderLocations = orderLocations;
	}
	
	
	/**
	 * Main function to run the algorithm and print the output
	 * 
	 * @return ArrayList<DeliveryPoint> containing the fastest path to take
	 */
	public ArrayList<DeliveryPoint> runTSP() {
		//Ensure that the print and recursion only run if there are valid points to visit
		if (orderLocations.size() > 0) {
			//Start the recursive algorithm using an empty ArrayList and the points to visit
			recursiveFindPath(new ArrayList<DeliveryPoint>(), orderLocations);
			
			//Print the best path to take as text
			System.out.print("\n\nFound Best path:\n Home -> ");
			
			for (int i = 0; i < bestPath.size(); i++) {
				System.out.print(bestPath.get(i).getName() + " -> ");
			}
			
			System.out.print("Home \n\n");
		}
		
		return bestPath;
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
	
	//TODO comment
	private int findP2PDistance(DeliveryPoint a, DeliveryPoint b) {
		return (int) Math.sqrt( Math.pow(a.getY() - b.getY(), 2) 
				+ Math.pow(a.getX() - b.getX(), 2) );
	}
	
	//TODO comment
	private int findTotalDistance(ArrayList<DeliveryPoint> path) {
		int length = 0;
		length += findP2PDistance(new DeliveryPoint(0,0,"Home"), path.get(0));
		for (int i = 0; i < path.size() - 2; i++) {
			length += findP2PDistance(path.get(i), path.get(i + 1));
		}
		length += findP2PDistance(new DeliveryPoint(0,0,"Home"), path.get(path.size() - 1));
		
		return length;
	}
			
}
