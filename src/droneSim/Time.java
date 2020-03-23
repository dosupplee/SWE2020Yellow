package droneSim;

/**
 * 24 hour time class
 * 
 * @author LEHMANIT17
 *
 */
public class Time {
	private int shiftNum; // current shift # (starts at 1)
	private long seconds; // seconds since start time
	private int startHour, startMinute, startSecond; // the starting time for this instance

	// --------------------------------------------
	// CONSTRUCTOR STUFF
	// --------------------------------------------

	/**
	 * Time Default constructor: time = "0:0:0"
	 */
	public Time() {
		shiftNum = 1;
		seconds = 0;
		startHour = 0;
		startMinute = 0;
		startSecond = 0;
	}

	/**
	 * Time constructor with just start time
	 * 
	 * @param startHour
	 * @param startMinute
	 * @param startSecond
	 */
	public Time(int startHour, int startMinute, int startSecond) {
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.startSecond = startSecond;
	}

	/**
	 * Time constructor with all options
	 * 
	 * @param shiftNum
	 * @param seconds
	 * @param startHour
	 * @param startMinute
	 * @param startSecond
	 */
	public Time(int shiftNum, long seconds, int startHour, int startMinute, int startSecond) {
		this.shiftNum = shiftNum;
		this.seconds = seconds;
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.startSecond = startSecond;
	}

	/**
	 * Time constructor that takes in "h:m:s"
	 * 
	 * @param time
	 */
	public Time(String time) {
		shiftNum = 1;
		seconds = 0;
		parseStartTimeString(time);
	}

	/**
	 * returns a copy of this time instance
	 * 
	 * @return
	 */
	public Time copy() {
		return new Time(shiftNum, seconds, startHour, startMinute, startSecond);
	}

	// --------------------------------------------
	// TIMER STUFF
	// --------------------------------------------

	/**
	 * returns the timer in seconds
	 * 
	 * @return
	 */
	public long getTimer() {
		return seconds;
	}

	/**
	 * set timer to 0 seconds
	 */
	public void resetTimer() {
		seconds = 0;
	}

	/**
	 * adds 1 second to timer
	 */
	public void incrementTimerSecond() {
		seconds++;
	}
	/**
	 * adds 1 minute to timer
	 */
	public void incrementTimerMinute() {
		seconds += 60;
	}

	// --------------------------------------------
	// SHIFT STUFF
	// --------------------------------------------

	/**
	 * returns the shift number
	 * 
	 * @return
	 */
	public int getShift() {
		return shiftNum;
	}

	/**
	 * set shift to 1
	 */
	public void resetShift() {
		shiftNum = 1;
	}

	/**
	 * increments shift by 1
	 */
	public void incrementShift() {
		shiftNum++;
	}

	// --------------------------------------------
	// STRING STUFF
	// --------------------------------------------

	/**
	 * Set the start time for time instance from given string. Format time as
	 * "h:m:s"
	 * 
	 * @param time
	 */
	public void parseStartTimeString(String time) {
		String times[] = time.split(":");
		startHour = Integer.parseInt(times[0]);
		startMinute = Integer.parseInt(times[1]);
		startSecond = Integer.parseInt(times[2]);
	}

	/**
	 * Returns current time "h:m:s"  
	 */
	@Override
	public String toString() {
		// calculte seconds
		int secondsToAdd = (int) (seconds % 60);

		// calculate minutes
		int minutesToAdd = (int) (seconds / 60); // s * (s/m) = m
		int exraHours = minutesToAdd / 60; // for if more than 60 min, else 0
		minutesToAdd %= 60; // puts in 0-59 min range

		// calculate hours
		int hoursToAdd = exraHours; // add the extra hours from minutes
		hoursToAdd %= 24; // puts in 0-24 min range

		String out = (hoursToAdd) + ":"; // hours
		out += (minutesToAdd) + ":"; // minutes
		out += (secondsToAdd); // seconds

		return out;
	}

}