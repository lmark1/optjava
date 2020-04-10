package hr.fer.zemris.optjava.dz5.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz5.model.ICrossover;

/**
 * Uniform like crossover.
 * 
 * @author lmark
 *
 */
public class ULCrossover implements ICrossover<IntArraySolution>{

	@Override
	public IntArraySolution[] cross(IntArraySolution parent1,
			IntArraySolution parent2, Random random) {
		
		int[] childValues = new int[parent1.values.length];
		for (int j = 0; j < childValues.length; j++) {
			childValues[j] = -1;
		}
		
		List<Integer> digitList = new ArrayList<>();
		
		// Find duplicates and copy them to child
		for (int i = 0; i < childValues.length; i++) {
			
			// If both parent values are the same 
			if (parent1.values[i] == parent2.values[i]) {
				childValues[i] = parent1.values[i];
				digitList.add(parent1.values[i]);
				continue;
			}
			
		}
		
		// Fill up remaining with uniform distribution only if digits are not already added
		for (int i = 0; i < childValues.length; i++) {
			
			if (
					parent1.values[i] != parent2.values[i] && 
					!digitList.contains(parent1.values[i]) &&
					!digitList.contains(parent2.values[i])) {
				
				int newVal = random.nextDouble() <= 0.5 ? parent1.values[i] : parent2.values[i];
				digitList.add(newVal);
				childValues[i] = newVal;
			}
		}
		
		// Find remaining numbers
		List<Integer> remainingNumbers = new ArrayList<>();
		for (int i = 0; i < childValues.length; i++) {
			if (!digitList.contains(i)) {
				remainingNumbers.add(i);
			}
		}
		Collections.shuffle(remainingNumbers);
		
		// Fill the rest randomly
		for (int i = 0; i < childValues.length; i++) {
			
			if (childValues[i] == -1) {
				childValues[i] = remainingNumbers.remove(0);
			}
			
		}
		
		IntArraySolution sol = new IntArraySolution(childValues.length);
		sol.values = childValues;
		
		IntArraySolution[] sols = new IntArraySolution[1];
		sols[0] = sol;
		
		return sols;
	}

}
