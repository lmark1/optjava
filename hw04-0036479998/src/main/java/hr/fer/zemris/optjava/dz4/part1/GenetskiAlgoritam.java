package hr.fer.zemris.optjava.dz4.part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz4.impl.BLXCrossover;
import hr.fer.zemris.optjava.dz4.impl.DoubleArrayPopulation;
import hr.fer.zemris.optjava.dz4.impl.DoubleArraySolution;
import hr.fer.zemris.optjava.dz4.impl.GeneticAlgorithm;
import hr.fer.zemris.optjava.dz4.impl.NormalMutation;
import hr.fer.zemris.optjava.dz4.impl.RouletteWheelSelection;
import hr.fer.zemris.optjava.dz4.impl.SystemFunction;
import hr.fer.zemris.optjava.dz4.impl.TournamentSelection;
import hr.fer.zemris.optjava.dz4.model.ISelection;

/**
 * This class implements a genetic algorithm.
 * 
 * Generational, elitistic, BLX - alpha crossover.
 * 
 * Accepts following parameters from command line:
 * 1. arg - Population size
 * 2. arg - Minimal error for stopping
 * 3. arg - Maximum number of generation algorithm is permitted
 * 4. arg - selection type: "rouletteWheel" or "tournament:n" 
 * 			where n is number of solutions participating.
 * 5. arg - sigma parameter, used for mutations
 * 
 * @author lmark
 *
 */
public class GenetskiAlgoritam {
	
	private static int populationSize;
	private static double minimalError;
	private static int maxGenerations;
	private static double sigma;
	private static ISelection<DoubleArraySolution> selectionMethod;
	
	/**
	 * Main class method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 5) {
			System.out.println("Program accepts 5 command line arguments.");
			return;
		}
		
		// Parse numerical arguments
		try {
			populationSize = Integer.valueOf(args[0]);
			minimalError = Double.valueOf(args[1]);
			maxGenerations = Integer.valueOf(args[2]);
			sigma = Double.valueOf(args[4]);
		} catch (NumberFormatException e) {
			System.out.println("1, 2, 3, 5 argument have to be numbers");
			return;
		}
		
		// Parse selection argument
		String selection = args[3];
		
		if ("rouletteWheel".equals(selection)) {
			selectionMethod = new RouletteWheelSelection();
			
		} else if (selection.startsWith("tournament")) {
			String[] splitString = selection.split(":");
			selectionMethod = new TournamentSelection(Integer.valueOf(splitString[1]));
			
		} else {
			System.out.println("Expected selection: rouletteWheel and tournament:n");
			return;
		}
		
		List<Matrix> matrices = parseFile(Paths.get("./src/main/resources/02-zad-prijenosna.txt"));
		SystemFunction sysFunction = new SystemFunction(matrices.get(0), matrices.get(1));
		DoubleArrayPopulation population = new DoubleArrayPopulation(sysFunction, populationSize, 6);
		
		GeneticAlgorithm<DoubleArraySolution> ga = 
				new GeneticAlgorithm<>(
						maxGenerations, 
						selectionMethod, 
						new NormalMutation(sigma, 1./6.), 
						population, 
						new BLXCrossover(0.15), 
						minimalError);
		
		ga.run();
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
