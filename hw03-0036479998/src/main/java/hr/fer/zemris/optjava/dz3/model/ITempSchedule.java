package hr.fer.zemris.optjava.dz3.model;

/**
 * Temperature schedule for the simulated annealing algorithm.
 * 
 * @author lmark
 *
 */
public interface ITempSchedule {

	/**
	 * @return Returns next temperature.
	 */
	double getNextTermperature();
	
	/**
	 * @return Returns inner loop count.
	 */
	int getInnerLoopCounter();
	
	/**
	 * @return Returns outer loop count.
	 */
	int getOuterLoopCounter();
}
