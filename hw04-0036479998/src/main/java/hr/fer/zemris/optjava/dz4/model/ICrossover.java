package hr.fer.zemris.optjava.dz4.model;

import java.util.Random;

public interface ICrossover<T> {
	
	/**
	 * Perform crossover on 2 parents.
	 * 
	 * @param parent1
	 * @param parent2
	 * @return Return 2 children.
	 */
	T[] cross(T parent1, T parent2, Random random);
}
