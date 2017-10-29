package hr.fer.zemris.otpjava.dz2.functions;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 * This class implements transfer function.
 * y(a, b, c, d, e, f) = a*x1 + b*x1^3*x2 + c*e^(d*x3) * (1 + cos(e*x4)) + f*x4*x5^2
 * 
 * @author lmark
 *
 */
public class TransferFunction implements IHFunction{

	private final int numberOfVariables = 6;
	
	/**
	 * X vector. (x1, x2, x3, x4, x5)^T.
	 */
	private Matrix xVector;
	
	public TransferFunction(Matrix xVector) {
		this.xVector = xVector.copy();
	}
	
	@Override
	public int getNumberOfVariables() {
		return numberOfVariables;
	}

	/**
	 * @param params Column vector containing parameters 
	 * 		(a, b, c, d, e, f)
	 */
	@Override
	public Matrix evaluate(Matrix params) {
		
		if (params.getColumnDimension() != 1 ||
				params.getRowDimension() != numberOfVariables) {
			throw new IllegalArgumentException(
					String.format("Vector parameters exptcted to be (%d, %d)",
							numberOfVariables, 1));
		}
		
		Matrix sol = new Matrix(1, 1);
		sol.set(0, 0, evaluateForVector(xVector, params));
		
		return sol;
	}

	/**
	 * Evaulate function for given x vector and params.
	 * 
	 * @param vector
	 * @param params
	 * @return
	 */
	private double evaluateForVector(Matrix x, Matrix params) {
		double x1 = x.get(0, 0);
		double x2 = x.get(1, 0);
		double x3 = x.get(2, 0);
		double x4 = x.get(3, 0);
		double x5 = x.get(4, 0);
		
		double a = params.get(0, 0);
		double b = params.get(1, 0);
		double c = params.get(2, 0);
		double d = params.get(3, 0);
		double e = params.get(4, 0);
		double f = params.get(5, 0);
		
		return a*x1 + 
				b * Math.pow(x1, 3) * x2 + 
				c*Math.exp(d*x3) * (1 + Math.cos(e*x4)) + 
				f*x4*Math.pow(x5, 2);
	}
	
	private Matrix evaulateGradientForVector(Matrix x, Matrix params) {
		double x1 = x.get(0, 0);
		double x2 = x.get(1, 0);
		double x3 = x.get(2, 0);
		double x4 = x.get(3, 0);
		double x5 = x.get(4, 0);
		
		double a = params.get(0, 0);
		double b = params.get(1, 0);
		double c = params.get(2, 0);
		double d = params.get(3, 0);
		double e = params.get(4, 0);
		double f = params.get(5, 0);
		
		Matrix grad = 	new Matrix(6, 1);
		
		grad.set(0, 0, x1);
		grad.set(1, 0, x1*x1*x1*x2);
		grad.set(2, 0, Math.exp(d*x3)*(Math.cos(e*x4) + 1));
		grad.set(3, 0, c*x3*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
		grad.set(4, 0, -c*x4*Math.exp(d*x3)*Math.sin(e*x4));
		grad.set(5, 0, x4*x5 * x5);
		
		return grad;
	}
	
	@Override
	public Matrix evaluateGradient(Matrix params) {
		
		if (params.getColumnDimension() != 1 ||
				params.getRowDimension() != numberOfVariables) {
			throw new IllegalArgumentException(
					String.format("Vector parameters expected to be (%d, %d)",
							numberOfVariables, 1));
		}
		
		return evaulateGradientForVector(xVector, params);
	}

	@Override
	public Matrix getHesseAtPoint(Matrix params) {
		if (params.getColumnDimension() != 1 ||
				params.getRowDimension() != numberOfVariables) {
			throw new IllegalArgumentException(
					String.format("Vector parameters expected to be (%d, %d)",
							numberOfVariables, 1));
		}
		
		
		Matrix hes = new Matrix(6, 6);
		
		double x1 = xVector.get(0, 0);
		double x2 = xVector.get(1, 0);
		double x3 = xVector.get(2, 0);
		double x4 = xVector.get(3, 0);
		double x5 = xVector.get(4, 0);
		
		double a = params.get(0, 0);
		double b = params.get(1, 0);
		double c = params.get(2, 0);
		double d = params.get(3, 0);
		double e = params.get(4, 0);
		double f = params.get(5, 0);
		
		hes.set(2, 3, x3*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
		hes.set(2, 4, -x4*Math.exp(d*x3)*Math.sin(e*x4));
		
		hes.set(3, 2, x3*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
		hes.set(3, 3, c*x3*x3*Math.exp(d*x3)*(Math.cos(e*x4) + 1));
		hes.set(3, 4, -c*x3*x4*Math.exp(d*x3)*Math.sin(e*x4));
		
		hes.set(4, 2, -x4*Math.exp(d*x3)*Math.sin(e*x4));
		hes.set(4, 3, -c*x3*x4*Math.exp(d*x3)*Math.sin(e*x4));
		hes.set(4, 4, -c*x4*x4*Math.exp(d*x3)*Math.cos(e*x4));
		
		return hes;
	}

}
