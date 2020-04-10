package hr.fer.zemris.optjava.dz6;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Ant System algorithm, travelling salesmen problem. Min - Max variation.
 * 
 * @author lmark
 *
 */
public class MMAS {

	private static Random random = new Random();
	
	// Ant system parameters.
	private double ro;
	private double alpha;
	private double beta;
	
	/**
	 * Nodes containing cities and candidate lists.
	 */
	private Node[] nodes;
	
	/**
	 * Distances between nodes. Distance from i to j.
	 */
	private double[][] distances;
	
	/**
	 * Ant pheronome trails. Trail from node i to j.
	 */
	private double[][] pheronomeTrails;
	
	/**
	 * (1 / distance) ^beta. From i to j node.
	 */
	private double[][] betaDistances;
	
	/**
	 * Population of ants.
	 */
	private Ant[] ants;
	
	/**
	 * Global best ant.
	 */
	private Ant globalBestAnt;
	
	/**
	 * Maximum number of iterations.
	 */
	private int maxIter;
	
	// Additional Min - Max variant parameters
	
	/**
	 * Maximum amount of feronomes.
	 */
	private double tauMax;
	
	/**
	 * Minimum amount of feronomes.
	 */
	private double tauMin;
	
	/**
	 * Constant determining tauMin = tauMax / a
	 */
	private double a;
	
	private int maxAnts;
	
	/**
	 * Constructor. Initializes all parameters.
	 * 
	 * @param nodes
	 * @param maxIter
	 * @param maxAnts
	 */
	public MMAS(List<Node> nodes, int maxIter, int maxAnts, double ro, double alpha, double beta) {
		this.maxIter = maxIter;
		this.alpha = alpha;
		this.beta = beta;
		this.ro = ro;
		this.maxAnts = maxAnts;
		
		// Initialize node array
		this.nodes = new Node[nodes.size()];
		nodes.toArray(this.nodes);
		
		// Initialize ants
		ants = new Ant[maxAnts];
		for (int i = 0; i < maxAnts; i++) {
			ants[i] = new Ant();
			ants[i].cityIndexes = new int[this.nodes.length];
		}
		
		// Find initial trail 1 / (ro * C_NN)
		double C_nn = greedySolution();
		double initialTrail = ants.length / (ro * C_nn);
		System.out.println("Initial trail: " + initialTrail);
		
		// Initialize distances
		distances = new double[this.nodes.length][this.nodes.length];
		betaDistances = new double[this.nodes.length][this.nodes.length];
		pheronomeTrails = new double[this.nodes.length][this.nodes.length];
		
		for (int i = 0; i < this.nodes.length; i++) {
			for (int j = i+1; j < this.nodes.length; j++) {
				
				double distance = euclideanDistanceNode(this.nodes[i], this.nodes[j]);
				
				distances[i][j] = distance;
				distances[j][i] = distance;
				
				betaDistances[i][j] = Math.pow(1 / distance, this.beta);
				betaDistances[j][i] = betaDistances[i][j];
				
				pheronomeTrails[i][j] = initialTrail;
				pheronomeTrails[j][i] = initialTrail;
			}
		}
		
		// Initialize TauMax / Min
		tauMax = initialTrail;
		double p = 0.9;
		double n = this.nodes[0].nodeCandidates.length; // Candidate count
		double mi = (n - 1) / ( n * (Math.pow(p, -1/n) - 1));
		a = mi * n;
		tauMin = tauMax / a;
		System.out.println("TauMax: " + tauMax + ", TauMin: " + tauMin);
		
		try {
			System.out.println("Press button to continue...");
			System.in.read();
		} catch (IOException e) {}
	}
	
	/**
	 * Start MMAS algorithm.
	 */
	public void run() {
		int k = 0;
		
		while (k < maxIter) {
			
			Ant iterationBest = null;
			// Walk all ants through grid 
			for (int i = 0; i < ants.length; i++) {
				ants[i] = generateSolution(ants[i]);
				
				if (iterationBest == null) {
					iterationBest = ants[i];
				} else if (iterationBest.tourLength > ants[i].tourLength) {
					iterationBest = ants[i];
				}
			}

			System.out.println("Iteration best: " + iterationBest);
			System.out.println("Global best: " + globalBestAnt);
			
			int c1 = updatePheronoms(iterationBest);
			int c2 = evaporatePheronoms();
			
			// Reset case if there is a lot of phereomones set to min / max value
			if ( (c1 + c2) >= nodes.length * nodes.length / 2.0 )  {
				System.out.println("Reset!");
				reset();
			}
			
			k++;
		} // End main loop
	}
	
