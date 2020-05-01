package droneSim;

import java.util.ArrayList;
import java.util.Random;

public class Map {
	private String mapName;		// name of map
	private String fileAddress; // file address of current map
	private ArrayList<DeliveryPoint> points; // delivery locations
	private ArrayList<Tuple> pointLatLongDoubles;
	
	
	public Map(String name, String fileAddress) {
		super();
		this.mapName = name;
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
		this.mapName = name;
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
	
	
	/**
	 * Loads map 
	 * @param mapName
	 * @param fileAddress
	 */
	public void loadMap(String mapName, String fileAddress) {
		// save current map
		saveMap();
		this.mapName = mapName;
		this.fileAddress = fileAddress;
		File csvMap = new File(fileAddress); // open file
		
		 // if the file does not exist
		if (!csvMap.exists()) {
			System.err.println("File not found");
			return;
		}
		//If file does exit try
		try {
			//create scanner to read file
			Scanner fileReader = new Scanner(csvMap);
			
			//while scanner sees next line loop
			while (fileReader.hasNextLine()) {
				
				//comma separated file is read
				String []data = fileReader.nextLine().split(",");;
				String name = data[0].trim();
				int x = Integer.parseInt(data[1].trim());
			    int y = Integer.parseInt(data[2].trim());
			    

				//New delivery point created and added to map
			    DeliveryPoint point= new DeliveryPoint(x,y,name);	
			    addPoint(point);
			    
				}
			//close scanner 
			fileReader.close();
			
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	public ArrayList<DeliveryPoint> getPoints() {
		return points;
	}
	
	
	public ArrayList<DeliveryPoint> getPoints() {
		return points;
	}
	
	
	public ArrayList<Tuple> getLatLongPointDoubles() {
		return pointLatLongDoubles;
	}
	
	
	public String getPointName(int index) {
		return points.get(index).getName();
	}
	
	
	public int getNumPoints() {
		return points.size();
	}
	
	
	public Tuple getPointDoubles(int index) {
		return pointLatLongDoubles.get(index);
	}
	
	
	public void addPoint(DeliveryPoint point) {
		if (pointLatLongDoubles.size() == 0) {
			//return error
		} else {
			points.add(point);
			double latitude = pointLatLongDoubles.get(0).getLatitude() +
					convertFeetToAngular(point.getX() * 50);
			double longitude = pointLatLongDoubles.get(0).getLongitude() +
					convertFeetToAngular(point.getY() * 50);
			
			Tuple newPointDoubles = new Tuple(latitude, longitude);
			pointLatLongDoubles.add(newPointDoubles);
		}
	}
	
	public void addPoint(String name, double latitude, double longitude) {
		if (pointLatLongDoubles.size() == 0) {
			pointLatLongDoubles.add(new Tuple(latitude, longitude));
			points.add(new DeliveryPoint(0,0, name));
		} else {
			int newX = convertAngularToFeet(latitude) -
					convertAngularToFeet(pointLatLongDoubles.get(0).getLatitude());
			
			int newY = convertAngularToFeet(longitude) -
					convertAngularToFeet(pointLatLongDoubles.get(0).getLongitude());
			
			points.add(new DeliveryPoint(newX, newY, name));
			pointLatLongDoubles.add(new Tuple(latitude, longitude));
		}
	}
	
	
	public void deletePoint(int index) {
		points.remove(index);
		pointLatLongDoubles.remove(index);
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
	
	
	public double getLongestFlightDistance() {
		double longest = 0;
		for (DeliveryPoint dp : points) {
			for (DeliveryPoint dp2 : points) {
				if (dp != dp2) {
					//a^2 + b^2 = c^2
					double length = Math.sqrt(Math.pow((Math.abs(dp.getX() - dp2.getX())),2) + Math.pow((Math.abs(dp.getY() - dp2.getY())),2));

					if (length > longest) {
						longest = length;
					}
				}
			}
		}

		return longest;
	}
	
	
	private int convertAngularToFeet(double latitudeOrLongitude) {
		return (int) (latitudeOrLongitude * (10000/90) * (3280.4));
	}
	
	private double convertFeetToAngular(int feet) {
		return ((double) feet) / (3280.4 * (10000/90));
	}

}
