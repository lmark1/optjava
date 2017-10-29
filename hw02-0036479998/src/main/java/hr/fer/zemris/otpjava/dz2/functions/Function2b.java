package hr.fer.zemris.otpjava.dz2.functions;

import Jama.Matrix;

/**
 * Extends implemented function. Adds hessian matrix.
 * f(x1, x2) = (x1 - 1)^2 + 10*(x2 - 2)^2
 * 
 * @author lmark
 *
 */
public class Function2b extends Function2a implements IHFunction{

	@Override
	public Matrix getHesseAtPoint(Matrix x) {
		
		//[2, 0]
		//[0, 20]
		double[][] hesse = new double[2][2];
		hesse[0][0] = 2;
		hesse[1][1] = 20;
		
		return new Matrix(hesse);
	}
	

}
