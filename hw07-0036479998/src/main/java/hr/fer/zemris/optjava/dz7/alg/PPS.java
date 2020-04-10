package hr.fer.zemris.optjava.dz7.alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.FFANN;

/**
 * Particle swarm optimization.
 * 
 * @author lmark
 *
 */
public class PPS {

	private static Random random = new Random();
	
	private static int xMin = -5;
	private static int xMax = 5;
	private static int vMin = -5;
	private static int vMax = 5;
	
	/**
	 * Maximum number of iterations.
	 */
	private int maxIter;
	
	/**
	 * Particle population count.
	 */
	private int populationSize;
	
	/**
	 * Acceptable convergence error.
	 */
	private double acceptableError;
	
	/**
	 * Radius used for local 
	 */
	private double d;
	
	/**
	 * Artificial neural network.
	 */
	private FFANN ffan;
		
	private double[] gBest_solution;
	private double gBest_fit = -10e10;
	
	public PPS(
			int maxIter, 
			int populationSize, 
			double acceptableError, 
			double d, 
			FFANN ffan) {
		
		this.maxIter = maxIter;
		this.populationSize = populationSize;
		this.acceptableError = acceptableError;
		this.d = d;
		this.ffan = ffan;
	}
	
	/**
	 * Run Particle optimization.
	 * 
	 * @param C1
	 * @param C2
	 */
	public void run(int C1, int C2) {
		Particle[] particles = new Particle[this.populationSize];
		int dimension = ffan.getWeightsCount();
		
		// Generate particle population
		for (int i = 0; i < this.populationSize; i++) {
			particles[i] = new Particle(dimension);
			
			// Initialize solutions and velocities
			for (int j = 0; j < dimension; j++) {
				
				particles[i].currentSolution[j] = random.nextDouble() * 
						(xMax - xMin) + xMin;
				particles[i].velocity[j] = random.nextDouble() * 
						(vMax - vMin) + vMin;
			}
		}
		
		
		// Start iterations
		int k = 0;
		double w_t = maxIter;
		double T = maxIter / 2.0;
		while ( k < maxIter ) {
			
			// Evaluate population
			for (int i = 0; i < this.populationSize; i++) {
				particles[i].currentFitness = 
						- ffan.calculateTrainingError(
								particles[i].currentSolution, false, false);
			}
			
			// Get local best solution
			for (int i = 0; i < this.populationSize; i++) {
				
				if (particles[i].currentFitness > particles[i].bestFitness) {
					particles[i].bestFitness = particles[i].currentFitness;
					particles[i].bestSolution = particles[i].currentSolution;
				}
				
				if (particles[i].currentFitness > gBest_fit) {
					gBest_fit = particles[i].currentFitness;
					gBest_solution = particles[i].currentSolution;
				}
			}
			
			// Global Neighbourhood
			if (d == -1) {
				// Set global best
				for (int i = 0; i < this.populationSize; i++) {
					particles[i].gBestSolution = gBest_solution;
					particles[i].gBestFitness = gBest_fit;
				}
			} else {
				// Local neighbourhood
				for (int i = 0; i < this.populationSize; i++) {
					Particle bestNearest = kNearest(d, particles[i], particles);
					particles[i].gBestSolution = bestNearest.currentSolution;
					particles[i].gBestFitness = bestNearest.currentFitness;
				}
			}
			
			System.out.println("Iter: " + k + " Best: " + gBest_fit);
			
			// Update swarm velocities
			for (int i = 0; i < this.populationSize; i++) {
				for (int j = 0; j < dimension; j++) {
					
					// Calculate new velocity
					particles[i].velocity[j] = 
							particles[i].velocity[j] * w_t / T +
							C1 * random.nextDouble() * (
									particles[i].bestSolution[j] - particles[i].currentSolution[j]) +
							C2 * random.nextDouble() * (
									gBest_solution[j] - particles[i].currentSolution[j]);
					
					// Scale velocities from 
//					particles[i].velocity[j] = 
//							fromRange(particles[i].velocity[j], vMin, vMax); 
					
					// Update particle position
					particles[i].currentSolution[j] = 
							particles[i].currentSolution[j] + particles[i].velocity[j];
				}
			}
			
			k++;
			w_t--;
			
			double currentError = ffan.calculateTrainingError(gBest_solution, false, false);
			if (acceptableError >= currentError) {
				System.out.println("Error satisfied, solution found.");
				break;
			}
			
		} // End iterations
		
		System.out.println("Calculate error: " + 
				ffan.calculateTrainingError(gBest_solution, false, true));
		System.out.println("Total datasets: " + 
				ffan.dataset.size());
		System.out.println("Zero - one error: " + 
				ffan.zeroOneError(gBest_solution));
	}


	/**
	 * @param d2
	 * @param particle
	 * @param particles
	 * @return Return nearest best particle.
	 */
	private Particle kNearest(double k, Particle particle,
			Particle[] particles) {
		
		List<Particle> kNearest = new ArrayList<>();
		for (int i = 0; i < particles.length; i++) {
			Particle newParticle = new Particle(1);
			newParticle.bestFitness = L2Distance(particle, particles[i]);
			newParticle.currentSolution = particles[i].currentSolution.clone();
			newParticle.currentFitness = particles[i].currentFitness;
			
			boolean inserted = false;
			for (int j = 0; j < kNearest.size(); j++) {
				if (kNearest.get(j).bestFitness > newParticle.bestFitness) {
					kNearest.add(j, newParticle);
					inserted = true;
					break;
				}
			}
			
			if (!inserted) {
				kNearest.add(newParticle);
			}
			
			if (kNearest.size() == k) {
				break;
			}
		}
		
		// Find best nearest
		double best = -10e10;
		int bestIndex = -1;
		for (int i = 1; i < kNearest.size(); i++) {
			if (kNearest.get(i).currentFitness > best) {
				bestIndex = i;
				best = kNearest.get(i).currentFitness;
			}
		}
		
		return kNearest.get(bestIndex);
	}

	/**
	 * @param p1
	 * @param p2
	 * @return Returns L2 distance betweens p1, p2.
	 */
	private double L2Distance(Particle p1, Particle p2) {
		
		double distance = 0;
		for (int i = 0; i < p1.currentSolution.length; i++) {
			distance +=
					Math.pow(p1.currentSolution[i] - p2.currentSolution[i], 2);
		}
		return Math.sqrt(distance);
	}
	
	/**
	 * @param input
	 * @param min
	 * @param max
	 * @return Return min if input is < min, or max if input is > max.
	 */
	private double fromRange(double input, int min, int max) {
		if (input > max) {
			return max;
		} else if (input < min) {
			return min;
		} else {
			return input;
		}
	}
}
