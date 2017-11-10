package hr.fer.zemris.optjava.dz3.impl;

import hr.fer.zemris.optjava.dz3.model.IDecoder;

/**
 * Concrete implementation of a pass through decoder.
 * 
 * @author lmark
 *
 */
public class PassThroughDecoder implements IDecoder<DoubleArraySolution>{

	@Override
	public double[] decode(DoubleArraySolution solution) {
		return solution.values;
	}

	@Override
	public void decode(DoubleArraySolution solution, double[] values) {
		solution.values = values.clone();
	}

}
