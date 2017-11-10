package hr.fer.zemris.optjava.dz3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz3.impl.BitVectorNeighbourhood;
import hr.fer.zemris.optjava.dz3.impl.BitVectorSolution;
import hr.fer.zemris.optjava.dz3.impl.DoubleArrayNormNeighbourhood;
import hr.fer.zemris.optjava.dz3.impl.DoubleArraySolution;
import hr.fer.zemris.optjava.dz3.impl.GeometricTempSchedule;
import hr.fer.zemris.optjava.dz3.impl.NaturalBinaryDecoder;
import hr.fer.zemris.optjava.dz3.impl.PassThroughDecoder;
import hr.fer.zemris.optjava.dz3.impl.SystemFunction;
/**
 * Accepts 2 arguments:
 * 1. path to file
 * 2. decimal / binary:{number} - number of bits for each varaible.
 * 
 * @author lmark
 *
 */
public class RegresijaSustava {
	private final static Random random = new Random();
	
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("2 arguments expected.");
			return;
		}
		
		Path path = Paths.get(args[0]);
		List<Matrix> matrices = parseFile(path);
		SystemFunction tf = new SystemFunction(matrices.get(0), matrices.get(1));
		
		// Parse second argument
		if (args[1].toLowerCase().equals("decimal")) {
			
			// Generate initial
			DoubleArraySolution sol = new DoubleArraySolution(6);
			sol.randomize(random, new double[] {5, 5, 5, 5, 5, 5});
			
			SimulatedAnnealing<DoubleArraySolution> sim = new SimulatedAnnealing<DoubleArraySolution>(
					new PassThroughDecoder(), 
					new DoubleArrayNormNeighbourhood(new double[] {0.6, 0.6, 0.6, 0.6, 0.6, 0.6}), 
					sol, 
					tf, 
					new GeometricTempSchedule(0.99, 5000, 250, 1000),
					true);
			
			sim.run();					
		
		} else {
			String[] secArgs = args[1].split(":");
			
			if (secArgs.length != 2) {
				System.out.println("Expected 2nd argument in form binarx:{number}");
				return;
			}
			
			if (!secArgs[0].equals("binary")) {
				System.out.println("Only binary supported.");
				return;
			}
			
			int bitsPerVar = Integer.valueOf(secArgs[1]);
			BitVectorSolution sol = new BitVectorSolution(6 * bitsPerVar);
			sol.randomize(random, 0.4);
			
			SimulatedAnnealing<BitVectorSolution> sim = new SimulatedAnnealing<BitVectorSolution>(
					new NaturalBinaryDecoder(
							new double[] {-5, -5, -5, -5, -5, -5}, 
							new double[] {5, 5, 5, 5, 5, 5}, 
							new int[] {bitsPerVar, bitsPerVar, bitsPerVar, bitsPerVar, bitsPerVar, bitsPerVar}, 
							6), 
					new BitVectorNeighbourhood(0.4), 
					sol, 
					tf, 
					new GeometricTempSchedule(0.99, 5000, 250, 1000),
					true);
			
			sim.run();		
		}
		
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
