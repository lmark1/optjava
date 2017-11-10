package hr.fer.zemris.optjava.dz3;

import java.util.Arrays;
import java.util.Random;

import hr.fer.zemris.optjava.dz3.model.IDecoder;
import hr.fer.zemris.optjava.dz3.model.IFunction;
import hr.fer.zemris.optjava.dz3.model.INeighbourhood;
import hr.fer.zemris.optjava.dz3.model.IOptAlgorithm;
import hr.fer.zemris.optjava.dz3.model.ITempSchedule;

/**
 * This class implements a parametrised simulated annealing optimisation algorithm.
 * 
 * @author lmark
 *
 * @param <T>
 */
public class SimulatedAnnealing<T> implements IOptAlgorithm<T>{

	private final Random random = new Random();
	
	/**
	 * Value decoder.
	 */
	private IDecoder<T> decoder;
	
	/**
	 * Neighbourhood generator.
	 */
	private INeighbourhood<T> neighbourhood;
	
	/**
	 * Algoritm starting point.
	 */
	private T startsWith;
	
	/**
	 * Function being minimized / maximized.
	 */
	private IFunction function;
	
	/**
	 * Flag for minimizing or maximizing.
	 */
	private boolean minimize;
	
	/**
	 * Algorithm temperature schedule.
	 */
	private ITempSchedule tSchedule;
	
	/**
	 * Initializes all class variables.
	 * 
	 * @param decoder
	 * @param neighbourhood
	 * @param startsWith
	 * @param function
	 * @param minimize
	 */
	public SimulatedAnnealing(
			IDecoder<T> decoder, 
			INeighbourhood<T> neighbourhood,
			T startsWith,
			IFunction function,
			ITempSchedule tSchedule,
			boolean minimize) {
		
		this.decoder = decoder;
		this.neighbourhood = neighbourhood;
		this.startsWith = startsWith;
		this.function = function;
		this.tSchedule = tSchedule;
		this.minimize = minimize;
	}
	
	@Override
	public void run() {
		T currentSolution = null;
		double currentFitness = -1;
		
		while(tSchedule.getOuterLoopCounter() != -1) { // Outer loop
			
			if (currentSolution == null) {
				currentSolution = startsWith;
				currentFitness = function.valueAt(decoder.decode(currentSolution));
			}
			
			System.out.println("Current fitness: "  + currentFitness);
			while(tSchedule.getInnerLoopCounter() != -1) { // Inner loop
				double temp = tSchedule.getNextTermperature();
				T neighbour = neighbourhood.randomNeighbour(currentSolution);
				
				double[] decoded = decoder.decode(currentSolution);
//				System.out.println(
//						String.format("Temp: %.2f, Value: %.2f, Sol: %s", temp, currentFitness, 
//								Arrays.toString(decoded)));
				
				double nValue = function.valueAt(decoded);
				
				if (nValue <= currentFitness) {
					currentSolution = neighbour;
					currentFitness = nValue;
				} else {
					double probability = Math.exp(-(nValue - currentFitness) / temp);
					if (random.nextDouble() <= probability) {
						currentSolution = neighbour;
						currentFitness = nValue;
					}
				}
			}
		}
		
		System.out.println("Solution: " + Arrays.toString(decoder.decode(currentSolution)));
		
	}

}
