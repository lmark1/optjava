package hr.fer.zemris.trisat.model;

import java.util.Random;

/**
 * This class represents a read-only bit vector. 
 * 
 * @author lmark
 *
 */
public class BitVector {
	
	/**
	 * Internal boolean array.
	 */
	protected boolean[] bitVector;
	
	/**
	 * Initializes a random bit vector. 
	 * 
	 * @param rand
	 * @param numberOfBits
	 */
	public BitVector(Random rand, int numberOfBits) {
		bitVector = new boolean[numberOfBits];
		
		for(int i = 0; i < numberOfBits; i++) {
			bitVector[i] = rand.nextBoolean();
		}
	}
	
	/**
	 * Initializes the bit vector using given boolean values.
	 * 
	 * @param bits
	 */
	public BitVector(boolean ... bits) {
		bitVector = new boolean[bits.length]; 
		
		for(int i = 0; i < bits.length; i++) {
			bitVector[i] = bits[i];
		}
	}
	
	/**
	 * Initializes a bit vector representing decimal number
	 * n with a specified number of bits.
	 * 
	 * @param n
	 * @param numberOfBits
	 */
	public BitVector(int n, int numberOfBits) {
		bitVector = new boolean[numberOfBits];
		
		int index = numberOfBits-1;
		while(n > 0) {
			bitVector[index] = n % 2 == 0 ? false : true;
			n = n/2;
			index--;
		}
	}
	
	/**
	 * @param index
	 * @return Returns vector value at given position.
	 */
	public boolean get(int index) {
		
		if (index >= bitVector.length) {
			throw new IllegalArgumentException(
					"Index " + index + " out of range in vector: " + toString());
		}
		
		return bitVector[index];
	}
	
	/**
	 * @return Returns size of the vector.
	 */
	public int getSize() {
		return bitVector.length;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	
		sb.append("(");
		for(int i = 0; i < bitVector.length; i++) {
			sb.append(bitVector[i] ? "1" : "0");
			
			if ( i < bitVector.length - 1) {
				sb.append(", ");
			}
		}
		sb.append(")");
		
		return sb.toString();
	}
	
	/**
	 * @return Returns a copy of the current vector as a mutable 
	 */
	public MutableBitVector copy() {
		return new MutableBitVector(bitVector);
	}
}
