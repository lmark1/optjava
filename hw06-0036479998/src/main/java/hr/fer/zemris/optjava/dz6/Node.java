package hr.fer.zemris.optjava.dz6;

/**
 * This class represents a node in a TSP graph. And its candidates.
 * 
 * @author lmark
 *
 */
public class Node {
	
	public City root;
	public City[] candidates;
	public Node[] nodeCandidates;
	public int[] candidateIndexes;
	
	public Node(City root, City[] candidates) {
		this.root = root;
		this.candidates = candidates;
		this.candidateIndexes = new int[candidates.length];
		
		for (int i = 0; i < candidates.length; i++) {
			this.candidateIndexes[i] = candidates[i].ID - 1;
		}
	}
}
