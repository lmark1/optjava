package hr.fer.zemris.otpjava.dz2;

import java.nio.file.Paths;
import java.util.List;

import Jama.Matrix;
import hr.fer.zemris.otpjava.dz2.functions.TransferError;

/**
 * For a given system characteristic:
 * y(x1, x2, x3, x4, x5) = a*x1 + b*x1^3*x2 + x*e^(d*x3) * (1 + cos(e*x4)) + f*x4*x5^2
 * parse file: 02-zad-prijenosna.txt and fit coeffs. (a,b,c,d,e,f)
 * 
 * Class contains a main method. Expects 3 command line arguments:
 * 1. argument - "grad" / "newton" (choose which optimiziation method to use)
 * 2. argument - max iterations
 * 3. argument - path to the system file, e.g. 02-zad-prijenosna.txt
 * 
 * 
 * @author lmark
 *
 */
public class Prijenosna {
	
	/**
	 * Main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("3 arguments expected..");
			return;
		}
		
		List<Matrix> xAndYMatrix = Sustav.parseFile(Paths.get(args[2])); 
		TransferError func = new TransferError(
				xAndYMatrix.get(0), 
				xAndYMatrix.get(1));
		
		int maxIter = Integer.parseInt(args[1]);
		
		List<Matrix> solList = null;
		switch (args[0]) {
		
			case "grad":
				// Gradient descent
				solList = NumOptAlgorithms.gradientDescent(func, maxIter, null);
				break;

			case "newton":
				solList = NumOptAlgorithms.Newton(func, maxIter, null);
				break;
				
			default:
				System.out.println("Unknown algorithm selected.");
				return;
		}
		
		Matrix solution = solList.get(solList.size()-1);
		System.out.println("L2 error: " + func.evaluate(solution).get(0, 0));
	}

}
