package droneSim;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

import com.lynden.gmapsfx.javascript.object.LatLong;

public class Map {
	private String mapName;		// name of map
	private String fileAddress; // file address of current map
	private ArrayList<DeliveryPoint> points; // delivery locations
	private ArrayList<Tuple> pointLatLongDoubles;
	private LatLong homeBase;
	
	
	public Map(String name, String fileAddress) {
		super();
		this.mapName = mapName;
		this.fileAddress = fileAddress;
		this.points = new ArrayList<DeliveryPoint>();
		this.pointLatLongDoubles = new ArrayList<Tuple>();
	}
		
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
		this.pointLatLongDoubles = new ArrayList<Tuple>();
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
	
	
	public ArrayList<DeliveryPoint> getPoints() {
		return points;
	}
	
	
	public Dictionary<String, LatLong> getPointsInAngularMeasurement() {
		Dictionary<String, LatLong> convertedPoints = new Hashtable<String, LatLong>();
		convertedPoints.put(points.get(0).getName(), homeBase);
		
		for (int index = 1; index < points.size(); index++) {
			double calculatedLatitude = homeBase.getLatitude() + convertFeetToAngular(points.get(index).getX());
			double calculatedLongitude = homeBase.getLongitude() + convertFeetToAngular(points.get(index).getY());
			convertedPoints.put(points.get(index).getName(), new LatLong(calculatedLatitude, calculatedLongitude));
		}
		return convertedPoints;		
	}
	
	
	public int getNumPoints() {
		return points.size();
	}
	
	
	public void addPoint(DeliveryPoint point) {
		points.add(point);
	}
	
	public void addPoint(String name, LatLong location) {
		if (pointLatLongDoubles.get(0) == null) {
			pointLatLongDoubles.add(new Tuple(location.getLatitude(), location.getLongitude()));
			points.add(new DeliveryPoint(0,0, name));
		} else {
			int newX = convertAngularToFeet(location.getLatitude()) -
					convertAngularToFeet(pointLatLongDoubles.get(0).getLatitude());
			int newY = convertAngularToFeet(location.getLongitude()) -
					convertAngularToFeet(pointLatLongDoubles.get(0).getLongitude());
			
			points.add(new DeliveryPoint(newX, newY, name));
			pointLatLongDoubles.add(new Tuple(location.getLatitude(), location.getLongitude()));
		}
	}
	
	
	public void deletePoint(DeliveryPoint point) {
		points.remove(points.indexOf(point));
		pointLatLongDoubles.remove(points.indexOf(point));
	}
	
	
	/*
	 * Returns a random point from the currentMap
	 * Takes in nothing
	 * Returns the randomly chosen point
	 */
	public DeliveryPoint getRandomPoint()
	{
		Random rand = new Random();
		
		int randPoint = rand.nextInt(points.size());
		
		return points.get(randPoint);
	}
	
	/*
	 * Returns the delivery point with the specified name
	 * Takes in a delivery point name to find
	 * Returns the found delivery point
	 */
	public DeliveryPoint getDeliveryPointFromName(String name)
	{
		for(int i=0;i<points.size();i++)
		{
			if(points.get(i).getName().equals(name))
			{
				return points.get(i);
			}
		}
		
		return null;
	}
	
	private int convertAngularToFeet(double latitudeOrLongitude) {
		return (int) (latitudeOrLongitude * (10000/90) * (3280.4));
	}
	
	private double convertFeetToAngular(int feet) {
		return ((double) feet) / (3280.4 * (10000/90));
	}
	

}
