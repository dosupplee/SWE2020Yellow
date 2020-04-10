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
			/**
			 * Empty Constructor 
			 */
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
			
			/**
			 * Saves the map to file address
			 */
			public void saveMap() {
				File csvFile = new File(fileAddress); // open/create file
				try {
					PrintWriter fileWriter = new PrintWriter(csvFile); // create output stream
					for(DeliveryPoint point: this.points) {
						fileWriter.append(point.getName()+","+point.getX()+","+point.getY());
					}
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
					    int x = Integer.parseInt(data[1].trim());
					    int y = Integer.parseInt(data[2].trim());
					    String name = data[0].trim();

						//New delivery point created and added to map
					    DeliveryPoint point= new DeliveryPoint(x,y,name);	
					    newPoint(point);
						}
					//close scanner 
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
			
			/**
			 * Removes delivery point
			 * @param point: Delivery point to be removed from map
			 */
			public void deletePoint(DeliveryPoint point) {
				for(int i =0; i < this.points.size();i++) {
					if (point == this.points.get(i)) {
						this.points.remove(i);
					}
				}
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
	

	
}
