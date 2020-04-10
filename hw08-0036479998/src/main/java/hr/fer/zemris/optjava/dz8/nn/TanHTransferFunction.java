package hr.fer.zemris.optjava.dz8.nn;

/**
 * Transfer function modeled as tanh(x)
 * 
 * @author lmark
 *
 */
public class TanHTransferFunction implements ITransferFunction {

	@Override
	public double valueAt(double input) {
		return (1 - Math.exp(-input)) / (1 + Math.exp(-input));
	}

}
