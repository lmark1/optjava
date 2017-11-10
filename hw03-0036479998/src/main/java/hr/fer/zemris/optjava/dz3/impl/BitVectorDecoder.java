package hr.fer.zemris.optjava.dz3.impl;

import hr.fer.zemris.optjava.dz3.model.IDecoder;

/**
 * Implements an abstract bitvector decoder.
 * 
 * @author lmark
 *
 */
public abstract class BitVectorDecoder implements IDecoder<BitVectorSolution>{
	
	/**
	 * Minimal values for each variable.
	 */
	protected double[] mins;
	
	/**
	 * Maximum values for each variable.
	 */
	protected double[] maxs;
	
	/**
	 * Bit count for each variable.
	 */
	protected int[] bits;
	
	/**
	 * Total bits.
	 */
	protected int totalBits;
	
	/**
	 * Number of variables coded into bits.
	 */
	protected int n;
	
	/**
	 * @param mins Minimum bit values;
	 * @param maxs Maximum bit values;
	 * @param bits 
	 * @param n Number of variables stored in bits.
	 */
	public BitVectorDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		this.mins = mins;
		this.maxs = maxs;
		this.bits = bits;
		
		// Calculate total bits
		for (int bitCount : bits) {
			this.totalBits += bitCount;
		}
		
		this.n = n;
	}
	
	/**
	 * Single variable decoder.
	 * 
	 * @param min
	 * @param max
	 * @param bit
	 * @param n
	 */
	public BitVectorDecoder(double min, double max, int bit, int n) {
		this(
				new double[] {min}, 
				new double[] {max}, 
				new int[] {bit}, 
				n
			);
	}
	
	
	/**
	 * @return Returns total bits.
	 */
	public int getTotalBits() {
		return totalBits;
	}
	
	/**
	 * @return Returns dimensions.
	 */
	public int getDimensions() {
		return n;
	}
	
	@Override
	public abstract double[] decode(BitVectorSolution solution);

	@Override
	public abstract void decode(BitVectorSolution solution, double[] decodedSolution);

}
