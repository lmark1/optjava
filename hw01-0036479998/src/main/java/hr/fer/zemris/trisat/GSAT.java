package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.trisat.model.BitVector;
import hr.fer.zemris.trisat.model.MutableBitVector;
import hr.fer.zemris.trisat.model.SATFormula;
import hr.fer.zemris.trisat.model.SATFormulaStats;

/**
 * GSAT algorithm. Multistart search algorithm.
 * Each iteration flips the solution in order to minimize the number 
 * of unsatisfied clauses.
 * In case it gets stuck it will retry starting from a randomly generated solution.
 * 
 * @author lmark
 *
 */
public class GSAT {

	private static final int MAX_TRIES = 200;
	private static final int MAX_FLIPS = 1000;
	public static final Random r = new Random();
	
	private SATFormula formula;
	private SATFormulaStats formulaStats;
	
	public GSAT(SATFormula formula) {
		this.formula = formula;
		this.formulaStats = new SATFormulaStats(formula);
	}
	
	/**
	 * @return Attempt to return the solution.
	 */
	public String getSolution() {
		
		// Start algorithm again multiple times
		for(int restart = 0; restart < MAX_TRIES; restart++) {
			System.out.println("Try: " + restart);
			
			// Generate a random initial solution
			MutableBitVector solution = new MutableBitVector(r, formula.getNumberOfVariables());
			
			// Change on the solution
			for (int change = 0; change < MAX_FLIPS; change++) {
				
				if (isFormulaSatisfied(solution)) {
					return "Solution found:\n" + solution.toString(); 
				}
				
				// Flip the solution
				flipSolution(solution);
			}
		}
		
		return "Unable to find solution";
	}
	
	/**
	 * Flips the bit of the solution that results in least number of unsatisfied clauses.
	 * 
	 * @param solution
	 */
	public void flipSolution(MutableBitVector solution) {
		int bestIndex = getBestLiteralIndex(solution);
		solution.flip(bestIndex);
	}
	
	/**
	 * @param solution
	 * @return Check if solution satisfies the formula.
	 */
	public boolean isFormulaSatisfied(BitVector solution) {
		formulaStats.setAssignment(solution, false);
		return formulaStats.isSatisfied();
	}
	
	/**
	 * @param solution
	 * @return Returns number of satisfied clauses.
	 */
	public double getSatisfiedClauses(BitVector solution) {
		formulaStats.setAssignment(solution, false);
		return formulaStats.getNumberOfSatisfied();
	}
	
	/**
	 * @param solution
	 * @return Returns a number of unsatisfied clauses.
	 */
	public double getUnsatisfiedClauses(BitVector solution) {
		formulaStats.setAssignment(solution, false);
		return formulaStats.getNumberOfUnsatisfied();
	}
	
	/**
	 * @param solution
	 * @return Returns index of the literal from the solution which results in 
	 * least amount of unsatisfied clauses. If there are more than one best 
	 * indexes return a random one.
	 */
	public int getBestLiteralIndex(BitVector solution) {
		MutableBitVector cSol = solution.copy();
		
		List<Integer> bestIndexes = new ArrayList<>();
		double minimumCount = formula.getNumberOfClauses();
		for (int i = 0; i < cSol.getSize(); i++) {
			
			cSol.flip(i);
			double unsatisfiedCount = getUnsatisfiedClauses(cSol);
			if (unsatisfiedCount < minimumCount) {
				minimumCount = unsatisfiedCount;
				bestIndexes.clear();
				bestIndexes.add(i);
			} else if (unsatisfiedCount == minimumCount) {
				bestIndexes.add(i);
			}
			cSol.flip(i);
		}
		
		int min = 0;
		int max = bestIndexes.size() -1;
		int randomIndex = r.nextInt((max - min) + 1) + min;
		
		return bestIndexes.get(randomIndex);
	}
	
	public SATFormulaStats getFormulaStats() {
		return formulaStats;
	}
	
	public SATFormula getFormula() {
		return formula;
	}
}
