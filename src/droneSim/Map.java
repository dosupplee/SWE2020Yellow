package droneSim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Map {
	private String mapName;		// name of map
	private String fileAddress; // file address of current map
	private ArrayList<DeliveryPoint> points; // delivery locations
	private ArrayList<Tuple> pointLatLongDoubles;
	
	
	public Map(String name, String fileAddress) {
		super();
		this.mapName = name + ".csv";
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
	public Map(String name, String fileAddress, ArrayList<DeliveryPoint> points) {
		super();
		this.mapName = name + ".csv";
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
	
	/**
	 * Saves the map points to file address and clears all points
	 * Overloaded version saves new file
	 */
	public void saveMap(File fileArg) {
		File file = fileArg; // open/create file
		try {
			PrintWriter fileWriter = new PrintWriter(file); // create output stream
			fileWriter.append(points.get(0).getName() + "," + pointLatLongDoubles.get(0).getLatitude() + 
					"," + pointLatLongDoubles.get(0).getLongitude() + "\n");
			
			for (int index = 1; index < points.size(); index++) {
				fileWriter.append(points.get(index).getName() + "," + points.get(index).getX() + 
						"," + points.get(index).getY() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
			
		}
		catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	
	/**
	 * Saves the map points to file address and clears all points
	 * Overloaded version takes string and saves to previous location
	 */
	public void saveMap(String saveName) {
		File csvFile = new File(fileAddress + "\\" + saveName); // open/create file
		try {
			PrintWriter fileWriter = new PrintWriter(csvFile); // create output stream
			fileWriter.append(points.get(0).getName() + "," + pointLatLongDoubles.get(0).getLatitude() + 
					"," + pointLatLongDoubles.get(0).getLongitude() + "\n");
			
			for (int index = 1; index < points.size(); index++) {
				fileWriter.append(points.get(index).getName() + "," + points.get(index).getX() + 
						"," + points.get(index).getY() + "\n");
			}
			fileWriter.flush();
			fileWriter.close();
			
		}
		catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	
	
	
	/**
	 * Loads map 
	 * @param mapName
	 * @param fileAddress
	 */
	public void loadMap(String mapName, String fileAddress) {
		// Save current map and clear current contents
		saveMap(this.mapName);
		this.points = new ArrayList<DeliveryPoint>();
		this.pointLatLongDoubles = new ArrayList<Tuple>();
		
		// Update map with new data
		this.mapName = mapName;
		File csvMap = new File(fileAddress); // open file
		
		 // if the file does not exist
		if (!csvMap.exists()) {
			System.err.println("File not found");
		} else {
			//If file does exit try
			try {
				//create scanner to read file
				Scanner fileReader = new Scanner(csvMap);
				
				//while scanner sees next line loop
				Boolean isFirstLine = true;	//true for first loop 
				while (fileReader.hasNextLine()) {
					String[] data = fileReader.nextLine().split(",", 3);
					String name = data[0].trim();
					
					if (isFirstLine == true) {
						double lat= Double.parseDouble(data[1].trim());
					    double lon = Double.parseDouble(data[2].trim());
					    
					    addPoint(name, lat, lon);
					    isFirstLine = false;
					}
					else {
						//comma separated file is read
						int x = Integer.parseInt(data[1].trim());
						int y = Integer.parseInt(data[2].trim());
				    
	
						//New delivery point created and added to map
						DeliveryPoint point= new DeliveryPoint(x,y,name);	
						addPoint(point);
					}
				}
				//close scanner 
				fileReader.close();
				
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Return all delivery points in the form of DeliveryPoint objects
	 * @return ArrayList<DeliveryPoint>
	 */
	public ArrayList<DeliveryPoint> getPoints() {
		return points;
	}
	
	
	/**
	 * Return all delivery points in the form of tuples of latitude and longitude
	 * @return ArrayList<Tuple>
	 */
	public ArrayList<Tuple> getLatLongPoints() {
		return pointLatLongDoubles;
	}
	
	/**
	 * Return the name of a delivery point given its index
	 * @param index of delivery point in all ArrayLists (synchronized between all classes)
	 * @return String name of delivery point indexed
	 */
	public String getPointName(int index) {
		return points.get(index).getName();
	}
	
	/**
	 * Return number of delivery points in any ArrayList (all lists are synchronized)
	 * @return int size
	 */
	public int getNumPoints() {
		return points.size();
	}
	
	/**
	 * Return tuple of two doubles containing the latitude and longitude of any delivery point
	 * @param index of point in any ArrayLists (all lists are synchronized)
	 * @return Tuple of two doubles
	 */
	public Tuple getLatLongPoint(int index) {
		return pointLatLongDoubles.get(index);
	}
	
	
	/**
	 * Add point given a DeliveryPoint with relative values. First point added must be a tuple value of
	 * 	latitude and longitude to provide a reference point for the rest of the map points, so must use 
	 *  addPoint(String name, double latitude, double longitude) for initial point.
	 * @param point in the form of a DeliveryPoint object with relative location values
	 */
	public void addPoint(DeliveryPoint point) {
		if (pointLatLongDoubles.size() == 0) {
			System.err.println("ERROR: First point added must be a tuple of longitude and latitude values.");
		} else {
			points.add(point);
			
			// Convert relative values to absolute latitude and longitude and save for GoogleMapsAPI use
			double latitude = pointLatLongDoubles.get(0).getLatitude() +
					convertFeetToAngular(point.getX());
			double longitude = pointLatLongDoubles.get(0).getLongitude() +
					convertFeetToAngular(point.getY());
			
			Tuple newPointDoubles = new Tuple(latitude, longitude);
			pointLatLongDoubles.add(newPointDoubles);
		}
	}
	/**
	 * Add point given absolute location values (easily converted to relative values for TSP processing). First
	 * 	point added to a new map must use this method declaration.
	 * @param name of point
	 * @param latitude double value of absolute location
	 * @param longitude double value of absolute location
	 */
	public void addPoint(String name, double latitude, double longitude) {
		if (pointLatLongDoubles.size() == 0) {
			pointLatLongDoubles.add(new Tuple(latitude, longitude));
			points.add(new DeliveryPoint(0,0, name));
		} else {
			pointLatLongDoubles.add(new Tuple(latitude, longitude));
			
			// Convert absolute location to relative values for processing by TSP algorithm
			int newX = convertAngularToFeet(latitude) -
					convertAngularToFeet(pointLatLongDoubles.get(0).getLatitude());
			
			int newY = convertAngularToFeet(longitude) -
					convertAngularToFeet(pointLatLongDoubles.get(0).getLongitude());
			
			points.add(new DeliveryPoint(newX, newY, name));
		}
	}
	
	/**
	 * Delete point given the index (index is synchronized between all lists and classes holding delivery points).
	 * @param index of point to delete
	 */
	public void deletePoint(int index) {
		points.remove(index);
		pointLatLongDoubles.remove(index);
	}
	
	
	/**
	 * Returns a random point from the current map
	 * @return DeliveryPoint of the randomly chosen point
	 */
	public DeliveryPoint getRandomPoint()
	{
		Random rand = new Random();
		
		int randPoint = rand.nextInt(points.size());
		
		return points.get(randPoint);
	}
	
	/**
	 * Returns the delivery point with the specified name
	 * @param String name of the DeliveryPoint to find
	 * @return DeliveryPoint with the provided name
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
	
	/**
	 * Find the longest distance the drone can travel for statistical analysis
	 * @return double value of the longest distance
	 */
	public double getLongestFlightDistance() {
		double longestDistance = 0;
		for (DeliveryPoint firstPoint : points) {
			for (DeliveryPoint secondPoint : points) {
				if (firstPoint != secondPoint) {
					//a^2 + b^2 = c^2
					double length = Math.sqrt(Math.pow((Math.abs(firstPoint.getX() - secondPoint.getX())),2) + 
							Math.pow((Math.abs(firstPoint.getY() - secondPoint.getY())),2));

					if (length > longestDistance) {
						longestDistance = length;
					}
				}
			}
		}

		return longestDistance;
	}
	
	
	private int convertAngularToFeet(double latitudeOrLongitude) {
		return (int) (latitudeOrLongitude * (10000/90) * (3280.4));
	}
	
	private double convertFeetToAngular(int feet) {
		return ((double) feet) / (3280.4 * (10000/90));
	}

}
