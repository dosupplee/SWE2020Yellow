package droneSim;


public class Food {
	private String name;   // the name of the food item
	private int weight; // the weight of the food item
	
	
	/**  
	 * Food constructor: takes name and weight
	 * @param name
	 * @param weight
	 */
	public Food(String name, int weight) {
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
	public int getWeight() {
		return weight;
	}
	
	/**
	 * @param weight
	 * Sets food weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public String toString() {
		return name + "," + weight;
	}
	
	/**
	 * overide equals method 
	 */
	@Override 
	public boolean equals(Object o) {
		if (this == o) { // if same object
			return true;
		}
		if (!(o instanceof Food)) { // if other is not an instance of Food
			return false;
		}
		
		Food other = (Food) o; // check if same contents
		return (this.name.equals(other.name) && this.weight == other.getWeight());
	}

}
