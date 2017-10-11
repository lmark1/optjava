package hr.fer.zemris.trisat.model;

/**
 * This class represents SAT formula as a collection of clauses.
 * 
 * @author lmark
 *
 */
public class SATFormula {

	private int numberOfVariables;
	private Clause[] clauses;
	
	/**
	 * Initializes number of variables and clauses fields.
	 * 
	 * @param numberOfVariables
	 * @param clasues
	 */
	public SATFormula(int numberOfVariables, Clause[] clasues) {
		this.numberOfVariables = numberOfVariables;
		this.clauses = clasues;
	}
	
	/**
	 * @return Returns number of formula variables.
	 */
	public int getNumberOfVariables() {
		return numberOfVariables;
	}
	
	/**
	 * @return Returns number of formula clauses.
	 */
	public int getNumberOfClauses() {
		return clauses.length;
	}
	
	/**
	 * @param index
	 * @return Returns clause at given index.
	 */
	public Clause getClause(int index) {
		return clauses[index];
	}
	
	/**
	 * @param assignment
	 * @return True if formula is satisfied, otherwise false.
	 */
	public boolean isSatisfied(BitVector assignment) {
		for (Clause clause : clauses) {
			
			// All clauses must be satisfied to satisfy the formula
			if (!clause.isSatisfied(assignment)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0, len = clauses.length; i < len; i++) {
			sb.append(clauses[i]);
			
			if (i < len-1) {
				sb.append("*");
			}
		}
		
		return sb.toString();
	}
}
