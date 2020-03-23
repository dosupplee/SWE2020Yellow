package droneSim;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLOrderGenerator {

	private Random rand;
	private Document document;
	private Element rootElement;
	private CurrentSetup currentSetup;

	public XMLOrderGenerator(CurrentSetup currentSetup) {
		this.currentSetup = currentSetup;
		rand = new Random();
	}

	public void generateAllOrders() {
		try {
			// Create a factory builder instance
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			// Create new builder instance
			DocumentBuilder dBuilder = factory.newDocumentBuilder();

			// Create new document in our builder
			document = dBuilder.newDocument();

			// Create root
			rootElement = document.createElement("orders");
			document.appendChild(rootElement);


			for (int shift = 0; shift < currentSetup.getNumShifts(); shift++) // for each shift
			{
				for (int hour = 0; hour < currentSetup.getNumHours(); hour++) // for each hour
				{
					// generate random minutes
					ArrayList<Integer> randomMinutes = generateRandomOrderTimes(currentSetup.getOrdersPerHour(hour));

					for (int order = 0; order < currentSetup.getOrdersPerHour(hour); order++) // for each order
					{
						createIndividualOrder(shift, hour, randomMinutes.get(order));
					}
				}
			}

			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File("order.xml"));
			transformer.transform(source, result);

			// Output to console for testing
			//StreamResult consoleResult = new StreamResult(System.out);
			//transformer.transform(source, consoleResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Takes in the document to add the order to
	public void createIndividualOrder(int currentShift, int orderHour, int orderMin) {
		// create string timestamp
		String time = Integer.toString(orderHour) + ":" + Integer.toString(orderMin) + ":00";
		Meal meal = getRandomMeal();
		String clientName = "Client " + Integer.toString(rand.nextInt(1000));
		DeliveryPoint deliveryPoint = currentSetup.getCurrentMap().getRandomPoint();
		
		writeOrderXML(currentShift, time, meal, clientName, deliveryPoint);
	}

	/*
	 * Randomly selects a meal from the list of all meals using the probability
	 * Takes in nothing Return the randomly selected meal
	 */
	public Meal getRandomMeal() {
		Double randNum = rand.nextDouble(); // rand number to find random meal
		Meal ourMeal = null; // the meal that matches the random generator
		int i = 0; // the current meal id that we are inspecting
		double currentProb = 0.0; // the current aggregate proability we are up to
		do {
			Meal currentMeal = currentSetup.getMeal(i);

			// if the base prob + meals probs exceeds the random number
			if (currentProb + currentMeal.getScaledProbability() >= randNum) {
				ourMeal = currentMeal;
			}
			// otherwise increment current prob by meal's prob
			currentProb += currentMeal.getScaledProbability();
			i++;
		} while (currentProb < 1.0 || ourMeal == null);

		return ourMeal;
	}

	/*
	 * Generates and sorts a size amount of random ints Takes in integer of how many
	 * random numbers to generate Returns sorted array list
	 */
	public ArrayList<Integer> generateRandomOrderTimes(int size) {
		// create arraylist
		ArrayList<Integer> list = new ArrayList<Integer>();

		// generate random numbers for each order
		for (int i = 0; i < size; i++) {
			list.add(rand.nextInt(60));
		}

		// sort so the orders appear in the xml in order
		Collections.sort(list);

		return list;
	}

	// Takes in the document to add the order to
	public void writeOrderXML(int currentShift, String currentTime, Meal meal, String clientNameStr,
			DeliveryPoint point) {
		// System.out.println(document);

		// set root element
		Element order = document.createElement("order");
		rootElement.appendChild(order);

		// Create shift
		Element shift = document.createElement("shift");
		order.appendChild(shift);

		// set the shift number
		shift.appendChild(document.createTextNode(Integer.toString(currentShift)));

		// Create Timestamp
		Element time = document.createElement("time");
		order.appendChild(time);

		// Set Timestamp
		time.appendChild(document.createTextNode(currentTime));

		// Create Client Name
		Element clientName = document.createElement("clientName");
		order.appendChild(clientName);

		// set clientName
		clientName.appendChild(document.createTextNode(clientNameStr));

		// Create Delivery Point
		Element deliveryName = document.createElement("deliveryName");
		order.appendChild(deliveryName);

		// set Delivery Point
		deliveryName.appendChild(document.createTextNode(point.getName()));
		
		
		// Create Meal Name
		Element mealName = document.createElement("mealName");
		order.appendChild(mealName);

		// set Delivery Point
		mealName.appendChild(document.createTextNode(meal.getName()));

		// contents
		Element contents = document.createElement("contents");
		order.appendChild(contents);
		
		
		
		Element food;
		ArrayList<Food> foodItems = meal.getFoodItems();
		for (int i = 0; i < foodItems.size(); i++) // for each food item
		{
			food = document.createElement(foodItems.get(i).getName());
			contents.appendChild(food);
			// food.appendChild(document.createTextNode("1"));
		}
	}

}
