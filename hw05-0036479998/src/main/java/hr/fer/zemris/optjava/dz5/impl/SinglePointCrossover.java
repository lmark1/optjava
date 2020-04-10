package hr.fer.zemris.optjava.dz5.impl;

import java.util.Random;

import hr.fer.zemris.optjava.dz5.model.ICrossover;

public class SinglePointCrossover implements ICrossover<BitVectorSolution>{

	@Override
	public BitVectorSolution[] cross(BitVectorSolution parent1,
			BitVectorSolution parent2, Random random) {
		
		int breakPoint = random.nextInt(parent1.values.length);
		BitVectorSolution cross1 = new BitVectorSolution(parent1.values.length);
		BitVectorSolution cross2 = new BitVectorSolution(parent1.values.length);
		
		for (int i = 0; i < parent1.values.length; i++) {
			
			if (i <= breakPoint) {
				cross1.values[i] = parent2.values[i];
				cross2.values[i] = parent1.values[i];
			} else {
				cross1.values[i] = parent1.values[i];
				cross2.values[i] = parent2.values[i];
			}
		}
		
		return new BitVectorSolution[] { cross1, cross2 };
	}

}
