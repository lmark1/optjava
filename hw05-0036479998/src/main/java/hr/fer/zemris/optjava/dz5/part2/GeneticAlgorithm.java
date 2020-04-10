package hr.fer.zemris.optjava.dz5.part2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz5.impl.ULCrossover;
import hr.fer.zemris.optjava.dz5.impl.SwitchMutation;
import hr.fer.zemris.optjava.dz5.impl.TSelection;
import hr.fer.zemris.optjava.dz5.impl.IntArraySolution;
import hr.fer.zemris.optjava.dz5.impl.QAPFunction;
import hr.fer.zemris.optjava.dz5.model.IFunction;

/**
 * SASEGASA implementataion.
 * 
 * Accepts 3 command line arguments:
 * 1) Path to the configuration file
 * 2) Total population size
 * 3) Intial number of population chunks
 * 
 * @author lmark
 *
 */
public class GeneticAlgorithm {
	
	private static IFunction function;
	private static int MAX_ITER = 100;
	private static Random random = new Random();
	private static ULCrossover cross = new ULCrossover();
	private static IntArraySolution best = null;
	
	public static void main(String[] args) {
		
		if (args.length != 3) {
			System.out.println("Accepts 3 command line arguments.");
			return;
		}
		
		Path filePath = Paths.get(args[0]);
		int popSize = Integer.valueOf(args[1]);
		int N = Integer.valueOf(args[2]);
		
		List<Matrix> mats = parseFile(filePath);
		
		// n - number of factories
		Matrix d = mats.get(0); // nxn matrix, (i,j) -> distance from i to j location
		Matrix c = mats.get(1); // nxn matrix, (i,j) -> amount of resources sent from i to j factory
		function = new QAPFunction(c, d);
		
		List<IntArraySolution> currentSolution = generateCurrentSolution(d.getRowDimension(), popSize);
		evaluatePopulation(currentSolution);
		
		SASEGASA(currentSolution, N);
	}
	
	/**
	 * Initialize SASEGASA algorithm.
	 * 
	 * @param currentSolution
	 */
	private static void SASEGASA(
			List<IntArraySolution> currentSolution,
			int N) {
				
		while(N > 0) {
			List<IntArraySolution> childPopulation = new ArrayList<>();
			int index = 0;
			
			int chunkSize = (int) Math.ceil(currentSolution.size() / ((double)N));
			for (int i = 0; i < N; i++) {
				
				List<IntArraySolution> subPopulation = new ArrayList<>();
				while (subPopulation.size() < chunkSize && index < currentSolution.size()) {
					subPopulation.add(currentSolution.get(index).duplicate());
					index++;
				}
				
				childPopulation.addAll(
						offSpringSelection(
								subPopulation, 
								cross, 
								new SwitchMutation(0.05, function), 
								new TSelection(subPopulation.size()/10 == 0 ? 3 : subPopulation.size() / 10), 
								150, 
								0.2, 
								0.5));
				
			} // Evaluate all chunks of population
			
			currentSolution = new ArrayList<>();
			currentSolution.addAll(childPopulation);
			
			
			//Find best
			double max = 0;
			int index1 = 0;
			for(int i = 0; i < currentSolution.size(); i++) {
				if (currentSolution.get(i).fitness > max) {
					index1 = i;
					max = currentSolution.get(i).fitness;
				}
			}
			
			System.out.print("Current best: " + currentSolution.get(index1));
			if (best == null || best.fitness <= currentSolution.get(index1).fitness) {
				best = currentSolution.get(index1);
			}
			System.out.print("All time best: " + best + "\n");
						
			
			N --;
		} // Main loop
	}


	/**
	 * Start Offspring selection.
	 * 
	 * @param currentPop Current population.
	 * @param xOver Crossover operator.
	 * @param mutate Mutation operator.
	 * @param maxSelPres Maximum selection pressure.
	 * @param compFactor Comparison factor - used for accepting children [0,1]
	 * @param succRation Ration of the population containing better children, [0,1]
	 * 
	 * @return Returns evolved population.
	 */
	private static List<IntArraySolution> offSpringSelection(
			List<IntArraySolution> currentPop,
			ULCrossover xOver,
			SwitchMutation mutate,
			TSelection sel,
			int maxSelPres,
			double compFactor, 
			double succRation) {
		
		int k = 0;
		double actualSelectionPressure = 1;
		
		while (k < MAX_ITER && actualSelectionPressure < maxSelPres) {
			List<IntArraySolution> childPopulation = new ArrayList<>();
			List<IntArraySolution> pool = new ArrayList<>();
			double currentCompFactor = (1 - compFactor) / MAX_ITER * k + compFactor;
			int badChildCount = 0;
			
			while (
					(childPopulation.size() < currentPop.size() * succRation || pool.isEmpty() ) &&
					(childPopulation.size() + badChildCount) < (currentPop.size() * maxSelPres)) {
				
				// Select parents
				IntArraySolution parent1 = sel.selectParent(currentPop, random);
				IntArraySolution parent2 = sel.selectParent(currentPop, random);
				
				// Select children
				IntArraySolution[] children = xOver.cross(parent1, parent2, random);
				mutate.mutate(children[0], random);
				
				IntArraySolution child = chooseChild(parent1, parent2, 
						children[0], currentCompFactor);
				
				// If children are worse
				if (child == null) {					
					badChildCount++;
					pool.add(children[0]);
					continue;
				}
				
				calculateID(child);
				if (childPopulation.contains(child)) {
					badChildCount++;
					continue;
				}
							
				childPopulation.add(child);
			}
			
			//System.out.println(String.format("Good children: %d, BadChildren: %d", childPopulation.size(), badChildCount));
			actualSelectionPressure = 
					( childPopulation.size() + badChildCount ) / ((double) currentPop.size());
			//System.out.println("AcSelPres: " + actualSelectionPressure);
			
			// Add from pool if there is not enough children
			while (childPopulation.size() < currentPop.size()) {
				childPopulation.add(pool.get(random.nextInt(pool.size())));
			}
			
			currentPop = new ArrayList<>();
			currentPop.addAll(childPopulation);
			
			k++;
		} // Main loop
		
		return currentPop;
	}

