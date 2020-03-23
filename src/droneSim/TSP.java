import java.util.*;

public class TSP {
	private int numPoints;
	private DeliveryPoint baseMatrix[];

	private int bestLengthSoFar = Integer.MAX_VALUE;
	
	public TSP(int n, DeliveryPoint delivPoints[]) {
		this.numPoints = n;
		this.baseMatrix[] = new DeliveryPoint[n + 2];
		this.baseMatrix[0] = new DeliveryPoint(0, 0, "Home");
		
		int counter = 0;
		while (delivPoints[counter] != NULL) {
			this.baseMatrix[counter + 1] = delivPoints[counter];
		}
		this.baseMatrix[n + 2] = new DeliveryPoint(0, 0, "Home");
	}
	
	
	public DeliveryPoint[] runAlgorithm(DeliveryPoint c, ArrayList<DeliveryPoint> r) {
		int lengthSoFar = 0;
		DeliveryPoint curr = c;
		ArrayList<DeliveryPoint> remaining = r.clone();
		remaining.remove(curr);
		
		if (remaining.isEmpty()) {
			return findDistance(curr, new DeliveryPoint(0,0,"Home"));
		} else {
			for (int i = 0; i < remaining.size(); i++) {
				runAlgorithm(remaining.get(i), remaining);
				return findDistance(curr, remaining.get(i));
			}
		}
	}
	
	
	private int findDistance(DeliveryPoint a, DeliveryPoint b) {
		return Math.Sqrt( ( (a.getY() - b.getY()) * (a.getY() - b.getY()) ) 
				+ ( (a.getX() - b.getX()) * (a.getX() - b.getX()) ) );
	}
	
			
}
