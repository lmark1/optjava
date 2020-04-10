package hr.fer.zemris.optjava.dz9;


public interface IFunction {

	double[] valueAt(double[] point);
	int dimOfInput();
	int dimOfOutput();
}