	private static void calculateID(IntArraySolution child) {
		
		StringBuilder strBigNum = new StringBuilder();
		for (int n : child.values)
		    strBigNum.append(n);
		
		long bigNum = 0;
		long factor = 1;
		for (int k = strBigNum.length()-1; k >= 0; k--) {
		    bigNum += Character.digit(strBigNum.charAt(k), 10) * factor;
		    factor *= 10;
		}
		
		child.ID = bigNum;		
	}

	/**
	 * Choose from 2 children the best one based on their parents fitnesses.
	 * 
	 * @return Null if no child is chosen.
	 */
	private static IntArraySolution chooseChild(
			IntArraySolution parent1,
			IntArraySolution parent2, 
			IntArraySolution child,
			double compFactor) {
		
		double fitnessTreshold = 
				Math.min(parent1.fitness, parent2.fitness) + 
				Math.abs(parent1.fitness - parent2.fitness) * compFactor;
		
		evaluateSingle(child);
		
		if (child.fitness > fitnessTreshold) {
			return child;
			
		} else {
			return null;
		}
		
	}
	
	/**
	 * Evaulate given population.
	 * 
	 * @param currentSolution
	 */
	private static void evaluatePopulation(
			List<IntArraySolution> currentSolution) {
		
		for(int i = 0; i < currentSolution.size(); i++) {
			currentSolution.get(i).fitness = 
					- function.valueAt(currentSolution.get(i));
			

			StringBuilder strBigNum = new StringBuilder();
			for (int n : currentSolution.get(i).values)
			    strBigNum.append(n);
			
			long bigNum = 0;
			long factor = 1;
			for (int k = strBigNum.length()-1; k >= 0; k--) {
			    bigNum += Character.digit(strBigNum.charAt(k), 10) * factor;
			    factor *= 10;
			}
			
			currentSolution.get(i).ID = bigNum;
		}
	}


	/**
	 * Evaluate a single solution
	 * 
	 * @param sol
	 */
	private static void evaluateSingle(IntArraySolution sol) {
		sol.fitness = -function.valueAt(sol);
	}
	
	/**
	 * Generate initial solution population.
	 * 
	 * @param factoryCount
	 * @return
	 */
	private static List<IntArraySolution> generateCurrentSolution(
			int factoryCount,
			int popSize) {
		
		List<IntArraySolution> sList = new ArrayList<>();
		
		for (int i = 0; i < popSize; i++) {
			
			// Add all elements to permutation
			List<Integer> permutation = new ArrayList<>();
			for (int k = 0; k < factoryCount; k++) {
				permutation.add(k);
			}
			Collections.shuffle(permutation);
			
			// Convert to array
			int[] values = new int[factoryCount];
			for (int k = 0; k < values.length; k++) {
				values[k] = permutation.get(k);
			}
			
			IntArraySolution sol = new IntArraySolution(factoryCount);
			sol.values = values;
			sList.add(sol);
		}
		
		return sList;
	}


	private static List<Matrix> parseFile(Path path) {
		
		List<String> lines = null;
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		int n = Integer.valueOf(lines.get(0).trim());
		
		// Get first matrix
		Matrix A = new Matrix(n, n);
		for (int i = 2; i < 2 + n; i++) {
			String[] split = lines.get(i).trim().split("\\s++");
			
			for (int j = 0; j < split.length; j++) {
				A.set(i-2, j, Integer.valueOf(split[j]));
			}
		}
		
		// Get second matrix
		Matrix B = new Matrix(n, n);
		for (int i = 3+n; i < 3 + 2*n; i++) {
			String[] split = lines.get(i).trim().split("\\s++");
			
			for (int j = 0; j < split.length; j++) {
				B.set(i - 3 - n, j, Integer.valueOf(split[j]));
			}
		}
		
		List<Matrix> mat = new ArrayList<>();
		mat.add(A);
		mat.add(B);
		
		return mat;
	}
}
