package hr.fer.zemris.optjava.dz8.nn;

/**
 * Sigmoid transfer function. 
 * y = 1 / ( 1 + e^-x)
 * 
 * @author lmark
 *
 */
public class SigmoidTransferFunction implements ITransferFunction {

	@Override
	public double valueAt(double input) {
		return 1.0 / ( 1 + Math.exp(-input));
	}

}
