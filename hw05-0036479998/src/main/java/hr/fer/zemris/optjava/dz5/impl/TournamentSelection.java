package hr.fer.zemris.optjava.dz5.impl;

import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz5.model.ISelection;


/**
 * Thid class implements tournament selection.
 * 
 * @author lmark
 *
 */
public class TournamentSelection implements ISelection<BitVectorSolution>{
	
	private int n;
	
	public TournamentSelection(int n) {
		this.n = n;
	}
	
	@Override
	public BitVectorSolution selectParent(
			List<BitVectorSolution> population, Random random) {
		
		BitVectorSolution sol = null;
		for (int i = 0; i < n; i++) {
			
			BitVectorSolution curr = population.get(random.nextInt(population.size()));
			
			if (sol == null) {
				sol = curr;
				continue;
			}
			
			if (curr.fitness > sol.fitness) {
				sol = curr;
			}
		}
			
		return sol;
	}

}
