package droneSim;

import javax.xml.parsers.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.*;
import java.util.ArrayList;

public class XMLOrderParser 
{
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private Document document;
	
	private int nextOrderToGrab;
	
	/*
	 * Constructor - instansiates object, document, and open file
	 * Takes in a file as a string to parse
	 * Returns instansiated object
	 */
	
	public XMLOrderParser(String file)
	{
		nextOrderToGrab = 0;
		try
		{
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			File inputFile = new File("order.xml");
			document = builder.parse(inputFile);
			
		}
		catch (Exception e)
		{
			System.out.println(e.getStackTrace());
		}
	}
	
	/*
	 * Finds the "newly created orders" that came in between two times
	 * Takes the shift to search, the lastTime searched, and the currentTime
	 * Returns an ArrayList of orders
	 */
	public ArrayList<Order> getNewOrders(int shift, String thisTime)
	{
		ArrayList<Order> myOrders = new ArrayList<Order>();
		
		 NodeList orderList = document.getElementsByTagName("order");

		 int thisPseudoTime = getPseudoTime(thisTime); 
			 
		 
		 //init order time for first while check
		
		 
		 boolean canAdd = true;
		 do
		 {
			 if(nextOrderToGrab<orderList.getLength())
			 {
				 Node currentOrderNode = orderList.item(nextOrderToGrab);
				 Element element = (Element) currentOrderNode;
				 if(currentOrderNode.getNodeType() == Node.ELEMENT_NODE) 
				 {
					 int currentOrderShift = Integer.parseInt(element.getElementsByTagName("shift").item(0).getTextContent());
					 String currentOrderTime = element.getElementsByTagName("time").item(0).getTextContent();
					 String mealName = element.getElementsByTagName("mealName").item(0).getTextContent();
					 String deliveryPointName = element.getElementsByTagName("deliveryName").item(0).getTextContent();
					 
					 if(thisPseudoTime > getPseudoTime(currentOrderTime) && shift == currentOrderShift)
					 {
						 Meal thisMeal = Main.getCurrentSetup().getMealFromName(mealName);
						 DeliveryPoint thisDeliveryPoint = Main.getCurrentSetup().getCurrentMap().getDeliveryPointFromName(deliveryPointName);
						 
						 Order newOrder = new Order(thisMeal,thisDeliveryPoint,1.0);
						 myOrders.add(newOrder);
						 nextOrderToGrab++;
					 }
					 else
					 {
						 canAdd = false; //if failed a test stop adding
					 }

				 }
			 }
			 else
			 {
				 canAdd = false; //if failed a test stop adding
			 }
		 }
		 while(canAdd == true);
		
		return myOrders;
		
	}
	
	/*
	 * Gets an integer for 
	 * 
	 * 
	 */
	public int getPseudoTime(String time)
	{
		 String[] timeInt = time.split(":",3);
		 int hour =  Integer.parseInt(timeInt[0]);
		 int min =  Integer.parseInt(timeInt[1]);
		 int sec =  Integer.parseInt(timeInt[2]);
		 
		 int pseudoTime = (hour * 60) + min;
		 
		 return pseudoTime;
	}
	
	
	
	
}
