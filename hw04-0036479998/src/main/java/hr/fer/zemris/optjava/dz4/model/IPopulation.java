package hr.fer.zemris.optjava.dz4.model;

import java.util.List;

/**
 * Population handling interface.
 * 
 * @author lmark
 *
 * @param <T>
 */
public interface IPopulation<T> {

	/**
	 * @return List of new random population
	 */
	List<T> generateNewPopulation();
	
	/**
	 * Evaluate current population.
	 * 
	 * @param population
	 */
	void evaluatePopulation(List<T> population);
	
	/**
	 * Return n best singles from population.
	 * 
	 * @param population
	 * @param n
	 * @return
	 */
	List<T> getNBest(List<T> population, int n);
	
	/**
	 * @return Returns population size
	 */
	int getPopulationSize();
	
	/**
	 * @param bestSingle
	 * @param epsilon
	 * @return True if single is good enough, false otherwise.
	 */
	boolean isSatisfied(T bestSingle, double epsilon);
	
}
