package hr.fer.zemris.optjava.dz7.alg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz7.FFANN;

/**
 * This class implements a clonal selection algorithm.
 * 
 * @author lmark
 *
 */
public class ClonAlg {

	private static Random random = new Random();
	private static int min = -5;
	private static int max = 5;
	private static double rho = 1.5;
	private static double mutationChance = 0.2;
	
	private FFANN ffann;
	private int maxIter;
	private int maxPop;
	private double beta;
	private double stopErr;
	
	public ClonAlg(FFANN ffann, int maxIter, int maxPop, double beta, double stopErr) {
		this.ffann = ffann;
		this.maxIter = maxIter;
		this.maxPop = maxPop;
		this.beta = beta;
		this.stopErr = stopErr;
	}
	
	public void run() {
		int k = 0;
		
		// Initialize antibodies
		int dimension = ffann.getWeightsCount();
		AntiBody[] antibodies = new AntiBody[maxPop];
		for (int i = 0; i < antibodies.length; i++) {
			antibodies[i] = new AntiBody(dimension);
			
			for (int j = 0; j < dimension; j++) {
				antibodies[i].solution[j] = random.nextDouble() * 
						(max - min) + min;
			}
		}
		
		long d = Math.round(maxPop * 0.05); 
		while ( k < maxIter ) {
			
			evaluateAntibodies(antibodies);
			AntiBody[] nBest = chooseAndSort(antibodies, maxPop);
			List<AntiBody> clones = generateClones(nBest, beta); 
			hyperMutate(clones);
			
			AntiBody[] cArr = new AntiBody[clones.size()];
			clones.toArray(cArr);
			
			evaluateAntibodies(cArr);
			AntiBody[] newBest = chooseAndSort(cArr, (int) (maxPop - d));
			AntiBody[] newBirth = generate((int) d);
			
			antibodies = new AntiBody[maxPop];
			for (int i = 0; i < newBest.length; i++) {
				antibodies[i] = newBest[i];
			}
			int offset = newBest.length;
			for (int i = 0; i < newBirth.length; i++) {
				antibodies[i + offset] = newBirth[i];
			}
			System.out.println(antibodies[0]);
			
			if (- antibodies[0].fitness < stopErr) {
				System.out.println("Error achieved");
				break;
			}
			
		} // End iterations
		
		System.out.println("Calculate error: " + 
				ffann.calculateTrainingError(antibodies[0].solution, false, true));
		System.out.println("Total datasets: " + 
				ffann.dataset.size());
		System.out.println("Zero - one error: " + 
				ffann.zeroOneError(antibodies[0].solution));
	}
	

	private AntiBody[] generate(int d) {
		int dimension = ffann.getWeightsCount();
		AntiBody[] antibodies = new AntiBody[d];
		for (int i = 0; i < antibodies.length; i++) {
			antibodies[i] = new AntiBody(dimension);
			
			for (int j = 0; j < dimension; j++) {
				antibodies[i].solution[j] = random.nextDouble() * 
						(max - min) + min;
			}
		}
		
		return antibodies;
	}

	private List<AntiBody> hyperMutate(List<AntiBody> clones) {
		int dimension = clones.get(0).solution.length;
		
		for (int i = 0; i < clones.size(); i++) {
			for (int j = 0; j < dimension; j++) {
				
				if (random.nextDouble() <= mutationChance) {
					
					double intensity = Math.exp(-rho * clones.get(i).afinity);
					clones.get(i).solution[j] += 
							intensity * (random.nextDouble()*2 - 1);
				}
			}
		}
		
		return null;
	}

	private List<AntiBody> generateClones(AntiBody[] antibodies, double beta) {
		
		List<AntiBody> clones = new ArrayList<>();
		for (int i = 0; i < antibodies.length; i++) {
			long cloneCount = Math.round(antibodies.length * beta / (i+1));
			antibodies[i].afinity = ((double)antibodies.length) / (i+1);
			
			for (int j = 0; j < cloneCount; j++) {
				clones.add(antibodies[i].duplicate());
			}
		}
		
		return clones;
	}
	
	private AntiBody[] chooseAndSort(AntiBody[] antibodies, int n) {
		
		Arrays.sort(antibodies, new Comparator<AntiBody>() {

			@Override
			public int compare(AntiBody o1, AntiBody o2) {
				if (o1.fitness > o2.fitness) {
					return -1;
				} else if (o1.fitness < o2.fitness) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		AntiBody[] nBest = new AntiBody[n];
		for (int i = 0; i < n; i++) {
			nBest[i] = antibodies[i].duplicate();
		}
		
		return nBest;
	}

	private void evaluateAntibodies(AntiBody[] antibodies) {
		for (int i = 0; i < antibodies.length; i++) {
			antibodies[i].fitness = 
					- ffann.calculateTrainingError(antibodies[i].solution, false, false);
		}
	}
}

