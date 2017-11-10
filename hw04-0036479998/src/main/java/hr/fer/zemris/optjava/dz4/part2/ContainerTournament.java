package hr.fer.zemris.optjava.dz4.part2;

import java.util.List;
import java.util.Random;

import hr.fer.zemris.optjava.dz4.model.ISelection;

public class ContainerTournament implements ISelection<Container>{

	private int n;
	private boolean best;
	
	public ContainerTournament(int n, boolean best) {
		this.n = n;
		this.best = best;
	}
	
	@Override
	public Container selectParent(List<Container> population, Random random) {
		
		int index = -1;
		for (int i = 0; i < n; i++) {
			int rand = random.nextInt(population.size());
			
			if (index == -1) {
				index = rand;
				continue;
			}
			
			if (best && population.get(index).fitness < population.get(rand).fitness) {
				index = rand;
			} else if (!best &&  population.get(index).fitness > population.get(rand).fitness) {
				index = rand;
			}
		}
		
		return population.get(index);
	}

}
