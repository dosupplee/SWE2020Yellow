/**
 * 
 */
package droneSim;

import java.util.ArrayList;

public class Main 
{
	
	final static int numShifts = 5;

	private static ArrayList<Meal> allMeals;
	private static ArrayList<Food> allFoods;
	private static ArrayList<Order> orderBacklog;
	private static Map currentMap;
	
	private static double avgTimeFIFO;
	private static double slowestTimeFIFO;
	private static double fastestTimeFIFO;
	private static double sumFIFO;
	
	private static double avgTimeKnapsack;
	private static double slowestTimeKnapsack;
	private static double fastestTimeKnapsack;
	private static double sumKnapsack;
	
	
	
	public static void main(String[] args) 
	{
		//Create some basic food stuffs
		ArrayList<Food> allFoods = new ArrayList<Food>();
		allFoods.add(new Food("Burger",6));
		allFoods.add(new Food("Drink",14));
		allFoods.add(new Food("FrenchFries",4));
		
		//Create some basic meal stuffs ArrayList<Meal> 
		allMeals = new ArrayList<Meal>();
		Meal comboMeal = new Meal("Combo Meal",100); //I shouldnt have to give it a weight here
		comboMeal.addFood(allFoods.get(0));
		comboMeal.addFood(allFoods.get(1));
		comboMeal.addFood(allFoods.get(2));
		allMeals.add(comboMeal);
		
		//Create some basic point stuffs
		ArrayList<DeliveryPoint> points = new ArrayList<DeliveryPoint>();
		points.add(new DeliveryPoint(5,12,"HAL"));
		points.add(new DeliveryPoint(12,18,"STEM"));
		currentMap = new Map("mainMap","C://MapLocation",points);
		
		XMLOrderGenerator gen = new XMLOrderGenerator(numShifts);
		gen.generateAllOrders();
		
	}
	
	public static ArrayList<Meal> getAllMeals()
	{
		return allMeals;
	}
	
	public static Map getCurrentMap()
	{
		return currentMap;
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
	
	public static void loadFoodSettings() {
		// TODO load the saved food settings
	}
	
	public static void saveFoodSettings() {
		// TODO save the current food settings
	}
	
	public static void addMeal(Meal mealToAdd) {
		// TODO add a new meal
	}
	
	public static void addFood(Food foodToAdd) {
		// TODO add a new food
	}
	
	public static void deleteFood(int foodIndex) {
		// TODO implement deleting a food
	}
	
	public static void deleteMeal(int mealIndex) {
		// TODO implement deleting a meal
	}
}
