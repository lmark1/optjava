package hr.fer.zemris.otpjava.dz2.functions;

import Jama.Matrix;

/**
 * Class implementation of the following function: 
 * f(x1, x2) = (x1 - 1)^2 + 10*(x2 - 2)^2
 * 
 * @author lmark
 *
 */
public class Function2a implements IFunction {
	
	/**
	 * Number of variables.
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
		
		return Matrix.identity(1, 1).times((x1-1)*(x1-1) + 10 * (x2 - 2) * (x2 - 2));
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
		
		// df/dx1 = 2(x1-1)
		// df/dx2 = 20(x2-2)
		double[][] grad = new double[2][1];
		grad[0][0] = 2*(x1-1);
		grad[1][0] = 20*(x2-2);
		
		return new Matrix(grad);
	}

}
