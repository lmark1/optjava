package hr.fer.zemris.optjava.dz3.impl;

import java.util.Random;

import hr.fer.zemris.optjava.dz3.model.INeighbourhood;

/**
 * This class implements the neighbourhood interface. 
 * Generates neighbours with uniform distribution.
 * 
 * @author lmark
 *
 */
public class DoubleArrayUnifNeighbourhood implements INeighbourhood<DoubleArraySolution>{

	private double[] deltas;
	private double flipChance;
	private final Random random = new Random();
	
	public DoubleArrayUnifNeighbourhood(double[] deltas, double flipChance) {
		this.deltas = deltas;
		this.flipChance = flipChance;
	}
	
	@Override
	public DoubleArraySolution randomNeighbour(DoubleArraySolution root) {
		DoubleArraySolution neighbour = root.duplicate();
		
		for(int i = 0, maxLen = neighbour.values.length; i < maxLen; i++) {
			if (random.nextDouble() <= flipChance) {
				neighbour.values[i] += -deltas[i]/2 + random.nextDouble()*deltas[i];
			}
		}
		
		return neighbour;
	}

}