	/**
	 * Pheromone reset.
	 */
	private void reset() {
		for (int i = 0; i < this.nodes.length; i++) {
			for (int j = i+1; j < this.nodes.length; j++) {
				
				pheronomeTrails[i][j] = tauMax;
				pheronomeTrails[j][i] = tauMax;
			}
		}
		
	}

	/**
	 * Evaporate pheronom trail.
	 */
	private int evaporatePheronoms() {
		int minCount = 0;
		
		for (int i = 0; i < nodes.length; i++) {
			for (int j = i + 1; j < nodes.length; j++) {
				
				pheronomeTrails[i][j] = pheronomeTrails[i][j] * (1 - ro);
				
				if (pheronomeTrails[i][j] < tauMin) {
					pheronomeTrails[i][j] = tauMin;
					minCount++;
				}
				
				pheronomeTrails[j][i] = pheronomeTrails[i][j];
			}
		}
		
		return minCount;
	}

	/**
	 * Update pheronom trail. 
	 * Update with iteration best or global best ant.
	 */
	private int updatePheronoms(Ant iterationBest) {
		double Ck = 0;
		Ant ant = null;
		int maxCount = 0;
		
		// If there are more than 100 nodes use global best ant, 
		// else use iteration best
		if (nodes.length >= 100) {
			Ck = maxAnts / (globalBestAnt.tourLength);
			ant = globalBestAnt;
		} else {
			Ck = maxAnts / (iterationBest.tourLength);
			ant = iterationBest;
		}
		
			
		for (int i = 0; i < ant.cityIndexes.length - 1; i++) {
			int i1 = ant.cityIndexes[i];
			int i2 = ant.cityIndexes[i+1];
	
			pheronomeTrails[i1][i2] += Ck;
			if (pheronomeTrails[i1][i2] > tauMax) {
				pheronomeTrails[i1][i2] = tauMax;
				maxCount++;
			}
			pheronomeTrails[i2][i1] = pheronomeTrails[i1][i2];	
		}
		
		// Update trail returning back
		int i1 = ant.cityIndexes[0];
		int i2 = ant.cityIndexes[ant.cityIndexes.length-1];
		pheronomeTrails[i1][i2] += Ck;
		if (pheronomeTrails[i1][i2] > tauMax) {
			pheronomeTrails[i1][i2] = tauMax;
		}
		pheronomeTrails[i2][i1] = pheronomeTrails[i1][i2];	
		
		return maxCount;
	}

	/**
	 * Walk through the grid using the given ant.
	 * 
	 * @param ant
	 * @return 
	 */
	private Ant generateSolution(Ant ant) {
		ant = new Ant();
		ant.cityIndexes = new int[nodes.length];
		for (int i = 0; i < ant.cityIndexes.length; i++) {
			ant.cityIndexes[i] = -1;
		}
		
		int[] availableIndexes = new int[nodes.length];
		
		// Initialize first city randomly
		ant.cityIndexes[0] = random.nextInt(nodes.length);
		availableIndexes[ant.cityIndexes[0]] = -1;
		
		// Calculate other ant steps
		for (int step = 1; step < nodes.length; step++) {
			
			// Extract reachable candidate node indexes
			int previousCityIndex = ant.cityIndexes[step-1];
			Node prevNode = nodes[previousCityIndex];
			int prevIndex = prevNode.root.ID - 1;
			int[] candidateIndexes = prevNode.candidateIndexes;
			
			// Construct candidate list
			List<Node> candidateList = new ArrayList<>();
			for (int cIndex = 0; cIndex < candidateIndexes.length; cIndex++) {
				
				if (availableIndexes[candidateIndexes[cIndex]] != -1) {
					candidateList.add(nodes[candidateIndexes[cIndex]]);
				}
			}
			
			// If candidate list is empty, add all remaining nodes to it
			if (candidateList.isEmpty()) {
				for (int i = 0; i < nodes.length; i++) {
					
					if (availableIndexes[nodes[i].root.ID-1] != -1) {
						candidateList.add(nodes[i]);
					}
				}
			}
			
			// Calculate probabilites for all available candidates
			double probSum = 0;
			double[] probabilities = new double[candidateList.size()];
			for (int i = 0; i < candidateList.size(); i++) {
				
				int currIndex = candidateList.get(i).root.ID - 1;
				probabilities[i] = Math.pow(pheronomeTrails[prevIndex][currIndex], alpha) * 
						betaDistances[prevIndex][currIndex];
				probSum += probabilities[i];
			}
			
			for (int i = 0; i < probabilities.length; i++) {
				probabilities[i] = probabilities[i] / probSum;
			}
			
			// Chose a candidate
			double percent = random.nextDouble();
			probSum = 0;
			int selectedIndex = -1;
			for (int i = 0; i < probabilities.length; i++) {
				probSum += probabilities[i];
				if (percent <= probSum) {
					selectedIndex = i;
					break;
				}
			}
			
			// Step to a new city
			ant.cityIndexes[step] = candidateList.get(selectedIndex).root.ID - 1;
			availableIndexes[ant.cityIndexes[step]] = -1;
		} // End step loop
		
		evaluateAnt(ant);
		
		// Update global best ant
		if (globalBestAnt == null || ant.tourLength < globalBestAnt.tourLength) {
			globalBestAnt = ant.duplicate();
			tauMax = 1 / (ro * globalBestAnt.tourLength);
			tauMin = tauMax / a;
			System.out.println("New TauMax: " + tauMax + ", new TauMin: " + tauMin);
		}
		
		return ant;
	}

