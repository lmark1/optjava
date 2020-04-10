package hr.fer.zemris.optjava.dz5.impl;

import hr.fer.zemris.optjava.dz5.model.IBitFunction;

/**
 * n - number of bits
 * k - number of ones
 * 
 * 1) k <= 0.8 * n - f(v) = k / n
 * 2) 0.8 * n < k < 0.9 * n - f(v) = 0.8
 * 3) k >= 0.9 * n - f(v) = (2k/n) - 1
 * 
 * @author lmark
 *
 */
public class BitFunction implements IBitFunction {

	@Override
	public double valueAt(BitVectorSolution input) {
		
		double n = input.values.length;
		double k = 0;
		
		for (byte b : input.values) {
			k += b == 1 ? 1 : 0;
		}
		
		if (k <= 0.8 * n) {
			return k/n;
		} else if (0.8 * n < k && k < 0.9 * n) {
			return 0.8;
		} else {
			return 2*k/n - 1;
		}
		
	}

}
