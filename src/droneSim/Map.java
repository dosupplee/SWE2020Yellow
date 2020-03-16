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
	public void loadMap(String mapName, String fileAddress) {
		// save current map
		saveMap();
		this.mapName = mapName;
		this.fileAddress = fileAddress;
		// TODO get file access and delivery points
	}
	public DeliveryPoint getPoint(int x, int y) {return new DeliveryPoint(0, 0);}// override = in point class??
	public ArrayList<DeliveryPoint> getPoints() {return points;}
	public void newPoint(DeliveryPoint point) {} // or could take a x, y??
	public void deletePoint(DeliveryPoint point) {}
	

}
