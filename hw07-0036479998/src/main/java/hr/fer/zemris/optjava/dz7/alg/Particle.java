package hr.fer.zemris.optjava.dz7.alg;

/**
 * Class representing a single particle.
 * 
 * @author lmark
 *
 */
public class Particle {

	/**
	 * Best solution particle has seen.
	 */
	double[] bestSolution;
	double bestFitness = -10e10;
	
	/**
	 * Current solution particle has
	 */
	double[] currentSolution;
	double currentFitness = 0;
	
	/**
	 * Global best solution;
	 */
	double[] gBestSolution;
	double gBestFitness = -10e10;
	
	/**
	 * Particle velocities.
	 */
	double[] velocity;
	
	/**
	 * Initializes particle fields.
	 * 
	 * @param n Dimension of solution vectors.
	 */
	public Particle(int n) {
		
		currentSolution = new double[n];
		velocity = new double[n];
		bestSolution = new double[n];
		gBestSolution = new double[n];
	}
	
	@Override
	public String toString() {
		return "Fitness: " + currentFitness;
	}
}
