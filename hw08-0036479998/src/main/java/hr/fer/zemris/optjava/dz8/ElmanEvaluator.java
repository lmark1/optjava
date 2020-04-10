package hr.fer.zemris.optjava.dz8;

import hr.fer.zemris.optjava.dz8.nn.ElmanNN;

/**
 * Evaluator for Elman networks.
 * 
 * @author lmark
 *
 */
public class ElmanEvaluator implements IEvaulator {

	private ElmanNN enn;
	
	public ElmanEvaluator(ElmanNN enn) {
		this.enn = enn;
	}
	
	@Override
	public double calculateError(double[] weights) {
		return enn.calculateTrainingError(weights, false, false, true);
	}

	@Override
	public int getParamCount() {
		return enn.getParameterCount();
	}

	@Override
	public void printErrors(double[] weights) {
		double trainErr = enn.calculateTrainingError(weights, false, true, true);
		double testErr = enn.calculateTrainingError(weights, false, true, false);
		System.out.println("Training error: " + trainErr);
		System.out.println("Testing error: " + testErr);
	}

}
