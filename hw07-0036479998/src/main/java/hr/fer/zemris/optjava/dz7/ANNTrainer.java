package hr.fer.zemris.optjava.dz7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.optjava.dz7.alg.ClonAlg;
import hr.fer.zemris.optjava.dz7.alg.PPS;


/**
 * This class represents a neural network trainer for IRIS 
 * flower detection.
 * 
 * Command line arguments are: 
 * 1) file - path to the iris dataset file 
 * 2) alg - algorithm used for neural network training
 * 			"pso-a" - Particle swarm optimization a) part
 * 			"pso-b-d" - Particle swarm optimization b) part, radius d
 * 			"clonalg" - Clonealg algorithm
 * 3) n - population size
 * 4) merr - acceptable average quadratic error
 * 5) maxiter - maximum number of iterations
 *  
 * @author lmark
 *
 */
public class ANNTrainer {

	public static void main(String[] args) {
		
		if (args.length != 5) {
			System.out.println("Program requires 5 command line arguments");
			return;
		}
		
		// Parse dataset file
		Path dFile = Paths.get(args[0]);
		List<DataExample> trainingSet = parseFile(dFile);
		System.out.println(trainingSet);
		int populationSize = Integer.valueOf(args[2]);
		double merr = Double.valueOf(args[3]);
		int maxiter = Integer.valueOf(args[4]);
		
		// Initialize neural network
		FFANN ffann = new FFANN(
				new int[] {4,3,3},
				new ITransferFunction[] {
				new SigmoidTransferFunction(),
				new SigmoidTransferFunction(),
				//new SigmoidTransferFunction(),
				new LinearTransferFunction() 
				},
				trainingSet);	
		
		// Test weights 
		double[] weights = new double[ffann.getWeightsCount()];
		for (int i = 0; i < weights.length; i++) {
			weights[i] = -0.2;
		}
		System.out.println("Test error: " + ffann.calculateTrainingError(weights, false, true));
		
		int C1 = 2;
		int C2 = 2;
		// Parse algorithm argument
		if ("pso-a".equals(args[1])) {
			PPS pps = new PPS(maxiter, populationSize, merr, -1, ffann);
			pps.run(C1, C2);
			
		} else if (args[1].startsWith("pso-b")){
			String[] split = args[1].split("[-]");
			PPS pps = new PPS(maxiter, populationSize, merr, Integer.valueOf(split[2]), ffann);
			pps.run(C1, C2);
					
		} else if ("clonalg".equals(args[1])) {
			ClonAlg ca = new ClonAlg(ffann, maxiter, populationSize, 2, merr);
			ca.run();
			
		} else {
			System.out.println("Unrecognized algorithm given.");
		}
	}
	
	/**
	 * @param dFile
	 * @return Return list of examples from dataset file.
	 */
	private static List<DataExample> parseFile(Path dFile) {
		List<String> lines = null;
		
		try {
			lines = Files.readAllLines(dFile);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		// Parse line exapmles
		List<DataExample> examples = new ArrayList<>();
		for (String line : lines) {
			
			DataExample newExample = new DataExample();
			String[] splitLine = line.trim().split("[:]");
			
			// Extract number strings
			String input = splitLine[0].substring(1, splitLine[0].length()-1);
			String output = splitLine[1].substring(1, splitLine[1].length()-1);
			
			// Separate numbers
			String[] inputNums = input.split("[,]");
			String[] classification = output.split("[,]");
			
			newExample.input = new double[inputNums.length];
			newExample.classification = new int[classification.length];
			
			// Extract input
			for (int i = 0; i < inputNums.length; i++) {
				newExample.input[i] = Double.valueOf(inputNums[i]);
			}
			
			// Extrace class
			for (int i = 0; i < classification.length; i++) {
				newExample.classification[i] = Integer.valueOf(classification[i]);
			}
			
			examples.add(newExample);
		}
		
		return examples;
	}

	/**
	 * Class representing each data example.
	 * 
	 * @author lmark
	 *
	 */
	public static class DataExample {
		double[] input;
		int[] classification;
		
		@Override
		public String toString() {
			return Arrays.toString(input) + ", " + Arrays.toString(classification) + "\n";
		}
	}
}
