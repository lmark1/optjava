package hr.fer.zemris.otpjava.dz2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import hr.fer.zemris.otpjava.dz2.functions.ErrorFunction;

/**
 * Class contains main method. Accepts 3 command line arguments.
 * 1. argument - "grad" / "newton" (choose which optimiziation method to use)
 * 2. argument - max iterations
 * 3. argument - path to the system file, e.g. 02-zad-sustav.txt
 * 
 * Outputs the solution and error at the time it stopped searching.
 * 
 * @author lmark
 *
 */
public class Sustav {

	/**
	 * Main method.
	 * @param args
	 */
	public static void main(String[] args) {
	
		if (args.length != 3) {
			System.out.println("3 arguments expected..");
			return;
		}
		
		List<Matrix> aAndYMatrix = parseFile(Paths.get(args[2])); 
		int maxIter = Integer.parseInt(args[1]);
		ErrorFunction func = new ErrorFunction(aAndYMatrix.get(0), aAndYMatrix.get(1));
		List<Matrix> solList = null;
		switch (args[0]) {
		
			case "grad":
				// Gradient descent
				solList = NumOptAlgorithms.gradientDescent(func, maxIter, null);
				break;

			case "newton":
				solList = NumOptAlgorithms.Newton(func, maxIter, null);
				break;
				
			default:
				System.out.println("Unknown algorithm selected.");
				return;
		}
		
		Matrix solution = solList.get(solList.size()-1);
		System.out.println("L2 error: " + func.evaluate(solution).get(0, 0));
	}

	/**
	 * Parse file. Find A and y matrix.
	 * Return them as a list where 0 index is A, 1 is y.
	 * 
	 * @param string Path to file.
	 * @return
	 */
	public static List<Matrix> parseFile(Path path) {
		List<String> lines = null;
		
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		int lineCount = lines.size();
		Matrix A = null;
		Matrix y = null;
		int rowIndex = 0;
		for (String line : lines) {
			
			// If line is a comment skip
			if (line.startsWith("#")) {
				lineCount--;
				continue;
			}
			
			if (A == null) {
				A = new Matrix(lineCount, lineCount);
			}
			
			if (y == null) {
				y = new Matrix(lineCount, 1);
			}
			
			List<Double> dList = Util.parseLine(line);
			
			// Extract numbers
			for (int i = 0, len = dList.size(); i < len; i++) {
			
				// Not last number - contained in A
				if (i+1 < len) {
					A.set(rowIndex, i, dList.get(i));
					
				} else {
					// ... else go to y
					y.set(rowIndex, 0, dList.get(i));
				}
			}
			
			rowIndex++;
		}
		
		List<Matrix> sol = new ArrayList<>();
		sol.add(A);
		sol.add(y);
		
		return sol;
	}
}
