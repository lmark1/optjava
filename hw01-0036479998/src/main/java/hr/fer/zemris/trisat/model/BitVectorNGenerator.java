package hr.fer.zemris.trisat.model;

import java.util.Iterator;

/**
 * Generates a neighbourhood of bit vectors around the given root vector.
 * 
 * @author lmark
 *
 */
public class BitVectorNGenerator implements Iterable<MutableBitVector>{

	/**
	 * Root vecotr.
	 */
	private MutableBitVector root;
	
	public BitVectorNGenerator(BitVector assignment) {
		root = assignment.copy();
	}
	
	/**
	 * @return Returns an array of neighbourhood vectors around the root vector.
	 */
	public MutableBitVector[] createNeighbourhood() {
		MutableBitVector[] nVectors = new MutableBitVector[root.getSize()];
		
		for(int i = 0; i < root.getSize(); i++) {
			nVectors[i] = root.copy();
			nVectors[i].set(i, !root.get(i));
		}
		
		return nVectors;
	}
	
	@Override
	public Iterator<MutableBitVector> iterator() {
		MutableBitVector[] nVectors = this.createNeighbourhood();
		
		return new Iterator<MutableBitVector>() {
			
			int index = 0;
			
			@Override
			public MutableBitVector next() {
				return nVectors[index];
			}
			
			@Override
			public boolean hasNext() {
				index++;
				return index < nVectors.length;
			}
		};
	}

}
