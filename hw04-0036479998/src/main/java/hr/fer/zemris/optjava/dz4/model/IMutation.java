package hr.fer.zemris.optjava.dz4.model;

import java.util.Random;

/**
 * Mutation interface. 
 * 
 * @author lmark
 *
 * @param <T>
 */
public interface IMutation<T> {

	/**
	 * Mutate a single solution.
	 * 
	 * @param single
	 */
	void mutate(T single, Random random);
}
