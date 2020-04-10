package hr.fer.zemris.optjava.dz8.nn;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.optjava.dz8.ANNTrainer.DataExample;
import hr.fer.zemris.optjava.dz8.ANNTrainer.Dataset;

/**
 * This class implements an Elman neural network.
 * 
 * @author lmark
 *
 */
public class ElmanNN {

	/**
	 * Actiavation functions for each layer.
	 */
	private ITransferFunction[] activationFunctions;
	
	/**
	 * Nerual network training dataset.
	 */
	public Dataset dataset;
	
	/**
	 * Neurons contained in each layer of the network.
	 */
	private int[] layers;
	
	/**
	 * Number of weights.
	 */
	private int weightCount = -1;
	
	/**
	 * Current context values.
	 */
	private double[] currentContext;
	
	/**
	 * Constructor initializing a neural network.
	 * 
	 * @param is
	 * @param iTransferFunctions
	 * @param dataset
	 */
	public ElmanNN(int[] layers, ITransferFunction[] activationFunctions,
			Dataset dataset) {
		
		this.activationFunctions = activationFunctions;
		this.dataset = dataset;
		this.layers = layers;
		
		// Check for hidden layers.
		if (this.layers.length < 3) {
			throw new IllegalArgumentException(""
					+ "Network needs to have atleast 1 hidden layer");
		}
		
		// Add contextual inputs 
		this.layers[0] = this.layers[0] + this.layers[1];
		
		weightCount = 0;
		for (int i = 0; i < layers.length - 1; i++) {
			// Connection weights
			weightCount += 
					layers[i] * layers[i+1];
			
			// Bias
			weightCount += 
					layers[i+1];
		}
	}

 
	/**
	 * Calculate training error given the weights.
	 * 
	 * @param weights - first layers[1] weights are initial contextual values
	 * @return
	 */
	public double calculateTrainingError(double[] weights, boolean roundUp, boolean out, boolean training) {
		double error = 0;
		
		// Check weight count
		if (weights.length != getParameterCount()) {
			throw new IllegalArgumentException("Invalid number of weights");
		}
		
		// Initialize context values
		currentContext = new double[layers[1]];
		for (int k = 0; k < layers[1]; k++) {
			currentContext[k] = weights[k];
		}
		
		// Copy actual connection weights
		double[] actualWeights = Arrays.copyOfRange(weights, layers[1], weights.length);
		
		List<DataExample> examples = null;
		if (training) {
			examples = dataset.trainingSet;
		} else {
			examples = dataset.testingSet;
		}
		
		for (DataExample dataExample : examples) {
			double[] prediction = calcOutputs(dataExample.input, actualWeights);
			
			if(roundUp) {
				for (int i = 0; i < prediction.length; i++) {
					prediction[i] = Math.round(prediction[i]);
				}
			}
			if (out) {
				System.out.println("Actual: " + Arrays.toString(dataExample.output) +
						" Prediction: " + Arrays.toString(prediction));
			}
			
			double currentError = 0;
			for (int i = 0; i < prediction.length; i++) {
				currentError +=
						( prediction[i] - dataExample.output[i] ) * 
						( prediction[i] - dataExample.output[i] );
			}
			error += currentError;	
		}
		
		currentContext = null;
		return error / examples.size();
	}
	
	/**
	 * @return Returns number of neural network weights.
	 */
	public int getWeightsCount() {
		return weightCount;
	}
	
	/**
	 * @return Returns number of parameters including 
	 * 		network weights and initial context.
	 */
	public int getParameterCount() {
		return weightCount + layers[1];
	}
	
	/**
	 * Calculate neural network output.
	 * 
	 * @param inputs
	 * @param weights
	 * @param outputs
	 */
	public double[] calcOutputs(double[] inputs, double[] weights) {
		
		// Check input count
		if (inputs.length + currentContext.length != layers[0]) {
			throw new IllegalArgumentException(
					(inputs.length+currentContext.length) 
					+ " inputs found, expected " + layers[0]);
		}
		
		// Check weight count
		if (weights.length != getWeightsCount()) {
			throw new IllegalArgumentException(
					weights.length + " weights found, expected " + weightCount);
		}
		
		// Calculate neural network output
		// Construct initial input vector containing 		
		double[] currentInput = new double[layers[0]];
		for (int i = 0; i < layers[0]; i++) {
			
			if (i < inputs.length) {
				currentInput[i] = inputs[i];
			
			} else {
				currentInput[i] = currentContext[i - inputs.length];
			}
		}
		
		int currWeightIndex = 0;
		for (int i = 1; i < layers.length; i++) {
						
			// Get current layer weight count
			int currWeightCount = layers[i] * layers[i-1] + layers[i];
			double[] layerWeights = Arrays.copyOfRange(
					weights, currWeightIndex, currWeightCount + currWeightIndex);
			currWeightIndex += currWeightCount;
			
			currentInput = calculateLayerOutput(layerWeights, currentInput, i);
			
			// If we calculated first layer output, save as current context
			if (i == 1) {
				
				currentContext = new double[layers[1]];
				for (int k = 0; k < layers[1]; k++) {
					currentContext[k] = currentInput[k];
				}
			}
		}
		
		return currentInput;
	}

	/**
	 * @param layerWeights
	 * @param currentInput
	 * @param i
	 * @return Returns a vector of NN layer outpus.
	 */
	private double[] calculateLayerOutput(double[] layerWeights,
			double[] currentInput, int layerIndex) {
		
		if (currentInput.length != layers[layerIndex-1]) {
			throw new IllegalArgumentException(
					currentInput.length + " inputs found, expected: " + layers[layerIndex-1]);
		}
		
		// Calculate single layer output
		double[] output = new double[layers[layerIndex]];
		int currWeightIndex = 0;
		for (int i = 0; i < output.length; i++) {
			
			// Extract i-th neuron weights
			int weightCount = layers[layerIndex - 1] + 1;
			double[] weights = Arrays.copyOfRange(
					layerWeights, currWeightIndex, weightCount + currWeightIndex);
			currWeightIndex += weightCount;
			
			output[i] = activationFunctions[layerIndex-1].valueAt(
					calculateNeuruonOutput(weights, currentInput));
		}
		
		return output;
	}

	/**
	 * Calculate number of false classified.
	 * 
	 * @param weights
	 * @return
	 */
	public int zeroOneError(double[] weights) {
		int error = 0;

		// Check weight count
		if (weights.length != weightCount) {
			throw new IllegalArgumentException("Invalid number of weights");
		}
		
		for (DataExample dataExample : dataset.trainingSet) {
			double[] prediction = calcOutputs(dataExample.input, weights);
			
			boolean equals = true;
			for (int i = 0; i < prediction.length; i++) {
				prediction[i] = Math.round(prediction[i]);
				
				if (prediction[i] != dataExample.output[i]) {
					equals = false;
				}
			}	
			
			if (!equals) {
				error++;
			}
			
		}
		return error;
	}
	
	/**
	 * @param weights
	 * @param currentInput
	 * @return Returns a single neuron output.
	 */
	private double calculateNeuruonOutput(double[] weights,
			double[] currentInput) {
		
		// Weight * input
		double sum = 0;
		for (int i = 0; i < currentInput.length; i++) {
			sum += 
					currentInput[i] * weights[i];
		}
		
		// Add bias last
		sum += weights[weights.length - 1];
		
		return sum;
	}

}
