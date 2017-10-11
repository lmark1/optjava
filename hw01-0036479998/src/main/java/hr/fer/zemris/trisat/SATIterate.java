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
 * Generates initial solution randomly. Uses best neighbours for next iterations.
 * Fitness function: numberOfSatisfiedClauses / numberOfTotalClauses.	
 * 
 * @author lmark
 *
 */
public class SATIterate {
	
	public static final int MAX_ITER = 100000;
	public static final Random r = new Random();
	
	private SATFormula formula;
	private SATFormulaStats formulaStats;
	private boolean enableStop = true;
	
	public SATIterate(SATFormula formula) {
		this.formula = formula;
		this.formulaStats = new SATFormulaStats(formula);
	}
	
	/**
	 * @return Attempts to find a solution for the formula.
	 */
	public String getSolution() {
		
		// Generate a random initial solution
		BitVector solution = new BitVector(
				r, 
				formula.getNumberOfVariables());
		
		int t = 0;
		while(t < MAX_ITER) {
			
			// Evaulate root assignment
			formulaStats.setAssignment(solution, true);
			
			// Check if solution is found
			if (formulaStats.isSatisfied()) {
				return "Found solution:\n" + solution.toString();
			}
			
			// Caluclate solution fit
			double rootFit = getFitness(solution);
			
			// Generate neighbours
			List<MutableBitVector> bestNeighbours = getNeighbours(solution);
			double bestNFit = getFitness(bestNeighbours.get(0));
			
			// If best nFit is less then root fit we're stuck
			if (enableStop && bestNFit < rootFit) {
				return "Stuck in local optimum";
			}
			
			// Pick a random solution from bestneighbours
			int min = 0;
			int max = bestNeighbours.size() - 1;
			int randomIndex = r.nextInt((max-min) + 1) + min;
			
			solution = bestNeighbours.get(randomIndex);
//			System.out.println("Best fit: " + bestNFit + 
//					" Root fit: " + rootFit + 
//					" Best neighbour count: " + bestNeighbours.size() + 
//					" \nSol: " + solution + "\n");
			
			t++;
		}
		
		return "Max iterations exceeded, no solution found";
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
		return formulaStats.getFit();
	}
	
	public SATFormulaStats getFormulaStats() {
		return formulaStats;
	}
	
	public void setEnableStop(boolean enableStop) {
		this.enableStop = enableStop;
	}
}
