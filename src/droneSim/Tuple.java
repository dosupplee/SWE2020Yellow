package droneSim;

/**
 * Creates a tuple of two object types (A, B)
 * @author LEHMANIT17
 *
 */
public class Tuple {

	// abstract objects
	private Object a; 
	private Object b;
	private double latitude;
	private double longitude;
	
	/**
	 * Default constructor for (a of type A, b of type B)
	 * @param a
	 * @param b
	 */
	public Tuple(Object a, Object b) {
		this.a = a;
		this.b = b;
	}
	
	public Tuple(double a, double b) {
		latitude = a;
		longitude = b;
	}

	/**
	 * @return the a
	 */
	public Object getA() {
		return a;
	}

	/**
	 * @param a the a to set
	 */
	public void setA(Object a) {
		this.a = a;
	}

	/**
	 * @return the b
	 */
	public Object getB() {
		return b;
	}

	/**
	 * @param b the b to set
	 */
	public void setB(Object b) {
		this.b = b;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
