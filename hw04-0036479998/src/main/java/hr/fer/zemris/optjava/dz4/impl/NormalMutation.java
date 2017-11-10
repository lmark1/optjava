package hr.fer.zemris.optjava.dz4.impl;

import java.util.Random;

import hr.fer.zemris.optjava.dz4.model.IMutation;

/**Class implements mutation based on normal distribution.
 * 
 * @author lmark
 *
 */
public class NormalMutation implements IMutation<DoubleArraySolution>{

	private double sigma;
	private double mutationChance;
	
	public NormalMutation(double sigma, double mutationChange) {
		this.sigma = sigma;
		this.mutationChance = mutationChange;
	}
	
	@Override
	public void mutate(DoubleArraySolution single, Random random) {
		for (int i = 0, maxLen = single.values.length; i < maxLen; i++) {
			
			if (mutationChance <= random.nextDouble()) {
				single.values[i] += random.nextGaussian()*sigma;
			}
		}
	}

}
