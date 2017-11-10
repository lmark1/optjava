package hr.fer.zemris.optjava.dz4.part1;

import java.util.ArrayList;
import java.util.List;

import Jama.Matrix;

/**
 * Utility class.
 * 
 * @author lmark
 *
 */
public class Util {

	/**
	 * Convert matrix to string.
	 * 
	 * @param mat
	 * @return String representation of matrix.
	 */
	public static String MatrixToString(Matrix mat) {
		StringBuffer b = new StringBuffer();
		for (int i = 0, iMax = mat.getRowDimension(); i < iMax; i++) {
			b.append("[");
			for (int j = 0, jMax = mat.getColumnDimension(); j < jMax; j++) {
				b.append(String.format("%.2f", mat.get(i, j)));
				
				if (j+1 < jMax) {
					b.append(", ");
				}
			}
			b.append("]");
			if (i+1 < iMax) {
				b.append("\n");
			}
		}
		
		return b.toString();
	}
	
	/**
	 * Check if all elements in matrix are close to zero.
	 * 
	 * @param mat
	 * @param toll
	 * @return
	 */
	public static boolean isZero(Matrix mat, double toll) {
		for (int i = 0, iMax = mat.getRowDimension(); i < iMax; i++) {
			for (int j = 0, jMax = mat.getColumnDimension(); j < jMax; j++) {
				if (Math.abs(mat.get(i, j))>=toll) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	/**
	 * Parse line of the following type:
	 * [double1, double2, ..., doublen]
	 * 
	 * @param line
	 * @return Return a list of doubles.
	 */
	public static List<Double> parseLine(String line) {
		String procLine = line.trim().substring(1, line.length()-1);
		String[] sNumbers = procLine.split(",");
		List<Double> doubles = new ArrayList<>();
		
		// Extract numbers
		for (int i = 0, len = sNumbers.length; i < len; i++) {
			double number = Double.valueOf(sNumbers[i].trim());
			
			doubles.add(number);
		}
		
		return doubles;
	}
}
