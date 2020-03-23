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

		 Time currentTime = new Time(thisTime);
		
		 
		 boolean canAdd = true; //if we can keep looking for more orders
		 do
		 {
			 if(nextOrderToGrab<orderList.getLength()) //if havent reached end of all total orders
			 {
				 Node currentOrderNode = orderList.item(nextOrderToGrab);
				 Element element = (Element) currentOrderNode;
				 
				 if(currentOrderNode.getNodeType() == Node.ELEMENT_NODE) //if of right type
				 {
					 //get the values from the XML
					 int currentOrderShift = Integer.parseInt(element.getElementsByTagName("shift").item(0).getTextContent());
					 String currentOrderTime = element.getElementsByTagName("time").item(0).getTextContent();
					 Time orderTimeObject = new Time(currentOrderTime);
					 String mealName = element.getElementsByTagName("mealName").item(0).getTextContent();
					 String deliveryPointName = element.getElementsByTagName("deliveryName").item(0).getTextContent();
					 
					 //if it has passed our current time ie if we can 'see' this order yet
					 if(currentTime.compareTo(orderTimeObject) > 0 && shift == currentOrderShift)
					 {
						//find values from list 
						 Meal thisMeal = Main.getCurrentSetup().getMealFromName(mealName);
						 DeliveryPoint thisDeliveryPoint = Main.getCurrentSetup().getCurrentMap().getDeliveryPointFromName(deliveryPointName);
						 
						 //create new order with those values
						 Order newOrder = new Order(thisMeal,thisDeliveryPoint,orderTimeObject);
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
	
	
}
