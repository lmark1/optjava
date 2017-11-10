package hr.fer.zemris.optjava.dz3.impl;

/**
 * This class extends a general decoder and implements a 
 * natural binary number decoder.
 * 
 * @author lmark
 *
 */
public class NaturalBinaryDecoder extends BitVectorDecoder {

	public NaturalBinaryDecoder(double[] mins, double[] maxs, int[] bits, int n) {
		super(mins, maxs, bits, n);
	}
	
	public NaturalBinaryDecoder(double min, double max, int bit, int n) {
		super(min, max, bit, n);
	}

	@Override
	public double[] decode(BitVectorSolution solution) {
		double[] decoded = new double[n];
		
		int bitIndex = 0;
		int variableIndex = n-1;
		for (int bitLength : bits) {
			
			// Extract bit values for this variable
			byte[] varBits = new byte[bitLength];
			for (int i = bitIndex, maxLen = bitIndex + bitLength; i < maxLen; i++) {
				varBits[i - bitIndex] = solution.values[i];
			}
			bitIndex += bitLength;
			
			// Convert and map
			int k = bitsToInt(varBits);
			double doubleValue = mapToDouble(
					k, 
					maxs[variableIndex], 
					mins[variableIndex], 
					bits[variableIndex]);
			
			// Store to decoded array
			decoded[variableIndex] = doubleValue;
			variableIndex--;
		}
		
		return decoded;
	}

	@Override
	public void decode(BitVectorSolution solution, double[] values) {
		
		int bitIndex = solution.values.length - 1;
		for (int doubleIndex = 0, maxLen = values.length; doubleIndex < maxLen; doubleIndex++) {
			
			int k = unmapToInt(
					values[doubleIndex], 
					maxs[doubleIndex], 
					mins[doubleIndex], 
					bits[doubleIndex]);
			
			byte[] bitValues = intToBits(k, bits[doubleIndex]);
			
			// Copy bits to solution
			int helperIndex = bitValues.length - 1;
			for (int i = bitIndex, minLen = bitIndex - bits[doubleIndex]; i > minLen; i--) {
				solution.values[i] = bitValues[helperIndex];
				helperIndex--;
			}
			bitIndex -= bits[doubleIndex];
		}
	}
	

	/**
	 * @param bits
	 * @return Returns int representation of given bits.
	 */
	private int bitsToInt(byte[] bits) {
		int value = 0;
		
		int maxIndex = bits.length - 1;
		for(int i = maxIndex; i >= 0; i--) {
			value += bits[i] * Math.pow(2, maxIndex - i);
		}
		
		return value;
	}
	
	/**
	 * Convert given number to bits.
	 * 
	 * @param number
	 * @param n
	 * @return
	 */
	private byte[] intToBits(int number, int n) {
		byte[] bits = new byte[n];
		
		String bitString = Integer.toBinaryString(number);
		while (bitString.length() < n) {
			bitString = "0" + bitString;
		}
		
		for(int i = 0, maxLen = bits.length; i < maxLen; i++) {
			bits[i] = (byte) (bitString.charAt(i) == 1 ? 1 : 0);
		}
		
		return bits;
	}
	
	/**
	 * Maps k to range [min, max]
	 * 
	 * @param k Integer we are mapping.
	 * @param max Maximum value.
	 * @param min Minimum value.
	 * @param n Number of bits we are mapping from.
	 * @return Mapped double value.
	 */
	private double mapToDouble(int k, double max, double min, int n) {
		return min + ((double)k)/(Math.pow(2, n)-1) * (max - min);
	}
	
	/**
	 * Unmaps x from range [min, max] to int value [0, 2^n -1]
	 * 
	 * @param x Double value we are unmaping.
	 * @param max Maximum value.
	 * @param min Minimum value.
	 * @param n Number of bits.
	 * @return Unmapped int value.
	 */
	private int unmapToInt(double x, double max, double min, int n) {
		return (int) ( (x - max) * ( Math.pow(2, n) - 1 ) / (max - min) );
	}

}
