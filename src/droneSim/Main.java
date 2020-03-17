/**
 * 
 */
package droneSim;

import java.util.ArrayList;

public class Main 
{
	
	final static int numShifts = 5;

	private static ArrayList<Meal> allMeals;
	private static Map currentMap;
	
	public static void main(String[] args) 
	{
		//Create some basic food stuffs
		ArrayList<Food> allFoods = new ArrayList<Food>();
		allFoods.add(new Food("Burger",6));
		allFoods.add(new Food("Drink",14));
		allFoods.add(new Food("FrenchFries",4));
		
		//Create some basic meal stuffs ArrayList<Meal> 
		allMeals = new ArrayList<Meal>();
		Meal comboMeal = new Meal("Combo Meal",24,100); //I shouldnt have to give it a weight here
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
	
	

	

}
