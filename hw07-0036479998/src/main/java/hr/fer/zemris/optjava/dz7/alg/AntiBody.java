package hr.fer.zemris.optjava.dz7.alg;

import java.util.Arrays;

public class AntiBody {

	public double[] solution;
	public double fitness = -10e10;
	public double afinity = 0;
	
	public AntiBody(int dim) {
		solution = new double[dim];
	}
	
	@Override
	public String toString() {
		return "Fit: " + fitness + " Sol: " + Arrays.toString(solution);
	}
	
	public AntiBody duplicate() {
		AntiBody duplicate = new AntiBody(this.solution.length);
		duplicate.fitness = this.fitness;
		duplicate.solution = this.solution.clone();
		duplicate.afinity = this.afinity;
		
		return duplicate;
	}
}
