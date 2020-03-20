package droneSim;

/**
 * Note: Time is stored in seconds
 * 
 * @author LEHMANIT17
 *
 */
public class Drone {
	private String name;   // the name of the drone
	private int weightCapacity; // the weight capacity of the drone (OZ)
	private double speed; // the speed of the drone (MPH)
	private double maxFlightTime; // the maximum flight time (seconds)
	private double turnAroundTime; // time between flights (seconds)
	private double dropOffTime; // time to unload the drone at a delivery point (seconds)
	
	/**
	 * Creates a custom Drone class
	 * @param name
	 * @param weightCapacity
	 * @param speed
	 * @param maxFlightTime
	 * @param turnAroundTime
	 * @param dropOffTime
	 */
	public Drone(String name, int weightCapacity, double speed, double maxFlightTime, double turnAroundTime, double dropOffTime) {
		this.name = name;
		this.weightCapacity = weightCapacity;
		this.speed = speed;
		this.maxFlightTime = maxFlightTime;
		this.turnAroundTime = turnAroundTime;
		this.dropOffTime = dropOffTime;
	}
	
	/**
	 * Default constructor with default values
	 */
	public Drone() {
		name = "DefaultDrone";
		weightCapacity = 12 * 16; // in oz
		speed = 20.0; // 20 mph
		maxFlightTime = 20.0 * 60.0; // 20 minutes
		turnAroundTime = 3.0 * 60.0; // 3 minutes
		dropOffTime = 30.0; // 30 seconds
	}

	/**
	 * get time between flights
	 * @return the turnAroundTime
	 */
	public double getTurnAroundTime() {
		return turnAroundTime;
	}

	/**
	 * set time between flights
	 * @param turnAroundTime 
	 */
	public void setTurnAroundTime(double turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	/**
	 * get time to unload the drone at a delivery point
	 * @return the dropOffTime
	 */
	public double getDropOffTime() {
		return dropOffTime;
	}

	/**
	 * set time to unload the drone at a delivery point
	 * @param dropOffTime 
	 */
	public void setDropOffTime(double dropOffTime) {
		this.dropOffTime = dropOffTime;
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
	public int getWeightCapacity() {
		return weightCapacity;
	}

	/**
	 * @param weightCapacity the weightCapacity to set
	 */
	public void setWeightCapacity(int weightCapacity) {
		this.weightCapacity = weightCapacity;
	}

	/**
	 * @return the speed in MPH
	 */
	public double getSpeedMPH() {
		return speed;
	}
	
	/**
	 * @return the speed in MPS
	 */
	public double getSpeedMPS() {
		return (speed * 3600.0);
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
				&& this.speed == other.getSpeedMPH()
				&& this.maxFlightTime == other.getMaxFlightTime()
				);
	}
}
