package hr.fer.zemris.optjava.dz5.part2;

import Jama.Matrix;

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
}