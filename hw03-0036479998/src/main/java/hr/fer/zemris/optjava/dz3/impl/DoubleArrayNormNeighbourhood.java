package hr.fer.zemris.optjava.dz3.impl;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.model.INeighbourhood;

/**
 * This class implements a random neighbour generator 
 * using the normal distribution.
 * 
 * @author lmark
 *
 */
public class DoubleArrayNormNeighbourhood implements INeighbourhood<DoubleArraySolution>{
	
	private double[] deltas;
	private final Random random = new Random();
	
	public DoubleArrayNormNeighbourhood(double[] deltas) {
		this.deltas = deltas;
	}
	
	@Override
	public DoubleArraySolution randomNeighbour(DoubleArraySolution root) {
		DoubleArraySolution neighbour = root.duplicate();
		
		for (int i = 0, maxLen = neighbour.values.length; i < maxLen; i++) {
			neighbour.values[i] += random.nextGaussian()*deltas[i];
		}
		
		return neighbour;
	}

}
