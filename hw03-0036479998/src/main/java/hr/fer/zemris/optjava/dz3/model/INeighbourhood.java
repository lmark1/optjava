package hr.fer.zemris.optjava.dz3.model;

/**
 * Interface used for generating random neighbours of type T.
 * 
 * @author lmark
 *
 * @param <T>
 */
public interface INeighbourhood<T> {

	/**
	 * Generate a random neighbour of type T.
	 * 
	 * @param root
	 * @return
	 */
	T randomNeighbour(T root);
}
