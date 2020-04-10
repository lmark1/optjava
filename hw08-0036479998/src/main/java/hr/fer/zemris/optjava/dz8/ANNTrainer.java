package hr.fer.zemris.optjava.dz8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.optjava.dz8.nn.ElmanNN;
import hr.fer.zemris.optjava.dz8.nn.ITransferFunction;
import hr.fer.zemris.optjava.dz8.nn.LinearTransferFunction;
import hr.fer.zemris.optjava.dz8.nn.SigmoidTransferFunction;
import hr.fer.zemris.optjava.dz8.nn.TDNN;


/**
 * This class represents a neural network trainer for sequence
 * detection.
 * 
 * Command line arguments are: 
 * 1) file - path to the iris dataset file 
 * 2) "tdnn-arh" - TDNN neural net architecture
 * 	  "elman-arh" - Elman neural network architecture
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
		int populationSize = Integer.valueOf(args[2]);
		double merr = Double.valueOf(args[3]);
		int maxiter = Integer.valueOf(args[4]);
		
		// Check algorithm
		String algName = args[1];
		String[] splitName = algName.split("[-]");
		
		// Check for false names
		if (splitName.length != 2 || !"arh".equals(splitName[1])) {
			System.out.println("Invalid alogrithm name found.");
			return;
		}
		
		IEvaulator evaluator = null;
		Dataset dSet = null;
		if ("tdnn".equals(splitName[0])) {
			dSet = parseAndNormalize(dFile, 8, 600);
			
			// Initialize neural network
			TDNN tdnn = new TDNN(
					new int[] {8,10,4,1},
					new ITransferFunction[] {
					new SigmoidTransferFunction(),
					new SigmoidTransferFunction(),
					new LinearTransferFunction()
					},
					dSet);
			evaluator = new TDNNEvaulator(tdnn);
			
		} else if ("elman".equals(splitName[0])) {
			dSet = parseAndNormalize(dFile, 1, 600);
			
			
			ElmanNN enn = new ElmanNN(
					new int[] {1,10,4,1},
					new ITransferFunction[] {
					new SigmoidTransferFunction(),
					new SigmoidTransferFunction(),
					new LinearTransferFunction()
					},
					dSet);
			evaluator = new ElmanEvaluator(enn);
			
		} else {
			System.out.println("Invalid algorithm name");
			return;
		}
		
		DIfferentialEvolution de = new DIfferentialEvolution(
				evaluator, maxiter, populationSize, merr, 1, 0.03);
		double[] bestWeights = de.run();
		evaluator.printErrors(bestWeights);
	}
	
	
	/**
	 * @param path
	 * @param exampleSize
	 * @param exampleCount
	 * @return List of examples.
	 */
	private static Dataset parseAndNormalize(
			Path path, int exampleSize, int exampleCount) {
		
		List<String> lines = null;
		
		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			return null;
		}
		
		List<Double> numbers = new ArrayList<>();
		
		// Parse lines into doubles
		for (String line : lines) {
			
			if (line.isEmpty()) {
				continue;
			}
			
			numbers.add(Double.valueOf(line));
		}
		
		numbers = normalize(numbers, -1, 1);
		
		if (exampleCount == -1) {
			exampleCount = numbers.size()-exampleSize;
		}
		
		// Construct example count datasets
		List<DataExample> trainingSet = new ArrayList<>();
		int dataOffset = 0;
		while(trainingSet.size() < exampleCount) {
			
			// Extract input and output
			double[] input = new double[exampleSize];
			for (int i = 0; i < exampleSize; i++) {
				input[i] = numbers.get(i + dataOffset);
			}
			double[] output = new double[]{
					numbers.get(exampleSize + dataOffset)
					};
			
			// Construct example
			DataExample example = new DataExample();
			example.input = input;
			example.output = output;
			trainingSet.add(example);
			
			dataOffset++;
		}
		
		List<DataExample> testingSet = new ArrayList<>();
		while (testingSet.size() + trainingSet.size() < numbers.size()-exampleSize) {
			// Extract input and output
			double[] input = new double[exampleSize];
			for (int i = 0; i < exampleSize; i++) {
				input[i] = numbers.get(i + dataOffset);
			}
			double[] output = new double[]{
					numbers.get(exampleSize + dataOffset)
					};
			
			// Construct example
			DataExample example = new DataExample();
			example.input = input;
			example.output = output;
			testingSet.add(example);
			
			dataOffset++;
		}
		
		Dataset dSet = new Dataset();
		dSet.testingSet = testingSet;
		dSet.trainingSet = trainingSet;
		
		return dSet;
	}
	
	
	/**
	 * @param numbers
	 * @param finalMin
	 * @param finalMax
	 * @return Scaled double list between finalMin and finalMax.
	 */
	private static List<Double> normalize(List<Double> numbers, double finalMin, double finalMax) {
		double min = 10e10;
		double max = -10e10;
		
		for (Double number : numbers) {
			if (number < min) {
				min = number;
			}
			
			if (number > max) {
				max = number;
			}
		}
		
		List<Double> scaledNumbers = new ArrayList<>();
		for (Double number : numbers) {
			scaledNumbers.add(
					(finalMax - finalMin)/(max - min) * (number - max) + finalMax);
		}
		
		return scaledNumbers;
	}

	/**
	 * Class representing each data example.
	 * 
	 * @author lmark
	 *
	 */
	public static class DataExample {
		public double[] input;
		public double[] output;
		
		@Override
		public String toString() {
			return Arrays.toString(input) + ", " + Arrays.toString(output) + "\n";
		}
	}
	
	public static class Dataset {
		public List<DataExample> trainingSet;
		public List<DataExample> testingSet;
	}
}
