package hr.fer.zemris.trisat;

import hr.fer.zemris.trisat.model.MutableBitVector;
import hr.fer.zemris.trisat.model.SATFormula;

/**
 * Extension of iterated local search. When algorithm gets stuck 
 * randomly change the current solution and try again.
 * 
 * @author lmark
 *
 */
public class SATIterateExtended extends SATIterate{

	public static final int MAX_TRIES = 100;
	public static final double flipPercent = 0.3;
	
	public SATIterateExtended(SATFormula formula) {
		super(formula);
	}
	
	@Override
	public String getSolution() {
		MutableBitVector solution = new MutableBitVector(
				r, 
				getFormula().getNumberOfVariables());
		
		int t = 0;
		while (t < MAX_TRIES) {
			System.out.println("Try: " + t);
			
			solution = (MutableBitVector) runLocalSearch(solution);
			
			getFormulaStats().setAssignment(solution, false);
			if (getFormulaStats().isSatisfied()) {
				return "Solution found:\n" + solution;
			} else {
				System.out.println(getFormulaStats().getFitness());
				mutateSolution(solution);
			}
			
			t++;
		}
		
		return "Max iteration exceeded";
	}

	/**
	 * Change the current solution.
	 * 
	 * @param solution
	 */
	private void mutateSolution(MutableBitVector solution) {
		for (int i = 0, len = solution.getSize(); i < len; i++) {
			if (Math.random() <= flipPercent) {
				solution.flip(i);
			}
		}
	}
}
