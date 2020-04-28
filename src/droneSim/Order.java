package droneSim;


public class Order { 
	
	private Meal meal;					 // meal ordered
	private DeliveryPoint deliveryPoint; // location order is delivered too
	private Time orderTime;			 	 // time ordered
	private Time deliveryTime;		     // time delivered
	private Time droneSentTime;			 // time the drone was sent out

	private int waitTime;				 // time waited in seconds
	


	/**
	 * Creates an Order
	 * @param meal
	 * @param deliveryPoint
	 * @param orderTime
	 * @param deliveryTime
	 */
	public Order(Meal meal, DeliveryPoint deliveryPoint, Time orderTime, Time deliveryTime) {
		this.meal = meal;
		this.deliveryPoint = deliveryPoint;
		this.orderTime = orderTime;
		this.deliveryTime = deliveryTime;
	}
	
	/*
	 * Version of constructor that does not require deliveryTime
	 * 
	 * 
	 */
	public Order(Meal meal, DeliveryPoint deliveryPoint, Time orderTime) {
		this.meal = meal;
		this.deliveryPoint = deliveryPoint;
		this.orderTime = orderTime;
		this.deliveryTime = new Time();
	}
	
	
	/**
	 * @return the waitTime
	 */
	public int getWaitTime() {
		return waitTime;
	}

	/**
	 * @param waitTime the waitTime to set
	 */
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}
	
	/**
	 * @return the meal
	 */
	public Meal getMeal() {
		return meal;
	}
	/**
	 * @param meal the meal to set
	 */
	public void setMeal(Meal meal) {
		this.meal = meal;
	}
	/**
	 * @return the deliveryPoint
	 */
	public DeliveryPoint getDeliveryPoint() {
		return deliveryPoint;
	}
	/**
	 * @param deliveryPoint the deliveryPoint to set
	 */
	public void setDeliveryPoint(DeliveryPoint deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}
	/**
	 * @return the orderTime
	 */
	public Time getOrderTime() {
		return orderTime;
	}
	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(Time orderTime) {
		this.orderTime = orderTime;
	}
	
	/**
	 * @return the droneSentTime
	 */
	public Time getDroneSentTime() {
		return droneSentTime;
	}

	/**
	 * @param droneSentTime the droneSentTime to set
	 */
	public void setDroneSentTime(Time droneSentTime) {
		this.droneSentTime = droneSentTime;
	}
	
	/**
	 * @return the deliveryTime
	 */
	public Time getDeliveryTime() {
		return deliveryTime;
	}
	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(Time deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	
	public int getOrderWeight() {
		return meal.getWeight();
	}
	
}
