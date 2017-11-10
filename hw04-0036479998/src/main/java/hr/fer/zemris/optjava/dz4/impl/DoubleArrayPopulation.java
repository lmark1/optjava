package hr.fer.zemris.optjava.dz4.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz4.model.IFunction;
import hr.fer.zemris.optjava.dz4.model.IPopulation;

/**
 * Double array population implementation. Population will be evaulted
 * according to the given function.
 * 
 * @author lmark
 *
 */
public class DoubleArrayPopulation implements IPopulation<DoubleArraySolution> {
	
	private int populationSize;
	private int arraySize;
	private IFunction function;
	
	private final int MAX = 5;
	private final int MIN = -5;
	private final Random random = new Random();
	
	public DoubleArrayPopulation(IFunction function, int populationSize, int arraySize) {
		this.populationSize = populationSize;
		this.arraySize = arraySize;
		this.function = function;
	}
	
	@Override
	public List<DoubleArraySolution> generateNewPopulation() {
		List<DoubleArraySolution> pop = new ArrayList<>();
		
		for (int i = 0; i < populationSize; i++) {
			double[] values = new double[arraySize];
			
			for (int j = 0; j < arraySize; j++) {
				values[j] = MIN + random.nextDouble() * (MAX - MIN);
			}
			
			DoubleArraySolution sol = new DoubleArraySolution(arraySize);
			sol.values = values;
			pop.add(sol);
		}
		
		return pop;
	}

	@Override
	public void evaluatePopulation(List<DoubleArraySolution> population) {
		
		for (DoubleArraySolution doubleArraySolution : population) {
			doubleArraySolution.fitness = - function.valueAt(doubleArraySolution.values);
		}		
	}
	
	@Override
	public int getPopulationSize() {
		return populationSize;
	}

	@Override
	public List<DoubleArraySolution> getNBest(
			List<DoubleArraySolution> population, int n) {
		
		Collections.sort(population, new Comparator<DoubleArraySolution>() {

			@Override
			public int compare(DoubleArraySolution o1, DoubleArraySolution o2) {
				return o1.compareTo(o2);
			}
		});
		
		List<DoubleArraySolution> best = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			best.add(population.get(i));
		}
		
		return best;
	}

	@Override
	public boolean isSatisfied(DoubleArraySolution bestSingle, double epsilon) {
		return Math.abs(bestSingle.fitness) <= epsilon;
	}
	
	

}
