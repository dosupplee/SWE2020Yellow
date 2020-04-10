package droneSim;

import droneSim.DeliveryPoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


import java.util.Random;

public class Map {

	private String mapName;		// name of map
	private String fileAddress; // file address of current map
	private ArrayList<DeliveryPoint> points=new ArrayList<DeliveryPoint>(); // delivery locations
				
			
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
			
			public Map() {
				this.mapName = "" ;		// name of map
				this.fileAddress =""; // file address of current map
			}

			/**
			 * Returns map name
			 * @return
			 */
			public String getMapName() {
				return mapName;
			}
			
			//TODO
			public void saveMap() {
				
			}
			public void loadMap(String mapName, String fileAddress) {
				// save current map
				saveMap();
				this.mapName = mapName;
				this.fileAddress = fileAddress;
				// TODO get file access and delivery points
				File csvMap = new File(fileAddress); // open file
				if (!csvMap.exists()) { // if the file does not exist
					System.err.println("File not found");
					return;
				}
								
				try {
					
					Scanner fileReader = new Scanner(csvMap);
					
					while (fileReader.hasNextLine()) {
						
						String []data = fileReader.nextLine().split(",");;
					    int x = Integer.parseInt(data[1].trim());
					    int y = Integer.parseInt(data[2].trim());
					    String name = data[0].trim();

						
					    DeliveryPoint point= new DeliveryPoint(x,y,name);	
					 
					    newPoint(point);
						}
					fileReader.close();
					
				}
				catch (Exception e) {
					System.err.println(e.getMessage());
				}
			}
			
			
			public DeliveryPoint getPoint(int x, int y) {
				return new DeliveryPoint(0, 0,"NULL");
			}// override = in point class??
			
			public ArrayList<DeliveryPoint> getPoints() {
				return points;
				
			}
			public void newPoint(DeliveryPoint point) {
				this.points.add(point);
			} 
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
	

	
}
