package droneSim;

import java.util.ArrayList;
 
public class Meal {
	private String name; // name of meal
	private double weight; // weight of meal
	private double probability; // probability of being picked in an order
	private ArrayList<Food> foodItems;
	
	
	/**
	 * Creates a meal with no foods
	 * @param name
	 * @param weight
	 * @param probability
	 */
	public Meal(String name, double weight, double probability) {
		this.name = name;
		this.weight = weight;
		this.probability = probability;
		this.foodItems = new ArrayList<Food>();
	}
	
	/**
	 * Creates a meal with given foods
	 * @param name
	 * @param weight
	 * @param probability
	 * @param foods
	 */
	public Meal(String name, double weight, double probability, ArrayList<Food> foods) {
		this.name = name;
		this.weight = weight;
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
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	/**
	 * @return the probability
	 */
	public double getProbability() {
		return probability;
	}
	/**
	 * @param probability the probability to set
	 */
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	public ArrayList<Food> getFoodItems()
	{
		return foodItems;
	}


}
