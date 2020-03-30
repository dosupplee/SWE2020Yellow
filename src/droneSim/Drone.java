package droneSim;

/**
 * Note: Time is stored in seconds
 * 
 * @author LEHMANIT17
 *
 */
public class Drone {
	private String name; // the name of the drone
	private int weightCapacity; // the weight capacity of the drone (OZ)
	private int speed; // the speed of the drone (MPS)
	private int maxFlightTime; // the maximum flight time (seconds)
	private int turnAroundTime; // time between flights (seconds)
	private int dropOffTime; // time to unload the drone at a delivery point (seconds)

	/**
	 * Creates a custom Drone class
	 * 
	 * @param name
	 * @param weightCapacity
	 * @param speed (MPH, but stored in MPS)
	 * @param maxFlightTime
	 * @param turnAroundTime
	 * @param dropOffTime
	 */
	public Drone(String name, int weightCapacity, int speed, int maxFlightTime, int turnAroundTime,
			int dropOffTime) {
		this.name = name;
		this.weightCapacity = weightCapacity;
		this.speed = speed * 3600;
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
		speed = 20; // 20 mph
		maxFlightTime = 20 * 60; // 20 minutes
		turnAroundTime = 3 * 60; // 3 minutes
		dropOffTime = 30; // 30 seconds
	}

	/**
	 * get time between flights
	 * 
	 * @return the turnAroundTime
	 */
	public int getTurnAroundTime() {
		return turnAroundTime;
	}

	/**
	 * set time between flights (seconds)
	 * 
	 * @param turnAroundTime
	 */
	public void setTurnAroundTime(int turnAroundTime) {
		this.turnAroundTime = turnAroundTime;
	}

	/**
	 * get time to unload the drone at a delivery point (seconds)
	 * 
	 * @return the dropOffTime
	 */
	public int getDropOffTime() {
		return dropOffTime;
	}

	/**
	 * set time to unload the drone at a delivery point (seconds)
	 * 
	 * @param dropOffTime
	 */
	public void setDropOffTime(int dropOffTime) {
		this.dropOffTime = dropOffTime;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param weightCapacity
	 *            the weightCapacity to set
	 */
	public void setWeightCapacity(int weightCapacity) {
		this.weightCapacity = weightCapacity;
	}

	/**
	 * @return the speed in MPH
	 */
	public int getSpeedMPH() {
		return (speed / 3600);
	}

	/**
	 * @return the speed in MPS
	 */
	public int getSpeedMPS() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set (MPH
	 */
	public void setSpeedMPH(int speed) {
		this.speed = speed * 3600; // convert to MPS
	}

	/**
	 * @return the maxFlightTime
	 */
	public int getMaxFlightTime() {
		return maxFlightTime;
	}

	/**
	 * @param maxFlightTime
	 *            the maxFlightTime to set
	 */
	public void setMaxFlightTime(int maxFlightTime) {
		this.maxFlightTime = maxFlightTime;
	}

	/**
	 * Converts Seconds to Minutes
	 * 
	 * @param seconds
	 * @return
	 */
	public int convertStoM(int seconds) {
		return seconds / 60;
	}
	
	/**
	 * Converts Minutes to Seconds
	 * 
	 * @param minutes
	 * @return
	 */
	public int convertMtoS(int minutes) {
		return minutes * 60;
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
		return (this.name.equals(other.name) // check if same contents
				&& this.weightCapacity == other.getWeightCapacity() 
				&& this.speed == other.getSpeedMPH()
				&& this.maxFlightTime == other.getMaxFlightTime());
	}
}
