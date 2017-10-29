package hr.fer.zemris.otpjava.dz2.functions;

import Jama.Matrix;

/**
 * Extension to the 1.a function with Hesse matrix.
 * f(x1, x2) = x1^2 + (x2-1)^2
 * 
 * @author lmark
 *
 */
public class Function1b extends Function1a implements IHFunction{

	@Override
	public Matrix getHesseAtPoint(Matrix x) {
		
		// [2, 0]
		// [0, 2]
		return Matrix.identity(2, 2).times(2);
	}

}
