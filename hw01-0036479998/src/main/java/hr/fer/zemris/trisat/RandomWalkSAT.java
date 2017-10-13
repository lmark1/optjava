package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.model.Clause;
import hr.fer.zemris.trisat.model.MutableBitVector;
import hr.fer.zemris.trisat.model.SATFormula;

/**
 * Random walk SAT. Multistart search algorithm. 
 * Each iteration chooses a random unsatisfied clause from the current solution,
 * flips a random literal from the clause, or flips the literal resulting in the 
 * least number of unsatisfied clauses.
 *  
 * @author lmark
 *
 */
public class RandomWalkSAT extends GSAT{

	/**
	 * Chance to flip the clause literals randomly.
	 */
	private static final double randomFlipPercent = 0.1;
	
	public RandomWalkSAT(SATFormula formula) {
		super(formula);
	}
	
	/**
	 * randomFlipPercent chance it flips random literal in a random unsatisfied clause.
	 * 1 - randomFlipPercent chance it flips a literal which results in a least amount of
	 * unsatisfied clauses.
	 * 
	 * @param solution
	 */
	@Override
	public void flipSolution(MutableBitVector solution) {
		Clause uClause = getFormulaStats().getRandomUnsatifiedClause();
		
		if (Math.random() <= randomFlipPercent) {
			// Flip some random bits in the clause
			getFormulaStats().setAssignment(solution, false);
			
			// Get random literal
			int randomIndex = r.nextInt(uClause.getSize());
			int literalIndex = Math.abs(uClause.getLiteral(randomIndex));
			solution.flip(literalIndex-1);
		} else {
			
			// Get best literal index
			double min = getFormula().getNumberOfClauses();
			int bestIndex = -1;
			for (int i = 0, lCount = uClause.getSize(); i < lCount; i++) {
				
				int literalIndex = Math.abs(uClause.getLiteral(i));
				solution.flip(literalIndex-1);
				
				double unsatisfiedCount = getUnsatisfiedClauses(solution);
				if (unsatisfiedCount <= min) {
					min = unsatisfiedCount;
					bestIndex = i;
				}
				
				solution.flip(literalIndex-1);
			}
			
			int bestLiteral = Math.abs(uClause.getLiteral(bestIndex));
			solution.flip(bestLiteral-1);
		}
	}
	
	
}
