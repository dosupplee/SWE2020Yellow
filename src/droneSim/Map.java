package droneSim;

import java.util.ArrayList;

public class Map {
	private String mapName;		// name of map
	private String fileAddress; // file address of current map
	private ArrayList<DeliveryPoint> points; // delivery locations
	
	/**
	 * Creates a map
	 * @param mapName
	 * @param fileAddress
	 * @param points
	 */
	public Map(String mapName, String fileAddress, ArrayList<DeliveryPoint> points) {
		super();
		this.mapName = mapName;
		this.fileAddress = fileAddress;
		this.points = points;
	}
	
	/**
	 * Returns map name
	 * @return
	 */
	public String getMapName() {
		return mapName;
	}
	
	//TODO
	public void saveMap() {}
	public void loadMap() {}
	public DeliveryPoint getPoint() {return new DeliveryPoint(0, 0);}
	public void newPoint(DeliveryPoint point) {} // or could take a x, y??
	public void deletePoint(DeliveryPoint point) {}
	

}
