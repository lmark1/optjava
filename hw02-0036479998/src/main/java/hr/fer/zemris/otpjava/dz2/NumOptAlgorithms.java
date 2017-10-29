package hr.fer.zemris.otpjava.dz2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Jama.Matrix;
import hr.fer.zemris.otpjava.dz2.functions.IFunction;
import hr.fer.zemris.otpjava.dz2.functions.IHFunction;

/**
 * Class containing numerical optimization algorithm
 * methods.
 * 
 * @author lmark
 *
 */
public class NumOptAlgorithms {

	/**
	 * Static random class.
	 */
	private static final Random r = new Random();
	
	private static final int RANGE_MIN = -5;
	private static final int RANGE_MAX = 5;
	private static final double TOLL = 10e-3;
	private static final int MAX_LAMBDA_ITER = 50;
	
	/**
	 * Static method implementing gradient descent function. Returns
	 * a minized solution after a maximum number of iterations.
	 * 
	 * @param function Function reference.
	 * @param maxIter Maximum number of iterations.
	 * @param initialSolution If null, initialSoution is generated from [-5, 5] range.
	 * @return argmin(func)
	 */
	public static List<Matrix> gradientDescent(
			IFunction function, int maxIter, Matrix initialSolution) {
		
		// Generate initial solution
		if (initialSolution == null) {
			initialSolution = generateInitialSolution(function);
		}
		
		int t = 0;
		List<Matrix> solutionList = new ArrayList<>();
		Matrix currentSolution = initialSolution;
		while (t < maxIter) {
			
			// Add solution to the list
			solutionList.add(currentSolution.copy());
			
			// If current solution is minimum then break
			Matrix currentGrad = function.evaluateGradient(currentSolution);
			if (Util.isZero(currentGrad, TOLL)) {
				break;
			}
			
			currentGrad.timesEquals(1.0 / currentGrad.norm2());
			
			// Get reverse gradient
			Matrix d = currentGrad.uminus();
			
			// Calculate lambda
			double lambda = getLambda(function, currentSolution, d);
			
			// Generate new solution
			currentSolution.plusEquals(d.times(lambda));
			System.out.println("New solution: " + 
					Util.MatrixToString(currentSolution.transpose()));
			
			// Get function value
			System.err.println("Function value: " + 
					Util.MatrixToString(function.evaluate(currentSolution)));
			
			t++;
		}
		
		return solutionList;
	}
	
	/**
	 * Newtown minimization method.
	 * 
	 * @param function Function reference.
	 * @param maxIter Maximum number of iterations.
	 * @param initialSolution If null, initialSoution is generated from [-5, 5] range.
	 * @return argmin(func)
	 */
	public static List<Matrix> Newton(
			IHFunction function, int maxIter, Matrix initialSolution) {

		// Generate initial solution
		if (initialSolution == null) {
			initialSolution = generateInitialSolution(function);
		}
		
		int t = 0;
		List<Matrix> solutionList = new ArrayList<>();
		Matrix currentSolution = initialSolution;
		while (t < maxIter) {
			// Add solution to the list
			solutionList.add(currentSolution.copy());
			
			// If current solution is minimum then break
			Matrix currentGrad = function.evaluateGradient(currentSolution);
			if (Util.isZero(currentGrad, TOLL)) {
				break;
			}
			
			// Calculate step
			Matrix d = function.getHesseAtPoint(currentSolution).inverse()
					.times(function.evaluateGradient(currentSolution)).uminus();
			
			d.timesEquals(1.0 / d.normInf());
			
			// Calculate lambda
			double lambda = getLambda(function, currentSolution, d);
			
			// Generate new solution
			currentSolution.plusEquals(d.times(lambda));
			System.out.println("New solution: " + 
					Util.MatrixToString(currentSolution.transpose()));
			
			// Get function value
			System.err.println("Function value: " + 
					Util.MatrixToString(function.evaluate(currentSolution)));
			
			t++;
		}
		
		return solutionList;
	}
	
	/**
	 * Get lambda which results in minimal function value:
	 * theta(lambda) = func(x + lambda*d) where d and x are fixed.
	 * 
	 * @param func Given function.
	 * @param x Fixed solution x.
	 * @param d Fixed vector pointitnig to the function minimum.
	 * @return Lambda.
	 */
	private static double getLambda(IFunction func, Matrix x, Matrix d) {
		
		// Find lambda low and lambda high
		int lambdaLower = 0;
		
		// Find lambda high 
		int lambdaUpper = 1;
		double dTheta;
		do{
			lambdaUpper++;
			Matrix xMoved = x.plus(d.times(lambdaUpper));
			dTheta = func.evaluateGradient(xMoved).transpose().times(d).get(0, 0);
		} while (dTheta <= 0);
		//System.out.println("Lambda high is " + lambdaHigh);
		
		// Bisection method
		double lambdaL = lambdaLower;
		double lambdaU = lambdaUpper;
		double currLambda = 0;
		double k = 0;
		while(k < MAX_LAMBDA_ITER){
			currLambda = (lambdaL + lambdaU) / 2.0;
			Matrix xMoved = x.plus(d.times(currLambda));
			dTheta = func.evaluateGradient(xMoved).transpose().times(d).get(0, 0);
			
			// If dtheta/dlambda = 0, stop we are at minimum
			if (Math.abs(dTheta) < TOLL) {
				break;
			}
			
			// ... else constrict lambda bounds 
			if (dTheta > 0) {
				lambdaU = currLambda;
			
			} else {
				lambdaL = currLambda;
			}
			
			k++;
		}
		
		return currLambda;
	}
	
	/**
	 * Generate a random initial solution from [-5, 5]
	 * @param function
	 * @return Initial solution.
	 */
	private static Matrix generateInitialSolution(IFunction function) {
		
		Matrix initialSolution = new Matrix(function.getNumberOfVariables(), 1);
		for (int i = 0; i < initialSolution.getRowDimension(); i++) {
			initialSolution.set(i, 0, RANGE_MIN + (RANGE_MAX - RANGE_MIN) * r.nextDouble());
		}
		
		System.out.println("Generated initial solution:\n" + Util.MatrixToString(initialSolution));
		return initialSolution;
	}
}
