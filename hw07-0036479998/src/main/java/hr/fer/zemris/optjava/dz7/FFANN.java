package hr.fer.zemris.optjava.dz7;

import java.util.Arrays;
import java.util.List;

import hr.fer.zemris.optjava.dz7.ANNTrainer.DataExample;

/**
 * This class implements a feedfoward artificial neural network.
 * 
 * @author lmark
 *
 */
public class FFANN {

	/**
	 * Actiavation functions for each layer.
	 */
	private ITransferFunction[] activationFunctions;
	
	/**
	 * Nerual network training dataset.
	 */
	public List<DataExample> dataset;
	
	/**
	 * Neurons contained in each layer of the network.
	 */
	private int[] layers;
	
	/**
	 * Number of weights.
	 */
	private int weightCount = -1;
	
	/**
	 * Constructor initializing a neural network.
	 * 
	 * @param is
	 * @param iTransferFunctions
	 * @param dataset
	 */
	public FFANN(int[] layers, ITransferFunction[] activationFunctions,
			List<DataExample> dataset) {
		
		this.activationFunctions = activationFunctions;
		this.dataset = dataset;
		this.layers = layers;
		
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
	 * @param weights
	 * @return
	 */
	public double calculateTrainingError(double[] weights, boolean roundUp, boolean out) {
		double error = 0;
		
		// Check weight count
		if (weights.length != weightCount) {
			throw new IllegalArgumentException("Invalid number of weights");
		}
		
		for (DataExample dataExample : dataset) {
			double[] prediction = calcOutputs(dataExample.input, weights);
			
			if(roundUp) {
				for (int i = 0; i < prediction.length; i++) {
					prediction[i] = Math.round(prediction[i]);
				}
			}
			if (out) {
				System.out.println("Actual: " + Arrays.toString(dataExample.classification) +
						" Prediction: " + Arrays.toString(prediction));
				
			}
			
			double currentError = 0;
			for (int i = 0; i < prediction.length; i++) {
				currentError +=
						( prediction[i] - dataExample.classification[i] ) * 
						( prediction[i] - dataExample.classification[i] );
			}
			error += currentError;	
		}
		
		return error / dataset.size();
	}
	
	/**
	 * @return Returns number of neural network weights.
	 */
	public int getWeightsCount() {
		return weightCount;
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
		if (inputs.length != layers[0]) {
			throw new IllegalArgumentException(
					inputs.length + " inputs found, expected " + layers[0]);
		}
		
		// Check weight count
		if (weights.length != weightCount) {
			throw new IllegalArgumentException(
					weights.length + " weights found, expected " + weightCount);
		}
		
		// Calculate neural network output
		double[] currentInput = inputs;
		int currWeightIndex = 0;
		for (int i = 1; i < layers.length; i++) {
						
			// Get current layer weight count
			int currWeightCount = layers[i] * layers[i-1] + layers[i];
			double[] layerWeights = Arrays.copyOfRange(
					weights, currWeightIndex, currWeightCount + currWeightIndex);
			currWeightIndex += currWeightCount;
			
			currentInput = calculateLayerOutput(layerWeights, currentInput, i);
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
		
		for (DataExample dataExample : dataset) {
			double[] prediction = calcOutputs(dataExample.input, weights);
			
			boolean equals = true;
			for (int i = 0; i < prediction.length; i++) {
				prediction[i] = Math.round(prediction[i]);
				
				if (prediction[i] != dataExample.classification[i]) {
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
