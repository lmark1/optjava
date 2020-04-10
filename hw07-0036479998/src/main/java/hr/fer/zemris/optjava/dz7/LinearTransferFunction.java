package hr.fer.zemris.optjava.dz7;

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
