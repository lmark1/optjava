package hr.fer.zemris.optjava.dz5.model;

import hr.fer.zemris.optjava.dz5.impl.IntArraySolution;

/**
 * General function interface.
 * 
 * @author lmark
 *
 */
public interface IFunction {

	/**
	 * Evaulates a function at the given point.
	 * 
	 * @param input
	 * @return
	 */
	double valueAt(IntArraySolution input);
}
