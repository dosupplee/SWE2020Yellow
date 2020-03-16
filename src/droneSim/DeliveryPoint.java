package droneSim;

public class DeliveryPoint {
	
	private int x;
	private int y; 
	
	/**
	 * Creates a Delivery Point
	 * @param x
	 * @param y
	 */
	public DeliveryPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * the x to set
	 * @param x 
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * the y to set
	 * @param y 
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	

}
