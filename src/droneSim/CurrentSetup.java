package droneSim;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class CurrentSetup {
	//TODO change to not final so the user can update these???
	private final int numShifts = 5; // number of shifts to simulate
	private final int numHours = 4; // how many hours there are in a shift
	private final int[] ordersPerHour = {15,17,22,15}; // must be the length of numHours


	private ArrayList<Meal> allMeals;
	private ArrayList<Food> allFoods;
	private ArrayList<DeliveryPoint> points;
	private Map currentMap;
	private Drone currentDrone;

	public CurrentSetup() {
		allFoods = new ArrayList<>();
		allMeals = new ArrayList<>();
		points = new ArrayList<>();
		
		loadDefaultFoodSettings();
		loadDefaultDroneSettings();
	}
		
	//loadFoodSettings("foodSettings_2020.csv");
	//saveFoodSettings("foodSettings_2020.csv");
	
	// --------------------------------------------
	// MAP STUFF
	// --------------------------------------------

	/**
	 * @return currentMap
	 */
	public Map getCurrentMap() {
		return currentMap;
	}

	/**
	 * set the current map
	 * 
	 * @param map
	 */
	public void setCurrentMap(Map map) {
		currentMap = map;
	}
	
	/**
	 * Add a delivery point
	 * @param point
	 */
	public void addDeliveryPoint(DeliveryPoint point) {
		points.add(point);
	}
	
	/**
	 * remove point by index
	 * @param pointIndex
	 */
	public void removeDeliveryPoint(int pointIndex) {
		int indexH = points.size() - 1; // last index
		if (pointIndex < 0 || pointIndex > indexH) { // check if a valid index
			return; // TODO throw error??
		}
		
		points.remove(pointIndex);
	}
	
	/**
	 * Remove point by object
	 * @param point
	 */
	public void removeDeliveryPoint(DeliveryPoint point) {
		if (points.contains(point)) {
			points.remove(point);
		}
	}
	
	/**
	 * returns all the delivery points
	 * @return points
	 */
	public ArrayList<DeliveryPoint> getDeliveryPoints() {
		return points;
	}

	
	// --------------------------------------------
	// FILE/SETTINGS STUFF
	// --------------------------------------------
	
	public void loadDefaultFoodSettings() {
		// TODO load the saved food settings
		
		clearFoodsAndMeals();
		
		// Create some basic food stuffs
		addFood(new Food("Burger", 6));
		addFood(new Food("Drink", 14));
		addFood(new Food("FrenchFries", 4));

		// Create some basic meal stuffs ArrayList<Meal>
		Meal comboMeal = new Meal("Combo Meal", 0.75); 
		comboMeal.addFood(getFood(0));
		comboMeal.addFood(getFood(1));
		comboMeal.addFood(getFood(2));
		addMeal(comboMeal);
		
		
		Meal frenchFryDelight = new Meal("French Fry Delight", 0.25); 
		frenchFryDelight.addFood(getFood(2));
		frenchFryDelight.addFood(getFood(2));
		frenchFryDelight.addFood(getFood(2));
		addMeal(frenchFryDelight);
		
		adjustMealProbabilities();

		// Create some basic point stuffs

		addDeliveryPoint(new DeliveryPoint(0, 5, "HAL"));
		addDeliveryPoint(new DeliveryPoint(-2, -8, "STEM"));
		addDeliveryPoint(new DeliveryPoint(9, -15, "Lincoln"));
		addDeliveryPoint(new DeliveryPoint(9, -6, "Library"));
		
		
		setCurrentMap(new Map("mainMap", "C://MapLocation", getDeliveryPoints()));
	}
	
	
	public void loadFoodSettings(File file) {
		File csvFile = file; // open file
		if (!csvFile.exists()) { // if the file does not exist
			System.err.println("File not found");
			return;
		}
		
		clearFoodsAndMeals();
		
		try {
			Scanner fileReader = new Scanner(csvFile);
			String currentLine = "";
			boolean stillFoods = true;
			boolean moreMeals = true;
			
			//------------------
			// FOODS
			//------------------
			if (fileReader.hasNextLine()) {
				String titlesFood = fileReader.nextLine(); // get the titles (not used)
			}
			
			
			
			while (fileReader.hasNextLine() && stillFoods) { //  while more foods
				currentLine = fileReader.nextLine();
				//System.out.println(currentLine);
				
				// parse current line
				String[] items = currentLine.split(",");
				
				if (items.length == 2) { // if name, weight
					String name = items[0].trim();
					int weight = Integer.parseInt(items[1].trim());
					
					// add food to list
					Food food = new Food(name, weight);
					allFoods.add(food);
				} else { // if starting meal section
					stillFoods = false;
				}
			}
			
			
			//------------------
			// MEALS
			//------------------
			// get meal titles
			String titlesMeal = currentLine; // get the titles (not used)
			
			while (fileReader.hasNextLine() && moreMeals) { //  while more foods
				currentLine = fileReader.nextLine();
				//System.out.println(currentLine);
				
				// parse current line
				String[] items = currentLine.split(",");
				
				if (items.length >= 3) { // if a meal
					String name = items[0].trim();
					double rawProb = Double.parseDouble(items[1].trim());
					double scaledProb = Double.parseDouble(items[2].trim());
					
					Meal meal = new Meal(name, rawProb);
					meal.setScaledProbability(scaledProb);
					
					// add the foods
					for (int i = 3; i < items.length; i++) {
						String foodName = items[i].trim(); // name of food
					
						boolean foodFound = false;
						for (Food food : allFoods) { // get food 
							if (food.getName().equals(foodName)) {
								meal.addFood(food);
								foodFound = true;
							}
						}
						if (!foodFound) { // if not a valid food
							System.err.println("Food \"" + foodName + "\" not found.");
						}
					}
					
					allMeals.add(meal); // add meal to list
					
				} else { // if starting meal section
					moreMeals = false;
				}
			}
			
			
			fileReader.close(); // close the file reader
			adjustMealProbabilities();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Save food in a csv format
	 * all foods
	 * all meals
	 * -----------------------
	 * name, weight (oz)
	 * <pizza>,<5>
	 * .
	 * .
	 * .
	 * name, rawProb, scaledProb, food names
	 * <french fry delight>, <30>, <0.3>, <fry>, <fry>,<fry>
	 * .
	 * .
	 * .
	 */
	public void saveFoodSettings(File file) {
		if (allFoods == null || allFoods.size() == 0) { // if no food to save
			return;
		}
		
		File csvFile = file; // open/create file
		try {
			PrintWriter fileWriter = new PrintWriter(csvFile); // create output stream
		
			//---------------------
			// add the titles
			//---------------------
			fileWriter.append("name,weight (oz)" + "\n");
			
			//---------------------
			// add the foods
			//---------------------
			for (Food food : allFoods) {
				String foodCSVformat = food.toString();
				fileWriter.append(foodCSVformat + "\n"); // append food item with new line
			}
			
			//---------------------
			// add the meals
			//---------------------
			fileWriter.append("name,raw prob,scaled prob,foods:" + "\n"); // add meal header
			for (Meal meal : allMeals) {
				String mealCSVformat = meal.toString();
				fileWriter.append(mealCSVformat + "\n"); // append food item with new line
			}
			
			//---------------------
			// flush & close the file writer
			//---------------------
			fileWriter.flush();
			fileWriter.close();;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		}
	}

	// --------------------------------------------
	// FOOD STUFF
	// --------------------------------------------

	/**
	 * @return allFoods
	 */
	public ArrayList<Food> getAllFoods() {
		return allFoods;
	}

	/**
	 * @return allMeals
	 */
	public ArrayList<Meal> getAllMeals() {
		return allMeals;
	}

	/**
	 * Add meal to meal list
	 * 
	 * @param mealToAdd
	 */
	public void addMeal(Meal mealToAdd) {
		allMeals.add(mealToAdd);
		adjustMealProbabilities(); // readjust the probabilities to = 100%
	}

	/**
	 * Add food to food list
	 * 
	 * @param foodToAdd
	 */
	public void addFood(Food foodToAdd) {
		allFoods.add(foodToAdd);
	}

	// TODO throw error??
	public Food getFood(int foodIndex) {
		int indexH = allFoods.size() - 1; // last index
		if (foodIndex < 0 || foodIndex > indexH) { // check if a valid index
			return new Food("NULL", 0); // TODO throw error??
		}

		return allFoods.get(foodIndex);
	}

	// TODO throw error??
	public Meal getMeal(int mealIndex) {
		int indexH = allMeals.size() - 1; // last index
		if (mealIndex < 0 || mealIndex > indexH) { // check if a valid index
			return new Meal("NULL", 0.0); // TODO throw error??
		}

		return allMeals.get(mealIndex);
	}
	
	
	/*
	 * Iterates through all existing meals and finds the one with specified name
	 * Takes in the mealName to look for
	 * Returns the meal
	 */
	public Meal getMealFromName(String name)
	{
		for(int i=0;i<allMeals.size();i++)
		{
			if(allMeals.get(i).getName().equals(name))
				return allMeals.get(i);
		}
		
		return null;
		
	}

	/**
	 * removes food from food list and any occurence in a meal
	 * 
	 * @param foodIndex
	 */
	public void deleteFood(int foodIndex) {
		int indexH = allFoods.size() - 1; // last index
		if (foodIndex < 0 || foodIndex > indexH) { // check if a valid index
			return; // TODO throw error??
		}

		// if valid, check if food is in a meal
		for (Meal meal : allMeals) {
			if (meal.getFoodItems().contains(allFoods.get(foodIndex))) { // if a meal contains food
				meal.removeFood(allFoods.get(foodIndex)); // remove food from meal
			}
		}

		allFoods.remove(foodIndex); // remove from food list
	}
	
	/**
	 * remove food by object
	 * removes from allFoods and allMeals
	 * @param food
	 */
	public void deleteFood(Food food) {
		// check if food is in a meal
		for (Meal meal : allMeals) {
			if (meal.getFoodItems().contains(food)) { // if a meal contains food
				meal.removeFood(food); // remove food from meal
			}
		}
		if(allFoods.contains(food)) {
			allFoods.remove(food);
		}
	}

	/**
	 * removes meal from meal list
	 * 
	 * @param mealIndex
	 */
	public void deleteMeal(int mealIndex) {
		int indexH = allMeals.size() - 1; // last index
		if (mealIndex < 0 || mealIndex > indexH) { // check if a valid index
			return; // TODO throw error??
		}

		allMeals.remove(mealIndex); // remove from meal list
	}
	
	/**
	 * removes meal by object
	 * @param meal
	 */
	public void deleteMeal(Meal meal) {
		if (allMeals.contains(meal)) {
			allMeals.remove(meal); // remove from meal list
		}
	}

	/**
	 * Rescales all the probabilies so they add up to 100
	 * 
	 * Run whenever you change the probabilites or add new meals.
	 */
	public void adjustMealProbabilities() {
		if (allMeals.isEmpty()) { // if there are no meals
			return;
		}

		// get sum of all probabilities
		double probSum = 0;
		for (Meal meal : allMeals) {
			probSum += meal.getRawProbability();
		}

		// set new scaled probabilities (multiply by 100 for % output)
		for (Meal meal : allMeals) {
			double newProb = meal.getRawProbability() / probSum;
			meal.setScaledProbability(newProb);
		}
	}
	
	
	
	// --------------------------------------------
	// RUNNING STUFF
	// --------------------------------------------
	
	/**
	 * @return the numShifts
	 */
	public int getNumShifts() {
		return numShifts;
	}

	/**
	 * @return the numHours
	 */
	public int getNumHours() {
		return numHours;
	}
	
	/**
	 * returns the orders for a given hour
	 * @return the ordersPerHour
	 */
	public int getOrdersPerHour(int hour) {
		return ordersPerHour[hour];
	}	
	
	// --------------------------------------------
	// DRONE STUFF
	// --------------------------------------------
	
	/**
	 * Loads custom drone settings
	 */
	public void loadCustomDroneSettings() {
		//TODO 
		//currentDrone = new Drone(name, weightCapacity, speed, maxFlightTime, turnAroundTime, dropOffTime)	
	}
	
	/**
	 * Loads the default drone settings
	 */
	public void loadDefaultDroneSettings() {
		currentDrone = new Drone();
	}
	
	public int getDroneWeight()
	{
		return currentDrone.getWeightCapacity();
	}
	
	public int sendDrone(ArrayList<Order> orders) {
		return currentDrone.runTSP(orders);
	}
	
	public String[] getAllFoodNames() {
		String[] names = new String[allFoods.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = allFoods.get(i).getName(); 
		}
		return names;
	}
	
	public void clearFoodsAndMeals() {
		allFoods.clear();
		allMeals.clear();
	}
}
