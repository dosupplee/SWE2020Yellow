package droneSim;

import java.util.ArrayList;
 
public class Meal {
	private String name; // name of meal
	private double probability; // probability of being picked in an order
	private ArrayList<Food> foodItems; // foods in the meal
	
	
	/**
	 * Creates a meal with no foods
	 * @param name
	 * @param probability
	 */
	public Meal(String name, double probability) {
		this.name = name;
		this.probability = probability;
		this.foodItems = new ArrayList<Food>();
	}
	
	/**
	 * Creates a meal with given foods
	 * @param name
	 * @param probability
	 * @param foods
	 */
	public Meal(String name, double probability, ArrayList<Food> foods) {
		this.name = name;
		this.probability = probability;
		
		// deep copy the foods
		this.foodItems = new ArrayList<Food>();
		for (Food food : foods) { 
			this.foodItems.add(food);
		}
	}
	
	
	/**
	 * Adds a food to meal
	 * @param food
	 */
	public void addFood(Food food) {
		foodItems.add(food);
	}
	
	/**
	 * Removes a food from meal
	 * @param food
	 */
	public void removeFood(Food food) {
		if(foodItems.contains(food)) {
			foodItems.remove(food);
		}
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * set the meal's name
	 * @param name 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the weight
	 */
	public double getWeight() {
		int weight = 0;
		for (Food food : foodItems) {
			weight += food.getWeight();
		}
		return weight;
	}
	
	/**
	 * @return probability
	 */
	public double getProbability() {
		return probability;
	}
	
	/**
	 * sets the probability
	 * @param probability 
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	/**
	 * Returns all the food items in the meal
	 * @return foodItems
	 */
	public ArrayList<Food> getFoodItems()
	{
		return foodItems;
	}


}