	/**
	 * Evaluate ant.
	 * 
	 * @param ant
	 */
	private void evaluateAnt(Ant ant) {
		// Calculate euclidean distance
		double distance = 0;
		for (int i = 0; i < ant.cityIndexes.length; i++) {
			
			if (i == 0) {
				distance += euclideanDistanceNode(nodes[ant.cityIndexes[i]], nodes[ant.cityIndexes[ant.cityIndexes.length-1]]);
				continue;
			}
			
			distance += euclideanDistanceNode(nodes[ant.cityIndexes[i]], nodes[ant.cityIndexes[ant.cityIndexes[i-1]]]);
		}
		ant.tourLength = distance;
	}

	/**
	 * @param nodes
	 * @return Return length of a greedy solution.
	 */
	
	private double greedySolution() {
		List<City> cityList = new ArrayList<>();
		
		Node currentNode = this.nodes[random.nextInt(this.nodes.length)];
		cityList.add(currentNode.root);
		
		while(cityList.size() < this.nodes.length) {
			boolean added = false;
			
			// Try to add 1 from candidate list
			for (int i = 0; i < currentNode.nodeCandidates.length; i++) {
				
				// If list does not contain candidate city add it.
				if (!cityList.contains(currentNode.nodeCandidates[i].root)) {
					cityList.add(currentNode.nodeCandidates[i].root);
					currentNode = currentNode.nodeCandidates[i];
					added = true;
					break;
				}
			}
			
			// Continue if city is added
			if (added) {
				continue;
			}
			
			// If it didn't add any go through nodes and add first node available
			for (int i = 0; i < this.nodes.length; i++) {
				if (!cityList.contains(this.nodes[i].root)) {
					cityList.add(this.nodes[i].root);
					currentNode = this.nodes[i];
					break;
				}
			}
		}
		
		// Calculate euclidean distance
		double distance = 0;
		for (int i = 0; i < cityList.size(); i++) {
			
			if (i == 0) {
				distance += euclideanDistanceCity(cityList.get(i), cityList.get(cityList.size()-1));
				continue;
			}
			
			distance += euclideanDistanceCity(cityList.get(i), cityList.get(i-1));
		}
		
		// Initialize global best ant
//		globalBestAnt = new Ant();
//		globalBestAnt.cityIndexes = new int[this.nodes.length];
//		for (int i = 0; i < this.nodes.length; i++) {
//			globalBestAnt.cityIndexes[i] = cityList.get(i).ID - 1;
//		}
//		globalBestAnt.tourLength = distance;
		
		return distance;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return Returns euclidean distance from node a to b.
	 */
	private double euclideanDistanceNode(Node a, Node b) {
		return Math.sqrt(
				Math.pow(a.root.x - b.root.x, 2) +
				Math.pow(a.root.y - b.root.y, 2));
	}
	
	/**
	 * @param a
	 * @param b
	 * @return Returns euclidean distance between cities a and b.
	 */
	private double euclideanDistanceCity(City a, City b) {
		return Math.sqrt(
				Math.pow(a.x - b.x, 2) + 
				Math.pow(a.y - b.y, 2));
				
	}
}
