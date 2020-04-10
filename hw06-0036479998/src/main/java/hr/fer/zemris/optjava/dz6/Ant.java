package hr.fer.zemris.optjava.dz6;

import java.util.Arrays;

/**
 * This class represents a single ant as a TSP solution.
 * 
 * @author lmark
 *
 */
public class Ant {

	/**
	 * City indexes order.
	 */
	public int[] cityIndexes;
	
	/**
	 * Length of traversal.
	 */
	public double tourLength;
	
	public Ant() {
	}
	
	@Override
	public String toString() {
		return "len: " + tourLength + ", " + Arrays.toString(cityIndexes);
	}
	
	public Ant duplicate() {
		Ant copy = new Ant();
		copy.cityIndexes = new int[this.cityIndexes.length];
		copy.tourLength = this.tourLength;
		
		for(int i = 0; i < this.cityIndexes.length; i++) {
			copy.cityIndexes[i] = this.cityIndexes[i];
		}
		
		return copy;
	}
}
