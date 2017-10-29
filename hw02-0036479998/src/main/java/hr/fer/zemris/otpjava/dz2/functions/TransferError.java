package hr.fer.zemris.otpjava.dz2.functions;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 * Implements error function
 * f(theta) = 1/2 * ( f(theta;X) - y ) ^2.
 * 
 * @author lmark
 *
 */
public class TransferError implements IHFunction {

	/**
	 * List of transfer functions for all examples
	 */
	List<TransferFunction> tfList = new ArrayList<>();
	private Matrix X;
	private Matrix y;
	private final int numberOfVariables = 6;
	
	public TransferError(Matrix X, Matrix y) {
		this.X = X;
		this.y = y;
		
		// Get all x vectors from the matrix
		for (int i = 0, iMax = X.getRowDimension(); i < iMax; i++) {
			Matrix vector = new Matrix(iMax, 1);
			
			for (int j = 0, jMax = X.getColumnDimension(); j < jMax; j++) {
				vector.set(j, 0, X.get(i, j));
			}
			
			// Generate new transfer function with different x vector
			tfList.add(new TransferFunction(vector));
		}
	}
	
	@Override
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	@Override
	public Matrix evaluate(Matrix params) {
		double error = 0;
		
		for (int i = 0, len = tfList.size(); i < len; i++) {
			error += Math.pow(tfList.get(i).evaluate(params).get(0, 0) - y.get(i, 0), 2);
		}
		
		Matrix eMat = new Matrix(1,1);
		eMat.set(0, 0, error/2.0);
		
		return eMat;
	}

	@Override
	public Matrix evaluateGradient(Matrix params) {
		Matrix grad = new Matrix(getNumberOfVariables(), 1);
		
		// (yHat_i - y_i) * grad(f) 
		for (int i =0, len = tfList.size(); i < len; i++) {
			TransferFunction tf = tfList.get(i);
			
			Matrix currGrad = tf.evaluateGradient(params);
			double yHat = tf.evaluate(params).get(0, 0);
			double yCurr = y.get(i, 0);
			
			grad.plusEquals(currGrad.times(yCurr-yHat));
		}
		
		// Norm gradient - only direction is needed
		grad.timesEquals(1.0);
		
		// Average the gradient over all inputs
		return grad.uminus();
	}

	@Override
	public Matrix getHesseAtPoint(Matrix params) {
		Matrix hess = new Matrix(getNumberOfVariables(), getNumberOfVariables());
		
		for (int i = 0, len = tfList.size(); i < len; i++) {
			TransferFunction tf = tfList.get(i);
			
			hess.plusEquals(
					tf.getHesseAtPoint(params)
					.times(tf.evaluate(params).get(0, 0) - y.get(i, 0)));
			
			hess.plusEquals(
					tf.evaluateGradient(params)
					.times(tf.evaluateGradient(params).transpose()));
		}
		
		return hess.times(1.0 / getNumberOfVariables());
	}

}
