package hr.fer.zemris.optjava.dz3.model;

/**
 * Decoder interface used for decoding objects from T to 
 * double[].
 * 
 * @author lmark
 *
 * @param <T>
 */
public interface IDecoder<T> {

	/**
	 * Decode the solution and return a new field of doubles.
	 * 
	 * @param solution
	 * @return
	 */
	double[] decode(T solution);
	
	/**
	 * Decode the given solution into the T solution object.
	 * 
	 * @param solution
	 * @param decodedSolution
	 */
	void decode(T solution, double[] values);
}
