package hr.fer.zemris.optjava.dz4.impl;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;
import hr.fer.zemris.optjava.dz4.model.IFunction;

/**
 * This class implements the following function: 
 * y(a, b, c, d, e, f) = a*x1 + b*x1^3*x2 + c*e^(d*x3) * (1 + cos(e*x4)) + f*x4*x5^2
 * 
 * Value of the function is LOSS ( minimizing loss).
 * @author lmark
 *
 */
public class SystemFunction implements IFunction {

	private List<Matrix> xVector = new ArrayList<>();
	private Matrix y;
	
	/**
	 * Initializes dataset matrices.
	 * 
	 * @param X
	 * @param y
	 */
	public SystemFunction(Matrix X, Matrix y) {
		this.y = y;
		

		// Get all x vectors from the matrix
		for (int i = 0, iMax = X.getRowDimension(); i < iMax; i++) {
			Matrix vector = new Matrix(iMax, 1);
			
			for (int j = 0, jMax = X.getColumnDimension(); j < jMax; j++) {
				vector.set(j, 0, X.get(i, j));
			}
			
			xVector.add(vector);
		}
	}
	
	@Override
	public double valueAt(double[] input) {
		
		if (input.length != 6) {
			throw new IllegalArgumentException("6 variables expected.");
		}
		
		// Calculate the total quadratic error.
		double sum = 0;
		for (int i = 0, maxLen = xVector.size(); i < maxLen; i++) {
			sum += Math.pow(evaluateActualFunction(input, xVector.get(i)) - y.get(i, 0), 2); 
		}
		
		return sum;
	}
	
	/**
	 * Evaulate actual f(params, x) 
	 * 
	 * @param params
	 * @param x
	 * @return
	 */
	private double evaluateActualFunction(double[] params, Matrix x) {
		double a = params[0];
		double b = params[1];
		double c = params[2];
		double d = params[3];
		double e = params[4];
		double f = params[5];
		
		double x1 = x.get(0, 0);
		double x2 = x.get(1, 0);
		double x3 = x.get(2, 0);
		double x4 = x.get(3, 0);
		double x5 = x.get(4, 0);
		
		//y(a, b, c, d, e, f) = a*x1 + b*x1^3*x2 + c*e^(d*x3) * (1 + cos(e*x4)) + f*x4*x5^2
		return a*x1 + b*x1*x1*x1*x2 + c*Math.exp(d*x3) * (1 + Math.cos(e*x4)) + f*x4*x5 * x5;
	}

}
