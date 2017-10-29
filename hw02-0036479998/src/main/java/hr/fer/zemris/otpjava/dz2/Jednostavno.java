package hr.fer.zemris.otpjava.dz2;

import java.nio.file.Paths;
import java.util.List;

import Jama.Matrix;
import hr.fer.zemris.otpjava.dz2.functions.Function1a;
import hr.fer.zemris.otpjava.dz2.functions.Function1b;
import hr.fer.zemris.otpjava.dz2.functions.Function2a;
import hr.fer.zemris.otpjava.dz2.functions.Function2b;

/**
 * Main class. Expects 2 or 4 arguments:
 * 
 * 1. argument - 1a, 1b, 2a, 2b (Number of homework task)
 * 2. argument - maximum number of iterations
 * 3. / 4. argument - if available, should be the starting point for 
 * 		optimization function. If not the point is randomly generated.
 * 
 * @author lmark
 *
 */
public class Jednostavno {
	
	/**
	 * Main class method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 2 && args.length != 4) {
			System.out.println("2 or 4 arguments expected...");
			return;
		}
		
		// Get max iterations
		int maxIter = Integer.valueOf(args[1]);
		
		// Get initial solution
		Matrix initialSolution = null;
		try{
			if (args.length == 4) {
				initialSolution = new Matrix(2,1);
				initialSolution.set(0, 0, Double.valueOf(args[2])); 
				initialSolution.set(1, 0, Double.valueOf(args[3]));
				System.out.println("Initial solution found:" 
						+ Util.MatrixToString(initialSolution.transpose()));
			}
		} catch (NumberFormatException e) {
			System.out.println("3. and 4. argument should be initial solution.");
			return;
		}
		
		// Check algorithm
		List<Matrix> solList = null;
		String algorithm = args[0];
		String chartTitle = null;
		String sPath = "./src/main/resources/";
		switch (algorithm) {
		
			case "1a":
				// Calculate gradient descent
				Function1a f1a = new Function1a();
				solList = NumOptAlgorithms.gradientDescent(f1a, maxIter, initialSolution);
				System.out.println(
						"Minimum argument found: " 
						+ Util.MatrixToString(
								solList.get(solList.size()-1).transpose()));
				
				chartTitle = "GradientDescent";
				break;

			case "1b":
				// Calculate newton method
				Function1b f1b = new Function1b();
				solList = NumOptAlgorithms.Newton(f1b, maxIter, initialSolution);
				System.out.println(
						"Minimum argument found: " 
						+ Util.MatrixToString(
								solList.get(solList.size()-1).transpose()));
				
				chartTitle = "NewtonMethod";
				break;
			
			case "2a":
				// Calculate gradient descent
				Function2a f2a = new Function2a();
				solList = NumOptAlgorithms.gradientDescent(f2a, maxIter, initialSolution);
				System.out.println(
						"Minimum argument found: " 
						+ Util.MatrixToString(
								solList.get(solList.size()-1).transpose()));
				
				chartTitle = "GradientDescent";
				break;
			
			case "2b":
				// Calculate newton method
				Function2b f2b = new Function2b();
				solList = NumOptAlgorithms.Newton(f2b, maxIter, initialSolution);
				System.out.println(
						"Minimum argument found: " 
						+ Util.MatrixToString(
								solList.get(solList.size()-1).transpose()));
				
				chartTitle = "NewtonMethod";
				break;
			
			default:
				System.out.println("Unknown algorithm argument found");
				return;
		}
		
		sPath += algorithm + "_" + chartTitle + ".png";
		Util.generateGraph(solList, algorithm + " - " + chartTitle, Paths.get(sPath));
	}
	
	
	
}
