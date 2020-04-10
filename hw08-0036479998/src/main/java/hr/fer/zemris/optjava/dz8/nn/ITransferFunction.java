package hr.fer.zemris.optjava.dz8.nn;

/**
 * Interface implementing a transfer function.
 * 
 * @author lmark
 *
 */
public interface ITransferFunction {
	
	/**
	 * @param input
	 * @return Returns transfer function value at given input.
	 */
	double valueAt(double input);
}
