package hr.fer.zemris.optjava.dz4.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz4.model.ICrossover;
import hr.fer.zemris.optjava.dz4.model.IMutation;
import hr.fer.zemris.optjava.dz4.model.IOptAlgorithm;
import hr.fer.zemris.optjava.dz4.model.IPopulation;
import hr.fer.zemris.optjava.dz4.model.ISelection;

/**
 * Optimization method - genetic algorithm.
 * Generational - elitistic.
 * 
 * @author lmark
 *
 */
public class GeneticAlgorithm<T> implements IOptAlgorithm<T>{
	
	private static final Random random = new Random();
	
	private int maximumIterations;
	private ISelection<T> selectionMethod;
	private IMutation<T> mutationMethod;
	private ICrossover<T> crossover;
	private IPopulation<T> popHelper;
	private double minimalError;
	
	public GeneticAlgorithm(
			int maximumIterations,
			ISelection<T> selectionMethod,
			IMutation<T> mutationMethod,
			IPopulation<T> population,
			ICrossover<T> crossover,
			double minimalError) {
		
		this.maximumIterations = maximumIterations;
		this.selectionMethod = selectionMethod;
		this.mutationMethod = mutationMethod;
		this.popHelper = population;
		this.crossover = crossover;
		this.minimalError = minimalError;
	}
	
	@Override
	public void run() {
			
		// Generate and evaluate population
		List<T> currentPopulation = popHelper.generateNewPopulation();
		popHelper.evaluatePopulation(currentPopulation);
		
		for (int k = 0; k < maximumIterations; k++) {
			
			// Create new population
			List<T> newPopulation = new ArrayList<>();
			
			// Implement elitism
			List<T> best = popHelper.getNBest(
					currentPopulation, 
					(int) (popHelper.getPopulationSize() * 0.05));
			newPopulation.addAll(best);
			System.out.println(
					String.format("Iter: %d, Best: %s", k, best.get(0)));
			
			if (popHelper.isSatisfied(best.get(0), minimalError)) {
				System.out.println("Minimal error achieved.");
				break;
			}
			
			while(newPopulation.size() < popHelper.getPopulationSize()) {
				
				// Choose 2 parents
				T parent1 = selectionMethod.selectParent(currentPopulation, random);
				T parent2 = selectionMethod.selectParent(currentPopulation, random);
				
				// Crossover
				T[] children = crossover.cross(parent1, parent2, random);
				
				// Mutate children
				mutationMethod.mutate(children[0], random);
				mutationMethod.mutate(children[1], random);
				
				// Add to new population
				newPopulation.add(children[0]);
				newPopulation.add(children[1]);
			}
			
			// Update current population
			currentPopulation = newPopulation;
			popHelper.evaluatePopulation(currentPopulation);
		} // End iterations 
	}

}
