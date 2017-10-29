package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.trisat.model.BitVector;
import hr.fer.zemris.trisat.model.BitVectorNGenerator;
import hr.fer.zemris.trisat.model.MutableBitVector;
import hr.fer.zemris.trisat.model.SATFormula;
import hr.fer.zemris.trisat.model.SATFormulaStats;

/**
 * Iterative greedy search algorithm. 
 * Implements basic iterative search algorithm.
 * Generates initial solution randomly. Uses best neighbours for next iterations.
 * Fitness function: numberOfSatisfiedClauses / numberOfTotalClauses.	
 * 
 * @author lmark
 *
 */
public class SATIterate {
	
	public static final int MAX_ITER = 100000;
	public static int MAX_STUCK_COUNT = 100;
	
	public static final Random r = new Random();
	
	private SATFormula formula;
	private SATFormulaStats formulaStats;
	
	public SATIterate(SATFormula formula) {
		this.formula = formula;
		this.formulaStats = new SATFormulaStats(formula);
	}
	
	/**
	 * @return Attempts to find a solution for the formula.
	 */
	public String getSolution() {
		
		// Generate a random initial solution
		BitVector initialSolution = new BitVector(
				r, 
				formula.getNumberOfVariables());
		
		BitVector solution = runLocalSearch(initialSolution);
				
		formulaStats.setAssignment(solution, false);
		if (formulaStats.isSatisfied()) {
			return "Solution found:\n" + solution;
		} else {
			return "Incomplete solution found:\n" + solution;
		}
	}
	
	/**
	 * Runs local search around the given solution.
	 * 
	 * @param solution
	 * @return Returns solution if one is found or stuck in local optimum.
	 */
	public BitVector runLocalSearch(BitVector solution) {
		int t = 0;
		int stuckCount = 0;
		while(t < MAX_ITER) {
			
			// Evaulate root assignment
			formulaStats.setAssignment(solution, true);
			
			// Check if solution is found
			if (formulaStats.isSatisfied()) {
				return solution;
			}
			
			// Caluclate solution fit
			double rootFit = getFitness(solution);
			
			// Generate neighbours
			List<MutableBitVector> bestNeighbours = getNeighbours(solution);
			
			// Check if algorithm is stuck
			double bestNFit = getFitness(bestNeighbours.get(0));
			if (isStuck(bestNFit, rootFit, stuckCount)) {
				System.out.println("Stuck in local optimum");
				return solution;
			} else if (Math.abs(bestNFit - rootFit) < 10e-10) {
				stuckCount++;
			}
			
			// Pick a random solution from bestneighbours
			int min = 0;
			int max = bestNeighbours.size() - 1;
			int randomIndex = r.nextInt((max-min) + 1) + min;
			
			solution = bestNeighbours.get(randomIndex);
//			System.out.println(String.format("Fit: %2f, Unsatisfied: %d",
//					getFitness(solution), getUnsatisfied(solution)));
			
			t++;
		}
		
		System.out.println("Max iterations passed");
		return solution;
	}
	
	/**
	 * @param bestNFit Best neighbour fit.
	 * @param rootFit Best root parent fit.
	 * @param stuckCount Number of iterations algorithm is on the same fitness.
	 * @return True if algorithm is stuck and should exit, false otherwise.
	 */
	public boolean isStuck(double bestNFit, double rootFit, int stuckCount) {
		return (bestNFit < rootFit) || (stuckCount > MAX_STUCK_COUNT);
	}

	/**
	 * @param root
	 * @return Returns a neighbourhood around the root solution.
	 */
	public List<MutableBitVector> getNeighbours(BitVector root) {
		// Generate a neighbourhood of solutions
		BitVectorNGenerator gen = new BitVectorNGenerator(root);
		
		// Find best neighbours
		List<MutableBitVector> bestNeighbours = new ArrayList<>();
		double bestNFit = -1;
		for (MutableBitVector mutableBitVector : gen) {
			double nFit = getFitness(mutableBitVector);
			
			if (nFit > bestNFit) {
				bestNeighbours.clear();
				bestNeighbours.add(mutableBitVector);
				bestNFit = nFit;
			
			} else if (Math.abs(nFit - bestNFit) < 10e-10) {
				bestNeighbours.add(mutableBitVector);
			}
		}
		
		return bestNeighbours;
	}
	
	/**
	 * @param solution
	 * @return Returns fitness of the given solution.
	 */
	public double getFitness(BitVector solution) {
		formulaStats.setAssignment(solution, false);
		return formulaStats.getFitness();
	}
	
	public int getUnsatisfied(BitVector solution) {
		formulaStats.setAssignment(solution, false);
		return formulaStats.getNumberOfUnsatisfied();
	}
	
	public SATFormulaStats getFormulaStats() {
		return formulaStats;
	}
	
	public SATFormula getFormula() {
		return formula;
	}
}
