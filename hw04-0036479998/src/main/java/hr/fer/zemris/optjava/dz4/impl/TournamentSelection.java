package hr.fer.zemris.optjava.dz4.impl;

import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz4.model.ISelection;

/**
 * Thid class implements tournament selection.
 * 
 * @author lmark
 *
 */
public class TournamentSelection implements ISelection<DoubleArraySolution>{
	
	private int n;
	
	public TournamentSelection(int n) {
		this.n = n;
	}
	
	@Override
	public DoubleArraySolution selectParent(
			List<DoubleArraySolution> population, Random random) {
		
		DoubleArraySolution sol = null;
		for (int i = 0; i < n; i++) {
			
			DoubleArraySolution curr = population.get(random.nextInt(population.size()));
			
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
