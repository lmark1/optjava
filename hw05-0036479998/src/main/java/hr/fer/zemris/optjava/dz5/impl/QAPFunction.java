package hr.fer.zemris.optjava.dz5.impl;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz5.model.IFunction;

/**
 * This class implements quadratic assignemnt probelm function.
 * 
 * @author lmark
 *
 */
public class QAPFunction implements IFunction {

	private Matrix d;
	private Matrix c;
	private int n;
	
	/**
	 * @param d Matrix specifiying distance from i to j location.
	 * @param c Matrix specifiying amount of resoures sent from i to j factory.
	 */
	public QAPFunction(Matrix d, Matrix c) {
		this.d = d;
		this.c = c;
		
		if (d.getRowDimension() != d.getColumnDimension() || 
				c.getRowDimension() != d.getRowDimension()) {
			throw new IllegalArgumentException("Matrix have to be quadratic");
		}
		
		if (d.getColumnDimension() != c.getColumnDimension()) {
			throw new IllegalArgumentException("Matrices have to be same order.");
		}
		
		this.n = d.getColumnDimension();
	}
	
	@Override
	public double valueAt(IntArraySolution input) {
		int[] p = input.values;
		double sum = 0;
		
		if (p.length != n) {
			throw new IllegalArgumentException(
					"Input length: " + p.length + ", wanted length: " + n);
		}
		for (int i = 0; i < d.getRowDimension(); i++) {
			for (int j = 0; j < d.getColumnDimension(); j++) {
				sum += c.get(i, j) * d.get(p[i], p[j]);			
			}
		}
		
		return sum;
	}

}
