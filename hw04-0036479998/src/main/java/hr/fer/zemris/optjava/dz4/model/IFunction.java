package hr.fer.zemris.optjava.dz4.model;

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
	double valueAt(double[] input);
}
