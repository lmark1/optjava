package hr.fer.zemris.optjava.dz4.impl;

import java.util.Arrays;
import java.util.Random;

/**
 * Solution represented as an array of doubles.
 * 
 * @author lmark
 *
 */
public class DoubleArraySolution extends SingleObjectiveSolution{
	
	/**
	 * Array of double values.
	 */
	public double[] values;
	
	/**
	 * Initializes double array.
	 * 
	 * @param n
	 */
	public DoubleArraySolution(int n) {
		values = new double[n];
	}
	
	/**
	 * @return Returns a new solution like this one.
	 */
	public DoubleArraySolution newLikeThis() {
		return new DoubleArraySolution(values.length);
	}
	
	/**
	 * @return Returns duplicate solution.
	 */
	public DoubleArraySolution duplicate() {
		double[] copyValues = new double[values.length];
		
		/**
		 * Copy all the current values.
		 */
		for(int i = 0, maxLen = values.length; i < maxLen; i++) {
			copyValues[i] = values[i];
		}
		
		DoubleArraySolution copySol = new DoubleArraySolution(values.length);
		copySol.values = copyValues;
		copySol.fitness = fitness;
		copySol.value = value;
		
		return copySol;
	}
	
	public DoubleArraySolution randomize(Random random, double[] deltas) {
		for(int i = 0, maxLen = values.length; i < maxLen; i++) {
			values[i] += -deltas[i]/2 + random.nextDouble()*deltas[i];
		}
		
		return this;
	}
	
	@Override
	public String toString() {
		return "Fitness: " + fitness + " Values: " + Arrays.toString(values);
	}
}
