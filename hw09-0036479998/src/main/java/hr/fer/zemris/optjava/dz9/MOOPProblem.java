package hr.fer.zemris.optjava.dz9;

public interface MOOPProblem {

	int getNumberOfObjectives();
	void evaluateSolution(double[] solution, double[] objectives);

 }
