package hr.fer.zemris.optjava.dz8;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class implements a differential evolution algorithm.
 * 
 * @author lmark
 *
 */
public class DIfferentialEvolution {

	private static Random random = new Random();
	
	private IEvaulator evaluator;
	private int maxIter;
	private int populationSize;
	private double acceptableError;
	private int solutionSize;
	private double F;
	private double Cr;
	
	private double bestFit = -10e10;
	private Solution bestSol = null;
	
	public DIfferentialEvolution(
			IEvaulator evaluator,
			int maxIter,
			int populationSize,
			double acceptableError,
			double F,
			double Cr) {
		
		this.evaluator = evaluator;
		this.maxIter = maxIter;
		this.populationSize = populationSize;
		this.acceptableError = acceptableError;
		this.solutionSize = evaluator.getParamCount();
		this.F = F;
		this.Cr = Cr;
	}
	
	/**
	 * @return Return best solution.
	 */
	public double[] run() {
		
		// Generate initial population
		Solution[] population = new Solution[populationSize];
		for (int i = 0; i < populationSize; i++) {
			population[i] = initializeRandomMember();
		}
		
		int k = 0;
		while (k < maxIter) {
			
			Solution[] newPopulation = new Solution[populationSize];
			for (int i = 0; i < populationSize; i++) {
				
				Solution[] randomSolutions = getNRandomSolutions(population, 3);
				Solution mutantVector = calculateMutant(randomSolutions);
				Solution xOver = calculateCrossover(population[i], mutantVector);
				
				if (xOver.fitness > population[i].fitness) {
					newPopulation[i] = xOver;
				} else {
					newPopulation[i] = population[i].duplicate();
				}
				
				// Check for best solution
				if (newPopulation[i].fitness > bestFit) {
					bestFit = newPopulation[i].fitness;
					bestSol = newPopulation[i].duplicate();
					
					if (- bestFit < acceptableError) {
						System.out.println("Acceptable error achieved.");
						return bestSol.weights;
					}
				}
			}
			
			population = new Solution[populationSize];
			for (int i = 0; i < populationSize; i++) {
				population[i] = newPopulation[i].duplicate();
			}
			
			System.out.println(bestSol + " Iter: " + k);
			k++;
		}
		
		return bestSol.weights;
	}
	
	/**
	 * @param solution
	 * @param mutantVector
	 * @return Calculate crossover.
	 */
	private Solution calculateCrossover(Solution solution,
			Solution mutantVector) {
		
		double[] xOver = new double[solutionSize];
		boolean changed = false;
		
		// Perform crossover
		for (int i = 0; i < solutionSize; i++) {
			
			if (random.nextDouble() <= Cr) {
				changed = true;
				xOver[i] = mutantVector.weights[i];
			
			} else {
				xOver[i] = solution.weights[i];
			}
		}
		
		// If no change happened perform atleast 1 change
		if (!changed) {
			int rIndex = random.nextInt(solutionSize);
			xOver[rIndex] = mutantVector.weights[rIndex];
		}
		
		Solution xOverM = new Solution();
		xOverM.weights = xOver;
		xOverM.fitness = - evaluator.calculateError(xOver);
		
		return xOverM;
	}

	/**
	 * @param randomSolutions
	 * @return Return mutant vector x0 + F * (x1 - x2)
	 */
	private Solution calculateMutant(Solution[] randomSolutions) {
		
		double[] mutant = new double[solutionSize];
		for (int i = 0; i < solutionSize; i++) {
			mutant[i] = randomSolutions[0].weights[i] + 
					F * (randomSolutions[1].weights[i] 
							- randomSolutions[2].weights[i]);
		}
		
		Solution mSol = new Solution();
		mSol.weights = mutant;
		mSol.fitness = - evaluator.calculateError(mutant);
		
		return mSol;
	}

	/**
	 * @param population
	 * @return Return n random solutions from the population.
	 */
	private Solution[] getNRandomSolutions(Solution[] population, int n) {
		Solution[] rSol = new Solution[n];
		
		List<Solution> cPop = new ArrayList<>();
		for(int i = 0; i < populationSize; i++) {
			cPop.add(population[i].duplicate());
		}
		
		for (int i = 0; i < n; i++) {
			int rIndex = random.nextInt(cPop.size());
			rSol[i] = cPop.get(rIndex);
			cPop.remove(rIndex);
		}	
		
		return rSol;
	}

	/**
	 * @return Return a random population memeber.
	 */
	private Solution initializeRandomMember() {
		Solution newSol = new Solution();
		newSol.weights = new double[solutionSize];
		
		for (int i = 0; i < solutionSize; i++) {
			newSol.weights[i] = 2 * random.nextDouble() - 1;
		}
		newSol.fitness = - evaluator.calculateError(newSol.weights);
		
		return newSol;
	}

	private static class Solution {
		double[] weights;
		double fitness;
		
		public Solution duplicate() {
			Solution dup = new Solution();
			dup.fitness = fitness;
			dup.weights = weights;
			return dup;
		}
		
		@Override
		public String toString() {
			return "Fit = " + fitness;
		}
	}
}
