package hr.fer.zemris.optjava.dz4.impl;

/**
 * This class represents a single solution used in optimization
 * algorithms.
 * 
 * @author lmark
 *
 */
public class SingleObjectiveSolution {

	/**
	 * Solution fitness.
	 */
	public double fitness;
	
	/**
	 * Solution value.
	 */
	public double value;
	
	public SingleObjectiveSolution() {
		//TODO Unimplemented
	}
	
	/**
	 * Compare this solution with the given solution.
	 * 
	 * @param solution
	 * @return
	 */
	public int compareTo(SingleObjectiveSolution solution) {
		if (this.fitness > solution.fitness) {
			return -1;
		} else if (this.fitness < solution.fitness) {
			return 1;
		} else {
			return 0;
		}
	}
}
