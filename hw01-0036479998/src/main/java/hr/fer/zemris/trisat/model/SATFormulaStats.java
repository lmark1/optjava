package hr.fer.zemris.trisat.model;

import java.util.Random;

/**
 * This class analyzes statistics from SATFormula objects.
 * 
 * @author lmark
 *
 */
public class SATFormulaStats {
	
	private static final double percentageConstantUp = 0.01;
	private static final double percentageConstantDown = 0.1;
	
	private static final double percentageUnitAmount = 50;
	
	/**
	 * Formula being analyzed.
	 */
	private SATFormula formula;
	
	/**
	 * Bit vector formula assignment.
	 */
	private BitVector assignment;
	
	/**
	 * Percentages of success for each clause.
	 */
	private double[] post;
		
	/**
	 * Number of satisfied formula clauses.
	 */
	private int numberOfSatisfied;
	
	public SATFormulaStats(SATFormula formula) {
		this.formula = formula;
		this.post = new double[formula.getNumberOfClauses()];
	}
	
	/**
	 * Sets the assignment for the formula.
	 * 
	 * @param assignment
	 * @param updatePercentages
	 */
	public void setAssignment(BitVector assignment, boolean updatePercentages) {
		numberOfSatisfied = 0;
		this.assignment = assignment;
		
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			
			boolean satisfied = formula.getClause(i).isSatisfied(assignment);
			if (satisfied) {
				numberOfSatisfied++;
			}
			
			if (updatePercentages) {
				post[i] = satisfied ? 
						post[i] + (1-post[i])*percentageConstantUp :
							post[i] + (0 - post[i])*percentageConstantDown;
			}
		}
		
	}
	
	/**
	 * @return Returns amount of clauses satisfied based on setAssignment method.
	 */
	public int getNumberOfSatisfied() {
		return numberOfSatisfied;
	}
	
	/**
	 * @return Returns a number of unsatisfid clauses.
	 */
	public int getNumberOfUnsatisfied() {
		return formula.getNumberOfClauses() - numberOfSatisfied;
	}
	
	/**
	 * @return Returns a random unsatisfied clause.
	 */
	public Clause getRandomUnsatifiedClause() {
		Random r = new Random();
		
		int min = 1;
		int max = getNumberOfUnsatisfied();
		int randomIndex = r.nextInt((max-min) + 1) + min;
		
		int unsatisfiedCount = 0;
		for(int i = 0, count = formula.getNumberOfClauses(); i < count; i++) {
			if (!formula.getClause(i).isSatisfied(assignment)) {
				unsatisfiedCount++;
			}
			
			if (unsatisfiedCount == randomIndex) {
				return formula.getClause(i);
			}
		}
		
		return null;
	}
	
	/**
	 * @return Returns fitness values as quotient of satisfied and total clauses.
	 */
	public double getFitness() {
		return ((double) numberOfSatisfied) / formula.getNumberOfClauses(); 
	}
	
	/**
	 * @return Returns a fitness value while taking in account success rates of each clause.
	 */
	public double getCorrectedFitness() {
		return numberOfSatisfied + getPercentageBonus();
	}
	
	/**
	 * @return Returns ture if formula is fully satisfied, otherwise false.
	 * 		   Based on assignment method.
	 */
	public boolean isSatisfied() {
		return numberOfSatisfied == formula.getNumberOfClauses();
	}
	
	/**
	 * @return Returns percentage correction bonus for all clause corrections.
	 */
	public double getPercentageBonus() {
		double pSum = 0;
		
		for(int i = 0; i < formula.getNumberOfClauses(); i++) {
			pSum += getPercentage(i);
		}
		
		return pSum;
	}
	
	/**
	 * @param index
	 * @return Returns a percentage correction for a single clause.
	 */
	public double getPercentage(int index) {
		boolean clauseSatisfied = formula
				.getClause(index)
				.isSatisfied(assignment);
		
		double correction = percentageUnitAmount * (1 - post[index]);
		return clauseSatisfied ?  correction: - correction;
	}
}
