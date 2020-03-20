package droneSim;

public class DeliveryPoint {
	
	private int x;
	private int y; 
	private String name;
	
	/**
	 * Creates a Delivery Point
	 * @param x
	 * @param y
	 */
	public DeliveryPoint(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
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
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name =name;
	}
	
	/**
	 * overide equals method 
	 */
	@Override 
	public boolean equals(Object o) {
		if (this == o) { // if same object
			return true;
		}
		if (!(o instanceof DeliveryPoint)) { // if other is not an instance of DeliveryPoint
			return false;
		}
		
		DeliveryPoint other = (DeliveryPoint) o;
		return (this.name.equals(other.name)  // check if same contents
				&& this.x == other.getX()
				&& this.y == other.getY() 
				);
	}

}
