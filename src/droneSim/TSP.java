package droneSim;
import java.util.*;

public class TSP {
	private int numPoints;
	private int bestLengthSoFar = Integer.MAX_VALUE;
	private ArrayList<DeliveryPoint> currentPath;
	private ArrayList<DeliveryPoint> bestPath;
	private ArrayList<DeliveryPoint> citiesToSearch;
	
	
	public TSP(ArrayList<DeliveryPoint> points) {
		this.numPoints = points.size();
		this.citiesToSearch = points;
		this.currentPath = new ArrayList<DeliveryPoint>();
		this.bestPath = new ArrayList<DeliveryPoint>();
	}
	
	
	public ArrayList<DeliveryPoint> runTSP() {
		recursiveFindPath(currentPath, citiesToSearch);
		return bestPath;
	}
	
	
	private void recursiveFindPath(ArrayList<DeliveryPoint> path, ArrayList<DeliveryPoint> remaining) {
		
		if (!remaining.isEmpty()) {
			for (int i = 0; i < remaining.size(); i++) {
				DeliveryPoint current = new DeliveryPoint(remaining.remove(0));
				ArrayList<DeliveryPoint> newPath = (ArrayList<DeliveryPoint>) path.clone();
				newPath.add(current);
				
				recursiveFindPath(newPath, remaining);
				remaining.add(current);
				//System.out.println(i);
			}
		} else {
			//System.out.print("Size of next path: " + path.size());
			if (copyToBest(path)) {
				System.out.print("Found Best path:\n Home -> ");
				for (int i = 0; i < numPoints; i++) {
					System.out.print(bestPath.get(i).getName() + " -> ");
				}
				System.out.print("Home \n\n");
			}
		}

	}
	
	
	private int findDistance(DeliveryPoint a, DeliveryPoint b) {
		return (int) Math.sqrt( ( (a.getY() - b.getY()) * (a.getY() - b.getY()) ) 
				+ ( (a.getX() - b.getX()) * (a.getX() - b.getX()) ) );
	}
	
	
	private boolean copyToBest(ArrayList<DeliveryPoint> toCheck) {
		//calculate length of path passed
		int length = 0;
		length += findDistance(new DeliveryPoint(0,0,"Home"), toCheck.get(0));
		for (int i = 0; i < toCheck.size() - 1; i++) {
			System.out.println(toCheck.get(i).getName());
			length += findDistance(toCheck.get(i), toCheck.get(i + 1));
		}
		System.out.println();
		length += findDistance(new DeliveryPoint(0,0,"Home"), toCheck.get(toCheck.size() - 1));
		
		if (length < bestLengthSoFar) {
			//System.out.println("\nSize of toCheck: " + toCheck.size());
			bestLengthSoFar = length;
			System.out.println("Length is: " + length);
			for (int i = 0; i < toCheck.size(); i++) {
				bestPath.add(toCheck.get(i));
			}
			return true;
		}
		
		return false;
	}
	
			
}
