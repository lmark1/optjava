package hr.fer.zemris.optjava.dz5.model;

import hr.fer.zemris.optjava.dz5.impl.BitVectorSolution;

/**
 * General function interface.
 * 
 * @author lmark
 *
 */
public interface IBitFunction {

	/**
	 * Evaulates a function at the given point.
	 * 
	 * @param input
	 * @return
	 */
	double valueAt(BitVectorSolution input);
}
