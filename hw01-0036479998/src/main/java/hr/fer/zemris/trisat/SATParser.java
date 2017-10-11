package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.trisat.model.Clause;
import hr.fer.zemris.trisat.model.SATFormula;

/**
 * Parsers files containing SAT formula information.
 * 
 * @author lmark
 *
 */
public class SATParser {

	/**
	 * Lines of the document.
	 */
	private List<String> lines = new ArrayList<>();
	
	/**
	 * Current line index being evaluated.
	 */
	private int currentLineIndex = 0;
	
	/**
	 * Initializes document lines.
	 * 
	 * @param lines
	 */
	public SATParser(List<String> lines) {
		this.lines.addAll(lines);
	}
	
	/**
	 * @return Returns a complete formula found in the document.
	 */
	public SATFormula getFormula() {
		Clause[] clauses = null;
		int clauseIndex = 0;
		int numberOfVariables = 0;
		
		while (currentLineIndex < lines.size()) {
			String[] words = lines
					.get(currentLineIndex)
					.trim()
					.split("\\s+");
			
			// If '%' is found break.
			if ("%".equals(words[0])) {
				break;
			}
			
			// If line is a comment skip.
			if ("c".equals(words[0])) {
				currentLineIndex++;
				continue;
			}
			
			// Information about the formula
			if ("p".equals(words[0])) {
				numberOfVariables = Integer.valueOf(words[2]);
				
				// Initialize clauses
				int numberOfClauses = Integer.valueOf(words[3]);
				clauses = new Clause[numberOfClauses];
				
				currentLineIndex++;
				continue;
			}
			
			// Try parsing a clause
			try{
				Clause clause = parseClause(words);
				clauses[clauseIndex] = clause;
				
				clauseIndex++;
				currentLineIndex++;
			} catch (NumberFormatException e) {
				System.out.println("Unable to parse: " + Arrays.toString(words));
				throw e;
			}
		}
		
		return new SATFormula(numberOfVariables, clauses);
	}

	/**
	 * @param words
	 * @return Return a constructed clause based on words input.
	 */
	private Clause parseClause(String[] words) {
		int numberOfVariables = 0;
		
		// Count number of variables
		for (String string : words) {
			if (string.equals("0")) {
				break;
			}
			
			numberOfVariables++;
		}
		
		// Initializes field of indexes.
		int[] indexes = new int[numberOfVariables];
		for (int i = 0; i < words.length; i++) {
			int index = Integer.valueOf(words[i]);
			
			if (index == 0) {
				break;
			}
			
			indexes[i] = index;
		}
		
		return new Clause(indexes);
	}
	
}
