package hr.fer.zemris.trisat.model;

import java.util.Random;

/**
 * This class represents a modifiable bit vector.
 * 
 * @author lmark
 *
 */
public class MutableBitVector extends BitVector {

	
	public MutableBitVector(boolean ...bits) {
		super(bits);
	}
	
	public MutableBitVector(int n, int numberOfBits) {
		super(n, numberOfBits);
	}
	
	public MutableBitVector(Random r, int numberOfVariables) {
		super(r, numberOfVariables);
	}

	public void set(int index, boolean value) {
		bitVector[index] = value;
	}
	
	public void flip(int index) {
		bitVector[index] = !bitVector[index];
	}
}
