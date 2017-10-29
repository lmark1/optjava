package hr.fer.zemris.otpjava.dz2.functions;

import Jama.Matrix;

/**
 * This class implements a function.
 * f(x) = 1/2 * (Ax - y)^T * (Ax - y)
 * 
 * where y and x are vectors and A is a matrix.
 * 
 * @author lmark
 *
 */
public class ErrorFunction implements IHFunction{

	/**
	 * A matrix.
	 */
	private Matrix A;
	
	/**
	 * y vector.
	 */
	private Matrix y;
	
	/**
	 * Number of variables.
	 */
	private int numberOfVariables;
	
	/**
	 * Constructor. Initializes matrix A.
	 * @param A
	 */
	public ErrorFunction(Matrix A, Matrix y) {
		this.A = A.copy();
		this.y = y;
		numberOfVariables = A.getRowDimension();
	}
	
	@Override
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public Matrix evaluate(Matrix x) {
		
		if (x.getColumnDimension() != 1 ||
				x.getRowDimension() != A.getRowDimension()) {
			throw new IllegalArgumentException(
					String.format("Vector x exptcted to be (%d, %d)",
							A.getRowDimension(), 1));
		}
		
		Matrix brackets = A.times(x).minus(y);
		
		return brackets.transpose().times(brackets).times(1./2.);
	}

	
	@Override
	public Matrix evaluateGradient(Matrix x) {
		
		if (x.getColumnDimension() != 1 ||
				x.getRowDimension() != A.getRowDimension()) {
			throw new IllegalArgumentException(
					String.format("Vector x exptcted to be (%d, %d)",
							A.getRowDimension(), 1));
		}
		
		// (x^T A^T - y^T) * A
		Matrix brackets = x.transpose().times(A.transpose()).minus(y.transpose());
		return brackets.times(A).transpose();
	}

	@Override
	public Matrix getHesseAtPoint(Matrix x) {
		return A.transpose().times(A);
	}
	

}
