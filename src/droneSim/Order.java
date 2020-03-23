package droneSim;


public class Order { 
	
	private Meal meal;					 // meal ordered
	private DeliveryPoint deliveryPoint; // location order is delivered too
	private double orderTime;			 // time ordered
	private double deliveryTime;		 // time delivered
	
	
	/**
	 * Creates an Order
	 * @param meal
	 * @param deliveryPoint
	 * @param orderTime
	 * @param deliveryTime
	 */
	public Order(Meal meal, DeliveryPoint deliveryPoint, double orderTime, double deliveryTime) {
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
	public Order(Meal meal, DeliveryPoint deliveryPoint, double orderTime) {
		this.meal = meal;
		this.deliveryPoint = deliveryPoint;
		this.orderTime = orderTime;
		this.deliveryTime = 0.0;
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
	public double getOrderTime() {
		return orderTime;
	}
	/**
	 * @param orderTime the orderTime to set
	 */
	public void setOrderTime(double orderTime) {
		this.orderTime = orderTime;
	}
	/**
	 * @return the deliveryTime
	 */
	public double getDeliveryTime() {
		return deliveryTime;
	}
	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(double deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	
}
