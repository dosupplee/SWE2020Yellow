package droneSim;

import java.util.ArrayList;

public class CurrentSetup {
	final int numShifts = 5;

	private ArrayList<Meal> allMeals;
	private ArrayList<Food> allFoods;
	private ArrayList<Order> orderBacklog;
	private Map currentMap;
	
	public ArrayList<Meal> getAllMeals()
	{
		return allMeals;
	}
	
	public Map getCurrentMap()
	{
		return currentMap;
	}

	public void loadFoodSettings() {
		// TODO load the saved food settings
	}
	
	public void saveFoodSettings() {
		// TODO save the current food settings
	}
	
	/**
	 * Add meal to meal list
	 * @param mealToAdd
	 */
	public void addMeal(Meal mealToAdd) {
		allMeals.add(mealToAdd);
	}
	
	/**
	 * Add food to food list
	 * @param foodToAdd
	 */
	public void addFood(Food foodToAdd) {
		allFoods.add(foodToAdd);
	}
	
	/**
	 * removes food from food list and any 
	 * occurence in a meal
	 * 
	 * @param foodIndex
	 */
	public void deleteFood(int foodIndex) {
		int indexH = allFoods.size() - 1; // last index
		if(foodIndex < 0 || foodIndex > indexH) { // check if a valid index
			return; //TODO throw error??
		}
		
		// if valid, check if food is in a meal
		for (Meal meal : allMeals) {
			if (meal.getFoodItems().contains(allFoods.get(foodIndex))) { // if a meal contains food
				meal.removeFood(allFoods.get(foodIndex)); // remove food from meal
			}
		}
		
		allFoods.remove(foodIndex); // remove from food list
	}
	
	public void deleteMeal(int mealIndex) {
		// TODO implement deleting a meal
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
}
