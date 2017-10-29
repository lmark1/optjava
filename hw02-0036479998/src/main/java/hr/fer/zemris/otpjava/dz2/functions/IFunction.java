package hr.fer.zemris.otpjava.dz2.functions;

import Jama.Matrix;

/**
 * Interface modelling a basic function.
 * 
 * @author lmark
 *
 */
public interface IFunction {

	/**
	 * @return Returns a number of variables.
	 */
	int getNumberOfVariables();
	
	/**
	 * @param x 
	 * @return Function value evaulated at given point.
	 */
	Matrix evaluate(Matrix x);
	
	/**
	 * @param x
	 * @return Value of the gradient of the function evaluated at a given point.
	 */
	Matrix evaluateGradient(Matrix x);
}
