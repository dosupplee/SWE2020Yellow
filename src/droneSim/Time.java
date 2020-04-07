package droneSim;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 24 hour time class
 * 
 * @author LEHMANIT17
 *
 */
public class Time implements Comparable<Time>{
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
	 * Time constructor: time = "0:0:0" + timer seconds
	 */
	public Time(long timerSeconds) {
		shiftNum = 1;
		seconds = timerSeconds;
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

	// --------------------------------------------
	// TIME TOOL'S STUFF
	// --------------------------------------------

	/**
	 * returns a copy of this time instance
	 * 
	 * @return
	 */
	public Time copy() {
		return new Time(shiftNum, seconds, startHour, startMinute, startSecond);
	}

	/**
	 * Do Stop - Start
	 * 
	 * Returns (thisT - thatT) uses the current time
	 * returns a time instance with start time 0:0:0
	 * 
	 * @param that
	 * @return
	 */
	public Time subtractTime(Time that) {
		String time1 = this.toString();
		String time2 = that.toString();
 
		Time newTime = null;
		try { // get the difference in seconds
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			Date date1 = format.parse(time1);
			Date date2 = format.parse(time2);
			long difference = date1.getTime() - date2.getTime();
			long seconds = difference/1000; // convert from mili-sec to sec
			
			newTime = new Time(seconds);
		} catch (Exception e) {
			System.err.println("Bad Time Format");
		}
		
		
		return newTime;
	}
	
	
	/**
	 * returns (+) if this > that
	 * @param otherTime
	 * @return
	 */
	@Override
	public int compareTo(Time otherTime) {
		int pseudoTime = (startHour * 60) + startMinute;
		int otherPseudoTime = (otherTime.startHour * 60) + otherTime.startMinute;

		return pseudoTime - otherPseudoTime;
	}

	/**
	 * overide equals method 
	 */
	@Override 
	public boolean equals(Object o) {
		if (this == o) { // if same object
			return true;
		}
		if (!(o instanceof Time)) { // if other is not an instance of Meal
			return false;
		}
		
		Time other = (Time) o;
		return (this.shiftNum == other.shiftNum // check if same contents
				&& this.seconds == other.seconds
				&& this.startHour == other.startHour
				&& this.startMinute == other.startMinute 
				&& this.startSecond == other.startSecond 
				);
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
	 * adds n seconds to timer
	 */
	public void incrementTimerSecond(int n) {
		seconds += n;
	}


	/**
	 * adds 1 minute to timer
	 */
	public void incrementTimerMinute() {
		seconds += 60;
	}
	
	/**
	 * adds n minutes to timer
	 */
	public void incrementTimerMinute(int n) {
		seconds += 60 * n;
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
		String out = getCurrentHour() + ":"; // hours
		out += getCurrentMinute() + ":"; // minutes
		out += getCurrentSecond(); // seconds

		return out;
	}

	// --------------------------------------------
	// GET/SET STUFF
	// --------------------------------------------

	// -------------------
	// CURRENT TIME
	// -------------------

	/**
	 * return the current hour
	 * 
	 * @return
	 */
	public int getCurrentHour() {
		// calculate minutes
		int minutesToAdd = startMinute + (int) ((seconds + startSecond) / 60); // s * (s/m) = m

		int exraHours = startHour + (minutesToAdd / 60); // for if more than 60 min, else 0
		minutesToAdd %= 60; // puts in 0-59 min range

		// calculate hours
		int currentHour = exraHours; // add the extra hours from minutes
		currentHour %= 24; // puts in 0-24 hr range
		return currentHour;
	}

	/**
	 * return the current minute
	 * 
	 * @return
	 */
	public int getCurrentMinute() {
		// calculate minutes
		int currentMinute = startMinute + (int) ((seconds + startSecond) / 60); // s * (s/m) = m
		currentMinute %= 60; // puts in 0-59 min range

		return currentMinute;
	}

	/**
	 * return the current Second
	 * 
	 * @return
	 */
	public int getCurrentSecond() {
		// calculte seconds
		return (int) ((seconds + startSecond) % 60);
	}

	// -------------------
	// START TIME
	// -------------------

	/**
	 * @return the startHour
	 */
	public int getStartHour() {
		return startHour;
	}

	/**
	 * @param startHour
	 *            the startHour to set
	 */
	public void setStartHour(int startHour) {
		this.startHour = startHour;
	}

	/**
	 * @return the startMinute
	 */
	public int getStartMinute() {
		return startMinute;
	}

	/**
	 * @param startMinute
	 *            the startMinute to set
	 */
	public void setStartMinute(int startMinute) {
		this.startMinute = startMinute;
	}

	/**
	 * @return the startSecond
	 */
	public int getStartSecond() {
		return startSecond;
	}

	/**
	 * @param startSecond
	 *            the startSecond to set
	 */
	public void setStartSecond(int startSecond) {
		this.startSecond = startSecond;
	}

	public void setShift(int shift) {
		
		this.shiftNum = shift;
		
	}

}
