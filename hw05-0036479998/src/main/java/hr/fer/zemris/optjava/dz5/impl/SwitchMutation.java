package hr.fer.zemris.optjava.dz5.impl;

import java.util.Random;

import hr.fer.zemris.optjava.dz5.model.IFunction;
import hr.fer.zemris.optjava.dz5.model.IMutation;

/**
 * Mutation with a switch chance.
 * 
 * @author lmark
 *
 */
public class SwitchMutation implements IMutation<IntArraySolution> {

	private double switchChance;
	private IFunction function;
	
	public SwitchMutation(double switchChance, IFunction function) {
		this.switchChance = switchChance;
		this.function = function;
	}
	
	@Override
	public void mutate(IntArraySolution single, Random random) {
		
		for (int i = 0; i < single.values.length; i++) {
			
			if (random.nextDouble() > switchChance) {
				continue;
			} 
			
			int randomIndex = random.nextInt(single.values.length);
			int c = single.values[i];
			single.values[i] = single.values[randomIndex];
			single.values[randomIndex] = c;
			
		}
	}

}
