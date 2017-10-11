package hr.fer.zemris.trisat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import hr.fer.zemris.trisat.model.SATFormula;

/**
 * 3SAT solver class.
 * 
 * @author lmark 
 *
 */
public class TriSatSolver	 {
	
	/**
	 * Main method.
	 * 
	 * @param args First argument - index of the algorithm.
	 * 			   Second argument - path to boolean clauses.
	 */
	public static void main(String[] args) {
	
		if (args.length != 2) {
			System.out.println("2 arguments expected");
			return;
		}
		
		Path satPath = Paths.get(args[1]);
		List<String> lines = null;
		try {
			lines = Files.readAllLines(satPath);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		SATParser parser = new SATParser(lines);
		SATFormula formula = parser.getFormula();
		System.out.println("Read file: " + satPath.getFileName());
		System.out.println("Found formula: " + formula);
		
		// Check algorithm
		switch (Integer.valueOf(args[0])) {
		
		case 1:
			SATBruteForce bf = new SATBruteForce(formula);
			System.out.println("\nSolutions:\n"+bf.getSolutions());
			break;
		
		case 2:
			SATIterate it = new SATIterate(formula);
			System.out.println(it.getSolution());
			break;
		
		case 3:
			SATIterateExtended itE = new SATIterateExtended(formula);
			System.out.println(itE.getSolution());
			break;
			
		default:
			System.out.println("First argument is number of algorithm: 1-6");
			return;
		}
	}
}
