package hr.fer.zemris.optjava.dz8;

/**
 * Evaulator for neural networks.
 * 
 * @author lmark
 *
 */
public interface IEvaulator {

	/**
	 * Caluclate neural network error.
	 * 
	 * @param weights
	 */
	double calculateError(double[] weights);
	
	/**
	 * Get parameter count.
	 * 
	 * @return
	 */
	int getParamCount();
	
	void printErrors(double[] weights);
}
