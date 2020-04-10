package hr.fer.zemris.optjava.dz5.impl;

import java.util.Arrays;
import java.util.Random;

/**
 * Solution represented as an array of doubles.
 * 
 * @author lmark
 *
 */
public class IntArraySolution extends SingleObjectiveSolution{
	
	/**
	 * Array of double values.
	 */
	public int[] values;
	
	public long ID;
	
	/**
	 * Initializes double array.
	 * 
	 * @param n
	 */
	public IntArraySolution(int n) {
		values = new int[n];
	}
	
	/**
	 * @return Returns a new solution like this one.
	 */
	public IntArraySolution newLikeThis() {
		return new IntArraySolution(values.length);
	}
	
	/**
	 * @return Returns duplicate solution.
	 */
	public IntArraySolution duplicate() {
		int[] copyValues = new int[values.length];
		
		/**
		 * Copy all the current values.
		 */
		for(int i = 0, maxLen = values.length; i < maxLen; i++) {
			copyValues[i] = values[i];
		}
		
		IntArraySolution copySol = new IntArraySolution(values.length);
		copySol.values = copyValues;
		copySol.fitness = fitness;
		copySol.value = value;
		
		return copySol;
	}
	
	public IntArraySolution randomize(Random random, double[] deltas) {
		for(int i = 0, maxLen = values.length; i < maxLen; i++) {
			values[i] += -deltas[i]/2 + random.nextDouble()*deltas[i];
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		return "F: " + fitness + ", " + Arrays.toString(values) + "\n";
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof IntArraySolution) {
			if (((IntArraySolution)obj).ID == this.ID) {
				return true;
			} else {
				return false;
			}
		}
		
		return super.equals(obj);
	}
}

