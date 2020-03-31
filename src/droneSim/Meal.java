package droneSim;

import java.util.ArrayList;
 
public class Meal {
	private String name; // name of meal
	private double rawProb; // raw probability of being picked in an order (user input)
	private double scaledProb; // scaled probability of being picked in an order (use for picking order)
	private ArrayList<Food> foodItems; // foods in the meal
	
	
	/**
	 * Defualt Meal constructor.
	 * Creates a meal with:
	 * - burger
	 * - fry
	 * - drink
	 */
	public Meal() {
		name = "DefaultMeal";
		rawProb = 1.0;
		scaledProb = 1.0;
		foodItems = new ArrayList<Food>();

		// Create basic food items
		addFood(new Food("Burger", 6));
		addFood(new Food("Drink", 14));
		addFood(new Food("FrenchFries", 4));	
	}
	
	/**
	 * Creates a meal with no foods
	 * Must call adjustMealProbabilities() from currentSetup to scale
	 * probabilities accross meals
	 * @param name
	 * @param probability
	 */
	public Meal(String name, double rawProbability) {
		this.name = name;
		this.rawProb = rawProbability;
		this.scaledProb = 0.0;
		this.foodItems = new ArrayList<Food>();
	}
	
	/**
	 * Creates a meal with given foods
	 * Must call adjustMealProbabilities() from currentSetup to scale
	 * probabilities accross meals
	 * @param name
	 * @param probability
	 * @param foods
	 */
	public Meal(String name, double rawProbability, ArrayList<Food> foods) {
		this.name = name;
		this.rawProb = rawProbability;
		this.scaledProb = 0.0;
		
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
	public int getWeight() {
		int weight = 0;
		for (Food food : foodItems) {
			weight += food.getWeight();
		}
		return weight;
	}
	
	/**
	 * @return rawProb
	 */
	public double getRawProbability() {
		return rawProb;
	}
	
	/**
	 * @return scaledProb
	 */
	public double getScaledProbability() {
		return scaledProb; 
	}
	
	/**
	 * sets the raw probability
	 * @param rawProbability 
	 */
	public void setRawProbability(double rawProbability) {
		this.rawProb = rawProbability;
	}
	
	/**
	 * sets the raw probability
	 * @param rawProbability 
	 */
	public void setScaledProbability(double scaledProbability) {
		this.scaledProb = scaledProbability;
	}
	
	/**
	 * Returns all the food items in the meal
	 * @return foodItems
	 */
	public ArrayList<Food> getFoodItems()
	{
		return foodItems;
	}
	
	@Override
	public String toString() {
		String out = name + ",";
		out += rawProb + ",";
		out += scaledProb + ",";
		
		for (Food food : foodItems) {
			out += food.getName() + ",";
		}
		
		out = out.substring(0, out.lastIndexOf(",")); // get rid of last ","
		return out;
	}

	/**
	 * overide equals method 
	 */
	@Override 
	public boolean equals(Object o) {
		if (this == o) { // if same object
			return true;
		}
		if (!(o instanceof Meal)) { // if other is not an instance of Meal
			return false;
		}
		
		Meal other = (Meal) o;
		return (this.name.equals(other.name)  // check if same contents
				&& this.rawProb == other.getRawProbability()
				&& this.scaledProb == other.getScaledProbability()
				&& this.getWeight() == other.getWeight() 
				);
	}


}
