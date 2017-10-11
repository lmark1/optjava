package hr.fer.zemris.trisat.model;

/**
 * This class represents a boolean clause.
 * 
 * @author lmark
 *
 */
public class Clause {

	private int[] clauseIndexes;
	
	/**
	 * Initialize clause indexes. Clause indexes start at 1.
	 * 
	 * @param indexes
	 */
	public Clause(int[] indexes) {
		clauseIndexes = new int[indexes.length];
		
		for (int i = 0; i < indexes.length; i++) {
			clauseIndexes[i] = indexes[i];
		}
	}
	
	/**
	 * @return Returns size of the clause.
	 */
	public int getSize() {
		return clauseIndexes.length;
	}
	
	/**
	 * @param index
	 * @return Returns a clause literal at given index.
	 */
	public int getLiteral(int index) {
		return clauseIndexes[index];
	}
	
	/**
	 * @param assignment
	 * @return Returns true if this clause satisfies the given assignment.
	 */
	public boolean isSatisfied(BitVector assignment) {
		for (int literal : clauseIndexes) {
			boolean literalValue = assignment.get(Math.abs(literal)-1);
			
			// Check if it's a negation
			if (literal < 0) {
				literalValue = !literalValue;
			}
			
			// If any literal value is true clause is satisfied
			if (literalValue) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("(");
		for(int i = 0, len = clauseIndexes.length; i < len; i++) {
			sb.append(clauseIndexes[i]);
			
			if (i < len-1) {
				sb.append(" + ");
			}
		}
		sb.append(")");
		
		return sb.toString();
	}
}
