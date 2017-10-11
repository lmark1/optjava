package hr.fer.zemris.trisat.model;

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
	
	public void set(int index, boolean value) {
		bitVector[index] = value;
	}
}
