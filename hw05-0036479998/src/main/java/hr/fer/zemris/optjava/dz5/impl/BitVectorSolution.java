package hr.fer.zemris.optjava.dz5.impl;

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
	
	public int index;
	
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
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (byte b : values) {
			sb.append(b);
		}
		
		return "F: " + fitness;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof BitVectorSolution) {
			if (((BitVectorSolution)obj).index == this.index) {
				return true;
			} else {
				return false;
			}
		}
		return super.equals(obj);
	}
}
