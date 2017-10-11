package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.trisat.model.BitVector;
import hr.fer.zemris.trisat.model.SATFormula;

/**
 * Solves sat problem using brute force.
 * 
 * @author lmark
 *
 */
public class SATBruteForce {

	private SATFormula formula;
	private List<BitVector> solutions = new ArrayList<>();
	
	public SATBruteForce(SATFormula formula) {
		this.formula = formula;
		solve();
	}

	/**
	 * Solve using brute force.
	 */
	private void solve() {
		double max = Math.pow(2, formula.getNumberOfVariables())-1;
		int vectorLength = formula.getNumberOfVariables();
		
		for(int i = 0; i <= max; i++) {
			BitVector assignment = new BitVector(i, vectorLength);
			
			if (formula.isSatisfied(assignment)) {
				solutions.add(assignment);
			}
		}
	}
	
	/**
	 * @return Returns solutions for the given formula.
	 */
	public String getSolutions() {
		StringBuilder sb = new StringBuilder();
		
		for (BitVector bitVector : solutions) {
			sb.append(bitVector + "\n");
		}
		
		return sb.toString();
	}
}
