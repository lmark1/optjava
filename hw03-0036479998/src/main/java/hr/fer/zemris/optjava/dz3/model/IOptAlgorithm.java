package hr.fer.zemris.optjava.dz3.model;

/**
 * General interface for running optimization algorithm.
 * 
 * @author lmark
 * 
 * @param <T>
 *
 */
public interface IOptAlgorithm<T> {

	/**
	 * Starts the optimization algorithm.
	 */
	void run();
}
