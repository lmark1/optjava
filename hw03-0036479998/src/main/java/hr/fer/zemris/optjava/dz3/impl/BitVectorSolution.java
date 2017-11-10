package hr.fer.zemris.optjava.dz3.impl;

import java.util.Random;

/**
 * Solution represented by an array of bytes.
 * 
 * @author lmark
 *
 */
public class BitVectorSolution extends SingleObjectiveSolution {

	/**
	 * Array of byte values.
	 */
	public byte[] values;
	
	/**
	 * Initializes byte array.
	 * 
	 * @param n
	 */
	public BitVectorSolution(int n) {
		values = new byte[n];
	}
	
	/**
	 * @return Returns a new solution like this.
	 */
	public BitVectorSolution newLikeThis() {
		return new BitVectorSolution(values.length);
	}
	
	/**
	 * @return Returns a duplicate solution.
	 */
	public BitVectorSolution duplicate() {
		byte[] copyValues = new byte[values.length];
		
		/**
		 * Copy all the current values.
		 */
		for(int i = 0, maxLen = values.length; i < maxLen; i++) {
			copyValues[i] = values[i];
		}
		
		BitVectorSolution copySol = new BitVectorSolution(values.length);
		copySol.values = copyValues;
		copySol.fitness = fitness;
		copySol.value = value;
		
		return copySol;
	}
	
	public BitVectorSolution randomize(Random random, double flipChance) {
		for (int i = 0, maxLen = values.length; i < maxLen; i++) {
			if (random.nextDouble() <= flipChance) {
				byte val = values[i];
				values[i] = (byte) (1 - val);
			}
		}
		
		return this;
	}
}
