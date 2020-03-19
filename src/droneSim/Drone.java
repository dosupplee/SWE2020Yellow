package droneSim;

public class Drone {
	private String name;   // the name of the drone
	private double weightCapacity; // the weight capacity of the drone
	private double speed; // the speed of the drone
	private double maxFlightTime; // the maximum flight time
	
	/**
	 * Creates a Drone class
	 * @param name
	 * @param weightCapacity
	 * @param speed
	 * @param maxFlightTime
	 */
	public Drone(String name, double weightCapacity, double speed, double maxFlightTime) {
		this.name = name;
		this.weightCapacity = weightCapacity;
		this.speed = speed;
		this.maxFlightTime = maxFlightTime;
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
	 * @return the weightCapacity
	 */
	public double getWeightCapacity() {
		return weightCapacity;
	}

	/**
	 * @param weightCapacity the weightCapacity to set
	 */
	public void setWeightCapacity(double weightCapacity) {
		this.weightCapacity = weightCapacity;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return the maxFlightTime
	 */
	public double getMaxFlightTime() {
		return maxFlightTime;
	}

	/**
	 * @param maxFlightTime the maxFlightTime to set
	 */
	public void setMaxFlightTime(double maxFlightTime) {
		this.maxFlightTime = maxFlightTime;
	}
	

	/**
	 * overide equals method 
	 */
	@Override 
	public boolean equals(Object o) {
		if (this == o) { // if same object
			return true;
		}
		if (!(o instanceof Drone)) { // if other is not an instance of Drone
			return false;
		}
		
		Drone other = (Drone) o;
		return (this.name.equals(other.name)  // check if same contents
				&& this.weightCapacity == other.getWeightCapacity()
				&& this.speed == other.getSpeed()
				&& this.maxFlightTime == other.getMaxFlightTime()
				);
	}
}
