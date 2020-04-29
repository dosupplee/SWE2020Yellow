package droneSim;

import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

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
	public DeliveryPoint getPoint(int x, int y) {return new DeliveryPoint(0, 0,"NULL");}// override = in point class??
	public ArrayList<DeliveryPoint> getPoints() {return points;}
	public void newPoint(DeliveryPoint point) {} // or could take a x, y??
	public void deletePoint(DeliveryPoint point) {}
	
	
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
	
	/*
	 * 
	 * 
	 */
	public double getLongestFlighDistance()
	{
		double longest = 0;
		for(DeliveryPoint dp : points)
		{
			for(DeliveryPoint dp2: points)
			{
				if(dp!=dp2)
				{
					//a^2 + b^2 = c^2
					double length = Math.sqrt(Math.pow((Math.abs(dp.getX() - dp2.getX())),2) + Math.pow((Math.abs(dp.getY() - dp2.getY())),2));
					
					if(length>longest)
					{
						longest = length;
					}
				}
			}
		}
		
		return longest;
	}
	
	
	

}
