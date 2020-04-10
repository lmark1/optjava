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
public class TSelection implements ISelection<IntArraySolution>{
	
	private int n;
	
	public TSelection(int n) {
		this.n = n;
	}
	
	
	@Override
	public IntArraySolution selectParent(List<IntArraySolution> population,
			Random random) {
		
		IntArraySolution sol = null;
		for (int i = 0; i < n; i++) {
			
			IntArraySolution curr = population.get(random.nextInt(population.size()));
			
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
