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
		
		for(int shift = 0;shift<currentSetup.getNumShifts();shift++)
		{
			boolean canStopLastHour = false;
			for(int hour=0;hour<4;hour++)
			{
				int min = 0;
				while((min<60 && hour<=2) ||((min<60 ||canStopLastHour==false)&&hour==3))
				{
					System.out.println("\nShift ("+Integer.toString(shift)+") - Printing Out New Orders Received at: "+Integer.toString(hour)+":"+Integer.toString(min)+":00");
					String time = Integer.toString(hour)+":"+Integer.toString(min)+":00";
					ArrayList<Order> newOrders = parse.getNewOrders(shift,time);
					
					for(int i=0;i<newOrders.size();i++)
					{
						Order order = newOrders.get(i);
						System.out.println("\t"+Double.toString(order.getOrderTime())+" - "
						+ order.getMeal().getName() + " - " + order.getDeliveryPoint().getName());
					}
					
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
