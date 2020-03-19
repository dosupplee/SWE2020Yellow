package droneSim;

public class Food {
	private String name;   // the name of the food item
	private double weight; // the weight of the food item
	
	
	/**  
	 * Food constructor: takes name and weight
	 * @param name
	 * @param weight
	 */
	public Food(String name, double weight) {
		this.name = name;
		this.weight = weight;
	}
	
	/**
	 * @return Returns food name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param food
	 * Sets food name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Returns food weight
	 */
	public double getWeight() {
		return weight;
	}
	
	/**
	 * @param weight
	 * Sets food weight
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

}
