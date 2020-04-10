package hr.fer.zemris.optjava.dz8.nn;

/**
 * Linear y = x transfer function.
 * 
 * @author lmark
 *
 */
public class LinearTransferFunction implements ITransferFunction {

	@Override
	public double valueAt(double input) {
		return input;
	}

}
