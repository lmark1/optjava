package hr.fer.zemris.otpjava.dz2.functions;

import Jama.Matrix;

/**
 * This class implements a function: 
 * 
 * f(x1, x2) = x1^2 + (x2 - 1)^2
 * 
 * @author lmark
 *
 */
public class Function1a implements IFunction{

	/**
	 * Number of variables
	 */
	private final int numberOfVariables = 2;
	
	@Override
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public Matrix evaluate(Matrix x) {
		
		if (x.getRowDimension() != numberOfVariables) {
			throw new IllegalArgumentException(
					String.format("X length should be %d, found %d.",
							numberOfVariables, x.getRowDimension()));
		}
		
		double x1 = x.get(0, 0);
		double x2 = x.get(1, 0);
		
		return Matrix.identity(1, 1).times(x1*x1 + (x2 - 1) * (x2 - 1));
	}

	@Override
	public Matrix evaluateGradient(Matrix x) {
		
		if (x.getRowDimension() != numberOfVariables) {
			throw new IllegalArgumentException(
					String.format("X length should be %d, found %d.",
							numberOfVariables, x.getRowDimension()));
		}
		
		double x1 = x.get(0, 0);
		double x2 = x.get(1, 0);
		
		// df / dx1 = 2x1
		// df / dex2 = 2(x2-1) 
		double[][] grad = new double[2][1];
		grad[0][0] = 2*x1;
		grad[1][0] = 2*(x2-1);
		
		return new Matrix(grad);
	}

}
