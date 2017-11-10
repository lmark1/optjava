package hr.fer.zemris.optjava.dz4.impl;

import java.util.Random;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz4.model.ICrossover;

public class BLXCrossover implements ICrossover<DoubleArraySolution>{

	private double alpha;
	
	public BLXCrossover(double alpha) {
		this.alpha = alpha;
	}
	
	@Override
	public DoubleArraySolution[] cross(DoubleArraySolution parent1,
			DoubleArraySolution parent2, Random random) {
		
		Matrix p1 = new Matrix(new double[][] {parent1.values});
		Matrix p2 = new Matrix(new double[][] {parent2.values});
		
		Matrix cMin = new Matrix(1, parent1.values.length);
		Matrix cMax = new Matrix(1, parent1.values.length);
		
		// Calculate minimum and maximum vectors
		for (int i = 0, maxLen = parent1.values.length; i < maxLen; i++) {
			cMin.set(0, i, Math.min(p1.get(0, i), p2.get(0, i)));
			cMax.set(0, i, Math.max(p1.get(0, i), p2.get(0, i)));
		}
		Matrix I = cMax.minus(cMin);
		Matrix lower = cMin.minus(I.times(alpha));
		Matrix upper = cMax.plus(I.times(alpha));
		
		// Assign child values
		DoubleArraySolution child1 = new DoubleArraySolution(parent1.values.length);
		child1.values = lower.plus( 
				upper.minus(lower).times(random.nextDouble()))
				.getArray()[0];

		DoubleArraySolution child2 = new DoubleArraySolution(parent2.values.length);
		child2.values = lower.plus( 
				upper.minus(lower).times(random.nextDouble()))
				.getArray()[0];
		
		return new DoubleArraySolution[] {child1, child2};
	}

}
