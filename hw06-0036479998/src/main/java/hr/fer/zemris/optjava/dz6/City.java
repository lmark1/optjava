package hr.fer.zemris.optjava.dz6;

/**
 * This class represents a city with its coordinates.
 * 
 * @author lmark
 *
 */
public class City {

	public int ID;
	public double x;
	public double y;
	
	public City(int ID, double x, double y) {
		this.ID = ID;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public String toString() {
		return ID + ", " + x + ", " + y + "\n";
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof City) {
			if (((City)obj).ID == this.ID) {
				return true;
			} else {
				return false;
			}
		}
		return super.equals(obj);
	}
}
