package hr.fer.zemris.optjava.dz8;


import hr.fer.zemris.optjava.dz8.nn.TDNN;

/**
 * Evaluator for TDNN network.
 * 
 * @author lmark
 *
 */
public class TDNNEvaulator implements IEvaulator {

	private TDNN tdnn;
	
	public TDNNEvaulator(TDNN tdnn) {
		this.tdnn = tdnn;
	}
	
	@Override
	public double calculateError(double[] weights) {
		return tdnn.calculateTrainingError(weights, false, false, true);
	}

	@Override
	public int getParamCount() {
		return tdnn.getWeightsCount();
	}

	@Override
	public void printErrors(double[] weights) {
		double trainErr = tdnn.calculateTrainingError(weights, false, true, true);
		double testErr = tdnn.calculateTrainingError(weights, false, true, false);
		System.out.println("Training error: " + trainErr);
		System.out.println("Testing error: " + testErr);
	}

}
