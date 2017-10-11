package hr.fer.zemris.trisat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.trisat.model.BitVector;
import hr.fer.zemris.trisat.model.BitVectorNGenerator;
import hr.fer.zemris.trisat.model.MutableBitVector;
import hr.fer.zemris.trisat.model.SATFormula;

/**
 * Iterative search algorithm.
 * Uses additional percentages of satisfied clauses as correction to the 
 * solution fitness function - it values more solution that satisfies rarely 
 * satisfied clauses.
 *  
 * @author lmark
 *
 */
public class SATIterateExtended extends SATIterate{

	private static final int numberOfBest = 10;
	private final Comparator<MutableBitVector> c = new Comparator<MutableBitVector>() {
		
		@Override
		public int compare(MutableBitVector o1, MutableBitVector o2) {
			double v1 = getFitness(o1);
			double v2 = getFitness(o2);
			
			if (v1 > v2) {
				return -1;
			} else if (v1 < v2){
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	public SATIterateExtended(SATFormula formula) {
		super(formula);
		setEnableStop(false);
	}
	
	@Override
	public double getFitness(BitVector solution) {
		getFormulaStats().setAssignment(solution, false);
		return getFormulaStats().getCorrectedFit();
	}
	
	@Override
	public List<MutableBitVector> getNeighbours(BitVector root) {
		// Generate a neighbourhood of solutions
		BitVectorNGenerator gen = new BitVectorNGenerator(root);
		
		// Find best neighbours
		Set<MutableBitVector> bestNeighbours = new TreeSet<>(c);
		
		// Add all neighbours to the set 
		for (MutableBitVector mutableBitVector : gen) {
			bestNeighbours.add(mutableBitVector);
		}
		
		// Pick n best neighbours
		List<MutableBitVector> nList = new ArrayList<>();
		for (MutableBitVector mutableBitVector : bestNeighbours) {
			if (nList.size() == numberOfBest) {
				break;
			}
			nList.add(mutableBitVector);
		}
		
		return nList;
	}
}
