package hr.fer.zemris.optjava.dz4.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz4.model.ISelection;

public class RouletteWheelSelection implements ISelection<DoubleArraySolution>{

	@Override
	public DoubleArraySolution selectParent(
			List<DoubleArraySolution> population, Random random) {
		
		// Find minimum fitness
		Double minFitness = null;
		for (DoubleArraySolution doubleArraySolution : population) {
			
			if (minFitness == null) {
				minFitness = doubleArraySolution.fitness;
				continue;
			}
			
			if (doubleArraySolution.fitness > minFitness) {
				continue;
			}
			
			minFitness = doubleArraySolution.fitness;
		}
		
		// Assign new fitnesses
		List<Double> newFitness = new ArrayList<>();
		double fitnesSum = 0;
		for (DoubleArraySolution doubleArraySolution : population) {
			double newval = (doubleArraySolution.fitness - minFitness);
			newFitness.add(newval);
			fitnesSum += newval;
		}		
		
		// Regularize all fitnesses 
		double randValue = random.nextDouble();
		double sum = 0;
		int randomIndex = -1;
		for (int i = 0; i < newFitness.size(); i++) {
			newFitness.set(i, newFitness.get(i)/fitnesSum);
			
			sum += newFitness.get(i);
			if (randValue < sum) {
				randomIndex = i;
				break;
			}
		}
		
		
		
		return population.get(randomIndex);
	}

}
