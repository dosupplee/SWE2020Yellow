/**
 * 
 */
package droneSim;

import java.util.ArrayList;
import java.util.LinkedList; 
import java.util.Queue; 

public class Main {
	private static CurrentSetup currentSetup;

	private static double avgTimeFIFO;
	private static double slowestTimeFIFO;
	private static double fastestTimeFIFO;
	private static double sumFIFO;

	private static double avgTimeKnapsack;
	private static double slowestTimeKnapsack;
	private static double fastestTimeKnapsack;
	private static double sumKnapsack;


	public static void main(String[] args) {
		// current setup
		currentSetup = new CurrentSetup();

		//generate all orders to XML
		XMLOrderGenerator gen = new XMLOrderGenerator(currentSetup);
		gen.generateAllOrders();
		
		XMLOrderParser parse = new XMLOrderParser("order.xml");
		
		System.out.println("Orders Generated");
		
		//This Section Mimics how we will be occassionally refreshing our order backlog list at certain times
		
		for(int shift = 0;shift<currentSetup.getNumShifts();shift++) //for each shift
		{
			boolean canStopLastHour = false; //if we can stop our final hour overRun
			for(int hour=0;hour<4;hour++) //for each hour
			{
				int min = 0;
				//while either is one of the first three hours or is the final hour and have finished all orders
				while((min<60 && hour<=2) ||((min<60 ||canStopLastHour==false)&&hour==3)) 
				{
					System.out.println("\nShift ("+Integer.toString(shift)+") - Printing Out New Orders Received at: "+Integer.toString(hour)+":"+Integer.toString(min)+":00");
					String time = Integer.toString(hour)+":"+Integer.toString(min)+":00";
					
					//grab new orders from parser
					//Normally we will be appending to existing backlog
					ArrayList<Order> newOrders = parse.getNewOrders(shift,time);
					
					//list new orders grabbed
					for(int i=0;i<newOrders.size();i++)
					{
						Order order = newOrders.get(i);
						System.out.println("\t"+order.getOrderTime().toString()+" - "
						+ order.getMeal().getName() + " - " + order.getDeliveryPoint().getName());
					}
					
					//if have gotten the last few orders from that shift
					if(hour==3 && min>=60 && newOrders.size()==0)
						canStopLastHour = true;
					
					min +=7;
				}
			}
		}
		
		System.out.println("<terminated>");

	}

	public static void createAllOrders() {
		// TODO generate the orders for the simulation
	}

	public static void runSimulation() {
		// TODO run the simulation
	}

	public static void runTSP() {
		ArrayList<DeliveryPoint> pointsToVisit = new ArrayList<DeliveryPoint>();
		ArrayList<DeliveryPoint> bestPath;
		
		DeliveryPoint hal = new DeliveryPoint(0, 5, "HAL");
		pointsToVisit.add(hal);
		DeliveryPoint stem = new DeliveryPoint(-2, -8, "STEM");
		pointsToVisit.add(stem);
		DeliveryPoint lincoln = new DeliveryPoint(9, -15, "Lincoln");
		pointsToVisit.add(lincoln);
		DeliveryPoint library = new DeliveryPoint(9, -6, "Library");
		pointsToVisit.add(library);
		
		TSP driver = new TSP(pointsToVisit);
		bestPath = driver.runTSP();
	}

	public static void packFIFO() {
		// TODO pack drones for FIFO simulation
	}

	public static void packKnapsack() {
		// TODO pack drones for knapsack simulation
	}
	
	public static CurrentSetup getCurrentSetup()
	{
		return currentSetup;
	}

}
