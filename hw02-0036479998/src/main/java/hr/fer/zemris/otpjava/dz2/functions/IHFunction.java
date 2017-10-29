package hr.fer.zemris.otpjava.dz2.functions;

import Jama.Matrix;

/**
 * Extends basic IFunction interface with Hesse matrix getter.
 * 
 * @author lmark
 *
 */
public interface IHFunction extends IFunction {
	
	/**
	 * @param x 
	 * @return Returns a Hesse matrix evaulated at the given point.
	 */
	Matrix getHesseAtPoint(Matrix x);
}
