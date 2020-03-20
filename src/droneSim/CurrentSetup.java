package droneSim;

import java.util.ArrayList;

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
	
	public void loadFoodSettings() {
		// TODO load the saved food settings
		
	}
	
	public void loadDefaultFoodSettings() {
		// TODO load the saved food settings
		
		// Create some basic food stuffs
		addFood(new Food("Burger", 6));
		addFood(new Food("Drink", 14));
		addFood(new Food("FrenchFries", 4));

		// Create some basic meal stuffs ArrayList<Meal>
		Meal comboMeal = new Meal("Combo Meal", 1.0); // I shouldnt have to give it a weight here
		comboMeal.addFood(getFood(0));
		comboMeal.addFood(getFood(1));
		comboMeal.addFood(getFood(2));
		addMeal(comboMeal);

		// Create some basic point stuffs

		addDeliveryPoint(new DeliveryPoint(0, 0, "SAC"));
		addDeliveryPoint(new DeliveryPoint(5, 12, "HAL"));
		addDeliveryPoint(new DeliveryPoint(12, 18, "STEM"));
		
		
		setCurrentMap(new Map("mainMap", "C://MapLocation", getDeliveryPoints()));
	}

	public void saveFoodSettings() {
		// TODO save the current food settings
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
			return new Food("NULL", 0.0); // TODO throw error??
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
			probSum += meal.getProbability();
		}

		// set new scaled probabilities (multiply by 100 for % output)
		for (Meal meal : allMeals) {
			double newProb = meal.getProbability() / probSum;
			meal.setProbability(newProb);
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
}
