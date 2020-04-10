package hr.fer.zemris.optjava.dz5.model;

import java.util.List;
import java.util.Random;

/**
 * Interface used for implementing selection algorithms.
 * 
 * @author lmark
 *
 */
public interface ISelection<T> {

	/**
	 * @param population
	 * @return Given population, returns a parent based on some 
	 * 		selection algorithm.
	 */
	T selectParent(List<T> population, Random random);
}
