package hr.fer.zemris.optjava.dz5.part1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz5.impl.BitFunction;
import hr.fer.zemris.optjava.dz5.impl.BitVectorNeighbourhood;
import hr.fer.zemris.optjava.dz5.impl.BitVectorSolution;
import hr.fer.zemris.optjava.dz5.impl.SinglePointCrossover;
import hr.fer.zemris.optjava.dz5.impl.TournamentSelection;
import hr.fer.zemris.optjava.dz5.model.IBitFunction;

/**
 * RAPGA implementation. Solves max - ones problem with a 
 * predefined plateu.
 * 
 * Accepts 1 command line argument: 
 * 
 * 1) n - number of bits.
 * @author lmark
 *
 * Odgovor na pitanja:
 * 
 * 1 ) Nema velike razlike između 2 selekcijska proces, evenutalno povećavanje
 * 		prostora pretraživanja pri nasumičnom odabiru.
 * 
 * 2 ) Veći K pri turninrskoj selekciji ne donosi poboljšanja jer u nekom trenutku
 * 		sva rješenja će imati jednaki fitness (kada zapnu na platou).
 */
public class GeneticAlgorithm {
	
	private static Random random = new Random();
	private static IBitFunction function;
	private static int MAX_ITER = 100;
	private static boolean randomSelection = false; 
	private static int tCount = 2;
	private static SinglePointCrossover xOver = new SinglePointCrossover();
	private static BitVectorNeighbourhood generator = new BitVectorNeighbourhood(0.5);
	private static BitVectorNeighbourhood mutator = new BitVectorNeighbourhood(0.06);
	
	/**
	 * Main function.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Expected 1 command line arguments.");
			return;
		}
		
		int n = Integer.valueOf(args[0]);
		function = new BitFunction();
		
		RAPGA(2, 200, 200, 0, n);
	}
	
	/**
	 * RAPGA implementation.
	 * 
	 * @param minimumPopulation Minimum population count.
	 * @param maximumPopulation Maximum population count.
	 * @param maxSelPres Maximum selection pressure.
	 * @param bitCount Solution bit count.
	 */
	private static void RAPGA(
			int minimumPopulation, 
			int maximumPopulation, 
			double maxSelPres,
			double compFactor,
			int bitCount) {
		
		int initialPopulationCount = 
				random.nextInt(maximumPopulation - minimumPopulation + 1) +
				minimumPopulation;
		
		BitVectorSolution root = new BitVectorSolution(bitCount);
		
		// Generate current population
		List<BitVectorSolution> currPopulationList = new ArrayList<>();
		for(int i = 0; i < initialPopulationCount; i++) {
			currPopulationList.add(generator.randomNeighbour(root));
		}
		evaluatePopulation(currPopulationList);
			
		
		// Initialize helper variables
		double actualSelectionPressure = 1;
		int k = 0;
		int currentParentCount = currPopulationList.size();
		
		while (k < MAX_ITER) {
			
			double currentCompFactor = (1 - compFactor) / MAX_ITER * k + compFactor;
			System.out.println(
					String.format("Iter %d, Comparison factor: %f: ", k, currentCompFactor));
			
			// Print out current population
			System.out.println("Curr pop count - " + currPopulationList.size());
			System.out.println(currPopulationList);
			
			//Generate child population
			List<BitVectorSolution> childPopulationList = new ArrayList<>();
			int currChildCount = 0;
			int badChildCount = 0;
			
			while (	
					currChildCount < maximumPopulation && 
					currChildCount + badChildCount < currentParentCount * maxSelPres) {
				
							
				// Select 2 parents
				TournamentSelection selection = new TournamentSelection(tCount);
				BitVectorSolution parent1 = selection.selectParent(currPopulationList, random);
				
				BitVectorSolution parent2 = null;
				// If random selection is enabled select the other parent randomly 
				if (randomSelection) {
					parent2 = currPopulationList.get(random.nextInt(currentParentCount));
				} else {
					parent2 = selection.selectParent(currPopulationList, random);
				}
				
				// Do crossover
				BitVectorSolution[] children = xOver.cross(parent1, parent2, random);
				BitVectorSolution child1 = mutator.randomNeighbour(children[0]);
				BitVectorSolution child2 = mutator.randomNeighbour(children[1]);
				
				// CHoose the best child
				BitVectorSolution child = chooseChild(parent1, parent2, child1, child2, currentCompFactor);		
				
				// If it's zero continue with the process
				if (child == null) {
					badChildCount++;
					continue;
				}
				
				// Try to add it to child array
				calulateIndex(child);
				if (childPopulationList.contains(child)) {
					badChildCount++;
					continue;
				}
				
				childPopulationList.add(child.duplicate());
				currChildCount++;
				
			} // Child generator loop
			
			if (currChildCount < 2) {
				System.out.println("Unable to genereate any more good singles");
				return;
			}
			
			System.out.println(String.format("Good children: %d, BadChildren: %d", currChildCount, badChildCount));
			actualSelectionPressure = 
					( currChildCount + badChildCount ) / ((double) currentParentCount);
			System.out.println("AcSelPres: " + actualSelectionPressure);
			//currPop = childPop;
			currPopulationList = new ArrayList<>();
			currPopulationList.addAll(childPopulationList);
			currentParentCount = currPopulationList.size();
			
			k++;
		} // Iteration count loop
		
	}

	/**
	 * Choose from 2 children the best one based on their parents fitnesses.
	 * 
	 * @return Null if no child is chosen.
	 */
	private static BitVectorSolution chooseChild(
			BitVectorSolution parent1,
			BitVectorSolution parent2, 
			BitVectorSolution child1,
			BitVectorSolution child2, 
			double compFactor) {
		
		double fitnessTreshold = 
				Math.min(parent1.fitness, parent2.fitness) + 
				Math.abs(parent1.fitness - parent2.fitness) * compFactor;
		
		evaluateSingle(child2);
		evaluateSingle(child1);
		
		if (child1.fitness >= child2.fitness && child1.fitness > fitnessTreshold) {
			return child1;
			
		} else if (child2.fitness > child1.fitness && child2.fitness > fitnessTreshold) {
			return child2;
		
		} else {
			return null;
		}
		
	}

	/**
	 * Evaulates current population.
	 * 
	 * @param currentPopulation
	 */
	private static void evaluatePopulation(
			List<BitVectorSolution> currentPopulation) {
		
		for (int i = 0; i < currentPopulation.size(); i++) {
			currentPopulation.get(i).fitness = function.valueAt(currentPopulation.get(i));
			
			int n = currentPopulation.get(i).values.length - 1;
			int index = 0;
			for (byte b : currentPopulation.get(i).values) {
				index += b * Math.pow(2, n);
				n--;
			}
			
			currentPopulation.get(i).index = index;
		}
	}
	
	/**
	 * Evaluate a single solution
	 * 
	 * @param sol
	 */
	private static void evaluateSingle(BitVectorSolution sol) {
		sol.fitness = function.valueAt(sol);
	}
	
	
	private static void calulateIndex(BitVectorSolution sol) {
		int n = sol.values.length - 1;
		int index = 0;
		for (byte b : sol.values) {
			index += b * Math.pow(2, n);
			n--;
		}
		
		sol.index = index;
	}
}
